package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.core.service.IMchWxauthInfoService;
import org.xxpay.service.dao.mapper.MchWxauthInfoMapper;

/**
 * <p>
 * 商户第三方授权信息表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-23
 */
@Service
public class MchWxauthInfoServiceImpl extends ServiceImpl<MchWxauthInfoMapper, MchWxauthInfo> implements IMchWxauthInfoService {


    @Override
    public MchWxauthInfo getOneByMchIdAndAuthFrom(Long mchId, Byte authFrom) {
        LambdaQueryWrapper<MchWxauthInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchWxauthInfo::getMchId, mchId);
        queryWrapper.eq(MchWxauthInfo::getAuthFrom, authFrom);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public String getCheckVal(String checkFileName, Long mchId, Byte authFrom){
        MchWxauthInfo result = this.getOne(new QueryWrapper<MchWxauthInfo>()
                .lambda().eq(MchWxauthInfo::getWxCheckFileName, checkFileName)
                .eq(MchWxauthInfo::getMchId, mchId)
                .eq(MchWxauthInfo::getAuthFrom, authFrom)
                .select(MchWxauthInfo::getWxCheckFileValue)
        );
        return result == null ? null : result.getWxCheckFileValue();
    }

}
