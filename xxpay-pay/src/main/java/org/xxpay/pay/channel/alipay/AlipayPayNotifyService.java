package org.xxpay.pay.channel.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class AlipayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(AlipayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理支付宝支付回调】";
        _log.info("====== 开始处理支付宝支付回调通知 ======");
        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);
        if(!verifyAliPayParams(payContext)) {
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
            return retObj;
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);
        String trade_status = params.get("trade_status").toString();		// 交易状态
        // 支付状态成功或者完成
        if (trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_SUCCESS) ||
                trade_status.equals(PayConstant.AlipayConstant.TRADE_STATUS_FINISHED)) {
            int updatePayOrderRows;
            payOrder = (PayOrder)payContext.get("payOrder");
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
            if (payStatus == PayConstant.PAY_STATUS_PAYING) { //仅判断支付中， 当前可能为已退款状态
                updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), StrUtil.toString(params.get("trade_no"), null));
                if (updatePayOrderRows != 1) {
                    _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    _log.info("{}响应给支付宝结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    retObj.put("resResult", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                    return retObj;
                }
                _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            }
        }else{
            // 其他状态
            _log.info("{}支付状态trade_status={},不做业务处理", logPrefix, trade_status);
            _log.info("{}响应给支付宝结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
            return retObj;
        }
        baseNotify4MchPay.doNotify(payOrder, true);
        _log.info("====== 完成处理支付宝支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
        return retObj;
    }

    @Override
    public JSONObject doReturn(Object notifyData) {
        String logPrefix = "【处理支付宝同步跳转】";
        _log.info("====== 开始处理支付宝同步跳转 ======");

        Map params = null;
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
        }
        _log.info("{}请求数据:{}", logPrefix, params);

        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空");
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();

        payContext.put("parameters", params);
        if(!verifyAliPayParams(payContext)) {
            retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
            return retObj;
        }
        _log.info("{}验证支付通知数据及签名通过", logPrefix);

        PayOrder payOrder = (PayOrder)payContext.get("payOrder");

        _log.info("====== 完成处理支付宝同步跳转 ======");
        String url = baseNotify4MchPay.createNotifyUrl(payOrder, "1");
        retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_ALIPAY_VALUE_SUCCESS);
        retObj.put(PayConstant.JUMP_URL, url);
        return retObj;
    }

    /**
     * 解析支付宝回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 验证支付宝支付通知参数
     * @return
     */
    public boolean verifyAliPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        String out_trade_no = params.get("out_trade_no");		// 商户订单号
        String total_amount = params.get("total_amount"); 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(out_trade_no)) {
            _log.error("AliPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("AliPay Notify parameter total_amount is empty. total_fee={}", total_amount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
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
        boolean verify_result = false;
        try {
            AlipayConfig alipayConfig = new AlipayConfig(getMainPayParam(payOrder));

            if("cert".equals(alipayConfig.getEncryptionType())){  //证书方式
                String alipayPublicCertPath = payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAlipayPublicCert();
                verify_result = AlipaySignature.rsaCertCheckV1(params, alipayPublicCertPath, AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);

            }else{
                verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getAlipayPublicKey(), AlipayConfig.CHARSET, AlipayConfig.SIGNTYPE);
            }



        } catch (AlipayApiException e) {
            _log.error(e, "AlipaySignature.rsaCheckV1 error");
        }

        // 验证签名
        if (!verify_result) {
            errorMessage = "rsaCheckV1 failed.";
            _log.error("AliPay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long aliPayAmt = new BigDecimal(total_amount).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }


}
