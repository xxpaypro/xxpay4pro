package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.SxfMchSnapshot;
import org.xxpay.core.service.ISxfMchSnapshotService;
import org.xxpay.service.dao.mapper.SxfMchSnapshotMapper;

/**
 * <p>
 * 随行付商户入驻快照信息 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-01
 */
@Service
public class SxfMchSnapshotServiceImpl extends ServiceImpl<SxfMchSnapshotMapper, SxfMchSnapshot> implements ISxfMchSnapshotService {

    @Autowired
    private SxfMchSnapshotMapper sxfMchSnapshotMapper;

    @Override
    public SxfMchSnapshot findByMchAndIsv(Long mchId, Long currentIsvId) {
        QueryWrapper<SxfMchSnapshot> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SxfMchSnapshot::getMchId, mchId);
        wrapper.lambda().eq(SxfMchSnapshot::getIsvId, currentIsvId);
        return sxfMchSnapshotMapper.selectOne(wrapper);
    }
}
