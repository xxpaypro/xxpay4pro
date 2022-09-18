package org.xxpay.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.IXxPayAlipayApiService;
import org.xxpay.pay.channel.PayConfig;
import org.xxpay.pay.channel.alipay.AlipayApiService;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;

/**
 * @author: dingzhiwei
 * @date: 2019-07-06
 * @description:
 */
@Service(interfaceName = "org.xxpay.core.service.IXxPayAlipayApiService", version = Constant.XXPAY_SERVICE_VERSION, retries = Constant.DUBBO_RETRIES)
public class XxPayAlipayApiServiceImpl implements IXxPayAlipayApiService {

    @Autowired
    private AlipayApiService alipayApiService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    public PayConfig payConfig;

    @Override
    public JSONObject authenticationSmilepayInitialize(Long mchId, String metaInfo) {
        int productId = PayConstant.PAY_PRODUCT_ALIPAY_BAR;  // 支付宝条码支付
        String alipayParam = payOrderService.getSubPayParam(mchId, productId);
        return alipayApiService.authenticationSmilepayInitialize(alipayParam, metaInfo);
    }


    @Override
    public void depositConsume(String mchTradeOrderId, Long consumeAmount) {

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchTradeOrderId);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(mchTradeOrder.getPayOrderId());

        //调起上游接口
        alipayApiService.depositConsume(payOrder, consumeAmount);

        //处理订单状态
        rpcCommonService.rpcMchTradeOrderService.depositConsumeFinish(mchTradeOrderId, payOrder.getPayOrderId(), consumeAmount);
    }

    @Override
    public void depositReverse(String mchTradeOrderId) {

        int productId = PayConstant.PAY_PRODUCT_ALIPAY_BAR;  // 支付宝付款码支付
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(mchTradeOrderId);
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchTradeOrder.getMchId());
        String mainPayParam = payOrderService.getMainPayParam(mchInfo, productId);
        String subPayParam = payOrderService.getSubPayParam(mchInfo, productId);
        PayOrder payOrder = rpcCommonService.rpcPayOrderService.findByPayOrderId(mchTradeOrder.getPayOrderId());

        //调起上游接口
        alipayApiService.depositReverse(JSON.parseObject(mainPayParam), JSON.parseObject(subPayParam),
                                    payOrder.getDepositAmount(), payOrder.getChannelOrderNo());

        //处理订单状态
        rpcCommonService.rpcMchTradeOrderService.depositReverse(mchTradeOrderId, payOrder.getPayOrderId());
    }


    @Override
    public void mchApply(AlipayMchSnapshot alipayMchSnapshot) {


        //1.1 检查必备约束条件， 参考支付宝接口文档：https://docs.open.alipay.com/api_50/alipay.open.agent.offlinepayment.sign/

        //检查必要条件
        if(StringUtils.isAnyEmpty(alipayMchSnapshot.getMccCode(), alipayMchSnapshot.getRate(), alipayMchSnapshot.getShopSignBoardPic(),
                alipayMchSnapshot.getAlipayAccount(), alipayMchSnapshot.getContactName(), alipayMchSnapshot.getContactMobile(),
                alipayMchSnapshot.getContactEmail())
        ){
            throw new ServiceException(RetEnum.RET_COMM_PARAM_NOT_FOUND);
        }

        //1.2 查询服务商配置信息  & 检查
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByIsv(alipayMchSnapshot.getIsvId(), PayConstant.CHANNEL_NAME_ALIPAY);
        if(payPassage == null) throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        String isvParam = payPassage.getIsvParam();
        JSONObject isvParamJSON = JSONObject.parseObject(isvParam);
        if(isvParamJSON.isEmpty()){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR);
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("pid"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "pid");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("privateKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "privateKey");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("alipayPublicKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "alipayPublicKey");
        }

        //2. 入库
        boolean isTrue = rpcCommonService.rpcAlipayMchSnapshotService.saveOrUpdate(alipayMchSnapshot);
        if(!isTrue){ //保存失败！
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        //3. 调起支付宝进件接口
        AlipayMchSnapshot result = alipayApiService.alipayApplymentSubmit(isvParamJSON, alipayMchSnapshot);

        AlipayMchSnapshot updateRecord = new AlipayMchSnapshot();
        updateRecord.setApplyNo(alipayMchSnapshot.getApplyNo());

        boolean isSuccess = result.getPsBooleanVal("isSuccess"); //是否请求成功

        if(isSuccess){ //请求成功

            updateRecord.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_ING); //支付宝审核中
            updateRecord.setBatchNo(result.getBatchNo());
            updateRecord.setUserId(result.getUserId());
            updateRecord.setAuthAppId(result.getAuthAppId());
            updateRecord.setAppAuthToken(result.getAppAuthToken());
            updateRecord.setAppRefreshToken(result.getAppRefreshToken());
            updateRecord.setExpiresIn(result.getExpiresIn());
            updateRecord.setReExpiresIn(result.getReExpiresIn());

        }else{ //失败
            updateRecord.setApplyStatus(MchConstant.ALIPAY_MCH_STATUS_MCH_CANCELED); //失败
            updateRecord.setAuditInfo(result.getAuditInfo()); //错误信息
        }
        rpcCommonService.rpcAlipayMchSnapshotService.updateById(updateRecord);
    }

    @Override
    public void queryMchApply(Long applyNo) {

        AlipayMchSnapshot alipayMchSnapshot = rpcCommonService.rpcAlipayMchSnapshotService.getById(applyNo);
        if(alipayMchSnapshot == null) throw new ServiceException(RetEnum.RET_COMM_RECORD_NOT_EXIST);

        if(alipayMchSnapshot.getApplyStatus() == MchConstant.ALIPAY_MCH_STATUS_ING ||
                alipayMchSnapshot.getApplyStatus() == MchConstant.ALIPAY_MCH_STATUS_WAIT_SIGN
        ){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "当前状态无需查询");
        }

        //1.2 查询服务商配置信息  & 检查
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByIsv(alipayMchSnapshot.getIsvId(), PayConstant.CHANNEL_NAME_ALIPAY);
        if(payPassage == null) throw new ServiceException(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST);
        String isvParam = payPassage.getIsvParam();
        JSONObject isvParamJSON = JSONObject.parseObject(isvParam);
        if(isvParamJSON.isEmpty()){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR);
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("pid"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "pid");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("privateKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "privateKey");
        }
        if(StringUtils.isEmpty(isvParamJSON.getString("alipayPublicKey"))){
            throw new ServiceException(RetEnum.RET_SERVICE_ISV_PARAMS_ERROR, "alipayPublicKey");
        }


        //3. 调起支付宝接口查询
        AlipayMchSnapshot result = alipayApiService.queryApplymentSubmit(isvParamJSON, alipayMchSnapshot);

        if(result.getPsBooleanVal("isSuccess")){ //查询成功

            AlipayMchSnapshot updateRecord = new AlipayMchSnapshot();
            updateRecord.setApplyNo(alipayMchSnapshot.getApplyNo());
            updateRecord.setApplyStatus(result.getApplyStatus());

            if(StringUtils.isNotEmpty(result.getConfirmUrl())){
                String url = payConfig.getPayUrl() + "/qrcode_img_get?url=" + result.getConfirmUrl() + "&widht=200&height=200";
                updateRecord.setConfirmUrl(url);
            }

            if(StringUtils.isNotEmpty(result.getAgentAppId())){
                updateRecord.setAgentAppId(result.getAgentAppId());
            }

            if(StringUtils.isNotEmpty(result.getMerchantPid())){
                updateRecord.setMerchantPid(result.getMerchantPid());
            }

            if(StringUtils.isNotEmpty(result.getAuditInfo())){
                updateRecord.setAuditInfo(result.getAuditInfo());
            }

            rpcCommonService.rpcAlipayMchSnapshotService.updateById(updateRecord);

        }else{
            throw new ServiceException(RetEnum.RET_SERVICE_INTERFACE_ERROR, result.getAuditInfo());
        }
    }

}
