package org.xxpay.core.common.domain.api;

import org.xxpay.core.entity.RefundOrder;

/**
 * 退款查单接口api 实体Bean
 */
public class RefundOrderRes extends AbstractRes {

    /**  **/
    private String mchId;

    /**  **/
    private String appId;

    /**  **/
    private String refundOrderId;

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

    private String channelId;

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

    public String getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(String refundOrderId) {
        this.refundOrderId = refundOrderId;
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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public void setByOrder(RefundOrder refundOrder){
        this.mchId = refundOrder.getMchId() + "";
        this.appId = refundOrder.getAppId();
        this.refundOrderId = refundOrder.getRefundOrderId();
        this.mchRefundNo = refundOrder.getMchRefundNo();
        this.refundAmount = refundOrder.getRefundAmount() + "";
        this.status = refundOrder.getStatus() + "";
        this.channelOrderNo = refundOrder.getChannelOrderNo();
        this.refundSuccTime = refundOrder.getRefundSuccTime() == null ? "" : refundOrder.getRefundSuccTime().getTime() + "";
    }

    public void setByAsync(RefundOrder refundOrder){

        this.refundOrderId = refundOrder.getRefundOrderId();
        this.mchId = refundOrder.getMchId() + "";
        this.appId = refundOrder.getAppId();
        this.refundAmount = refundOrder.getRefundAmount() + "";
        this.channelId = refundOrder.getChannelId();

    }

}


