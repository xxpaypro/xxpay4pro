package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.AlipayMchSnapshot;

/**
 * <p>
 * 支付宝商户入驻快照信息 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-27
 */
public interface IAlipayMchSnapshotService extends IService<AlipayMchSnapshot> {

    /** 根据商户号和isv查询 **/
    AlipayMchSnapshot findByMchAndIsv(Long mchId, Long isvId);

}
