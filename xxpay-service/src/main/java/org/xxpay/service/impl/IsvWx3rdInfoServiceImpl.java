package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.core.service.IIsvWx3rdInfoService;
import org.xxpay.service.dao.mapper.IsvWx3rdInfoMapper;

/**
 * <p>
 * 服务商第三方配置表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-23
 */
@Service
public class IsvWx3rdInfoServiceImpl extends ServiceImpl<IsvWx3rdInfoMapper, IsvWx3rdInfo> implements IIsvWx3rdInfoService {


    @Override
    public IsvWx3rdInfo findByIsvIdAndAppId(Long isvId, String componentAppId){

        return this.getOne(new QueryWrapper<IsvWx3rdInfo>()
        .lambda().eq(IsvWx3rdInfo::getIsvId, isvId)
                 .eq(IsvWx3rdInfo::getComponentAppId, componentAppId)
        );
    }

    @Override
    public String getCheckVal(String checkFileName){
        IsvWx3rdInfo result = this.getOne(new QueryWrapper<IsvWx3rdInfo>()
                .lambda().eq(IsvWx3rdInfo::getConfigWxCheckFileName, checkFileName)
                .select(IsvWx3rdInfo::getConfigWxCheckFileValue)
        );
        return result == null ? null : result.getConfigWxCheckFileValue();
    }

}
