package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.WxMchUpgradeSnapshot;
import org.xxpay.core.service.IWxMchUpgradeSnapshotService;
import org.xxpay.service.dao.mapper.WxMchUpgradeSnapshotMapper;

/**
 * <p>
 * 微信商户升级快照 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-28
 */
@Service
public class WxMchUpgradeSnapshotServiceImpl extends ServiceImpl<WxMchUpgradeSnapshotMapper, WxMchUpgradeSnapshot> implements IWxMchUpgradeSnapshotService {


    @Override
    public WxMchUpgradeSnapshot findByMchAndIsv(Long mchId, Long isvId){

        return getOne(
                new QueryWrapper<WxMchUpgradeSnapshot>().lambda()
                        .eq(WxMchUpgradeSnapshot::getMchId, mchId)
                        .eq(WxMchUpgradeSnapshot::getIsvId, isvId)
        );
    }

}
