package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_user_account_log")
public class UserAccountLog implements Serializable {
    @TableField("LogId")
    private Long logId;

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
     * 变动金额
     *
     * @mbggenerated
     */
    @TableField("ChangeAmount")
    private Long changeAmount;

    /**
     * 账户变动类型.0表示转入.1表示转出.2表示初始化
     *
     * @mbggenerated
     */
    @TableField("Type")
    private Short type;

    /**
     * 账户类型 0:总账户 1:可用账户
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Short accountType;

    /**
     * 变动前账户余额
     *
     * @mbggenerated
     */
    @TableField("OldBalance")
    private Long oldBalance;

    /**
     * 变动前账户更新时间
     *
     * @mbggenerated
     */
    @TableField("OldUpdateTimeJava")
    private Long oldUpdateTimeJava;

    /**
     * 变动前用户账户校验码
     *
     * @mbggenerated
     */
    @TableField("OldCheckSum")
    private String oldCheckSum;

    /**
     * 变动后账户余额
     *
     * @mbggenerated
     */
    @TableField("NewBalance")
    private Long newBalance;

    /**
     * 变动后账户更新时间
     *
     * @mbggenerated
     */
    @TableField("NewUpdateTimeJava")
    private Long newUpdateTimeJava;

    /**
     * 变动后用户账户校验码
     *
     * @mbggenerated
     */
    @TableField("NewCheckSum")
    private String newCheckSum;

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

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

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

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getAccountType() {
        return accountType;
    }

    public void setAccountType(Short accountType) {
        this.accountType = accountType;
    }

    public Long getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(Long oldBalance) {
        this.oldBalance = oldBalance;
    }

    public Long getOldUpdateTimeJava() {
        return oldUpdateTimeJava;
    }

    public void setOldUpdateTimeJava(Long oldUpdateTimeJava) {
        this.oldUpdateTimeJava = oldUpdateTimeJava;
    }

    public String getOldCheckSum() {
        return oldCheckSum;
    }

    public void setOldCheckSum(String oldCheckSum) {
        this.oldCheckSum = oldCheckSum;
    }

    public Long getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(Long newBalance) {
        this.newBalance = newBalance;
    }

    public Long getNewUpdateTimeJava() {
        return newUpdateTimeJava;
    }

    public void setNewUpdateTimeJava(Long newUpdateTimeJava) {
        this.newUpdateTimeJava = newUpdateTimeJava;
    }

    public String getNewCheckSum() {
        return newCheckSum;
    }

    public void setNewCheckSum(String newCheckSum) {
        this.newCheckSum = newCheckSum;
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
        sb.append(", logId=").append(logId);
        sb.append(", userId=").append(userId);
        sb.append(", mchId=").append(mchId);
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", type=").append(type);
        sb.append(", accountType=").append(accountType);
        sb.append(", oldBalance=").append(oldBalance);
        sb.append(", oldUpdateTimeJava=").append(oldUpdateTimeJava);
        sb.append(", oldCheckSum=").append(oldCheckSum);
        sb.append(", newBalance=").append(newBalance);
        sb.append(", newUpdateTimeJava=").append(newUpdateTimeJava);
        sb.append(", newCheckSum=").append(newCheckSum);
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
        UserAccountLog other = (UserAccountLog) that;
        return (this.getLogId() == null ? other.getLogId() == null : this.getLogId().equals(other.getLogId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getChangeAmount() == null ? other.getChangeAmount() == null : this.getChangeAmount().equals(other.getChangeAmount()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
            && (this.getOldBalance() == null ? other.getOldBalance() == null : this.getOldBalance().equals(other.getOldBalance()))
            && (this.getOldUpdateTimeJava() == null ? other.getOldUpdateTimeJava() == null : this.getOldUpdateTimeJava().equals(other.getOldUpdateTimeJava()))
            && (this.getOldCheckSum() == null ? other.getOldCheckSum() == null : this.getOldCheckSum().equals(other.getOldCheckSum()))
            && (this.getNewBalance() == null ? other.getNewBalance() == null : this.getNewBalance().equals(other.getNewBalance()))
            && (this.getNewUpdateTimeJava() == null ? other.getNewUpdateTimeJava() == null : this.getNewUpdateTimeJava().equals(other.getNewUpdateTimeJava()))
            && (this.getNewCheckSum() == null ? other.getNewCheckSum() == null : this.getNewCheckSum().equals(other.getNewCheckSum()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLogId() == null) ? 0 : getLogId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getChangeAmount() == null) ? 0 : getChangeAmount().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getOldBalance() == null) ? 0 : getOldBalance().hashCode());
        result = prime * result + ((getOldUpdateTimeJava() == null) ? 0 : getOldUpdateTimeJava().hashCode());
        result = prime * result + ((getOldCheckSum() == null) ? 0 : getOldCheckSum().hashCode());
        result = prime * result + ((getNewBalance() == null) ? 0 : getNewBalance().hashCode());
        result = prime * result + ((getNewUpdateTimeJava() == null) ? 0 : getNewUpdateTimeJava().hashCode());
        result = prime * result + ((getNewCheckSum() == null) ? 0 : getNewCheckSum().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
