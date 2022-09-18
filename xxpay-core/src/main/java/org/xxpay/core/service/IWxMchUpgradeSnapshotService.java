package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.WxMchUpgradeSnapshot;

/**
 * <p>
 * 微信商户升级快照 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-28
 */
public interface IWxMchUpgradeSnapshotService extends IService<WxMchUpgradeSnapshot> {


    /** 根据isvId + 商户ID 查询 **/
    WxMchUpgradeSnapshot findByMchAndIsv(Long mchId, Long isvId);

}
