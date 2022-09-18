package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_agentpay_passage")
public class AgentpayPassage extends BaseModel {
    /**
     * 代付通道ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 所属角色ID 商户ID / 0(平台)
     *
     * @mbggenerated
     */
    @TableField("BelongInfoId")
    private Long belongInfoId;

    /**
     * 所属角色类型:1-代理商,2-商户,3-平台
     *
     * @mbggenerated
     */
    @TableField("BelongInfoType")
    private Byte belongInfoType;

    /**
     * 通道名称
     *
     * @mbggenerated
     */
    @TableField("PassageName")
    private String passageName;

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
     * 通道状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 手续费类型,1-百分比收费,2-固定收费
     *
     * @mbggenerated
     */
    @TableField("FeeType")
    private Byte feeType;

    /**
     * 手续费费率,百分比
     *
     * @mbggenerated
     */
    @TableField("FeeRate")
    private BigDecimal feeRate;

    /**
     * 手续费每笔金额,单位分
     *
     * @mbggenerated
     */
    @TableField("FeeEvery")
    private Long feeEvery;

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
     * 是否默认,0-否,1-是
     *
     * @mbggenerated
     */
    @TableField("IsDefault")
    private Byte isDefault;

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

    public Long getBelongInfoId() {
        return belongInfoId;
    }

    public void setBelongInfoId(Long belongInfoId) {
        this.belongInfoId = belongInfoId;
    }

    public Byte getBelongInfoType() {
        return belongInfoType;
    }

    public void setBelongInfoType(Byte belongInfoType) {
        this.belongInfoType = belongInfoType;
    }

    public String getPassageName() {
        return passageName;
    }

    public void setPassageName(String passageName) {
        this.passageName = passageName;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getFeeType() {
        return feeType;
    }

    public void setFeeType(Byte feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public Long getFeeEvery() {
        return feeEvery;
    }

    public void setFeeEvery(Long feeEvery) {
        this.feeEvery = feeEvery;
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

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
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
        sb.append(", belongInfoId=").append(belongInfoId);
        sb.append(", belongInfoType=").append(belongInfoType);
        sb.append(", passageName=").append(passageName);
        sb.append(", ifCode=").append(ifCode);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", status=").append(status);
        sb.append(", feeType=").append(feeType);
        sb.append(", feeRate=").append(feeRate);
        sb.append(", feeEvery=").append(feeEvery);
        sb.append(", maxDayAmount=").append(maxDayAmount);
        sb.append(", tradeStartTime=").append(tradeStartTime);
        sb.append(", tradeEndTime=").append(tradeEndTime);
        sb.append(", riskStatus=").append(riskStatus);
        sb.append(", isDefault=").append(isDefault);
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
        AgentpayPassage other = (AgentpayPassage) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getBelongInfoId() == null ? other.getBelongInfoId() == null : this.getBelongInfoId().equals(other.getBelongInfoId()))
                && (this.getBelongInfoType() == null ? other.getBelongInfoType() == null : this.getBelongInfoType().equals(other.getBelongInfoType()))
                && (this.getPassageName() == null ? other.getPassageName() == null : this.getPassageName().equals(other.getPassageName()))
                && (this.getIfCode() == null ? other.getIfCode() == null : this.getIfCode().equals(other.getIfCode()))
                && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getFeeType() == null ? other.getFeeType() == null : this.getFeeType().equals(other.getFeeType()))
                && (this.getFeeRate() == null ? other.getFeeRate() == null : this.getFeeRate().equals(other.getFeeRate()))
                && (this.getFeeEvery() == null ? other.getFeeEvery() == null : this.getFeeEvery().equals(other.getFeeEvery()))
                && (this.getMaxDayAmount() == null ? other.getMaxDayAmount() == null : this.getMaxDayAmount().equals(other.getMaxDayAmount()))
                && (this.getTradeStartTime() == null ? other.getTradeStartTime() == null : this.getTradeStartTime().equals(other.getTradeStartTime()))
                && (this.getTradeEndTime() == null ? other.getTradeEndTime() == null : this.getTradeEndTime().equals(other.getTradeEndTime()))
                && (this.getRiskStatus() == null ? other.getRiskStatus() == null : this.getRiskStatus().equals(other.getRiskStatus()))
                && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBelongInfoId() == null) ? 0 : getBelongInfoId().hashCode());
        result = prime * result + ((getBelongInfoType() == null) ? 0 : getBelongInfoType().hashCode());
        result = prime * result + ((getPassageName() == null) ? 0 : getPassageName().hashCode());
        result = prime * result + ((getIfCode() == null) ? 0 : getIfCode().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getFeeType() == null) ? 0 : getFeeType().hashCode());
        result = prime * result + ((getFeeRate() == null) ? 0 : getFeeRate().hashCode());
        result = prime * result + ((getFeeEvery() == null) ? 0 : getFeeEvery().hashCode());
        result = prime * result + ((getMaxDayAmount() == null) ? 0 : getMaxDayAmount().hashCode());
        result = prime * result + ((getTradeStartTime() == null) ? 0 : getTradeStartTime().hashCode());
        result = prime * result + ((getTradeEndTime() == null) ? 0 : getTradeEndTime().hashCode());
        result = prime * result + ((getRiskStatus() == null) ? 0 : getRiskStatus().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
