package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchPointsGoods;
import org.xxpay.core.entity.MemberGoodsExchange;

/**
 * <p>
 * 商户积分商品表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMchPointsGoodsService extends IService<MchPointsGoods> {

    /**
     * 积分商品列表
     * @param mchPointsGoods
     * @param page
     * @return
     */
    IPage<MchPointsGoods> list(MchPointsGoods mchPointsGoods, Page page);

    /**
     * 核销积分商品
     * @param exchange
     * @param operatorId
     * @param operatorName
     * @return
     */
    boolean checkPointGoods(MemberGoodsExchange exchange, Long operatorId, String operatorName);

    /**
     * 更新积分商品信息
     * @param mchPointsGoods
     * @return
     */
    boolean updatePointsGoods(MchPointsGoods mchPointsGoods, String storeIds);
}
