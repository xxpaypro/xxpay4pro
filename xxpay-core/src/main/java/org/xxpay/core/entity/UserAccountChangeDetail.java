package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_user_account_change_detail")
public class UserAccountChangeDetail implements Serializable {
    @TableId(type= IdType.AUTO)
    private Long id;

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
     * 收入日期，格式：yyyymmdd
     *
     * @mbggenerated
     */
    @TableField("ChangeDay")
    private Integer changeDay;

    /**
     * 账户变动类型.0表示转入.1表示转出
     *
     * @mbggenerated
     */
    @TableField("ChangeType")
    private Short changeType;

    /**
     * 变动账户类型 0:账户 1:可用账户
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Short accountType;

    /**
     * 账户变动金额
     *
     * @mbggenerated
     */
    @TableField("ChangeAmount")
    private Long changeAmount;

    /**
     * 变动日志记录ID
     *
     * @mbggenerated
     */
    @TableField("ChangeLogId")
    private Long changeLogId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getChangeDay() {
        return changeDay;
    }

    public void setChangeDay(Integer changeDay) {
        this.changeDay = changeDay;
    }

    public Short getChangeType() {
        return changeType;
    }

    public void setChangeType(Short changeType) {
        this.changeType = changeType;
    }

    public Short getAccountType() {
        return accountType;
    }

    public void setAccountType(Short accountType) {
        this.accountType = accountType;
    }

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getChangeLogId() {
        return changeLogId;
    }

    public void setChangeLogId(Long changeLogId) {
        this.changeLogId = changeLogId;
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
        sb.append(", userId=").append(userId);
        sb.append(", mchId=").append(mchId);
        sb.append(", changeDay=").append(changeDay);
        sb.append(", changeType=").append(changeType);
        sb.append(", accountType=").append(accountType);
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", changeLogId=").append(changeLogId);
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
        UserAccountChangeDetail other = (UserAccountChangeDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getChangeDay() == null ? other.getChangeDay() == null : this.getChangeDay().equals(other.getChangeDay()))
            && (this.getChangeType() == null ? other.getChangeType() == null : this.getChangeType().equals(other.getChangeType()))
            && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
            && (this.getChangeAmount() == null ? other.getChangeAmount() == null : this.getChangeAmount().equals(other.getChangeAmount()))
            && (this.getChangeLogId() == null ? other.getChangeLogId() == null : this.getChangeLogId().equals(other.getChangeLogId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getChangeDay() == null) ? 0 : getChangeDay().hashCode());
        result = prime * result + ((getChangeType() == null) ? 0 : getChangeType().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getChangeAmount() == null) ? 0 : getChangeAmount().hashCode());
        result = prime * result + ((getChangeLogId() == null) ? 0 : getChangeLogId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
