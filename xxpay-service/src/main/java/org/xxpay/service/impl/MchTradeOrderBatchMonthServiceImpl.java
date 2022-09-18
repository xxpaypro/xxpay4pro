package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.MchTradeOrderBatchMonth;
import org.xxpay.core.service.IMchTradeOrderBatchMonthService;
import org.xxpay.service.dao.mapper.MchTradeOrderBatchMonthMapper;

@Service
public class MchTradeOrderBatchMonthServiceImpl extends ServiceImpl<MchTradeOrderBatchMonthMapper, MchTradeOrderBatchMonth> implements IMchTradeOrderBatchMonthService {

    @Override
    public void add(MchTradeOrderBatchMonth mchTradeOrderBatchMonth) {
        //持久化
        if(!this.save(mchTradeOrderBatchMonth)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    @Override
    public void updateMchTradeOrder(MchTradeOrderBatchMonth mchTradeOrderBatchMonth) {

    }

    @Override
    public MchTradeOrderBatchMonth findByBatchId(String batchId) {
        return null;
    }

    @Override
    public MchTradeOrderBatchMonth getOneMchTradeOrderBatchMonth(String batchDate, Long mchId) {
        MchTradeOrderBatchMonth mchTradeOrderBatchMonth = new MchTradeOrderBatchMonth();
        mchTradeOrderBatchMonth.setBatchDate(batchDate);
        mchTradeOrderBatchMonth.setMchId(mchId);
        return getOne(getQueryWrapper(mchTradeOrderBatchMonth));
    }

    @Override
    public IPage<MchTradeOrderBatchMonth> selectPage(IPage page, MchTradeOrderBatchMonth mchTradeOrderBatchMonth) {
        LambdaQueryWrapper<MchTradeOrderBatchMonth> queryWrapper = getQueryWrapper(mchTradeOrderBatchMonth);
        queryWrapper.orderByDesc(MchTradeOrderBatchMonth::getCreateTime);
        return page(page, queryWrapper);
    }


    /** 生成[wrapper]查询条件 **/
    private LambdaQueryWrapper<MchTradeOrderBatchMonth> getQueryWrapper(MchTradeOrderBatchMonth mchTradeOrderBatchMonth){

        LambdaQueryWrapper<MchTradeOrderBatchMonth> queryWrapper = new LambdaQueryWrapper();

        if(mchTradeOrderBatchMonth != null) {
            if(StringUtils.isNotEmpty(mchTradeOrderBatchMonth.getBatchId())) queryWrapper.eq(MchTradeOrderBatchMonth::getBatchId, mchTradeOrderBatchMonth.getBatchId());
            if(StringUtils.isNotEmpty(mchTradeOrderBatchMonth.getBatchDate())) queryWrapper.eq(MchTradeOrderBatchMonth::getBatchDate, mchTradeOrderBatchMonth.getBatchDate());

            if(mchTradeOrderBatchMonth.getMchId() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getMchId, mchTradeOrderBatchMonth.getMchId());

            if(mchTradeOrderBatchMonth.getWxSumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getWxSumRealAmount, mchTradeOrderBatchMonth.getWxSumRealAmount());
            if(mchTradeOrderBatchMonth.getWxSumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getWxSumRefundAmount, mchTradeOrderBatchMonth.getWxSumRefundAmount());
            if(mchTradeOrderBatchMonth.getWxCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getWxCuntTrade, mchTradeOrderBatchMonth.getWxCuntTrade());
            if(mchTradeOrderBatchMonth.getWxRefundCunt() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getWxRefundCunt, mchTradeOrderBatchMonth.getWxRefundCunt());
            if(mchTradeOrderBatchMonth.getAliPaySumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getAliPaySumRealAmount, mchTradeOrderBatchMonth.getAliPaySumRealAmount());
            if(mchTradeOrderBatchMonth.getAliPaySumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getAliPaySumRefundAmount, mchTradeOrderBatchMonth.getAliPaySumRefundAmount());
            if(mchTradeOrderBatchMonth.getAliPayCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getAliPayCuntTrade, mchTradeOrderBatchMonth.getAliPayCuntTrade());
            if(mchTradeOrderBatchMonth.getAliPayRefundCount() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getAliPayRefundCount, mchTradeOrderBatchMonth.getAliPayRefundCount());
            if(mchTradeOrderBatchMonth.getBatchTaskStatus() != null) queryWrapper.eq(MchTradeOrderBatchMonth::getBatchTaskStatus, mchTradeOrderBatchMonth.getBatchTaskStatus());
        }
        return queryWrapper;
    }
}


