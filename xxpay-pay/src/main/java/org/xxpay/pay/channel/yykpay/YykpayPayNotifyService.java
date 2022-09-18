package org.xxpay.pay.channel.yykpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.yykpay.util.PaymentUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/08/20
 * @description: 易游酷支付通知
 */
@Service
public class YykpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(YykpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_YYKPAY;
    }

    /* 异步回调数据
        BillNo=W20180809171256913277635&ChannelNo&BankNo&MerNo=43255&Amount=500&SignInfo=F0F3AE690991B5CEF31AC7D135C5E40F&OrderNo=0175755453&Succeed=88&Result=SUCCESS
        这个是易游酷那边真实返回的异步POST参数
     */
    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理易游酷支付回调】";
        _log.info("====== 开始处理易游酷支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;

        String bizType = req.getParameter("bizType");                           // 业务类型
        String result = req.getParameter("result");                             // 支付结果
        String merchantNo = req.getParameter("merchantNo");                     // 商户编号
        String merchantOrderNo	 = req.getParameter("merchantOrderNo");         // 商户订单号
        String successAmount = req.getParameter("successAmount");               // 成功金额,单位元,精确到分
        String cardCode = req.getParameter("cardCode");                         // 支付方式
        String noticeType = req.getParameter("noticeType");                     // 通知类型
        String extInfo = req.getParameter("extInfo");                           // 返回提交的扩展信息
        String cardNo = req.getParameter("cardNo");                             // 卡序列号组
        String cardStatus = req.getParameter("cardStatus");                     // 卡状态组
        String cardReturnInfo = req.getParameter("cardReturnInfo");             // 卡处理描述
        String cardIsbalance = req.getParameter("cardIsbalance");               // 支付方式
        String cardBalance = req.getParameter("cardBalance");                   // 卡余额
        String cardSuccessAmount = req.getParameter("cardSuccessAmount");       // 卡成功金额
        String hmac = req.getParameter("hmac");                                 // 签名数据

        Map<String, String> params = new HashMap<>();
        params.put("bizType", bizType);
        params.put("result", result);
        params.put("merchantNo", merchantNo);
        params.put("merchantOrderNo", merchantOrderNo);
        params.put("successAmount", successAmount);
        params.put("cardCode", cardCode);
        params.put("noticeType", noticeType);
        params.put("extInfo", extInfo);
        params.put("cardNo", cardNo);
        params.put("cardStatus", cardStatus);
        params.put("cardReturnInfo", cardReturnInfo);
        params.put("cardIsbalance", cardIsbalance);
        params.put("cardBalance", cardBalance);
        params.put("cardSuccessAmount", cardSuccessAmount);
        params.put("hmac", hmac);

        _log.info("{}通知参数:{}", logPrefix, params);
        String respString = "处理失败";
        try {
            payContext.put("parameters", params);
            if(!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, "验证数据没有通过");
                return retObj;
            }
            payOrder = (PayOrder) payContext.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                if("SUCCESS".equals(result)) {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), null);
                    if (updatePayOrderRows != 1) {
                        _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                        retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                        return retObj;
                    }
                    _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                }else {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrder.getPayOrderId());
                    if (updatePayOrderRows != 1) {
                        _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_FAILED);
                        retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                        return retObj;
                    }
                    _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_FAILED);
                    payOrder.setStatus(PayConstant.PAY_STATUS_FAILED);
                }
            }
            // 业务系统后端通知
            baseNotify4MchPay.doNotify(payOrder, true);
            _log.info("====== 完成处理易游酷支付回调通知 ======");
            respString = PayConstant.RETURN_YYKPAY_VALUE_SUCCESS;
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;

    }

    /**
     * 验证支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext) {
        Map<String, String> params = (Map<String,String>)payContext.get("parameters");
        String merchantOrderNo = params.get("merchantOrderNo");		// 商户订单号
        if (StringUtils.isEmpty(merchantOrderNo)) {
            _log.error("Notify parameter merchantOrderNo is empty. out_trade_no={}", merchantOrderNo);
            payContext.put("retMsg", "BillNo is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = merchantOrderNo;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        YykpayConfig yykpayConfig = new YykpayConfig(getPayParam(payOrder));
        //
        String sign = PaymentUtil.buildNotifyHmac(params.get("bizType"), params.get("result"), params.get("merchantNo"),
                params.get("merchantOrderNo"), params.get("successAmount"), params.get("cardCode"), params.get("noticeType"),
                params.get("extInfo"), params.get("cardNo"), params.get("cardStatus"), params.get("cardReturnInfo"),
                params.get("cardIsbalance"), params.get("cardBalance"), params.get("cardSuccessAmount"), yykpayConfig.getKey());

        String hmac = params.get("hmac");
        // 验证签名
        if (!sign.equals(hmac)) {
            errorMessage = "check sign failed.";
            _log.error("Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        payContext.put("payOrder", payOrder);
        return true;
    }

}
