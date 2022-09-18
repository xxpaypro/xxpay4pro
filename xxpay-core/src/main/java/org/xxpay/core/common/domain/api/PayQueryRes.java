package org.xxpay.core.common.domain.api;

import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.PayOrder;

/**
 * 支付查单接口api 实体Bean
 */
public class PayQueryRes extends AbstractRes {

    /**  **/
    private String mchId;

    /**  **/
    private String appId;

    /**  **/
    private String productId;

    /**  **/
    private String payOrderId;

    /**  **/
    private String mchOrderNo;

    /**  **/
    private String amount;

    /**  **/
    private String currency;

    /**  **/
    private String status;

    /**  **/
    private String channelUserId;

    /**  **/
    private String channelOrderNo;

    /**  **/
    private String channelAttach;

    /**  **/
    private String paySuccTime;

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChannelUserId() {
        return channelUserId;
    }

    public void setChannelUserId(String channelUserId) {
        this.channelUserId = channelUserId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getChannelAttach() {
        return channelAttach;
    }

    public void setChannelAttach(String channelAttach) {
        this.channelAttach = channelAttach;
    }

    public String getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(String paySuccTime) {
        this.paySuccTime = paySuccTime;
    }


    public void setByOrder(PayOrder payOrder){
        this.mchId = StrUtil.toString(payOrder.getMchId());
        this.appId = StrUtil.toString(payOrder.getAppId());
        this.productId = StrUtil.toString(payOrder.getProductId());
        this.payOrderId = StrUtil.toString(payOrder.getPayOrderId());
        this.mchOrderNo = StrUtil.toString(payOrder.getMchOrderNo());
        this.amount = StrUtil.toString(payOrder.getAmount());
        this.currency = StrUtil.toString(payOrder.getCurrency());
        this.status = StrUtil.toString(payOrder.getStatus());
        this.channelUserId = StrUtil.toString(payOrder.getChannelUser());
        this.channelOrderNo = StrUtil.toString(payOrder.getChannelOrderNo());
        this.channelAttach = "".equals(StrUtil.toString(payOrder.getChannelAttach())) ? "" : payOrder.getChannelAttach();
        this.paySuccTime = payOrder.getPaySuccTime() == null ? "" : payOrder.getPaySuccTime().getTime() + "";
    }

}


