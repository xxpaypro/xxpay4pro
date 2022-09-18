package org.xxpay.pay.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.apache.commons.lang3.ObjectUtils;
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
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.pay.channel.BasePayment;

import java.io.File;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
@Service
public class AlipayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(AlipayPaymentService.class);

    @Autowired
    private AlipayApiService alipayApiService;

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_ALIPAY;
    }

    @Override
    public AbstractRes pay(PayOrder payOrder) {
        String channelId = payOrder.getChannelId();
        AbstractRes retObj;
        switch (channelId) {
            case PayConstant.PAY_CHANNEL_ALIPAY_MOBILE :
                retObj = doAliPayMobileReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_ALIPAY_PC :
                retObj = doAliPayPcReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_ALIPAY_WAP :
                retObj = doAliPayWapReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_ALIPAY_QR :
                retObj = doAliPayQrReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_ALIPAY_BAR :
                retObj = doAliPayBarReq(payOrder);
                break;
            case PayConstant.PAY_CHANNEL_ALIPAY_JSAPI :
                retObj = doAliPayJsApiReq(payOrder);
                break;
            default:
                retObj = ApiBuilder.bizError("不支持的支付宝渠道[channelId="+channelId+"]");
                break;
        }
        return retObj;
    }

    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        String logPrefix = "【支付宝订单查询】";
        String payOrderId = payOrder.getPayOrderId();
        String channelOrderNo = payOrder.getChannelOrderNo();
        _log.info("{}开始查询支付宝通道订单,payOrderId={}", logPrefix, payOrderId);

        JSONObject mainParam = getMainPayParam(payOrder);  // 主调起方的支付配置参数
        AlipayConfig alipayConfig = new AlipayConfig(mainParam);
        AlipayTradeQueryRequest alipay_request = new AlipayTradeQueryRequest();
        JSONObject subMchParam = getSubMchPayParam(payOrder);
        if(subMchParam != null) {
            subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
            alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
        }

        // 商户订单号，商户网站订单系统中唯一订单号，必填
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(payOrderId);
        model.setTradeNo(channelOrderNo);
        alipay_request.setBizModel(model);

        AlipayTradeQueryResponse alipay_response;
        String result = "";
        try {
            alipay_response = alipayExecute(alipayConfig, alipay_request);
            // 交易状态：
            // WAIT_BUYER_PAY（交易创建，等待买家付款）、
            // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
            // TRADE_SUCCESS（交易支付成功）、
            // TRADE_FINISHED（交易结束，不可退款）
            result = alipay_response.getTradeStatus();
            channelOrderNo = alipay_response.getTradeNo();
            _log.info("{}payOrderId={}返回结果:{}", logPrefix, payOrderId, result);

        } catch (AlipayApiException e) {
            _log.error(e, "");
        }

        if("TRADE_SUCCESS".equals(result)) {
            return QueryRetMsg.confirmSuccess(channelOrderNo);  //支付成功
        }else if("WAIT_BUYER_PAY".equals(result)) {
            return QueryRetMsg.waiting(); //支付中
        }
        return QueryRetMsg.waiting(); //支付中
    }

    @Override
    public AbstractRes close(PayOrder payOrder) {
        String logPrefix = "【支付宝关闭订单】";
        String payOrderId = payOrder.getPayOrderId();
        PayCloseRes retObj = ApiBuilder.buildSuccess(PayCloseRes.class);

        try {
            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayClient client = getAlipayClient(alipayConfig);
            AlipayTradeCloseRequest alipay_request = new AlipayTradeCloseRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            // 封装请求支付信息
            AlipayTradeCloseModel model=new AlipayTradeCloseModel();
            model.setOutTradeNo(payOrderId);
            model.setOperatorId("");

            alipay_request.setBizModel(model);
            _log.info("{}生成请求支付宝数据,req={}", logPrefix, alipay_request.getBizModel());
            AlipayTradeCloseResponse response = client.execute(alipay_request);
            _log.info("{}响应数据,subCode={},subMsg={}", logPrefix, response.getSubCode(), response.getSubMsg());

            if(response.isSuccess()){
                retObj.setResultCode(PayCloseRes.ResultCode.SUCCESS);
            } else {
                retObj.setResultCode(PayCloseRes.ResultCode.FAIL);
            }

        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("关单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("关单失败[调取通道异常]");
        }

        return retObj;
    }

    /**
     * 支付宝wap支付
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayWapReq(PayOrder payOrder) {
        String logPrefix = "【支付宝WAP支付下单】";
        String payOrderId = payOrder.getPayOrderId();
        String payUrl = null;
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayClient client = getAlipayClient(alipayConfig);
            AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            // 封装请求支付信息
            AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
            model.setOutTradeNo(payOrderId);
            model.setSubject(payOrder.getSubject());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
            model.setBody(payOrder.getBody());
            model.setProductCode("QUICK_WAP_PAY");
            // 获取objParams参数
            String objParams = payOrder.getExtra();
            if (StringUtils.isNotEmpty(objParams)) {
                try {
                    JSONObject objParamsJson = JSON.parseObject(objParams);
                    if(StringUtils.isNotBlank(objParamsJson.getString("quit_url"))) {
                        model.setQuitUrl(objParamsJson.getString("quit_url"));
                    }
                } catch (Exception e) {
                    _log.error("{}objParams参数格式错误！", logPrefix);
                }
            }
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            alipay_request.setReturnUrl(payConfig.getReturnUrl(getChannelName()));

            _log.info("{}生成请求支付宝数据,req={}", logPrefix, alipay_request.getBizModel());

            String body = client.pageExecute(alipay_request).getBody();
            //payUrl = buildWapUrl(body);
            payUrl = body;
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
        _log.info("{}生成跳转路径：payUrl={}", logPrefix, payUrl);

        if(StringUtils.isBlank(payUrl)) {
            return ApiBuilder.bizError("调用支付宝异常!");
        }
        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
        _log.info("###### 商户统一下单处理完成 ######");

        retObj.setPayOrderId(payOrderId);
        retObj.setJumpInfo(payUrl);
        return retObj;
    }

    /**
     * 支付宝手机支付
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayMobileReq(PayOrder payOrder) {
        String logPrefix = "【支付宝APP支付下单】";
        String payOrderId = payOrder.getPayOrderId();
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        String payParams = null;
        try {

            JSONObject mainParam = getMainPayParam(payOrder);  // 主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayClient client = getAlipayClient(alipayConfig);
            AlipayTradeAppPayRequest alipay_request = new AlipayTradeAppPayRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            // 封装请求支付信息
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            model.setOutTradeNo(payOrderId);
            model.setSubject(payOrder.getSubject());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
            model.setBody(payOrder.getBody());
            model.setProductCode("QUICK_MSECURITY_PAY");
            alipay_request.setBizModel(model);

            // 设置异步通知地址
            alipay_request.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            alipay_request.setReturnUrl(payConfig.getReturnUrl(getChannelName()));
            // 支付app发起使用参数
            payParams = client.sdkExecute(alipay_request).getBody();
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }

        if(StringUtils.isBlank(payParams)) {
            return ApiBuilder.bizError("调用支付宝异常!");
        }

        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
        _log.info("{}生成请求支付宝数据,payParams={}", logPrefix, payParams);
        _log.info("###### 商户统一下单处理完成 ######");
        retObj.setPayOrderId(payOrderId);
        JSONObject payParamsJSON = new JSONObject();
        payParamsJSON.put("appStr", payParams);
        retObj.setAppInfo(PayConstant.PAY_METHOD_ALIPAY_APP, payParamsJSON);
        return retObj;
    }

    /**
     * 支付宝pc支付
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayPcReq(PayOrder payOrder) {

        String logPrefix = "【支付宝PC支付下单】";
        String payOrderId = payOrder.getPayOrderId();
        String payUrl = null;
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayClient client = getAlipayClient(alipayConfig);
            AlipayTradePagePayRequest alipay_request = new AlipayTradePagePayRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            // 封装请求支付信息
            AlipayTradePagePayModel model=new AlipayTradePagePayModel();
            model.setOutTradeNo(payOrderId);
            model.setSubject(payOrder.getSubject());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
            model.setBody(payOrder.getBody());
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            // 获取objParams参数
            String objParams = payOrder.getExtra();
            String qr_pay_mode = "2";
            String qrcode_width = "200";
            if (StringUtils.isNotEmpty(objParams)) {
                try {
                    JSONObject objParamsJson = JSON.parseObject(objParams);
                    qr_pay_mode = ObjectUtils.toString(objParamsJson.getString("qr_pay_mode"), "2");
                    qrcode_width = ObjectUtils.toString(objParamsJson.getString("qrcode_width"), "200");
                } catch (Exception e) {
                    _log.error("{}objParams参数格式错误！", logPrefix);
                }
            }
            model.setQrPayMode(qr_pay_mode);
            model.setQrcodeWidth(Long.parseLong(qrcode_width));
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            alipay_request.setReturnUrl(payConfig.getReturnUrl(getChannelName()));

            _log.info("{}生成请求支付宝数据,req={}", logPrefix, alipay_request.getBizModel());

            payUrl = client.pageExecute(alipay_request).getBody();

            if(StringUtils.isBlank(payUrl)) {
                return ApiBuilder.bizError("调用支付宝异常!");
            }

        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
        _log.info("{}生成跳转路径：payUrl={}", logPrefix, payUrl);

        rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
        _log.info("###### 商户统一下单处理完成 ######");
        retObj.setPayOrderId(payOrderId);
        retObj.setJumpInfo(payUrl);
        return retObj;
    }

    /**
     * 支付宝当面付(扫码)支付
     * 收银员通过收银台或商户后台调用支付宝接口，生成二维码后，展示给用户，由用户扫描二维码完成订单支付。
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayQrReq(PayOrder payOrder) {
        String logPrefix = "【支付宝当面付之扫码支付下单】";
        String payOrderId = payOrder.getPayOrderId();
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

        try {

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayTradePrecreateRequest alipay_request = new AlipayTradePrecreateRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                alipay_request.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            // 封装请求支付信息
            AlipayTradePrecreateModel model=new AlipayTradePrecreateModel();
            model.setOutTradeNo(payOrderId);
            model.setSubject(payOrder.getSubject());
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));
            model.setBody(payOrder.getBody());
            // 获取objParams参数
            String objParams = payOrder.getExtra();
            if (StringUtils.isNotEmpty(objParams)) {
                try {
                    JSONObject objParamsJson = JSON.parseObject(objParams);
                    if(StringUtils.isNotBlank(objParamsJson.getString("discountable_amount"))) {
                        //可打折金额
                        model.setDiscountableAmount(objParamsJson.getString("discountable_amount"));
                    }
                    if(StringUtils.isNotBlank(objParamsJson.getString("undiscountable_amount"))) {
                        //不可打折金额
                        model.setUndiscountableAmount(objParamsJson.getString("undiscountable_amount"));
                    }
                } catch (Exception e) {
                    _log.error("{}objParams参数格式错误！", logPrefix);
                }
            }
            alipay_request.setBizModel(model);
            // 设置异步通知地址
            alipay_request.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            alipay_request.setReturnUrl(payConfig.getReturnUrl(getChannelName()));
            String aliResult;
            String codeUrl = "";

            aliResult = alipayExecute(alipayConfig, alipay_request).getBody();
            JSONObject aliObj = JSONObject.parseObject(aliResult);
            JSONObject aliResObj = aliObj.getJSONObject("alipay_trade_precreate_response");
            codeUrl = aliResObj.getString("qr_code");
            _log.info("{}生成支付宝二维码：codeUrl={}", logPrefix, codeUrl);
            if(StringUtils.isBlank(codeUrl)) {
                return ApiBuilder.bizError("调用支付宝异常!");
            }

            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);
            retObj.setPayOrderId(payOrderId);
            retObj.setQrInfo(codeUrl, genCodeImgUrl(codeUrl));
            _log.info("###### 商户统一下单处理完成 ######");
            return retObj;
        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
    }

    /**
     * 支付宝条码支付（被扫）
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayBarReq(PayOrder payOrder) {

        if(payOrder.getDepositMode() != null && payOrder.getDepositMode() == MchConstant.PUB_YES){ //押金模式
            return doAliPayBarReq4DepositMode(payOrder);
        }

        String logPrefix = "【支付宝条码支付】";
        String payOrderId = payOrder.getPayOrderId();

        try {

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayTradePayRequest req = new AlipayTradePayRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                req.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            AlipayTradePayModel model = new AlipayTradePayModel();

            model.setOutTradeNo(payOrderId);
            model.setScene("bar_code"); //条码支付 bar_code ; 声波支付 wave_code
            model.setAuthCode(payOrder.getExtra()); //支付授权码
            model.setSubject(payOrder.getSubject()); //订单标题
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));  //支付金额

            if(subMchParam != null) {   //服务商返佣字段
                ExtendParams extendParams = new ExtendParams();
                extendParams.setSysServiceProviderId(alipayConfig.getPid());
                model.setExtendParams(extendParams);
            }

            req.setBizModel(model);
            // 设置异步通知地址
            req.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            req.setReturnUrl(payConfig.getReturnUrl(getChannelName()));

            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);  //更新订单为支付中
            AlipayTradePayResponse alipayResp = alipayExecute(alipayConfig, req); //调起支付宝条码支付
            _log.info("调起支付宝条码支付结果：{}, code={}, msg={}, subCode={}, subMsg={}", alipayResp.isSuccess(), alipayResp.getCode(), alipayResp.getMsg(), alipayResp.getSubCode(), alipayResp.getSubMsg());

            //当条码重复发起时，支付宝返回的code = 10003, subCode = null [等待用户支付], 此时需要特殊判断 = = 。
            if("10000".equals(alipayResp.getCode()) && alipayResp.isSuccess()){ //支付成功, 等待支付宝的异步回调接口

                retObj.setOrderStatus(PayConstant.PAY_STATUS_SUCCESS);
                rpcCommonService.rpcPayOrderService.updateStatus4Success(payOrder.getPayOrderId(), alipayResp.getTradeNo()); //更新订单为成功
            }else if("10003".equals(alipayResp.getCode())){ //10003 表示为 处理中, 例如等待用户输入密码

                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING);

            }else{  //其他状态, 表示下单失败
                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);  //更新为订单失败
                return ApiBuilder.bizError("下单失败[" + (StringUtils.isEmpty(alipayResp.getSubMsg()) ? alipayResp.getMsg() : alipayResp.getSubMsg() )+ "]");
            }


            _log.info("{}生成请求支付宝数据,req={}", logPrefix, req.getBizModel());
            _log.info("###### 商户统一下单处理完成 ######");

            retObj.setPayOrderId(payOrderId);
            return retObj;

        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
    }


    /**
     * 支付宝条码支付（被扫） - 押金模式
     * @param payOrder
     * @return
     */
    public AbstractRes doAliPayBarReq4DepositMode(PayOrder payOrder) {
        String logPrefix = "【支付宝条码支付-押金模式】";
        String payOrderId = payOrder.getPayOrderId();

        try {

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);

            AlipayFundAuthOrderFreezeRequest authReq = new AlipayFundAuthOrderFreezeRequest();
            AlipayFundAuthOrderFreezeModel model = new AlipayFundAuthOrderFreezeModel();
            model.setAuthCode(payOrder.getExtra()); //支付授权码
            model.setAuthCodeType("bar_code");  //固定值bar_code
            model.setOutOrderNo(payOrderId);  //商户授权资金订单号
            model.setOutRequestNo(MySeq.getUUID());  //商户本次资金操作的请求流水号
            model.setOrderTitle("押金冻结"); //业务订单的简单描述
            model.setAmount(AmountUtil.convertCent2Dollar(payOrder.getDepositAmount())); //需要冻结的金额，单位为：元（人民币）
            model.setProductCode("PRE_AUTH");  //销售产品码，后续新接入预授权当面付的业务，本字段取值固定为PRE_AUTH。

            authReq.setBizModel(model);

            JSONObject subMchParam = getSubMchPayParam(payOrder);
            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                authReq.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            authReq.setBizModel(model);
            // 设置异步通知地址
            authReq.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            authReq.setReturnUrl(payConfig.getReturnUrl(getChannelName()));

            _log.info("r: " + authReq);

            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);  //更新订单为支付中
            AlipayFundAuthOrderFreezeResponse alipayResp = alipayExecute(alipayConfig, authReq); //调起支付宝条码支付
            _log.info("调起支付宝条码支付结果：{}, code={}, msg={}, subCode={}, subMsg={}", alipayResp.isSuccess(), alipayResp.getCode(), alipayResp.getMsg(), alipayResp.getSubCode(), alipayResp.getSubMsg());

            //当条码重复发起时，支付宝返回的code = 10003, subCode = null [等待用户支付], 此时需要特殊判断 = = 。
            if("10000".equals(alipayResp.getCode()) && alipayResp.isSuccess()){ //支付成功, 等待支付宝的异步回调接口

                retObj.setOrderStatus(PayConstant.PAY_STATUS_DEPOSIT_ING);
                rpcCommonService.rpcPayOrderService.updateStatus4depositIng(payOrder.getPayOrderId(), alipayResp.getAuthNo(),  alipayResp.getPayerUserId()); //更新订单为成功

            }else if("10003".equals(alipayResp.getCode())){ //10003 表示为 处理中, 例如等待用户输入密码

                retObj.setOrderStatus(PayConstant.PAY_STATUS_PAYING);

            }else{  //其他状态, 表示下单失败
                rpcCommonService.rpcPayOrderService.updateStatus4Fail(payOrderId);  //更新为订单失败
                return ApiBuilder.bizError("下单失败[" + (StringUtils.isEmpty(alipayResp.getSubMsg()) ? alipayResp.getMsg() : alipayResp.getSubMsg() )+ "]");
            }


            _log.info("{}生成请求支付宝数据,req={}", logPrefix, authReq.getBizModel());
            _log.info("###### 商户统一下单处理完成 ######");

            retObj.setPayOrderId(payOrderId);
            return retObj;

        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
    }

    /** 支付宝当面付 之 服务窗扫码支付 **/
    public AbstractRes doAliPayJsApiReq(PayOrder payOrder) {
        String logPrefix = "【支付宝服务窗支付】";
        String payOrderId = payOrder.getPayOrderId();

        try {

            PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);

            JSONObject mainParam = getMainPayParam(payOrder);  //主调起方的支付配置参数
            AlipayConfig alipayConfig = new AlipayConfig(mainParam);

            AlipayTradeCreateRequest req = new AlipayTradeCreateRequest();
            JSONObject subMchParam = getSubMchPayParam(payOrder);

            if(subMchParam != null) {
                subMchParam = refreshToken(alipayConfig, payOrder, subMchParam);
                req.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            AlipayTradeCreateModel model = new AlipayTradeCreateModel();

            model.setOutTradeNo(payOrderId);
            model.setSubject(payOrder.getSubject()); //订单标题
            model.setTotalAmount(AmountUtil.convertCent2Dollar(payOrder.getAmount().toString()));  //支付金额
            model.setBuyerId(payOrder.getChannelUser()); //支付宝UserId

            if(subMchParam != null) {   //服务商返佣字段
                ExtendParams extendParams = new ExtendParams();
                extendParams.setSysServiceProviderId(alipayConfig.getPid());
                model.setExtendParams(extendParams);
            }

            req.setBizModel(model);
            // 设置异步通知地址
            req.setNotifyUrl(payConfig.getNotifyUrl(getChannelName()));
            // 设置同步跳转地址
            req.setReturnUrl(payConfig.getReturnUrl(getChannelName()));

            rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrderId, null);  //更新订单为支付中
            AlipayTradeCreateResponse alipayResp = alipayExecute(alipayConfig, req); //调起支付宝条码支付
            _log.info("调起支付宝条码支付结果：{}, code={}, msg={}, subCode={}, subMsg={}", alipayResp.isSuccess(), alipayResp.getCode(), alipayResp.getMsg(), alipayResp.getSubCode(), alipayResp.getSubMsg());


            if(!alipayResp.isSuccess()){ //调用失败

                String errMsg = "调起支付宝口异常：";
                if(StringUtils.isNotEmpty(alipayResp.getMsg())) errMsg += "["+ alipayResp.getMsg()+"]";
                if(StringUtils.isNotEmpty(alipayResp.getSubMsg())) errMsg += "["+ alipayResp.getSubMsg()+"]";

                return ApiBuilder.channelError(alipayResp.getCode(), errMsg);
            }

            String alipayTradeNo = alipayResp.getTradeNo(); //支付宝交易号， 用于调起jsapi

            JSONObject payParam = new JSONObject();
            payParam.put("alipayTradeNo", alipayTradeNo);
            retObj.setPayParams(payParam);

            _log.info("{}生成请求支付宝数据,req={}", logPrefix, req.getBizModel());
            _log.info("###### 商户统一下单处理完成 ######");

            retObj.setPayOrderId(payOrderId);
            return retObj;

        } catch (AlipayApiException e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[" + e.getErrMsg() + "]");
        } catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError("下单失败[调取通道异常]");
        }
    }


    /** 获取支付宝接口调用类 **/
    private DefaultAlipayClient getAlipayClient(AlipayConfig alipayConfig) throws AlipayApiException {

        String serverUrl = XXPayUtil.getAlipayUrl4env(1, alipayConfig.getConvertJSON());

        if("cert".equals(alipayConfig.getEncryptionType())){ //证书加密方式
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl(serverUrl);
            certAlipayRequest.setAppId(alipayConfig.getAppId());
            certAlipayRequest.setPrivateKey(alipayConfig.getPrivateKey());
            certAlipayRequest.setFormat(AlipayConfig.FORMAT);
            certAlipayRequest.setCharset(AlipayConfig.CHARSET);
            certAlipayRequest.setSignType(AlipayConfig.SIGNTYPE);

            certAlipayRequest.setCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAppPublicCert());
            certAlipayRequest.setAlipayPublicCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAlipayPublicCert());
            certAlipayRequest.setRootCertPath(payConfig.getUploadIsvCertRootDir() + File.separator + alipayConfig.getAlipayRootCert());
            return new DefaultAlipayClient(certAlipayRequest);

        }else{ //key 或者 空都为默认普通加密方式

            return new DefaultAlipayClient(XXPayUtil.getAlipayUrl4env(1, alipayConfig.getConvertJSON()), alipayConfig.getAppId(),
                    alipayConfig.getPrivateKey(), AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                    alipayConfig.getAlipayPublicKey(), AlipayConfig.SIGNTYPE);
        }
    }

    /**
     * 刷新支付宝请求token
     * @param alipayConfig
     * @param payOrder
     * @param subMchParam
     * @return
     */
    JSONObject refreshToken(AlipayConfig alipayConfig, PayOrder payOrder, JSONObject subMchParam) {
        if(subMchParam == null) return subMchParam;
        Long expireTime = subMchParam.getLong("expireTimestamp"); //过期日期（13位时间戳）
        if(expireTime != null && StringUtils.isNotBlank(subMchParam.getString("refreshToken")) &&
                expireTime - System.currentTimeMillis()  < (30 * 24 * 3600 * 1000) ){ //默认30天前更新appAuthToken
            // 查询最新token并更新到数据库中
            try {
                subMchParam = alipayApiService.getOrRefreshToken(alipayConfig, null, subMchParam.getString("refreshToken"));
            } catch (AlipayApiException e) {
                _log.error(e, "");
            }
            PayPassage updateRecord = new PayPassage();
            updateRecord.setId(payOrder.getMchPassageId());
            updateRecord.setMchParam(subMchParam.toJSONString());
            rpcCommonService.rpcPayPassageService.updateById(updateRecord);
        }
        return subMchParam;
    }

    /** 封装支付宝接口调用函数 **/
    private <T extends AlipayResponse> T alipayExecute(AlipayConfig alipayConfig, AlipayRequest<T> request) throws AlipayApiException {

        if("cert".equals(alipayConfig.getEncryptionType())){ //证书加密方式
            return getAlipayClient(alipayConfig).certificateExecute(request);

        }else{ //key 或者 空都为默认普通加密方式
            return getAlipayClient(alipayConfig).execute(request);
        }
    }

}
