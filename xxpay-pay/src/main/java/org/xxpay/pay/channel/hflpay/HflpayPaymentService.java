package org.xxpay.pay.channel.hflpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.hflpay.util.HuifulaUtil;
import org.xxpay.pay.channel.hflpay.util.MD5;
import org.xxpay.pay.channel.hflpay.util.Signature;

import java.util.*;

/**
 * @author: zhuxiao
 * @date: 18/11/14
 * @description: 惠付拉支付接口
 */
@Service
public class HflpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(HflpayPaymentService.class);

    @Override
    public String getChannelName() { return HflpayConfig.CHANNEL_NAME_HFLPAY; }



    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        switch (channelId) {
            case HflpayConfig.PAY_CHANNEL_HFL_ALIPAY_WAP :
                return doAlipayNativeReq(payOrder);
            default:
                return ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
        }
    }

    /**
     * 支付宝扫码支付
     * @param payOrder
     * @return
     */
    public AbstractRes doAlipayNativeReq(PayOrder payOrder) {
        HflpayConfig hflpayConfig = new HflpayConfig(getPayParam(payOrder));
        String  cope_pay_amount = String.valueOf(payOrder.getAmount());
        String  merchant_open_id = hflpayConfig.getMchId();
        String  merchant_order_number = payOrder.getPayOrderId();
        String  notify_url = payConfig.getNotifyUrl(getChannelName());
        String  subject = payOrder.getSubject();
        String  pay_wap_mark = "pay004";

        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        JSONObject params = new JSONObject();
        // 应付金额,单位分
        params.put("cope_pay_amount", cope_pay_amount);
        //商户ID
        params.put("merchant_open_id",merchant_open_id);
        // 商户订单号
        params.put("merchant_order_number", merchant_order_number);
        // 通知地址
        params.put("notify_url",notify_url);
        // 支付方式
        params.put("pay_type", 1);
        //当前时间戳
        String date = new Date().getTime() + "";
        //订单名称
        params.put("subject", subject);
        params.put("timestamp", date);
        // 商户支付通道标示
        params.put("pay_wap_mark", pay_wap_mark);

        Map<String,String> map= new HashMap<>();
        map.put("merchant_order_number", merchant_order_number);
        map.put("pay_type", "1");
        map.put("cope_pay_amount", cope_pay_amount);
        map.put("pay_wap_mark", "pay004");
        map.put("timestamp", date);
        map.put("subject", subject);
        map.put("notify_url", notify_url);
        map.put("merchant_open_id", merchant_open_id);
        String key = hflpayConfig.getKey();
        String reqUrl = hflpayConfig.getReqUrl();
        if (reqUrl.endsWith("/")){
            reqUrl = reqUrl + "add";
        }else{
            reqUrl = reqUrl + "/add";
        }
        String parm = Signature.getSignContent(map);
        // 生成sign
        String sign = MD5.code(parm + key);
        //String sign = Signature.generateMD5Sign(map,key);
        params.put("reqUrl",reqUrl);
        params.put("sign", sign);
        try {
            _log.info("惠付拉请求数据:{}", parm);
            JSONObject json = HuifulaUtil.createZhiFuUtil(params);
            _log.info("惠付拉请求结果:{}", json.toJSONString());

            StringBuffer payForm = new StringBuffer();
            payForm.append("<form action=\""+json.get("pay_url")+"\" method=\"post\">");
            for(String k : map.keySet()) {
                payForm.append("<input type=\"text\" name=\""+k+"\" value=\""+map.get(k)+"\">");
            }
            payForm.append("<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >");
            payForm.append("</form>");
            payForm.append("<script>document.forms[0].submit();</script>");
            System.out.println((String) json.get("out_trade_no"));
            int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), (String) json.get("out_trade_no"));
            _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(),json.get("out_trade_no"), "", result);
            retObj.setJumpInfo(payForm.toString(), json.getString("pay_url"));
            return retObj;
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("操作失败!");
        }
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        HflpayConfig hflpayConfig = new HflpayConfig(getPayParam(payOrder));
        JSONObject retObj = new JSONObject();
        String key = hflpayConfig.getKey();
        String queryUrl = hflpayConfig.getReqUrl();
        if (queryUrl.endsWith("/")){
            queryUrl = queryUrl + "query";
        }else{
            queryUrl = queryUrl + "/query";
        }
        String date = System.currentTimeMillis() + "";          // 当前时间戳
        String mchId = hflpayConfig.getMchId();                 // 商户号
        String out_trade_no = payOrder.getChannelOrderNo();           // 商户订单号

        SortedMap<String,String> map = new TreeMap();
        map.put("merchant_open_id", mchId);
        map.put("out_trade_no", out_trade_no);
        map.put("timestamp",date);
        String parm = Signature.getSignContent(map);

        JSONObject params = new JSONObject();
        params.put("merchant_open_id", mchId);
        params.put("out_trade_no", out_trade_no);
        params.put("timestamp",date);
        params.put("sign",MD5.code(parm + key));
        params.put("queryUrl",queryUrl);
        try {
            _log.info("惠付拉请求数据:{}", parm);
            JSONObject json = HuifulaUtil.queryUtil(params);
            if(json != null){
                _log.info("{}请求结果:{}", json.toJSONString());
                String mark = json.getString("mark");
                if(mark == null || !"0".equals(mark)){
                    return QueryRetMsg.unknown();
                }
                Map<String,String> map1 = new HashMap<>();
                map1.put("mark",json.getString("mark"));
                map1.put("tip",json.getString("tip"));
                map1.put("account_amount",json.getString("account_amount"));
                map1.put("cope_pay_amount",json.getString("cope_pay_amount"));
                map1.put("merchant_open_id",json.getString("merchant_open_id"));
                map1.put("merchant_order_number",json.getString("merchant_order_number"));
                map1.put("out_trade_no",json.getString("out_trade_no"));
                map1.put("timestamp",json.getString("timestamp"));
                String parm1 = Signature.getSignContent(map1);
                String code = MD5.code(parm1+key);
                String sign = json.getString("sign");
                // 验证签名
                if (!code.equals(sign)) {
                    _log.error("Hflpaypay Notify parameter {}", "验签失败");
                    return QueryRetMsg.unknown();
                }
                if("0".equals(mark)) {
                    return QueryRetMsg.confirmSuccess(null);    // 成功
                }
                return QueryRetMsg.waiting(); //支付中
            }else{
                return QueryRetMsg.unknown();
            }
        } catch (Exception e) {
            _log.error(e, "");
            return QueryRetMsg.sysError();
        }
    }

}
