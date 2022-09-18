package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.entity.MchMemberConfig;
import org.xxpay.core.entity.Member;
import org.xxpay.core.entity.MemberOpenidRela;
import org.xxpay.core.service.IMchMemberConfigService;
import org.xxpay.core.service.IMemberBalanceService;
import org.xxpay.core.service.IMemberPointsService;
import org.xxpay.core.service.IMemberService;
import org.xxpay.service.dao.mapper.MemberMapper;
import org.xxpay.service.dao.mapper.MemberOpenidRelaMapper;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 商户会员表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    @Autowired
    private IMemberBalanceService memberBalanceService;

    @Autowired
    private IMemberPointsService memberPointsService;

    @Autowired
    private IMchMemberConfigService mchMemberConfigService;

    @Autowired
    private MemberOpenidRelaMapper memberOpenidRelaMapper;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int initMember(Member member, Long amount, Long points, Long operatorId, String operatorName) {
        return initMemberWithOpenid(member, amount, points, operatorId, operatorName, null, null);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int initMemberWithOpenid(Member member, Long amount, Long points, Long operatorId, String operatorName, Byte authFrom, String openid) {
        //插入会员基本信息
        member.setMemberNo(getMbrCardNo(member.getMchId()));
        member.setAvatar(StringUtils.isNotBlank(member.getAvatar()) ? member.getAvatar() : MchConstant.MCH_MEMBER_DEFAULT_AVATAR);
        int count = baseMapper.insert(member);
        if(count != 1) return 0;

        //查询商户会员首次领取赠送规则NewMemberRule：0-无优惠, 1-赠余额, 2-赠积分
        Long newMemberGiveBalance = 0L;
        Long newMemberGivePoint = 0L;
        MchMemberConfig mchMemberConfig = mchMemberConfigService.getById(member.getMchId());
        if (mchMemberConfig != null && mchMemberConfig.getNewMemberRule() == 1) {
            newMemberGiveBalance = mchMemberConfig.getConsumeGivePoints();
        } else if (mchMemberConfig != null && mchMemberConfig.getNewMemberRule() == 2) {
            newMemberGivePoint = mchMemberConfig.getNewMemberGivePoints();
        }

        //插入会员储值账户
        count = memberBalanceService.initMemberBalance(member.getMchId(), member.getMemberId(), member.getMemberNo(),
                amount, newMemberGiveBalance, operatorId, operatorName);
        if(count != 1) return 0;

        //插入会员积分账户
        count = memberPointsService.initMemberPoint(member.getMchId(), member.getMemberId(), member.getMemberNo(),
                points, newMemberGivePoint, operatorId, operatorName);
        if(count != 1) return 0;

        if (authFrom != null && StringUtils.isNotBlank(openid)) {
            //保存openid
            MemberOpenidRela openidRela = new MemberOpenidRela();
            openidRela.setMemberId(member.getMemberId());
            openidRela.setWxOpenId(openid);
            openidRela.setWxOpenIdFrom(authFrom);
            openidRela.setMchId(member.getMchId());
            count = memberOpenidRelaMapper.insert(openidRela);
            if(count != 1) return 0;
        }

        return 1;
    }

    @Override
    public Member getByMchIdAndTel(Member member) {
        LambdaQueryWrapper<Member> queryWrapper = new QueryWrapper<Member>().lambda();
        queryWrapper.eq(Member::getMchId, member.getMchId());
        queryWrapper.eq(Member::getTel, member.getTel());
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public JSONObject selectMemberAllInfo(Long mchId, Long memberId, String wxOpenId){

        if(memberId == null && StringUtils.isEmpty(wxOpenId)){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);  //参数有误
        }

        Map queryResult = baseMapper.selectMemberAllInfo(mchId, memberId, wxOpenId);
        return (JSONObject)JSONObject.toJSON(queryResult);
    }
	
	@Override
    public Integer countAddMember(Date startTime, Date endTime, Long mchId){

        return baseMapper.selectCount(new QueryWrapper<Member>().lambda()
                .eq(Member::getMchId, mchId)
        .ge(Member::getCreateTime, startTime)
        .le(Member::getCreateTime, endTime));
    }

    @Override
    public Integer countMember(Long mchId){

        return baseMapper.selectCount(new QueryWrapper<Member>().lambda()
                .eq(Member::getMchId, mchId));
    }

    @Override
    public int countMemberBySex(Date thisStartQueryTime, Date thisEndQueryTime, Long queryMchId, Byte sex) {
        LambdaQueryWrapper<Member> lambda = new QueryWrapper<Member>().lambda();
        setQueryParam(lambda, thisStartQueryTime, thisEndQueryTime, queryMchId, sex);
        int count = baseMapper.selectCount(lambda);
        return count;
    }
    
    private String getMbrCardNo(Long mchId) {
        String mchIdStr = String.valueOf(mchId);
        String mbrSeq = MySeq.getMbrCardNoSeq();
        return String.format("%s%s", mchIdStr.substring(mchIdStr.length()-4), mbrSeq);
    }

    void setQueryParam(LambdaQueryWrapper<Member> lambda, Date thisStartQueryTime, Date thisEndQueryTime, Long queryMchId, Byte sex) {
        if(queryMchId != null) lambda.eq(Member::getMchId, queryMchId);
        if (sex != null)lambda.eq(Member::getSex, sex);

        if(thisStartQueryTime != null){
            lambda.ge(Member::getCreateTime, thisStartQueryTime);
        }
        if(thisEndQueryTime != null){
            lambda.le(Member::getCreateTime, thisEndQueryTime);
        }
    }


}
