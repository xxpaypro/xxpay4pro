package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_check_mistake")
public class CheckMistake implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

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
     * 账单类型(默认全部是交易成功)
     *
     * @mbggenerated
     */
    @TableField("BillType")
    private Byte billType;

    /**
     * 银行类型,wxpay:微信,alipay:支付宝
     *
     * @mbggenerated
     */
    @TableField("BankType")
    private String bankType;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    @TableField("ChannelMchId")
    private String channelMchId;

    /**
     * 下单时间
     *
     * @mbggenerated
     */
    @TableField("OrderTime")
    private Date orderTime;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 商户名称
     *
     * @mbggenerated
     */
    @TableField("MchName")
    private String mchName;

    /**
     * 商户订单号
     *
     * @mbggenerated
     */
    @TableField("MchOrderNo")
    private String mchOrderNo;

    /**
     * 平台交易时间
     *
     * @mbggenerated
     */
    @TableField("TradeTime")
    private Date tradeTime;

    /**
     * 平台订单ID
     *
     * @mbggenerated
     */
    @TableField("OrderId")
    private String orderId;

    /**
     * 平台交易金额
     *
     * @mbggenerated
     */
    @TableField("OrderAmount")
    private Long orderAmount;

    /**
     * 平台退款金额
     *
     * @mbggenerated
     */
    @TableField("RefundAmount")
    private Long refundAmount;

    /**
     * 平台订单状态
     *
     * @mbggenerated
     */
    @TableField("OrderStatus")
    private Byte orderStatus;

    /**
     * 平台手续费
     *
     * @mbggenerated
     */
    @TableField("Fee")
    private Long fee;

    /**
     * 银行交易时间
     *
     * @mbggenerated
     */
    @TableField("BankTradeTime")
    private Date bankTradeTime;

    /**
     * 银行订单号
     *
     * @mbggenerated
     */
    @TableField("BankOrderNo")
    private String bankOrderNo;

    /**
     * 银行订单状态
     *
     * @mbggenerated
     */
    @TableField("BankOrderStatus")
    private String bankOrderStatus;

    /**
     * 银行交易金额
     *
     * @mbggenerated
     */
    @TableField("BankAmount")
    private Long bankAmount;

    /**
     * 银行退款金额
     *
     * @mbggenerated
     */
    @TableField("BankRefundAmount")
    private Long bankRefundAmount;

    /**
     * 银行手续费
     *
     * @mbggenerated
     */
    @TableField("BankFee")
    private Long bankFee;

    /**
     * 差错类型:1
     *
     * @mbggenerated
     */
    @TableField("ErrType")
    private Byte errType;

    /**
     * 类型:0-未处理,1-已处理
     *
     * @mbggenerated
     */
    @TableField("HandleStatus")
    private Byte handleStatus;

    /**
     * 处理结果
     *
     * @mbggenerated
     */
    @TableField("HandleValue")
    private String handleValue;

    /**
     * 处理备注
     *
     * @mbggenerated
     */
    @TableField("HandleRemark")
    private String handleRemark;

    /**
     * 操作人姓名
     *
     * @mbggenerated
     */
    @TableField("OperatorName")
    private String operatorName;

    /**
     * 操作人用户ID
     *
     * @mbggenerated
     */
    @TableField("OperatorUserId")
    private String operatorUserId;

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

    public Byte getBillType() {
        return billType;
    }

    public void setBillType(Byte billType) {
        this.billType = billType;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Long orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Date getBankTradeTime() {
        return bankTradeTime;
    }

    public void setBankTradeTime(Date bankTradeTime) {
        this.bankTradeTime = bankTradeTime;
    }

    public String getBankOrderNo() {
        return bankOrderNo;
    }

    public void setBankOrderNo(String bankOrderNo) {
        this.bankOrderNo = bankOrderNo;
    }

    public String getBankOrderStatus() {
        return bankOrderStatus;
    }

    public void setBankOrderStatus(String bankOrderStatus) {
        this.bankOrderStatus = bankOrderStatus;
    }

    public Long getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(Long bankAmount) {
        this.bankAmount = bankAmount;
    }

    public Long getBankRefundAmount() {
        return bankRefundAmount;
    }

    public void setBankRefundAmount(Long bankRefundAmount) {
        this.bankRefundAmount = bankRefundAmount;
    }

    public Long getBankFee() {
        return bankFee;
    }

    public void setBankFee(Long bankFee) {
        this.bankFee = bankFee;
    }

    public Byte getErrType() {
        return errType;
    }

    public void setErrType(Byte errType) {
        this.errType = errType;
    }

    public Byte getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Byte handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleValue() {
        return handleValue;
    }

    public void setHandleValue(String handleValue) {
        this.handleValue = handleValue;
    }

    public String getHandleRemark() {
        return handleRemark;
    }

    public void setHandleRemark(String handleRemark) {
        this.handleRemark = handleRemark;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOperatorUserId() {
        return operatorUserId;
    }

    public void setOperatorUserId(String operatorUserId) {
        this.operatorUserId = operatorUserId;
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
        sb.append(", batchNo=").append(batchNo);
        sb.append(", billDate=").append(billDate);
        sb.append(", billType=").append(billType);
        sb.append(", bankType=").append(bankType);
        sb.append(", channelMchId=").append(channelMchId);
        sb.append(", orderTime=").append(orderTime);
        sb.append(", mchId=").append(mchId);
        sb.append(", mchName=").append(mchName);
        sb.append(", mchOrderNo=").append(mchOrderNo);
        sb.append(", tradeTime=").append(tradeTime);
        sb.append(", orderId=").append(orderId);
        sb.append(", orderAmount=").append(orderAmount);
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", fee=").append(fee);
        sb.append(", bankTradeTime=").append(bankTradeTime);
        sb.append(", bankOrderNo=").append(bankOrderNo);
        sb.append(", bankOrderStatus=").append(bankOrderStatus);
        sb.append(", bankAmount=").append(bankAmount);
        sb.append(", bankRefundAmount=").append(bankRefundAmount);
        sb.append(", bankFee=").append(bankFee);
        sb.append(", errType=").append(errType);
        sb.append(", handleStatus=").append(handleStatus);
        sb.append(", handleValue=").append(handleValue);
        sb.append(", handleRemark=").append(handleRemark);
        sb.append(", operatorName=").append(operatorName);
        sb.append(", operatorUserId=").append(operatorUserId);
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
        CheckMistake other = (CheckMistake) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getBillDate() == null ? other.getBillDate() == null : this.getBillDate().equals(other.getBillDate()))
            && (this.getBillType() == null ? other.getBillType() == null : this.getBillType().equals(other.getBillType()))
            && (this.getBankType() == null ? other.getBankType() == null : this.getBankType().equals(other.getBankType()))
            && (this.getChannelMchId() == null ? other.getChannelMchId() == null : this.getChannelMchId().equals(other.getChannelMchId()))
            && (this.getOrderTime() == null ? other.getOrderTime() == null : this.getOrderTime().equals(other.getOrderTime()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getMchName() == null ? other.getMchName() == null : this.getMchName().equals(other.getMchName()))
            && (this.getMchOrderNo() == null ? other.getMchOrderNo() == null : this.getMchOrderNo().equals(other.getMchOrderNo()))
            && (this.getTradeTime() == null ? other.getTradeTime() == null : this.getTradeTime().equals(other.getTradeTime()))
            && (this.getOrderId() == null ? other.getOrderId() == null : this.getOrderId().equals(other.getOrderId()))
            && (this.getOrderAmount() == null ? other.getOrderAmount() == null : this.getOrderAmount().equals(other.getOrderAmount()))
            && (this.getRefundAmount() == null ? other.getRefundAmount() == null : this.getRefundAmount().equals(other.getRefundAmount()))
            && (this.getOrderStatus() == null ? other.getOrderStatus() == null : this.getOrderStatus().equals(other.getOrderStatus()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getBankTradeTime() == null ? other.getBankTradeTime() == null : this.getBankTradeTime().equals(other.getBankTradeTime()))
            && (this.getBankOrderNo() == null ? other.getBankOrderNo() == null : this.getBankOrderNo().equals(other.getBankOrderNo()))
            && (this.getBankOrderStatus() == null ? other.getBankOrderStatus() == null : this.getBankOrderStatus().equals(other.getBankOrderStatus()))
            && (this.getBankAmount() == null ? other.getBankAmount() == null : this.getBankAmount().equals(other.getBankAmount()))
            && (this.getBankRefundAmount() == null ? other.getBankRefundAmount() == null : this.getBankRefundAmount().equals(other.getBankRefundAmount()))
            && (this.getBankFee() == null ? other.getBankFee() == null : this.getBankFee().equals(other.getBankFee()))
            && (this.getErrType() == null ? other.getErrType() == null : this.getErrType().equals(other.getErrType()))
            && (this.getHandleStatus() == null ? other.getHandleStatus() == null : this.getHandleStatus().equals(other.getHandleStatus()))
            && (this.getHandleValue() == null ? other.getHandleValue() == null : this.getHandleValue().equals(other.getHandleValue()))
            && (this.getHandleRemark() == null ? other.getHandleRemark() == null : this.getHandleRemark().equals(other.getHandleRemark()))
            && (this.getOperatorName() == null ? other.getOperatorName() == null : this.getOperatorName().equals(other.getOperatorName()))
            && (this.getOperatorUserId() == null ? other.getOperatorUserId() == null : this.getOperatorUserId().equals(other.getOperatorUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getBillDate() == null) ? 0 : getBillDate().hashCode());
        result = prime * result + ((getBillType() == null) ? 0 : getBillType().hashCode());
        result = prime * result + ((getBankType() == null) ? 0 : getBankType().hashCode());
        result = prime * result + ((getChannelMchId() == null) ? 0 : getChannelMchId().hashCode());
        result = prime * result + ((getOrderTime() == null) ? 0 : getOrderTime().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getMchName() == null) ? 0 : getMchName().hashCode());
        result = prime * result + ((getMchOrderNo() == null) ? 0 : getMchOrderNo().hashCode());
        result = prime * result + ((getTradeTime() == null) ? 0 : getTradeTime().hashCode());
        result = prime * result + ((getOrderId() == null) ? 0 : getOrderId().hashCode());
        result = prime * result + ((getOrderAmount() == null) ? 0 : getOrderAmount().hashCode());
        result = prime * result + ((getRefundAmount() == null) ? 0 : getRefundAmount().hashCode());
        result = prime * result + ((getOrderStatus() == null) ? 0 : getOrderStatus().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getBankTradeTime() == null) ? 0 : getBankTradeTime().hashCode());
        result = prime * result + ((getBankOrderNo() == null) ? 0 : getBankOrderNo().hashCode());
        result = prime * result + ((getBankOrderStatus() == null) ? 0 : getBankOrderStatus().hashCode());
        result = prime * result + ((getBankAmount() == null) ? 0 : getBankAmount().hashCode());
        result = prime * result + ((getBankRefundAmount() == null) ? 0 : getBankRefundAmount().hashCode());
        result = prime * result + ((getBankFee() == null) ? 0 : getBankFee().hashCode());
        result = prime * result + ((getErrType() == null) ? 0 : getErrType().hashCode());
        result = prime * result + ((getHandleStatus() == null) ? 0 : getHandleStatus().hashCode());
        result = prime * result + ((getHandleValue() == null) ? 0 : getHandleValue().hashCode());
        result = prime * result + ((getHandleRemark() == null) ? 0 : getHandleRemark().hashCode());
        result = prime * result + ((getOperatorName() == null) ? 0 : getOperatorName().hashCode());
        result = prime * result + ((getOperatorUserId() == null) ? 0 : getOperatorUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
