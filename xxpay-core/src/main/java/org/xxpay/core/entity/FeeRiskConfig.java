package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_fee_risk_config")
public class FeeRiskConfig implements Serializable {
    /**
     * FeeScale ID
     *
     * @mbggenerated
     */
    @TableField("FeeScaleId")
    private Integer feeScaleId;

    /**
     * 日累计金额：单位分
     *
     * @mbggenerated
     */
    @TableField("MaxDayAmount")
    private Long maxDayAmount;

    /**
     * 单笔最大限额,单位分
     *
     * @mbggenerated
     */
    @TableField("MaxEveryAmount")
    private Long maxEveryAmount;

    /**
     * 单笔最小限额,单位分
     *
     * @mbggenerated
     */
    @TableField("MinEveryAmount")
    private Long minEveryAmount;

    /**
     * 交易开始时间 格式: HH:mm:ss
     *
     * @mbggenerated
     */
    @TableField("TradeStartTime")
    private String tradeStartTime;

    /**
     * 交易结束时间 格式: HH:mm:ss
     *
     * @mbggenerated
     */
    @TableField("TradeEndTime")
    private String tradeEndTime;

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

    public Integer getFeeScaleId() {
        return feeScaleId;
    }

    public void setFeeScaleId(Integer feeScaleId) {
        this.feeScaleId = feeScaleId;
    }

    public Long getMaxDayAmount() {
        return maxDayAmount;
    }

    public void setMaxDayAmount(Long maxDayAmount) {
        this.maxDayAmount = maxDayAmount;
    }

    public Long getMaxEveryAmount() {
        return maxEveryAmount;
    }

    public void setMaxEveryAmount(Long maxEveryAmount) {
        this.maxEveryAmount = maxEveryAmount;
    }

    public Long getMinEveryAmount() {
        return minEveryAmount;
    }

    public void setMinEveryAmount(Long minEveryAmount) {
        this.minEveryAmount = minEveryAmount;
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
        sb.append(", feeScaleId=").append(feeScaleId);
        sb.append(", maxDayAmount=").append(maxDayAmount);
        sb.append(", maxEveryAmount=").append(maxEveryAmount);
        sb.append(", minEveryAmount=").append(minEveryAmount);
        sb.append(", tradeStartTime=").append(tradeStartTime);
        sb.append(", tradeEndTime=").append(tradeEndTime);
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
        FeeRiskConfig other = (FeeRiskConfig) that;
        return (this.getFeeScaleId() == null ? other.getFeeScaleId() == null : this.getFeeScaleId().equals(other.getFeeScaleId()))
            && (this.getMaxDayAmount() == null ? other.getMaxDayAmount() == null : this.getMaxDayAmount().equals(other.getMaxDayAmount()))
            && (this.getMaxEveryAmount() == null ? other.getMaxEveryAmount() == null : this.getMaxEveryAmount().equals(other.getMaxEveryAmount()))
            && (this.getMinEveryAmount() == null ? other.getMinEveryAmount() == null : this.getMinEveryAmount().equals(other.getMinEveryAmount()))
            && (this.getTradeStartTime() == null ? other.getTradeStartTime() == null : this.getTradeStartTime().equals(other.getTradeStartTime()))
            && (this.getTradeEndTime() == null ? other.getTradeEndTime() == null : this.getTradeEndTime().equals(other.getTradeEndTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFeeScaleId() == null) ? 0 : getFeeScaleId().hashCode());
        result = prime * result + ((getMaxDayAmount() == null) ? 0 : getMaxDayAmount().hashCode());
        result = prime * result + ((getMaxEveryAmount() == null) ? 0 : getMaxEveryAmount().hashCode());
        result = prime * result + ((getMinEveryAmount() == null) ? 0 : getMinEveryAmount().hashCode());
        result = prime * result + ((getTradeStartTime() == null) ? 0 : getTradeStartTime().hashCode());
        result = prime * result + ((getTradeEndTime() == null) ? 0 : getTradeEndTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
