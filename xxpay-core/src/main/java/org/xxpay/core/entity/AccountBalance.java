package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_account_balance")
public class AccountBalance implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 代理商或商户ID
     *
     * @mbggenerated
     */
    @TableField("InfoId")
    private Long infoId;

    /**
     * 类型:1-商户 2-代理商 3-平台
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 名称
     *
     * @mbggenerated
     */
    @TableField("InfoName")
    private String infoName;

    /**
     * 账户金额类型 1-账户余额 2-代付余额 3-保证金
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Byte accountType;

    /**
     * 金额 单位:分
     *
     * @mbggenerated
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 不可用金额 单位:分（业务中间状态时产生的金额）
     *
     * @mbggenerated
     */
    @TableField("UnAmount")
    private Long unAmount;

    /**
     * 平台冻结金额 单位:分
     *
     * @mbggenerated
     */
    @TableField("FrozenAmount")
    private Long frozenAmount;

    /**
     * 可提现金额 单位:分
     *
     * @mbggenerated
     */
    @TableField("SettAmount")
    private Long settAmount;

    /**
     * 针对Amount字段的累计增加金额（用于统计，仅支持累加操作，不支持扣减修改） 单位:分
     *
     * @mbggenerated
     */
    @TableField("TotalAddAmount")
    private Long totalAddAmount;

    /**
     * 针对Amount字段的累计减少金额（用于统计，仅支持累加操作，不支持扣减修改） 单位:分
     *
     * @mbggenerated
     */
    @TableField("TotalSubAmount")
    private Long totalSubAmount;

    /**
     * 账户状态,1-可用,0-停止使用
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 数据安全保护秘钥
     *
     * @mbggenerated
     */
    @TableField("SafeKey")
    private String safeKey;

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

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getUnAmount() {
        return unAmount;
    }

    public void setUnAmount(Long unAmount) {
        this.unAmount = unAmount;
    }

    public Long getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Long frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Long getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(Long settAmount) {
        this.settAmount = settAmount;
    }

    public Long getTotalAddAmount() {
        return totalAddAmount;
    }

    public void setTotalAddAmount(Long totalAddAmount) {
        this.totalAddAmount = totalAddAmount;
    }

    public Long getTotalSubAmount() {
        return totalSubAmount;
    }

    public void setTotalSubAmount(Long totalSubAmount) {
        this.totalSubAmount = totalSubAmount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getSafeKey() {
        return safeKey;
    }

    public void setSafeKey(String safeKey) {
        this.safeKey = safeKey;
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
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", infoName=").append(infoName);
        sb.append(", accountType=").append(accountType);
        sb.append(", amount=").append(amount);
        sb.append(", unAmount=").append(unAmount);
        sb.append(", frozenAmount=").append(frozenAmount);
        sb.append(", settAmount=").append(settAmount);
        sb.append(", totalAddAmount=").append(totalAddAmount);
        sb.append(", totalSubAmount=").append(totalSubAmount);
        sb.append(", status=").append(status);
        sb.append(", safeKey=").append(safeKey);
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
        AccountBalance other = (AccountBalance) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
                && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
                && (this.getInfoName() == null ? other.getInfoName() == null : this.getInfoName().equals(other.getInfoName()))
                && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
                && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
                && (this.getUnAmount() == null ? other.getUnAmount() == null : this.getUnAmount().equals(other.getUnAmount()))
                && (this.getFrozenAmount() == null ? other.getFrozenAmount() == null : this.getFrozenAmount().equals(other.getFrozenAmount()))
                && (this.getSettAmount() == null ? other.getSettAmount() == null : this.getSettAmount().equals(other.getSettAmount()))
                && (this.getTotalAddAmount() == null ? other.getTotalAddAmount() == null : this.getTotalAddAmount().equals(other.getTotalAddAmount()))
                && (this.getTotalSubAmount() == null ? other.getTotalSubAmount() == null : this.getTotalSubAmount().equals(other.getTotalSubAmount()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getSafeKey() == null ? other.getSafeKey() == null : this.getSafeKey().equals(other.getSafeKey()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getInfoName() == null) ? 0 : getInfoName().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getUnAmount() == null) ? 0 : getUnAmount().hashCode());
        result = prime * result + ((getFrozenAmount() == null) ? 0 : getFrozenAmount().hashCode());
        result = prime * result + ((getSettAmount() == null) ? 0 : getSettAmount().hashCode());
        result = prime * result + ((getTotalAddAmount() == null) ? 0 : getTotalAddAmount().hashCode());
        result = prime * result + ((getTotalSubAmount() == null) ? 0 : getTotalSubAmount().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSafeKey() == null) ? 0 : getSafeKey().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
