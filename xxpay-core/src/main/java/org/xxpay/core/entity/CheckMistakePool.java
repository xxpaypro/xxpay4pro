package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_check_mistake_pool")
public class CheckMistakePool implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 商品名称
     *
     * @mbggenerated
     */
    @TableField("Productame")
    private String productame;

    /**
     * 商户订单号
     *
     * @mbggenerated
     */
    @TableField("MchOrderNo")
    private String mchOrderNo;

    /**
     * 平台订单ID
     *
     * @mbggenerated
     */
    @TableField("OrderId")
    private String orderId;

    /**
     * 银行订单号
     *
     * @mbggenerated
     */
    @TableField("BankOrderNo")
    private String bankOrderNo;

    /**
     * 订单金额
     *
     * @mbggenerated
     */
    @TableField("OrderAmount")
    private Long orderAmount;

    /**
     * 平台收入
     *
     * @mbggenerated
     */
    @TableField("PlatIncome")
    private Long platIncome;

    /**
     * 平台费率
     *
     * @mbggenerated
     */
    @TableField("Feeate")
    private BigDecimal feeate;

    /**
     * 平台成本
     *
     * @mbggenerated
     */
    @TableField("PlatCost")
    private Long platCost;

    /**
     * 平台利润
     *
     * @mbggenerated
     */
    @TableField("PlatProfit")
    private Long platProfit;

    /**
     * 状态
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 渠道ID
     *
     * @mbggenerated
     */
    @TableField("ChannelId")
    private String channelId;

    /**
     * 渠道类型,WX:微信,ALIPAY:支付宝
     *
     * @mbggenerated
     */
    @TableField("ChannelType")
    private String channelType;

    /**
     * 支付成功时间
     *
     * @mbggenerated
     */
    @TableField("PaySuccessTime")
    private Date paySuccessTime;

    /**
     * 完成时间
     *
     * @mbggenerated
     */
    @TableField("CompleteTime")
    private Date completeTime;

    /**
     * 类型:0-否,1-是
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
     * 成功退款总金额
     *
     * @mbggenerated
     */
    @TableField("SuccessRefundAmount")
    private Long successRefundAmount;

    /**
     * 备注
     *
     * @mbggenerated
     */
    @TableField("Remark")
    private String remark;

    /**
     * 对账批次号
     *
     * @mbggenerated
     */
    @TableField("BatchNo")
    private String batchNo;

    /**
     * 账单时间(账单交易发生时间)
     *
     * @mbggenerated
     */
    @TableField("BillDate")
    private Date billDate;

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

    public String getProductame() {
        return productame;
    }

    public void setProductame(String productame) {
        this.productame = productame;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getPlatIncome() {
        return platIncome;
    }

    public void setPlatIncome(Long platIncome) {
        this.platIncome = platIncome;
    }

    public BigDecimal getFeeate() {
        return feeate;
    }

    public void setFeeate(BigDecimal feeate) {
        this.feeate = feeate;
    }

    public Long getPlatCost() {
        return platCost;
    }

    public void setPlatCost(Long platCost) {
        this.platCost = platCost;
    }

    public Long getPlatProfit() {
        return platProfit;
    }

    public void setPlatProfit(Long platProfit) {
        this.platProfit = platProfit;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public Date getPaySuccessTime() {
        return paySuccessTime;
    }

    public void setPaySuccessTime(Date paySuccessTime) {
        this.paySuccessTime = paySuccessTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
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
        sb.append(", productame=").append(productame);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", orderId=").append(orderId);
        sb.append(", bankOrderNo=").append(bankOrderNo);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", platIncome=").append(platIncome);
        sb.append(", feeate=").append(feeate);
        sb.append(", platCost=").append(platCost);
        sb.append(", platProfit=").append(platProfit);
        sb.append(", status=").append(status);
        sb.append(", channelId=").append(channelId);
        sb.append(", channelType=").append(channelType);
        sb.append(", paySuccessTime=").append(paySuccessTime);
        sb.append(", completeTime=").append(completeTime);
        sb.append(", isRefund=").append(isRefund);
        sb.append(", refundTimes=").append(refundTimes);
        sb.append(", successRefundAmount=").append(successRefundAmount);
        sb.append(", remark=").append(remark);
        sb.append(", batchNo=").append(batchNo);
        sb.append(", billDate=").append(billDate);
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
        CheckMistakePool other = (CheckMistakePool) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProductame() == null ? other.getProductame() == null : this.getProductame().equals(other.getProductame()))
            && (this.getMchOrderNo() == null ? other.getMchOrderNo() == null : this.getMchOrderNo().equals(other.getMchOrderNo()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getBankOrderNo() == null ? other.getBankOrderNo() == null : this.getBankOrderNo().equals(other.getBankOrderNo()))
            && (this.getOrderAmount() == null ? other.getOrderAmount() == null : this.getOrderAmount().equals(other.getOrderAmount()))
            && (this.getPlatIncome() == null ? other.getPlatIncome() == null : this.getPlatIncome().equals(other.getPlatIncome()))
            && (this.getFeeate() == null ? other.getFeeate() == null : this.getFeeate().equals(other.getFeeate()))
            && (this.getPlatCost() == null ? other.getPlatCost() == null : this.getPlatCost().equals(other.getPlatCost()))
            && (this.getPlatProfit() == null ? other.getPlatProfit() == null : this.getPlatProfit().equals(other.getPlatProfit()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getChannelId() == null ? other.getChannelId() == null : this.getChannelId().equals(other.getChannelId()))
            && (this.getChannelType() == null ? other.getChannelType() == null : this.getChannelType().equals(other.getChannelType()))
            && (this.getPaySuccessTime() == null ? other.getPaySuccessTime() == null : this.getPaySuccessTime().equals(other.getPaySuccessTime()))
            && (this.getCompleteTime() == null ? other.getCompleteTime() == null : this.getCompleteTime().equals(other.getCompleteTime()))
            && (this.getIsRefund() == null ? other.getIsRefund() == null : this.getIsRefund().equals(other.getIsRefund()))
            && (this.getRefundTimes() == null ? other.getRefundTimes() == null : this.getRefundTimes().equals(other.getRefundTimes()))
            && (this.getSuccessRefundAmount() == null ? other.getSuccessRefundAmount() == null : this.getSuccessRefundAmount().equals(other.getSuccessRefundAmount()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getBillDate() == null ? other.getBillDate() == null : this.getBillDate().equals(other.getBillDate()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductame() == null) ? 0 : getProductame().hashCode());
        result = prime * result + ((getMchOrderNo() == null) ? 0 : getMchOrderNo().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getBankOrderNo() == null) ? 0 : getBankOrderNo().hashCode());
        result = prime * result + ((getOrderAmount() == null) ? 0 : getOrderAmount().hashCode());
        result = prime * result + ((getPlatIncome() == null) ? 0 : getPlatIncome().hashCode());
        result = prime * result + ((getFeeate() == null) ? 0 : getFeeate().hashCode());
        result = prime * result + ((getPlatCost() == null) ? 0 : getPlatCost().hashCode());
        result = prime * result + ((getPlatProfit() == null) ? 0 : getPlatProfit().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getChannelId() == null) ? 0 : getChannelId().hashCode());
        result = prime * result + ((getChannelType() == null) ? 0 : getChannelType().hashCode());
        result = prime * result + ((getPaySuccessTime() == null) ? 0 : getPaySuccessTime().hashCode());
        result = prime * result + ((getCompleteTime() == null) ? 0 : getCompleteTime().hashCode());
        result = prime * result + ((getIsRefund() == null) ? 0 : getIsRefund().hashCode());
        result = prime * result + ((getRefundTimes() == null) ? 0 : getRefundTimes().hashCode());
        result = prime * result + ((getSuccessRefundAmount() == null) ? 0 : getSuccessRefundAmount().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getBillDate() == null) ? 0 : getBillDate().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
