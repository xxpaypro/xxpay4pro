package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.AgentpayPassageExample;
import org.xxpay.core.service.IAgentpayPassageService;
import org.xxpay.service.dao.mapper.AgentpayPassageMapper;

import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 2018/5/3
 * @description: 代付通道
 */
@Service
public class AgentpayPassageServiceImpl extends ServiceImpl<AgentpayPassageMapper, AgentpayPassage> implements IAgentpayPassageService {

    @Autowired
    private AgentpayPassageMapper agentpayPassageMapper;

    @Override
    public int add(AgentpayPassage agentpayPassage) {
        return agentpayPassageMapper.insertSelective(agentpayPassage);
    }

    @Override
    public int update(AgentpayPassage agentpayPassage) {
    	
    	if( agentpayPassage.getFeeEvery() != null){  ///判断是否修改了 费用
    		
    		AgentpayPassage record = agentpayPassageMapper.selectByPrimaryKey(agentpayPassage.getId());
    		if(record.getFeeEvery() == null || (agentpayPassage.getFeeEvery() > record.getFeeEvery()) ){  //费用变大，需校验是否合法
    			
    			Map<String, Long> result = agentpayPassageMapper.checkPassageFee(agentpayPassage.getId(), agentpayPassage.getFeeEvery());
    			if(result.get("mchCount") > 0)  throw new ServiceException(RetEnum.RET_MGR_SET_APAY_FEE_ERROR_MCH_SET);
    			if(result.get("agentCount") > 0) throw new ServiceException(RetEnum.RET_MGR_SET_APAY_FEE_ERROR_AGENT_SET);
    		}
    	}
    	
        return agentpayPassageMapper.updateByPrimaryKeySelective(agentpayPassage);
    }

    @Override
    public AgentpayPassage findById(Integer id) {
        return agentpayPassageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<AgentpayPassage> select(int offset, int limit, AgentpayPassage agentpayPassage) {
        AgentpayPassageExample example = new AgentpayPassageExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentpayPassageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayPassage);
        return agentpayPassageMapper.selectByExample(example);
    }

    @Override
    public Integer count(AgentpayPassage agentpayPassage) {
        AgentpayPassageExample example = new AgentpayPassageExample();
        AgentpayPassageExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayPassage);
        return agentpayPassageMapper.countByExample(example);
    }

    @Override
    public List<AgentpayPassage> selectAllPlatPassage(AgentpayPassage agentpayPassage) {
        AgentpayPassageExample example = new AgentpayPassageExample();
        example.setOrderByClause("createTime DESC");
        AgentpayPassageExample.Criteria criteria = example.createCriteria();
        agentpayPassage.setBelongInfoId(0L);
        agentpayPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        setCriteria(criteria, agentpayPassage);
        return agentpayPassageMapper.selectByExample(example);
    }

    void setCriteria(AgentpayPassageExample.Criteria criteria, AgentpayPassage obj) {
        if(obj != null) {
            if(obj.getIsDefault() != null) criteria.andIsDefaultEqualTo(obj.getIsDefault());
            if(obj.getRiskStatus() != null && obj.getRiskStatus().byteValue() != -99) criteria.andRiskStatusEqualTo(obj.getRiskStatus());
            if(obj.getStatus() != null && obj.getStatus().byteValue() != -99) criteria.andStatusEqualTo(obj.getStatus());
            if(obj.getBelongInfoId() != null) criteria.andBelongInfoIdEqualTo(obj.getBelongInfoId());
            if(obj.getBelongInfoType() != null) criteria.andBelongInfoTypeEqualTo(obj.getBelongInfoType());
        }
    }
}
