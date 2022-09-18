package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchWxauthInfo;

/**
 * <p>
 * 商户第三方授权信息表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-23
 */
public interface IMchWxauthInfoService extends IService<MchWxauthInfo> {

    MchWxauthInfo getOneByMchIdAndAuthFrom(Long mchId, Byte authFrom);

    String getCheckVal(String checkFileName, Long mchId, Byte authFrom);
}
