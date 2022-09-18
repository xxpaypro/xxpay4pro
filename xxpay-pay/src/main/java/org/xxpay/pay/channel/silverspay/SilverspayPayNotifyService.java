package org.xxpay.pay.channel.silverspay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/6/29
 * @description: 睿联支付通知
 */
@Service
public class SilverspayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(SilverspayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SILVERSPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理睿联支付回调】";
        _log.info("====== 开始处理睿联支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;

        String memberid = req.getParameter("memberid");                 //商户编号
        String orderid = req.getParameter("orderid");                   //订单号
        String amount = req.getParameter("amount");                     //订单金额
        String transactionId = req.getParameter("transaction_id");      //交易流水号
        String datetime = req.getParameter("datetime");                 //交易时间
        String returncode = req.getParameter("returncode");             //交易状态
        String sign = req.getParameter("sign");                         //交易签名

        Map<String, String> params = new HashMap<>();
        params.put("memberid", memberid);
        params.put("orderid", orderid);
        params.put("amount", amount);
        params.put("transaction_id", transactionId);
        params.put("datetime", datetime);
        params.put("returncode", returncode);
        params.put("sign", sign);
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
            String transaction_id = params.get("transaction_id");
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transaction_id);
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
            _log.info("====== 完成处理睿联支付回调通知 ======");
            respString = PayConstant.RETURN_SILVERSPAY_VALUE_SUCCESS;
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

        //校验结果是否成功
        String returncode = params.get("returncode");
        if(!"00".equals(returncode)) {
            _log.error("returncode={}", returncode);
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        String out_trade_no = params.get("orderid");		// 商户订单号
        String total_amount = params.get("amount"); 		    // 支付金额
        if (StringUtils.isEmpty(out_trade_no)) {
            _log.error("Notify parameter out_trade_no is empty. out_trade_no={}", out_trade_no);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (StringUtils.isEmpty(total_amount)) {
            _log.error("Notify parameter total_amount is empty. total_amount={}", total_amount);
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

        SilverspayConfig silverspayConfig = new SilverspayConfig(getPayParam(payOrder));
        // 验证签名
        try {
            if (!Util.isSignatureValid(params, silverspayConfig.getKey())) {
                errorMessage = "check sign failed.";
                _log.error("Notify parameter {}", errorMessage);
                payContext.put("retMsg", errorMessage);
                return false;
            }
        } catch (Exception e) {
            _log.error(e, "");
        }

        // 核对金额
        long outPayAmt = new BigDecimal(total_amount).multiply(new BigDecimal(100)).longValue();
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
