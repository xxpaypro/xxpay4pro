package org.xxpay.core.service;
import com.baomidou.mybatisplus.extension.service.IService;

import org.xxpay.core.entity.AgentpayPassage;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/5/3
 * @description: 代付通道
 */
public interface IAgentpayPassageService extends IService<AgentpayPassage> {

    int add(AgentpayPassage agentpayPassage);

    int update(AgentpayPassage agentpayPassage);

    AgentpayPassage findById(Integer id);

    List<AgentpayPassage> select(int offset, int limit, AgentpayPassage agentpayPassage);

    Integer count(AgentpayPassage agentpayPassage);

    List<AgentpayPassage> selectAllPlatPassage(AgentpayPassage agentpayPassage);

}
