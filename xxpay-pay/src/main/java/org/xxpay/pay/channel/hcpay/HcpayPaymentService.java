package org.xxpay.pay.channel.hcpay;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.hcpay.model.QueryOrderResponse;
import org.xxpay.pay.channel.hcpay.util.XStreamUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 18/8/9
 * @description: 汇潮支付接口
 */
@Service
public class HcpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(HcpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_HCPAY;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {
        HcpayConfig hcpayConfig = new HcpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        String key = hcpayConfig.getKey();
        String url = hcpayConfig.getReqUrl();
        String mchId = hcpayConfig.getMchId();
        String payOrderId = payOrder.getPayOrderId();                                   // 商户订单号
        String amount = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");         // 金额
        String orderTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());   // 请求时间
        String returnUrl = payConfig.getReturnUrl(getChannelName());                    // 页面跳转同步通知页面
        String adviceURL = payConfig.getNotifyUrl(getChannelName());                    // 服务器异步通知路径

        // 支付参数
        Map<String, String> params = new HashMap<>();
        params.put("MerNo", mchId);                 // 商户id
        params.put("BillNo", payOrderId);           // 订单号
        params.put("Amount", amount);               // 金额
        params.put("ReturnURL", returnUrl);         // 页面跳转同步通知页面
        params.put("AdviceURL", adviceURL);         // 服务器异步通知路径
        params.put("OrderTime", orderTime);         // 请求时间
        params.put("defaultBankNumber", "");        // 银行编码
        params.put("payType", "");                  // 支付方式, B2CCredit：B2C信用卡 B2CDebit: B2C借记卡 noCard： 银联快捷支付  quickPay： 快捷支付 B2B：企业网银支付
        params.put("Remark", "");                   // 备注
        params.put("products", "");                 // 物品信息
        params.put("SignInfo", genSignInfo(params, key));   // 签名信息
        _log.info("[{}]下单请求参数:{}", getChannelName(), params);
        StringBuffer payForm = new StringBuffer();
        payForm.append("<form action=\""+url+"\" method=\"post\">");
        for(String k : params.keySet()) {
            payForm.append("<input type=\"text\" name=\""+k+"\" value=\""+params.get(k)+"\">");
        }
        payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
        payForm.append("</form>");
        payForm.append("<script>document.forms[0].submit();</script>");
        int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
        _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
        // 支付链接地址
        retObj.setPayOrderId(payOrder.getPayOrderId()); // 设置支付订单ID
        retObj.setJumpInfo(payForm.toString());

        return retObj;
    }

    /**
     * 生成MD5签名数据
     * @param params
     * @param key
     * @return
     */
    String genSignInfo(Map<String, String> params, String key) {
        StringBuffer sb = new StringBuffer();
        sb.append("MerNo=").append(params.get("MerNo"))
                .append("&BillNo=").append(params.get("BillNo"))
                .append("&Amount=").append(params.get("Amount"))
                .append("&OrderTime=").append(params.get("OrderTime"))
                .append("&ReturnURL=").append(params.get("ReturnURL"))
                .append("&AdviceURL=").append(params.get("AdviceURL"))
                .append("&").append(key);
        return MD5Util.string2MD5(sb.toString()).toUpperCase();
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        HcpayConfig hcpayConfig = new HcpayConfig(getPayParam(payOrder));
        String key = hcpayConfig.getKey();
        String queryUrl = hcpayConfig.getQueryUrl();
        SortedMap<String,String> map = new TreeMap();
        map.put("merCode", hcpayConfig.getMchId());         // 商户号
        map.put("orderNumber", payOrder.getPayOrderId());   // 商户订单号
        map.put("sign", MD5Util.string2MD5(hcpayConfig.getMchId() + "&" + key).toUpperCase()); // 签名
        map.put("tx", "1001");
        String requestDomain = MyBase64.encode(parseXML(map).getBytes());
        try {
            _log.info("{}请求数据:{}", getChannelName(), requestDomain);
            String result = call4Post(queryUrl + "?requestDomain=" + requestDomain);
            if(result != null){
                QueryOrderResponse queryOrderResponse = (QueryOrderResponse)XStreamUtil.xmlToBean(result);
                _log.info("{}请求结果:{}", getChannelName(), queryOrderResponse);
                String resultCode = queryOrderResponse.getResultCode();
                if(resultCode == null || !"00".equals(resultCode)){
                    return QueryRetMsg.unknown();

                }
                List<QueryOrderResponse.Order> list = queryOrderResponse.getList();
                if(CollectionUtils.isEmpty(list)) {
                    return QueryRetMsg.unknown();
                }
                QueryOrderResponse.Order order = list.get(0);
                String orderStatus = order.getOrderStatus();    // 订单状态
                if("1".equals(orderStatus)){
                    return QueryRetMsg.confirmSuccess(order.getOrderNumber());
                }else {
                    return QueryRetMsg.waiting();
                }
            }else{
                return QueryRetMsg.unknown();
            }
        } catch (Exception e) {
            _log.error(e, "");
            return QueryRetMsg.sysError();
        }
    }

    public static String parseXML(SortedMap<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><root tx=\"1001\">");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = (String)entry.getValue();
            if (null != v && !"".equals(v) && !"appkey".equals(k)) {
                sb.append("<" + k + ">" + parameters.get(k) + "</" + k + ">\n");
            }
        }
        sb.append("</root>");
        return sb.toString();
    }

    public static String call4Post(String url) {
        try {
            URL url1 = new URL(url);
            if("https".equals(url1.getProtocol())) {
                return HttpClient.callHttpsPost(url);
            }else if("http".equals(url1.getProtocol())) {
                return HttpClient.callHttpPost(url);
            }else {
                return "";
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return "";
    }


}
