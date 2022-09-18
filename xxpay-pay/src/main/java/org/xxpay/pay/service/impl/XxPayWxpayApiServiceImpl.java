package org.xxpay.pay.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.IXxPayWxpayApiService;
import org.xxpay.pay.channel.wxpay.WxpayApiService;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 2019-07-06
 * @description:
 */
@Service(interfaceName = "org.xxpay.core.service.IXxPayWxpayApiService", version = Constant.XXPAY_SERVICE_VERSION, retries = Constant.DUBBO_RETRIES)
public class XxPayWxpayApiServiceImpl implements IXxPayWxpayApiService {

    @Autowired
    private WxpayApiService wxpayApiService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;


    @Override
    public JSONObject getWxpayFaceAuthInfo(Long mchId, String storeId, String storeName, String deviceId, String rawdata) {
        int productId = PayConstant.PAY_PRODUCT_WX_BAR;  // 微信付款码支付
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        String mainPayParam = payOrderService.getMainPayParam(mchInfo, productId);
        String subPayParam = payOrderService.getSubPayParam(mchInfo, productId);
        return wxpayApiService.getWxpayFaceAuthInfo(mainPayParam, subPayParam, storeId, storeName, deviceId, rawdata);
    }

    @Override
    public void wxDepositConsume(String mchTradeOrderId, Long consumeAmount) {

        int productId = PayConstant.PAY_PRODUCT_WX_BAR;  // 微信付款码支付
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchTradeOrderId);
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchTradeOrder.getMchId());
        String mainPayParam = payOrderService.getMainPayParam(mchInfo, productId);
        String subPayParam = payOrderService.getSubPayParam(mchInfo, productId);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(mchTradeOrder.getPayOrderId());

        //调起上游接口
        wxpayApiService.wxDepositConsume(mainPayParam, subPayParam, payOrder, consumeAmount);

        //处理订单状态
        rpcCommonService.rpcMchTradeOrderService.depositConsumeFinish(mchTradeOrderId, payOrder.getPayOrderId(), consumeAmount);
    }

    @Override
    public void wxDepositReverse(String mchTradeOrderId) {

        int productId = PayConstant.PAY_PRODUCT_WX_BAR;  // 微信付款码支付
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchTradeOrderId);
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchTradeOrder.getMchId());
        String mainPayParam = payOrderService.getMainPayParam(mchInfo, productId);
        String subPayParam = payOrderService.getSubPayParam(mchInfo, productId);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(mchTradeOrder.getPayOrderId());

        //调起上游接口
        wxpayApiService.wxDepositReverse(mainPayParam, subPayParam, payOrder.getPayOrderId());

        //处理订单状态
        rpcCommonService.rpcMchTradeOrderService.depositReverse(mchTradeOrderId, payOrder.getPayOrderId());
    }

    @Override
    public void wxMicroApply(WxMchSnapshot wxMchSnapshot) {

        //0. 查询最新状态 & 判断必要条件
        WxMchSnapshot dbRecord = rpcCommonService.rpcWxMchSnapshotService.findByMchAndIsv(wxMchSnapshot.getMchId(), wxMchSnapshot.getIsvId());
        if(dbRecord != null){

            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_ING){ //审核中， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN){ //待签约， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_OK){ //审核完成， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            wxMchSnapshot.setApplyNo(dbRecord.getApplyNo());  //解决saveOrUpdate主键问题；
        }

        //1.1 检查必备约束条件， 参考微信接口文档：https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
        //检查必要条件
        if(StringUtils.isAnyEmpty(wxMchSnapshot.getIdCardCopy(), wxMchSnapshot.getIdCardNational(), wxMchSnapshot.getIdCardName(),
                wxMchSnapshot.getIdCardNumber(), wxMchSnapshot.getIdCardValidStartTime(), wxMchSnapshot.getIdCardValidEndTime(), wxMchSnapshot.getAccountName(),
                wxMchSnapshot.getAccountBank(), wxMchSnapshot.getBankAddressCode(), wxMchSnapshot.getAccountNumber(),
                wxMchSnapshot.getStoreName(), wxMchSnapshot.getStoreAddressCode(), wxMchSnapshot.getStoreStreet(),
                wxMchSnapshot.getIndoorPic(), wxMchSnapshot.getMerchantShortName(), wxMchSnapshot.getServicePhone(),
                wxMchSnapshot.getSettlementId(), wxMchSnapshot.getQualificationType(), wxMchSnapshot.getContact(), wxMchSnapshot.getContactPhone())){

            throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
        }

        //1.2 查询服务商配置信息  & 检查
        JSONObject isvParamJSON = getAndCheckIsvParams(wxMchSnapshot.getIsvId());

        //2. 入库
        boolean isTrue = rpcCommonService.rpcWxMchSnapshotService.saveOrUpdate(wxMchSnapshot);
        if(!isTrue){ //保存失败！
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 调起微信进件接口
        JSONObject result = wxpayApiService.wxApplymentSubmit(isvParamJSON, wxMchSnapshot);

        WxMchSnapshot updateRecord = new WxMchSnapshot();
        updateRecord.setApplyNo(wxMchSnapshot.getApplyNo());
        if(result.getBoolean("isSuccess")){ //请求成功

            updateRecord.setApplyStatus(MchConstant.WXAPPLY_MICRO_STATUS_ING); //微信审核中
            updateRecord.setWxApplymentId(result.getString("applymentId"));

        }else{ //失败
            updateRecord.setApplyStatus(MchConstant.WXAPPLY_MICRO_STATUS_FAIL); //失败
            updateRecord.setAuditInfo(result.getString("errMsg")); //错误信息
        }
        rpcCommonService.rpcWxMchSnapshotService.updateById(updateRecord);
    }


    @Override
    public JSONObject wxQueryMicroApply(Long applyNo) {

        WxMchSnapshot wxMchSnapshot = rpcCommonService.rpcWxMchSnapshotService.getById(applyNo);
        if(wxMchSnapshot == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        if(wxMchSnapshot.getApplyStatus() != MchConstant.WXAPPLY_MICRO_STATUS_ING &&
           wxMchSnapshot.getApplyStatus() != MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN
        ){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "当前状态无需查询");
        }

        //1.2 查询服务商配置信息  & 检查
        JSONObject isvParamJSON = getAndCheckIsvParams(wxMchSnapshot.getIsvId());

        //3. 调起微信接口查询
        JSONObject result = wxpayApiService.wxQueryApplymentSubmit(isvParamJSON, wxMchSnapshot);

        if(result.getBoolean("isSuccess")){ //查询成功

            WxMchSnapshot updateRecord = new WxMchSnapshot();
            updateRecord.setApplyNo(wxMchSnapshot.getApplyNo());
            updateRecord.setApplyStatus(result.getByte("applyStatus"));
            updateRecord.setAuditInfo(result.getString("errMsg"));
            updateRecord.setWxMchId(result.getString("subMchId"));
            updateRecord.setWxApplymentMchQrImg(result.getString("wxApplymentMchQrImg"));

            rpcCommonService.rpcWxMchSnapshotService.updateById(updateRecord);

        }else{
            throw new ServiceException(RetEnum.RET_SERVICE_INTERFACE_ERROR, result.getString("errMsg"));
        }

        return result;
    }



    @Override
    public void wxMicroApplyUpgrade(WxMchUpgradeSnapshot wxInfo) {

        //0. 查询最新状态 & 判断必要条件
        WxMchUpgradeSnapshot dbRecord = rpcCommonService.rpcWxMchUpgradeSnapshotService.findByMchAndIsv(wxInfo.getMchId(), wxInfo.getIsvId());
        if(dbRecord != null){

            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_GENERAL_STATUS_ING){ //升级审核中， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_SIGN){ //待签约， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT){ //待账号验证， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_GENERAL_STATUS_OK){ //升级完成， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            wxInfo.setApplyNo(dbRecord.getApplyNo());  //解决saveOrUpdate主键问题；
        }


        //1.1 检查必备约束条件， 参考微信接口文档：https://pay.weixin.qq.com/wiki/doc/api/xiaowei.php?chapter=28_2&index=2

        //检查必要条件
        if(StringUtils.isAnyEmpty(
                wxInfo.getSubMchId(), wxInfo.getOrganizationType(),
                wxInfo.getBusinessLicenseCopy(), wxInfo.getBusinessLicenseNumber(), wxInfo.getMerchantName(),
                wxInfo.getCompanyAddress(), wxInfo.getLegalPerson(), wxInfo.getBusinessTime(),
                wxInfo.getBusinessLicenceType(), wxInfo.getMerchantShortname(), wxInfo.getBusiness(),
                wxInfo.getBusinessScene()
        )){

            throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
        }

        //未三证合一时, 组织机构必填
        if("1763".equalsIgnoreCase(wxInfo.getBusinessLicenceType())){
            if(StringUtils.isAnyEmpty(
                    wxInfo.getOrganizationCopy(), wxInfo.getOrganizationNumber(), wxInfo.getOrganizationTime()
            )){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }
        }

        //企业 | 党政机关 | 其他组织时  对公账户必填
        if("2".equalsIgnoreCase(wxInfo.getOrganizationType()) || "3".equalsIgnoreCase(wxInfo.getOrganizationType())
                || "1708".equalsIgnoreCase(wxInfo.getOrganizationType())){

            if(StringUtils.isAnyEmpty(
                    wxInfo.getAccountName(), wxInfo.getAccountBank(), wxInfo.getBankAddressCode(),
                    wxInfo.getBankName(), wxInfo.getAccountNumber()
            )){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }
        }

        JSONArray businessScene = JSONArray.parseArray(wxInfo.getBusinessScene());

        if(businessScene.isEmpty()) throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);

        //根据经营场所判断
        if(businessScene.contains("1721")){  //包含 ：1721-线下
        }

        if(businessScene.contains("1837")){  //包含 ：1837-公众号

            if(StringUtils.isAnyEmpty(wxInfo.getMpAppid())){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }
        }

        if(businessScene.contains("1838")){  //包含 ：1838-小程序
            if(StringUtils.isAnyEmpty(wxInfo.getMiniprogramAppid())){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }

        }

        if(businessScene.contains("1724")){  //包含 ：1724-APP
            if(StringUtils.isAnyEmpty(wxInfo.getAppAppid(), wxInfo.getAppScreenShots())){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }

        }

        if(businessScene.contains("1840")){  //包含 ：1840-PC网站
            if(StringUtils.isAnyEmpty(wxInfo.getWebUrl(), wxInfo.getWebAppid())){
                throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
            }
        }

        //检查 & 获取isv支付参数
        JSONObject isvParamJSON = getAndCheckIsvParams(wxInfo.getIsvId());

        //2. 入库
        boolean isTrue = rpcCommonService.rpcWxMchUpgradeSnapshotService.saveOrUpdate(wxInfo);
        if(!isTrue){ //保存失败！
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 调起微信进件接口
        JSONObject result = wxpayApiService.wxApplymentSubmitUpgrade(isvParamJSON, wxInfo);

        WxMchUpgradeSnapshot updateRecord = new WxMchUpgradeSnapshot();
        updateRecord.setApplyNo(wxInfo.getApplyNo());
        if(result.getBoolean("isSuccess")){ //请求成功

            updateRecord.setApplyStatus(MchConstant.WXAPPLY_GENERAL_STATUS_ING); //微信审核中

        }else{ //失败
            updateRecord.setApplyStatus(MchConstant.WXAPPLY_GENERAL_STATUS_FAIL); //失败
            updateRecord.setAuditInfo(result.getString("errMsg")); //错误信息
        }
        rpcCommonService.rpcWxMchUpgradeSnapshotService.updateById(updateRecord);
    }


    @Override
    public JSONObject wxQueryMicroApplyUpgrade(Long applyNo) {

        WxMchUpgradeSnapshot wxMch = rpcCommonService.rpcWxMchUpgradeSnapshotService.getById(applyNo);
        if(wxMch == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        if(wxMch.getApplyStatus() != MchConstant.WXAPPLY_GENERAL_STATUS_ING &&
                wxMch.getApplyStatus() != MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_SIGN &&
                wxMch.getApplyStatus() != MchConstant.WXAPPLY_GENERAL_STATUS_WAIT_ACCOUNT
        ){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "当前状态无需查询");
        }

        //1.2 查询服务商配置信息  & 检查
        JSONObject isvParamJSON = getAndCheckIsvParams(wxMch.getIsvId());

        //3. 调起微信接口查询
        JSONObject result = wxpayApiService.wxQueryApplymentSubmitUpgrade(isvParamJSON, wxMch);

        if(result.getBoolean("isSuccess")){ //查询成功

            WxMchUpgradeSnapshot updateRecord = new WxMchUpgradeSnapshot();
            updateRecord.setApplyNo(applyNo);
            updateRecord.setApplyStatus(result.getByte("applyStatus"));
            updateRecord.setAuditInfo(result.getString("errMsg"));
            updateRecord.setWxApplymentMchQrImg(result.getString("signUrl"));
            updateRecord.setAccountVerifyInfo(result.getString("accountVerifyInfo"));

            rpcCommonService.rpcWxMchUpgradeSnapshotService.updateById(updateRecord);

        }else{
            throw new ServiceException(RetEnum.RET_SERVICE_INTERFACE_ERROR, result.getString("errMsg"));
        }

        return result;
    }

    @Override
    public void microApply(WxMchSnapshot wxMchSnapshot) {

        //0. 查询最新状态 & 判断必要条件
        WxMchSnapshot dbRecord = rpcCommonService.rpcWxMchSnapshotService.findByMchAndIsv(wxMchSnapshot.getMchId(), wxMchSnapshot.getIsvId());
        if(dbRecord != null){

            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_ING){ //审核中， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN){ //待签约， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.WXAPPLY_MICRO_STATUS_OK){ //审核完成， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            wxMchSnapshot.setApplyNo(dbRecord.getApplyNo());  //解决saveOrUpdate主键问题；
        }

        //1.1 检查必备约束条件， 参考微信接口文档：https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
        //检查必要条件
        if(StringUtils.isAnyEmpty(wxMchSnapshot.getIdCardCopy(), wxMchSnapshot.getIdCardNational(), wxMchSnapshot.getIdCardName(),
                wxMchSnapshot.getIdCardNumber(), wxMchSnapshot.getIdCardValidEndTime(), wxMchSnapshot.getAccountName(),
                wxMchSnapshot.getAccountBank(), wxMchSnapshot.getBankAddressCode(), wxMchSnapshot.getAccountNumber(),
                wxMchSnapshot.getStoreName(), wxMchSnapshot.getStoreUrl(), wxMchSnapshot.getOrganizationType(),
                wxMchSnapshot.getMerchantShortName(), wxMchSnapshot.getContactIdCardNo(),
                wxMchSnapshot.getContact(), wxMchSnapshot.getContactPhone(), wxMchSnapshot.getContactEmail())){

            throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
        }

        //1.2 查询服务商配置信息  & 检查
        JSONObject isvParamJSON = getAndCheckIsvParams(wxMchSnapshot.getIsvId());

        //2. 入库
        boolean isTrue = rpcCommonService.rpcWxMchSnapshotService.saveOrUpdate(wxMchSnapshot);
        if(!isTrue){ //保存失败！
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 调起微信进件接口
        JSONObject result = wxpayApiService.wxMicroApplymentSubmit(isvParamJSON, wxMchSnapshot);

        WxMchSnapshot updateRecord = new WxMchSnapshot();
        updateRecord.setApplyNo(wxMchSnapshot.getApplyNo());
        if(result.getBoolean("isSuccess")){ //请求成功

            updateRecord.setApplyStatus(MchConstant.WXAPPLY_MICRO_STATUS_ING); //微信审核中
            updateRecord.setWxApplymentId(result.getString("applymentId"));

        }else{ //失败
            updateRecord.setApplyStatus(MchConstant.WXAPPLY_MICRO_STATUS_FAIL); //失败
            updateRecord.setAuditInfo(result.getString("errMsg")); //错误信息
        }
        rpcCommonService.rpcWxMchSnapshotService.updateById(updateRecord);
    }

    @Override
    public JSONObject queryMicroApply(Long applyNo) {

        WxMchSnapshot wxMchSnapshot = rpcCommonService.rpcWxMchSnapshotService.getById(applyNo);
        if(wxMchSnapshot == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        if(wxMchSnapshot.getApplyStatus() != MchConstant.WXAPPLY_MICRO_STATUS_ING &&
                wxMchSnapshot.getApplyStatus() != MchConstant.WXAPPLY_MICRO_STATUS_WAIT_SIGN
        ){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "当前状态无需查询");
        }

        //1.2 查询服务商配置信息  & 检查
        JSONObject isvParamJSON = getAndCheckIsvParams(wxMchSnapshot.getIsvId());

        //3. 调起微信接口查询
        JSONObject result = wxpayApiService.wxQueryMicroApplymentSubmit(isvParamJSON, wxMchSnapshot);

        if(result.getBoolean("isSuccess")){ //查询成功

            WxMchSnapshot updateRecord = new WxMchSnapshot();
            updateRecord.setApplyNo(wxMchSnapshot.getApplyNo());
            updateRecord.setApplyStatus(result.getByte("applyStatus"));
            updateRecord.setAuditInfo(result.getString("errMsg"));
            updateRecord.setWxMchId(result.getString("subMchId"));
            updateRecord.setWxApplymentMchQrImg(result.getString("wxApplymentMchQrImg"));

            rpcCommonService.rpcWxMchSnapshotService.updateById(updateRecord);

        }else{
            throw new ServiceException(RetEnum.RET_SERVICE_INTERFACE_ERROR, result.getString("errMsg"));
        }

        return result;
    }



    private JSONObject getAndCheckIsvParams(Long isvId){
        //1.2 查询服务商配置信息  & 检查
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByIsv(isvId, PayConstant.CHANNEL_NAME_WXPAY);
        if(payPassage == null) throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        String isvParam = payPassage.getIsvParam();
        JSONObject isvParamJSON = JSONObject.parseObject(isvParam);
        if(StringUtils.isEmpty(isvParamJSON.getString("apiV3Key"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "apiV3Key");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("cert"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "cert");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("mchId"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "mchId");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("key"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "key");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("serialNo"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "serialNo");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("apiClientKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "apiClientKey");
        }

        return isvParamJSON;
    }



}
