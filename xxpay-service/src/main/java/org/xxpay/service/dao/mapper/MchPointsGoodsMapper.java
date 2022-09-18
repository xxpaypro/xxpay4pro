package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MchPointsGoods;

/**
 * <p>
 * 商户积分商品表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MchPointsGoodsMapper extends BaseMapper<MchPointsGoods> {

    int updateStockNumByGoodsId(MchPointsGoods updateRecord);
}
