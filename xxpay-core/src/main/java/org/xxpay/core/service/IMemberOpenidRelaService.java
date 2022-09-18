package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MemberOpenidRela;

/**
 * <p>
 * 会员与微信openid关联表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-16
 */
public interface IMemberOpenidRelaService extends IService<MemberOpenidRela> {

    int saveOrUpdateRecord(Long memberId, String openid, Byte authFrom, Long mchId);
}
