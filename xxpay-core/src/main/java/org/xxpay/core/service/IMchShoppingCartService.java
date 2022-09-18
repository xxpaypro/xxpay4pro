package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchShoppingCart;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchShoppingCartService extends IService<MchShoppingCart> {

    /**
     * 购物车列表
     * @param industryType
     * @param memberId
     * @param mchId
     * @return
     */
    List<Map> selectCartList(Integer offset, Integer limit, Byte industryType, Long memberId, Long mchId);

    /**
     * 购物车列表总数
     * @param industryType
     * @param memberId
     * @param mchId
     * @return
     */
    int count(Byte industryType, Long memberId, Long mchId);
}
