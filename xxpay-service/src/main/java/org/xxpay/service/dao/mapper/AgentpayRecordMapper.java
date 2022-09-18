package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.AgentpayRecordExample;

import java.util.List;
import java.util.Map;

public interface AgentpayRecordMapper extends BaseMapper<AgentpayRecord> {
    int countByExample(AgentpayRecordExample example);

    int deleteByExample(AgentpayRecordExample example);

    int deleteByPrimaryKey(String agentpayOrderId);

    int insert(AgentpayRecord record);

    int insertSelective(AgentpayRecord record);

    List<AgentpayRecord> selectByExample(AgentpayRecordExample example);

    AgentpayRecord selectByPrimaryKey(String agentpayOrderId);

    int updateByExampleSelective(@Param("record") AgentpayRecord record, @Param("example") AgentpayRecordExample example);

    int updateByExample(@Param("record") AgentpayRecord record, @Param("example") AgentpayRecordExample example);

    int updateByPrimaryKeySelective(AgentpayRecord record);

    int updateByPrimaryKey(AgentpayRecord record);

    /**
     * 统计所有订单
     * @param param
     * @return
     */
    Map count4All(Map param);

    List<Map> agentpayStatistics(Map param);

    List<Map> countAgentpayStatistics(Map param);

    List<Map> agentpaySelect(Map param);

    List<Map> agentpayCount(Map param);

    public List<AgentpayRecord> selectAllBill(AgentpayRecord agentpayRecord) ;
}
