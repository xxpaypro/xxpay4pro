package org.xxpay.pay.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.PayPassage;
import org.xxpay.pay.mq.BaseNotify4MchPay;

/**
 * @author: dingzhiwei
 * @date: 17/9/9
 * @description:
 */
@Service
public class PayOrderService {

    private static final MyLog _log = MyLog.getLog(PayOrderService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    public BaseNotify4MchPay baseNotify4MchPay;

    public PayOrder query(Long mchId, String payOrderId, String mchOrderNo, boolean executeNotify) {
        PayOrder payOrder;
        if(StringUtils.isNotBlank(payOrderId)) {
            payOrder = rpcCommonService.rpcPayOrderService.selectByMchIdAndPayOrderId(mchId, payOrderId);
        }else {
            payOrder = rpcCommonService.rpcPayOrderService.selectByMchIdAndMchOrderNo(mchId, mchOrderNo);
        }
        if(payOrder == null) return null;

        if(executeNotify && PayConstant.PAY_STATUS_SUCCESS == payOrder.getStatus() ) {
            baseNotify4MchPay.doNotify(payOrder, false);
            _log.info("业务查单完成,并再次发送业务支付通知.发送完成");
        }
        return payOrder;
    }

    public PayOrder query(Long mchId, String payOrderId, String mchOrderNo) {
        return query(mchId, payOrderId, mchOrderNo, false);
    }

    /**
     * 获取接口主发起方的支付参数信息
     * @param mchId 商户ID
     * @param productId 产品ID
     * @return
     */
    public String getMainPayParam(Long mchId, Integer productId) {
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        return getMainPayParam(mchInfo, productId);
    }

    /**
     * 获取接口主发起方的支付参数信息
     * @param mchInfo 商户信息
     * @param productId 产品ID
     * @return
     */
    public String getMainPayParam(MchInfo mchInfo, Integer productId) {
        if(mchInfo == null) return null;
        Long mchId = mchInfo.getMchId();
        // 判断商户类型,如果私有则直接使用,否则为服务商对应通道
        if(MchConstant.MCH_TYPE_PRIVATE == mchInfo.getType()) {
            FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchId, productId);
            if(feeScale == null || feeScale.getStatus() != MchConstant.PUB_YES) {
                _log.error("商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+"]");
                return null;
            }
            String ifTypeCode = feeScale.getExtConfig();
            PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByMch(mchId, ifTypeCode);
            return payPassage.getMchParam();
        }
        // 服务商ID
        Long isvId = mchInfo.getIsvId();
        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByIsv(isvId, productId);
        if(feeScale == null || feeScale.getStatus() != MchConstant.PUB_YES) {
            _log.error("服务商没有该产品的支付通道[productId="+productId+",isvId="+isvId+"]");
            return null;
        }
        String ifTypeCode = feeScale.getExtConfig();
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByIsv(isvId, ifTypeCode);
        return payPassage.getIsvParam();
    }

    /**
     * 获取接口子发起方的支付参数信息
     * @param mchId 商户ID
     * @param productId 产品ID
     * @return
     */
    public String getSubPayParam(Long mchId, Integer productId) {
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        return getSubPayParam(mchInfo, productId);
    }

    /**
     * 获取接口子发起方的支付参数信息
     * @param mchInfo 商户信息
     * @param productId 产品ID
     * @return
     */
    public String getSubPayParam(MchInfo mchInfo, Integer productId) {
        if(mchInfo == null) return null;
        Long mchId = mchInfo.getMchId();
        // 判断商户类型,如果私有则直接使用,否则为服务商对应通道
        if(MchConstant.MCH_TYPE_PRIVATE == mchInfo.getType()) return null;
        // 服务商ID
        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchId, productId);
        if(feeScale == null || feeScale.getStatus() != MchConstant.PUB_YES) {
            _log.error("商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+"]");
            return null;
        }
        String ifTypeCode = feeScale.getExtConfig();
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.selectByMch(mchId, ifTypeCode);
        return payPassage.getMchParam();
    }

}
