package org.xxpay.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.core.entity.SxfMchSnapshot;
import org.xxpay.core.service.IXxPaySxfpayApiService;
import org.xxpay.pay.channel.suixingpay.SuixingpayApiService;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 2019-07-06
 * @description:
 */
@Service(interfaceName = "org.xxpay.core.service.IXxPaySxfpayApiService", version = Constant.XXPAY_SERVICE_VERSION, retries = Constant.DUBBO_RETRIES)
public class XxPaySxfpayApiServiceImpl implements IXxPaySxfpayApiService {

    @Autowired
    private SuixingpayApiService sxfpayApiService;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Override
    public void sxfMicroApply(SxfMchSnapshot sxfMchSnapshot) {

        //1. 查询最新状态 & 判断必要条件
        SxfMchSnapshot dbRecord = rpcCommonService.rpcSxfMchSnapshotService.findByMchAndIsv(sxfMchSnapshot.getMchId(), sxfMchSnapshot.getIsvId());
        if(dbRecord != null){

            if(dbRecord.getApplyStatus() == MchConstant.SXFPAY_MCH_STATUS_ING){ //审核中， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            if(dbRecord.getApplyStatus() == MchConstant.SXFPAY_MCH_STATUS_SUCCESS){ //审核完成， 无需再次发起
                throw new ServiceException(RetEnum.RET_MGR_STATUS_ERROR);
            }
            sxfMchSnapshot.setApplyNo(dbRecord.getApplyNo());  //解决saveOrUpdate主键问题；
        }

        //2 检查必备约束条件
        if(StringUtils.isAnyEmpty(sxfMchSnapshot.getMecDisNm(), sxfMchSnapshot.getMblNo(), sxfMchSnapshot.getOperationalType(),
                sxfMchSnapshot.getHaveLicenseNo(), sxfMchSnapshot.getMecTypeFlag(), sxfMchSnapshot.getCprRegAddr(),
                sxfMchSnapshot.getRegProvCd(), sxfMchSnapshot.getRegCityCd(), sxfMchSnapshot.getRegDistCd(), sxfMchSnapshot.getMccCd(),
                sxfMchSnapshot.getStmManIdNo(), sxfMchSnapshot.getIdentityName(), sxfMchSnapshot.getCsTelNo(), sxfMchSnapshot.getIdentityNo(),
                sxfMchSnapshot.getQrcodeList(), sxfMchSnapshot.getOnlineType(), sxfMchSnapshot.getLicenseMatch(), sxfMchSnapshot.getLbnkNo(),
                sxfMchSnapshot.getLicensePic())){

            throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
        }

        //3 查询服务商配置信息 & 检查
        JSONObject paramJSON = getAndCheckIsvParams(sxfMchSnapshot.getIsvId(), sxfMchSnapshot.getMchId());

        //4 入库
        boolean isTrue = rpcCommonService.rpcSxfMchSnapshotService.saveOrUpdate(sxfMchSnapshot);
        if(!isTrue){ //保存失败！
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //5 调起随行付进件接口
        JSONObject result = sxfpayApiService.sxfApplymentSubmit(sxfMchSnapshot, paramJSON);
        if (result == null) {
            throw new ServiceException(RetEnum.RET_ISV_INPUT_PARAMS_ERROR);
        }
        SxfMchSnapshot sxfMchSnapshotUpdate = new SxfMchSnapshot();
        sxfMchSnapshotUpdate.setApplyNo(sxfMchSnapshot.getApplyNo());
        if("0000".equals(result.getString("bizCode"))){ //请求成功

            sxfMchSnapshotUpdate.setApplyStatus(MchConstant.SXFPAY_MCH_STATUS_ING); //微信审核中
            sxfMchSnapshotUpdate.setApplicationId(result.getString("applicationId"));
        }else{ //请求失败
            sxfMchSnapshotUpdate.setApplyStatus(MchConstant.SXFPAY_MCH_STATUS_FAIL); //失败
            sxfMchSnapshotUpdate.setAuditInfo(result.getString("bizMsg")); //错误信息
        }
        rpcCommonService.rpcSxfMchSnapshotService.updateById(sxfMchSnapshotUpdate);
    }


    @Override
    public JSONObject sxfQueryMicroApply(Long applyNo) {

        SxfMchSnapshot sxfMchSnapshot = rpcCommonService.rpcSxfMchSnapshotService.getById(applyNo);
        if(sxfMchSnapshot == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        if(sxfMchSnapshot.getApplyStatus() == MchConstant.SXFPAY_MCH_STATUS_SUCCESS){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "当前状态无需查询");
        }

        //1 查询服务商配置信息  & 检查
        JSONObject paramJSON = getAndCheckIsvParams(sxfMchSnapshot.getIsvId(), sxfMchSnapshot.getMchId());

        //2 调起随行付接口查询
        JSONObject result = sxfpayApiService.sxfQueryMerchantInfo(sxfMchSnapshot, paramJSON);

        if("0000".equals(result.getString("bizCode"))){ //查询成功
            SxfMchSnapshot updateSnapshot = new SxfMchSnapshot();
            if (result.getByte("taskStatus") != null && StringUtils.isNotEmpty(result.getString("suggestion"))) {
                updateSnapshot.setApplyNo(sxfMchSnapshot.getApplyNo());
                updateSnapshot.setApplyStatus(result.getByte("taskStatus"));
                updateSnapshot.setAuditInfo(result.getString("suggestion"));
                rpcCommonService.rpcSxfMchSnapshotService.updateById(updateSnapshot);
            }else {

            }
        }else{
            throw new ServiceException(RetEnum.RET_SERVICE_INTERFACE_ERROR, result.getString("bizMsg"));
        }

        return result;
    }

    private JSONObject getAndCheckIsvParams(Long isvId, Long mchId){
        // 查询服务商 & 检查
        PayPassage payPassageIsv = rpcCommonService.rpcPayPassageService.selectByIsv(isvId, PayConstant.CHANNEL_NAME_SUIXINGPAY);
        if(payPassageIsv == null) throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        String isvParam = payPassageIsv.getIsvParam();
        JSONObject isvParamJSON = JSONObject.parseObject(isvParam);
        JSONObject jsonConfig = new JSONObject();
        // 机构ID
        if(StringUtils.isEmpty(isvParamJSON.getString("orgId"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "orgId");
        }
        // 商户私钥
        if(StringUtils.isEmpty(isvParamJSON.getString("privateKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "privateKey");
        }
        // 随行付公钥
        if(StringUtils.isEmpty(isvParamJSON.getString("sxfPublic"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "sxfPublic");
        }
        jsonConfig.put("orgId", isvParamJSON.getString("orgId"));
        jsonConfig.put("privateKey", isvParamJSON.getString("privateKey"));
        jsonConfig.put("sxfPublic", isvParamJSON.getString("sxfPublic"));
        return jsonConfig;
    }



}
