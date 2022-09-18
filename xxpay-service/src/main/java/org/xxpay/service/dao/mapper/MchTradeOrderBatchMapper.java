package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MchTradeOrderBatch;

import java.util.List;
import java.util.Map;


public interface MchTradeOrderBatchMapper extends BaseMapper<MchTradeOrderBatch> {

    int deleteByPrimaryKey(String batchId);

    int insertSelective(MchTradeOrderBatch record);

    List<MchTradeOrderBatch> selectDataTrend(Map condition);

    List<MchTradeOrderBatch> selectDataTrendForMonth(Map condition);

    //按日期查找跑批表
    MchTradeOrderBatch selectDataTrendByDay(Map condition);

    //按月份查找跑批表
    MchTradeOrderBatch selectDataTrendByMonth(Map condition);

    //交易分析--日报
    List<Map> selectDataTrendDailyPage(Map condition);

    //交易分析--月报
    List<Map> selectDataTrendMonthPage(Map conditon);

}
