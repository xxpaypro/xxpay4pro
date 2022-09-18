package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchSettBatchRecord;
import org.xxpay.core.entity.MchSettBatchRecordExample;

import java.util.List;

public interface MchSettBatchRecordMapper extends BaseMapper<MchSettBatchRecord> {
    int countByExample(MchSettBatchRecordExample example);

    int deleteByExample(MchSettBatchRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MchSettBatchRecord record);

    int insertSelective(MchSettBatchRecord record);

    List<MchSettBatchRecord> selectByExample(MchSettBatchRecordExample example);

    MchSettBatchRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MchSettBatchRecord record, @Param("example") MchSettBatchRecordExample example);

    int updateByExample(@Param("record") MchSettBatchRecord record, @Param("example") MchSettBatchRecordExample example);

    int updateByPrimaryKeySelective(MchSettBatchRecord record);

    int updateByPrimaryKey(MchSettBatchRecord record);
}
