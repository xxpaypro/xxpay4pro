package org.xxpay.pay.channel.wxpay;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description: 微信支付通知处理
 */
@Service
public class WxpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(WxpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_WXPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理微信支付回调】";
        _log.info("====== 开始处理微信支付回调通知 ======");
        String xmlResult = null;
        if(notifyData instanceof String) {
            xmlResult = (String) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) notifyData;
            try {
                xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
            } catch (IOException e) {
                _log.error(e, "");
            }
        }
        _log.info("{}请求数据:{}", logPrefix, xmlResult);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(StringUtils.isEmpty(xmlResult)) {
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail("请求数据为空"));
            return retObj;
        }
        try {
            WxPayService wxPayService = new WxPayServiceImpl();
            WxPayOrderNotifyResult result = WxPayOrderNotifyResult.fromXML(xmlResult);
            Map<String, Object> payContext = new HashMap();
            payContext.put("parameters", result);
            // 验证业务数据是否正确,验证通过后返回PayOrder和WxPayConfig对象
            if(!verifyWxPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail((String) payContext.get("retMsg")));
                return retObj;
            }
            PayOrder payOrder = (PayOrder) payContext.get("payOrder");
            WxPayConfig wxPayConfig = (WxPayConfig) payContext.get("wxPayConfig");
            wxPayService.setConfig(wxPayConfig);
            // 这里做了签名校验(这里又做了一次xml转换对象,可以考虑优化)
            wxPayService.parseOrderNotifyResult(xmlResult);
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), result.getTransactionId());
                if (updatePayOrderRows != 1) {
                    _log.info("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail("处理订单失败"));
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理微信支付回调通知 ======");
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.success("OK"));
            return retObj;
        } catch (WxPayException e) {
            //出现业务错误
            _log.error(e, "微信回调结果异常,异常原因");
            _log.info("{}请求数据result_code=FAIL", logPrefix);
            _log.info("err_code:", e.getErrCode());
            _log.info("err_code_des:", e.getErrCodeDes());
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail(e.getMessage()));
            return retObj;
        } catch (Exception e) {
            _log.error(e, "微信回调结果异常,异常原因");
            retObj.put(PayConstant.RESPONSE_RESULT, WxPayNotifyResponse.fail(e.getMessage()));
            return retObj;
        }
    }

    /**
     * 验证微信支付通知参数
     * @return
     */
    public boolean verifyWxPayParams(Map<String, Object> payContext) {
        WxPayOrderNotifyResult params = (WxPayOrderNotifyResult)payContext.get("parameters");

        //校验结果是否成功
        if (!PayConstant.RETURN_VALUE_SUCCESS.equalsIgnoreCase(params.getResultCode())
                || !PayConstant.RETURN_VALUE_SUCCESS.equalsIgnoreCase(params.getResultCode())) {
            _log.error("returnCode={},resultCode={},errCode={},errCodeDes={}", params.getReturnCode(), params.getResultCode(), params.getErrCode(), params.getErrCodeDes());
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        Integer total_fee = params.getTotalFee();   			// 总金额
        String out_trade_no = params.getOutTradeNo();			// 商户系统订单号

        // 查询payOrder记录
        String payOrderId = out_trade_no;

        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder==null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        // 查询mchChannel记录
        /*Long mchId = payOrder.getMchId();
        String channelId = payOrder.getChannelId();
        String appId = payOrder.getAppId();
        MchChannel mchChannel = rpcCommonService.rpcMchChannelService.findByMACId(mchId, appId, channelId);
        if(mchChannel == null) {
            _log.error("Can't found mchChannel form db. mchId={} channelId={}, ", payOrderId, mchId, channelId);
            payContext.put("retMsg", "Can't found mchChannel");
            return false;
        }*/

        payContext.put("wxPayConfig", WxPayUtil.getWxPayConfig(getMainPayParam(payOrder)));

        // 核对金额
        long wxPayAmt = new BigDecimal(total_fee).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != wxPayAmt) {
            _log.error("db payOrder record payPrice not equals total_fee. total_fee={},payOrderId={}", total_fee, payOrderId);
            payContext.put("retMsg", "total_fee is not the same");
            return false;
        }

        payContext.put("payOrder", payOrder);
        return true;
    }

}
