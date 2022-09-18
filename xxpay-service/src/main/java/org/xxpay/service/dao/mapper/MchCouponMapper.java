package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchCoupon;

import java.util.List;

/**
 * <p>
 * 商户优惠券表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MchCouponMapper extends BaseMapper<MchCoupon> {

    /**
     * 更新优惠券已领取数量
     * @return
     */
    int updateReceiveNumByCouponId(MchCoupon mchCoupon);

    /** 查询会员已领取的所有优惠券列表集合 **/
    List<MchCoupon> selectMemberCanUseCoupon(@Param("memberId") Long memberId, @Param("mchId") Long mchId);
}
