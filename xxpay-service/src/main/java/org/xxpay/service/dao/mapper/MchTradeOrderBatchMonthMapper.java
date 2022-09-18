package org.xxpay.service.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xxpay.core.entity.MchTradeOrderBatchMonth;

public interface MchTradeOrderBatchMonthMapper extends BaseMapper<MchTradeOrderBatchMonth> {

    int deleteByPrimaryKey(String batchId);

    int insertSelective(MchTradeOrderBatchMonth record);

}
