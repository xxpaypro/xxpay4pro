package org.xxpay.pay.channel.suixingpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.util.SignUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.suixingpay.utils.SuixingpayUtil;
import org.xxpay.pay.mq.BaseNotify4MchPay;
import org.xxpay.pay.mq.Mq4PayQuery;

import java.util.HashMap;
import java.util.Map;

@Service
public class SuixingpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(SuixingpayPaymentService.class);

    @Autowired private BaseNotify4MchPay baseNotify4MchPay;
    @Autowired private Mq4PayQuery mq4PayQuery;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_SUIXINGPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;

        switch (channelId) {
            case PayConstant.PAY_CHANNEL_SUIXINGPAY_WX_BAR :
                retObj = doWxBar(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_SUIXINGPAY_WX_JSAPI :
                retObj = doWxJSAPI(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_SUIXINGPAY_ALIPAY_BAR :
                retObj = doAlipayBar(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_SUIXINGPAY_ALIPAY_JSAPI :
                retObj = doAlipayJSAPI(payOrder);
                break;
            default:
                retObj = ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
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

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", subMchParam.getString("mno")); //商户编号
//            reqData.put("subMechId", subMchParam.getString("subMechId")); //子商户号
            reqData.put("ordNo", payOrder.getPayOrderId()); //商户订单号
            reqData.put("authCode", payOrder.getExtra()); //授权码
            reqData.put("amt", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //订单总金额
            reqData.put("payType", "WECHAT"); //支付渠道
            reqData.put("scene", "1"); //支付场景，1： 刷卡 2：声波 3：刷脸   不上传默认为 1
            reqData.put("subject", payOrder.getSubject()); //订单标题
            reqData.put("tradeSource", "01"); //交易来源 01服务商，02收银台，03硬件
            reqData.put("trmIp", payOrder.getClientIp());
            reqData.put("notifyUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = SuixingpayUtil.req("/order/reverseScan", payOrder, mainParam, reqData);

            //业务编码
            String bizCode = respData.getString("bizCode");

            if("0000".equals(bizCode)){ //明确支付成功

                String buyerId = respData.getString("buyerId");
                String transactionId = respData.getString("transactionId"); //微信侧流水ID

                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

                String channelAttach = null;
                if(StringUtils.isNotEmpty(buyerId)){
                    JSONObject openIdJSON = new JSONObject();  //createNotifyUrl指定 ChannelAttach 必须为json形式
                    openIdJSON.put("openId", buyerId);
                    channelAttach = openIdJSON.toJSONString();
                }

                rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transactionId, channelAttach);

                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS); //回调商户
                baseNotify4MchPay.doNotify(payOrder, true);
                retObj.setPayOrderId(payOrder.getPayOrderId());
                retObj.setOrderStatus(PayConstant.PAY_STATUS_SUCCESS); //支付成功状态
                return retObj;
            }

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
            retObj.setPayOrderId(payOrderId);
            retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING); //支付中， 需要商户主动查询
            return retObj;

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

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", subMchParam.getString("mno")); //商户编号
//            reqData.put("subMechId", subMchParam.getString("subMechId")); //子商户号
            reqData.put("ordNo", payOrder.getPayOrderId()); //商户订单号
            reqData.put("amt", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //订单总金额
            reqData.put("payType", "WECHAT"); //支付渠道
            reqData.put("payWay", "02"); //支付方式02 公众号/服务窗/js支付 03小程序
            reqData.put("subject", payOrder.getSubject()); //订单标题
            reqData.put("tradeSource", "01"); //交易来源 01服务商，02收银台，03硬件
            reqData.put("trmIp", payOrder.getClientIp()); //IP
            reqData.put("userId", payOrder.getChannelUser()); //微信OPENID
            reqData.put("notifyUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = SuixingpayUtil.req("/order/jsapiScan", payOrder, mainParam, reqData);

            //业务编码
            String bizCode = respData.getString("bizCode");

            if(!"0000".equals(bizCode)){ //业务异常
                return ApiBuilder.bizError(respData.getString("bizMsg"));
            }

            Map<String, String> payInfo = new HashMap<>(); // 如果用JsonObject会出现签名错误
            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
            retObj.setPayOrderId(payOrderId);
            payInfo.put("appId", respData.getString("payAppId"));
            payInfo.put("timeStamp", respData.getString("payTimeStamp"));
            payInfo.put("nonceStr", respData.getString("paynonceStr"));
            payInfo.put("package", respData.getString("payPackage"));
            payInfo.put("signType", respData.getString("paySignType"));
            payInfo.put("paySign", respData.getString("paySign"));
            payInfo.put("prepayId", respData.getString("prepayId"));
            retObj.setAppInfo(PayConstant.PAY_METHOD_WX_JSAPI, JSONObject.parseObject(JSON.toJSONString(payInfo)));

            return retObj;

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

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", subMchParam.getString("mno")); //商户编号
//            reqData.put("subMechId", subMchParam.getString("subMechId")); //子商户号
            reqData.put("ordNo", payOrder.getPayOrderId()); //商户订单号
            reqData.put("authCode", payOrder.getExtra()); //授权码
            reqData.put("amt", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //订单总金额
            reqData.put("payType", "ALIPAY"); //支付渠道
            reqData.put("scene", "1"); //支付场景，1： 刷卡 2：声波 3：刷脸   不上传默认为 1
            reqData.put("subject", payOrder.getSubject()); //订单标题
            reqData.put("tradeSource", "01"); //交易来源 01服务商，02收银台，03硬件
            reqData.put("trmIp", payOrder.getClientIp());
            reqData.put("notifyUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = SuixingpayUtil.req("/order/reverseScan", payOrder, mainParam, reqData);


            //业务编码
            String bizCode = respData.getString("bizCode");

            if("0000".equals(bizCode)){ //支付成功

                String transactionId = respData.getString("transactionId"); //支付宝侧流水ID

                rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), transactionId); //更新订单为成功

                //随行付明确支付成功不回调， 仅支付中状态回调。
                payOrder.setStatus(PayConstant.PAY_STATUS_SUCCESS); //回调商户
                baseNotify4MchPay.doNotify(payOrder, true);

                PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
                retObj.setOrderStatus(PayConstant.PAY_STATUS_SUCCESS);
                retObj.setPayOrderId(payOrderId);
                return retObj;
            }

            //以下按照支付中处理
            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
            retObj.setPayOrderId(payOrderId);
            retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING); //支付中， 需要商户主动查询
            return retObj;

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

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", subMchParam.getString("mno")); //商户编号
//            reqData.put("subMechId", subMchParam.getString("subMechId")); //子商户号
            reqData.put("ordNo", payOrder.getPayOrderId()); //商户订单号
            reqData.put("amt", AmountUtil.convertCent2Dollar(payOrder.getAmount())); //订单总金额
            reqData.put("payType", "ALIPAY"); //支付渠道
            reqData.put("payWay", "02"); //支付方式02 公众号/服务窗/js支付 03小程序
            reqData.put("subject", payOrder.getSubject()); //订单标题
            reqData.put("tradeSource", "01"); //交易来源 01服务商，02收银台，03硬件
            reqData.put("trmIp", payOrder.getClientIp()); //IP
            reqData.put("userId", payOrder.getChannelUser()); //支付宝userID
            reqData.put("notifyUrl", payConfig.getNotifyUrl(getChannelName())); //回调地址

            //更新订单为【支付中】状态
            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);

            //请求接口
            JSONObject respData = SuixingpayUtil.req("/order/jsapiScan", payOrder, mainParam, reqData);

            //业务编码
            String bizCode = respData.getString("bizCode");

            if(!"0000".equals(bizCode)){ //业务异常
                return ApiBuilder.bizError(respData.getString("bizMsg"));
            }

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

            JSONObject payParam = new JSONObject();
            payParam.put("alipayTradeNo", respData.getString("source"));  //支付宝交易号， 用于调起jsapi
            retObj.setPayParams(payParam);
            retObj.setPayOrderId(payOrderId);
            return retObj;

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
            JSONObject reqData = new JSONObject();
            reqData.put("mno", mainParam.getString("mno")); //商户编号
            reqData.put("ordNo", payOrder.getPayOrderId()); //商户订单号

            //请求接口
            JSONObject resData = SuixingpayUtil.req("/query/tradeQuery", payOrder, mainParam, reqData);

            String tranSts = resData.getString("tranSts");
            String transactionId = resData.getString("transactionId");

            if("SUCCESS".equals(tranSts)){ //支付成功
                return QueryRetMsg.confirmSuccess(transactionId);

            }else if("PAYING".equals(tranSts)){ //支付中，等待用户输入密码
                return QueryRetMsg.waiting(); //支付中

            }else if("CLOSED".equals(tranSts) || "CANCELED".equals(tranSts) || "FAIL".equals(tranSts) ){
                return QueryRetMsg.confirmFail(); //支付失败
            }else{
                return QueryRetMsg.unknown();
            }

        } catch (Exception e) {

            _log.error("查单异常", e);
            return QueryRetMsg.sysError();
        }
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            JSONObject subMchParam = getSubMchPayParam(payOrder);  //支付子商户

            //业务参数
            JSONObject reqData = new JSONObject();
            reqData.put("mno", mainParam.getString("mno")); //商户编号
            reqData.put("origOrderNo", payOrder.getPayOrderId()); //商户订单号

            //请求接口
            JSONObject resData = SuixingpayUtil.req("/query/close", payOrder, mainParam, reqData);

            PayCloseRes retObj = ApiBuilder.buildSuccess(PayCloseRes.class);
            retObj.setResultCode(PayCloseRes.ResultCode.SUCCESS);
            return retObj;

        } catch (Exception e) {

            _log.error("关单异常", e);
            return ApiBuilder.bizError("关单异常");
        }
    }


}
