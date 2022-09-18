package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户优惠券表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-08-27
 */
@TableName("t_mch_coupon")
public class MchCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    @TableId(value = "CouponId", type = IdType.AUTO)
    private Long couponId;

    /**
     * 优惠券名称
     */
    @TableField("CouponName")
    private String couponName;

    /**
     * 优惠券配色
     */
    @TableField("CouponColor")
    private String couponColor;

    /**
     * 优惠券logo路径
     */
    @TableField("LogoImgPath")
    private String logoImgPath;

    /**
     * 优惠券面值, 单位分
     */
    @TableField("CouponAmount")
    private Long couponAmount;

    /**
     * 最低消费金额
     */
    @TableField("PayAmountLimit")
    private Long payAmountLimit;

    /**
     * 状态:0-暂停发放 1-正常发放 2-活动已结束
     */
    @TableField("Status")
    private Byte status;

    /**
     * 有效期类型:1-领取后天数, 2-按照活动日期
     */
    @TableField("ValidateType")
    private Byte validateType;

    /**
     * 领取后有效期天数
     */
    @TableField("ValidateDay")
    private Integer validateDay;

    /**
     * 活动开始时间
     */
    @TableField("BeginTime")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @TableField("EndTime")
    private Date endTime;

    /**
     * 总发放数量
     */
    @TableField("TotalNum")
    private Integer totalNum;

    /**
     * 已领取数量
     */
    @TableField("ReceiveNum")
    private Integer receiveNum;

    /**
     * 优惠券使用限制，例如时段,周,时间, json格式
     */
    @TableField("UseTimeConfig")
    private String useTimeConfig;

    /**
     * 单用户领取数量限制, -1不限制
     */
    @TableField("SingleUserLimit")
    private Integer singleUserLimit;

    /**
     * 是否限制门店 0-不限门店 1-限制门店,详见门店关联表
     */
    @TableField("StoreLimitType")
    private Byte storeLimitType;

    /**
     * 过期提醒提前天数：-1不提醒
     */
    @TableField("ExpiredWarningTime")
    private Integer expiredWarningTime;

    /**
     * 是否同步到微信卡包 1-是, 0-否
     */
    @TableField("SyncWX")
    private Byte syncWX;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

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

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }
    public String getCouponColor() {
        return couponColor;
    }

    public void setCouponColor(String couponColor) {
        this.couponColor = couponColor;
    }
    public String getLogoImgPath() {
        return logoImgPath;
    }

    public void setLogoImgPath(String logoImgPath) {
        this.logoImgPath = logoImgPath;
    }
    public Long getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Long couponAmount) {
        this.couponAmount = couponAmount;
    }
    public Long getPayAmountLimit() {
        return payAmountLimit;
    }

    public void setPayAmountLimit(Long payAmountLimit) {
        this.payAmountLimit = payAmountLimit;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Byte getValidateType() {
        return validateType;
    }

    public void setValidateType(Byte validateType) {
        this.validateType = validateType;
    }
    public Integer getValidateDay() {
        return validateDay;
    }

    public void setValidateDay(Integer validateDay) {
        this.validateDay = validateDay;
    }
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
    public Integer getReceiveNum() {
        return receiveNum;
    }

    public void setReceiveNum(Integer receiveNum) {
        this.receiveNum = receiveNum;
    }
    public String getUseTimeConfig() {
        return useTimeConfig;
    }

    public void setUseTimeConfig(String useTimeConfig) {
        this.useTimeConfig = useTimeConfig;
    }
    public Integer getSingleUserLimit() {
        return singleUserLimit;
    }

    public void setSingleUserLimit(Integer singleUserLimit) {
        this.singleUserLimit = singleUserLimit;
    }
    public Byte getStoreLimitType() {
        return storeLimitType;
    }

    public void setStoreLimitType(Byte storeLimitType) {
        this.storeLimitType = storeLimitType;
    }
    public Integer getExpiredWarningTime() {
        return expiredWarningTime;
    }

    public void setExpiredWarningTime(Integer expiredWarningTime) {
        this.expiredWarningTime = expiredWarningTime;
    }
    public Byte getSyncWX() {
        return syncWX;
    }

    public void setSyncWX(Byte syncWX) {
        this.syncWX = syncWX;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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
        return "MchCoupon{" +
                "CouponId=" + couponId +
                ", couponName=" + couponName +
                ", couponColor=" + couponColor +
                ", logoImgPath=" + logoImgPath +
                ", couponAmount=" + couponAmount +
                ", payAmountLimit=" + payAmountLimit +
                ", status=" + status +
                ", validateType=" + validateType +
                ", validateDay=" + validateDay +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", totalNum=" + totalNum +
                ", receiveNum=" + receiveNum +
                ", useTimeConfig=" + useTimeConfig +
                ", singleUserLimit=" + singleUserLimit +
                ", storeLimitType=" + storeLimitType +
                ", expiredWarningTime=" + expiredWarningTime +
                ", syncWX=" + syncWX +
                ", mchId=" + mchId +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
