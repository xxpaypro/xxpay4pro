package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_agentpay_passage_account")
public class AgentpayPassageAccount extends BaseModel {
    /**
     * 账户ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 账户名称
     *
     * @mbggenerated
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 代付通道ID
     *
     * @mbggenerated
     */
    @TableField("AgentpayPassageId")
    private Integer agentpayPassageId;

    /**
     * 接口代码
     *
     * @mbggenerated
     */
    @TableField("IfCode")
    private String ifCode;

    /**
     * 接口类型代码
     *
     * @mbggenerated
     */
    @TableField("IfTypeCode")
    private String ifTypeCode;

    /**
     * 账户配置参数,json字符串
     *
     * @mbggenerated
     */
    @TableField("Param")
    private String param;

    /**
     * 账户状态,0-停止,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 轮询权重
     *
     * @mbggenerated
     */
    @TableField("PollWeight")
    private Integer pollWeight;

    /**
     * 通道商户ID
     *
     * @mbggenerated
     */
    @TableField("PassageMchId")
    private String passageMchId;

    /**
     * 风控模式,1-继承,2-自定义
     *
     * @mbggenerated
     */
    @TableField("RiskMode")
    private Byte riskMode;

    /**
     * 当天交易金额,单位分
     *
     * @mbggenerated
     */
    @TableField("MaxDayAmount")
    private Long maxDayAmount;

    /**
     * 交易开始时间
     *
     * @mbggenerated
     */
    @TableField("TradeStartTime")
    private String tradeStartTime;

    /**
     * 交易结束时间
     *
     * @mbggenerated
     */
    @TableField("TradeEndTime")
    private String tradeEndTime;

    /**
     * 风控状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("RiskStatus")
    private Byte riskStatus;

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

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getAgentpayPassageId() {
        return agentpayPassageId;
    }

    public void setAgentpayPassageId(Integer agentpayPassageId) {
        this.agentpayPassageId = agentpayPassageId;
    }

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getPollWeight() {
        return pollWeight;
    }

    public void setPollWeight(Integer pollWeight) {
        this.pollWeight = pollWeight;
    }

    public String getPassageMchId() {
        return passageMchId;
    }

    public void setPassageMchId(String passageMchId) {
        this.passageMchId = passageMchId;
    }

    public Byte getRiskMode() {
        return riskMode;
    }

    public void setRiskMode(Byte riskMode) {
        this.riskMode = riskMode;
    }

    public Long getMaxDayAmount() {
        return maxDayAmount;
    }

    public void setMaxDayAmount(Long maxDayAmount) {
        this.maxDayAmount = maxDayAmount;
    }

    public String getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(String tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }

    public String getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(String tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }

    public Byte getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Byte riskStatus) {
        this.riskStatus = riskStatus;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", accountName=").append(accountName);
        sb.append(", agentpayPassageId=").append(agentpayPassageId);
        sb.append(", ifCode=").append(ifCode);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", param=").append(param);
        sb.append(", status=").append(status);
        sb.append(", pollWeight=").append(pollWeight);
        sb.append(", passageMchId=").append(passageMchId);
        sb.append(", riskMode=").append(riskMode);
        sb.append(", maxDayAmount=").append(maxDayAmount);
        sb.append(", tradeStartTime=").append(tradeStartTime);
        sb.append(", tradeEndTime=").append(tradeEndTime);
        sb.append(", riskStatus=").append(riskStatus);
        sb.append(", remark=").append(remark);
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
        AgentpayPassageAccount other = (AgentpayPassageAccount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getAgentpayPassageId() == null ? other.getAgentpayPassageId() == null : this.getAgentpayPassageId().equals(other.getAgentpayPassageId()))
            && (this.getIfCode() == null ? other.getIfCode() == null : this.getIfCode().equals(other.getIfCode()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getParam() == null ? other.getParam() == null : this.getParam().equals(other.getParam()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPollWeight() == null ? other.getPollWeight() == null : this.getPollWeight().equals(other.getPollWeight()))
            && (this.getPassageMchId() == null ? other.getPassageMchId() == null : this.getPassageMchId().equals(other.getPassageMchId()))
            && (this.getRiskMode() == null ? other.getRiskMode() == null : this.getRiskMode().equals(other.getRiskMode()))
            && (this.getMaxDayAmount() == null ? other.getMaxDayAmount() == null : this.getMaxDayAmount().equals(other.getMaxDayAmount()))
            && (this.getTradeStartTime() == null ? other.getTradeStartTime() == null : this.getTradeStartTime().equals(other.getTradeStartTime()))
            && (this.getTradeEndTime() == null ? other.getTradeEndTime() == null : this.getTradeEndTime().equals(other.getTradeEndTime()))
            && (this.getRiskStatus() == null ? other.getRiskStatus() == null : this.getRiskStatus().equals(other.getRiskStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAgentpayPassageId() == null) ? 0 : getAgentpayPassageId().hashCode());
        result = prime * result + ((getIfCode() == null) ? 0 : getIfCode().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPollWeight() == null) ? 0 : getPollWeight().hashCode());
        result = prime * result + ((getPassageMchId() == null) ? 0 : getPassageMchId().hashCode());
        result = prime * result + ((getRiskMode() == null) ? 0 : getRiskMode().hashCode());
        result = prime * result + ((getMaxDayAmount() == null) ? 0 : getMaxDayAmount().hashCode());
        result = prime * result + ((getTradeStartTime() == null) ? 0 : getTradeStartTime().hashCode());
        result = prime * result + ((getTradeEndTime() == null) ? 0 : getTradeEndTime().hashCode());
        result = prime * result + ((getRiskStatus() == null) ? 0 : getRiskStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
