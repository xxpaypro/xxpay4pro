package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.service.IMchStoreService;
import org.xxpay.service.dao.mapper.MchStoreMapper;

/**
 * <p>
 * 商户门店表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MchStoreServiceImpl extends ServiceImpl<MchStoreMapper, MchStore> implements IMchStoreService {

    @Autowired
    private MchStoreMapper mchStoreMapper;

    @Override
    public IPage<MchStore> list(MchStore mchStore, IPage page) {
        LambdaQueryWrapper<MchStore> lambda = new QueryWrapper<MchStore>().lambda();
        lambda.orderByDesc(MchStore::getCreateTime);
        setCriteria(lambda, mchStore);
        return page(page, lambda);
    }

    void setCriteria(LambdaQueryWrapper<MchStore> lambda, MchStore mchStore) {
        if(mchStore != null) {
            if (mchStore.getStoreId() != null) lambda.eq(MchStore::getStoreId, mchStore.getStoreId());
            if (StringUtils.isNotEmpty(mchStore.getStoreNo())) lambda.eq(MchStore::getStoreNo, mchStore.getStoreNo());
            if (StringUtils.isNotEmpty(mchStore.getStoreName())) lambda.like(MchStore::getStoreName, "%"+ mchStore.getStoreName() + "%");
            if (mchStore.getStatus() != null) lambda.eq(MchStore::getStatus, mchStore.getStatus());
            if (mchStore.getMchId() != null) lambda.eq(MchStore::getMchId, mchStore.getMchId());
        }
    }
}
