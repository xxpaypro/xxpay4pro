package org.xxpay.pay.channel.swiftpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.swiftpay.util.SignUtils;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;
import org.xxpay.pay.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class SwiftpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(SwiftpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SWIFTPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理威富通支付回调】";
        _log.info("====== 开始处理威富通支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = PayConstant.RETURN_SWIFTPAY_VALUE_FAIL;
        try {
            req.setCharacterEncoding("utf-8");
            String resString = XmlUtils.parseRequst(req);
            System.out.println("通知内容：" + resString);
            if(resString != null && !"".equals(resString)){
                Map<String,String> params = XmlUtils.toMap(resString.getBytes(), "utf-8");
                payContext.put("parameters", params);
                if(!verifyPayParams(payContext)) {
                    retObj.put(PayConstant.RESPONSE_RESULT, PayConstant.RETURN_SWIFTPAY_VALUE_FAIL);
                    return retObj;
                }
                payOrder = (PayOrder) payContext.get("payOrder");
                // 处理订单
                byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
                String transaction_id = params.get("transaction_id");
                if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transaction_id, Util.buildSwiftpayAttach(params));
                    if (updatePayOrderRows != 1) {
                        _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                        retObj.put(PayConstant.RESPONSE_RESULT, "处理订单失败");
                        return retObj;
                    }
                    _log.error("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                    payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
                }
                // 业务系统后端通知
                baseNotify4MchPay.doNotify(payOrder, true);
                _log.info("====== 完成处理威富通支付回调通知 ======");
                respString = PayConstant.RETURN_SWIFTPAY_VALUE_SUCCESS;
            }
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;

    }

    /**
     * 验证威富通支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");

        //校验结果是否成功
        String status = params.get("status");
        String result_code = params.get("result_code");
        String pay_result = params.get("pay_result");
        if(status == null || !"0".equals(status) ||
                result_code == null || !"0".equals(result_code) ||
                pay_result == null || !"0".equals(pay_result)) {
            _log.error("status={},result_code={},err_code={},err_msg={},pay_result={},pay_info={}",status, result_code, params.get("err_code"),
                    params.get("err_msg"), pay_result, params.get("pay_info"));
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        String out_trade_no = params.get("out_trade_no");		// 商户订单号
        String total_amount = params.get("total_fee"); 		    // 支付金额
        if (org.springframework.util.StringUtils.isEmpty(out_trade_no)) {
            _log.error("AliPay Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("AliPay Notify parameter total_amount is empty. total_amount={}", total_amount);
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

        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        // 验证签名
        if (!SignUtils.checkParam(params, swiftpayConfig.getKey())) {
            errorMessage = "check sign failed.";
            _log.error("Swiftpay Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(total_amount).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
