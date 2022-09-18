package org.xxpay.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MemberPoints;

/**
 * <p>
 * 商户会员积分表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberPointsService extends IService<MemberPoints> {

    /**
     * 更新会员积分信息， 保证积分信息数据准确
     * @param memberPoints
     * @return
     */
    int updatePointByMemberId(MemberPoints memberPoints);

    /**
     * 初始化会员积分账户
     * @param mchId
     * @param memberId
     * @param memberNo
     * @param amount
     * @param newMemberGivePoint
     * @return
     */
    int initMemberPoint(Long mchId, Long memberId, String memberNo, Long points, Long newMemberGivePoint, Long operatorId, String operatorName);


    /** 退还积分 **/
    void refundPoints(Long memberId, Long subGivePoints, String mchRefundOrderId);

}
