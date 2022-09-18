package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 订单分润明细表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-23
 */
@TableName("t_order_profit_detail")
public class OrderProfitDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务类型,1-支付, 2-充值, 3-退款
     */
    @TableField("BizType")
    private Byte bizType;

    /**
     * 业务关联单号
     */
    @TableField("BizOrderId")
    private String bizOrderId;

    /**
     * 业务订单实际支付金额
     */
    @TableField("BizOrderPayAmount")
    private Long bizOrderPayAmount;

    /**
     * 业务订单创建日期，便于查询统计
     */
    @TableField("BizOrderCreateDate")
    private Date bizOrderCreateDate;

    /**
     * 产品ID
     */
    @TableField("ProductId")
    private Integer productId;

    /**
     * 计算订单分润参考对象：1-商户 2-代理商 3-平台 4-服务商
     */
    @TableField("ReferInfoType")
    private Byte referInfoType;

    /**
     * 订单分润参考对象关联ID
     */
    @TableField("ReferInfoId")
    private Long referInfoId;

    /**
     * 所属服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 收入金额,单位分; 商户入账金额|代理商返佣|服务商返佣
     */
    @TableField("IncomeAmount")
    private Long incomeAmount;

    /**
     * 支出金额,单位分; 商户费率费用|代理商退款退还返佣|服务商退款退还返佣
     */
    @TableField("FeeAmount")
    private Long feeAmount;

    /**
     * 费率/费用快照, 商家费率|代理商返佣比例|服务商基础费率
     */
    @TableField("FeeRateSnapshot")
    private String feeRateSnapshot;

    /**
     * 结算任务状态: -1无需执行结算任务, 0-待执行结算任务, 1-已完成结算任务
     */
    @TableField("SettTaskStatus")
    private Byte settTaskStatus;

    /**
     * 结算任务ID
     */
    @TableField("SettTaskId")
    private Long settTaskId;

    /**
     * 结算时间
     */
    @TableField("SettTime")
    private Date settTime;

    /**
     * 分润明细创建时间
     */
    @TableField("CreatedTime")
    private Date createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Byte getBizType() {
        return bizType;
    }

    public void setBizType(Byte bizType) {
        this.bizType = bizType;
    }
    public String getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(String bizOrderId) {
        this.bizOrderId = bizOrderId;
    }
    public Long getBizOrderPayAmount() {
        return bizOrderPayAmount;
    }

    public void setBizOrderPayAmount(Long bizOrderPayAmount) {
        this.bizOrderPayAmount = bizOrderPayAmount;
    }
    public Date getBizOrderCreateDate() {
        return bizOrderCreateDate;
    }

    public void setBizOrderCreateDate(Date bizOrderCreateDate) {
        this.bizOrderCreateDate = bizOrderCreateDate;
    }
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Byte getReferInfoType() {
        return referInfoType;
    }

    public void setReferInfoType(Byte referInfoType) {
        this.referInfoType = referInfoType;
    }
    public Long getReferInfoId() {
        return referInfoId;
    }

    public void setReferInfoId(Long referInfoId) {
        this.referInfoId = referInfoId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Long getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Long incomeAmount) {
        this.incomeAmount = incomeAmount;
    }
    public Long getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(Long feeAmount) {
        this.feeAmount = feeAmount;
    }
    public String getFeeRateSnapshot() {
        return feeRateSnapshot;
    }

    public void setFeeRateSnapshot(String feeRateSnapshot) {
        this.feeRateSnapshot = feeRateSnapshot;
    }
    public Byte getSettTaskStatus() {
        return settTaskStatus;
    }

    public void setSettTaskStatus(Byte settTaskStatus) {
        this.settTaskStatus = settTaskStatus;
    }
    public Long getSettTaskId() {
        return settTaskId;
    }

    public void setSettTaskId(Long settTaskId) {
        this.settTaskId = settTaskId;
    }
    public Date getSettTime() {
        return settTime;
    }

    public void setSettTime(Date settTime) {
        this.settTime = settTime;
    }
    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "OrderProfitDetail{" +
            "Id=" + id +
            ", bizType=" + bizType +
            ", bizOrderId=" + bizOrderId +
            ", bizOrderPayAmount=" + bizOrderPayAmount +
            ", bizOrderCreateDate=" + bizOrderCreateDate +
            ", productId=" + productId +
            ", referInfoType=" + referInfoType +
            ", referInfoId=" + referInfoId +
            ", isvId=" + isvId +
            ", incomeAmount=" + incomeAmount +
            ", feeAmount=" + feeAmount +
            ", feeRateSnapshot=" + feeRateSnapshot +
            ", settTaskStatus=" + settTaskStatus +
            ", settTaskId=" + settTaskId +
            ", settTime=" + settTime +
            ", createdTime=" + createdTime +
        "}";
    }
}
