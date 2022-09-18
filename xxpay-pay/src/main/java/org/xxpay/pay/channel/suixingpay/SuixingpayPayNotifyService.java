package org.xxpay.pay.channel.suixingpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;
import org.xxpay.pay.channel.suixingpay.utils.RSASignature;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class SuixingpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(SuixingpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SUIXINGPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {

        String logPrefix = "【处理随行付支付回调】";
        _log.info("====== 开始处理随行付支付回调通知 ======");
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

        int updatePayOrderRows;
        payOrder = (PayOrder)payContext.get("payOrder");
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
        if (payStatus == PayConstant.PAY_STATUS_PAYING) { //仅判断支付中， 当前可能为已退款状态
            updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), StrUtil.toString(params.get("trade_no"), null));
            if (updatePayOrderRows != 1) {
                _log.error("{}更新支付状态失败,将payOrderId={},更新payStatus={}失败", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
                _log.info("{}响应给随行付结果：{}", logPrefix, PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                retObj.put("resResult", PayConstant.RETURN_ALIPAY_VALUE_FAIL);
                return retObj;
            }
            _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
            payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        }

        baseNotify4MchPay.doNotify(payOrder, true);
        _log.info("====== 完成处理随行付支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, "{\"code\":\"success\",\"msg\":\"成功\"}");
        return retObj;
    }

    /**
     * 解析随行付回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {

        String reqText = "";
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }
            br.close();
            reqText = sb.toString();

        } catch (Exception e) {
            _log.error("", e);
        }

        return JSON.parseObject(reqText);
    }

    /**
     * 验证随行付支付通知参数
     * @return
     */
    public boolean verifyAliPayParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");

        String ordNo = params.get("ordNo");		// 商户订单号
        String amt = params.get("amt"); 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(ordNo)) {
            _log.error("Suixingpay Notify parameter out_trade_no is empty. ordNo={}", ordNo);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(amt)) {
            _log.error("Suixingpay Notify parameter total_amount is empty. amt={}", amt);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = ordNo;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }


        // 核对金额
        long aliPayAmt = new BigDecimal(amt).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != aliPayAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. amt={},payOrderId={}", amt, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }

        String signResult = params.get("sign"); // 签名参数
        params.remove("sign");  //剔除sign字段

        JSONObject mainParam = getMainPayParam(payOrder);


        //验签
        boolean isTrue = RSASignature.doCheck(RSASignature.getOrderContent(params), signResult, mainParam.getString("sxfPublic"));
        if(!isTrue){ //验签失败

            _log.error("sign error ");
            payContext.put("retMsg", "sign error");
            return false;
        }

        payContext.put("payOrder", payOrder);
        return true;
    }


}
