package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchStoreAreaManage;
import org.xxpay.core.service.IMchStoreAreaManageService;
import org.xxpay.service.dao.mapper.MchStoreAreaManageMapper;

/**
 * <p>
 * 商户餐饮店区域管理表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@Service
public class MchStoreAreaManageServiceImpl extends ServiceImpl<MchStoreAreaManageMapper, MchStoreAreaManage> implements IMchStoreAreaManageService {

    @Override
    public IPage<MchStoreAreaManage> list(MchStoreAreaManage mchStoreAreaManage, IPage page) {
        LambdaQueryWrapper<MchStoreAreaManage> lambda = new QueryWrapper<MchStoreAreaManage>().lambda();
        if (mchStoreAreaManage.getMchId() != null) lambda.eq(MchStoreAreaManage::getMchId, mchStoreAreaManage.getMchId());
        if (mchStoreAreaManage.getId() != null) lambda.eq(MchStoreAreaManage::getId, mchStoreAreaManage.getId());
        lambda.orderByDesc(MchStoreAreaManage::getCreateTime);
        return page(page, lambda);
    }
}
