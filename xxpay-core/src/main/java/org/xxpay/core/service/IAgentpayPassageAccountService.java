package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.AgentpayPassageAccount;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/5/3
 * @description: 代付通道账户
 */
public interface IAgentpayPassageAccountService extends IService<AgentpayPassageAccount> {

    int add(AgentpayPassageAccount agentpayPassageAccount);

    int update(AgentpayPassageAccount agentpayPassageAccount);

    AgentpayPassageAccount findById(Integer id);

    List<AgentpayPassageAccount> select(int offset, int limit, AgentpayPassageAccount agentpayPassageAccount);

    Integer count(AgentpayPassageAccount agentpayPassageAccount);

    List<AgentpayPassageAccount> selectAll(AgentpayPassageAccount agentpayPassageAccount);

    /**
     * 根据代付通道ID,查询所有代付通道账户列表
     * @param agentpayPassageId
     * @return
     */
    List<AgentpayPassageAccount> selectAllByPassageId(Integer agentpayPassageId);

    List<AgentpayPassageAccount> selectAccBaseInfo(AgentpayPassageAccount agentpayPassageAccount);

}
