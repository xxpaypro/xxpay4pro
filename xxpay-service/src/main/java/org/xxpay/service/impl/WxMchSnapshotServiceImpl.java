package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xxpay.core.entity.WxMchSnapshot;
import org.xxpay.core.service.IWxMchSnapshotService;
import org.xxpay.service.dao.mapper.WxMchSnapshotMapper;

/**
 * <p>
 * 微信商户入驻快照信息 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-21
 */
@Service
public class WxMchSnapshotServiceImpl extends ServiceImpl<WxMchSnapshotMapper, WxMchSnapshot> implements IWxMchSnapshotService {

    @Override
    public WxMchSnapshot findByMchAndIsv(Long mchId, Long isvId){

        return getOne(
                new QueryWrapper<WxMchSnapshot>().lambda()
                        .eq(WxMchSnapshot::getMchId, mchId)
                        .eq(WxMchSnapshot::getIsvId, isvId)
        );
    }

}
