package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.service.IMchCouponStoreRelaService;
import org.xxpay.service.dao.mapper.MchCouponStoreRelaMapper;

/**
 * <p>
 * 商户优惠券-门店关联表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MchCouponStoreRelaServiceImpl extends ServiceImpl<MchCouponStoreRelaMapper, MchCouponStoreRela> implements IMchCouponStoreRelaService {

    @Override
    public boolean canUseByStoreId(Long couponId, Long storeId){

        return
                (this.count(new QueryWrapper<MchCouponStoreRela>()
                        .lambda().eq(MchCouponStoreRela::getCouponId, couponId)
                        .eq(MchCouponStoreRela::getStoreId, storeId))
                )> 0;
    }


}
