package org.xxpay.pay.channel.swiftpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayCloseRes;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.swiftpay.util.MD5;
import org.xxpay.pay.channel.swiftpay.util.SignUtils;
import org.xxpay.pay.channel.swiftpay.util.XmlUtils;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.mq.Mq4PayQuery;
import org.xxpay.pay.util.Util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: dingzhiwei
 * @date: 18/3/1
 * @description: 威富通支付接口
 */
@Service
public class SwiftpayPaymentService extends BasePayment {

    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    private static final MyLog _log = MyLog.getLog(SwiftpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SWIFTPAY;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;
        switch (channelId) {
            case PayConstant.PAY_CHANNEL_SWIFTPAY_WXPAY_NATIVE :
                retObj = doWxpayNativeReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_SWIFTPAY_ALIPAY_NATIVE :
                retObj = doAlipayNativeReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_SWIFTPAY_MICROPAY :
                retObj = doMicropayReq(payOrder);
                break;
            default: return ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
        }
        return retObj;
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        QueryRetMsg retObj = new QueryRetMsg();
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "unified.trade.query");
        // 商户ID
        map.put("mch_id", swiftpayConfig.getMchId());
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());

        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        String res;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            String req = XmlUtils.parseXML(map);
            _log.info("Swiftpass请求数据:{}", req);
            StringEntity entityParams = new StringEntity(req, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    return QueryRetMsg.unknown();
                }
                // 交易状态
                /*
                    SUCCESS—支付成功
                    REFUND—转入退款
                    NOTPAY—未支付
                    CLOSED—已关闭
                    REVOKED—已撤销
                    USERPAYING—用户支付中
                    PAYERROR—支付失败(其他原因，如银行返回失败)
                */
                String trade_state = resultMap.get("trade_state");

                retObj.setChannelOriginResponse(res); //上游返回结果
                if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code")) && "SUCCESS".equals(trade_state)){

                    retObj.setChannelState(QueryRetMsg.ChannelState.CONFIRM_SUCCESS); //支付成功
                    retObj.setChannelOrderId(resultMap.get("transaction_id"));
                    retObj.setChannelAttach(Util.buildSwiftpayAttach(resultMap));
                    return retObj;

                }else {
                    return QueryRetMsg.waiting(); //支付中
                }
            }else{
                return QueryRetMsg.unknown(); //状态未知
            }
        } catch (Exception e) {
            _log.error(e, "");
            return QueryRetMsg.sysError(); //异常
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

    /**
     * 关闭订单
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes close(PayOrder payOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        PayCloseRes retObj = ApiBuilder.buildSuccess(PayCloseRes.class);
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "unified.trade.close");
        // 商户ID
        map.put("mch_id", swiftpayConfig.getMchId());
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
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
                    retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
                    retObj.setResultMsg("验证签名不通过");
                    return retObj;
                }
                // 业务结果
                /*
                    0表示成功，非0表示失败；0表示关单成功，此笔订单不能再发起支付；若已支付完成，则会发起退款；非0或其它表示关单接口异常，可再次发起关单操作；
                */
                String result_code = resultMap.get("result_code");

                if("0".equals(resultMap.get("status")) && "0".equals(result_code)){
                    retObj.setResultCode(PayCloseRes.ResultCode.SUCCESS);
                }else {
                    retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
                }

                return retObj;
            }else{
                retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
                retObj.setResultMsg("操作失败");
                return retObj;
            }
        } catch (Exception e) {
            _log.error(e, "");
            retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
            retObj.setResultMsg("操作失败");
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

    /**
     * 统一刷卡
     * @param payOrder
     * @return
     */
    public AbstractRes doMicropayReq(PayOrder payOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "unified.trade.micropay");
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
        // 获取objParams参数
        String objParams = payOrder.getExtra();
        String authCode = null;
        if (StringUtils.isNotEmpty(objParams)) {
            JSONObject objParamsJson = JSON.parseObject(objParams);
            authCode = objParamsJson.getString("auth_code");
        }
        if(StringUtils.isBlank(authCode)) {
            return ApiBuilder.bizError("auth_code不能为空!");
        }
        map.put("auth_code", authCode);
        // 商品描述
        map.put("body", payOrder.getBody());
        // 总金额,单位分
        map.put("total_fee", String.valueOf(payOrder.getAmount()));
        // 终端IP
        map.put("mch_create_ip", payOrder.getClientIp());
        // 通知地址
        map.put("notify_url", payConfig.getNotifyUrl(getChannelName()));
        String res;
        map.put("mch_id", swiftpayConfig.getMchId());
        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            String req = XmlUtils.parseXML(map);
            _log.info("Swiftpass请求数据:{}", req);
            StringEntity entityParams = new StringEntity(req, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    return ApiBuilder.bizError("验证签名不通过");
                }
                // 将订单更改为支付中
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
                // 判断订单是否支付成功
                /*
                    关于调用支付接口后相关情况的处理方案：当调用扣款接口返回支付中或未知状态，需要调用查询接口查询订单实际支付状态 。
                    当遇到用户超过日限额需要输入密码返回“支付中”的状态，建议 5 秒调一次查询，调用 6 次后还未成功作支付超时处理。
                    ①支付请求后：status和result code字段返回都为0时，判定订单支付成功；
                    ②支付请求后：返回的参数need_query为Y或没有该参数返回时，必须调用订单查询接口进行支付结果确认（查询接口调用建议参照第④点）；
                    ③支付请求后：返回的参数need_query为N时，可明确为失败订单；
                    ④调用订单查询接口建议：查询6次每隔5秒查询一次（具体的查询次数和时间也可自定义，建议查询时间不低于30秒）6次查询完成，接口仍未返回成功标识(即查询接口返回的trade_state不是SUCCESS)则调用撤销接口；
                */
                String transaction_id = resultMap.get("transaction_id");    // 平台订单号
                if("0".equals(resultMap.get("status")) && "0".equals(resultMap.get("result_code"))){
                    result = rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transaction_id, Util.buildSwiftpayAttach(resultMap));
                    _log.info("[{}]更新订单状态为支付成功:payOrderId={},channelOrderNo={},result={}", getChannelName(), payOrder.getPayOrderId(), transaction_id, result);


                    retObj.setNeedQuery(false); //无需再次查询

                    JSONObject payParams = new JSONObject();
                    payParams.put("channelAttach", Util.buildSwiftpayAttach(resultMap));
                    retObj.setPayParams(payParams); //自定义支付参数

                    // 发送商户支付成功通知
                    if (result == 1) {
                        // 通知业务系统
                        baseNotify4MchPay.doNotify(payOrder, true);
                    }
                }else if(resultMap.get("need_query") == null || "Y".equals(resultMap.get("need_query"))){


                    retObj.setNeedQuery(true); //需要再次查询

                    JSONObject mqMsgObj = new JSONObject();
                    mqMsgObj.put("count", 1);
                    mqMsgObj.put("payOrderId", payOrder.getPayOrderId());
                    mqMsgObj.put("channelName", getChannelName());
                    mq4PayQuery.send(mqMsgObj.toJSONString(), 5 * 1000);  // 5秒后查询

                }else if(resultMap.get("need_query") != null && "N".equals(resultMap.get("need_query"))) {
                    // 明确为失败,可修改订单状态为失败
                    return ApiBuilder.channelError(resultMap.get("err_code"), resultMap.get("err_msg"));
                }
                retObj.setPayMethod(PayConstant.PAY_METHOD_OTHER);
                return retObj;
            }else{
                return ApiBuilder.bizError("操作失败!");
            }
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("操作失败!");
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

    /**
     * 微信扫码支付
     * @param payOrder
     * @return
     */
    public AbstractRes doWxpayNativeReq(PayOrder payOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "pay.weixin.native");
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
        // 商品描述
        map.put("body", payOrder.getBody());
        // 总金额,单位分
        map.put("total_fee", String.valueOf(payOrder.getAmount()));
        // 终端IP
        map.put("mch_create_ip", payOrder.getClientIp());
        // 通知地址
        map.put("notify_url", payConfig.getNotifyUrl(getChannelName()));
        String res;
        map.put("mch_id", swiftpayConfig.getMchId());
        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            String req = XmlUtils.parseXML(map);
            _log.info("Swiftpass请求数据:{}", req);
            StringEntity entityParams = new StringEntity(req, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    return ApiBuilder.bizError("验证签名不通过");
                }
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
                retObj.setQrInfo(resultMap.get("code_url"), resultMap.get("code_img_url"));
                return retObj;
            }else{
                return ApiBuilder.bizError("操作失败!");
            }
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("操作失败!");
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

    /**
     * 支付宝扫码支付
     * @param payOrder
     * @return
     */
    public AbstractRes doAlipayNativeReq(PayOrder payOrder) {
        SwiftpayConfig swiftpayConfig = new SwiftpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        SortedMap<String,String> map = new TreeMap();
        // 接口类型
        map.put("service", "pay.alipay.native");
        // 商户订单号
        map.put("out_trade_no", payOrder.getPayOrderId());
        // 商品描述
        map.put("body", payOrder.getBody());
        // 总金额,单位分
        map.put("total_fee", String.valueOf(payOrder.getAmount()));
        // 终端IP
        map.put("mch_create_ip", payOrder.getClientIp());
        // 通知地址
        map.put("notify_url", payConfig.getNotifyUrl(getChannelName()));
        String res;
        map.put("mch_id", swiftpayConfig.getMchId());
        String key = swiftpayConfig.getKey();
        String reqUrl = swiftpayConfig.getReqUrl();
        // 随机字符串
        map.put("nonce_str", String.valueOf(new Date().getTime()));
        Map<String,String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String preStr = buf.toString();
        String sign = MD5.sign(preStr, "&key=" + key, "utf-8");
        map.put("sign", sign);
        CloseableHttpResponse response;
        CloseableHttpClient client = null;
        try {
            HttpPost httpPost = new HttpPost(reqUrl);
            String req = XmlUtils.parseXML(map);
            _log.info("Swiftpass请求数据:{}", req);
            StringEntity entityParams = new StringEntity(req, "utf-8");
            httpPost.setEntity(entityParams);
            httpPost.setHeader("Content-Type", "text/xml;charset=ISO-8859-1");
            client = HttpClients.createDefault();
            response = client.execute(httpPost);
            if(response != null && response.getEntity() != null){
                Map<String,String> resultMap = XmlUtils.toMap(EntityUtils.toByteArray(response.getEntity()), "utf-8");
                res = XmlUtils.toXml(resultMap);
                _log.info("Swiftpass请求结果:{}", res);
                if(resultMap.containsKey("sign") && !SignUtils.checkParam(resultMap, key)){
                    return ApiBuilder.bizError("验证签名不通过");
                }
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},prepayId={},result={}", getChannelName(), payOrder.getPayOrderId(), "", result);
                retObj.setQrInfo(resultMap.get("code_url"), resultMap.get("code_img_url"));
                return retObj;
            }else{
                return ApiBuilder.bizError("操作失败!");
            }
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("操作失败!");
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
