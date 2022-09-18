package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MemberBalance;

/**
 * <p>
 * 商户会员储值账户表 Mapper 接口
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface MemberBalanceMapper extends BaseMapper<MemberBalance> {

    /** 更新会员账户信息 **/
    int updateBalanceByMemberId(MemberBalance memberBalance);

}
