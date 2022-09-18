package org.xxpay.pay.channel.maxpay.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * 实时代付交易请求对象
 * Created by yujinshui on 2017/12/01.
 */
public class PayRealTimeRequest extends PaymaxBase {

    /**
     * 商户订单号
     * 商户系统内唯一
     */
    @JSONField(name = "order_no")
    private String orderNo;

    /**
     * 手机号
     */
    @JSONField(name = "mobile_no")
    private String mobileNo;

    /**
     * 账户类型
     * 0-对公；1-对私借记卡；2-对私贷记卡；3-对私存折
     */
    @JSONField(name = "account_type")
    private String accountType;

    /**
     * 银行卡号
     */
    @JSONField(name = "bank_account_no")
    private String bankAccountNo;

    /**
     * 银行卡户名
     */
    @JSONField(name = "bank_account_name")
    private String bankAccountName;

    /**
     * 金额
     * 精确到小数点后两位，单位：元，
     */
    private BigDecimal amount;

    /**
     * 币种代码:CNY（人民币）
     */
    private String currency;

    /**
     * 预留字段
     */
    private String extra;

    /**
     *备注
     */
    private String comment;

    /**
     * 入账卡开户行电子联行行号
     */
    @JSONField(name = "bank_branch_no")
    private String bankBranchNo;

    /**
     * 银行编码
     */
    @JSONField(name = "bank_code")
    private String bankCode;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getBankBranchNo() {
        return bankBranchNo;
    }

    public void setBankBranchNo(String bankBranchNo) {
        this.bankBranchNo = bankBranchNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

}
