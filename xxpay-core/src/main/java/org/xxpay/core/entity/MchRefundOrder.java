package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户退款表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-05
 */
@TableName("t_mch_refund_order")
public class MchRefundOrder extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商户退款订单号
     */
    @TableId("MchRefundOrderId")
    private String mchRefundOrderId;

    /**
     * 商户交易表
     */
    @TableField("TradeOrderId")
    private String tradeOrderId;

    /**
     * 渠道退款单号
     */
    @TableField("ChannelRefundNo")
    private String channelRefundNo;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 支付金额,单位分
     */
    @TableField("PayAmount")
    private Long payAmount;

    /**
     * 本次退款金额,单位分
     */
    @TableField("RefundAmount")
    private Long refundAmount;

    /**
     * 三位货币代码,人民币:cny
     */
    @TableField("Currency")
    private String currency;

    /**
     * 退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败
     */
    @TableField("Status")
    private Byte status;

    /**
     * 退款原因
     */
    @TableField("RefundDesc")
    private String refundDesc;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

    /**
     * 订单退款成功时间
     */
    @TableField("RefundSuccTime")
    private Date refundSuccTime;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UpdateTime")
    private Date updateTime;

    public String getMchRefundOrderId() {
        return mchRefundOrderId;
    }

    public void setMchRefundOrderId(String mchRefundOrderId) {
        this.mchRefundOrderId = mchRefundOrderId;
    }
    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }
    public String getChannelRefundNo() {
        return channelRefundNo;
    }

    public void setChannelRefundNo(String channelRefundNo) {
        this.channelRefundNo = channelRefundNo;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }
    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getRefundDesc() {
        return refundDesc;
    }

    public void setRefundDesc(String refundDesc) {
        this.refundDesc = refundDesc;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Date getRefundSuccTime() {
        return refundSuccTime;
    }

    public void setRefundSuccTime(Date refundSuccTime) {
        this.refundSuccTime = refundSuccTime;
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
        return "MchRefundOrder{" +
                "MchRefundOrderId=" + mchRefundOrderId +
                ", tradeOrderId=" + tradeOrderId +
                ", channelRefundNo=" + channelRefundNo +
                ", mchId=" + mchId +
                ", payAmount=" + payAmount +
                ", refundAmount=" + refundAmount +
                ", currency=" + currency +
                ", status=" + status +
                ", refundDesc=" + refundDesc +
                ", remark=" + remark +
                ", refundSuccTime=" + refundSuccTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
