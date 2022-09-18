package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.MchNotify;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/10/26
 * @description: 商户通知业务
 */
public interface IMchNotifyService extends IService<MchNotify> {

    MchNotify findByOrderId(String orderId);

    List<MchNotify> select(int offset, int limit, MchNotify mchNotify);

    Integer count(MchNotify mchNotify);

    int insertSelectiveOnDuplicateKeyUpdate(String orderId, Long mchId, Long isvId, String appId, String mchOrderNo, String orderType, String notifyUrl);

    int updateMchNotifySuccess(String orderId, String result, byte notifyCount);

    int updateMchNotifyFail(String orderId, String result, byte notifyCount);

}
