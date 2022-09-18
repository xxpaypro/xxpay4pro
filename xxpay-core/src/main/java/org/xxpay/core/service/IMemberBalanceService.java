package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MemberBalance;

/**
 * <p>
 * 商户会员储值账户表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberBalanceService extends IService<MemberBalance> {

    /**
     * 初始化会员储值账户
     * @param mchId
     * @param memberId
     * @param memberNo
     * @param amount
     * @param newMemberGiveBalance
     * @return
     */
    int initMemberBalance(Long mchId, Long memberId, String memberNo, Long amount, Long newMemberGiveBalance, Long operatorId, String operatorName);


    /**
     * 余额退款
     * 1. 余额回充
     * 2. 记录余额资金流水
    **/
    void refundAmount(Long memberId, Long refundAmount, String mchRefundOrderId, byte payType);


	
	/** 更新会员账户余额信息 **/
    int updateBalanceByMemberId(MemberBalance memberBalance);
}
