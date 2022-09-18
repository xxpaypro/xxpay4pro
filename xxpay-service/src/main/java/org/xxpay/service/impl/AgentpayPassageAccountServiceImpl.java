package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.IAgentpayPassageAccountService;
import org.xxpay.service.dao.mapper.AgentpayPassageAccountMapper;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/5/3
 * @description: 代付通道账户
 */
@Service
public class AgentpayPassageAccountServiceImpl extends ServiceImpl<AgentpayPassageAccountMapper, AgentpayPassageAccount> implements IAgentpayPassageAccountService {

    @Autowired
    private AgentpayPassageAccountMapper agentpayPassageAccountMapper;

    @Override
    public int add(AgentpayPassageAccount agentpayPassageAccount) {
        return agentpayPassageAccountMapper.insertSelective(agentpayPassageAccount);
    }

    @Override
    public int update(AgentpayPassageAccount agentpayPassageAccount) {
        return agentpayPassageAccountMapper.updateByPrimaryKeySelective(agentpayPassageAccount);
    }

    @Override
    public AgentpayPassageAccount findById(Integer id) {
        return agentpayPassageAccountMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AgentpayPassageAccount> select(int offset, int limit, AgentpayPassageAccount agentpayPassageAccount) {
        AgentpayPassageAccountExample example = new AgentpayPassageAccountExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentpayPassageAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayPassageAccount);
        return agentpayPassageAccountMapper.selectByExample(example);
    }

    @Override
    public Integer count(AgentpayPassageAccount agentpayPassageAccount) {
        AgentpayPassageAccountExample example = new AgentpayPassageAccountExample();
        AgentpayPassageAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayPassageAccount);
        return agentpayPassageAccountMapper.countByExample(example);
    }

    @Override
    public List<AgentpayPassageAccount> selectAll(AgentpayPassageAccount agentpayPassageAccount) {
        AgentpayPassageAccountExample example = new AgentpayPassageAccountExample();
        example.setOrderByClause("createTime DESC");
        AgentpayPassageAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayPassageAccount);
        return agentpayPassageAccountMapper.selectByExample(example);
    }

    @Override
    public List<AgentpayPassageAccount> selectAllByPassageId(Integer agentpayPassageId) {
        AgentpayPassageAccount agentpayPassageAccount = new AgentpayPassageAccount();
        agentpayPassageAccount.setAgentpayPassageId(agentpayPassageId);
        return selectAll(agentpayPassageAccount);
    }

    void setCriteria(AgentpayPassageAccountExample.Criteria criteria, AgentpayPassageAccount obj) {
        if(obj != null) {
            if(obj.getAgentpayPassageId() != null) criteria.andAgentpayPassageIdEqualTo(obj.getAgentpayPassageId());
            if(obj.getRiskStatus() != null && obj.getRiskStatus().byteValue() != -99) criteria.andRiskStatusEqualTo(obj.getRiskStatus());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
            if(obj.getPsListVal("ids") != null) criteria.andIdIn(obj.getPsListVal("ids"));
        }
    }

    public List<AgentpayPassageAccount> selectAccBaseInfo(AgentpayPassageAccount obj){

        AgentpayPassageAccountExample example = new AgentpayPassageAccountExample();
        AgentpayPassageAccountExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, obj);
        return agentpayPassageAccountMapper.selectAccBaseInfo(example);

    }
}
