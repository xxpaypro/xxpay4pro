package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.MemberPointsHistory;
import org.xxpay.core.service.IMemberPointsHistoryService;
import org.xxpay.service.dao.mapper.MemberPointsHistoryMapper;

import java.util.Date;

/**
 * <p>
 * 商户会员积分流水表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberPointsHistoryServiceImpl extends ServiceImpl<MemberPointsHistoryMapper, MemberPointsHistory> implements IMemberPointsHistoryService {

    @Override
    public IPage<MemberPointsHistory> selectPage(Page page, MemberPointsHistory pointsHistory) {
        LambdaQueryWrapper<MemberPointsHistory> lambda = new QueryWrapper<MemberPointsHistory>().lambda();
        lambda.eq(MemberPointsHistory::getMchId, pointsHistory.getMchId());
        lambda.orderByDesc(MemberPointsHistory::getCreateTime);
        setQueryParam(lambda, pointsHistory);

        return baseMapper.selectPage(page, lambda);
    }

    @Override
    public Long sumGivePoints(Date startTime, Date endTime, Long mchId) {
        return baseMapper.sumGivePoints(startTime, endTime, mchId);
    }

    @Override
    public Long sumConsumePoints(Date startTime, Date endTime, Long mchId) {
        return baseMapper.sumConsumePoints(startTime, endTime, mchId);
    }

    void setQueryParam(LambdaQueryWrapper<MemberPointsHistory> lambda, MemberPointsHistory pointsHistory) {
        if(pointsHistory != null) {
            if(pointsHistory.getMemberId() != null) lambda.eq(MemberPointsHistory::getMemberId, pointsHistory.getMemberId());
            if(StringUtils.isNotBlank(pointsHistory.getMemberNo())) lambda.eq(MemberPointsHistory::getMemberNo, pointsHistory.getMemberNo());
            if(pointsHistory.getBizType() != null) lambda.eq(MemberPointsHistory::getBizType, pointsHistory.getBizType());
            if(StringUtils.isNotBlank(pointsHistory.getOperatorId())) lambda.eq(MemberPointsHistory::getOperatorId, pointsHistory.getOperatorId());
            if(StringUtils.isNotBlank(pointsHistory.getBizOrderId())) lambda.eq(MemberPointsHistory::getBizOrderId, pointsHistory.getBizOrderId());
            if(pointsHistory.getMemberId() != null) lambda.eq(MemberPointsHistory::getMemberId, pointsHistory.getMemberId());

            if(StringUtils.isNotEmpty(pointsHistory.getPsStringVal("createTimeStart"))){
                lambda.ge(MemberPointsHistory::getCreateTime, DateUtil.str2date(pointsHistory.getPsStringVal("createTimeStart")));
            }
            if(StringUtils.isNotEmpty(pointsHistory.getPsStringVal("createTimeEnd"))){
                lambda.le(MemberPointsHistory::getCreateTime, DateUtil.str2date(pointsHistory.getPsStringVal("createTimeEnd")));
            }
        }
    }
}
