package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.MchSettRecord;
import org.xxpay.core.entity.MchSettRecordExample;

import java.util.List;

public interface MchSettRecordMapper extends BaseMapper<MchSettRecord> {
    int countByExample(MchSettRecordExample example);

    int deleteByExample(MchSettRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MchSettRecord record);

    int insertSelective(MchSettRecord record);

    List<MchSettRecord> selectByExample(MchSettRecordExample example);

    MchSettRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MchSettRecord record, @Param("example") MchSettRecordExample example);

    int updateByExample(@Param("record") MchSettRecord record, @Param("example") MchSettRecordExample example);

    int updateByPrimaryKeySelective(MchSettRecord record);

    int updateByPrimaryKey(MchSettRecord record);
}
