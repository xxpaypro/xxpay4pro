package org.xxpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.MchTradeOrderBatchHour;
import org.xxpay.core.service.IMchTradeOrderBatchHourService;
import org.xxpay.service.dao.mapper.MchTradeOrderBatchHourMapper;

import java.util.List;
import java.util.Map;

@Service
public class MchTradeOrderBatchHourServiceImpl extends ServiceImpl<MchTradeOrderBatchHourMapper, MchTradeOrderBatchHour> implements IMchTradeOrderBatchHourService {

    @Autowired
    private MchTradeOrderBatchHourMapper mchTradeOrderBatchHourMapper;

    @Override
    public void add(MchTradeOrderBatchHour mchTradeOrderBatchHour) {
        //持久化
        if(!this.save(mchTradeOrderBatchHour)){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    @Override
    public List<MchTradeOrderBatchHour> selectPayTrend(Map conditon) {
        return mchTradeOrderBatchHourMapper.selectPayTrend(conditon);
    }


    /** 生成[wrapper]查询条件 **/
    private LambdaQueryWrapper<MchTradeOrderBatchHour> getQueryWrapper(MchTradeOrderBatchHour mchTradeOrderBatchHour){

        LambdaQueryWrapper<MchTradeOrderBatchHour> queryWrapper = new LambdaQueryWrapper();

        if(mchTradeOrderBatchHour != null) {
            if(StringUtils.isNotEmpty(mchTradeOrderBatchHour.getBatchId())) queryWrapper.eq(MchTradeOrderBatchHour::getBatchId, mchTradeOrderBatchHour.getBatchId());
            if(StringUtils.isNotEmpty(mchTradeOrderBatchHour.getBatchDate())) queryWrapper.eq(MchTradeOrderBatchHour::getBatchDate, mchTradeOrderBatchHour.getBatchDate());
            if(StringUtils.isNotEmpty(mchTradeOrderBatchHour.getHour())) queryWrapper.eq(MchTradeOrderBatchHour::getHour, mchTradeOrderBatchHour.getHour());
            if(mchTradeOrderBatchHour.getMchId() != null) queryWrapper.eq(MchTradeOrderBatchHour::getMchId, mchTradeOrderBatchHour.getMchId());

            if(mchTradeOrderBatchHour.getWxSumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatchHour::getWxSumRealAmount, mchTradeOrderBatchHour.getWxSumRealAmount());
            if(mchTradeOrderBatchHour.getWxSumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatchHour::getWxSumRefundAmount, mchTradeOrderBatchHour.getWxSumRefundAmount());
            if(mchTradeOrderBatchHour.getWxCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatchHour::getWxCuntTrade, mchTradeOrderBatchHour.getWxCuntTrade());
            if(mchTradeOrderBatchHour.getWxRefundCunt() != null) queryWrapper.eq(MchTradeOrderBatchHour::getWxRefundCunt, mchTradeOrderBatchHour.getWxRefundCunt());
            if(mchTradeOrderBatchHour.getAliPaySumRealAmount() != null) queryWrapper.eq(MchTradeOrderBatchHour::getAliPaySumRealAmount, mchTradeOrderBatchHour.getAliPaySumRealAmount());
            if(mchTradeOrderBatchHour.getAliPaySumRefundAmount() != null) queryWrapper.eq(MchTradeOrderBatchHour::getAliPaySumRefundAmount, mchTradeOrderBatchHour.getAliPaySumRefundAmount());
            if(mchTradeOrderBatchHour.getAliPayCuntTrade() != null) queryWrapper.eq(MchTradeOrderBatchHour::getAliPayCuntTrade, mchTradeOrderBatchHour.getAliPayCuntTrade());
            if(mchTradeOrderBatchHour.getAliPayRefundCount() != null) queryWrapper.eq(MchTradeOrderBatchHour::getAliPayRefundCount, mchTradeOrderBatchHour.getAliPayRefundCount());
            if(mchTradeOrderBatchHour.getBatchTaskStatus() != null) queryWrapper.eq(MchTradeOrderBatchHour::getBatchTaskStatus, mchTradeOrderBatchHour.getBatchTaskStatus());
            if(mchTradeOrderBatchHour.getHospitalId() != null) queryWrapper.eq(MchTradeOrderBatchHour::getHospitalId, mchTradeOrderBatchHour.getHospitalId());
            if(mchTradeOrderBatchHour.getProvinceCode() != null) queryWrapper.eq(MchTradeOrderBatchHour::getProvinceCode, mchTradeOrderBatchHour.getProvinceCode());
            if(mchTradeOrderBatchHour.getCityCode() != null) queryWrapper.eq(MchTradeOrderBatchHour::getCityCode, mchTradeOrderBatchHour.getCityCode());
            if(mchTradeOrderBatchHour.getAreaCode() != null) queryWrapper.eq(MchTradeOrderBatchHour::getAreaCode, mchTradeOrderBatchHour.getAreaCode());
        }
        return queryWrapper;
    }
}
