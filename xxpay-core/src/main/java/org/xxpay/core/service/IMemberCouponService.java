package org.xxpay.core.service;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MemberCoupon;

/**
 * <p>
 * 会员优惠券领取记录表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberCouponService extends IService<MemberCoupon> {

    /**
     * 会员优惠券核销
     * @param memberCoupon
     * @return
     */
    Boolean checkCoupon(MemberCoupon memberCoupon);

    /**
     * 领取优惠券
     * @param mchCoupon
     * @param memberId
     * @param mchId
     * @return
     */
    int addMbrCoupon(MchCoupon mchCoupon, Long memberId, Long mchId);

    /** 使用优惠券的预先检查： 使用规则校验, 门店验证等;  返回对应不适用信息, 返回null表示该优惠券可用 **/
    String useCouponPreCheck(MchCoupon useCoupon, Long payAmount, Long currentStoreId);


    /** 查询用户领取的优惠券列表集合, 返回JSON格式 （一般用于会员支付时选择） **/
    JSONArray selectMemberReceiveCouponList(Long memberId, Long currentMchId, Long currentStoreId, Long payAmount);

    /** 使用优惠券（更新优惠券状态为已使用）  **/
    boolean use(String couponNo);

}
