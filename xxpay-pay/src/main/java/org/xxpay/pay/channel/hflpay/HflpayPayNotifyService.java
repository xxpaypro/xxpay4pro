package org.xxpay.pay.channel.hflpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.hflpay.util.MD5;
import org.xxpay.pay.channel.hflpay.util.Signature;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhuxiao
 * @date: 18/11/14
 * @description:
 */
@Service
public class HflpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(HflpayPayNotifyService.class);

    @Override
    public String getChannelName() { return HflpayConfig.CHANNEL_NAME_HFLPAY; }

    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理惠付拉支付回调】";
        _log.info("====== 开始处理惠付拉支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, String> params = new HashMap<>();

        params.put("out_trade_no",req.getParameter("out_trade_no"));
        params.put("cope_pay_amount",req.getParameter("cope_pay_amount"));
        params.put("pay_type",req.getParameter("pay_type"));
        params.put("state",req.getParameter("state"));
        params.put("merchant_order_number",req.getParameter("merchant_order_number"));
        params.put("timestamp",req.getParameter("timestamp"));
        params.put("sign",req.getParameter("sign"));
        PayOrder payOrder;
        String respString = HflpayConfig.RETURN_VALUE_FAIL;
        try {
            System.out.println("通知内容：" + params.toString());
            if(params != null && !"".equals(params.toString())){
                if(!verifyPayParams(params)) {
                    _log.error("{}参数验证失败");
                    retObj.put(PayConstant.RESPONSE_RESULT, HflpayConfig.RETURN_VALUE_FAIL);
                    return retObj;
                }
                payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(params.get("merchant_order_number"));
                if(payOrder == null){
                    _log.error("{}订单不存在",params.get("out_trade_no") );
                    retObj.put(PayConstant.RESPONSE_RESULT, HflpayConfig.RETURN_VALUE_FAIL);
                    return retObj;
                }
                // 处理订单
                byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
                if (payStatus != PayConstant.PAY_STATUS_SUCCESS ) {
                    int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId());
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
                _log.info("====== 完成处理惠付拉支付回调通知 ======");
                respString = HflpayConfig.RETURN_VALUE_SUCCESS;
            }
        } catch (Exception e) {
            _log.error(e, logPrefix + "处理异常");
        }
        retObj.put(PayConstant.RESPONSE_RESULT, respString);
        return retObj;

    }

    /**
     * 验证惠付拉支付通知参数
     * @return
     */
    public boolean verifyPayParams(Map<String, String> params) {
        //校验结果是否成功
        String status = params.get("state");
        if(status == null || !"0".equals(status)) {
            _log.error("status={}",status);
            return false;
        }

        String sign = params.get("sign");
        String merchant_order_number = params.get("merchant_order_number");		        // 商户订单号
        String total_amount = params.get("cope_pay_amount"); 		    // 支付金额
        if (org.springframework.util.StringUtils.isEmpty(merchant_order_number)) {
            _log.error("HflPay Notify parameter merchant_order_number is empty. merchant_order_number={}", merchant_order_number);
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(total_amount)) {
            _log.error("HflPay Notify parameter total_amount is empty. total_amount={}", total_amount);
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = merchant_order_number;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            return false;
        }

        HflpayConfig hflpayConfig = new HflpayConfig(getPayParam(payOrder));
        // 验证签名
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no",params.get("out_trade_no"));
        map.put("cope_pay_amount",params.get("cope_pay_amount"));
        map.put("pay_type",params.get("pay_type"));
        map.put("state",params.get("state"));
        map.put("merchant_order_number",params.get("merchant_order_number"));
        map.put("timestamp",params.get("timestamp"));
        String key = hflpayConfig.getKey();
        String parm = Signature.getSignContent(map);
        String code = MD5.code(parm+key);
        try {
            if (!code.equals(sign)) {
                errorMessage = "check sign failed.";
                _log.error("Hflpaypay Notify parameter {}", errorMessage);
                return false;
            }
        } catch (Exception e) {
            errorMessage = "check sign failed.";
            _log.error("Hflpaypay Notify parameter {}", errorMessage);
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(total_amount).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. total_amount={},payOrderId={}", total_amount, payOrderId);
            return false;
        }
        return true;
    }

}
