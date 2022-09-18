package org.xxpay.pay.channel.dlbpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayCloseRes;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.mq.Mq4PayQuery;

import java.util.HashMap;
import java.util.Map;

@Service
public class DlbpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(DlbpayPaymentService.class);

    @Autowired private BaseNotify4MchPay baseNotify4MchPay;
    @Autowired private Mq4PayQuery mq4PayQuery;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_DLBPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;

        switch (channelId) {
            case PayConstant.PAY_CHANNEL_DLBPAY_WX_BAR :
                retObj = doWxBar(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_DLBPAY_WX_JSAPI :
                retObj = doWxJSAPI(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_DLBPAY_ALIPAY_BAR :
                retObj = doAlipayBar(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_DLBPAY_ALIPAY_JSAPI :
                retObj = doAlipayJSAPI(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_DLBPAY_JD_H5 :
                retObj = doJDH5(payOrder);
                break;
            default:
                retObj = ApiBuilder.bizError("不支持的哆啦宝渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    /** 微信条码支付 **/
    private AbstractRes doWxBar(PayOrder payOrder){

        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号

            reqData.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //金额， 单位元

            reqData.put("authCode", payOrder.getExtra());  //⽤户付款码
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("source", "API");
            reqData.put("callbackUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/passive/create", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));


            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                retObj.setPayOrderId(payOrderId);
                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING); //支付中， 需要商户主动查询
                return retObj;
            }else{ //支付失败

                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);
                return ApiBuilder.bizError("下单失败");
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }
    }

    /** 微信服务窗支付 **/
    private AbstractRes doWxJSAPI(PayOrder payOrder){

        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号

            reqData.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //金额， 单位元

            reqData.put("authId", payOrder.getChannelUser());  //⽤户ID
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("callbackUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址
            reqData.put("bankType", "WX"); //类型： 微信

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/order/pay/create", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                JSONObject bankRequestData = respData.getJSONObject("data").getJSONObject("bankRequest");

                Map<String, String> payInfo = new HashMap<>(); // 如果用JsonObject会出现签名错误
                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                retObj.setPayOrderId(payOrderId);
                payInfo.put("appId", bankRequestData.getString("APPID"));
                payInfo.put("timeStamp", bankRequestData.getString("TIMESTAMP"));
                payInfo.put("nonceStr", bankRequestData.getString("NONCESTR"));
                payInfo.put("package", bankRequestData.getString("PACKAGE"));
                payInfo.put("signType", bankRequestData.getString("SIBGTYPE"));
                payInfo.put("paySign", bankRequestData.getString("PAYSIGN"));
                retObj.setAppInfo(PayConstant.PAY_METHOD_WX_JSAPI, JSONObject.parseObject(JSON.toJSONString(payInfo)));

                return retObj;

            }else{ //支付失败

                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);
                return ApiBuilder.bizError("下单失败");
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }

    }

    /** 支付宝条码支付 **/
    private AbstractRes doAlipayBar(PayOrder payOrder){

        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号

            reqData.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //金额， 单位元

            reqData.put("authCode", payOrder.getExtra());  //⽤户付款码
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("source", "API");
            reqData.put("callbackUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/passive/create", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));


            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                retObj.setPayOrderId(payOrderId);
                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING); //支付中， 需要商户主动查询
                return retObj;
            }else{ //支付失败

                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);
                return ApiBuilder.bizError("下单失败");
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }

    }

    /** 支付宝服务窗支付 **/
    private AbstractRes doAlipayJSAPI(PayOrder payOrder){

        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号

            reqData.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //金额， 单位元

            reqData.put("authId", payOrder.getChannelUser());  //⽤户ID
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("callbackUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址
            reqData.put("bankType", "ALIPAY"); //类型： 支付宝

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/order/pay/create", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                JSONObject bankRequestData = respData.getJSONObject("data").getJSONObject("bankRequest");


                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                JSONObject payParam = new JSONObject();
                payParam.put("alipayTradeNo", bankRequestData.getString("TRADENO"));  //支付宝交易号， 用于调起jsapi
                retObj.setPayParams(payParam);
                retObj.setPayOrderId(payOrderId);
                return retObj;

            }else{ //支付失败

                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);
                return ApiBuilder.bizError("下单失败");
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }

    }

    /** 京东H5支付 **/
    private AbstractRes doJDH5(PayOrder payOrder){

        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("agentNum", mainParam.getString("agentNum"));  //代理商编号
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("shopNum", subMchParam.getString("shopNum")); //店铺编号

            reqData.put("amount", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //金额， 单位元

            reqData.put("authId", MySeq.getUUID());  //⽤户ID ()随机字符串
            reqData.put("requestNum", payOrderId);//必须是 18--32 位的纯数字
            reqData.put("callbackUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址
            reqData.put("bankType", "JD"); //类型： 京东支付

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v1/agent/order/pay/create", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                JSONObject bankRequestData = respData.getJSONObject("data").getJSONObject("bankRequest");


                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                JSONObject payParam = new JSONObject();
                payParam.put("ORDERNUM", bankRequestData.getString("ORDERNUM"));  //订单编号
                payParam.put("OUT_TRADE_NO", bankRequestData.getString("OUT_TRADE_NO"));  //银⾏流⽔号
                payParam.put("PAY_URL", bankRequestData.getString("PAY_URL"));  //京东⽀付链接
                payParam.put("CUSTOMERNUM", bankRequestData.getString("CUSTOMERNUM"));  //商户编号
                retObj.setPayParams(payParam);
                retObj.setPayOrderId(payOrderId);
                return retObj;

            }else{ //支付失败

                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);
                return ApiBuilder.bizError("下单失败");
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }

    }



    @Override
    public QueryRetMsg query(PayOrder payOrder) {

        try {
            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //业务参数
            String urlPath = String.format("/v2/agent/order/payresult/%s/%s/%s/%s",
                    mainParam.getString("agentNum"), subMchParam.getString("customerNum"),
                    subMchParam.getString("shopNum"), payOrder.getPayOrderId()
            );


            //请求接口
            JSONObject resData = DlbHttpUtils.connect("GET", urlPath, null, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            if(!"success".equals(resData.getString("result"))){
                return QueryRetMsg.waiting(); //支付中  按照支付成功处理
            }

            JSONObject data = resData.getJSONObject("data");
            String orderNum = data.getString("orderNum");  //哆啦宝订单号
            String status = data.getString("status"); //订单状态


            if("SUCCESS".equals(status)) { //支付成功
                return QueryRetMsg.confirmSuccess(orderNum);

            }else if("FAIL".equals(status)){ //支付失败
                return QueryRetMsg.confirmFail(); //支付失败

            }else if("REFUND".equals(status)){ //支付退款
                return QueryRetMsg.waiting();

            }else if("CLOSE".equals(status)){ //订单关闭
                return QueryRetMsg.confirmFail(); //支付失败

            }else if("CANCLE".equals(status)){ //订单撤销
                return QueryRetMsg.confirmFail(); //支付失败

            }else if("INIT".equals(status)){ //订单状态不明确

                JSONObject payRecord = (JSONObject) data.getJSONArray("payRecordList").get(0);

                if("isPaying".equals(payRecord.getString("payStatus"))){
                    return QueryRetMsg.waiting();

                }else if("SUCCESS".equals(payRecord.getString("payStatus"))){
                    return QueryRetMsg.confirmSuccess(orderNum);
                }else if("FAIL".equals(payRecord.getString("payStatus"))){
                    return QueryRetMsg.confirmFail(); //支付失败
                }
            }

        } catch (Exception e) {

            _log.error("查单异常", e);
            return QueryRetMsg.sysError();
        }

        return QueryRetMsg.waiting();
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {


        String payOrderId = payOrder.getPayOrderId();

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //构造请求参数
            JSONObject reqData = new JSONObject();
            reqData.put("customerNum", subMchParam.getString("customerNum")); //商户编号
            reqData.put("requestNum", payOrderId);

            //请求接口
            JSONObject respData = DlbHttpUtils.connect("POST", "/v2/agent/order/cancel", reqData, mainParam.getString("secretKey"), mainParam.getString("accessKey"));

            //哆啦宝返回的状态
            String dlbResult = respData.getString("result");

            if("success".equals(dlbResult)){ //照支付中处理

                PayCloseRes retObj = ApiBuilder.buildSuccess(PayCloseRes.class);
                retObj.setResultCode(PayCloseRes.ResultCode.SUCCESS);
                return retObj;

            }else{ //失败

                _log.error("关单失败！ res={}", respData);
                return null;
            }

        } catch (Exception e) {
            _log.error("下单失败！", e);
            return ApiBuilder.bizError("下单失败[" + e.getMessage() + "]");
        }

    }
}
