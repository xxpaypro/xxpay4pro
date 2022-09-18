package org.xxpay.pay.channel.hcpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MD5Util;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/08/09
 * @description: 汇潮支付通知
 */
@Service
public class HcpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(HcpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_HCPAY;
    }

    /* 异步回调数据
        BillNo=W20180809171256913277635&ChannelNo&BankNo&MerNo=43255&Amount=500&SignInfo=F0F3AE690991B5CEF31AC7D135C5E40F&OrderNo=0175755453&Succeed=88&Result=SUCCESS
        这个是汇潮那边真实返回的异步POST参数
     */
    @Override
    public JSONObject doNotify(Object notifyData) {
        String logPrefix = "【处理汇潮支付回调】";
        _log.info("====== 开始处理汇潮支付回调通知 ======");
        HttpServletRequest req = (HttpServletRequest) notifyData;
        JSONObject retObj = buildRetObj();
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;

        String MerNo = req.getParameter("MerNo");             // 商户号
        String BillNo = req.getParameter("BillNo");           // 订单号
        String OrderNo = req.getParameter("OrderNo");         // 支付订单号
        String Amount	 = req.getParameter("Amount");        // 金额
        String Succeed = req.getParameter("Succeed");         // 状态码
        String Result = req.getParameter("Result");           // 订单详情
        String SignInfo = req.getParameter("SignInfo");       // 签名信息

        Map<String, String> params = new HashMap<>();
        params.put("MerNo", MerNo);
        params.put("BillNo", BillNo);
        params.put("OrderNo", OrderNo);
        params.put("Amount", Amount);
        params.put("Succeed", Succeed);
        params.put("Result", Result);
        params.put("SignInfo", SignInfo);
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
                int updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), OrderNo);
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
            _log.info("====== 完成处理汇潮支付回调通知 ======");
            respString = PayConstant.RETURN_HCPAY_VALUE_SUCCESS;
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
        String Succeed = params.get("Succeed");
        if(!"88".equals(Succeed)) {
            _log.error("Succeed={}", Succeed);
            payContext.put("retMsg", "notify data failed");
            return false;
        }

        String BillNo = params.get("BillNo");		// 商户订单号
        String Amount = params.get("Amount"); 		// 支付金额
        if (StringUtils.isEmpty(BillNo)) {
            _log.error("Notify parameter BillNo is empty. out_trade_no={}", BillNo);
            payContext.put("retMsg", "BillNo is empty");
            return false;
        }
        if (StringUtils.isEmpty(Amount)) {
            _log.error("Notify parameter Amount is empty. Amount={}", Amount);
            payContext.put("retMsg", "Amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = BillNo;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }

        HcpayConfig hcpayConfig = new HcpayConfig(getPayParam(payOrder));


        //Md5(MerNo=商户编号&BillNo=商户订单号&OrderNo=三方订单号&Amount=支付金额&Succeed=支付状态&商户密钥)
        StringBuffer sb = new StringBuffer();
        sb.append("MerNo=").append(params.get("MerNo"))
                .append("&BillNo=").append(params.get("BillNo"))
                .append("&OrderNo=").append(params.get("OrderNo"))
                .append("&Amount=").append(params.get("Amount"))
                .append("&Succeed=").append(params.get("Succeed"))
                .append("&").append(hcpayConfig.getKey());
        String sign = MD5Util.string2MD5(sb.toString()).toUpperCase();
        // 验证签名
        if (!sign.equals(params.get("SignInfo"))) {
            errorMessage = "check sign failed.";
            _log.error("Notify parameter {}", errorMessage);
            payContext.put("retMsg", errorMessage);
            return false;
        }

        // 核对金额
        long outPayAmt = new BigDecimal(Amount).multiply(new BigDecimal(100)).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != outPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. Amount={},payOrderId={}", Amount, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }
        payContext.put("payOrder", payOrder);
        return true;
    }

}
