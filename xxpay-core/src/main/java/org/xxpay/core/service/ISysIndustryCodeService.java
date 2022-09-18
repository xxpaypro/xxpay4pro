package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.SysIndustryCode;

/**
 * <p>
 * 行业编码表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
public interface ISysIndustryCodeService extends IService<SysIndustryCode> {

    /** 获取行业信息 **/
    String formatIndustryInfo(Integer industryCode);

    /** 递归获取获取所有ID **/
    String getAllIndustryCode(Integer industryCode);

}
