package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.IsvInfo;
import org.xxpay.core.entity.SysUser;

/**
 * <p>
 * 服务商信息表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-27
 */
public interface IIsvInfoService extends IService<IsvInfo> {

    IsvInfo findByLoginName(String loginName);

    /** 新建服务商 **/
    void addIsv(IsvInfo isvInfo);

    /** 更改服务商信息 **/
    void updateIsv(IsvInfo isvInfo, SysUser isvUser);

    /**
     * 服务商审核
     * @param isvId
     * @param status
     */
    void auditMch(Long isvId, Byte status);
}
