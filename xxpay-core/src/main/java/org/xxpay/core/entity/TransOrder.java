package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_trans_order")
public class TransOrder extends BaseModel {
    /**
     * 转账订单号
     *
     * @mbggenerated
     */
    @TableField("TransOrderId")
    private String transOrderId;

    /**
     * 代理商或商户ID
     *
     * @mbggenerated
     */
    @TableField("InfoId")
    private Long infoId;

    /**
     * 账号类型:1-代理商,2-商户
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 商户类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    @TableField("MchType")
    private Byte mchType;

    /**
     * 应用ID
     *
     * @mbggenerated
     */
    @TableField("AppId")
    private String appId;

    /**
     * 商户转账单号
     *
     * @mbggenerated
     */
    @TableField("MchTransNo")
    private String mchTransNo;

    /**
     * 渠道类型,对接支付接口类型代码
     *
     * @mbggenerated
     */
    @TableField("ChannelType")
    private String channelType;

    /**
     * 渠道ID,对应支付接口代码
     *
     * @mbggenerated
     */
    @TableField("ChannelId")
    private String channelId;

    /**
     * 通道ID
     *
     * @mbggenerated
     */
    @TableField("PassageId")
    private Integer passageId;

    /**
     * 通道账户ID
     *
     * @mbggenerated
     */
    @TableField("PassageAccountId")
    private Integer passageAccountId;

    /**
     * 转账金额,单位分
     *
     * @mbggenerated
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 三位货币代码,人民币:cny
     *
     * @mbggenerated
     */
    @TableField("Currency")
    private String currency;

    /**
     * 转账状态:0-订单生成,1-转账中,2-转账成功,3-转账失败
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 转账结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败
     *
     * @mbggenerated
     */
    @TableField("Result")
    private Byte result;

    /**
     * 客户端IP
     *
     * @mbggenerated
     */
    @TableField("ClientIp")
    private String clientIp;

    /**
     * 设备
     *
     * @mbggenerated
     */
    @TableField("Device")
    private String device;

    /**
     * 备注
     *
     * @mbggenerated
     */
    @TableField("RemarkInfo")
    private String remarkInfo;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     *
     * @mbggenerated
     */
    @TableField("ChannelUser")
    private String channelUser;

    /**
     * 账户属性:0-对私,1-对公,默认对私
     *
     * @mbggenerated
     */
    @TableField("AccountAttr")
    private Byte accountAttr;

    /**
     * 账户类型:1-银行卡转账,2-微信转账,3-支付宝转账
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Byte accountType;

    /**
     * 账户名
     *
     * @mbggenerated
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 账户号
     *
     * @mbggenerated
     */
    @TableField("AccountNo")
    private String accountNo;

    /**
     * 开户行所在省
     *
     * @mbggenerated
     */
    @TableField("Province")
    private String province;

    /**
     * 开户行所在市
     *
     * @mbggenerated
     */
    @TableField("City")
    private String city;

    /**
     * 开户行名称
     *
     * @mbggenerated
     */
    @TableField("BankName")
    private String bankName;

    /**
     * 联行号
     *
     * @mbggenerated
     */
    @TableField("BankType")
    private Long bankType;

    /**
     * 银行代码
     *
     * @mbggenerated
     */
    @TableField("BankCode")
    private String bankCode;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    @TableField("ChannelMchId")
    private String channelMchId;

    /**
     * 渠道费率
     *
     * @mbggenerated
     */
    @TableField("ChannelRate")
    private BigDecimal channelRate;

    /**
     * 渠道每笔费用,单位分
     *
     * @mbggenerated
     */
    @TableField("ChannelFeeEvery")
    private Long channelFeeEvery;

    /**
     * 渠道成本,单位分
     *
     * @mbggenerated
     */
    @TableField("ChannelCost")
    private Long channelCost;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    @TableField("ChannelOrderNo")
    private String channelOrderNo;

    /**
     * 渠道错误码
     *
     * @mbggenerated
     */
    @TableField("ChannelErrCode")
    private String channelErrCode;

    /**
     * 渠道错误描述
     *
     * @mbggenerated
     */
    @TableField("ChannelErrMsg")
    private String channelErrMsg;

    /**
     * 特定渠道发起时额外参数
     *
     * @mbggenerated
     */
    @TableField("Extra")
    private String extra;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    @TableField("NotifyUrl")
    private String notifyUrl;

    /**
     * 扩展参数1
     *
     * @mbggenerated
     */
    @TableField("Param1")
    private String param1;

    /**
     * 扩展参数2
     *
     * @mbggenerated
     */
    @TableField("Param2")
    private String param2;

    /**
     * 订单失效时间
     *
     * @mbggenerated
     */
    @TableField("ExpireTime")
    private Date expireTime;

    /**
     * 订单转账成功时间
     *
     * @mbggenerated
     */
    @TableField("TransSuccTime")
    private Date transSuccTime;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    @TableField("UpdateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getTransOrderId() {
        return transOrderId;
    }

    public void setTransOrderId(String transOrderId) {
        this.transOrderId = transOrderId;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Byte getInfoType() {
        return infoType;
    }

    public void setInfoType(Byte infoType) {
        this.infoType = infoType;
    }

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchTransNo() {
        return mchTransNo;
    }

    public void setMchTransNo(String mchTransNo) {
        this.mchTransNo = mchTransNo;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public Integer getPassageId() {
        return passageId;
    }

    public void setPassageId(Integer passageId) {
        this.passageId = passageId;
    }

    public Integer getPassageAccountId() {
        return passageAccountId;
    }

    public void setPassageAccountId(Integer passageAccountId) {
        this.passageAccountId = passageAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getResult() {
        return result;
    }

    public void setResult(Byte result) {
        this.result = result;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getRemarkInfo() {
        return remarkInfo;
    }

    public void setRemarkInfo(String remarkInfo) {
        this.remarkInfo = remarkInfo;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public Byte getAccountAttr() {
        return accountAttr;
    }

    public void setAccountAttr(Byte accountAttr) {
        this.accountAttr = accountAttr;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getBankType() {
        return bankType;
    }

    public void setBankType(Long bankType) {
        this.bankType = bankType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public Long getChannelFeeEvery() {
        return channelFeeEvery;
    }

    public void setChannelFeeEvery(Long channelFeeEvery) {
        this.channelFeeEvery = channelFeeEvery;
    }

    public Long getChannelCost() {
        return channelCost;
    }

    public void setChannelCost(Long channelCost) {
        this.channelCost = channelCost;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public String getChannelErrCode() {
        return channelErrCode;
    }

    public void setChannelErrCode(String channelErrCode) {
        this.channelErrCode = channelErrCode;
    }

    public String getChannelErrMsg() {
        return channelErrMsg;
    }

    public void setChannelErrMsg(String channelErrMsg) {
        this.channelErrMsg = channelErrMsg;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getTransSuccTime() {
        return transSuccTime;
    }

    public void setTransSuccTime(Date transSuccTime) {
        this.transSuccTime = transSuccTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", transOrderId=").append(transOrderId);
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", mchType=").append(mchType);
        sb.append(", appId=").append(appId);
        sb.append(", mchTransNo=").append(mchTransNo);
        sb.append(", channelType=").append(channelType);
        sb.append(", channelId=").append(channelId);
        sb.append(", passageId=").append(passageId);
        sb.append(", passageAccountId=").append(passageAccountId);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", status=").append(status);
        sb.append(", result=").append(result);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", remarkInfo=").append(remarkInfo);
        sb.append(", channelUser=").append(channelUser);
        sb.append(", accountAttr=").append(accountAttr);
        sb.append(", accountType=").append(accountType);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankType=").append(bankType);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", channelMchId=").append(channelMchId);
        sb.append(", channelRate=").append(channelRate);
        sb.append(", channelFeeEvery=").append(channelFeeEvery);
        sb.append(", channelCost=").append(channelCost);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", channelErrCode=").append(channelErrCode);
        sb.append(", channelErrMsg=").append(channelErrMsg);
        sb.append(", extra=").append(extra);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", param1=").append(param1);
        sb.append(", param2=").append(param2);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", transSuccTime=").append(transSuccTime);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TransOrder other = (TransOrder) that;
        return (this.getTransOrderId() == null ? other.getTransOrderId() == null : this.getTransOrderId().equals(other.getTransOrderId()))
                && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
                && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
                && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
                && (this.getAppId() == null ? other.getAppId() == null : this.getAppId().equals(other.getAppId()))
                && (this.getMchTransNo() == null ? other.getMchTransNo() == null : this.getMchTransNo().equals(other.getMchTransNo()))
                && (this.getChannelType() == null ? other.getChannelType() == null : this.getChannelType().equals(other.getChannelType()))
                && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
                && (this.getPassageId() == null ? other.getPassageId() == null : this.getPassageId().equals(other.getPassageId()))
                && (this.getPassageAccountId() == null ? other.getPassageAccountId() == null : this.getPassageAccountId().equals(other.getPassageAccountId()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getResult() == null ? other.getResult() == null : this.getResult().equals(other.getResult()))
                && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
                && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
                && (this.getRemarkInfo() == null ? other.getRemarkInfo() == null : this.getRemarkInfo().equals(other.getRemarkInfo()))
                && (this.getChannelUser() == null ? other.getChannelUser() == null : this.getChannelUser().equals(other.getChannelUser()))
                && (this.getAccountAttr() == null ? other.getAccountAttr() == null : this.getAccountAttr().equals(other.getAccountAttr()))
                && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
                && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
                && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
                && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
                && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
                && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
                && (this.getBankType() == null ? other.getBankType() == null : this.getBankType().equals(other.getBankType()))
                && (this.getBankCode() == null ? other.getBankCode() == null : this.getBankCode().equals(other.getBankCode()))
                && (this.getChannelMchId() == null ? other.getChannelMchId() == null : this.getChannelMchId().equals(other.getChannelMchId()))
                && (this.getChannelRate() == null ? other.getChannelRate() == null : this.getChannelRate().equals(other.getChannelRate()))
                && (this.getChannelFeeEvery() == null ? other.getChannelFeeEvery() == null : this.getChannelFeeEvery().equals(other.getChannelFeeEvery()))
                && (this.getChannelCost() == null ? other.getChannelCost() == null : this.getChannelCost().equals(other.getChannelCost()))
                && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
                && (this.getChannelErrCode() == null ? other.getChannelErrCode() == null : this.getChannelErrCode().equals(other.getChannelErrCode()))
                && (this.getChannelErrMsg() == null ? other.getChannelErrMsg() == null : this.getChannelErrMsg().equals(other.getChannelErrMsg()))
                && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()))
                && (this.getNotifyUrl() == null ? other.getNotifyUrl() == null : this.getNotifyUrl().equals(other.getNotifyUrl()))
                && (this.getParam1() == null ? other.getParam1() == null : this.getParam1().equals(other.getParam1()))
                && (this.getParam2() == null ? other.getParam2() == null : this.getParam2().equals(other.getParam2()))
                && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
                && (this.getTransSuccTime() == null ? other.getTransSuccTime() == null : this.getTransSuccTime().equals(other.getTransSuccTime()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getTransOrderId() == null) ? 0 : getTransOrderId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getAppId() == null) ? 0 : getAppId().hashCode());
        result = prime * result + ((getMchTransNo() == null) ? 0 : getMchTransNo().hashCode());
        result = prime * result + ((getChannelType() == null) ? 0 : getChannelType().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getPassageId() == null) ? 0 : getPassageId().hashCode());
        result = prime * result + ((getPassageAccountId() == null) ? 0 : getPassageAccountId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getResult() == null) ? 0 : getResult().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getRemarkInfo() == null) ? 0 : getRemarkInfo().hashCode());
        result = prime * result + ((getChannelUser() == null) ? 0 : getChannelUser().hashCode());
        result = prime * result + ((getAccountAttr() == null) ? 0 : getAccountAttr().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankType() == null) ? 0 : getBankType().hashCode());
        result = prime * result + ((getBankCode() == null) ? 0 : getBankCode().hashCode());
        result = prime * result + ((getChannelMchId() == null) ? 0 : getChannelMchId().hashCode());
        result = prime * result + ((getChannelRate() == null) ? 0 : getChannelRate().hashCode());
        result = prime * result + ((getChannelFeeEvery() == null) ? 0 : getChannelFeeEvery().hashCode());
        result = prime * result + ((getChannelCost() == null) ? 0 : getChannelCost().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getChannelErrCode() == null) ? 0 : getChannelErrCode().hashCode());
        result = prime * result + ((getChannelErrMsg() == null) ? 0 : getChannelErrMsg().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        result = prime * result + ((getNotifyUrl() == null) ? 0 : getNotifyUrl().hashCode());
        result = prime * result + ((getParam1() == null) ? 0 : getParam1().hashCode());
        result = prime * result + ((getParam2() == null) ? 0 : getParam2().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getTransSuccTime() == null) ? 0 : getTransSuccTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
