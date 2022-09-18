package org.xxpay.pay.channel.maxpay.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * 头寸查询响应对象
 * Created by yujinshui on 2017/12/01.
 */
public class PayBalanceResponse extends PaymaxBase {

    /**
     * 现金余额
     */
    @JSONField(name = "cash_balance")
    private BigDecimal cashBalance;

    /**
     * 代付可用余额
     */
    @JSONField(name = "pay_balance")
    private BigDecimal payBalance;

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

    public BigDecimal getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(BigDecimal cashBalance) {
        this.cashBalance = cashBalance;
    }

    public BigDecimal getPayBalance() {
        return payBalance;
    }

    public void setPayBalance(BigDecimal payBalance) {
        this.payBalance = payBalance;
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
