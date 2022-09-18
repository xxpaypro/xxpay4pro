package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.AlipayMchSnapshot;
import org.xxpay.core.service.IAlipayMchSnapshotService;
import org.xxpay.service.dao.mapper.AlipayMchSnapshotMapper;

/**
 * <p>
 * 支付宝商户入驻快照信息 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-27
 */
@Service
public class AlipayMchSnapshotServiceImpl extends ServiceImpl<AlipayMchSnapshotMapper, AlipayMchSnapshot> implements IAlipayMchSnapshotService {


    @Override
    public AlipayMchSnapshot findByMchAndIsv(Long mchId, Long isvId){

        return getOne(
                new QueryWrapper<AlipayMchSnapshot>().lambda()
                        .eq(AlipayMchSnapshot::getMchId, mchId)
                        .eq(AlipayMchSnapshot::getIsvId, isvId)
        );
    }


}
