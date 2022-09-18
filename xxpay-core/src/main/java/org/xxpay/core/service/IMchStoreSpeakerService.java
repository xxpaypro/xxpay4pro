package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.IsvSpeakerConfig;
import org.xxpay.core.entity.MchStoreSpeaker;
import org.xxpay.core.entity.SysUser;

/**
 * <p>
 * 门店与云喇叭关联表 服务类
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-09
 */
public interface IMchStoreSpeakerService extends IService<MchStoreSpeaker> {

    /**
     * 生成验证金额
     * @param storeId
     * @param speakerId
     * @return
     */
    Long creatCodeMoney(Long storeId, Long speakerId);

}
