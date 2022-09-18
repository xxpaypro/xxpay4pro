package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchTradeOrderBatch;

import java.util.List;
import java.util.Map;

public interface IMchTradeOrderBatchService extends IService<MchTradeOrderBatch> {

    /** 添加商户 **/
    void add(MchTradeOrderBatch mchTradeOrderBatch);

    List<MchTradeOrderBatch> selectDataTrend(Map condition);

    /** 月分析报表 **/
    List<MchTradeOrderBatch> selectDataTrendForMonth(Map condition);

    /**  按天统计数据 **/
    public MchTradeOrderBatch selectDataTrendByDay(Map condition);

    /** 按月份统计数据**/
    public MchTradeOrderBatch selectDataTrendByMonth(Map condition);

    /** 更新商户资料 **/
    void updateMchTradeOrder(MchTradeOrderBatch mchTradeOrderBatch);

    MchTradeOrderBatch findByBatchId(String batchId);

    MchTradeOrderBatch getOneMchTradeOrderBatch(String batchDate, Long mchId);

    IPage<MchTradeOrderBatch> selectPage(IPage page, MchTradeOrderBatch mchTradeOrderBatch);

    /** 交易分析---日报 **/
    List<Map> selectDataTrendDailyPage(Map condition);

    /**  交易分析---月报   **/
    List<Map> selectDataTrendMonthPage(Map condition);

}
