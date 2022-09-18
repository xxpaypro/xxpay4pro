package org.xxpay.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.xxpay.core.entity.MchTradeOrderBatchMonth;

public interface IMchTradeOrderBatchMonthService extends IService<MchTradeOrderBatchMonth> {

    void add(MchTradeOrderBatchMonth mchTradeOrderBatchMonth);

    void updateMchTradeOrder(MchTradeOrderBatchMonth mchTradeOrderBatchMonth);

    MchTradeOrderBatchMonth findByBatchId(String batchId);

    MchTradeOrderBatchMonth getOneMchTradeOrderBatchMonth(String batchDate, Long mchId);

    IPage<MchTradeOrderBatchMonth> selectPage(IPage page, MchTradeOrderBatchMonth mchTradeOrderBatchMonth);

}

