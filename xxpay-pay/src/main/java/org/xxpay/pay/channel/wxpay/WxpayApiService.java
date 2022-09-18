package org.xxpay.pay.channel.wxpay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayDefaultRequest;
import com.github.binarywang.wxpay.bean.request.WxPayFaceAuthInfoRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderReverseRequest;
import com.github.binarywang.wxpay.bean.result.*;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.gson.JsonObject;
import com.wechat.pay.contrib.apache.httpclient.Validator;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.CertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.HttpClient;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.*;
import org.xxpay.pay.channel.PayConfig;
import org.xxpay.pay.channel.wxpay.request.*;
import org.xxpay.pay.channel.wxpay.utils.RSAEncryptUtil;
import org.xxpay.pay.channel.wxpay.utils.WxHttpsUtil;
import org.xxpay.pay.service.RpcCommonService;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 19/09/05
 * @description: 微信支付相关接口
 */
@Service
public class WxpayApiService {


    @Autowired
    private PayConfig payConfig;

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(WxpayApiService.class);

    /**
     * 获取微信刷脸调用凭证
     * 返回给SDK刷脸支付使用
     * @param mainPayParam
     * @param subPayParam
     * @param rawdata
     * @return
     */
    public JSONObject getWxpayFaceAuthInfo(String mainPayParam, String subPayParam, String storeId, String storeName, String deviceId, String rawdata) {
        String logPrefix = "【微信刷脸获取调用凭证】";
        JSONObject resultJson = new JSONObject();
        // 获取主发起参数
        JSONObject mainParam = toDefaultJSONObject(mainPayParam);
        _log.debug("{}mainParam={}", logPrefix, mainParam);
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(mainParam.getString("mchId"));
        wxPayConfig.setAppId(mainParam.getString("appId"));
        wxPayConfig.setMchKey(mainParam.getString("key"));
        wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
        // 获取子商户参数
        JSONObject subMchParam = toJSONObject(subPayParam);
        _log.debug("{}subMchParam={}", logPrefix, subMchParam);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig); // 微信配置信息

        WxPayFaceAuthInfoRequest request = WxPayFaceAuthInfoRequest.newBuilder().build();

        if(subMchParam != null){ // 子账户发起
            request.setSubMchId(subMchParam.getString("subMchId"));
        }

        request.setStoreId(storeId);
        request.setStoreName(storeName);
        request.setDeviceId(deviceId);
        request.setAttach("");
        request.setRawdata(rawdata);
        request.setNow(System.currentTimeMillis()/1000+"");
        request.setVersion("1");

        try {
            WxPayFaceAuthInfoResult result = wxPayService.getWxPayFaceAuthInfo(request); //调起微信接口
            resultJson.put("return_code", result.getReturnCode());
            resultJson.put("return_msg", result.getReturnMsg());
            resultJson.put("authinfo", result.getAuthinfo());       // SDK调用凭证
            resultJson.put("expires_in", result.getExpiresIn());    // authinfo的有效时间, 单位秒。 例如: 3600 在有效时间内, 对于同一台终端设备，相同的参数的前提下(如：相同的公众号、商户号、 门店编号等），可以用同一个authinfo，多次调用SDK的getWxpayfaceCode接口。
            return resultJson;
        } catch (WxPayException e) {
            _log.error("{}回失败, return_code:{}, return_msg:{}", logPrefix, e.getReturnCode(), e.getReturnMsg());
        }

        return null;

    }

    /** 撤销订单接口  **/
    public boolean reverse(String mainPayParam, String subPayParam, String payOrderId) {
        String logPrefix = "【微信撤销订单】";
        _log.info("{}, payOrderId={}", logPrefix, payOrderId);
        // 获取主发起参数
        JSONObject mainParam = toDefaultJSONObject(mainPayParam);
        _log.debug("{}mainParam={}", logPrefix, mainParam);
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setMchId(mainParam.getString("mchId"));
        wxPayConfig.setAppId(mainParam.getString("appId"));
        wxPayConfig.setMchKey(mainParam.getString("key"));
        wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
        wxPayConfig.setKeyPath(payConfig.getUploadIsvCertRootDir() + File.separator + mainParam.getString("cert"));

        // 获取子商户参数
        JSONObject subMchParam = toJSONObject(subPayParam);
        _log.debug("{}subMchParam={}", logPrefix, subMchParam);
        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(wxPayConfig); // 微信配置信息

        WxPayOrderReverseRequest request = WxPayOrderReverseRequest.newBuilder().build();

        if(subMchParam != null){ // 子账户发起
            request.setSubMchId(subMchParam.getString("subMchId"));
        }
        request.setOutTradeNo(payOrderId);

        try {
            WxPayOrderReverseResult result = wxPayService.reverseOrder(request); //调起微信接口
            _log.info("{}返回报文:", result);
            return true;
        } catch (WxPayException e) {
            _log.error("{}回失败, return_code:{}, return_msg:{}", logPrefix, e.getReturnCode(), e.getReturnMsg());
            return false;
        }
    }



    /** 微信 - 押金消费
     * 消费成功后， 将订单改为[支付成功]状态， 将订单金额改为消费金额；
     * 包含支付订单表  和  商户交易表；
     * */
    public void wxDepositConsume(String mainPayParam, String subPayParam, PayOrder payOrder, Long consumeAmount) {

        String logPrefix = "【微信押金消费】";

        try {
            // 获取主发起参数
            JSONObject mainParam = toDefaultJSONObject(mainPayParam);
            _log.debug("{}mainParam={}", logPrefix, mainParam);
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));
            wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
            // 获取子商户参数
            JSONObject subMchParam = toJSONObject(subPayParam);
            _log.debug("{}subMchParam={}", logPrefix, subMchParam);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); // 微信配置信息


            WxDepositConsumeRequest request = new WxDepositConsumeRequest();
            if(subMchParam != null){ // 子账户发起
                request.setSubMchId(subMchParam.getString("subMchId"));
            }

            request.setTotalFee(payOrder.getDepositAmount().intValue()); //押金
            request.setConsumeFee(consumeAmount.intValue());  //消费金额
            request.setOutTradeNo(payOrder.getPayOrderId()); //商户订单号
            request.setFeeType("CNY");

            //签名
            request.checkAndSign(wxPayService.getConfig());
            String reqUrl = wxPayService.getPayBaseUrl() + "/deposit/consume";
            String responseContent = wxPayService.post(reqUrl, request.toXML(), false);
            WxDepositConsumeResult result = BaseWxPayResult.fromXML(responseContent, WxDepositConsumeResult.class);
            result.checkResult(wxPayService, request.getSignType(), true);

        } catch (WxPayException e) {
            _log.error("{}wxError:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_UNKNOWN_ERROR);
        }
    }

    /** 撤销订单
     *  撤销完成后订单状态改为： 订单状态为： 押金退还；
     * */
    public void wxDepositReverse(String mainPayParam, String subPayParam, String payOrderId) {

        String logPrefix = "【微信押金订单撤销】";
        try {
            // 获取主发起参数
            JSONObject mainParam = toDefaultJSONObject(mainPayParam);
            _log.debug("{}mainParam={}", logPrefix, mainParam);
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(mainParam.getString("mchId"));
            wxPayConfig.setAppId(mainParam.getString("appId"));
            wxPayConfig.setMchKey(mainParam.getString("key"));
            wxPayConfig.setSignType(WxPayConstants.SignType.MD5);
            // 获取子商户参数
            JSONObject subMchParam = toJSONObject(subPayParam);
            _log.debug("{}subMchParam={}", logPrefix, subMchParam);
            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); // 微信配置信息


            WxDepositConsumeRequest request = new WxDepositConsumeRequest();

            if(subMchParam != null){ // 子账户发起
                request.setSubMchId(subMchParam.getString("subMchId"));
            }

            request.setOutTradeNo(payOrderId); //商户订单号

            //签名
            request.checkAndSign(wxPayService.getConfig());
            String reqUrl = wxPayService.getPayBaseUrl() + "/deposit/reverse";
            String responseContent = wxPayService.post(reqUrl, request.toXML(), false);
            WxDepositConsumeResult result = BaseWxPayResult.fromXML(responseContent, WxDepositConsumeResult.class);
            result.checkResult(wxPayService, request.getSignType(), true);

        } catch (WxPayException e) {
            _log.error("{}wxError:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_UNKNOWN_ERROR);
        }
    }


    /** 微信 - 商户进件接口
     * return JSON { isSuccess: false, errMsg: '', applymentId: '123'}
     * **/
    public JSONObject wxApplymentSubmit(JSONObject isvParam, WxMchSnapshot wxMchSnapshot) {

        String logPrefix = "【微信商户进件】";
        JSONObject result = new JSONObject();

        try {

            String isvMchId = isvParam.getString("mchId");
            String isvMchKey = isvParam.getString("key");
            String mchV3Key = isvParam.getString("apiV3Key");
            String serialNo = isvParam.getString("serialNo");

            String isvCertPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("cert");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            String apiClientKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("apiClientKey");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            PrivateKey mchPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath));
            _log.debug("{}mchPrivateKey={}", logPrefix, mchPrivateKey);

            //获取微信平台证书
            JSONObject certJson = getCertificate(isvMchId, apiClientKeyPath, serialNo, mchV3Key);
            if(!certJson.getBoolean("isSuccess")){
                throw new IOException("获取微信平台证书失败");
            }

            String wechatPaySerialNo = certJson.getString("wechatPaySerialNo");
            List<X509Certificate> certificates = (List<X509Certificate>) certJson.get("certs");
            X509Certificate wxCert = certificates.get(0);

            //请求参数
            JSONObject request = new JSONObject();
            request.put("business_code", isvMchId + "_" +wxMchSnapshot.getApplyNo()); //业务申请编号, 每个申请单审核通过后会生成一个微信支付商户号。

            //超级管理员信息
            JSONObject contactInfo = new JSONObject();
            contactInfo.put("contact_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContact(), wxCert));//超级管理员姓名
            contactInfo.put("contact_id_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContactIdCardNo(), wxCert));//超级管理员身份证号
            contactInfo.put("mobile_phone", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContactPhone(), wxCert));//手机号码
            //联系人邮箱
            if(StringUtils.isNotEmpty(wxMchSnapshot.getContactEmail())){
                contactInfo.put("contact_email", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContactEmail(), wxCert));
            }
            request.put("contact_info", contactInfo);

            //主体资料
            JSONObject subjectInfo = new JSONObject();
            subjectInfo.put("subject_type", wxMchSnapshot.getOrganizationType());//主体类型

                //营业执照
                JSONObject businessLicenseInfo = new JSONObject();
                businessLicenseInfo.put("license_copy", WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getBusinessLicenseCopy()));//营业执照照片
                businessLicenseInfo.put("license_number", wxMchSnapshot.getBusinessLicenseNumber());//注册号/统一社会信用代码
                businessLicenseInfo.put("merchant_name", wxMchSnapshot.getBusinessMerchantName());//商户名称
                businessLicenseInfo.put("legal_person", wxMchSnapshot.getIdCardName());//个体户经营者/法人姓名

                subjectInfo.put("business_license_info", businessLicenseInfo);

                //经营者/法人身份证件
                JSONObject identityInfo = new JSONObject();
                identityInfo.put("id_doc_type", "IDENTIFICATION_TYPE_IDCARD");//证件类型，目前仅支持身份证
                identityInfo.put("owner", true);//经营者/法人是否为受益人

                    //身份证信息
                    JSONObject idCardInfo = new JSONObject();
                    idCardInfo.put("id_card_copy", WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getIdCardCopy()));//身份证人像面照片
                    idCardInfo.put("id_card_national", WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getIdCardNational()));//身份证国徽面照片
                    idCardInfo.put("id_card_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getIdCardName(), wxCert));//身份证姓名
                    idCardInfo.put("id_card_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getIdCardNumber(), wxCert));//身份证号码
                    idCardInfo.put("card_period_begin", wxMchSnapshot.getIdCardValidStartTime());//身份证有效期开始时间
                    idCardInfo.put("card_period_end", wxMchSnapshot.getIdCardValidEndTime());//身份证有效期结束时间

                    identityInfo.put("id_card_info", idCardInfo);
                subjectInfo.put("identity_info", identityInfo);
            request.put("subject_info", subjectInfo);

            //经营资料
            JSONObject businessInfo = new JSONObject();
            businessInfo.put("merchant_shortname", wxMchSnapshot.getMerchantShortName());//商户简称
            businessInfo.put("service_phone", wxMchSnapshot.getServicePhone());//客服电话

                //经营场景
                JSONObject salesInfo = new JSONObject();

                JSONArray salesScenesType = new JSONArray();
                salesScenesType.add(wxMchSnapshot.getSalesSceneType());
                salesInfo.put("sales_scenes_type", salesScenesType);//经营场景类型

                    //线下门店场景
                    JSONObject bizStoreInfo = new JSONObject();
                    bizStoreInfo.put("biz_store_name", wxMchSnapshot.getStoreName());//门店名称
                    bizStoreInfo.put("biz_address_code", wxMchSnapshot.getStoreAddressCode());//门店省市编码
                    bizStoreInfo.put("biz_store_address", wxMchSnapshot.getStoreStreet());//门店地址

                    JSONArray storeEntrancePic = new JSONArray();
                    storeEntrancePic.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getStoreEntrancePic()));
                    bizStoreInfo.put("store_entrance_pic", storeEntrancePic);//门店门头照片

                    JSONArray indoorPic = new JSONArray();
                    indoorPic.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getIndoorPic()));
                    bizStoreInfo.put("indoor_pic", indoorPic);//店内环境照片

                    bizStoreInfo.put("biz_sub_appid", wxMchSnapshot.getMpAppid());//线下场所对应的商家APPID

                    salesInfo.put("biz_store_info", bizStoreInfo);
                businessInfo.put("sales_info", salesInfo);
            request.put("business_info", businessInfo);

            //结算规则
            JSONObject settlementInfo = new JSONObject();
            settlementInfo.put("settlement_id", wxMchSnapshot.getSettlementId());//入驻结算规则ID
            settlementInfo.put("qualification_type", wxMchSnapshot.getQualificationType());//所属行业

            //特殊资质
            if(StringUtils.isNotEmpty(wxMchSnapshot.getQualifications())){
                String[] qualificationsList = wxMchSnapshot.getQualifications().split(",");

                JSONArray qualifications = new JSONArray();
                for (String str : qualificationsList) {
                    qualifications.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, str));
                }
                settlementInfo.put("qualifications", qualifications);
            }

            request.put("settlement_info", settlementInfo);

            //结算银行账户
            JSONObject bankAccountInfo = new JSONObject();
            bankAccountInfo.put("bank_account_type", wxMchSnapshot.getBankAccountType());//账户类型
            bankAccountInfo.put("account_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getAccountName(), wxCert));//开户名称
            bankAccountInfo.put("account_bank", wxMchSnapshot.getAccountBank());//开户银行
            bankAccountInfo.put("bank_address_code", wxMchSnapshot.getBankAddressCode());//开户银行省市编码
            bankAccountInfo.put("bank_name", wxMchSnapshot.getBankName());//开户银行全称（含支行)
            bankAccountInfo.put("account_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getAccountNumber(), wxCert));//银行账号

            request.put("bank_account_info", bankAccountInfo);

            //补充材料
            JSONObject additionInfo = new JSONObject();
            additionInfo.put("business_addition_msg", wxMchSnapshot.getBusinessAdditionDesc());//补充说明

            if(StringUtils.isNotEmpty(wxMchSnapshot.getBusinessAdditionPics())){
                JSONArray additionPics = new JSONArray();
                additionPics.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath,isvMchKey, wxMchSnapshot.getBusinessAdditionPics()));
                additionInfo.put("business_addition_pics", additionPics);//补充材料
            }
            request.put("addition_info", additionInfo);


            //进件接口
            String reqUrl = "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/";

            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Wechatpay-Serial", wechatPaySerialNo);

            StringEntity postingString = new StringEntity(request.toJSONString(), "utf-8");
            httpPost.setEntity(postingString);

            _log.info("=====请求报文====" + request.toJSONString());

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(isvMchId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath)))
                    .withWechatpay(certificates);

            CloseableHttpClient httpClient = builder.build();

            CloseableHttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            _log.info("=====response_body=====" + body);

            JSONObject res = JSON.parseObject(body);

            if (statusCode == 200) {
                String applymentId = res.getString("applyment_id");

                result.put("isSuccess", true);
                result.put("applymentId", applymentId);
            } else {
                _log.info("apply failed,resp code=" + statusCode);
                throw new IOException("request failed " + "[" + res.getString("message") + "]");
            }
        } /*catch (Exception e) {
            _log.error("{}wxError:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", e.getReturnMsg() + "|" + e.getErrCodeDes());

        }*/catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }

    /** 微信 - 查询微信审核状态
     * return JSON
     * isSuccess : 查询接口是否调用成功
     * errMsg： 查询失败 / 驳回时的返回值
     * status: 状态
     * subMchId:
     * signUrl:
     * **/
    public JSONObject wxQueryApplymentSubmit(JSONObject isvParam, WxMchSnapshot wxMchSnapshot) {

        String logPrefix = "【查询微信商户进件状态】";
        JSONObject result = new JSONObject();

        try {
//            if(true){
//                result.put("isSuccess", true);
//                Byte testStatus = MchConstant.WXAPPLY_MICRO_STATUS_OK;
//
//                result.put("applyStatus", testStatus);
//                if(testStatus == MchConstant.WXAPPLY_MICRO_STATUS_ING){  //审核中
//                    result.put("applymentStateDesc", "风险评估中");
//
//                }else if(testStatus == MchConstant.WXAPPLY_MICRO_STATUS_FAIL){  //审核失败
//                    result.put("errMsg", "微信审核失败！！");
//
//                }else if(testStatus == MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN){  //待签约
//                    result.put("subMchId", "888888");
//                    result.put("signUrl", "https://xxpayvip.oss-cn-beijing.aliyuncs.com/apply/a6b10f8f-7a6e-4860-98f4-d25ebad5ce5b.png");
//
//                }else if(testStatus == MchConstant.WXAPPLY_MICRO_STATUS_OK){  //审核通过
//                    result.put("subMchId", "888888");
//                    result.put("signUrl", "https://xxpayvip.oss-cn-beijing.aliyuncs.com/apply/a6b10f8f-7a6e-4860-98f4-d25ebad5ce5b.png");
//                }
//
//                return result;
//            }

            String isvMchId = isvParam.getString("mchId");
            String mchV3Key = isvParam.getString("apiV3Key");
            String serialNo = isvParam.getString("serialNo");

            String apiClientKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("apiClientKey");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            //获取微信平台证书
            JSONObject certJson = getCertificate(isvMchId, apiClientKeyPath, serialNo, mchV3Key);
            if(!certJson.getBoolean("isSuccess")){
                throw new IOException("获取微信平台证书失败");
            }

            String wechatPaySerialNo = certJson.getString("wechatPaySerialNo");
            List<X509Certificate> certificates = (List<X509Certificate>) certJson.get("certs");
            X509Certificate wxCert = certificates.get(0);

            String bussinessCode = isvMchId + "_" +wxMchSnapshot.getApplyNo(); //业务申请编号, 每个申请单审核通过后会生成一个微信支付商户号。

            //进件接口
            String reqUrl = "https://api.mch.weixin.qq.com/v3/applyment4sub/applyment/business_code/" + bussinessCode;

            HttpGet httpGet = new HttpGet(reqUrl);
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("Wechatpay-Serial", wechatPaySerialNo);

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(isvMchId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath)))
                    .withWechatpay(certificates);

            CloseableHttpClient httpClient = builder.build();

            CloseableHttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                _log.info("=====body=====" + body);
                JSONObject res = JSON.parseObject(body);
                String applyMentState = res.getString("applyment_state");

                if("APPLYMENT_STATE_EDITTING".equals(applyMentState)){ //编辑中
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_EDITING);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                }

                if("APPLYMENT_STATE_AUDITING".equals(applyMentState)){ //审核中
                    String singUrl = res.getString("sign_url") == null ? null : res.getString("sign_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_ING);
                    result.put("signUrl", singUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(singUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);
                }

                if("APPLYMENT_STATE_REJECTED".equals(applyMentState)){ //已驳回
                    String singUrl = res.getString("sign_url") == null ? null : res.getString("sign_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_FAIL);
                    result.put("signUrl", singUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(singUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);

                    JSONArray auditDetail = JSONArray.parseArray(res.getString("audit_detail"));
                    result.put("errMsg", auditDetail.getJSONObject(0).getString("reject_reason"));
                }

                if("APPLYMENT_STATE_TO_BE_CONFIRMED".equals(applyMentState)){ //待账户验证
                    String singUrl = res.getString("sign_url") == null ? null : res.getString("sign_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_WAIT_VERIFY);
                    result.put("signUrl", singUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(singUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);
                }

                if("APPLYMENT_STATE_TO_BE_SIGNED".equals(applyMentState)){ //待签约
                    String singUrl = res.getString("sign_url") == null ? null : res.getString("sign_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN);
                    result.put("signUrl", singUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(singUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);
                }

                if("APPLYMENT_STATE_FINISHED".equals(applyMentState)){ //完成
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_OK);
                    result.put("subMchId", res.getString("sub_mchid"));
                    result.put("signUrl", res.getString("sign_url") == null ? null : res.getString("sign_url"));
                }

                if("APPLYMENT_STATE_CANCELED".equals(applyMentState)){ //已作废
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_CANCLE);
                    result.put("errMsg", "已作废");
                }

                result.put("isSuccess", true);
            } else {
                _log.info("download failed,resp code=" + statusCode + ",body=" + body);
                throw new IOException("request failed");
            }
        } catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }


    /** 微信 - 商户升级接口
     * return JSON { isSuccess: false, errMsg: '', applymentId: '123'}
     * **/
    public JSONObject wxApplymentSubmitUpgrade(JSONObject isvParam, WxMchUpgradeSnapshot wxMch) {

        String logPrefix = "【微信商户升级】";
        JSONObject result = new JSONObject();

        try {

//            if(true){
//                result.put("isSuccess", true);
//                return result;
//            }

            String isvMchId = isvParam.getString("mchId");
            String isvMchKey = isvParam.getString("key");
            String mchV3Key = isvParam.getString("apiV3Key");
            String serialNo = isvParam.getString("serialNo");

            String apiClientKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("apiClientKey");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            PrivateKey mchPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath));

            String isvCertPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("cert");
            _log.debug("{}mainParam={}", logPrefix, isvParam);
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(isvMchId);
            wxPayConfig.setMchKey(isvMchKey);
            wxPayConfig.setKeyPath(isvCertPath);
            wxPayConfig.setSignType(WxPayConstants.SignType.HMAC_SHA256);

            //微信证书序列号
            JSONObject certJSON = getCertficatesSerialNo(wxPayConfig.getMchId(), wxPayConfig.getMchKey());
            String certNo = certJSON.getString("serial_no"); //证书序列号
            String wxCert = RSAEncryptUtil.getWXCert(certJSON, mchV3Key); //微信证书， 解密文件

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); // 微信配置信息

            WxApplymentSubmitUpgradeRequest request = new WxApplymentSubmitUpgradeRequest();
            request.setVersion("1.0"); //版本号固定为 1.0
            request.setCertSn(certNo); //平台证书序列号

            request.setSubMchId(wxMch.getSubMchId()); //小微商户号
            request.setOrganizationType(wxMch.getOrganizationType()); //主体类型 2-企业   4-个体工商户 3-党政、机关及事业单位  1708-其他组织


            request.setBusinessLicenseCopy(WxHttpsUtil.uploadFile(isvMchId, isvCertPath,isvMchKey, wxMch.getBusinessLicenseCopy()));  //营业执照扫描件 图片
            request.setBusinessLicenseNumber(wxMch.getBusinessLicenseNumber()); //营业执照注册号
            request.setMerchantName(wxMch.getMerchantName());  //商户名称
            request.setCompanyAddress(wxMch.getCompanyAddress()); //注册地址
            //request.setLegalPerson(RSAEncryptUtil.rsaEncrypt(wxMch.getLegalPerson(), wxCert)); //经营者姓名/法定代表人  需加密
            request.setBusinessTime(wxMch.getBusinessTime()); //营业期限
            request.setBusinessLicenceType(wxMch.getBusinessLicenceType());  //营业执照类型 1762-已三证合一    1763-未三证合一

            //组织结构代码证照片
            if(StringUtils.isNotEmpty(wxMch.getOrganizationCopy())){
                request.setOrganizationCopy(WxHttpsUtil.uploadFile(isvMchId, isvCertPath,isvMchKey, wxMch.getOrganizationCopy()));
            }

            request.setOrganizationNumber(wxMch.getOrganizationNumber()); //组织机构代码
            request.setOrganizationTime(wxMch.getOrganizationTime()); //组织机构代码有效期限

            //开户名称
            if(StringUtils.isNotEmpty(wxMch.getAccountName())){
                //request.setAccountName(RSAEncryptUtil.rsaEncrypt(wxMch.getAccountName(), wxCert));
            }


            request.setAccountBank(wxMch.getAccountBank());   //开户银行
            request.setBankAddressCode(wxMch.getBankAddressCode());   //开户银行省市县编码
            request.setBankName(wxMch.getBankName()); //开户银行全称 （含支行）
            //request.setAccountNumber(RSAEncryptUtil.rsaEncrypt(wxMch.getAccountNumber(), wxCert)); //银行卡号
            request.setMerchantShortname(wxMch.getMerchantShortname()); //商户简称
            request.setBusiness(wxMch.getBusiness()); //费率结算规则ID

            //特殊资质  图片组格式
            request.setQualifications(convertImgArray(wxMch.getQualifications(), isvMchId, isvCertPath, serialNo, isvMchKey));
            request.setBusinessScene(wxMch.getBusinessScene());  //经营场景1721-线下  1837-公众号  1838-小程序  1724-APP  1840-PC网站
            request.setBusinessAdditionDesc(wxMch.getBusinessAdditionDesc()); //补充说明

            //补充材料 图片组格式
            request.setBusinessAdditionPics(convertImgArray(wxMch.getBusinessAdditionPics(), isvMchId, isvCertPath, serialNo, isvMchKey));
            //request.setContactEmail(RSAEncryptUtil.rsaEncrypt(wxMch.getContactEmail(), wxCert));  //微信联系邮箱， 必填

            request.setMpAppid(wxMch.getMpAppid());  //公众号ID
            request.setMpAppScreenShots(convertImgArray(wxMch.getMpAppScreenShots(), isvMchId, isvCertPath, serialNo, isvMchKey)); //公众号页面截屏

            request.setMiniprogramAppid(wxMch.getMiniprogramAppid());  //小程序APPID
            request.setMiniprogramScreenShots(convertImgArray(wxMch.getMiniprogramAppid(), isvMchId, isvCertPath, serialNo, isvMchKey));//小程序页面截屏

            request.setAppAppid(wxMch.getAppAppid());  //应用APPID
            request.setAppScreenShots(convertImgArray(wxMch.getAppScreenShots(), isvMchId, isvCertPath, serialNo, isvMchKey));//应用页面截屏
            request.setAppDownloadUrl(wxMch.getAppDownloadUrl());  //应用下载链接

            request.setWebUrl(wxMch.getWebUrl());  //PC网站域名
            request.setWebAuthoriationLetter(WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMch.getWebAuthoriationLetter()));//网站授权函
            request.setWebAppid(wxMch.getWebAppid());  //PC网站对应的公众号APPID

            //签名
            request.checkAndSign(wxPayService.getConfig());
            String reqUrl = "https://api.mch.weixin.qq.com/applyment/micro/submitupgrade";
            String responseContent = wxPayService.post(reqUrl, request.toXML(), true);
            WxPayCommonResult wxResult = WxPayCommonResult.fromXML(responseContent, WxPayCommonResult.class);
            wxResult.checkResult(wxPayService, request.getSignType(), true);
            result.put("isSuccess", true);

        } catch (WxPayException e) {
            _log.error("{}wxError:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", e.getReturnMsg() + "|" + e.getErrCodeDes());

        }catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }

    /** 微信 - 查询微信升级审核状态
     * return JSON
     * isSuccess : 查询接口是否调用成功
     * errMsg： 查询失败 / 驳回时的返回值
     * status: 状态
     * subMchId:
     * signUrl:
     * **/
    public JSONObject wxQueryApplymentSubmitUpgrade(JSONObject isvParam, WxMchUpgradeSnapshot wxMch) {

        String logPrefix = "【查询微信升级审核状态】";
        JSONObject result = new JSONObject();

        try {

//            if(true){
//                result.put("isSuccess", true);
//                Byte testStatus = MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT;
//
//                result.put("applyStatus", testStatus);
//                if(testStatus == MchConstant.WXAPPLY_GENERAL_STATUS_ING){  //升级审核中
//                    result.put("applymentStateDesc", "升级风险评估中");
//
//                }else if(testStatus == MchConstant.WXAPPLY_GENERAL_STATUS_FAIL){  //升级失败
//                    result.put("errMsg", "微信审核失败！！");
//
//                }else if(testStatus == MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_SIGN){  //微信升级 待签约
//                    result.put("signUrl", "https://xxpayvip.oss-cn-beijing.aliyuncs.com/apply/a6b10f8f-7a6e-4860-98f4-d25ebad5ce5b.png");
//
//                }else if(testStatus == MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT){  //微信升级 ， 待商户账户验证
//
//                    String accountInfo = "付款户名:[1];" +
//                            "汇款金额:[2];" +
//                            "收款卡号:[3];" +
//                            "收款户名:[4];" +
//                            "开户银行:[5];" +
//                            "省市信息:[6];" +
//                            "备注信息:[7];" +
//                            "汇款截止时间:[8];";
//                    result.put("accountVerifyInfo", accountInfo);
//
//
//                }else if(testStatus == MchConstant.WXAPPLY_GENERAL_STATUS_OK){  //升级完成
//                }
//
//                return result;
//            }


            String isvMchId = isvParam.getString("mchId");
            String isvMchKey = isvParam.getString("key");

            String isvCertPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("cert");
            _log.debug("{}mainParam={}", logPrefix, isvParam);
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(isvMchId);
            wxPayConfig.setMchKey(isvMchKey);
            wxPayConfig.setKeyPath(isvCertPath);
            wxPayConfig.setSignType(WxPayConstants.SignType.HMAC_SHA256);

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); // 微信配置信息

            WxQueryApplymentSubmitUpgradeRequest request = new WxQueryApplymentSubmitUpgradeRequest();
            request.setVersion("1.0"); //版本号固定为 1.0
            request.setSubMchId(wxMch.getSubMchId()); //小微商户号

            //签名
            request.checkAndSign(wxPayService.getConfig());
            String reqUrl = "https://api.mch.weixin.qq.com/applyment/micro/getupgradestate";
            String responseContent = wxPayService.post(reqUrl, request.toXML(), true);
            WxPayCommonResult wxResult = WxPayCommonResult.fromXML(responseContent, WxPayCommonResult.class);
            wxResult.checkResult(wxPayService, request.getSignType(), true);
            Map resultMap = wxResult.toMap();

            String applyMentState = (String)resultMap.get("applyment_state");

            if("CHECKING".equals(applyMentState) || "AUDITING".equals(applyMentState)){ //审核中
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_ING);
                result.put("applymentStateDesc", resultMap.get("applymentStateDesc"));

            }

            if("REJECTED".equals(applyMentState)){ //已驳回
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_FAIL);
                result.put("errMsg", resultMap.get("audit_detail").toString());
            }

            if("FROZEN".equals(applyMentState)){ //已冻结
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_FAIL);
                result.put("errMsg", "已冻结");
            }

            if("NEED_SIGN".equals(applyMentState)){ //待签约
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_SIGN);
                result.put("signUrl", resultMap.get("sign_qrcode").toString());
            }
            if("ACCOUNT_NEED_VERIFY".equals(applyMentState)){ //待账号验证
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT);

                String accountInfo = "付款户名:["+result.getString("account_name")+"];" +
                        "汇款金额:["+result.getString("pay_amount")+"];" +
                        "收款卡号:["+result.getString("destination_account_number")+"];" +
                        "收款户名:["+result.getString("destination_account_name")+"];" +
                        "开户银行:["+result.getString("destination_account_bank")+"];" +
                        "省市信息:["+result.getString("city")+"];" +
                        "备注信息:["+result.getString("remark")+"];" +
                        "汇款截止时间:["+result.getString("deadline_time")+"];";
                result.put("accountVerifyInfo", accountInfo);
            }

            if("FINISH".equals(applyMentState)){ //完成
                result.put("applyStatus", MchConstant.WXAPPLY_GENERAL_STATUS_OK);
                result.put("signUrl", resultMap.get("sign_qrcode").toString());
            }

            result.put("isSuccess", true);
        } catch (WxPayException e) {
            _log.error("{}wxError:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", e.getReturnMsg() + "|" + e.getErrCodeDes());

        }catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }



    /** 微信 - 小微进件接口
     * return JSON { isSuccess: false, errMsg: '', applymentId: '123'}
     * **/
    public JSONObject wxMicroApplymentSubmit(JSONObject isvParam, WxMchSnapshot wxMchSnapshot) {

        String logPrefix = "【微信小微进件】";
        JSONObject result = new JSONObject();

        try {

            String isvMchId = isvParam.getString("mchId");
            String isvMchKey = isvParam.getString("key");
            String mchV3Key = isvParam.getString("apiV3Key");
            String serialNo = isvParam.getString("serialNo");

            String isvCertPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("cert");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            String apiClientKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("apiClientKey");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            PrivateKey mchPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath));
            _log.debug("{}mchPrivateKey={}", logPrefix, mchPrivateKey);

            //获取微信平台证书
            JSONObject certJson = getCertificate(isvMchId, apiClientKeyPath, serialNo, mchV3Key);
            if(!certJson.getBoolean("isSuccess")){
                throw new IOException("获取微信平台证书失败");
            }

            String wechatPaySerialNo = certJson.getString("wechatPaySerialNo");
            List<X509Certificate> certificates = (List<X509Certificate>) certJson.get("certs");
            X509Certificate wxCert = certificates.get(0);

            //请求参数
            JSONObject request = new JSONObject();
            request.put("out_request_no", isvMchId + "_" +wxMchSnapshot.getApplyNo()); //业务申请编号, 每个申请单审核通过后会生成一个微信支付商户号。

            //超级管理员信息
            JSONObject contactInfo = new JSONObject();
            contactInfo.put("contact_type", wxMchSnapshot.getContactType());//超级管理员类型
            contactInfo.put("contact_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContact(), wxCert));//超级管理员姓名
            contactInfo.put("contact_id_card_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getIdCardNumber(), wxCert));//超级管理员身份证号
            contactInfo.put("mobile_phone", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContactPhone(), wxCert));//手机号码
            contactInfo.put("contact_email", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getContactEmail(), wxCert));//联系人邮箱
            request.put("contact_info", contactInfo);

            //主体资料
            request.put("organization_type", wxMchSnapshot.getOrganizationType()); //主体类型
            request.put("id_doc_type", "IDENTIFICATION_TYPE_MAINLAND_IDCARD"); //经营者/法人证件类型，小微仅支持身份证

            //身份证信息
            JSONObject idCardInfo = new JSONObject();
            idCardInfo.put("id_card_copy", WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getIdCardCopy()));//身份证人像面照片
            idCardInfo.put("id_card_national", WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, wxMchSnapshot.getIdCardNational()));//身份证国徽面照片
            idCardInfo.put("id_card_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getIdCardName(), wxCert));//身份证姓名
            idCardInfo.put("id_card_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getIdCardNumber(), wxCert));//身份证号码
            idCardInfo.put("id_card_valid_time", wxMchSnapshot.getIdCardValidEndTime());//身份证有效期结束时间
            request.put("id_card_info", idCardInfo);

            request.put("need_account_info", true);//是否填写结算账户信息

            //结算银行账户
            JSONObject accountInfo = new JSONObject();
            accountInfo.put("bank_account_type", wxMchSnapshot.getBankAccountType());//账户类型
            accountInfo.put("account_name", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getAccountName(), wxCert));//开户名称
            accountInfo.put("account_bank", wxMchSnapshot.getAccountBank());//开户银行
            accountInfo.put("bank_address_code", wxMchSnapshot.getBankAddressCode());//开户银行省市编码
            accountInfo.put("bank_name", wxMchSnapshot.getBankName());//开户银行全称（含支行)
            accountInfo.put("account_number", RSAEncryptUtil.rsaEncrypt(wxMchSnapshot.getAccountNumber(), wxCert));//银行账号

            request.put("account_info", accountInfo);

            //店铺信息
            JSONObject salesSceneInfo = new JSONObject();
            salesSceneInfo.put("store_name", wxMchSnapshot.getStoreName());//门店名称
            salesSceneInfo.put("store_url", "http://www.qq.com");//店铺链接
            request.put("sales_scene_info", salesSceneInfo);

            request.put("merchant_shortname", wxMchSnapshot.getMerchantShortName());//商户简称
            request.put("business_addition_desc", wxMchSnapshot.getBusinessAdditionDesc());//补充说明

            //补充材料
            if(StringUtils.isNotEmpty(wxMchSnapshot.getBusinessAdditionPics())){
                JSONArray additionPics = new JSONArray();
                additionPics.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath,isvMchKey, wxMchSnapshot.getBusinessAdditionPics()));
                request.put("business_addition_pics", additionPics.toJSONString());
            }

            //特殊资质
            if(StringUtils.isNotEmpty(wxMchSnapshot.getQualifications())){
                String[] qualificationsList = wxMchSnapshot.getQualifications().split(",");

                JSONArray qualifications = new JSONArray();
                for (String str : qualificationsList) {
                    qualifications.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath, isvMchKey, str));
                }
                request.put("qualifications", qualifications);
            }


            //进件接口
            String reqUrl = "https://api.mch.weixin.qq.com/v3/ecommerce/applyments/";

            HttpPost httpPost = new HttpPost(reqUrl);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("Wechatpay-Serial", wechatPaySerialNo);

            StringEntity postingString = new StringEntity(request.toJSONString(), "utf-8");
            httpPost.setEntity(postingString);

            _log.info("=====请求报文====" + request.toJSONString());

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(isvMchId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath)))
                    .withWechatpay(certificates);

            CloseableHttpClient httpClient = builder.build();

            CloseableHttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            _log.info("=====response_body=====" + body);

            JSONObject res = JSON.parseObject(body);

            if (statusCode == 200) {
                String applymentId = res.getString("applyment_id");

                result.put("isSuccess", true);
                result.put("applymentId", applymentId);
            } else {
                _log.info("apply failed,resp code=" + statusCode);
                throw new IOException("request failed " + "[" + res.getString("message") + "]");
            }
        } /*catch (Exception e) {
            _log.error("{}wxError:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", e.getReturnMsg() + "|" + e.getErrCodeDes());

        }*/catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);

            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }


    /** 微信 - 查询微信小微进件审核状态
     * return JSON
     * isSuccess : 查询接口是否调用成功
     * errMsg： 查询失败 / 驳回时的返回值
     * status: 状态
     * subMchId:
     * signUrl:
     * **/
    public JSONObject wxQueryMicroApplymentSubmit(JSONObject isvParam, WxMchSnapshot wxMchSnapshot) {

        String logPrefix = "【查询微信小微进件状态】";
        JSONObject result = new JSONObject();

        try {

            String isvMchId = isvParam.getString("mchId");
            String mchV3Key = isvParam.getString("apiV3Key");
            String serialNo = isvParam.getString("serialNo");

            String apiClientKeyPath = payConfig.getUploadIsvCertRootDir() + File.separator + isvParam.getString("apiClientKey");
            _log.debug("{}mainParam={}", logPrefix, isvParam);

            //获取微信平台证书
            JSONObject certJson = getCertificate(isvMchId, apiClientKeyPath, serialNo, mchV3Key);
            if(!certJson.getBoolean("isSuccess")){
                throw new IOException("获取微信平台证书失败");
            }

            String wechatPaySerialNo = certJson.getString("wechatPaySerialNo");
            List<X509Certificate> certificates = (List<X509Certificate>) certJson.get("certs");
            X509Certificate wxCert = certificates.get(0);

            String outRequestNo = isvMchId + "_" +wxMchSnapshot.getApplyNo(); //业务申请编号, 每个申请单审核通过后会生成一个微信支付商户号。

            //进件接口
            String reqUrl = "https://api.mch.weixin.qq.com/v3/ecommerce/applyments/out-request-no/" + outRequestNo;

            HttpGet httpGet = new HttpGet(reqUrl);
            httpGet.addHeader("Content-Type", "application/json");
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("Wechatpay-Serial", wechatPaySerialNo);

            WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                    .withMerchant(isvMchId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(apiClientKeyPath)))
                    .withWechatpay(certificates);

            CloseableHttpClient httpClient = builder.build();

            CloseableHttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            if (statusCode == 200) {
                _log.info("=====body=====" + body);
                JSONObject res = JSON.parseObject(body);
                String applyMentState = res.getString("applyment_state");

                if("CHECKING".equals(applyMentState)){ //资料校验中
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_EDITING);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                }

                if("AUDITING".equals(applyMentState)){ //审核中
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_ING);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                }

                if("REJECTED".equals(applyMentState)){ //已驳回
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_FAIL);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));

                    JSONArray auditDetail = JSONArray.parseArray(res.getString("audit_detail"));
                    result.put("errMsg", auditDetail.getJSONObject(0).getString("reject_reason"));
                }

                if("ACCOUNT_NEED_VERIFY".equals(applyMentState)){ //待账户验证
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                    String legalValidationUrl = res.getString("legal_validation_url") == null ? null : res.getString("legal_validation_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_WAIT_VERIFY);
                    result.put("legalValidationUrl", legalValidationUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(legalValidationUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);
                }

                if("NEED_SIGN".equals(applyMentState)){ //待签约
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                    String singUrl = res.getString("sign_url") == null ? null : res.getString("sign_url");
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN);
                    result.put("signUrl", singUrl);
                    String imgUrl =  payConfig.getPayUrl() + "/qrcode_img_get?url=" + URLEncoder.encode(singUrl);
                    result.put("wxApplymentMchQrImg", imgUrl);
                }

                if("FINISH".equals(applyMentState)){ //完成
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_OK);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));
                    result.put("subMchId", res.getString("sub_mchid"));
                }

                if("FROZEN".equals(applyMentState)){ //已冻结
                    result.put("applyStatus", MchConstant.WXAPPLY_MICRO_STATUS_FROZEN);
                    result.put("applymentStateDesc", res.getString("applyment_state_msg"));

                    JSONArray auditDetail = JSONArray.parseArray(res.getString("audit_detail"));
                    result.put("errMsg", auditDetail.getJSONObject(0).getString("reject_reason"));
                }

                result.put("isSuccess", true);
            } else {
                _log.info("download failed,resp code=" + statusCode + ",body=" + body);
                throw new IOException("request failed");
            }
        } catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            result.put("isSuccess", false);
            result.put("errMsg", "系统异常，请检查服务商配置或请求参数, e=" + e.getMessage());
        }

        return result;
    }



    /** 微信 - 获取平台证书 **/
    private JSONObject getCertificate(String wxMchId, String wxKeyPath, String serialNo, String apiV3key) throws IOException, GeneralSecurityException {

        JSONObject result = new JSONObject();
        result.put("isSuccess", false);

        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(wxMchId, serialNo, PemUtil.loadPrivateKey(new FileInputStream(wxKeyPath)));

        //不做验签
        builder.withValidator(response -> true);

        //TODO 当前为每次获取证书
        /*if (wechatpayCertificatePath == null) {
            //不做验签
            builder.withValidator(response -> true);
        } else {
            List<X509Certificate> certs = new ArrayList<>();
            certs.add(PemUtil.loadCertificate(new FileInputStream(wechatpayCertificatePath)));
            builder.withWechatpay(certs);
        }*/

        List<X509Certificate> x509Certs = new ArrayList<>();

        HttpGet httpGet = new HttpGet("https://api.mch.weixin.qq.com/v3/certificates");
        httpGet.addHeader("Accept", "application/json");

        try (CloseableHttpClient client = builder.build()) {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body = EntityUtils.toString(response.getEntity());
            _log.info("获取证书结果：" + body);

            JSONObject object  = (JSONObject) JSON.parse(body);
            if(StringUtils.isBlank(object.getString("data"))){
                return result;
            }

            if (statusCode == 200) {
                List<CertificateItem> certList = JSONObject.parseArray(object.getString("data"), CertificateItem.class);

                AesUtil decryptor = new AesUtil(apiV3key.getBytes(StandardCharsets.UTF_8));
                for (CertificateItem item : certList) {
                    PlainCertificateItem plainCert = new PlainCertificateItem(
                            item.getSerialNo(), item.getEffectiveTime(), item.getExpireTime(),
                            decryptor.decryptToString(
                                    item.getEncryptCertificate().getAssociatedData().getBytes(StandardCharsets.UTF_8),
                                    item.getEncryptCertificate().getNonce().getBytes(StandardCharsets.UTF_8),
                                    item.getEncryptCertificate().getCiphertext()));

                    ByteArrayInputStream inputStream = new ByteArrayInputStream(plainCert.getPlainCertificate().getBytes(StandardCharsets.UTF_8));
                    X509Certificate x509Cert = PemUtil.loadCertificate(inputStream);
                    x509Certs.add(x509Cert);
                }

                //从下载的证书中，获取对报文签名的私钥所对应的证书，并进行验签来验证证书准确
                Verifier verifier = new CertificatesVerifier(x509Certs);
                Validator validator = new WechatPay2Validator(verifier);
                boolean isCorrectCert = validator.validate(response);
                _log.info(isCorrectCert ? "=== validate success ===" : "=== validate failed ===");

                if(isCorrectCert) {
                    result.put("isSuccess", true);
                    result.put("certs", x509Certs);
                    result.put("wechatPaySerialNo", certList.get(0).getSerialNo());
                }

                return result;
            } else {
                _log.info("download failed,resp code=" + statusCode);
                throw new IOException("request failed " + object.getString("message"));
            }
        }
    }

    /*public static void main(String[] args) {
        try {
            downloadCertificate("1493126272", "D:\\cert\\apiclient_key.pem", "580F998D4ACF5DA73ABA9EE8E7A4C06557A1E6B7", "9bEBe7uJr9DNa670748bc65ev8Eytiu9");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }*/

    /** 微信 - 获取获取平台证书 **/
    private JSONObject getCertficatesSerialNo(String wxMchId, String wxKey) {

        String logPrefix = "【微信-获取平台证书】";

        try {
            // 获取主发起参数
            WxPayConfig wxPayConfig = new WxPayConfig();
            wxPayConfig.setMchId(wxMchId);
            wxPayConfig.setMchKey(wxKey);
            wxPayConfig.setSignType(WxPayConstants.SignType.HMAC_SHA256);  //该接口仅支持 HMAC-SHA256

            WxPayService wxPayService = new WxPayServiceImpl();
            wxPayService.setConfig(wxPayConfig); // 微信配置信息


            WxPayDefaultRequest request = new WxPayDefaultRequest();

            //签名
            request.checkAndSign(wxPayService.getConfig());
            String reqUrl = "https://api.mch.weixin.qq.com/risk/getcertficates";
            String responseContent = wxPayService.post(reqUrl, request.toXML(), false);

            WxPayCommonResult result = WxPayCommonResult.fromXML(responseContent, WxPayCommonResult.class);
            result.checkResult(wxPayService, request.getSignType(), true);

            Map resultMap = result.toMap();
            String certificates = resultMap.get("certificates").toString();
            JSONObject certificatesJSON = JSONObject.parseObject(certificates);
            JSONObject jsonObject = (JSONObject)certificatesJSON.getJSONArray("data").get(0);
            return jsonObject;

        } catch (WxPayException e) {
            _log.error("{}wxError:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }catch (Exception e) {
            _log.error("{}error:{}", logPrefix, e);
            throw ServiceException.build(RetEnum.RET_COMM_UNKNOWN_ERROR);
        }
    }

    /**
     * 将json串转为一个json对象，如果json串为空则返回一个默认对象
     * @param json
     * @return
     */
    private JSONObject toDefaultJSONObject(String json) {
        if(StringUtils.isEmpty(json)) return new JSONObject();
        return JSONObject.parseObject(json);
    }

    /**
     * 将json串转为一个json对象，如果json串为空则返回空
     * @param json
     * @return
     */
    private JSONObject toJSONObject(String json) {
        if(StringUtils.isEmpty(json)) return null;
        return JSONObject.parseObject(json);
    }


    /** 将图片地址数组转换为 微信要求数组格式 **/
    private String convertImgArray(String imgUrlArr, String isvMchId, String isvCertPath,String serialNo, String isvMchKey) {

        if (StringUtils.isEmpty(imgUrlArr)) {
            return null;
        }

        JSONArray imgArr = JSONArray.parseArray(imgUrlArr);
        if (imgArr.isEmpty()) {
            return null;
        }

        try {
            JSONArray imgMediaIdArr = new JSONArray();
            for (Object imgUrl : imgArr) {
                imgMediaIdArr.add(WxHttpsUtil.uploadFile(isvMchId, isvCertPath,isvMchKey, imgUrl.toString()));
            }
            return imgMediaIdArr.toJSONString();
        } catch (Exception e) {
            e.printStackTrace();
            throw ServiceException.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }
}
