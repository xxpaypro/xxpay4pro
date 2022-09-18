package org.xxpay.pay.channel.maxpay.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * 查询实时代付交易响应对象
 * Created by yujinshui on 2017/12/01.
 */
public class PayRealTimeQueryResponse extends PaymaxBase {

    /**
     * Paymax 订单号
     */
    private String id;

    /**
     * 商户订单号
     */
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 交易结果状态
     */
    private OrderStatus status;

    /**
     * 预留字段
     */
    private String extra;

    /**
     * 备注
     */
    private String comment;

    /**
     * 响应码
     */
    @JSONField(name = "failure_code")
    private String failureCode;

    /**
     * 响应信息
     */
    @JSONField(name = "failure_msg")
    private String failureMsg;

    /**
     * 本次请求是否成功 true:成功,false:失败
     */
    private Boolean reqSuccessFlag;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFailureCode() {
        return failureCode;
    }

    public void setFailureCode(String failureCode) {
        this.failureCode = failureCode;
    }

    public String getFailureMsg() {
        return failureMsg;
    }

    public void setFailureMsg(String failureMsg) {
        this.failureMsg = failureMsg;
    }

    public Boolean getReqSuccessFlag() {
        return reqSuccessFlag;
    }

    public void setReqSuccessFlag(Boolean reqSuccessFlag) {
        this.reqSuccessFlag = reqSuccessFlag;
    }
}
