package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.CheckBatch;
import org.xxpay.core.entity.CheckBatchExample;

import java.util.List;

public interface CheckBatchMapper extends BaseMapper<CheckBatch> {
    int countByExample(CheckBatchExample example);

    int deleteByExample(CheckBatchExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CheckBatch record);

    int insertSelective(CheckBatch record);

    List<CheckBatch> selectByExample(CheckBatchExample example);

    CheckBatch selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CheckBatch record, @Param("example") CheckBatchExample example);

    int updateByExample(@Param("record") CheckBatch record, @Param("example") CheckBatchExample example);

    int updateByPrimaryKeySelective(CheckBatch record);

    int updateByPrimaryKey(CheckBatch record);
}
