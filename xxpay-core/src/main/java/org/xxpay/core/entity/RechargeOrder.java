package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_recharge_order")
public class RechargeOrder implements Serializable {
    /**
     * 充值订单号
     *
     * @mbggenerated
     */
    @TableField("RechargeOrderId")
    private String rechargeOrderId;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 商户类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    @TableField("MchType")
    private Byte mchType;

    /**
     * 充值类型:1-B2C,2-B2B
     *
     * @mbggenerated
     */
    @TableField("RechargeType")
    private Byte rechargeType;

    /**
     * 商户费率
     *
     * @mbggenerated
     */
    @TableField("MchRate")
    private BigDecimal mchRate;

    /**
     * 商户入账,单位分
     *
     * @mbggenerated
     */
    @TableField("MchIncome")
    private Long mchIncome;

    /**
     * 代理商ID
     *
     * @mbggenerated
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 代理商费率
     *
     * @mbggenerated
     */
    @TableField("AgentRate")
    private BigDecimal agentRate;

    /**
     * 代理商利润,单位分
     *
     * @mbggenerated
     */
    @TableField("AgentProfit")
    private Long agentProfit;

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
     * 充值金额,单位分
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
     * 充值状态,0-订单生成,1-充值中,2-充值成功,3-充值失败
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

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
     * 商品标题
     *
     * @mbggenerated
     */
    @TableField("Subject")
    private String subject;

    /**
     * 商品描述信息
     *
     * @mbggenerated
     */
    @TableField("Body")
    private String body;

    /**
     * 特定渠道发起额外参数
     *
     * @mbggenerated
     */
    @TableField("Extra")
    private String extra;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     *
     * @mbggenerated
     */
    @TableField("ChannelUser")
    private String channelUser;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    @TableField("ChannelMchId")
    private String channelMchId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    @TableField("ChannelOrderNo")
    private String channelOrderNo;

    /**
     * 平台利润,单位分
     *
     * @mbggenerated
     */
    @TableField("PlatProfit")
    private Long platProfit;

    /**
     * 渠道费率
     *
     * @mbggenerated
     */
    @TableField("ChannelRate")
    private BigDecimal channelRate;

    /**
     * 渠道成本,单位分
     *
     * @mbggenerated
     */
    @TableField("ChannelCost")
    private Long channelCost;

    /**
     * 是否退款,0-未退款,1-退款
     *
     * @mbggenerated
     */
    @TableField("IsRefund")
    private Byte isRefund;

    /**
     * 退款次数
     *
     * @mbggenerated
     */
    @TableField("RefundTimes")
    private Integer refundTimes;

    /**
     * 成功退款金额,单位分
     *
     * @mbggenerated
     */
    @TableField("SuccessRefundAmount")
    private Long successRefundAmount;

    /**
     * 渠道支付错误码
     *
     * @mbggenerated
     */
    @TableField("ErrCode")
    private String errCode;

    /**
     * 渠道支付错误描述
     *
     * @mbggenerated
     */
    @TableField("ErrMsg")
    private String errMsg;

    /**
     * 订单失效时间
     *
     * @mbggenerated
     */
    @TableField("ExpireTime")
    private Date expireTime;

    /**
     * 订单支付成功时间
     *
     * @mbggenerated
     */
    @TableField("PaySuccTime")
    private Date paySuccTime;

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

    public String getRechargeOrderId() {
        return rechargeOrderId;
    }

    public void setRechargeOrderId(String rechargeOrderId) {
        this.rechargeOrderId = rechargeOrderId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public Byte getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Byte rechargeType) {
        this.rechargeType = rechargeType;
    }

    public BigDecimal getMchRate() {
        return mchRate;
    }

    public void setMchRate(BigDecimal mchRate) {
        this.mchRate = mchRate;
    }

    public Long getMchIncome() {
        return mchIncome;
    }

    public void setMchIncome(Long mchIncome) {
        this.mchIncome = mchIncome;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public Long getAgentProfit() {
        return agentProfit;
    }

    public void setAgentProfit(Long agentProfit) {
        this.agentProfit = agentProfit;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }

    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }

    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }

    public Long getPlatProfit() {
        return platProfit;
    }

    public void setPlatProfit(Long platProfit) {
        this.platProfit = platProfit;
    }

    public BigDecimal getChannelRate() {
        return channelRate;
    }

    public void setChannelRate(BigDecimal channelRate) {
        this.channelRate = channelRate;
    }

    public Long getChannelCost() {
        return channelCost;
    }

    public void setChannelCost(Long channelCost) {
        this.channelCost = channelCost;
    }

    public Byte getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Byte isRefund) {
        this.isRefund = isRefund;
    }

    public Integer getRefundTimes() {
        return refundTimes;
    }

    public void setRefundTimes(Integer refundTimes) {
        this.refundTimes = refundTimes;
    }

    public Long getSuccessRefundAmount() {
        return successRefundAmount;
    }

    public void setSuccessRefundAmount(Long successRefundAmount) {
        this.successRefundAmount = successRefundAmount;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(Date paySuccTime) {
        this.paySuccTime = paySuccTime;
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
        sb.append(", rechargeOrderId=").append(rechargeOrderId);
        sb.append(", mchId=").append(mchId);
        sb.append(", mchType=").append(mchType);
        sb.append(", rechargeType=").append(rechargeType);
        sb.append(", mchRate=").append(mchRate);
        sb.append(", mchIncome=").append(mchIncome);
        sb.append(", agentId=").append(agentId);
        sb.append(", agentRate=").append(agentRate);
        sb.append(", agentProfit=").append(agentProfit);
        sb.append(", passageId=").append(passageId);
        sb.append(", passageAccountId=").append(passageAccountId);
        sb.append(", channelType=").append(channelType);
        sb.append(", channelId=").append(channelId);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", status=").append(status);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", subject=").append(subject);
        sb.append(", body=").append(body);
        sb.append(", extra=").append(extra);
        sb.append(", channelUser=").append(channelUser);
        sb.append(", channelMchId=").append(channelMchId);
        sb.append(", channelOrderNo=").append(channelOrderNo);
        sb.append(", platProfit=").append(platProfit);
        sb.append(", channelRate=").append(channelRate);
        sb.append(", channelCost=").append(channelCost);
        sb.append(", isRefund=").append(isRefund);
        sb.append(", refundTimes=").append(refundTimes);
        sb.append(", successRefundAmount=").append(successRefundAmount);
        sb.append(", errCode=").append(errCode);
        sb.append(", errMsg=").append(errMsg);
        sb.append(", expireTime=").append(expireTime);
        sb.append(", paySuccTime=").append(paySuccTime);
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
        RechargeOrder other = (RechargeOrder) that;
        return (this.getRechargeOrderId() == null ? other.getRechargeOrderId() == null : this.getRechargeOrderId().equals(other.getRechargeOrderId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
            && (this.getRechargeType() == null ? other.getRechargeType() == null : this.getRechargeType().equals(other.getRechargeType()))
            && (this.getMchRate() == null ? other.getMchRate() == null : this.getMchRate().equals(other.getMchRate()))
            && (this.getMchIncome() == null ? other.getMchIncome() == null : this.getMchIncome().equals(other.getMchIncome()))
            && (this.getAgentId() == null ? other.getAgentId() == null : this.getAgentId().equals(other.getAgentId()))
            && (this.getAgentRate() == null ? other.getAgentRate() == null : this.getAgentRate().equals(other.getAgentRate()))
            && (this.getAgentProfit() == null ? other.getAgentProfit() == null : this.getAgentProfit().equals(other.getAgentProfit()))
            && (this.getPassageId() == null ? other.getPassageId() == null : this.getPassageId().equals(other.getPassageId()))
            && (this.getPassageAccountId() == null ? other.getPassageAccountId() == null : this.getPassageAccountId().equals(other.getPassageAccountId()))
            && (this.getChannelType() == null ? other.getChannelType() == null : this.getChannelType().equals(other.getChannelType()))
            && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getCurrency() == null ? other.getCurrency() == null : this.getCurrency().equals(other.getCurrency()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
            && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
            && (this.getSubject() == null ? other.getSubject() == null : this.getSubject().equals(other.getSubject()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()))
            && (this.getChannelUser() == null ? other.getChannelUser() == null : this.getChannelUser().equals(other.getChannelUser()))
            && (this.getChannelMchId() == null ? other.getChannelMchId() == null : this.getChannelMchId().equals(other.getChannelMchId()))
            && (this.getChannelOrderNo() == null ? other.getChannelOrderNo() == null : this.getChannelOrderNo().equals(other.getChannelOrderNo()))
            && (this.getPlatProfit() == null ? other.getPlatProfit() == null : this.getPlatProfit().equals(other.getPlatProfit()))
            && (this.getChannelRate() == null ? other.getChannelRate() == null : this.getChannelRate().equals(other.getChannelRate()))
            && (this.getChannelCost() == null ? other.getChannelCost() == null : this.getChannelCost().equals(other.getChannelCost()))
            && (this.getIsRefund() == null ? other.getIsRefund() == null : this.getIsRefund().equals(other.getIsRefund()))
            && (this.getRefundTimes() == null ? other.getRefundTimes() == null : this.getRefundTimes().equals(other.getRefundTimes()))
            && (this.getSuccessRefundAmount() == null ? other.getSuccessRefundAmount() == null : this.getSuccessRefundAmount().equals(other.getSuccessRefundAmount()))
            && (this.getErrCode() == null ? other.getErrCode() == null : this.getErrCode().equals(other.getErrCode()))
            && (this.getErrMsg() == null ? other.getErrMsg() == null : this.getErrMsg().equals(other.getErrMsg()))
            && (this.getExpireTime() == null ? other.getExpireTime() == null : this.getExpireTime().equals(other.getExpireTime()))
            && (this.getPaySuccTime() == null ? other.getPaySuccTime() == null : this.getPaySuccTime().equals(other.getPaySuccTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRechargeOrderId() == null) ? 0 : getRechargeOrderId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getRechargeType() == null) ? 0 : getRechargeType().hashCode());
        result = prime * result + ((getMchRate() == null) ? 0 : getMchRate().hashCode());
        result = prime * result + ((getMchIncome() == null) ? 0 : getMchIncome().hashCode());
        result = prime * result + ((getAgentId() == null) ? 0 : getAgentId().hashCode());
        result = prime * result + ((getAgentRate() == null) ? 0 : getAgentRate().hashCode());
        result = prime * result + ((getAgentProfit() == null) ? 0 : getAgentProfit().hashCode());
        result = prime * result + ((getPassageId() == null) ? 0 : getPassageId().hashCode());
        result = prime * result + ((getPassageAccountId() == null) ? 0 : getPassageAccountId().hashCode());
        result = prime * result + ((getChannelType() == null) ? 0 : getChannelType().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getCurrency() == null) ? 0 : getCurrency().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getSubject() == null) ? 0 : getSubject().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        result = prime * result + ((getChannelUser() == null) ? 0 : getChannelUser().hashCode());
        result = prime * result + ((getChannelMchId() == null) ? 0 : getChannelMchId().hashCode());
        result = prime * result + ((getChannelOrderNo() == null) ? 0 : getChannelOrderNo().hashCode());
        result = prime * result + ((getPlatProfit() == null) ? 0 : getPlatProfit().hashCode());
        result = prime * result + ((getChannelRate() == null) ? 0 : getChannelRate().hashCode());
        result = prime * result + ((getChannelCost() == null) ? 0 : getChannelCost().hashCode());
        result = prime * result + ((getIsRefund() == null) ? 0 : getIsRefund().hashCode());
        result = prime * result + ((getRefundTimes() == null) ? 0 : getRefundTimes().hashCode());
        result = prime * result + ((getSuccessRefundAmount() == null) ? 0 : getSuccessRefundAmount().hashCode());
        result = prime * result + ((getErrCode() == null) ? 0 : getErrCode().hashCode());
        result = prime * result + ((getErrMsg() == null) ? 0 : getErrMsg().hashCode());
        result = prime * result + ((getExpireTime() == null) ? 0 : getExpireTime().hashCode());
        result = prime * result + ((getPaySuccTime() == null) ? 0 : getPaySuccTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
