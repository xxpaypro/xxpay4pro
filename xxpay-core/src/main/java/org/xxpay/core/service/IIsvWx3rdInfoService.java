package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.IsvWx3rdInfo;

/**
 * <p>
 * 服务商第三方配置表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-23
 */
public interface IIsvWx3rdInfoService extends IService<IsvWx3rdInfo> {

    /** 根据isvId 和 开放平台appId 获取信息 **/
    IsvWx3rdInfo findByIsvIdAndAppId(Long isvId, String componentAppId);

    /** 根据名称获取微信校验内容 **/
    String getCheckVal(String checkFileName);

}
