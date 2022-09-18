package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchSettBatchRecord;
import org.xxpay.core.entity.MchSettBatchRecordExample;
import org.xxpay.core.service.IMchSettBatchRecordService;
import org.xxpay.service.dao.mapper.MchSettBatchRecordMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/7
 * @description:
 */
@Service
public class MchSettBatchRecordServiceImpl extends ServiceImpl<MchSettBatchRecordMapper, MchSettBatchRecord> implements IMchSettBatchRecordService {

    @Autowired
    private MchSettBatchRecordMapper mchSettBatchRecordMapper;

    private static final MyLog _log = MyLog.getLog(MchSettBatchRecordServiceImpl.class);

    @Override
    public List<MchSettBatchRecord> select(int offset, int limit, MchSettBatchRecord mchSettBatchRecord) {
        MchSettBatchRecordExample example = new MchSettBatchRecordExample();
        example.setOrderByClause("createTime asc");
        example.setOffset(offset);
        example.setLimit(limit);
        MchSettBatchRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchSettBatchRecord);
        return mchSettBatchRecordMapper.selectByExample(example);
    }

    @Override
    public int count(MchSettBatchRecord mchSettBatchRecord) {
        MchSettBatchRecordExample example = new MchSettBatchRecordExample();
        MchSettBatchRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, mchSettBatchRecord);
        return mchSettBatchRecordMapper.countByExample(example);
    }

    @Override
    public int insert(Long settRecordId, List<MchSettBatchRecord> mchSettBatchRecords) {
        // 判断记录是否为空
        if(CollectionUtils.isEmpty(mchSettBatchRecords)) {
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }

        // 先删除,避免之前已经有数据
        deleteBySettRecordId(settRecordId);

        // 批量插入收款账号,记录多需修改为数据库批量插入,提供性能
        for(MchSettBatchRecord mchSettBatchRecord : mchSettBatchRecords) {
            try {
                mchSettBatchRecordMapper.insertSelective(mchSettBatchRecord);
            }catch (Exception e) {
                // 异常时,则删除所有
                deleteBySettRecordId(settRecordId);
                // 退出
                return 0;
            }
        }
        return mchSettBatchRecords.size();
    }

    @Override
    public int deleteBySettRecordId(Long settRecordId) {
        MchSettBatchRecordExample example = new MchSettBatchRecordExample();
        MchSettBatchRecordExample.Criteria criteria = example.createCriteria();
        criteria.andSettRecordIdEqualTo(settRecordId);
        int delCount = mchSettBatchRecordMapper.deleteByExample(example);
        _log.info("删除批量结算记录,删除:{}条", delCount);
        return delCount;
    }

    void setCriteria(MchSettBatchRecordExample.Criteria criteria, MchSettBatchRecord mchSettBatchRecord) {
        if(mchSettBatchRecord != null) {
            if(mchSettBatchRecord.getMchId() != null) criteria.andMchIdEqualTo(mchSettBatchRecord.getMchId());
            if(mchSettBatchRecord.getSettRecordId() != null) criteria.andSettRecordIdEqualTo(mchSettBatchRecord.getSettRecordId());
        }
    }

}
