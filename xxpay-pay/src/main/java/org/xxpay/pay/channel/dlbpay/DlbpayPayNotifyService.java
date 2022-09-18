package org.xxpay.pay.channel.dlbpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayNotify;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class DlbpayPayNotifyService extends BasePayNotify {

    private static final MyLog _log = MyLog.getLog(DlbpayPayNotifyService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_DLBPAY;
    }

    @Override
    public JSONObject doNotify(Object notifyData) {

        String logPrefix = "【处理哆啦宝支付回调】";
        _log.info("====== 开始处理哆啦宝支付回调通知 ======");
        Map params = null;
        Map<String, String> headerMap = new HashMap<>();
        if(notifyData instanceof Map) {
            params  = (HashMap) notifyData;
        }else if(notifyData instanceof HttpServletRequest) {
            params = buildNotifyData((HttpServletRequest) notifyData);
            headerMap.put("timestamp", ( (HttpServletRequest)notifyData ).getHeader("timestamp"));
            headerMap.put("token", ( (HttpServletRequest)notifyData ).getHeader("token"));
        }
        _log.info("{}请求数据:{}", logPrefix, params);
        // 构建返回对象
        JSONObject retObj = buildRetObj();
        if(params == null || params.isEmpty()) {
            retObj.put(PayConstant.RESPONSE_RESULT, "请求数据为空！");  // 哆啦宝只回调一次！！！ 无论响应码和内容，   除非请求超时！！
            return retObj;
        }
        Map<String, Object> payContext = new HashMap();
        PayOrder payOrder;
        payContext.put("parameters", params);
        payContext.put("headerMap", headerMap);
        if(!verifyParams(payContext)) {
            retObj.put(PayConstant.RESPONSE_RESULT, "参数有误或验签失败！");  // 哆啦宝只回调一次！！！ 无论响应码和内容，   除非请求超时！！
            return retObj;
        }

        _log.info("{}验证支付通知数据及签名通过", logPrefix);

        int updatePayOrderRows;
        payOrder = (PayOrder)payContext.get("payOrder");
        byte payStatus = payOrder.getStatus(); // 0：订单生成，1：支付中，-1：支付失败，2：支付成功，-2：订单过期
        if (payStatus == PayConstant.PAY_STATUS_PAYING) { //仅判断支付中， 当前可能为已退款状态
            updatePayOrderRows = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), StrUtil.toString(params.get("trade_no"), null));
            if (updatePayOrderRows != 1) {
                retObj.put(PayConstant.RESPONSE_RESULT, "更新数据失败！");  // 哆啦宝只回调一次！！！ 无论响应码和内容，   除非请求超时！！
                return retObj;
            }
            _log.info("{}更新支付状态成功,将payOrderId={},更新payStatus={}成功", logPrefix, payOrder.getPayOrderId(), PayConstant.PAY_STATUS_SUCCESS);
            payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
        }

        baseNotify4MchPay.doNotify(payOrder, true);
        _log.info("====== 完成处理哆啦宝支付回调通知 ======");
        retObj.put(PayConstant.RESPONSE_RESULT, "{\"code\":\"success\",\"msg\":\"成功\"}");
        return retObj;
    }

    /**
     * 解析哆啦宝回调请求的数据
     * @param request
     * @return
     */
    public Map buildNotifyData(HttpServletRequest request) {

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
     * 验证哆啦宝支付通知参数
     * @return
     */
    private boolean verifyParams(Map<String, Object> payContext) {
        Map<String,String> params = (Map<String,String>)payContext.get("parameters");
        Map<String,String> headerMap = (Map<String,String>)payContext.get("headerMap");

        String requestNum = params.get("requestNum");		// 商户订单号
        String orderAmount = params.get("orderAmount"); 		// 支付金额
        if (org.springframework.util.StringUtils.isEmpty(requestNum)) {
            _log.error("Dlbpay Notify parameter out_trade_no is empty. requestNum={}", requestNum);
            payContext.put("retMsg", "out_trade_no is empty");
            return false;
        }
        if (org.springframework.util.StringUtils.isEmpty(orderAmount)) {
            _log.error("Dlbpay Notify parameter total_amount is orderAmount. amt={}", orderAmount);
            payContext.put("retMsg", "total_amount is empty");
            return false;
        }
        String errorMessage;
        // 查询payOrder记录
        String payOrderId = requestNum;
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(payOrderId);
        if (payOrder == null) {
            _log.error("Can't found payOrder form db. payOrderId={}, ", payOrderId);
            payContext.put("retMsg", "Can't found payOrder");
            return false;
        }


        // 核对金额
        long channelAmt = new BigDecimal(orderAmount).movePointRight(2).longValue();
        long dbPayAmt = payOrder.getAmount().longValue();
        if (dbPayAmt != channelAmt) {
            _log.error("db payOrder record payPrice not equals total_amount. amt={},payOrderId={}", channelAmt, payOrderId);
            payContext.put("retMsg", "");
            return false;
        }

        JSONObject mainParam = getMainPayParam(payOrder);

        //验签
        String sign = DlbHttpUtils.getSignStrByNotify(mainParam.getString("secretKey"), headerMap.get("timestamp"));
        if(StringUtils.isEmpty(sign) || !sign.equals(headerMap.get("token"))){
            _log.error("sign error ");
            payContext.put("retMsg", "sign error");
            return false;
        }

        payContext.put("payOrder", payOrder);
        return true;
    }


}
