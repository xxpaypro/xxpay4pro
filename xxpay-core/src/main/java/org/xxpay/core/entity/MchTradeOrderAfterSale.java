package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商城订单售后表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-25
 */
@TableName("t_mch_trade_order_after_sale")
public class MchTradeOrderAfterSale extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情ID
     */
    @TableId("AfterSaleId")
    private Long afterSaleId;

    /**
     * 交易单号
     */
    @TableField("TradeOrderId")
    private String tradeOrderId;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 商品信息
     */
    @TableField("GoodsDesc")
    private String goodsDesc;

    /**
     * 申请退款商品总金额
     */
    @TableField("AfterSaleAmount")
    private Long afterSaleAmount;

    /**
     * 售后类型：1-退款 2-换货 3-维修
     */
    @TableField("AfterSaleType")
    private Byte afterSaleType;

    /**
     * 所属小程序：1-点餐 2-商城
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 售后状态：-2-审核不通过，-1-申请待审核，1-审核通过，2-待发货，3-已发货，4-商家收货并处理，5-商家寄回买家，6-售后单完成
     */
    @TableField("Status")
    private Byte status;

    /**
     * 审核时间
     */
    @TableField("AuditTime")
    private Date auditTime;

    /**
     * 审核描述
     */
    @TableField("AuditRemark")
    private String auditRemark;

    /**
     * 申请原因
     */
    @TableField("Reason")
    private String reason;

    /**
     * 售后申请描述
     */
    @TableField("AfterSaleDesc")
    private String afterSaleDesc;

    /**
     * 售后申请凭证
     */
    @TableField("AfterSaleImg")
    private String afterSaleImg;

    /**
     * 详细地址
     */
    @TableField("AddressInfo")
    private String addressInfo;

    /**
     * 退回商家运单号
     */
    @TableField("BackTransportNo")
    private String backTransportNo;

    /**
     * 商家收到退货时间
     */
    @TableField("BackTransportTime")
    private Date backTransportTime;

    /**
     * 换货/维修完成，发往用户运单号
     */
    @TableField("TransportNo")
    private String transportNo;

    /**
     * 商家发回货物时间
     */
    @TableField("TransportTime")
    private Date transportTime;

    /**
     * 售后完成时间
     */
    @TableField("CompleteTime")
    private Date completeTime;

    /**
     * 退款金额，单位分
     */
    @TableField("RefundAmount")
    private Long refundAmount;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

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

    @TableField("AddressId")
    private Long addressId;

    public Long getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }
    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }
    public Long getAfterSaleAmount() {
        return afterSaleAmount;
    }

    public void setAfterSaleAmount(Long afterSaleAmount) {
        this.afterSaleAmount = afterSaleAmount;
    }
    public Byte getAfterSaleType() {
        return afterSaleType;
    }

    public void setAfterSaleType(Byte afterSaleType) {
        this.afterSaleType = afterSaleType;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getAfterSaleDesc() {
        return afterSaleDesc;
    }

    public void setAfterSaleDesc(String afterSaleDesc) {
        this.afterSaleDesc = afterSaleDesc;
    }
    public String getAfterSaleImg() {
        return afterSaleImg;
    }

    public void setAfterSaleImg(String afterSaleImg) {
        this.afterSaleImg = afterSaleImg;
    }
    public String getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }
    public String getBackTransportNo() {
        return backTransportNo;
    }

    public void setBackTransportNo(String backTransportNo) {
        this.backTransportNo = backTransportNo;
    }
    public Date getBackTransportTime() {
        return backTransportTime;
    }

    public void setBackTransportTime(Date backTransportTime) {
        this.backTransportTime = backTransportTime;
    }
    public String getTransportNo() {
        return transportNo;
    }

    public void setTransportNo(String transportNo) {
        this.transportNo = transportNo;
    }
    public Date getTransportTime() {
        return transportTime;
    }

    public void setTransportTime(Date transportTime) {
        this.transportTime = transportTime;
    }
    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
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
    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "MchTradeOrderAfterSale{" +
                "AfterSaleId=" + afterSaleId +
                ", tradeOrderId=" + tradeOrderId +
                ", memberId=" + memberId +
                ", goodsDesc=" + goodsDesc +
                ", afterSaleAmount=" + afterSaleAmount +
                ", afterSaleType=" + afterSaleType +
                ", authFrom=" + authFrom +
                ", status=" + status +
                ", auditTime=" + auditTime +
                ", auditRemark=" + auditRemark +
                ", reason=" + reason +
                ", afterSaleDesc=" + afterSaleDesc +
                ", afterSaleImg=" + afterSaleImg +
                ", addressInfo=" + addressInfo +
                ", backTransportNo=" + backTransportNo +
                ", backTransportTime=" + backTransportTime +
                ", transportNo=" + transportNo +
                ", transportTime=" + transportTime +
                ", completeTime=" + completeTime +
                ", refundAmount=" + refundAmount +
                ", mchId=" + mchId +
                ", isvId=" + isvId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", addressId=" + addressId +
                "}";
    }
}
