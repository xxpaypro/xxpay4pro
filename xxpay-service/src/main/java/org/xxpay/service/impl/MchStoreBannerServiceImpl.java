package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MchStoreBanner;
import org.xxpay.core.entity.MchStoreBannerRela;
import org.xxpay.core.service.IMchStoreBannerService;
import org.xxpay.service.dao.mapper.MchStoreBannerMapper;
import org.xxpay.service.dao.mapper.MchStoreBannerRelaMapper;
import org.xxpay.service.dao.mapper.MchStoreMapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户小程序轮播图配置表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-27
 */
@Service
public class MchStoreBannerServiceImpl extends ServiceImpl<MchStoreBannerMapper, MchStoreBanner> implements IMchStoreBannerService {

    @Autowired
    private MchStoreMapper mchStoreMapper;

    @Autowired
    private MchStoreBannerMapper mchStoreBannerMapper;

    @Autowired
    private MchStoreBannerRelaMapper mchStoreBannerRelaMapper;

    @Override
    public IPage<MchStoreBanner> list(MchStoreBanner mchStoreBanner, IPage page, Date currentDate) {
        LambdaQueryWrapper<MchStoreBanner> lambda = new QueryWrapper<MchStoreBanner>().lambda();
        lambda.orderByDesc(MchStoreBanner::getCreateTime);
        lambda.orderByAsc(MchStoreBanner::getSortNum);
        setCriteria(lambda, mchStoreBanner, currentDate);
        return page(page, lambda);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public boolean saveBanner(String storeIds, MchStoreBanner mchStoreBanner) {
        // 根据不同的投放门店方式存入
        if (mchStoreBanner.getPutStoreLimitType() == MchConstant.PUB_YES) {
            String[] storeIdSplit = storeIds.split(",");
            MchStoreBannerRela bannerRela = new MchStoreBannerRela();
            // 如果轮播图配置ID不为空，执行删除关联操作
            if (mchStoreBanner.getBannerId() != null) {
                mchStoreBannerRelaMapper.deleteById(mchStoreBanner.getBannerId());
            }
            for (int i = 0; i < storeIdSplit.length ; i++) {
                // 保存轮播图配置
                mchStoreBannerMapper.insert(mchStoreBanner);
                bannerRela.setBannerId(mchStoreBanner.getBannerId());
                bannerRela.setStoreId(Long.valueOf(storeIdSplit[i]));
                mchStoreBannerRelaMapper.insert(bannerRela);
            }
            return true;
        }else {
            MchStoreBannerRela bannerRela = new MchStoreBannerRela();
            // 查询当前商户下所有门店
            List<MchStore> storeList = mchStoreMapper.selectList(new QueryWrapper<MchStore>().lambda().eq(MchStore::getMchId, mchStoreBanner.getMchId()));
            if (storeList.size() == 0) return false;
            // 如果轮播图配置ID不为空，执行删除关联操作
            if (mchStoreBanner.getBannerId() != null) {
                mchStoreBannerRelaMapper.deleteById(mchStoreBanner.getBannerId());
            }
            for (MchStore mchStore : storeList) {
                // 保存轮播图配置
                mchStoreBannerMapper.insert(mchStoreBanner);
                bannerRela.setBannerId(mchStoreBanner.getBannerId());
                bannerRela.setStoreId(mchStore.getStoreId());
                mchStoreBannerRelaMapper.insert(bannerRela);
            }
            return true;
        }
    }

    @Override
    public boolean updateBanner(String storeIds, MchStoreBanner mchStoreBanner) {
        // 根据不同的投放门店方式存入
        if (mchStoreBanner.getPutStoreLimitType() == MchConstant.PUB_YES) {
            String[] storeIdSplit = storeIds.split(",");
            MchStoreBannerRela bannerRela = new MchStoreBannerRela();
            // 如果轮播图配置ID不为空，执行删除关联操作
            if (mchStoreBanner.getBannerId() != null) {
                mchStoreBannerRelaMapper.deleteById(mchStoreBanner.getBannerId());
            }
            for (int i = 0; i < storeIdSplit.length ; i++) {
                // 保存轮播图配置
                mchStoreBannerMapper.updateById(mchStoreBanner);
                bannerRela.setBannerId(mchStoreBanner.getBannerId());
                bannerRela.setStoreId(Long.valueOf(storeIdSplit[i]));
                mchStoreBannerRelaMapper.insert(bannerRela);
            }
            return true;
        }else {
            MchStoreBannerRela bannerRela = new MchStoreBannerRela();
            // 查询当前商户下所有门店
            List<MchStore> storeList = mchStoreMapper.selectList(new QueryWrapper<MchStore>().lambda().eq(MchStore::getMchId, mchStoreBanner.getMchId()));
            if (storeList.size() == 0) return false;
            // 如果轮播图配置ID不为空，执行删除关联操作
            if (mchStoreBanner.getBannerId() != null) {
                mchStoreBannerRelaMapper.deleteById(mchStoreBanner.getBannerId());
            }
            for (MchStore mchStore : storeList) {
                // 保存轮播图配置
                mchStoreBannerMapper.updateById(mchStoreBanner);
                bannerRela.setBannerId(mchStoreBanner.getBannerId());
                bannerRela.setStoreId(mchStore.getStoreId());
                mchStoreBannerRelaMapper.insert(bannerRela);
            }
            return true;
        }
    }

    void setCriteria(LambdaQueryWrapper<MchStoreBanner> lambda, MchStoreBanner mchStoreBanner, Date currentDate) {
        if(mchStoreBanner != null) {
            if (mchStoreBanner.getBannerId() != null) lambda.eq(MchStoreBanner::getBannerId, mchStoreBanner.getBannerId());
            if (mchStoreBanner.getMchId() != null) lambda.eq(MchStoreBanner::getMchId, mchStoreBanner.getMchId());
            if (StringUtils.isNotEmpty(mchStoreBanner.getBannerName())) lambda.like(MchStoreBanner::getBannerName, "%" + mchStoreBanner.getBannerName() + "%");
            if (mchStoreBanner.getShowType() != null && mchStoreBanner.getShowType() != -99) lambda.eq(MchStoreBanner::getShowType, mchStoreBanner.getShowType());
            if (mchStoreBanner.getStatus() != null && mchStoreBanner.getStatus() != -99) lambda.eq(MchStoreBanner::getStatus, mchStoreBanner.getStatus());
            if (mchStoreBanner.getPutStoreLimitType() != null) lambda.eq(MchStoreBanner::getPutStoreLimitType, mchStoreBanner.getPutStoreLimitType());
            if (mchStoreBanner.getAuthFrom() != null) lambda.eq(MchStoreBanner::getAuthFrom, mchStoreBanner.getAuthFrom());
        }
    }
}
