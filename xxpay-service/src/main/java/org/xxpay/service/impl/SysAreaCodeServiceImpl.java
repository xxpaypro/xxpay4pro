package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SysAreaCode;
import org.xxpay.core.entity.SysCityCode;
import org.xxpay.core.entity.SysProvinceCode;
import org.xxpay.core.service.ISysAreaCodeService;
import org.xxpay.core.service.ISysCityCodeService;
import org.xxpay.core.service.ISysProvinceCodeService;
import org.xxpay.service.dao.mapper.SysAreaCodeMapper;

/**
 * <p>
 * 地区编码表（区/县 级别） 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
@Service
public class SysAreaCodeServiceImpl extends ServiceImpl<SysAreaCodeMapper, SysAreaCode> implements ISysAreaCodeService {


    @Autowired
    private ISysProvinceCodeService sysProvinceCodeService;

    @Autowired
    private ISysCityCodeService sysCityCodeService;

    @Override
    public String formatAddress(Integer areaCode, String address){

        SysAreaCode areaCodeInfo = this.getById(areaCode);
        if(areaCodeInfo == null) return null;

        SysProvinceCode sysProvinceCode = sysProvinceCodeService.getById(areaCodeInfo.getProvinceCode());
        SysCityCode sysCityCode = sysCityCodeService.getById(areaCodeInfo.getCityCode());

        return sysProvinceCode.getProvinceName() + " " + sysCityCode.getCityName() + " "
                + areaCodeInfo.getAreaName() + " " + address;
    }

}
