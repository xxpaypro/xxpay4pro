package org.xxpay.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AccountBalance;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.AgentpayRecordExample;
import org.xxpay.core.service.IAccountBalanceService;
import org.xxpay.core.service.IAgentpayService;
import org.xxpay.core.service.IBillService;
import org.xxpay.service.dao.mapper.AgentpayRecordMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 2018/4/21
 * @description:
 */
@Service
public class AgentpayServiceImpl implements IAgentpayService {

    @Autowired
    private AgentpayRecordMapper agentpayRecordMapper;

    @Autowired
    private IBillService mchBillService;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int applyAgentpay(AgentpayRecord agentpayRecord) {

        return applyAgentpayBatch(Arrays.asList(agentpayRecord));
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int applyAgentpayBatch(List<AgentpayRecord> agentpayRecordList) {

        Long infoId = agentpayRecordList.get(0).getInfoId();
        Byte infoType = agentpayRecordList.get(0).getInfoType();
        Byte mchType = agentpayRecordList.get(0).getMchType();

        //是否为私有账户账户
        boolean isPrivateMch = (infoType == MchConstant.INFO_TYPE_MCH && mchType == MchConstant.MCH_TYPE_PRIVATE);


        Long allChangeAmount = 0L; //所有资金变动

        for(AgentpayRecord agentpayRecord : agentpayRecordList) {

            if(agentpayRecord.getSubAmountFrom() == null ) {
                agentpayRecord.setSubAmountFrom(MchConstant.AGENTPAY_OUT_AGENTPAY_BALANCE); //默认从代付账户出资, 目前接口形式没有上送该字段
            }
            allChangeAmount += agentpayRecord.getSubAmount();
        }
        byte subAmountFrom = agentpayRecordList.get(0).getSubAmountFrom();


        Long availableAmount = 0L;
        AccountBalance balanceAccount = null;

        if(MchConstant.AGENTPAY_OUT_PAY_BALANCE == subAmountFrom){ ///收款账户余额出款

            balanceAccount = accountBalanceService.findOne(infoType, infoId, MchConstant.ACCOUNT_TYPE_BALANCE);

            boolean isSettBalance = isPrivateMch ? false : true; //私有账户查询amount字段， 其他查询sett 可结算金额字段。
            availableAmount = XXPayUtil.getAvailableAmount(balanceAccount, isSettBalance);
        }else{ //代付余额出款

            balanceAccount = accountBalanceService.findOne(infoType, infoId, MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE);
            availableAmount = XXPayUtil.getAvailableAmount(balanceAccount, false);
        }

        // 金额超限
        if(allChangeAmount.compareTo(availableAmount) > 0) throw ServiceException.build(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);

        //私有账户不冻结金额。
        if(!isPrivateMch){
            //添加冻结金额
            accountBalanceService.changeUnAmountOnly(balanceAccount, allChangeAmount);
        }

        // 插入代付记录
        int batchCount = 0;
        for(AgentpayRecord agentpayRecord : agentpayRecordList) {
            batchCount += agentpayRecordMapper.insertSelective(agentpayRecord);
        }
        return batchCount;
    }

    @Override
    public int updateStatus4Ing(String agentpayOrderId, String transOrderId) {
        AgentpayRecord agentpayRecord = new AgentpayRecord();
        if(StringUtils.isNotBlank(transOrderId)) agentpayRecord.setTransOrderId(transOrderId);
        agentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_ING);
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        criteria.andAgentpayOrderIdEqualTo(agentpayOrderId);
        criteria.andStatusEqualTo(PayConstant.AGENTPAY_STATUS_INIT);
        return agentpayRecordMapper.updateByExampleSelective(agentpayRecord, example);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateStatus4Success(String agentpayOrderId, String transOrderId, Integer agentpayPassageId) {
        AgentpayRecord updateAgentpayRecord = new AgentpayRecord();
        if(StringUtils.isNotBlank(transOrderId)) updateAgentpayRecord.setTransOrderId(transOrderId);
        updateAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_SUCCESS);
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        criteria.andAgentpayOrderIdEqualTo(agentpayOrderId);
        criteria.andStatusEqualTo(PayConstant.AGENTPAY_STATUS_ING);
        int result =  agentpayRecordMapper.updateByExampleSelective(updateAgentpayRecord, example);
        if(result == 1) {

            // 查询代付记录
            AgentpayRecord queryAgentpayRecord = new AgentpayRecord();
            queryAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
            AgentpayRecord agentpayRecord = find(queryAgentpayRecord);

        }
        return result;
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int updateStatus4Fail(String agentpayOrderId, Byte subAmountFrom, String transOrderId, String transMsg) {
        AgentpayRecord updateAgentpayRecord = new AgentpayRecord();
        if(StringUtils.isNotBlank(transOrderId)) updateAgentpayRecord.setTransOrderId(transOrderId);
        if(StringUtils.isNotBlank(transMsg)) updateAgentpayRecord.setTransMsg(transMsg);
        updateAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_FAIL);
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        criteria.andAgentpayOrderIdEqualTo(agentpayOrderId);
        criteria.andStatusEqualTo(PayConstant.AGENTPAY_STATUS_ING);
        // 更新状态
        int result = agentpayRecordMapper.updateByExampleSelective(updateAgentpayRecord, example);
        if(result == 1) {
            // 查询代付记录
            AgentpayRecord queryAgentpayRecord = new AgentpayRecord();
            queryAgentpayRecord.setAgentpayOrderId(agentpayOrderId);
            AgentpayRecord agentpayRecord = find(queryAgentpayRecord);

            //商户类型 && 私有账户
            if(agentpayRecord.getInfoType() == MchConstant.INFO_TYPE_MCH && agentpayRecord.getMchType() == MchConstant.MCH_TYPE_PRIVATE) {
                return result;
            }

            byte accountType = subAmountFrom == MchConstant.AGENTPAY_OUT_PAY_BALANCE ? MchConstant.ACCOUNT_TYPE_BALANCE : MchConstant.ACCOUNT_TYPE_AGPAY_BALANCE;
            accountBalanceService.changeUnAmountOnly(agentpayRecord.getInfoType(), agentpayRecord.getInfoId(), accountType, (0 - agentpayRecord.getAmount() - agentpayRecord.getFee()));


        }
        return result;
    }

    @Override
    public List<AgentpayRecord> select(int offset, int limit, AgentpayRecord agentpayRecord, JSONObject queryObj) {
        AgentpayRecordExample example = new AgentpayRecordExample();
        example.setOrderByClause("createTime desc");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayRecord, queryObj);
        return agentpayRecordMapper.selectByExample(example);
    }

    @Override
    public int updateTrans(String agentpayOrderId, String transOrderId, String transMsg) {
        AgentpayRecord updateAgentpayRecord = new AgentpayRecord();
        if(StringUtils.isNotBlank(transOrderId)) updateAgentpayRecord.setTransOrderId(transOrderId);
        if(StringUtils.isNotBlank(transMsg)) updateAgentpayRecord.setTransMsg(transMsg);
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        criteria.andAgentpayOrderIdEqualTo(agentpayOrderId);
        return agentpayRecordMapper.updateByExampleSelective(updateAgentpayRecord, example);
    }

    @Override
    public int count(AgentpayRecord agentpayRecord, JSONObject queryObj) {
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayRecord, queryObj);
        return agentpayRecordMapper.countByExample(example);
    }

    @Override
    public AgentpayRecord find(AgentpayRecord agentpayRecord) {
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayRecord);
        List<AgentpayRecord> agentpayRecordList = agentpayRecordMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(agentpayRecordList)) return null;
        return agentpayRecordList.get(0);
    }

    @Override
    public AgentpayRecord findByTransOrderId(String transOrderId) {
        AgentpayRecord agentpayRecord = new AgentpayRecord();
        agentpayRecord.setTransOrderId(transOrderId);
        return find(agentpayRecord);
    }

    @Override
    public AgentpayRecord findByAgentpayOrderId(String agentpayOrderId) {
        return agentpayRecordMapper.selectByPrimaryKey(agentpayOrderId);
    }

    @Override
    public AgentpayRecord findByMchIdAndAgentpayOrderId(Long mchId, String agentpayOrderId) {
        AgentpayRecord agentpayRecord = new AgentpayRecord();
        agentpayRecord.setInfoId(mchId);
        agentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        agentpayRecord.setAgentpayOrderId(agentpayOrderId);
        return find(agentpayRecord);
    }

    @Override
    public AgentpayRecord findByMchIdAndMchOrderNo(Long mchId, String mchOrderNo) {
        AgentpayRecord agentpayRecord = new AgentpayRecord();
        agentpayRecord.setInfoId(mchId);
        agentpayRecord.setInfoType(MchConstant.INFO_TYPE_MCH);
        agentpayRecord.setMchOrderNo(mchOrderNo);
        return find(agentpayRecord);
    }

    @Override
    public List<AgentpayRecord> select(int offset, int limit, List<Byte> statusList, AgentpayRecord agentpayRecord) {
        AgentpayRecordExample example = new AgentpayRecordExample();
        example.setOrderByClause("createTime desc");
        example.setOffset(offset);
        example.setLimit(limit);
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayRecord);
        criteria.andStatusIn(statusList);
        return agentpayRecordMapper.selectByExample(example);
    }

    @Override
    public int count(List<Byte> statusList, AgentpayRecord agentpayRecord) {
        AgentpayRecordExample example = new AgentpayRecordExample();
        AgentpayRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, agentpayRecord);
        criteria.andStatusIn(statusList);
        return agentpayRecordMapper.countByExample(example);
    }

    @Override
    public Map count4All(Long infoId, String accountName, String agentpayOrderId, String transOrderId, Byte status, Byte agentpayChannel, Byte infoType,Byte subAmountFrom, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(infoId != null) param.put("infoId", infoId);
        if(StringUtils.isNotBlank(agentpayOrderId)) param.put("agentpayOrderId", agentpayOrderId);
        if(StringUtils.isNotBlank(transOrderId)) param.put("transOrderId", transOrderId);
        if(StringUtils.isNotBlank(accountName)) param.put("accountName", accountName);
        if(status != null && status != -99) param.put("status", status);
        if(agentpayChannel != null && agentpayChannel != -99) param.put("agentpayChannel", agentpayChannel);
        if(infoType != null && infoType != -99) param.put("infoType", infoType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        if(subAmountFrom != null) param.put("subAmountFrom", subAmountFrom);
        return agentpayRecordMapper.count4All(param);
    }

    @Override
    public List<Map> agentpayStatistics(int offset, int limit, Long infoId, Byte infoType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        param.put("offset",offset);
        param.put("limit",limit);
        if (infoId != null) param.put("infoId",infoId);
        if(infoType != null && infoType != -99) param.put("infoType", infoType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return agentpayRecordMapper.agentpayStatistics(param);
    }

    @Override
    public List<Map> countAgentpayStatistics(Long infoId, Byte infoType, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        if (infoId != null) param.put("infoId",infoId);
        if(infoType != null && infoType != -99) param.put("infoType", infoType);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return agentpayRecordMapper.countAgentpayStatistics(param);
    }

    @Override
    public List<Map> agentpayCount(Long currentAgentId, Long searchInfoId, Byte searchInfoType, String accountName, String agentpayOrderId, Byte agentpayChannel, Byte status, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        param.put("currentAgentId", currentAgentId);
        if(searchInfoId != null) param.put("searchInfoId", searchInfoId);
        if(searchInfoType != null) param.put("searchInfoType", searchInfoType);
        if(StringUtils.isNotBlank(accountName)) param.put("accountName", accountName);
        if(StringUtils.isNotBlank(agentpayOrderId)) param.put("agentpayOrderId", agentpayOrderId);
        if(status != null && status != -99) param.put("status", status);
        if(agentpayChannel != null && agentpayChannel != -99) param.put("agentpayChannel", agentpayChannel);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return agentpayRecordMapper.agentpayCount(param);
    }

    @Override
    public List<Map> agentpaySelect(int offset, int limit, Long currentAgentId, Long searchInfoId, Byte searchInfoType, String accountName, String agentpayOrderId, Byte agentpayChannel, Byte status, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        param.put("offset", offset);
        param.put("limit", limit);

        param.put("currentAgentId", currentAgentId);
        if(searchInfoId != null) param.put("searchInfoId", searchInfoId);
        if(searchInfoType != null) param.put("searchInfoType", searchInfoType);
        if(StringUtils.isNotBlank(accountName)) param.put("accountName", accountName);
        if(StringUtils.isNotBlank(agentpayOrderId)) param.put("agentpayOrderId", agentpayOrderId);
        if(status != null && status != -99) param.put("status", status);
        if(agentpayChannel != null && agentpayChannel != -99) param.put("agentpayChannel", agentpayChannel);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return agentpayRecordMapper.agentpaySelect(param);
    }

    void setCriteria(AgentpayRecordExample.Criteria criteria, AgentpayRecord agentpayRecord) {
        setCriteria(criteria, agentpayRecord, null);
    }

    void setCriteria(AgentpayRecordExample.Criteria criteria, AgentpayRecord agentpayRecord, JSONObject queryObj) {
        if(agentpayRecord != null) {
            if(agentpayRecord.getInfoId() != null) criteria.andInfoIdEqualTo(agentpayRecord.getInfoId());
            if(agentpayRecord.getInfoType() != null) criteria.andInfoTypeEqualTo(agentpayRecord.getInfoType());
            if(StringUtils.isNotBlank(agentpayRecord.getTransOrderId())) criteria.andTransOrderIdEqualTo(agentpayRecord.getTransOrderId());
            if(StringUtils.isNotBlank(agentpayRecord.getAgentpayOrderId())) criteria.andAgentpayOrderIdEqualTo(agentpayRecord.getAgentpayOrderId());
            if(StringUtils.isNotBlank(agentpayRecord.getMchOrderNo())) criteria.andMchOrderNoEqualTo(agentpayRecord.getMchOrderNo());
            if(StringUtils.isNotBlank(agentpayRecord.getAccountName())) criteria.andAccountNameEqualTo(agentpayRecord.getAccountName());
            if(agentpayRecord.getStatus() != null && agentpayRecord.getStatus().byteValue() != -99) criteria.andStatusEqualTo(agentpayRecord.getStatus());
            if(agentpayRecord.getAgentpayChannel() != null && agentpayRecord.getAgentpayChannel().byteValue() != 99) criteria.andAgentpayChannelEqualTo(agentpayRecord.getAgentpayChannel());
            if(agentpayRecord.getSubAmountFrom() != null) criteria.andSubAmountFromEqualTo(agentpayRecord.getSubAmountFrom());
        }
        if(queryObj != null) {
            if(queryObj.getDate("createTimeStart") != null) criteria.andCreateTimeGreaterThanOrEqualTo(queryObj.getDate("createTimeStart"));
            if(queryObj.getDate("createTimeEnd") != null) criteria.andCreateTimeLessThanOrEqualTo(queryObj.getDate("createTimeEnd"));
        }
    }
    
    
    @Override
    public List<AgentpayRecord> selectAllBill(int offset, int limit, AgentpayRecord agentpayRecord) {

        agentpayRecord.setPsVal("offset", offset);
        agentpayRecord.setPsVal("limit", limit);
        return agentpayRecordMapper.selectAllBill(agentpayRecord);
    }
    
    

}
