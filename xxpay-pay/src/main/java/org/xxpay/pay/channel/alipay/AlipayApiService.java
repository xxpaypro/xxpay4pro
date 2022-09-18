package org.xxpay.pay.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.*;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AlipayMchSnapshot;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.pay.channel.PayConfig;
import org.xxpay.pay.service.RpcCommonService;

import java.io.File;
import java.net.URL;

/**
 * @author: dingzhiwei
 * @date: 19/09/04
 * @description: 支付宝相关接口
 */
@Service
public class AlipayApiService {

    private static final MyLog _log = MyLog.getLog(AlipayApiService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private PayConfig payConfig;

    /**
     * 刷脸初始化
     * @param payParam
     * @param metaInfo
     * @return
     */
    public JSONObject authenticationSmilepayInitialize(String payParam, String metaInfo) {

        AlipayConfig alipayConfig = new AlipayConfig(payParam);

        ZolozAuthenticationCustomerSmilepayInitializeRequest req
                = new ZolozAuthenticationCustomerSmilepayInitializeRequest();

        req.setBizContent(metaInfo);
        try {
            ZolozAuthenticationCustomerSmilepayInitializeResponse resp = alipayExecute(alipayConfig, req);
            if(resp.isSuccess()) {
                String result = resp.getResult();
                JSONObject resultJson = JSON.parseObject(result);
                return resultJson;
            }
        } catch (AlipayApiException e) {
            _log.error(e, "");
        } catch (Exception e) {
            _log.error(e, "");
        }

        return null;
    }


    /** 押金消费接口 **/
    public void depositConsume(PayOrder payOrder, Long consumeAmount){

        JSONObject mainParam = getMainPayParam(payOrder);
        JSONObject subMchParam = getSubMchPayParam(payOrder);

        //押金消费
        String logPrefix = "【支付宝押金 - 消费】";
        try {

            AlipayConfig alipayConfig = new AlipayConfig(mainParam);

            AlipayTradePayRequest payReq = new AlipayTradePayRequest();
            AlipayTradePayModel model = new AlipayTradePayModel();
            model.setOutTradeNo(payOrder.getPayOrderId());  //商户订单号，需要保证不重复
            model.setTotalAmount(AmountUtil.convertCent2Dollar(consumeAmount)); //订单金额, 单位元
            model.setProductCode("PRE_AUTH"); //产品码，当面授权场景传固定值PRE_AUTH
            model.setAuthNo(payOrder.getChannelOrderNo());  //支付宝资金授权单号
            model.setSubject(payOrder.getSubject()); //订单标题
            model.setBuyerId(payOrder.getChannelUser()); //买家支付宝uid，即资金授权用户uid，必传
            model.setSellerId(alipayConfig.getPid());    //卖家支付宝uid，即资金授权收款方uid，必传
            model.setAuthConfirmMode("COMPLETE"); //预授权转交易时结束预授权（并解冻剩余资金）时需要传入COMPLETE 或NOT_COMPLETE

            if(subMchParam != null) {
                payReq.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
                ExtendParams extendParams = new ExtendParams();
                extendParams.setSysServiceProviderId(alipayConfig.getPid());
                model.setExtendParams(extendParams);
            }

            payReq.setBizModel(model);

            AlipayTradePayResponse alipayResp = alipayExecute(alipayConfig, payReq); //调起支付宝条码支付
            _log.info("调起支付宝条码支付结果：{}, code={}, msg={}, subCode={}, subMsg={}", alipayResp.isSuccess(), alipayResp.getCode(), alipayResp.getMsg(), alipayResp.getSubCode(), alipayResp.getSubMsg());

            if(!alipayResp.isSuccess()){ //支付成功, 等待支付宝的异步回调接口
                throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }

        } catch (AlipayApiException e) {
            _log.error(e, "");
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (Exception e) {
            _log.error(e, "");
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 押金退还接口 **/
    public void depositReverse(JSONObject mainParam,JSONObject subMchParam, Long amount, String channelOrderId){
        String logPrefix = "【支付宝押金退还】";

        try {

            AlipayConfig alipayConfig = new AlipayConfig(mainParam);
            AlipayFundAuthOrderUnfreezeRequest authReq = new AlipayFundAuthOrderUnfreezeRequest();
            AlipayFundAuthOrderUnfreezeModel model = new AlipayFundAuthOrderUnfreezeModel();
            model.setAuthNo(channelOrderId); //支付宝资金授权订单号
            model.setOutRequestNo(MySeq.getUUID());  //商户本次资金操作的请求流水号，同一商户每次不同的资金操作请求，商户请求流水号不能重复
            model.setAmount(AmountUtil.convertCent2Dollar(amount));  //本次操作解冻的金额，单位为：元（人民币），精确到小数点后两位，取值范围：[0.01,100000000.00]
            model.setRemark("解除冻结金额");  //商户对本次解冻操作的附言描述

            authReq.setBizModel(model);
            if(subMchParam != null) {
                authReq.putOtherTextParam("app_auth_token", subMchParam.getString("appAuthToken"));
            }

            authReq.setBizModel(model);

            AlipayFundAuthOrderUnfreezeResponse alipayResp = alipayExecute(alipayConfig, authReq); //调起支付宝条码支付
            _log.info("调起支付宝条码支付结果：{}, code={}, msg={}, subCode={}, subMsg={}", alipayResp.isSuccess(), alipayResp.getCode(), alipayResp.getMsg(), alipayResp.getSubCode(), alipayResp.getSubMsg());

            if(!alipayResp.isSuccess()){ //支付成功, 等待支付宝的异步回调接口
                throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }

        } catch (AlipayApiException e) {
            _log.error(e, "");
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (Exception e) {
            _log.error(e, "");
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 支付宝进件接口 **/
    public AlipayMchSnapshot alipayApplymentSubmit(JSONObject isvParam, AlipayMchSnapshot mch){

        AlipayMchSnapshot result = new AlipayMchSnapshot();

        try {

            AlipayConfig alipayConfig = new AlipayConfig(isvParam);

            //1. 开启事务
            AlipayOpenAgentCreateRequest openRequest = new  AlipayOpenAgentCreateRequest();
            AlipayOpenAgentCreateModel openModel = new AlipayOpenAgentCreateModel();
            openModel.setAccount(mch.getAlipayAccount());  //商户支付宝账号

            ContactModel contactModel = new ContactModel();
            contactModel.setContactName(mch.getContactName());  //联系人名称
            contactModel.setContactMobile(mch.getContactMobile());  //联系人手机号码
            contactModel.setContactEmail(mch.getContactEmail());  //联系人邮箱
            openModel.setContactInfo(contactModel);
            openModel.setAccount(mch.getAlipayAccount());  //商户支付宝账号

            openRequest.setBizModel(openModel);
            AlipayOpenAgentCreateResponse openResponse = alipayExecute(alipayConfig, openRequest);
            String batchNo = openResponse.getBatchNo();

            _log.error("req:" + openRequest.getBizContent());
            _log.error("(1)开启代商户签约，开启事务:msg={}, code={}" + openResponse.getMsg(), openResponse.getCode());

            if(!openResponse.isSuccess()){
                throw new Exception("事务开启失败[" + openResponse.getCode() + "|" + openResponse.getSubCode()
                                     + "|" + openResponse.getMsg() + "|" + openResponse.getSubMsg() + "]");
            }

            if(!"init".equalsIgnoreCase(openResponse.getBatchStatus())){ //只有init初始状态才可进行事务的提交
                throw new Exception("batchStatus状态不等于init, 提交失败！");
            }

            AlipayOpenAgentFacetofaceSignRequest offRequest = new AlipayOpenAgentFacetofaceSignRequest();
            offRequest.setBatchNo(batchNo); //代商户操作事务编号，通过alipay.open.agent.create接口进行创建。
            offRequest.setMccCode(mch.getMccCode()); //所属MCCCode

            //企业特殊资质图片
            if(StringUtils.isNotEmpty(mch.getSpecialLicensePic())){

                String specialLicensePic = mch.getSpecialLicensePic();

                //获取文件名称
                String imgFileName = specialLicensePic.substring(specialLicensePic.lastIndexOf("/") + 1);

                //下载文件 & 转换为byte数组格式
                byte[] imgFileByteArray = IOUtils.toByteArray(new URL(specialLicensePic));

                offRequest.setSpecialLicensePic(new FileItem(imgFileName, imgFileByteArray));
            }


            offRequest.setRate(mch.getRate()); //费率 0.38 - 3之间

            //营业执照编号
            if (StringUtils.isNotEmpty(mch.getBusinessLicenseNo())){
                offRequest.setBusinessLicenseNo(mch.getBusinessLicenseNo());
            }

            //营业执照图片
            if(StringUtils.isNotEmpty(mch.getBusinessLicensePic())){

                String bBusinessLicensePic = mch.getBusinessLicensePic();

                //获取文件名称
                String imgFileName = bBusinessLicensePic.substring(bBusinessLicensePic.lastIndexOf("/") + 1);

                //下载文件 & 转换为byte数组格式
                byte[] imgFileByteArray = IOUtils.toByteArray(new URL(bBusinessLicensePic));

                offRequest.setSpecialLicensePic(new FileItem(imgFileName, imgFileByteArray));
            }

            //营业执照是否长期有效
            if (mch.getLongTerm() != null) {
                offRequest.setLongTerm(MchConstant.PUB_YES == mch.getLongTerm());
            }

            //营业期限
            if (StringUtils.isNotEmpty(mch.getDateLimitation())) {
                offRequest.setDateLimitation(mch.getDateLimitation());
            }

            //门头照
            if(StringUtils.isNotEmpty(mch.getShopSignBoardPic())){
                String shopSignBoardPic = mch.getShopSignBoardPic();

                //获取文件名称
                String imgFileName = shopSignBoardPic.substring(shopSignBoardPic.lastIndexOf("/") + 1);

                //下载文件 & 转换为byte数组格式
                byte[] imgFileByteArray = IOUtils.toByteArray(new URL(shopSignBoardPic));

                offRequest.setShopSignBoardPic(new FileItem(imgFileName, imgFileByteArray));
            }

            AlipayOpenAgentFacetofaceSignResponse offResponse = alipayExecute(alipayConfig, offRequest);
            _log.error("(2)代签约当面付产品:msg={}, code={}" + openResponse.getMsg(), openResponse.getCode());
            if(!offResponse.isSuccess()){ //请求失败
                throw new Exception("发起待签约失败[" + offResponse.getCode() + "|" + offResponse.getSubCode()
                        + "|" + offResponse.getMsg() + "|" + offResponse.getSubMsg() + "]");
            }

            AlipayOpenAgentConfirmRequest confirmRequest = new AlipayOpenAgentConfirmRequest();
            AlipayOpenAgentConfirmModel confirmModel = new AlipayOpenAgentConfirmModel();
            confirmModel.setBatchNo(batchNo);
            confirmRequest.setBizModel(confirmModel);
            AlipayOpenAgentConfirmResponse confirmResponse = alipayExecute(alipayConfig, confirmRequest);
            _log.error("(3)提交待签约，创建应用事务msg={}, code={}" + openResponse.getMsg(), openResponse.getCode());
            if(!confirmResponse.isSuccess()){
                throw new Exception("提交事务失败[" + confirmResponse.getCode() + "|" + confirmResponse.getSubCode()
                        + "|" + confirmResponse.getMsg() + "|" + confirmResponse.getSubMsg() + "]");
            }

            result.setBatchNo(batchNo);
            result.setUserId(confirmResponse.getUserId());
            result.setAuthAppId(confirmResponse.getAuthAppId());
            result.setAppAuthToken(confirmResponse.getAppAuthToken());
            result.setAppRefreshToken(confirmResponse.getAppRefreshToken());
            result.setExpiresIn(confirmResponse.getExpiresIn());
            result.setReExpiresIn(confirmResponse.getReExpiresIn());

            result.setPsVal("isSuccess", true);

        } catch (AlipayApiException e) {
            _log.error("支付宝进件异常", e);
            result.setPsVal("isSuccess", false);
            result.setAuditInfo("进件失败[errCode="+e.getErrCode()+"][errMsg="+e.getErrMsg()+"]");

        } catch (Exception e) {
            _log.error("支付宝进件系统异常", e);
            result.setPsVal("isSuccess", false);
            result.setAuditInfo(e.getMessage());
        }

        return result;


    }

    /** 支付宝进件接口 - 查询 **/
    public AlipayMchSnapshot queryApplymentSubmit(JSONObject isvParam, AlipayMchSnapshot mch){

        AlipayMchSnapshot result = new AlipayMchSnapshot();

        try {

            AlipayConfig alipayConfig = new AlipayConfig(isvParam);

            //1. 开启事务
            AlipayOpenAgentOrderQueryRequest queryRequest = new  AlipayOpenAgentOrderQueryRequest();
//            AlipayOpenAgentOrderQueryModel queryModel = new AlipayOpenAgentOrderQueryModel();
//            queryModel.setBatchNo(mch.getBatchNo());
            JSONObject json = new JSONObject();
            json.put("batch_no", mch.getBatchNo());
            queryRequest.setBizContent(json.toJSONString());

            AlipayOpenAgentOrderQueryResponse queryResponse = alipayExecute(alipayConfig, queryRequest);

            if(!queryResponse.isSuccess()){
                throw new Exception("接口查询失败[" + queryResponse.getCode() + "|" + queryResponse.getSubCode()
                        + "|" + queryResponse.getMsg() + "|" + queryResponse.getSubCode() + "]");

            }

            //MERCHANT_INFO_HOLD=暂存，提交事务出现业务校验异常时，会暂存申请单信息，可以调用业务接口修正参数，并重新提交
            //MERCHANT_AUDITING=审核中，申请信息正在人工审核中
            //MERCHANT_CONFIRM=待商户确认，申请信息审核通过，等待联系人确认签约或授权
            //MERCHANT_CONFIRM_SUCCESS=商户确认成功，商户同意签约或授权
            //MERCHANT_CONFIRM_TIME_OUT=商户超时未确认，如果商户受到确认信息15天内未确认，则需要重新提交申请信息
            //MERCHANT_APPLY_ORDER_CANCELED=审核失败或商户拒绝，申请信息审核被驳回，或者商户选择拒绝签约或授权
            String queryStatus = queryResponse.getOrderStatus();
            if("MERCHANT_INFO_HOLD".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_HOLD);
            }else if("MERCHANT_AUDITING".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_ING);
            }else if("MERCHANT_CONFIRM".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_WAIT_SIGN);
            }else if("MERCHANT_CONFIRM_SUCCESS".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_SUCCESS);
            }else if("MERCHANT_CONFIRM_TIME_OUT".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_TIME_OUT);
            }else if("MERCHANT_APPLY_ORDER_CANCELED".equalsIgnoreCase(queryStatus)){
                result.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_MCH_CANCELED);
            }

            result.setConfirmUrl(queryResponse.getConfirmUrl());
            result.setAgentAppId(queryResponse.getAgentAppId());
            result.setAuditInfo(queryResponse.getRejectReason());
            result.setMerchantPid(queryResponse.getMerchantPid());
            result.setPsVal("isSuccess", true);

        } catch (AlipayApiException e) {
            _log.error("支付宝进件查询异常", e);
            result.setPsVal("isSuccess", false);
            result.setAuditInfo("进件失败[errCode="+e.getErrCode()+"][errMsg="+e.getErrMsg()+"]");

        } catch (Exception e) {
            _log.error("支付宝进件查询系统异常", e);
            result.setPsVal("isSuccess", false);
            result.setAuditInfo(e.getMessage());
        }

        return result;


    }




    /**
     * 通过code换取token 或者刷新token
     * @param alipayConfig  支付宝发起方配置参数
     * @param appAuthCode appAuthCode  [与refreshToken二选一]
     * @param refreshToken 刷新Token   [与appAuthCode二选一]
     * @return
     * @throws AlipayApiException
     */
    public JSONObject getOrRefreshToken(AlipayConfig alipayConfig, String appAuthCode, String refreshToken) throws AlipayApiException {

        //通过appAuthCode / refreshToken  换取 appAuthToken
        AlipayOpenAuthTokenAppRequest request = new AlipayOpenAuthTokenAppRequest();
        if(StringUtils.isNotEmpty(appAuthCode)){  //通过 appAuthCode
            request.setBizContent("{" +
                    "    \"grant_type\":\"authorization_code\"," +
                    "    \"code\":\""+appAuthCode+"\"" +
                    "  }");
        }else{                                     //通过 refreshToken
            request.setBizContent("{" +
                    "    \"grant_type\":\"refresh_token\"," +
                    "    \"refresh_token\":\""+refreshToken+"\"" +
                    "  }");
        }

        AlipayOpenAuthTokenAppResponse tokenResponse = alipayExecute(alipayConfig, request);
        String appAuthToken = tokenResponse.getAppAuthToken();  //appAuthToken
        String expiresIn = tokenResponse.getExpiresIn(); //令牌有效期，单位：秒
        String appRefreshToken = tokenResponse.getAppRefreshToken();  //刷新token

        JSONObject mchParam = new JSONObject();
        mchParam.put("appAuthToken", appAuthToken);  //appAuthToken
        mchParam.put("refreshToken", appRefreshToken);  //刷新token
        mchParam.put("expireTimestamp", System.currentTimeMillis() + (Long.parseLong(expiresIn) * 1000)); //13位时间戳格式

        return mchParam;
    }


    /**
     * 根据用户authCode 换取 支付宝userID
     * @param alipayConfig
     * @param authCode
     * @return
     */
    public String getUserId(AlipayConfig alipayConfig, String authCode) {

        //通过code 换取openId
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(authCode);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayExecute(alipayConfig, request);
            return oauthTokenResponse.getUserId();

        } catch (Exception e) {
            _log.error("异常", e);
            return null;
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

    /** 封装支付宝接口调用函数 **/
    private <T extends AlipayResponse> T alipayExecute(AlipayConfig alipayConfig, AlipayRequest<T> request) throws AlipayApiException {

        if("cert".equals(alipayConfig.getEncryptionType())){ //证书加密方式
            return getAlipayClient(alipayConfig).certificateExecute(request);

        }else{ //key 或者 空都为默认普通加密方式
            return getAlipayClient(alipayConfig).execute(request);
        }
    }



    /** 获取接口主发起方的支付参数信息 **/
    public JSONObject getMainPayParam(PayOrder payOrder) {

        if(MchConstant.MCH_TYPE_PRIVATE == payOrder.getMchType()){ //私有商户获取
            PayPassage mchPassage = rpcCommonService.rpcPayPassageService.getById(payOrder.getMchPassageId());
            return JSONObject.parseObject(mchPassage.getMchParam());
        }else{ //获取服务商通道
            PayPassage isvPassage = rpcCommonService.rpcPayPassageService.getById(payOrder.getIsvPassageId());
            return JSONObject.parseObject(isvPassage.getIsvParam());

        }
    }

    /** 获取子商户的支付参数信息 **/
    public JSONObject getSubMchPayParam(PayOrder payOrder) {
        if(MchConstant.MCH_TYPE_PRIVATE == payOrder.getMchType()){ //私有商户 没有子商户配置信息
            return null;
        }else{
            PayPassage mchPassage = rpcCommonService.rpcPayPassageService.getById(payOrder.getMchPassageId());
            return JSONObject.parseObject(mchPassage.getMchParam());
        }
    }

}
