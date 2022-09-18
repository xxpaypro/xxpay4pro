package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.FeeRiskConfig;

public interface IFeeRiskConfigService extends IService<FeeRiskConfig> {


    FeeRiskConfig findById(Integer id);



}
