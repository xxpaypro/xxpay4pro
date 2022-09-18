package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchCouponStoreRela;

/**
 * <p>
 * 商户优惠券-门店关联表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMchCouponStoreRelaService extends IService<MchCouponStoreRela> {

    /** 查询该优惠券是否可用（根据门店ID） */
    boolean canUseByStoreId(Long couponId, Long storeId);

}
