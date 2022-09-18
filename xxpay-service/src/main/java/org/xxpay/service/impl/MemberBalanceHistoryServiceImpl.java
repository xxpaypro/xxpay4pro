package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.MemberBalanceHistory;
import org.xxpay.core.service.IMemberBalanceHistoryService;
import org.xxpay.service.dao.mapper.MemberBalanceHistoryMapper;

/**
 * <p>
 * 商户会员储值流水表 服务实现类
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@Service
public class MemberBalanceHistoryServiceImpl extends ServiceImpl<MemberBalanceHistoryMapper, MemberBalanceHistory> implements IMemberBalanceHistoryService {

    @Override
    public IPage<MemberBalanceHistory> selectPage(Page page, MemberBalanceHistory balanceHistory) {

        LambdaQueryWrapper<MemberBalanceHistory> lambda = new QueryWrapper<MemberBalanceHistory>().lambda();
        lambda.eq(MemberBalanceHistory::getMchId, balanceHistory.getMchId());
        lambda.orderByDesc(MemberBalanceHistory::getCreateTime);
        setQueryParam(lambda, balanceHistory);

        return baseMapper.selectPage(page, lambda);
    }

    void setQueryParam(LambdaQueryWrapper<MemberBalanceHistory> lambda, MemberBalanceHistory balanceHistory) {
        if(balanceHistory != null) {
            if(balanceHistory.getMemberId() != null) lambda.eq(MemberBalanceHistory::getMemberId, balanceHistory.getMemberId());
            if(StringUtils.isNotBlank(balanceHistory.getMemberNo())) lambda.eq(MemberBalanceHistory::getMemberNo, balanceHistory.getMemberNo());
            if(balanceHistory.getBizType() != null) lambda.eq(MemberBalanceHistory::getBizType, balanceHistory.getBizType());
            if(balanceHistory.getPayType() != null) lambda.eq(MemberBalanceHistory::getPayType, balanceHistory.getPayType());
            if(balanceHistory.getPageOrigin() != null) lambda.eq(MemberBalanceHistory::getPageOrigin, balanceHistory.getPageOrigin());
            if(StringUtils.isNotBlank(balanceHistory.getOperatorId())) lambda.eq(MemberBalanceHistory::getOperatorId, balanceHistory.getOperatorId());
            if(StringUtils.isNotBlank(balanceHistory.getBizOrderId())) lambda.eq(MemberBalanceHistory::getBizOrderId, balanceHistory.getBizOrderId());
            if(balanceHistory.getMemberId() != null) lambda.eq(MemberBalanceHistory::getMemberId, balanceHistory.getMemberId());

            if(StringUtils.isNotEmpty(balanceHistory.getPsStringVal("createTimeStart"))){
                lambda.ge(MemberBalanceHistory::getCreateTime, DateUtil.str2date(balanceHistory.getPsStringVal("createTimeStart")));
            }
            if(StringUtils.isNotEmpty(balanceHistory.getPsStringVal("createTimeEnd"))){
                lambda.le(MemberBalanceHistory::getCreateTime, DateUtil.str2date(balanceHistory.getPsStringVal("createTimeEnd")));
            }
        }
    }
}
