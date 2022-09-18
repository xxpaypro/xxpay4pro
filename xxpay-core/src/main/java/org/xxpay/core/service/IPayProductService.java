package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.PayProduct;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/5/3
 * @description: 支付产品
 */
public interface IPayProductService extends IService<PayProduct> {

    int add(PayProduct payProduct);

    int update(PayProduct payProduct);

    PayProduct findById(Integer id);

    List<PayProduct> select(int offset, int limit, PayProduct payProduct);

    Integer count(PayProduct payProduct);

    List<PayProduct> selectAll();

    List<PayProduct> selectAll(PayProduct payProduct);

    List<PayProduct> selectAll(List<Integer> ids);

    List<PayProduct> selectAll(String[] ids);

    /**
     * 根据支付类型查询所有支付产品列表
     * @param payType
     * @return
     */
    List<PayProduct> selectAllByPayType(String payType);

}
