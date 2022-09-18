package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.SxfMchSnapshot;
import org.xxpay.core.entity.WxMchSnapshot;

/**
 * <p>
 * 随行付商户入驻快照信息 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-01
 */
public interface ISxfMchSnapshotService extends IService<SxfMchSnapshot> {

    SxfMchSnapshot findByMchAndIsv(Long mchId, Long currentIsvId);
}
