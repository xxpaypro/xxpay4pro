package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchGoods;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户商品表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface MchGoodsMapper extends BaseMapper<MchGoods> {

    List<Map> storesList(@Param("goodsId") Long goodsId);

    List<Map> propsCategoryList(@Param("goodsId") Long goodsId);
}
