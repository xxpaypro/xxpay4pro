package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.entity.MchStoreSpeaker;
import org.xxpay.core.service.IMchStoreSpeakerService;
import org.xxpay.service.dao.mapper.MchStoreSpeakerMapper;

/**
 * <p>
 * 门店与云喇叭关联表 服务实现类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-09
 */
@Service
public class MchStoreSpeakerServiceImpl extends ServiceImpl<MchStoreSpeakerMapper, MchStoreSpeaker> implements IMchStoreSpeakerService {

    @Autowired
    private MchStoreSpeakerMapper speakerMapper;

    @Override
    public Long creatCodeMoney(Long storeId, Long speakerId) {
        MchStoreSpeaker speaker = new MchStoreSpeaker();
        //判断是否已存在
        MchStoreSpeaker mchStoreSpeaker = speakerMapper.selectById(storeId);
        //生成随机金额
        Double code = (Math.random()*(9999-1000+1))+1000;
        long codeMoney = Math.round(code);
        speaker.setMoneyCode(codeMoney);
        if (mchStoreSpeaker == null) {
            speaker.setStoreId(storeId);
            speaker.setSpeakerId(speakerId);
            speaker.setStatus(MchConstant.MCH_STORE_SPEAKER_STATUS_NOT);
            speakerMapper.insert(speaker);
        } else {
            mchStoreSpeaker.setSpeakerId(speakerId);
            mchStoreSpeaker.setMoneyCode(codeMoney);
            mchStoreSpeaker.setStatus(MchConstant.MCH_STORE_SPEAKER_STATUS_NOT);
            speakerMapper.updateById(mchStoreSpeaker);
        }

        return codeMoney;
    }
}
