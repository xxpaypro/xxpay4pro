package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_account_history")
public class AccountHistory extends BaseModel {
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
     * 类型: 1-商户 2-代理商 3-平台
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 变动金额
     *
     * @mbggenerated
     */
    @TableField("ChangeAmount")
    private Long changeAmount;

    /**
     * 变更前账户余额
     *
     * @mbggenerated
     */
    @TableField("Balance")
    private Long balance;

    /**
     * 变更后账户余额
     *
     * @mbggenerated
     */
    @TableField("AfterBalance")
    private Long afterBalance;

    /**
     * 平台订单号
     *
     * @mbggenerated
     */
    @TableField("BizOrderId")
    private String bizOrderId;

    /**
     * 渠道订单号
     *
     * @mbggenerated
     */
    @TableField("BizChannelOrderNo")
    private String bizChannelOrderNo;

    /**
     * 订单金额
     *
     * @mbggenerated
     */
    @TableField("BizOrderAmount")
    private Long bizOrderAmount;

    /**
     * 手续费
     *
     * @mbggenerated
     */
    @TableField("BizOrderFee")
    private Long bizOrderFee;

    /**
     * 资金变动方向,1-加款,2-减款
     *
     * @mbggenerated
     */
    @TableField("FundDirection")
    private Byte fundDirection;

    /**
     * 业务类型,1-支付,2-提现,3-调账,4-充值,5-差错处理,6-代付 7-分润
     *
     * @mbggenerated
     */
    @TableField("BizType")
    private Byte bizType;

    /**
     * 业务类目:10-余额,11-代付余额,12-冻结金额,13-保证金,20-支付,21-代付,22-线下充值,23-线上充值
     *
     * @mbggenerated
     */
    @TableField("BizItem")
    private String bizItem;

    /**
     * 变更账户类型 1-账户余额 2-代付余额 3-保证金
     *
     * @mbggenerated
     */
    @TableField("ChangeAccountType")
    private Byte changeAccountType;

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

    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
    }

    public String getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(String bizOrderId) {
        this.bizOrderId = bizOrderId;
    }

    public String getBizChannelOrderNo() {
        return bizChannelOrderNo;
    }

    public void setBizChannelOrderNo(String bizChannelOrderNo) {
        this.bizChannelOrderNo = bizChannelOrderNo;
    }

    public Long getBizOrderAmount() {
        return bizOrderAmount;
    }

    public void setBizOrderAmount(Long bizOrderAmount) {
        this.bizOrderAmount = bizOrderAmount;
    }

    public Long getBizOrderFee() {
        return bizOrderFee;
    }

    public void setBizOrderFee(Long bizOrderFee) {
        this.bizOrderFee = bizOrderFee;
    }

    public Byte getFundDirection() {
        return fundDirection;
    }

    public void setFundDirection(Byte fundDirection) {
        this.fundDirection = fundDirection;
    }

    public Byte getBizType() {
        return bizType;
    }

    public void setBizType(Byte bizType) {
        this.bizType = bizType;
    }

    public String getBizItem() {
        return bizItem;
    }

    public void setBizItem(String bizItem) {
        this.bizItem = bizItem;
    }

    public Byte getChangeAccountType() {
        return changeAccountType;
    }

    public void setChangeAccountType(Byte changeAccountType) {
        this.changeAccountType = changeAccountType;
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
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", changeAmount=").append(changeAmount);
        sb.append(", balance=").append(balance);
        sb.append(", afterBalance=").append(afterBalance);
        sb.append(", bizOrderId=").append(bizOrderId);
        sb.append(", bizChannelOrderNo=").append(bizChannelOrderNo);
        sb.append(", bizOrderAmount=").append(bizOrderAmount);
        sb.append(", bizOrderFee=").append(bizOrderFee);
        sb.append(", fundDirection=").append(fundDirection);
        sb.append(", bizType=").append(bizType);
        sb.append(", bizItem=").append(bizItem);
        sb.append(", changeAccountType=").append(changeAccountType);
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
        AccountHistory other = (AccountHistory) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
                && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
                && (this.getChangeAmount() == null ? other.getChangeAmount() == null : this.getChangeAmount().equals(other.getChangeAmount()))
                && (this.getBalance() == null ? other.getBalance() == null : this.getBalance().equals(other.getBalance()))
                && (this.getAfterBalance() == null ? other.getAfterBalance() == null : this.getAfterBalance().equals(other.getAfterBalance()))
                && (this.getBizOrderId() == null ? other.getBizOrderId() == null : this.getBizOrderId().equals(other.getBizOrderId()))
                && (this.getBizChannelOrderNo() == null ? other.getBizChannelOrderNo() == null : this.getBizChannelOrderNo().equals(other.getBizChannelOrderNo()))
                && (this.getBizOrderAmount() == null ? other.getBizOrderAmount() == null : this.getBizOrderAmount().equals(other.getBizOrderAmount()))
                && (this.getBizOrderFee() == null ? other.getBizOrderFee() == null : this.getBizOrderFee().equals(other.getBizOrderFee()))
                && (this.getFundDirection() == null ? other.getFundDirection() == null : this.getFundDirection().equals(other.getFundDirection()))
                && (this.getBizType() == null ? other.getBizType() == null : this.getBizType().equals(other.getBizType()))
                && (this.getBizItem() == null ? other.getBizItem() == null : this.getBizItem().equals(other.getBizItem()))
                && (this.getChangeAccountType() == null ? other.getChangeAccountType() == null : this.getChangeAccountType().equals(other.getChangeAccountType()))
                && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getChangeAmount() == null) ? 0 : getChangeAmount().hashCode());
        result = prime * result + ((getBalance() == null) ? 0 : getBalance().hashCode());
        result = prime * result + ((getAfterBalance() == null) ? 0 : getAfterBalance().hashCode());
        result = prime * result + ((getBizOrderId() == null) ? 0 : getBizOrderId().hashCode());
        result = prime * result + ((getBizChannelOrderNo() == null) ? 0 : getBizChannelOrderNo().hashCode());
        result = prime * result + ((getBizOrderAmount() == null) ? 0 : getBizOrderAmount().hashCode());
        result = prime * result + ((getBizOrderFee() == null) ? 0 : getBizOrderFee().hashCode());
        result = prime * result + ((getFundDirection() == null) ? 0 : getFundDirection().hashCode());
        result = prime * result + ((getBizType() == null) ? 0 : getBizType().hashCode());
        result = prime * result + ((getBizItem() == null) ? 0 : getBizItem().hashCode());
        result = prime * result + ((getChangeAccountType() == null) ? 0 : getChangeAccountType().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
