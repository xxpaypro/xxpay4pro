package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户操作员交班记录表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-29
 */
@TableName("t_mch_operator_handover")
public class MchOperatorHandover extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 操作员ID
     */
    @TableField("UserId")
    private Long userId;

    /**
     * 操作员名称
     */
    @TableField("UserName")
    private String userName;

    /**
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 上班时间
     */
    @TableField("WorkStartTime")
    private Date workStartTime;

    /**
     * 交班时间
     */
    @TableField("WorkEndTime")
    private Date workEndTime;

    /**
     * 订单总数量
     */
    @TableField("CountTotalOrder")
    private Long countTotalOrder;

    /**
     * 充值金额, 单位: 分
     */
    @TableField("SumRechargeAmount")
    private Long sumRechargeAmount;

    /**
     * 优惠金额, 单位: 分
     */
    @TableField("SumDiscountAmount")
    private Long sumDiscountAmount;

    /**
     * 实收现金, 单位: 分
     */
    @TableField("SumCashAmount")
    private Long sumCashAmount;

    /**
     * 退款金额, 单位: 分
     */
    @TableField("SumRefundAmount")
    private Long sumRefundAmount;

    /**
     * 实际收款, 单位: 分
     */
    @TableField("SumRealAmount")
    private Long sumRealAmount;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(Date workStartTime) {
        this.workStartTime = workStartTime;
    }
    public Date getWorkEndTime() {
        return workEndTime;
    }

    public void setWorkEndTime(Date workEndTime) {
        this.workEndTime = workEndTime;
    }
    public Long getCountTotalOrder() {
        return countTotalOrder;
    }

    public void setCountTotalOrder(Long countTotalOrder) {
        this.countTotalOrder = countTotalOrder;
    }
    public Long getSumRechargeAmount() {
        return sumRechargeAmount;
    }

    public void setSumRechargeAmount(Long sumRechargeAmount) {
        this.sumRechargeAmount = sumRechargeAmount;
    }
    public Long getSumDiscountAmount() {
        return sumDiscountAmount;
    }

    public void setSumDiscountAmount(Long sumDiscountAmount) {
        this.sumDiscountAmount = sumDiscountAmount;
    }
    public Long getSumCashAmount() {
        return sumCashAmount;
    }

    public void setSumCashAmount(Long sumCashAmount) {
        this.sumCashAmount = sumCashAmount;
    }
    public Long getSumRefundAmount() {
        return sumRefundAmount;
    }

    public void setSumRefundAmount(Long sumRefundAmount) {
        this.sumRefundAmount = sumRefundAmount;
    }
    public Long getSumRealAmount() {
        return sumRealAmount;
    }

    public void setSumRealAmount(Long sumRealAmount) {
        this.sumRealAmount = sumRealAmount;
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
        return "MchOperatorHandover{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName=" + userName +
                ", storeId=" + storeId +
                ", mchId=" + mchId +
                ", workStartTime=" + workStartTime +
                ", workEndTime=" + workEndTime +
                ", countTotalOrder=" + countTotalOrder +
                ", sumRechargeAmount=" + sumRechargeAmount +
                ", sumDiscountAmount=" + sumDiscountAmount +
                ", sumCashAmount=" + sumCashAmount +
                ", sumRefundAmount=" + sumRefundAmount +
                ", sumRealAmount=" + sumRealAmount +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
