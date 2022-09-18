package org.xxpay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayCloseRes;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.wxpay.request.WxDepositMicropayRequest;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.mq.Mq4PayQuery;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class WxpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(WxpayPaymentService.class);

    @Autowired
    private BaseNotify4MchPay baseNotify4MchPay;
    @Autowired
    private Mq4PayQuery mq4PayQuery;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_WXPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {

        if (PayConstant.PAY_CHANNEL_WX_BAR.equals(payOrder.getChannelId())) { //微信条码支付

            return this.microPay(payOrder);

        }else{ //其他支付接口调用微信统一下单接口

            return this.unifiedOrder(payOrder);
        }
    }

    /** 微信统一下单接口 **/
    private AbstractRes unifiedOrder(PayOrder payOrder){
        String logPrefix = "【微信支付统一下单】";
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        try{
            String channelId = payOrder.getChannelId();
            String tradeType = channelId.substring(channelId.indexOf("_") + 1).toUpperCase();   // 转大写,与微信一致
            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));
            wxPayConfig.setTradeType(tradeType);

            //获取子商户参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); //微信配置信息

            WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = buildUnifiedOrderRequest(payOrder, wxPayConfig, subMchParam);
            String payOrderId = payOrder.getPayOrderId();
            WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
            try {
                wxPayUnifiedOrderResult = wxPayService.unifiedOrder(wxPayUnifiedOrderRequest);
                _log.info("{} >>> 下单成功", logPrefix);
                retObj.setPayOrderId(payOrderId);
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
                _log.info("更新第三方支付订单号:payOrderId={},prepayId={},result={}", payOrderId, wxPayUnifiedOrderResult.getPrepayId(), result);
                switch (tradeType) {
                    case PayConstant.WxConstant.TRADE_TYPE_NATIVE : {

                        String codeUrl = wxPayUnifiedOrderResult.getCodeURL();// 二维码支付链接
                        retObj.setQrInfo(codeUrl, genCodeImgUrl(codeUrl));

                        //自定义参数
                        JSONObject payParams = new JSONObject();
                        payParams.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        retObj.setPayParams(payParams);

                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_APP : {
                        Map<String, String> payInfo = new HashMap<>();
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        // APP支付绑定的是微信开放平台上的账号，APPID为开放平台上绑定APP后发放的参数
                        String wxAppId = wxPayUnifiedOrderRequest.getSubAppId();
                        Map<String, String> configMap = new HashMap<>();
                        // 此map用于参与调起sdk支付的二次签名,格式全小写，timestamp只能是10位,格式固定，切勿修改
                        String partnerId = wxPayUnifiedOrderRequest.getSubMchId();
                        configMap.put("prepayid", wxPayUnifiedOrderResult.getPrepayId());
                        configMap.put("partnerid", partnerId);
                        String packageValue = "Sign=WXPay";
                        configMap.put("package", packageValue);
                        configMap.put("timestamp", timestamp);
                        configMap.put("noncestr", nonceStr);
                        configMap.put("appid", wxAppId);
                        // 此map用于客户端与微信服务器交互
                        payInfo.put("sign", SignUtils.createSign(configMap, wxPayConfig.getSignType(), wxPayConfig.getMchKey(), null));
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("partnerId", partnerId);
                        payInfo.put("appId", wxAppId);
                        payInfo.put("package", packageValue);
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);

                        retObj.setAppInfo(PayConstant.PAY_METHOD_WX_APP, JSONObject.parseObject(JSON.toJSONString(payInfo)));
                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_JSPAI : {
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        Map<String, String>  payInfo = new HashMap<>(); // 如果用JsonObject会出现签名错误

                        // 如果传了appId则使用，否则使用服务商
                        if(StringUtils.isNotBlank(payOrder.getAppId())) {
                            payInfo.put("appId", payOrder.getAppId());
                        }else {
                            payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
                        }

                        // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("signType", WxPayConstants.SignType.MD5);
                        payInfo.put("paySign", SignUtils.createSign(payInfo, wxPayConfig.getSignType(), wxPayConfig.getMchKey(), null));
                        // 签名以后在增加prepayId参数
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());

                        retObj.setAppInfo(PayConstant.PAY_METHOD_WX_JSAPI, JSONObject.parseObject(JSON.toJSONString(payInfo)));
                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_MWEB : {

                        //自定义参数
                        JSONObject payParams = new JSONObject();
                        payParams.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());
                        retObj.setPayParams(payParams);
                        retObj.setJumpInfo(wxPayUnifiedOrderResult.getMwebUrl()); // h5支付链接地址

                        break;
                    }
                    case PayConstant.WxConstant.TRADE_TYPE_MINI : {
                        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                        String nonceStr = String.valueOf(System.currentTimeMillis());
                        Map<String, String>  payInfo = new HashMap<>(); // 如果用JsonObject会出现签名错误

                        // 如果传了appId则使用，否则使用服务商
                        if(StringUtils.isNotBlank(payOrder.getAppId())) {
                            payInfo.put("appId", payOrder.getAppId());
                        }else {
                            payInfo.put("appId", wxPayUnifiedOrderResult.getAppid());
                        }

                        // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                        payInfo.put("timeStamp", timestamp);
                        payInfo.put("nonceStr", nonceStr);
                        payInfo.put("package", "prepay_id=" + wxPayUnifiedOrderResult.getPrepayId());
                        payInfo.put("signType", WxPayConstants.SignType.MD5);
                        payInfo.put("paySign", SignUtils.createSign(payInfo, wxPayConfig.getSignType(), wxPayConfig.getMchKey(), null));
                        // 签名以后在增加prepayId参数
                        payInfo.put("prepayId", wxPayUnifiedOrderResult.getPrepayId());

                        // 微信小程序支付 同 微信公众号支付
                        retObj.setAppInfo(PayConstant.PAY_METHOD_WX_JSAPI, JSONObject.parseObject(JSON.toJSONString(payInfo)));
                        break;
                    }
                }
            } catch (WxPayException e) {
                _log.error(e, "下单失败");
                //出现业务错误
                _log.info("{}下单返回失败", logPrefix);
                _log.info("err_code:{}", e.getErrCode());
                _log.info("err_code_des:{}", e.getErrCodeDes());
                return ApiBuilder.bizError(e.getErrCodeDes());
            }
        }catch (Exception e) {
            _log.error(e, "微信支付统一下单异常");
            return ApiBuilder.bizError("微信支付统一下单异常");
        }
        return retObj;
    }

    /** 微信付款码支付（被扫） **/
    private AbstractRes microPay(PayOrder payOrder){
        String logPrefix = "【微信条码支付下单】";
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        String payOrderId = payOrder.getPayOrderId();

        //更新订单为【支付中】状态
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

        try{
            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));

            //获取子商户参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); //微信配置信息

            //初始化reqUrl,  request
            String reqUrl;
            WxPayMicropayRequest request;
            if(payOrder.getDepositMode() != null && payOrder.getDepositMode() == MchConstant.PUB_YES){ //押金模式
                reqUrl = wxPayService.getPayBaseUrl() + "/deposit/micropay";
                request = new WxDepositMicropayRequest();
                WxDepositMicropayRequest depositRequest = (WxDepositMicropayRequest)request;
                depositRequest.setDeposit("Y");
                request.setTotalFee(payOrder.getDepositAmount().intValue());
            }else{
                reqUrl = wxPayService.getPayBaseUrl() + "/pay/micropay";
                request = new WxPayMicropayRequest();
                request.setTotalFee(payOrder.getAmount().intValue());
            }

            if(subMchParam != null){ //子账户发起
                request.setSubMchId(subMchParam.getString("subMchId"));
            }

            request.setBody(payOrder.getBody());
            request.setOutTradeNo(payOrder.getPayOrderId());
            request.setFeeType("CNY");
            request.setSpbillCreateIp(payOrder.getClientIp());
            request.setAuthCode(payOrder.getExtra());
            request.checkAndSign(wxPayService.getConfig());

            String responseContent = wxPayService.post(reqUrl, request.toXML(), false);
            WxPayMicropayResult result = BaseWxPayResult.fromXML(responseContent, WxPayMicropayResult.class);
            result.checkResult(wxPayService, request.getSignType(), true);

            ///明确支付成功
            //更新订单为成功状态 //添加ChannelAttach 为微信的openId 20190823
            JSONObject openIdJSON = new JSONObject();  //createNotifyUrl指定 ChannelAttach 必须为json形式
            openIdJSON.put("openId", result.getOpenid());
            rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), result.getTransactionId(), openIdJSON.toJSONString());
            //回调商户
            payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS);
            baseNotify4MchPay.doNotify(payOrder, true);

            retObj.setPayOrderId(payOrderId);
            retObj.setOrderStatus(PayConstant.PAY_STATUS_SUCCESS); //支付成功状态
            return retObj;

        } catch (WxPayException e) {
            _log.error("{}下单返回失败, err_code:{}, err_code_des:{}", logPrefix, e.getErrCode(), e.getErrCodeDes());

            //微信返回支付状态为【支付结果未知】, 需进行查单操作
            if("SYSTEMERROR".equals(e.getErrCode()) || "USERPAYING".equals(e.getErrCode()) ||  "BANKERROR".equals(e.getErrCode())){

                //轮询查询订单
                JSONObject mqMsgObj = new JSONObject();
                mqMsgObj.put("count", 1);
                mqMsgObj.put("payOrderId", payOrder.getPayOrderId());
                mqMsgObj.put("channelName", getChannelName());
                mq4PayQuery.send(mqMsgObj.toJSONString(), 5 * 1000);  // 5秒后查询

                retObj.setPayOrderId(payOrderId);
                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING); //支付中， 需要商户主动查询
                return retObj;
            }

            //其他错误码, 表示明确失败
            String errMsg = "调起微信接口异常：";
            if(StringUtils.isNotEmpty(e.getErrCodeDes())) errMsg += "["+e.getErrCodeDes()+"]";
            if(StringUtils.isNotEmpty(e.getReturnMsg())) errMsg += "["+e.getReturnMsg()+"]";
            return ApiBuilder.bizError(errMsg);

        }catch (Exception e) {
            _log.error(e, "微信支付统一下单异常");
            return ApiBuilder.bizError("微信支付统一下单异常");
        }

    }

    @Override
    public QueryRetMsg query(PayOrder payOrder) {

        String logPrefix = "【微信条码支付查询】";

        try {
            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));

            //获取子商户参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); //微信配置信息
            WxPayOrderQueryRequest request = new WxPayOrderQueryRequest();

            if(subMchParam != null){ //子账户发起
                request.setSubMchId(subMchParam.getString("subMchId"));
            }

            request.setOutTradeNo(payOrder.getPayOrderId());
            WxPayOrderQueryResult result = wxPayService.queryOrder(request);
            if("SUCCESS".equals(result.getTradeState())){ //支付成功
                return QueryRetMsg.confirmSuccess(result.getTransactionId());
            }else if("USERPAYING".equals(result.getTradeState())){ //支付中，等待用户输入密码
                return QueryRetMsg.waiting(); //支付中
            }else if("CLOSED".equals(result.getTradeState())
            || "REVOKED".equals(result.getTradeState())
            || "PAYERROR".equals(result.getTradeState())){  //CLOSED—已关闭， REVOKED—已撤销(刷卡支付), PAYERROR--支付失败(其他原因，如银行返回失败)
                return QueryRetMsg.confirmFail(); //支付失败
            }else{
                return QueryRetMsg.unknown();
            }

        } catch (WxPayException e) {
            _log.error("{}WxPayException异常", logPrefix, e);
            return QueryRetMsg.sysError();
        }catch (Exception e) {
            _log.error("{}异常", logPrefix, e);
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {

        String logPrefix = "【微信订单关闭】";
        PayCloseRes retObj = ApiBuilder.buildSuccess(PayCloseRes.class);
        try {
            //获取主发起参数
            JSONObject mainParam = getMainPayParam(payOrder);

            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));

            //获取子商户参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); //微信配置信息
            WxPayOrderCloseRequest request = new WxPayOrderCloseRequest();

            if(subMchParam != null){ //子账户发起
                request.setSubMchId(subMchParam.getString("subMchId"));
            }

            request.setOutTradeNo(payOrder.getPayOrderId());
            WxPayOrderCloseResult result = wxPayService.closeOrder(request);

            if("SUCCESS".equals(result.getResultCode())){ //关闭成功
                retObj.setResultCode(PayCloseRes.ResultCode.SUCCESS);
            }else{
                retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
                retObj.setResultMsg(result.getResultMsg());
            }

        } catch (WxPayException e) {
            _log.error("{}WxPayException异常", logPrefix, e);
            return ApiBuilder.bizError("关单失败[" + e.getErrCodeDes() + "]");

        }catch (Exception e) {
            _log.error("{}异常", logPrefix, e);
            return ApiBuilder.bizError("关单异常");
        }

        return retObj;
    }

    /**
     * 构建微信统一下单请求数据
     * @param payOrder
     * @param wxPayConfig
     * @return
     */
    WxPayUnifiedOrderRequest buildUnifiedOrderRequest(PayOrder payOrder, WxPayConfig wxPayConfig, JSONObject subMchParam) {
        String tradeType = wxPayConfig.getTradeType();
        String payOrderId = payOrder.getPayOrderId();
        Integer totalFee = payOrder.getAmount().intValue();// 支付金额,单位分
        String deviceInfo = payOrder.getDevice();
        String body = payOrder.getBody();
        String detail = null;
        String attach = null;
        String outTradeNo = payOrderId;
        String feeType = "CNY";
        String spBillCreateIP = payOrder.getClientIp();
        String timeStart = null;
        String timeExpire = null;
        String goodsTag = null;
        String notifyUrl = payConfig.getNotifyUrl(getChannelName());
        String productId = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_NATIVE)) productId = System.currentTimeMillis()+"";
        String limitPay = null;
        String openId = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_JSPAI) || PayConstant.WxConstant.TRADE_TYPE_MINI.equals(tradeType)) openId = payOrder.getChannelUser();
        String sceneInfo = null;
        if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_MWEB)) sceneInfo = JSON.parseObject(payOrder.getExtra()).getString("sceneInfo");
        // 微信统一下单请求对象
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setDeviceInfo(deviceInfo);
        request.setBody(body);
        request.setDetail(detail);
        request.setAttach(attach);
        request.setOutTradeNo(outTradeNo);
        request.setFeeType(feeType);
        request.setTotalFee(totalFee);
        request.setSpbillCreateIp(spBillCreateIP);
        request.setTimeStart(timeStart);
        request.setTimeExpire(timeExpire);
        request.setGoodsTag(goodsTag);
        request.setNotifyUrl(notifyUrl);

        // 设置微信tradeType
        if(PayConstant.WxConstant.TRADE_TYPE_MINI.equals(tradeType)) {
            request.setTradeType(PayConstant.WxConstant.TRADE_TYPE_JSPAI);
        }else {
            request.setTradeType(tradeType);
        }

        request.setProductId(productId);
        request.setLimitPay(limitPay);
        request.setOpenid(openId);
        request.setSceneInfo(sceneInfo);

        if(subMchParam != null){
            request.setSubMchId(subMchParam.getString("subMchId"));
            // 服务商模式下小程序和公众号支付
            // 如果通知支付接口传了appId，则openId指的是该appId下的，否则指的是服务商appId下的
            if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_MINI)) {
                String subMchAppId = payOrder.getAppId();   // 对应小程序appID
                if(StringUtils.isNotBlank(subMchAppId)) {   // 如果不为空,则openId指的是该subMchAppId下的
                    request.setSubAppId(subMchAppId);
                    request.setSubOpenid(openId);
                    request.setOpenid(null);
                }
            }else if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_JSPAI)) {
                String subMchAppId = payOrder.getAppId();   // 公众号appID
                if(StringUtils.isNotBlank(subMchAppId)) {   // 如果不为空,则openId指的是该subMchAppId下的
                    request.setSubAppId(subMchAppId);
                    request.setSubOpenid(openId);
                    request.setOpenid(null);
                }
            }else if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_MWEB)) {
                String subMchAppId = payOrder.getAppId();   // H5支付appID
                if(StringUtils.isNotBlank(subMchAppId)) {
                    request.setSubAppId(subMchAppId);
                }
            }else if(tradeType.equals(PayConstant.WxConstant.TRADE_TYPE_APP)) {
                String subMchAppId = payOrder.getAppId();   // 特约商户在微信开放平台上申请的APPID
                if(StringUtils.isNotBlank(subMchAppId)) {
                    request.setSubAppId(subMchAppId);
                }
            }
        }

        return request;
    }


}
