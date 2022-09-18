package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 会员优惠券领取记录表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_member_coupon")
public class MemberCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠卷核销码
     */
    @TableId("CouponNo")
    private String couponNo;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 优惠券ID
     */
    @TableField("CouponId")
    private Long couponId;

    /**
     * 状态:0-未使用 1-已使用 2-已过期
     */
    @TableField("Status")
    private Byte status;

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
     * 有效期至
     */
    @TableField("ValidateEnd")
    private Date validateEnd;

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

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
    public Date getValidateEnd() {
        return validateEnd;
    }

    public void setValidateEnd(Date validateEnd) {
        this.validateEnd = validateEnd;
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
        return "MemberCoupon{" +
            "CouponNo=" + couponNo +
            ", memberId=" + memberId +
            ", couponId=" + couponId +
            ", status=" + status +
            ", mchId=" + mchId +
            ", remark=" + remark +
            ", validateEnd=" + validateEnd +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
