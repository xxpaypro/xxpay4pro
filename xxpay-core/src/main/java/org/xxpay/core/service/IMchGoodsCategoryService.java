package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchGoodsCategory;

/**
 * <p>
 * 商品分类表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
public interface IMchGoodsCategoryService extends IService<MchGoodsCategory> {

    /**
     * 商品分类列表
     * @param mchGoodsCategory
     * @param page
     * @return
     */
    IPage<MchGoodsCategory> list(MchGoodsCategory mchGoodsCategory, IPage page);

}
