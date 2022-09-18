package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.Member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 商户会员表 服务类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
public interface IMemberService extends IService<Member> {

    /**
     * 新增会员  会员储值账户  会员积分账户
     * @param member
     * @param amount
     * @param points
     * @return
     */
    int initMember(Member member, Long amount, Long points, Long operatorId, String operatorName);

    int initMemberWithOpenid(Member member, Long amount, Long points, Long operatorId, String operatorName, Byte authFrom, String openid);

    /**
     * 根据会员卡号和手机号查询会员
     * @param member
     * @return
     */
    Member getByMchIdAndTel(Member member);


    /** 查询会员所有信息, 包含会员注册信息, 会员余额， 会员积分数据 （一般用于会员支付时显示）, 并转为JSON格式,  memberId, wxOpenId二选一 **/
    JSONObject selectMemberAllInfo(Long mchId, Long memberId, String wxOpenId);

    /** 查询时间范围内的新增会员数量 **/
    Integer countAddMember(Date startTime, Date endTime, Long mchId);

    /** 查询时间范围内的会员性别统计**/
    int countMemberBySex(Date thisStartQueryTime, Date thisEndQueryTime, Long queryMchId, Byte sex);

    /** 查询会员总数 **/
    Integer countMember(Long queryMchId);
}
