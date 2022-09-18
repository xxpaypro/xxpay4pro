package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchMiniVersion;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.core.service.IMchMiniVersionService;
import org.xxpay.service.dao.mapper.MchMiniVersionMapper;
import org.xxpay.service.dao.mapper.MchWxauthInfoMapper;

/**
 * <p>
 * 商户授权小程序版本管理记录表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-28
 */
@Service
public class MchMiniVersionServiceImpl extends ServiceImpl<MchMiniVersionMapper, MchMiniVersion> implements IMchMiniVersionService {

    @Autowired
    private MchWxauthInfoMapper mchWxauthInfoMapper;

    @Override
    public MchMiniVersion getByMchIdAndVersionstatus(Long mchId, Byte authFrom,  Byte versionStatus) {

        MchWxauthInfo mchWxauthInfo = mchWxauthInfoMapper.selectOne(new QueryWrapper<MchWxauthInfo>().lambda()
            .eq(MchWxauthInfo::getMchId, mchId)
            .eq(MchWxauthInfo::getAuthFrom, authFrom)
        );

        if (mchWxauthInfo == null) return null;

        MchMiniVersion mchMiniVersion = baseMapper.selectOne(new QueryWrapper<MchMiniVersion>().lambda()
                .eq(MchMiniVersion::getMchId, mchWxauthInfo.getMchId())
                .eq(MchMiniVersion::getAuthFrom, mchWxauthInfo.getAuthFrom())
                .eq(MchMiniVersion::getAuthAppId, mchWxauthInfo.getAuthAppId())
                .eq(MchMiniVersion::getVersionStatus, versionStatus)
        );

        return mchMiniVersion;
    }

    @Override
    public MchMiniVersion getByIsvIdAndAppIdAndAudits(Long isvId, String appId, Byte auditStatus) {

        MchMiniVersion mchMiniVersion = baseMapper.selectOne(new QueryWrapper<MchMiniVersion>().lambda()
                .eq(MchMiniVersion::getIsvId, isvId)
                .eq(MchMiniVersion::getAuthAppId, appId)
                .eq(MchMiniVersion::getAuditStatus, auditStatus)
                .eq(MchMiniVersion::getVersionStatus, MchConstant.MCH_MINI_VERSION_AUDIT)
        );

        return mchMiniVersion;
    }
}
