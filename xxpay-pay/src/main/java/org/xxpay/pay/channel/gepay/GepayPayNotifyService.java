package org.xxpay.pay.channel.gepay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description: 个付通支付回调
 */
@Service
public class GepayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(GepayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return GepayConfig.CHANNEL_NAME;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理个付通支付回调】";
        _log.info("====== 开始处理个付通支付回调通知 ======");
        HttpServletRequest request = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        String respString = GepayConfig.RETURN_VALUE_FAIL;
        try {
            JSONObject params = new JSONObject();
            params.put("out_trade_no", request.getParameter("out_trade_no"));
            payContext.put("parameters", params);
            if(!verifyPayParams(payContext)) {
                retObj.put(PayConstant.RESPONSE_RESULT, GepayConfig.RETURN_VALUE_FAIL);
                return retObj;
            }
            payOrder = (PayOrder) payContext.get("payOrder");
            // 处理订单
            byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，3：业务处理完成，-2：订单过期
            String transaction_id = params.getString("transaction_id");
            if (payStatus != PayConstant.PAY_STATUS_SUCCESS) {
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
            _log.info("====== 完成处理个付通支付回调通知 ======");
            respString = GepayConfig.RETURN_VALUE_SUCCESS;
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
        JSONObject params = (JSONObject) payContext.get("parameters");

        //校验结果是否成功
        String status = params.getString("status");



        String out_trade_no = params.getString("out_trade_no");		// 商户订单号
        String total_amount = params.getString("total_fee"); 		    // 支付金额

        String errorMessage;
        // 查询payOrder记录
        String payOrderId = out_trade_no;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }



        // 核对金额
//        long outPayAmt = new BigDecimal(total_amount).longValue();
//        long dbPayAmt = payOrder.getAmount().longValue();
//        if (dbPayAmt != outPayAmt) {
//            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
//            payContext.put("retMsg", "");
//            return false;
//        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
