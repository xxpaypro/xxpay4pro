package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchGoodsCategory;
import org.xxpay.core.service.IMchGoodsCategoryService;
import org.xxpay.service.dao.mapper.MchGoodsCategoryMapper;

/**
 * <p>
 * 商品分类表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchGoodsCategoryServiceImpl extends ServiceImpl<MchGoodsCategoryMapper, MchGoodsCategory> implements IMchGoodsCategoryService {

    @Override
    public IPage<MchGoodsCategory> list(MchGoodsCategory mchGoodsCategory, IPage page) {
        LambdaQueryWrapper<MchGoodsCategory> lambda = new QueryWrapper<MchGoodsCategory>().lambda();
        lambda.orderByDesc(MchGoodsCategory::getCreateTime);
        setCriteria(lambda, mchGoodsCategory);
        return page(page, lambda);
    }

    void setCriteria(LambdaQueryWrapper<MchGoodsCategory> lambda, MchGoodsCategory mchGoodsCategory) {
        if(mchGoodsCategory != null) {
            if (mchGoodsCategory.getParentCategoryId() != null) lambda.eq(MchGoodsCategory::getParentCategoryId, mchGoodsCategory.getParentCategoryId());
            if (mchGoodsCategory.getCategoryId() != null) lambda.eq(MchGoodsCategory::getCategoryId, mchGoodsCategory.getCategoryId());
            if (StringUtils.isNotEmpty(mchGoodsCategory.getCategoryName())) lambda.like(MchGoodsCategory::getCategoryName, "%" + mchGoodsCategory.getCategoryName() + "%");
            if (mchGoodsCategory.getMchId() != null) lambda.eq(MchGoodsCategory::getMchId, mchGoodsCategory.getMchId());
            if (mchGoodsCategory.getIsvId() != null) lambda.eq(MchGoodsCategory::getIsvId, mchGoodsCategory.getIsvId());
            if (mchGoodsCategory.getAuthFrom() != null) lambda.eq(MchGoodsCategory::getAuthFrom, mchGoodsCategory.getAuthFrom());
        }
    }

}
