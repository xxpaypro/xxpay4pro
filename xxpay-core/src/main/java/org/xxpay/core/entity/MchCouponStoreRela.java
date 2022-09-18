package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户优惠券-门店关联表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@TableName("t_mch_coupon_store_rela")
public class MchCouponStoreRela extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券ID
     */
    @TableId("CouponId")
    private Long couponId;

    /**
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "MchCouponStoreRela{" +
            "CouponId=" + couponId +
            ", storeId=" + storeId +
        "}";
    }
}
