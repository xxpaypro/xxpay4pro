package org.xxpay.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.core.service.*;
import org.xxpay.service.dao.mapper.SettRecordMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 2018/5/19
 * @description:
 */
@Service
public class SettRecordServiceImpl extends ServiceImpl<SettRecordMapper, SettRecord> implements ISettRecordService {

    @Autowired
    private SettRecordMapper settRecordMapper;

    @Autowired
    private IMchInfoService mchInfoService;

    @Autowired
    private IAgentInfoService agentInfoService;

    @Autowired
    private ISettBankAccountService settBankAccountService;

    @Autowired
    private IAccountBalanceService accountBalanceService;

    @Override
    public List<SettRecord> select(int offset, int limit, SettRecord settRecord, JSONObject queryObj) {
        SettRecordExample example = new SettRecordExample();
        example.setOrderByClause("createTime desc");
        example.setOffset(offset);
        example.setLimit(limit);
        SettRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, settRecord, queryObj);
        return settRecordMapper.selectByExample(example);
    }

    @Override
    public int count(SettRecord settRecord, JSONObject queryObj) {
        SettRecordExample example = new SettRecordExample();
        SettRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, settRecord, queryObj);
        return settRecordMapper.countByExample(example);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int auditSett(Long id, Byte status, String remark) {
        SettRecord settRecord = settRecordMapper.selectByPrimaryKey(id);
        if(settRecord == null) {
            throw ServiceException.build(RetEnum.RET_SERVICE_SETT_RECORD_NOT_EXIST);
        }
        // 状态必须为审核中
        if(!settRecord.getSettStatus().equals(MchConstant.SETT_STATUS_AUDIT_ING)) {
            throw ServiceException.build(RetEnum.RET_SERVICE_SETT_STATUS_ERROR);
        }
        SettRecordExample example = new SettRecordExample();
        SettRecordExample.Criteria criteria = example.createCriteria();
        SettRecord updateSettRecord = new SettRecord();
        updateSettRecord.setSettStatus(status);
        updateSettRecord.setRemark(remark);
        criteria.andIdEqualTo(id);
        int updateCount = settRecordMapper.updateByExampleSelective(updateSettRecord, example);
        // 审核不通过,需要解冻金额(申请金额+手续费)
        if(updateCount == 1 && status == MchConstant.SETT_STATUS_AUDIT_NOT) {

            accountBalanceService.changeUnAmountOnly(settRecord.getInfoType(), settRecord.getInfoId(),
                        MchConstant.ACCOUNT_TYPE_BALANCE, (0-settRecord.getSettAmount()-settRecord.getSettFee()));
        }
        return updateCount;
    }

    @Override
    public SettRecord find(SettRecord settRecord) {
        SettRecordExample example = new SettRecordExample();
        example.setOrderByClause("createTime desc");
        SettRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, settRecord);
        List<SettRecord> settRecordList = settRecordMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(settRecordList)) return null;
        return settRecordList.get(0);
    }

    @Override
    public SettRecord findById(Long id) {
        return settRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public SettRecord findByTransOrderId(String transOrderId) {
        SettRecord settRecord = new SettRecord();
        settRecord.setTransOrderId(transOrderId);
        return find(settRecord);
    }

    @Override
    public SettRecord findBySettOrderId(String settOrderId) {
        SettRecord settRecord = new SettRecord();
        settRecord.setSettOrderId(settOrderId);
        return find(settRecord);
    }

    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = Exception.class)
    public int remit(Long id, Byte status, String remark, String remitRemark, String transOrderId, String transMsg) {
        SettRecord settRecord = settRecordMapper.selectByPrimaryKey(id);
        if(settRecord == null) {
            throw ServiceException.build(RetEnum.RET_SERVICE_SETT_RECORD_NOT_EXIST);
        }
        // 状态必须为审核成功|打款中
        if(!settRecord.getSettStatus().equals(MchConstant.SETT_STATUS_AUDIT_OK) &&
                !settRecord.getSettStatus().equals(MchConstant.SETT_STATUS_REMIT_ING)) {
            throw ServiceException.build(RetEnum.RET_SERVICE_SETT_STATUS_ERROR);
        }

        SettRecordExample example = new SettRecordExample();
        SettRecordExample.Criteria criteria = example.createCriteria();
        SettRecord updateSettRecord = new SettRecord();
        updateSettRecord.setSettStatus(status);
        updateSettRecord.setRemark(remark);
        updateSettRecord.setRemitRemark(remitRemark);
        updateSettRecord.setTransOrderId(transOrderId);
        updateSettRecord.setTransMsg(transMsg);
        criteria.andIdEqualTo(id);
        int updateCount = settRecordMapper.updateByExampleSelective(updateSettRecord, example);
        if(updateCount == 1) {

            if(status == MchConstant.SETT_STATUS_REMIT_FAIL) {

                accountBalanceService.changeUnAmountOnly(settRecord.getInfoType(), settRecord.getInfoId(),
                                    MchConstant.ACCOUNT_TYPE_BALANCE, (0-settRecord.getSettAmount() - settRecord.getSettFee()));

            }else if(status == MchConstant.SETT_STATUS_REMIT_SUCCESS) {

                accountBalanceService.changeAmountAndInsertHistory(settRecord.getInfoType(), settRecord.getInfoId(), MchConstant.BIZ_TYPE_REMIT, MchConstant.BIZ_ITEM_BALANCE, ( 0-settRecord.getSettAmount() - settRecord.getSettFee() ),
                        "平台打款成功", settRecord.getSettOrderId(), settRecord.getTransOrderId(), settRecord.getSettAmount(), settRecord.getSettFee());
            }
        }
        return updateCount;
    }

    @Override
    public int updateTrans(Long id, String transOrderId, String transMsg) {
        SettRecordExample example = new SettRecordExample();
        SettRecordExample.Criteria criteria = example.createCriteria();
        SettRecord updateSettRecord = new SettRecord();
        updateSettRecord.setTransOrderId(transOrderId);
        updateSettRecord.setTransMsg(transMsg);
        criteria.andIdEqualTo(id);
        return settRecordMapper.updateByExampleSelective(updateSettRecord, example);
    }

    @Override
    public List<SettRecord> select(int offset, int limit, List<Byte> settStatusList, SettRecord settRecord, JSONObject queryObj) {
        SettRecordExample example = new SettRecordExample();
        example.setOrderByClause("createTime desc");
        example.setOffset(offset);
        example.setLimit(limit);
        SettRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, settRecord, queryObj);
        criteria.andSettStatusIn(settStatusList);
        return settRecordMapper.selectByExample(example);
    }

    @Override
    public int count(List<Byte> settStatusList, SettRecord settRecord, JSONObject queryObj) {
        SettRecordExample example = new SettRecordExample();
        SettRecordExample.Criteria criteria = example.createCriteria();
        setCriteria(criteria, settRecord, queryObj);
        criteria.andSettStatusIn(settStatusList);
        return settRecordMapper.countByExample(example);
    }

    @Override
    public Map count4Sett(String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return settRecordMapper.count4Sett(param);
    }

    @Override
    public int applySett(Byte infoType, Long infoId, List<Map<String,Long>> settBatchList) {

        //获取结算账户信息
        AccountBalance balanceAccount = accountBalanceService.findOne(infoType, infoId, MchConstant.ACCOUNT_TYPE_BALANCE);

        //定义结算配置 (待后期将mchInfo agentInfo 整合， 再将此代码重构)
        Byte settConfigFeeType = null;
        BigDecimal settConfigFeeRate = null;
        String settConfigFeeLevel = null;
        Long settConfigDrawFeeLimit = null;
        Byte settConfigDrawFlag = null;
        String settConfigAllowDrawWeekDay = null;
        String settConfigDrawDayStartTime = null;
        String settConfigDrawDayEndTime = null;
        Integer settConfigDayDrawTimes = null;
        Long settConfigDrawMaxDayAmount = null;
        Long settConfigMaxDrawAmount = null;
        Long settConfigMinDrawAmount = null;

        AgentInfo info = agentInfoService.findByAgentId(infoId);
        if(info == null)  throw ServiceException.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST);  // 账户不存在
        if(info.getStatus() != MchConstant.PUB_YES) throw ServiceException.build(RetEnum.RET_AGENT_STATUS_STOP);  // 停止使用


        //累加所有扣减金额
        Long allDebitAmount = 0L;
        for(Map<String, Long> settMap : settBatchList){
        	
        	Long settAmount = settMap.get("settAmount");
            long settFee = XXPayUtil.calculationFee(settAmount, settConfigFeeType, settConfigFeeRate, settConfigFeeLevel, settConfigDrawFeeLimit );    // 手续费
            Long debitAmount = settAmount + settFee;// 扣减商户金额= 结算金额 + 手续费
            allDebitAmount += debitAmount;
        }
        
        if(allDebitAmount.compareTo(balanceAccount.getSettAmount() - balanceAccount.getUnAmount()) > 0) {
            throw new ServiceException(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT);  // 金额超限
        }

        int insertCount = 0;
        for(Map<String, Long> settMap : settBatchList){
        	
        	Long accountId = settMap.get("accountId");
        	Long settAmount = settMap.get("settAmount");
        	
        	//判断结算账户是否存在
            SettBankAccount bankAccount = settBankAccountService.findById(accountId);
            if(bankAccount == null){
            	 throw ServiceException.build(RetEnum.RET_SERVICE_BANK_ACCOUNT_NOT_EXIST);
            }
        	
        	// 判断金额是否够结算
            long settFee = XXPayUtil.calculationFee(settAmount, settConfigFeeType, settConfigFeeRate, settConfigFeeLevel, settConfigDrawFeeLimit);    // 手续费
            Long debitAmount = settAmount + settFee;// 扣减商户金额=结算金额 + 手续费

            // 重新查询商户最新账户信息
            balanceAccount = accountBalanceService.findOne(infoType, infoId, MchConstant.ACCOUNT_TYPE_BALANCE);
            if(debitAmount.compareTo((balanceAccount.getSettAmount() - balanceAccount.getUnAmount())) > 0) {
                throw ServiceException.build(RetEnum.RET_SERVICE_SETT_AMOUNT_NOT_SETT); // 金额超限
            }

            // 判断是否允许提现
            isAllowDraw(infoId, settAmount, settConfigDrawFlag, settConfigAllowDrawWeekDay, settConfigDrawDayStartTime, settConfigDrawDayEndTime,
                    settConfigDayDrawTimes, settConfigDrawMaxDayAmount, settConfigMaxDrawAmount, settConfigMinDrawAmount);

            // 冻结资金操作
            accountBalanceService.changeUnAmountOnly(infoType, infoId,  MchConstant.ACCOUNT_TYPE_BALANCE, debitAmount);

            SettRecord settRecord = new SettRecord();
            settRecord.setSettOrderId(MySeq.getSett());
            settRecord.setInfoId(infoId);
            settRecord.setInfoType(infoType);
            settRecord.setSettType((byte) 1);   // 手工结算
            settRecord.setSettDate(new Date()); // 结算日期
            settRecord.setSettAmount(settAmount);   // 申请结算金额
            settRecord.setSettFee(settFee);             // 结算手续费
            settRecord.setRemitAmount(settAmount);      // 打款金额 = 结算金额
            settRecord.setAccountAttr(bankAccount.getAccountAttr());    // 对私
            settRecord.setAccountType(bankAccount.getAccountType());    // 银行卡转账
            settRecord.setBankName(bankAccount.getBankName());
            settRecord.setBankNetName(bankAccount.getBankNetName());
            settRecord.setAccountName(bankAccount.getAccountName());
            settRecord.setAccountNo(bankAccount.getAccountNo());
            settRecord.setProvince(bankAccount.getProvince());
            settRecord.setCity(bankAccount.getCity());
            
            settRecord.setSettStatus(MchConstant.SETT_STATUS_AUDIT_ING);    // 结算状态
            settRecord.setRemark("");
            settRecord.setRemitRemark("");
            insertCount += settRecordMapper.insertSelective(settRecord);
        	
        }

        return insertCount;

    }

    @Override
    public Map count4All(Long infoId, String accountName, String settOrderId, Byte settStatus, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap<>();
        if(infoId != null) param.put("infoId", infoId);
        if(StringUtils.isNotBlank(settOrderId)) param.put("settOrderId", settOrderId);
        if(StringUtils.isNotBlank(accountName)) param.put("accountName", accountName);
        if(settStatus != null && settStatus != -99) param.put("settStatus", settStatus);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return settRecordMapper.count4All(param);
    }

    @Override
    public List<Map> settStatistics(int offset, int limit, Long mchId, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        param.put("offset",offset);
        param.put("limit",limit);
        if (mchId != null) param.put("mchId",mchId);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return settRecordMapper.settStatistics(param);
    }

    @Override
    public List<Map> countSettStatistics(Long mchId, String createTimeStart, String createTimeEnd) {
        Map param = new HashMap();
        if (mchId != null) param.put("mchId",mchId);
        if(StringUtils.isNotBlank(createTimeStart)) param.put("createTimeStart", createTimeStart);
        if(StringUtils.isNotBlank(createTimeEnd)) param.put("createTimeEnd", createTimeEnd);
        return settRecordMapper.countSettStatistics(param);
    }

    void setCriteria(SettRecordExample.Criteria criteria, SettRecord settRecord) {
        setCriteria(criteria, settRecord, null);
    }

    void setCriteria(SettRecordExample.Criteria criteria, SettRecord settRecord, JSONObject queryObj) {
        if(settRecord != null) {
            if(settRecord.getInfoId() != null) criteria.andInfoIdEqualTo(settRecord.getInfoId());
            if(StringUtils.isNotBlank(settRecord.getSettOrderId())) criteria.andSettOrderIdEqualTo(settRecord.getSettOrderId());
            if(StringUtils.isNotBlank(settRecord.getTransOrderId())) criteria.andTransOrderIdEqualTo(settRecord.getTransOrderId());
            if(StringUtils.isNotBlank(settRecord.getAccountName())) criteria.andAccountNameEqualTo(settRecord.getAccountName());
            if(settRecord.getId() != null) criteria.andIdEqualTo(settRecord.getId());
            if(settRecord.getSettStatus() != null && settRecord.getSettStatus().byteValue() != -99) criteria.andSettStatusEqualTo(settRecord.getSettStatus());
            if(settRecord.getInfoType() != null && settRecord.getInfoType().byteValue() != -99) criteria.andInfoTypeEqualTo(settRecord.getInfoType());
        }
        if(queryObj != null) {
            if(queryObj.getDate("createTimeStart") != null) criteria.andCreateTimeGreaterThanOrEqualTo(queryObj.getDate("createTimeStart"));
            if(queryObj.getDate("createTimeEnd") != null) criteria.andCreateTimeLessThanOrEqualTo(queryObj.getDate("createTimeEnd"));
        }
    }

    public String isAllowApply(Byte drawFlag, String allowDrawWeekDay, String drawStartTime, String drawEndTime) {
        // 判断当前提现开关是否开启
        if(drawFlag != 1) {
            return "系统提现关闭,暂时不允许提现";
        }
        // 判断周几可以提现,1,2,3格式存储,7表示周日
        if(StringUtils.isNotBlank(allowDrawWeekDay)) {
            boolean allowDrawWeekFlag = false;
            String currentWeek = String.valueOf(DateUtil.getCurrentWeek());
            String[] weeks = allowDrawWeekDay.split(",");
            for(String week : weeks) {
                if(week.equals(currentWeek)) {
                    allowDrawWeekFlag = true;
                    break;
                }
            }
            // 当天不允许提现
            if(!allowDrawWeekFlag) {
                return "今天不允许提现,系统开放提现为星期:" + allowDrawWeekDay;
            }
        }
        // 提现时间是否满足
        String ymd = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD);
        String drawDayStartTime = ymd + " " + drawStartTime;
        String drawDayEndTime = ymd + " " + drawEndTime;
        long startTime = DateUtil.str2date(drawDayStartTime).getTime();
        long endTime = DateUtil.str2date(drawDayEndTime).getTime();
        long currentTime = System.currentTimeMillis();
        if(currentTime < startTime || currentTime > endTime) {
            return "当前时间不允许提现,允许提现时间:" + drawStartTime + " - " + drawEndTime;
        }
        return "";
    }

    void isAllowDraw(Long infoId, Long settAmount, Byte drawFlag, String allowDrawWeekDay, String drawDayStartTime, String drawDayEndTime,
                     Integer dayDrawTimes, Long drawMaxDayAmount, Long maxDrawAmount, Long minDrawAmount) {

        // 1. 判断当前提现开关是否开启
        if(drawFlag != 1) {
            throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_CLOSE);
        }
        // 2. 判断单笔金额是否允许
        if(minDrawAmount != null && minDrawAmount >= 0 &&  settAmount < minDrawAmount) {
            throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_AMOUNT_MIN_LIMIT);
        }
        if(maxDrawAmount != null && maxDrawAmount >= 0 && settAmount > maxDrawAmount) {
            throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_AMOUNT_MAX_LIMIT);
        }
        // 3. 判断周几可以提现,1,2,3格式存储,7表示周日
        if(StringUtils.isNotBlank(allowDrawWeekDay)) {
            boolean allowDrawWeekFlag = false;
            String currentWeek = String.valueOf(DateUtil.getCurrentWeek());
            String[] weeks = allowDrawWeekDay.split(",");
            for(String week : weeks) {
                if(week.equals(currentWeek)) {
                    allowDrawWeekFlag = true;
                    break;
                }
            }
            // 当天不允许提现
            if(!allowDrawWeekFlag) {
                throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_DAY_NOT);
            }
        }
        // 4. 提现时间是否满足
        String ymd = DateUtil.date2Str(new Date(), DateUtil.FORMAT_YYYY_MM_DD);
        drawDayStartTime = ymd + " " + drawDayStartTime;
        drawDayEndTime = ymd + " " + drawDayEndTime;
        long startTime = DateUtil.str2date(drawDayStartTime).getTime();
        long endTime = DateUtil.str2date(drawDayEndTime).getTime();
        long currentTime = System.currentTimeMillis();
        if(currentTime < startTime || currentTime > endTime) {
            throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_TIME_LIMIT);
        }
        // 5. 是否超过每日提现次数
        if(dayDrawTimes != null && dayDrawTimes >= 0) {
            // 查询当天提现次数
            SettRecordExample example = new SettRecordExample();
            SettRecordExample.Criteria criteria = example.createCriteria();
            criteria.andInfoIdEqualTo(infoId);
            criteria.andSettDateEqualTo(new Date());
            int infoDrawTimes = settRecordMapper.countByExample(example);
            if(infoDrawTimes + 1 > dayDrawTimes) {
                throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_TIMES_LIMIT);
            }
        }
        // 6. 判断是否超过当日可提现总额
        if(drawMaxDayAmount != null && drawMaxDayAmount > 0) {
            // 查询当天提现金额
            SettRecordExample example = new SettRecordExample();
            SettRecordExample.Criteria criteria = example.createCriteria();
            criteria.andInfoIdEqualTo(infoId);
            List<Byte> statusList = new LinkedList<>();
            statusList.add(MchConstant.SETT_STATUS_AUDIT_ING);      // 等待审核
            statusList.add(MchConstant.SETT_STATUS_AUDIT_OK);       // 已审核
            statusList.add(MchConstant.SETT_STATUS_REMIT_ING);      // 打款中
            statusList.add(MchConstant.SETT_STATUS_REMIT_SUCCESS);  // 打款成功
            criteria.andSettStatusIn(statusList);
            criteria.andSettDateEqualTo(new Date());
            long sumSettAmount = settRecordMapper.sumSettAmountByExample(example);
            if(sumSettAmount + settAmount > drawMaxDayAmount) {
                throw ServiceException.build(RetEnum.RET_SERVICE_DRAW_DAY_AMOUNT_MAX_LIMIT);
            }
        }
    }

    public static void main(String[] args) throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;

        System.out.println(w);


        String datetime = "2018-08-05";
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date datet = f.parse(datetime);
        cal.setTime(datet);
        w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println(w);
    }
}
