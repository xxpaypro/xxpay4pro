package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_user_account")
public class UserAccount implements Serializable {
    /**
     * 用户ID
     *
     * @mbggenerated
     */
    @TableField("UserId")
    private String userId;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 账户余额
     *
     * @mbggenerated
     */
    @TableField("Balance")
    private Long balance;

    /**
     * 用户账户校验码
     *
     * @mbggenerated
     */
    @TableField("CheckSum")
    private String checkSum;

    /**
     * 账户更新时间
     *
     * @mbggenerated
     */
    @TableField("UpdateTimeJava")
    private Long updateTimeJava;

    /**
     * 用户累计转入金额
     *
     * @mbggenerated
     */
    @TableField("TotalRollIn")
    private Long totalRollIn;

    /**
     * 用户累计转出金额
     *
     * @mbggenerated
     */
    @TableField("TotalRollOut")
    private Long totalRollOut;

    /**
     * 账户可用余额
     *
     * @mbggenerated
     */
    @TableField("UseableBalance")
    private Long useableBalance;

    /**
     * 用户可用账户校验码
     *
     * @mbggenerated
     */
    @TableField("UseableCheckSum")
    private String useableCheckSum;

    /**
     * 可用账户更新时间
     *
     * @mbggenerated
     */
    @TableField("UseableUpdateTimeJava")
    private Long useableUpdateTimeJava;

    /**
     * 用户累计转入可用金额
     *
     * @mbggenerated
     */
    @TableField("UseableRollIn")
    private Long useableRollIn;

    /**
     * 用户累计转出可用金额
     *
     * @mbggenerated
     */
    @TableField("UseableRollOut")
    private Long useableRollOut;

    /**
     * 状态.0表示账户冻结.1表示正常
     *
     * @mbggenerated
     */
    @TableField("State")
    private Short state;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(String checkSum) {
        this.checkSum = checkSum;
    }

    public Long getUpdateTimeJava() {
        return updateTimeJava;
    }

    public void setUpdateTimeJava(Long updateTimeJava) {
        this.updateTimeJava = updateTimeJava;
    }

    public Long getTotalRollIn() {
        return totalRollIn;
    }

    public void setTotalRollIn(Long totalRollIn) {
        this.totalRollIn = totalRollIn;
    }

    public Long getTotalRollOut() {
        return totalRollOut;
    }

    public void setTotalRollOut(Long totalRollOut) {
        this.totalRollOut = totalRollOut;
    }

    public Long getUseableBalance() {
        return useableBalance;
    }

    public void setUseableBalance(Long useableBalance) {
        this.useableBalance = useableBalance;
    }

    public String getUseableCheckSum() {
        return useableCheckSum;
    }

    public void setUseableCheckSum(String useableCheckSum) {
        this.useableCheckSum = useableCheckSum;
    }

    public Long getUseableUpdateTimeJava() {
        return useableUpdateTimeJava;
    }

    public void setUseableUpdateTimeJava(Long useableUpdateTimeJava) {
        this.useableUpdateTimeJava = useableUpdateTimeJava;
    }

    public Long getUseableRollIn() {
        return useableRollIn;
    }

    public void setUseableRollIn(Long useableRollIn) {
        this.useableRollIn = useableRollIn;
    }

    public Long getUseableRollOut() {
        return useableRollOut;
    }

    public void setUseableRollOut(Long useableRollOut) {
        this.useableRollOut = useableRollOut;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
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
        sb.append(", userId=").append(userId);
        sb.append(", mchId=").append(mchId);
        sb.append(", balance=").append(balance);
        sb.append(", checkSum=").append(checkSum);
        sb.append(", updateTimeJava=").append(updateTimeJava);
        sb.append(", totalRollIn=").append(totalRollIn);
        sb.append(", totalRollOut=").append(totalRollOut);
        sb.append(", useableBalance=").append(useableBalance);
        sb.append(", useableCheckSum=").append(useableCheckSum);
        sb.append(", useableUpdateTimeJava=").append(useableUpdateTimeJava);
        sb.append(", useableRollIn=").append(useableRollIn);
        sb.append(", useableRollOut=").append(useableRollOut);
        sb.append(", state=").append(state);
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
        UserAccount other = (UserAccount) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
            && (this.getCheckSum() == null ? other.getCheckSum() == null : this.getCheckSum().equals(other.getCheckSum()))
            && (this.getUpdateTimeJava() == null ? other.getUpdateTimeJava() == null : this.getUpdateTimeJava().equals(other.getUpdateTimeJava()))
            && (this.getTotalRollIn() == null ? other.getTotalRollIn() == null : this.getTotalRollIn().equals(other.getTotalRollIn()))
            && (this.getTotalRollOut() == null ? other.getTotalRollOut() == null : this.getTotalRollOut().equals(other.getTotalRollOut()))
            && (this.getUseableBalance() == null ? other.getUseableBalance() == null : this.getUseableBalance().equals(other.getUseableBalance()))
            && (this.getUseableCheckSum() == null ? other.getUseableCheckSum() == null : this.getUseableCheckSum().equals(other.getUseableCheckSum()))
            && (this.getUseableUpdateTimeJava() == null ? other.getUseableUpdateTimeJava() == null : this.getUseableUpdateTimeJava().equals(other.getUseableUpdateTimeJava()))
            && (this.getUseableRollIn() == null ? other.getUseableRollIn() == null : this.getUseableRollIn().equals(other.getUseableRollIn()))
            && (this.getUseableRollOut() == null ? other.getUseableRollOut() == null : this.getUseableRollOut().equals(other.getUseableRollOut()))
            && (this.getState() == null ? other.getState() == null : this.getState().equals(other.getState()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getCheckSum() == null) ? 0 : getCheckSum().hashCode());
        result = prime * result + ((getUpdateTimeJava() == null) ? 0 : getUpdateTimeJava().hashCode());
        result = prime * result + ((getTotalRollIn() == null) ? 0 : getTotalRollIn().hashCode());
        result = prime * result + ((getTotalRollOut() == null) ? 0 : getTotalRollOut().hashCode());
        result = prime * result + ((getUseableBalance() == null) ? 0 : getUseableBalance().hashCode());
        result = prime * result + ((getUseableCheckSum() == null) ? 0 : getUseableCheckSum().hashCode());
        result = prime * result + ((getUseableUpdateTimeJava() == null) ? 0 : getUseableUpdateTimeJava().hashCode());
        result = prime * result + ((getUseableRollIn() == null) ? 0 : getUseableRollIn().hashCode());
        result = prime * result + ((getUseableRollOut() == null) ? 0 : getUseableRollOut().hashCode());
        result = prime * result + ((getState() == null) ? 0 : getState().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
