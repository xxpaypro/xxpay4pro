package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.SysUser;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/04/27
 * @description: 代理商接口
 */
public interface IAgentInfoService extends IService<AgentInfo> {

    int add(AgentInfo agentInfo);

    int updateAgent(AgentInfo agentInfo, SysUser agentUser);

    AgentInfo find(AgentInfo agentInfo);

    AgentInfo findByLoginName(String loginName);

    AgentInfo findByAgentId(Long agentId);

    AgentInfo findByUserName(String userName);

    AgentInfo findByMobile(Long mobile);

    AgentInfo findByEmail(String email);

    List<AgentInfo> select(int offset, int limit, AgentInfo agentInfo);

    Long count(AgentInfo agentInfo);

    Map count4Agent();

    /** 查询所有代理商的返佣比例， 按照等级降序  index=0 为远离商户，远离服务商的等级最高代理商  **/
    List<AgentInfo> selectAgentsProfitRate(Long agentId);

    /** 查询代理商数量：  根据代理商等级 **/
    Long countByAgentLevel(Date createTimeStart, Date createTimeEnd, Integer agentLevel, Long isvId, Long agentPid);

    /** 查询所有子代理商ID （包含当前代理商） **/
    List<Long> queryAllSubAgentIds(Long currentAgentId);

    /** 查询所有子代理商信息集合 （包含当前代理商） **/
    List<AgentInfo> queryAllSubAgentBaseInfo(Long currentAgentId);

    /** 统计代理商数量 **/
    Long countByAgentIdAndTime(Date createTimeStart, Date createTimeEnd, List<Long> agentIdList);

    /** 查询下级最高分佣比例 */
    BigDecimal selectSubAgentsMaxProfitRate(Long agentId);

}
