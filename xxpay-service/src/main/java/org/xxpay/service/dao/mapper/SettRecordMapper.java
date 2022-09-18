package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.SettRecord;
import org.xxpay.core.entity.SettRecordExample;

import java.util.List;
import java.util.Map;

public interface SettRecordMapper extends BaseMapper<SettRecord> {
    int countByExample(SettRecordExample example);

    int deleteByExample(SettRecordExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SettRecord record);

    int insertSelective(SettRecord record);

    List<SettRecord> selectByExample(SettRecordExample example);

    SettRecord selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SettRecord record, @Param("example") SettRecordExample example);

    int updateByExample(@Param("record") SettRecord record, @Param("example") SettRecordExample example);

    int updateByPrimaryKeySelective(SettRecord record);

    int updateByPrimaryKey(SettRecord record);

    /**
     * 统计结算情况
     * @param param
     * @return
     */
    Map count4Sett(Map param);

    /**
     * 统计结算金额总和
     * @param example
     * @return
     */
    long sumSettAmountByExample(SettRecordExample example);

    /**
     * 统计所有订单
     * @param param
     * @return
     */
    Map count4All(Map param);

    /**
     * 代付统计
     * @param param
     * @return
     */
    List<Map> settStatistics(Map param);

    List<Map> countSettStatistics(Map param);
}
