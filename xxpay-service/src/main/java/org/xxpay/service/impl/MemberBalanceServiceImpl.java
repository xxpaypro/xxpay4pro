package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.MemberBalanceMapper;

/**
 * <p>
 * 商户会员储值账户表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberBalanceServiceImpl extends ServiceImpl<MemberBalanceMapper, MemberBalance> implements IMemberBalanceService {

    @Autowired
    private IMemberBalanceHistoryService memberBalanceHistoryService;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IMemberPointsService memberPointsService;

    @Autowired
    private IMemberPointsHistoryService memberPointsHistoryService;


    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int initMemberBalance(Long mchId, Long memberId, String memberNo, Long amount, Long newMemberGiveBalance, Long operatorId, String operatorName) {

        //初始化会员账户
        MemberBalance record = new MemberBalance();
        record.setMemberId(memberId);
        record.setMemberNo(memberNo);
        record.setBalance(0L + newMemberGiveBalance + amount);
        record.setTotalRechargeAmount(0L + amount);
        record.setTotalGiveAmount(0L + newMemberGiveBalance);
        record.setTotalConsumeAmount(0L);
        record.setTotalRefundAmount(0L);
        int count = baseMapper.insert(record);
        if (count != 1) return 0;

        if (amount > 0) {
            MemberBalanceHistory balanceHistoryRecord = new MemberBalanceHistory();
            balanceHistoryRecord.setMemberId(memberId);
            balanceHistoryRecord.setMemberNo(memberNo);
            balanceHistoryRecord.setChangeAmount(amount);
            balanceHistoryRecord.setBalance(0L);
            balanceHistoryRecord.setAfterBalance(0L + amount);
            balanceHistoryRecord.setGiveAmount(0L);
            balanceHistoryRecord.setMchId(mchId);
            balanceHistoryRecord.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_RECHARGE);
            balanceHistoryRecord.setOperatorId(operatorId == null ? null : String.valueOf(operatorId));
            balanceHistoryRecord.setOperatorName(operatorName);
            boolean result = memberBalanceHistoryService.save(balanceHistoryRecord);
            if (!result) return 0;
        }

        MemberBalance record2 = this.getById(memberId);

        if (newMemberGiveBalance > 0) {
            MemberBalanceHistory balanceHistoryRecord2 = new MemberBalanceHistory();
            balanceHistoryRecord2.setMemberId(memberId);
            balanceHistoryRecord2.setMemberNo(memberNo);
            balanceHistoryRecord2.setChangeAmount(newMemberGiveBalance);
            balanceHistoryRecord2.setBalance(record2.getBalance() - newMemberGiveBalance);
            balanceHistoryRecord2.setAfterBalance(record2.getBalance());
            balanceHistoryRecord2.setGiveAmount(newMemberGiveBalance);
            balanceHistoryRecord2.setMchId(mchId);
            balanceHistoryRecord2.setBizType(MchConstant.MCH_MEMBER_BALANCE_HISTORY_BIZ_TYPE_GIVE);
            balanceHistoryRecord2.setOperatorId(operatorId == null ? null : String.valueOf(operatorId));
            balanceHistoryRecord2.setOperatorName(operatorName);
            boolean result = memberBalanceHistoryService.save(balanceHistoryRecord2);
            if (!result) return 0;
        }

        return 1;
    }

    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void refundAmount(Long memberId, Long refundAmount, String mchRefundOrderId, byte payType){

        Member member = memberService.getById(memberId);
        MemberBalance updateRecord = new MemberBalance();
        updateRecord.setMemberId(memberId);
        updateRecord.setTotalRefundAmount(refundAmount);
        updateRecord.setBalance(refundAmount);

        int updateRow = this.updateBalanceByMemberId(updateRecord);
        if(updateRow <= 0){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL); //事务回滚
        }

        MemberBalance record = this.getById(memberId);
        MemberBalanceHistory history = new MemberBalanceHistory();
        history.setMemberId(memberId);   //memberId
        history.setMemberNo(member.getMemberNo());   //memberNo
        history.setChangeAmount(refundAmount);  //变更金额
        history.setBalance(record.getBalance() - refundAmount ); //变更前账户余额
        history.setAfterBalance(record.getBalance()); //变更后账户余额
        history.setGiveAmount(0L); //赠送积分
        history.setMchId(member.getMchId());  //mchId
        history.setBizOrderId(mchRefundOrderId);  //bizOrderId
        history.setBizType((byte)3);  //交易类型:1-充值, 2-消费, 3-退款, 4-导入, 5-赠送
        history.setPayType(payType); //支付方式:1-微信, 2-支付宝, 3-储值卡, 4-导入
        history.setPageOrigin(null); //来源 1-PC, 2-ANDROID, 3-IOS, 4-H5  //TODO 页面来源， 操作员ID, 操作员姓名 如何在service中获取？？ 可封装成公共函数， 统一调用， 获取request / 线程变量, 取不到都是null.
        history.setOperatorId(null);  //操作员ID
        history.setOperatorName(null);  //操作员姓名

        if(!memberBalanceHistoryService.save(history)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

	
	@Override
    public int updateBalanceByMemberId(MemberBalance memberBalance){
        return baseMapper.updateBalanceByMemberId(memberBalance);
    }
	
	

}
