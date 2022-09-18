package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchGoodsCategory;
import org.xxpay.core.entity.MchGoodsPropsCategoryRela;
import org.xxpay.core.entity.MchGoodsStoreRela;
import org.xxpay.core.service.IMchGoodsCategoryService;
import org.xxpay.core.service.IMchGoodsPropsCategoryRelaService;
import org.xxpay.core.service.IMchGoodsService;
import org.xxpay.core.service.IMchGoodsStoreRelaService;
import org.xxpay.service.dao.mapper.MchGoodsMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商户商品表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchGoodsServiceImpl extends ServiceImpl<MchGoodsMapper, MchGoods> implements IMchGoodsService {

    @Autowired
    IMchGoodsStoreRelaService mchGoodsStoreRelaService;

    @Autowired
    IMchGoodsPropsCategoryRelaService mchGoodsPropsCategoryRelaService;

    @Autowired
    IMchGoodsCategoryService mchGoodsCategoryService;


    @Override
    public IPage<MchGoods> list(MchGoods mchGoods, Long outGoodsId, IPage page, Byte sortColumn, Byte sortType, Long floorPrice, Long ceilPrice, String createTimeStart, String createTimeEnd) {
        LambdaQueryWrapper<MchGoods> lambda = new QueryWrapper<MchGoods>().lambda();

        //查询分类ID集合
        List idList = new LinkedList();
        //根据分类查询该分类下全部商品
        if (mchGoods.getCategoryId() != null) {
            MchGoodsCategory goodsCategory = mchGoodsCategoryService.getById(mchGoods.getCategoryId());
            if (goodsCategory == null) {
                throw new ServiceException(RetEnum.RET_COMM_PARAM_ERROR);
            }

            idList.add(mchGoods.getCategoryId());
            //一级分类
            if (goodsCategory.getParentCategoryId() == 0) {
                List<MchGoodsCategory> list = mchGoodsCategoryService.list(new LambdaQueryWrapper<MchGoodsCategory>()
                        .eq(MchGoodsCategory::getParentCategoryId, goodsCategory.getCategoryId())
                );
                if (CollectionUtils.isNotEmpty(list)) {
                    for (MchGoodsCategory category : list) {
                        idList.add(category.getCategoryId());
                    }
                }
            }
        }

        //排序
        if(sortColumn != null && sortColumn == 1){
            if(sortType != null && sortType == 1){
                lambda.orderByDesc(MchGoods::getCellingPrice);
            }else if(sortType != null && sortType == 2){
                lambda.orderByAsc(MchGoods::getCellingPrice);
            }
        }else if(sortColumn != null && sortColumn == 2){
            if(sortType != null && sortType == 1){
                lambda.orderByDesc(MchGoods::getVirtualSaleNum);
            }else if(sortType != null && sortType == 2){
                lambda.orderByAsc(MchGoods::getVirtualSaleNum);
            }
        }else{
            lambda.orderByDesc(MchGoods::getCreateTime);
        }

        setCriteria(lambda, mchGoods, outGoodsId, idList, floorPrice, ceilPrice, createTimeStart, createTimeEnd);
        return page(page, lambda);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public boolean saveMchGoods(MchGoods mchGoods, String storeIds, String propsCategoryIds) {

        //新增商品
        int count = baseMapper.insert(mchGoods);
        if (count <= 0) return false;

        //如果限制门店增加门店与商品关联
        boolean result = false;
        if (mchGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            if (StringUtils.isBlank(storeIds))throw new ServiceException(RetEnum.RET_MBR_SELECT_STORES);
            String[] split = storeIds.split(",");
            MchGoodsStoreRela rela = new MchGoodsStoreRela();
            rela.setGoodsId(mchGoods.getGoodsId());
            for (String s : split) {
                rela.setStoreId(Long.valueOf(s));
                result = mchGoodsStoreRelaService.save(rela);
                if (!result) return false;
            }
        }

        //如果添加属性，则增加属性分类与商品关联
        if (mchGoods.getGoodsPropsType() == MchConstant.MCH_GOODS_PROPS_TYPE_YES){
            if (StringUtils.isBlank(propsCategoryIds))throw new ServiceException(RetEnum.RET_MBR_SELECT_GOODS_PROPS_CATEGORY);
            String[] propsCategoryIdList = propsCategoryIds.split(",");
            MchGoodsPropsCategoryRela rela = new MchGoodsPropsCategoryRela();
            rela.setGoodsId(mchGoods.getGoodsId());
            for (String s : propsCategoryIdList) {
                rela.setPropsCategoryId(Long.valueOf(s));
                result = mchGoodsPropsCategoryRelaService.save(rela);
                if (!result) return false;
            }
        }

        return true;
    }

    @Override
    public List<Map> storesList(Long goodsId) {
        return baseMapper.storesList(goodsId);
    }

    @Override
    public List<Map> propsCategoryList(Long goodsId) {
        return baseMapper.propsCategoryList(goodsId);
    }

    @Override
    public boolean updateMchGoods(MchGoods mchGoods, String storeIds, String propsCategoryIds) {

        MchGoods dbRecord = baseMapper.selectById(mchGoods.getGoodsId());
        if (dbRecord == null) throw new ServiceException(RetEnum.RET_MBR_NO_SUCH_GOODS);

        boolean result = false;
        //删除原有的商品与门店关联关系
        //如果限制门店则先删除原有的再增加门店与商品关联，不限制则删除原有关联
        if (dbRecord.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            result = mchGoodsStoreRelaService.remove(new QueryWrapper<MchGoodsStoreRela>().lambda()
                    .eq(MchGoodsStoreRela::getGoodsId, mchGoods.getGoodsId())
            );
            if (!result) return false;
        }

        if (mchGoods.getStoreLimitType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            if (StringUtils.isBlank(storeIds)) throw new ServiceException(RetEnum.RET_MBR_SELECT_STORES);
            String[] split = storeIds.split(",");
            MchGoodsStoreRela rela = new MchGoodsStoreRela();
            rela.setGoodsId(mchGoods.getGoodsId());
            for (String s : split) {
                rela.setStoreId(Long.valueOf(s));
                result = mchGoodsStoreRelaService.save(rela);
                if (!result) return false;
            }
        }

        //删除原有的商品与属性分类关联关系
        //如果添加属性则先删除原有的再增加属性分类与商品关联，不限制则删除原有关联
        if (dbRecord.getGoodsPropsType() == MchConstant.MCH_STORE_LIMIT_TYPE_YES){
            result = mchGoodsPropsCategoryRelaService.remove(new QueryWrapper<MchGoodsPropsCategoryRela>().lambda()
                    .eq(MchGoodsPropsCategoryRela::getGoodsId, mchGoods.getGoodsId())
            );
            if (!result) return false;
        }

        if (mchGoods.getGoodsPropsType() == MchConstant.MCH_GOODS_PROPS_TYPE_YES){
            if (StringUtils.isBlank(propsCategoryIds))throw new ServiceException(RetEnum.RET_MBR_SELECT_GOODS_PROPS_CATEGORY);
            String[] propsCategoryIdList = propsCategoryIds.split(",");
            MchGoodsPropsCategoryRela rela = new MchGoodsPropsCategoryRela();
            rela.setGoodsId(mchGoods.getGoodsId());
            for (String s : propsCategoryIdList) {
                rela.setPropsCategoryId(Long.valueOf(s));
                result = mchGoodsPropsCategoryRelaService.save(rela);
                if (!result) return false;
            }
        }

        int count = baseMapper.updateById(mchGoods);
        if(count <= 0) return false;

        return true;
    }

    void setCriteria(LambdaQueryWrapper<MchGoods> lambda, MchGoods mchGoods, Long outGoodsId, List idList, Long floorPrice, Long ceilPrice, String createTimeStart, String createTimeEnd) {
        if(mchGoods != null) {
            if (mchGoods.getGoodsId() != null) lambda.eq(MchGoods::getGoodsId, mchGoods.getGoodsId());
            if (StringUtils.isNotEmpty(mchGoods.getGoodsName())) lambda.like(MchGoods::getGoodsName, "%" + mchGoods.getGoodsName() + "%");
            if (mchGoods.getIndustryType() != null) lambda.eq(MchGoods::getIndustryType, mchGoods.getIndustryType());
            if (StringUtils.isNotBlank(mchGoods.getGoodsModule())) lambda.like(MchGoods::getGoodsModule, "%" + mchGoods.getGoodsModule() + "%");
            if (mchGoods.getStatus() != null) lambda.eq(MchGoods::getStatus, mchGoods.getStatus());
            if (mchGoods.getMchId() != null) lambda.eq(MchGoods::getMchId, mchGoods.getMchId());
            if (mchGoods.getIsvId() != null) lambda.eq(MchGoods::getIsvId, mchGoods.getIsvId());
            if (mchGoods.getIsRecommend() != null) lambda.eq(MchGoods::getIsRecommend, mchGoods.getIsRecommend());
        }
        if (CollectionUtils.isNotEmpty(idList)) lambda.in(MchGoods::getCategoryId, idList);
        if (outGoodsId != null) lambda.ne(MchGoods::getGoodsId, outGoodsId);
        if (floorPrice != null) lambda.ge(MchGoods::getCellingPrice, floorPrice);
        if (ceilPrice != null) lambda.le(MchGoods::getCellingPrice, ceilPrice);
        if (StringUtils.isNotBlank(createTimeStart)) lambda.ge(MchGoods::getCreateTime, createTimeStart);
        if (StringUtils.isNotBlank(createTimeEnd)) lambda.le(MchGoods::getCreateTime, createTimeEnd);
    }

}
