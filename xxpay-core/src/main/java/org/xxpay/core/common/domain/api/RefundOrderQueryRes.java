package org.xxpay.core.common.domain.api;

import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.RefundOrder;

/**
 * 退款查单接口api 实体Bean
 */
public class RefundOrderQueryRes extends AbstractRes {

    /**  **/
    private String mchId;

    /**  **/
    private String appId;

    /**  **/
    private String mchRefundNo;

    /**  **/
    private String refundAmount;

    /**  **/
    private String status;

    /**  **/
    private String channelOrderNo;

    /**  **/
    private String refundSuccTime;


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchRefundNo() {
        return mchRefundNo;
    }

    public void setMchRefundNo(String mchRefundNo) {
        this.mchRefundNo = mchRefundNo;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getRefundSuccTime() {
        return refundSuccTime;
    }

    public void setRefundSuccTime(String refundSuccTime) {
        this.refundSuccTime = refundSuccTime;
    }

    public void setByOrder(RefundOrder refundOrder){
        this.mchId = StrUtil.toString(refundOrder.getMchId());
        this.appId = StrUtil.toString(refundOrder.getAppId());
        this.mchRefundNo = StrUtil.toString(refundOrder.getMchRefundNo());
        this.refundAmount =  StrUtil.toString(refundOrder.getRefundAmount());
        this.status = StrUtil.toString(refundOrder.getStatus());
        this.channelOrderNo = StrUtil.toString(refundOrder.getChannelOrderNo());
        this.refundSuccTime = refundOrder.getRefundSuccTime() == null ? "" : refundOrder.getRefundSuccTime().getTime() + "";
    }

}


