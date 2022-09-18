package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.SysAreaCode;

/**
 * <p>
 * 地区编码表（区/县 级别） 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-27
 */
public interface ISysAreaCodeService extends IService<SysAreaCode> {

    /** 获取格式化后的地址格式 **/
    String formatAddress(Integer areaCode, String address);

}
