package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchMiniConfig;
import org.xxpay.core.service.IMchMiniConfigService;
import org.xxpay.service.dao.mapper.MchMiniConfigMapper;

/**
 * <p>
 * 商户小程序配置表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-18
 */
@Service
public class MchMiniConfigServiceImpl extends ServiceImpl<MchMiniConfigMapper, MchMiniConfig> implements IMchMiniConfigService {

    @Override
    public int visualableSaveOrUpdate(Long mchId, String componentList, String componentIndex, Byte authFrom, Byte isDraft) {
        MchMiniConfig mchMiniConfig = new MchMiniConfig();
        mchMiniConfig.setConfigCode(componentIndex);
        mchMiniConfig.setConfigName(componentIndex);
        mchMiniConfig.setValue(componentList);
        mchMiniConfig.setMchId(mchId);
        mchMiniConfig.setAuthFrom(authFrom);
        mchMiniConfig.setStatus(MchConstant.PUB_YES);
        mchMiniConfig.setIsDraft(isDraft);

        MchMiniConfig dbRecord = baseMapper.selectOne(new QueryWrapper<MchMiniConfig>().lambda()
                .eq(MchMiniConfig::getMchId, mchId)
                .eq(MchMiniConfig::getConfigCode, componentIndex)
                .eq(MchMiniConfig::getAuthFrom, authFrom)
                .eq(MchMiniConfig::getStatus, MchConstant.PUB_YES)
                .eq(MchMiniConfig::getIsDraft, isDraft)
        );

        int count;
        if (dbRecord == null) {
            count = baseMapper.insert(mchMiniConfig);
            if (count != 1) return 0;
            return count;
        }

        mchMiniConfig.setConfigId(dbRecord.getConfigId());
        count = baseMapper.updateById(mchMiniConfig);
        if (count != 1) return 0;

        return count;
    }
}
