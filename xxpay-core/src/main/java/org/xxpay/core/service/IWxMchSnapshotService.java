package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.WxMchSnapshot;

/**
 * <p>
 * 微信商户入驻快照信息 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-21
 */
public interface IWxMchSnapshotService extends IService<WxMchSnapshot> {

    /** 根据mchId & isvId 查询微信商户进件表信息 **/
    WxMchSnapshot findByMchAndIsv(Long mchId, Long isvId);

}
