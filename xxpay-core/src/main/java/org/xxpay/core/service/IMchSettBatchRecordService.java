package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.MchSettBatchRecord;
import org.xxpay.core.entity.MchSettRecord;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
public interface IMchSettBatchRecordService extends IService<MchSettBatchRecord> {

    /**
     * 批量增加
     * @paramsettRecordId
     * @param mchSettBatchRecords
     * @return
     */
    int insert(Long settRecordId, List<MchSettBatchRecord> mchSettBatchRecords);

    /**
     * 查询批量结算记录
     * @param offset
     * @param limit
     * @param mchSettBatchRecord
     * @return
     */
    List<MchSettBatchRecord> select(int offset, int limit, MchSettBatchRecord mchSettBatchRecord);

    int count(MchSettBatchRecord mchSettBatchRecord);

    int deleteBySettRecordId(Long settRecordId);

}
