package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.MchTradeOrderBatch;
import org.xxpay.core.service.IMchTradeOrderBatchService;
import org.xxpay.service.dao.mapper.MchTradeOrderBatchMapper;

import java.util.List;
import java.util.Map;

@Service
public class MchTradeOrderBatchServiceImpl extends ServiceImpl<MchTradeOrderBatchMapper, MchTradeOrderBatch> implements IMchTradeOrderBatchService {

    @Autowired
    private MchTradeOrderBatchMapper mchTradeOrderBatchMapper;
    @Override
    public void add(MchTradeOrderBatch mchTradeOrderBatch) {
        //持久化
        if(!this.save(mchTradeOrderBatch)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    @Override
    public void updateMchTradeOrder(MchTradeOrderBatch mchTradeOrderBatch) {

    }

    @Override
    public MchTradeOrderBatch findByBatchId(String batchId) {
        return null;
    }

    @Override
    public MchTradeOrderBatch getOneMchTradeOrderBatch(String batchDate, Long mchId) {
        MchTradeOrderBatch mchTradeOrderBatch = new MchTradeOrderBatch();
        mchTradeOrderBatch.setBatchDate(batchDate);
        mchTradeOrderBatch.setMchId(mchId);
        return getOne(getQueryWrapper(mchTradeOrderBatch));
    }

    @Override
    public IPage<MchTradeOrderBatch> selectPage(IPage page, MchTradeOrderBatch mchTradeOrderBatch) {
        LambdaQueryWrapper<MchTradeOrderBatch> queryWrapper = getQueryWrapper(mchTradeOrderBatch);
        queryWrapper.orderByDesc(MchTradeOrderBatch::getCreateTime);
        return page(page, queryWrapper);
    }

    @Override
    public List<Map> selectDataTrendDailyPage(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrendDailyPage(condition);
    }

    @Override
    public List<Map> selectDataTrendMonthPage(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrendMonthPage(condition);
    }

    @Override
    public List<MchTradeOrderBatch> selectDataTrend(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrend(condition);
    }

    @Override
    public List<MchTradeOrderBatch> selectDataTrendForMonth(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrendForMonth(condition);
    }

    @Override
    public MchTradeOrderBatch selectDataTrendByDay(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrendByDay(condition);
    }

    @Override
    public MchTradeOrderBatch selectDataTrendByMonth(Map condition) {
        return mchTradeOrderBatchMapper.selectDataTrendByMonth(condition);
    }




    /** 生成[wrapper]查询条件 **/
    private LambdaQueryWrapper<MchTradeOrderBatch> getQueryWrapper(MchTradeOrderBatch mchTradeOrderBatch){

        LambdaQueryWrapper<MchTradeOrderBatch> queryWrapper = new LambdaQueryWrapper();

        if(mchTradeOrderBatch != null) {
            if(StringUtils.isNotEmpty(mchTradeOrderBatch.getBatchId())) queryWrapper.eq(MchTradeOrderBatch::getBatchId, mchTradeOrderBatch.getBatchId());
            if(StringUtils.isNotEmpty(mchTradeOrderBatch.getBatchDate())) queryWrapper.eq(MchTradeOrderBatch::getBatchDate, mchTradeOrderBatch.getBatchDate());
            if(StringUtils.isNotEmpty(mchTradeOrderBatch.getBatchMonth())) queryWrapper.eq(MchTradeOrderBatch::getBatchMonth, mchTradeOrderBatch.getBatchMonth());
            if(mchTradeOrderBatch.getMchId() != null) queryWrapper.eq(MchTradeOrderBatch::getMchId, mchTradeOrderBatch.getMchId());

            if(mchTradeOrderBatch.getWxSumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatch::getWxSumRealAmount, mchTradeOrderBatch.getWxSumRealAmount());
            if(mchTradeOrderBatch.getWxSumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatch::getWxSumRefundAmount, mchTradeOrderBatch.getWxSumRefundAmount());
            if(mchTradeOrderBatch.getWxCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatch::getWxCuntTrade, mchTradeOrderBatch.getWxCuntTrade());
            if(mchTradeOrderBatch.getWxRefundCunt() != null) queryWrapper.eq(MchTradeOrderBatch::getWxRefundCunt, mchTradeOrderBatch.getWxRefundCunt());
            if(mchTradeOrderBatch.getAliPaySumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatch::getAliPaySumRealAmount, mchTradeOrderBatch.getAliPaySumRealAmount());
            if(mchTradeOrderBatch.getAliPaySumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatch::getAliPaySumRefundAmount, mchTradeOrderBatch.getAliPaySumRefundAmount());
            if(mchTradeOrderBatch.getAliPayCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatch::getAliPayCuntTrade, mchTradeOrderBatch.getAliPayCuntTrade());
            if(mchTradeOrderBatch.getAliPayRefundCount() != null) queryWrapper.eq(MchTradeOrderBatch::getAliPayRefundCount, mchTradeOrderBatch.getAliPayRefundCount());
            if(mchTradeOrderBatch.getBatchTaskStatus() != null) queryWrapper.eq(MchTradeOrderBatch::getBatchTaskStatus, mchTradeOrderBatch.getBatchTaskStatus());
            if(mchTradeOrderBatch.getHospitalId() != null) queryWrapper.eq(MchTradeOrderBatch::getHospitalId, mchTradeOrderBatch.getHospitalId());
            if(mchTradeOrderBatch.getProvinceCode() != null) queryWrapper.eq(MchTradeOrderBatch::getProvinceCode, mchTradeOrderBatch.getProvinceCode());
            if(mchTradeOrderBatch.getCityCode() != null) queryWrapper.eq(MchTradeOrderBatch::getCityCode, mchTradeOrderBatch.getCityCode());
            if(mchTradeOrderBatch.getAreaCode() != null) queryWrapper.eq(MchTradeOrderBatch::getAreaCode, mchTradeOrderBatch.getAreaCode());
        }
        return queryWrapper;
    }
}
