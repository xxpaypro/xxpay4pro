package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchMiniVersion;
import org.xxpay.core.entity.MchWxauthInfo;

/**
 * <p>
 * 商户授权小程序版本管理记录表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-28
 */
public interface IMchMiniVersionService extends IService<MchMiniVersion> {

    MchMiniVersion getByMchIdAndVersionstatus(Long mchId, Byte authFrom, Byte versionStatus);

    MchMiniVersion getByIsvIdAndAppIdAndAudits(Long isvId, String appId, Byte auditStatus);
}
