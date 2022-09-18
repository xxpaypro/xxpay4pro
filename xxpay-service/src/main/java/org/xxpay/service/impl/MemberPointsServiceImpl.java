package org.xxpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.vo.JWTBaseUser;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.IMemberPointsHistoryService;
import org.xxpay.core.service.IMemberPointsService;
import org.xxpay.core.service.IMemberService;
import org.xxpay.service.dao.mapper.MemberPointsMapper;

/**
 * <p>
 * 商户会员积分表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberPointsServiceImpl extends ServiceImpl<MemberPointsMapper, MemberPoints> implements IMemberPointsService {

    @Autowired
    private IMemberPointsHistoryService memberPointsHistoryService;

    @Autowired
    private IMemberService memberService;


    public int updatePointByMemberId(MemberPoints memberPoints){
        return baseMapper.updatePointByMemberId(memberPoints);
    }

    @Override
    public int initMemberPoint(Long mchId, Long memberId, String memberNo, Long points, Long newMemberGivePoint, Long operatorId, String operatorName) {

        MemberPoints record = new MemberPoints();
        record.setMemberId(memberId);
        record.setMemberNo(memberNo);
        record.setPoints(0L + newMemberGivePoint + points);
        record.setTotalAddPoints(0L + newMemberGivePoint + points);
        record.setTotalGivePoints(0L + newMemberGivePoint + points);
        record.setTotalConsumePoints(0L);
        record.setTotalRefundPoints(0L);
        int count = baseMapper.insert(record);
        if (count != 1) return 0;

        if (points > 0) {
            MemberPointsHistory pointsHistoryRecord = new MemberPointsHistory();
            pointsHistoryRecord.setMemberId(memberId);
            pointsHistoryRecord.setMemberNo(memberNo);
            pointsHistoryRecord.setChangePoints(points);
            pointsHistoryRecord.setPoints(0L);
            pointsHistoryRecord.setAfterPoints(0L + points);
            pointsHistoryRecord.setMchId(mchId);
            pointsHistoryRecord.setBizType(MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_RECHARGE);
            pointsHistoryRecord.setOperatorId(operatorId == null ? null : String.valueOf(operatorId));
            pointsHistoryRecord.setOperatorName(operatorName);
            boolean result = memberPointsHistoryService.save(pointsHistoryRecord);
            if (!result) return 0;
        }

        MemberPoints record2 = this.getById(memberId);

        if (newMemberGivePoint > 0) {
            MemberPointsHistory pointsHistoryRecord = new MemberPointsHistory();
            pointsHistoryRecord.setMemberId(memberId);
            pointsHistoryRecord.setMemberNo(memberNo);
            pointsHistoryRecord.setChangePoints(newMemberGivePoint);
            pointsHistoryRecord.setPoints(record2.getPoints() - newMemberGivePoint);
            pointsHistoryRecord.setAfterPoints(record2.getPoints());
            pointsHistoryRecord.setMchId(mchId);
            pointsHistoryRecord.setBizType(MchConstant.MCH_VIP_POINTS_HISTORY_BIZ_TYPE_GIVE);
            pointsHistoryRecord.setOperatorId(operatorId == null ? null : String.valueOf(operatorId));
            pointsHistoryRecord.setOperatorName(operatorName);
            boolean result = memberPointsHistoryService.save(pointsHistoryRecord);
            if (!result) return 0;
        }

        return 1;
    }


    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public void refundPoints(Long memberId, Long subGivePoints, String mchRefundOrderId){

        if(subGivePoints <= 0){  //无需扣减积分
            return ;
        }

        Member member = memberService.getById(memberId);

        MemberPoints updatePointsRecord = new MemberPoints();
        updatePointsRecord.setMemberId(memberId);
        updatePointsRecord.setTotalRefundPoints(subGivePoints);
        updatePointsRecord.setPoints((0-subGivePoints));

        int updateRow = updatePointByMemberId(updatePointsRecord);
        if(updateRow <= 0){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL); //事务回滚
        }

        MemberPoints pointsRecord = getById(memberId);
        MemberPointsHistory pointsHistory = new MemberPointsHistory();
        pointsHistory.setMemberId(memberId);   //memberId
        pointsHistory.setMemberNo(member.getMemberNo());   //memberNo
        pointsHistory.setChangePoints((0-subGivePoints));  //变更金额积分
        pointsHistory.setPoints(pointsRecord.getPoints() + subGivePoints ); //变更前账户余额积分
        pointsHistory.setAfterPoints(pointsRecord.getPoints()); //变更后账户余额积分
        pointsHistory.setMchId(member.getMchId());  //mchId
        pointsHistory.setBizOrderId(mchRefundOrderId);  //bizOrderId
        pointsHistory.setBizType((byte)3);  //交易类型:1-充值, 2-消费, 3-退款, 4-导入, 5-赠送
        pointsHistory.setOperatorId(null);  //操作员ID
        pointsHistory.setOperatorName(null);  //操作员姓名
        memberPointsHistoryService.save(pointsHistory);
    }







}
