package org.xxpay.service.dao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.AgentInfoExample;

public interface AgentInfoMapper extends BaseMapper<AgentInfo> {
    long countByExample(AgentInfoExample example);

    int deleteByExample(AgentInfoExample example);

    int deleteByPrimaryKey(Long agentId);

    int insert(AgentInfo record);

    int insertSelective(AgentInfo record);

    List<AgentInfo> selectByExample(AgentInfoExample example);

    AgentInfo selectByPrimaryKey(Long agentId);

    int updateByExampleSelective(@Param("record") AgentInfo record, @Param("example") AgentInfoExample example);

    int updateByExample(@Param("record") AgentInfo record, @Param("example") AgentInfoExample example);

    int updateByPrimaryKeySelective(AgentInfo record);

    int updateByPrimaryKey(AgentInfo record);

    /**
     * 统计代理商信息
     * @param param
     * @return
     */
    Map count4Agent(Map param);

    /***
     *  查询下级代理商中 的最小线下充值费率
     * @param currentAgentId
     * @return
     */
    BigDecimal selectChildrenAgentsMinOffRechargeRate(Long currentAgentId);

    /** 查询所有代理商的返佣比例， 按照等级降序  index=0 为远离商户，远离服务商的等级最高代理商  **/
    List<AgentInfo> selectAgentsProfitRate(Long agentId);

    /** 查询所有子代理商ID （包含当前代理商） **/
    List<Long> queryAllSubAgentIds(@Param("currentAgentId") Long currentAgentId);

    /** 查询所有子代理商信息集合 （包含当前代理商） **/
    List<AgentInfo> queryAllSubAgentBaseInfo(@Param("currentAgentId") Long currentAgentId);

    /** 查询下级最高分佣比例 */
    BigDecimal selectSubAgentsMaxProfitRate(Long agentId);

}
