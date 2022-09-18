package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.AgentpayPassageExample;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AgentpayPassageMapper extends BaseMapper<AgentpayPassage> {
    int countByExample(AgentpayPassageExample example);

    int deleteByExample(AgentpayPassageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AgentpayPassage record);

    int insertSelective(AgentpayPassage record);

    List<AgentpayPassage> selectByExample(AgentpayPassageExample example);

    AgentpayPassage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AgentpayPassage record, @Param("example") AgentpayPassageExample example);

    int updateByExample(@Param("record") AgentpayPassage record, @Param("example") AgentpayPassageExample example);

    int updateByPrimaryKeySelective(AgentpayPassage record);

    int updateByPrimaryKey(AgentpayPassage record);
    
    Map<String, Long> checkPassageFee(@Param("agentPayPassageId")Integer agentPayPassageId, @Param("setFee")Long setFee);

    BigDecimal selectMaxFeeByExample(AgentpayPassageExample example);

}
