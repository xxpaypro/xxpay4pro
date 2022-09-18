package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MemberGoodsExchange;

/**
 * <p>
 * 积分商品会员兑换表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberGoodsExchangeService extends IService<MemberGoodsExchange> {

    /**
     * 会员兑换积分商品
     * @param mchPointsGoods
     * @param memberId
     * @param memberNo
     * @param mchId
     * @return
     */
    int addMbrGoods(MchPointsGoods mchPointsGoods, Long memberId, String memberNo, Long mchId);
}
