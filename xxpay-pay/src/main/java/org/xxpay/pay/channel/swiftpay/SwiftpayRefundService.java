package org.xxpay.pay.channel.swiftpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.pay.channel.BaseRefund;
import org.xxpay.pay.channel.swiftpay.util.MD5;
import org.xxpay.pay.channel.swiftpay.util.SignUtils;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: dingzhiwei
 * @date: 18/03/08
 * @description: 威富通退款
 */
@Service
public class SwiftpayRefundService extends BaseRefund {

    private static final MyLog _log = MyLog.getLog(SwiftpayRefundService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SWIFTPAY;
    }

    public JSONObject refund(RefundOrder refundOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getRefundParam(refundOrder));
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "unified.trade.refund");
        // 商户ID
        map.put("mch_id", swiftpayConfig.getMchId());
        // 商户订单号
        map.put("out_trade_no", refundOrder.getPayOrderId());
        // 商户退款单号
        map.put("out_refund_no", refundOrder.getRefundOrderId());
        // 总金额
        map.put("total_fee", String.valueOf(refundOrder.getPayAmount()));
        // 退款金额
        map.put("refund_fee", String.valueOf(refundOrder.getRefundAmount()));
        // 操作员
        map.put("op_user_id", String.valueOf(refundOrder.getMchId()));
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));

        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        JSONObject retObj = buildRetObj();
        retObj.put("isSuccess", false);
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    retObj.put("errDes", "验证签名不通过");
                    return retObj;
                }
                if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                    retObj.put("isSuccess", true);
                    retObj.put("channelOrderNo", resultMap.get("refund_id"));
                }else {
                    retObj.put("channelErrCode", resultMap.get("err_code"));
                    retObj.put("isSuccess", false);
                }
                return retObj;
            }else{
                retObj.put("errDes", "操作失败!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        } finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    _log.error(e, "");
                }
            }
        }
    }

    public JSONObject query(RefundOrder refundOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getRefundParam(refundOrder));
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "unified.trade.refundquery");
        // 商户ID
        map.put("mch_id", swiftpayConfig.getMchId());
        // 商户订单号
        map.put("out_trade_no", refundOrder.getPayOrderId());
        // 商户退款单号
        map.put("out_refund_no", refundOrder.getRefundOrderId());
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();

        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        JSONObject retObj = buildRetObj();
        retObj.put("isSuccess", false);
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            StringEntity entityParams = new StringEntity(XmlUtils.parseXML(map),"utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    retObj.put("errDes", "验证签名不通过");
                    return retObj;
                }
                if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                    retObj.put("isSuccess", true);
                    retObj.putAll(resultMap);
                }else {
                    retObj.put("channelErrCode", resultMap.get("err_code"));
                    retObj.put("isSuccess", false);
                }
                return retObj;
            }else{
                retObj.put("errDes", "操作失败!");
                retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.put("errDes", "操作失败!");
            retObj.put(PayConstant.RETURN_PARAM_RETCODE, PayConstant.RETURN_VALUE_FAIL);
            return retObj;
        } finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    _log.error(e, "");
                }
            }
        }
    }

}
