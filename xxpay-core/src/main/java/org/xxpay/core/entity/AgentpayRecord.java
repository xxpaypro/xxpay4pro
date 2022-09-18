package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_agentpay_record")
public class AgentpayRecord extends BaseModel {
    /**
     * 代付单号
     *
     * @mbggenerated
     */
    @TableField("AgentpayOrderId")
    private String agentpayOrderId;

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
     * 申请代付金额
     *
     * @mbggenerated
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 代付手续费
     *
     * @mbggenerated
     */
    @TableField("Fee")
    private Long fee;

    /**
     * 打款金额
     *
     * @mbggenerated
     */
    @TableField("RemitAmount")
    private Long remitAmount;

    /**
     * 扣减账户金额
     *
     * @mbggenerated
     */
    @TableField("SubAmount")
    private Long subAmount;

    /**
     * 扣减资金来源:1:从收款账户出款,2:从代付余额账户出款
     *
     * @mbggenerated
     */
    @TableField("SubAmountFrom")
    private Byte subAmountFrom;

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
     * 开户网点名称
     *
     * @mbggenerated
     */
    @TableField("BankNetName")
    private String bankNetName;

    /**
     * 联行号
     *
     * @mbggenerated
     */
    @TableField("BankNumber")
    private String bankNumber;

    /**
     * 银行代码
     *
     * @mbggenerated
     */
    @TableField("BankCode")
    private String bankCode;

    /**
     * 状态:0-待处理,1-处理中,2-成功,3-失败
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 转账订单号
     *
     * @mbggenerated
     */
    @TableField("TransOrderId")
    private String transOrderId;

    /**
     * 转账返回消息
     *
     * @mbggenerated
     */
    @TableField("TransMsg")
    private String transMsg;

    /**
     * 渠道ID
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
     * 备注
     *
     * @mbggenerated
     */
    @TableField("Remark")
    private String remark;

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
     * 代付批次号
     *
     * @mbggenerated
     */
    @TableField("BatchNo")
    private String batchNo;

    /**
     * 代付渠道:1-平台,2-API接口
     *
     * @mbggenerated
     */
    @TableField("AgentpayChannel")
    private Byte agentpayChannel;

    /**
     * 商户单号
     *
     * @mbggenerated
     */
    @TableField("MchOrderNo")
    private String mchOrderNo;

    /**
     * 通知地址
     *
     * @mbggenerated
     */
    @TableField("NotifyUrl")
    private String notifyUrl;

    /**
     * 扩展域
     *
     * @mbggenerated
     */
    @TableField("Extra")
    private String extra;

    private static final long serialVersionUID = 1L;

    public String getAgentpayOrderId() {
        return agentpayOrderId;
    }

    public void setAgentpayOrderId(String agentpayOrderId) {
        this.agentpayOrderId = agentpayOrderId;
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

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(Long remitAmount) {
        this.remitAmount = remitAmount;
    }

    public Long getSubAmount() {
        return subAmount;
    }

    public void setSubAmount(Long subAmount) {
        this.subAmount = subAmount;
    }

    public Byte getSubAmountFrom() {
        return subAmountFrom;
    }

    public void setSubAmountFrom(Byte subAmountFrom) {
        this.subAmountFrom = subAmountFrom;
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

    public String getBankNetName() {
        return bankNetName;
    }

    public void setBankNetName(String bankNetName) {
        this.bankNetName = bankNetName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getTransOrderId() {
        return transOrderId;
    }

    public void setTransOrderId(String transOrderId) {
        this.transOrderId = transOrderId;
    }

    public String getTransMsg() {
        return transMsg;
    }

    public void setTransMsg(String transMsg) {
        this.transMsg = transMsg;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Byte getAgentpayChannel() {
        return agentpayChannel;
    }

    public void setAgentpayChannel(Byte agentpayChannel) {
        this.agentpayChannel = agentpayChannel;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", agentpayOrderId=").append(agentpayOrderId);
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", mchType=").append(mchType);
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append(", remitAmount=").append(remitAmount);
        sb.append(", subAmount=").append(subAmount);
        sb.append(", subAmountFrom=").append(subAmountFrom);
        sb.append(", accountAttr=").append(accountAttr);
        sb.append(", accountType=").append(accountType);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNetName=").append(bankNetName);
        sb.append(", bankNumber=").append(bankNumber);
        sb.append(", bankCode=").append(bankCode);
        sb.append(", status=").append(status);
        sb.append(", transOrderId=").append(transOrderId);
        sb.append(", transMsg=").append(transMsg);
        sb.append(", channelId=").append(channelId);
        sb.append(", passageId=").append(passageId);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", clientIp=").append(clientIp);
        sb.append(", device=").append(device);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", agentpayChannel=").append(agentpayChannel);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", notifyUrl=").append(notifyUrl);
        sb.append(", extra=").append(extra);
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
        AgentpayRecord other = (AgentpayRecord) that;
        return (this.getAgentpayOrderId() == null ? other.getAgentpayOrderId() == null : this.getAgentpayOrderId().equals(other.getAgentpayOrderId()))
                && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
                && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
                && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
                && (this.getRemitAmount() == null ? other.getRemitAmount() == null : this.getRemitAmount().equals(other.getRemitAmount()))
                && (this.getSubAmount() == null ? other.getSubAmount() == null : this.getSubAmount().equals(other.getSubAmount()))
                && (this.getSubAmountFrom() == null ? other.getSubAmountFrom() == null : this.getSubAmountFrom().equals(other.getSubAmountFrom()))
                && (this.getAccountAttr() == null ? other.getAccountAttr() == null : this.getAccountAttr().equals(other.getAccountAttr()))
                && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
                && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
                && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
                && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
                && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
                && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
                && (this.getBankNetName() == null ? other.getBankNetName() == null : this.getBankNetName().equals(other.getBankNetName()))
                && (this.getBankNumber() == null ? other.getBankNumber() == null : this.getBankNumber().equals(other.getBankNumber()))
                && (this.getBankCode() == null ? other.getBankCode() == null : this.getBankCode().equals(other.getBankCode()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getTransOrderId() == null ? other.getTransOrderId() == null : this.getTransOrderId().equals(other.getTransOrderId()))
                && (this.getTransMsg() == null ? other.getTransMsg() == null : this.getTransMsg().equals(other.getTransMsg()))
                && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
                && (this.getPassageId() == null ? other.getPassageId() == null : this.getPassageId().equals(other.getPassageId()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
                && (this.getClientIp() == null ? other.getClientIp() == null : this.getClientIp().equals(other.getClientIp()))
                && (this.getDevice() == null ? other.getDevice() == null : this.getDevice().equals(other.getDevice()))
                && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
                && (this.getAgentpayChannel() == null ? other.getAgentpayChannel() == null : this.getAgentpayChannel().equals(other.getAgentpayChannel()))
                && (this.getMchOrderNo() == null ? other.getMchOrderNo() == null : this.getMchOrderNo().equals(other.getMchOrderNo()))
                && (this.getNotifyUrl() == null ? other.getNotifyUrl() == null : this.getNotifyUrl().equals(other.getNotifyUrl()))
                && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getAgentpayOrderId() == null) ? 0 : getAgentpayOrderId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getRemitAmount() == null) ? 0 : getRemitAmount().hashCode());
        result = prime * result + ((getSubAmount() == null) ? 0 : getSubAmount().hashCode());
        result = prime * result + ((getSubAmountFrom() == null) ? 0 : getSubAmountFrom().hashCode());
        result = prime * result + ((getAccountAttr() == null) ? 0 : getAccountAttr().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNetName() == null) ? 0 : getBankNetName().hashCode());
        result = prime * result + ((getBankNumber() == null) ? 0 : getBankNumber().hashCode());
        result = prime * result + ((getBankCode() == null) ? 0 : getBankCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTransOrderId() == null) ? 0 : getTransOrderId().hashCode());
        result = prime * result + ((getTransMsg() == null) ? 0 : getTransMsg().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getPassageId() == null) ? 0 : getPassageId().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getClientIp() == null) ? 0 : getClientIp().hashCode());
        result = prime * result + ((getDevice() == null) ? 0 : getDevice().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getAgentpayChannel() == null) ? 0 : getAgentpayChannel().hashCode());
        result = prime * result + ((getMchOrderNo() == null) ? 0 : getMchOrderNo().hashCode());
        result = prime * result + ((getNotifyUrl() == null) ? 0 : getNotifyUrl().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        return result;
    }
}
