package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MchShoppingCart;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-25
 */
public interface MchShoppingCartMapper extends BaseMapper<MchShoppingCart> {

    /**
     * 购物车列表
     * @param param
     * @return
     */
    List<Map> selectCartList(Map param);

    /**
     * 购物车列表总数
     * @param param
     * @return
     */
    int count(Map param);
}
