package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.entity.MemberCoupon;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户优惠券表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMchCouponService extends IService<MchCoupon> {

    /**
     * 商户优惠券列表
     * @param mchCoupon
     * @param page
     * @return
     */
    IPage<MchCoupon> list(MchCoupon mchCoupon, IPage page, Date currentDate);

    /**
     * 更新优惠券并关联优惠券与门店
     * @param mchCoupon
     * @param storeIds
     * @return
     */
    Boolean updateCoupon(MchCoupon mchCoupon, String storeIds);

    /**
     * 检查优惠券是否可领取
     * @param coupon
     * @return
     */
    int checkCoupon(MchCoupon coupon, List<MemberCoupon> list, List<MchCouponStoreRela> relaList);
}
