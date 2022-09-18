package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.FeeRiskConfig;
import org.xxpay.core.service.IFeeRiskConfigService;
import org.xxpay.service.dao.mapper.FeeRiskConfigMapper;

@Service
public class FeeRiskConfigImpl extends ServiceImpl<FeeRiskConfigMapper, FeeRiskConfig> implements IFeeRiskConfigService {

    @Autowired
    private FeeRiskConfigMapper feeRiskConfigMapper;

    @Override
    public FeeRiskConfig findById(Integer id) {
        return feeRiskConfigMapper.selectByPrimaryKey(id);
    }
}
