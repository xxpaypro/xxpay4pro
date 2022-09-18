package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.AccountHistory;
import org.xxpay.core.entity.AccountHistoryExample;
import org.xxpay.core.service.IAccountBalanceService;
import org.xxpay.core.service.IAccountHistoryService;
import org.xxpay.core.service.IOrderProfitDetailService;
import org.xxpay.service.dao.mapper.AccountHistoryMapper;
import org.xxpay.service.dao.mapper.OrderProfitDetailMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountHistoryServiceImpl extends ServiceImpl<AccountHistoryMapper, AccountHistory> implements IAccountHistoryService {

    public static final MyLog _log = MyLog.getLog(AccountHistoryServiceImpl.class);

    @Autowired
    private OrderProfitDetailMapper orderProfitDetailMapper;

    @Autowired
    private IOrderProfitDetailService orderProfitDetailService;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Autowired
    private AccountHistoryMapper accountHistoryMapper;

    @Override
    public AccountHistory findById(Long id) {
        return accountHistoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public AccountHistory findById(byte infoType, Long infoId, Long id) {
        AccountHistoryExample example = new AccountHistoryExample();
        AccountHistoryExample.Criteria criteria = example.createCriteria();
        criteria.andInfoTypeEqualTo(infoType);
        criteria.andInfoIdEqualTo(infoId);
        criteria.andIdEqualTo(id);
        List<AccountHistory> mchAccountHistoryList = accountHistoryMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(mchAccountHistoryList)) return mchAccountHistoryList.get(0);
        return null;
    }

    @Override
    public AccountHistory findByOrderId(byte infoType, String bizOrderId) {
        AccountHistoryExample example = new AccountHistoryExample();
        AccountHistoryExample.Criteria criteria = example.createCriteria();
        criteria.andBizOrderIdEqualTo(bizOrderId);
        criteria.andInfoTypeEqualTo(infoType);
        List<AccountHistory> mchAccountHistoryList = accountHistoryMapper.selectByExample(example);
        if(CollectionUtils.isNotEmpty(mchAccountHistoryList)) return mchAccountHistoryList.get(0);
        return null;
    }


    @Override
    public List<AccountHistory> select(int offset, int limit, AccountHistory agentAccountHistory) {
        AccountHistoryExample example = new AccountHistoryExample();
        example.setOrderByClause("createTime DESC");
        example.setOffset(offset);
        example.setLimit(limit);
        AccountHistoryExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentAccountHistory);
        return accountHistoryMapper.selectByExample(example);
    }

    @Override
    public int count(AccountHistory agentAccountHistory) {
        AccountHistoryExample example = new AccountHistoryExample();
        AccountHistoryExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentAccountHistory);
        return accountHistoryMapper.countByExample(example);
    }


    @Override
    public Map selectSettDailyCollect4Mch(Long mchId, String collDate, byte fundDirection, int riskDay) {
        Map param = new HashMap<>();
        param.put("mchId", mchId);
        param.put("collDate", collDate);
        param.put("fundDirection", fundDirection);
        param.put("riskDay", riskDay);
        return accountHistoryMapper.selectSettDailyCollect4Mch(param);
    }

    @Override
    public void updateCompleteSett4Mch(Long mchId, String collDate, int riskDay) {
        Map param = new HashMap<>();
        param.put("mchId", mchId);
        param.put("collDate", collDate);
        param.put("riskDay", riskDay);
        accountHistoryMapper.updateCompleteSett4Mch(param);
    }

    @Override
    public Map selectSettDailyCollect4Agent(Long agentId, String collDate, byte fundDirection, int riskDay, Byte bizType, String bizItem) {
        Map param = new HashMap<>();
        param.put("agentId", agentId);
        param.put("collDate", collDate);
        param.put("fundDirection", fundDirection);
        param.put("riskDay", riskDay);
        param.put("bizType", bizType);
        param.put("bizItem", bizItem);
        return accountHistoryMapper.selectSettDailyCollect4Agent(param);
    }

    @Override
    public void updateCompleteSett4Agent(Long agentId, String collDate, int riskDay, Byte bizType, String bizItem) {
        Map param = new HashMap<>();
        param.put("agentId", agentId);
        param.put("collDate", collDate);
        param.put("riskDay", riskDay);
        param.put("bizType", bizType);
        param.put("bizItem", bizItem);
        accountHistoryMapper.updateCompleteSett4Agent(param);
    }

    @Override
    public List<AccountHistory> selectNotSettCollect4Agent(Long agentId, String collDate) {
        Map param = new HashMap<>();
        param.put("agentId", agentId);
        param.put("collDate", collDate);
        return accountHistoryMapper.selectNotSettCollect4Agent(param);
    }





    @Override
    public List<Map> count4Data3() {
        return accountHistoryMapper.count4Data3();
    }

    @Override
    public Map count4Data(Byte bizType) {
        Map param = new HashMap<>();
        if(bizType != null) param.put("bizType", bizType);
        return accountHistoryMapper.count4Data(param);
    }

    @Override
    public Map count4Data2(Long mchId,Long agentId, String orderId, Byte bizType, String createTimeStart,
                           String createTimeEnd) {
        Map param = new HashMap<>();
        if(mchId != null) param.put("mchId", mchId);
        if(agentId != null) param.put("agentId", agentId);
        if(StringUtils.isNotBlank(orderId)) param.put("orderId", orderId);
        if(bizType != null) param.put("bizType", bizType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return accountHistoryMapper.count4Data2(param);
    }

    @Override
    public List<Map> count4AgentTop(Long agentId, String bizType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        if(StringUtils.isNotBlank(bizType)) param.put("bizType", bizType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return accountHistoryMapper.count4AgentProfitTop(param);
    }

    void setCriteria(AccountHistoryExample.Criteria criteria, AccountHistory mchAccountHistory) {
        if(mchAccountHistory != null) {
            if(mchAccountHistory.getId() != null) criteria.andIdEqualTo(mchAccountHistory.getId());
            if(mchAccountHistory.getInfoType() != null) criteria.andInfoTypeEqualTo(mchAccountHistory.getInfoType());
            if(mchAccountHistory.getInfoId() != null) criteria.andInfoIdEqualTo(mchAccountHistory.getInfoId());
            if(StringUtils.isNotBlank(mchAccountHistory.getBizOrderId())) criteria.andBizOrderIdEqualTo(mchAccountHistory.getBizOrderId());
            if(mchAccountHistory.getFundDirection() != null && !"-99".equals(mchAccountHistory.getFundDirection())) criteria.andFundDirectionEqualTo(mchAccountHistory.getFundDirection());
            if(mchAccountHistory.getBizType() != null && !"-99".equals(mchAccountHistory.getBizType())) criteria.andBizTypeEqualTo(mchAccountHistory.getBizType());

            if(StringUtils.isNotEmpty(mchAccountHistory.getPsStringVal("createTimeStart"))){
                criteria.andCreateTimeGreaterThanOrEqualTo(DateUtil.str2date(mchAccountHistory.getPsStringVal("createTimeStart")));
            }
            if(StringUtils.isNotEmpty(mchAccountHistory.getPsStringVal("createTimeEnd"))){
                criteria.andCreateTimeLessThanOrEqualTo(DateUtil.str2date(mchAccountHistory.getPsStringVal("createTimeEnd")));
            }
        }
    }


    ///以下为agent
    @Override
    public List<Map> count4AgentProfit(Long agentId) {
        Map param = new HashMap<>();
        if(agentId != null) param.put("agentId", agentId);
        return accountHistoryMapper.count4AgentProfit(param);
    }


}
