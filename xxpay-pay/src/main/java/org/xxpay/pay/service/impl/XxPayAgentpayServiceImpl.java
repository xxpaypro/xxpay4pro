package org.xxpay.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.service.IXxPayAgentpayService;
import org.xxpay.pay.service.AgentpayService;
import org.xxpay.pay.service.RpcCommonService;

import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/10/2
 * @description:
 */
@Service(interfaceName = "org.xxpay.core.service.IXxPayAgentpayService", version = Constant.XXPAY_SERVICE_VERSION, retries = Constant.DUBBO_RETRIES)
public class XxPayAgentpayServiceImpl implements IXxPayAgentpayService {

    private static final MyLog _log = MyLog.getLog(XxPayAgentpayServiceImpl.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private AgentpayService agentpayService;

    @Override
    public int applyAgentpay(AgentpayRecord mchAgentpayRecord, String key) {

        Long agentpayAmountL = mchAgentpayRecord.getAmount();
        String remark = mchAgentpayRecord.getRemark();

        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.selectAgpayAvailable(mchAgentpayRecord.getInfoType(), mchAgentpayRecord.getInfoId());
        if(feeScale == null) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_PASSAGE_NOT_EXIST);
        }
        Integer passageId = feeScale.getRefProductId();

        // 设置
        mchAgentpayRecord.setFee(XXPayUtil.calSingleOrderFee(feeScale, agentpayAmountL));  // 代付手续费
        // 实际打款金额
        long remitAmount = agentpayAmountL;
        if(remitAmount <= 0) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_AMOUNT_ERROR);
        }

        mchAgentpayRecord.setSubAmount(remitAmount + mchAgentpayRecord.getFee());  // 扣减账户金额
        if(mchAgentpayRecord.getInfoType() == MchConstant.INFO_TYPE_MCH && mchAgentpayRecord.getMchType() == MchConstant.MCH_TYPE_PRIVATE){
            mchAgentpayRecord.setSubAmount(mchAgentpayRecord.getFee());  // 扣减账户金额
        }

        mchAgentpayRecord.setRemitAmount(remitAmount);      // 打款金额
        mchAgentpayRecord.setRemark(StringUtils.isBlank(remark) ? "代付:" + agentpayAmountL/100 + "元" : remark);
        mchAgentpayRecord.setPassageId(passageId);

        // 判断代付申请时间间隔
        if(agentpayService.isHasAgentpay(mchAgentpayRecord)) {
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL, "频繁代付,卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元");
        }
        agentpayService.setAgentpay2Redis(mchAgentpayRecord);

        int result = rpcCommonService.rpcAgentpayService.applyAgentpay(mchAgentpayRecord);
        if(result != 1) {
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
        // 发起转账
        agentpayService.excuteTrans(mchAgentpayRecord, key);

        return result;
    }

    @Override
    public JSONObject batchApplyAgentpay(String key, List<AgentpayRecord> applyAgentpayRecordList) {

        Long maxEveryAmount = 0L;

        Long infoId = applyAgentpayRecordList.get(0).getInfoId();
        Byte infoType = applyAgentpayRecordList.get(0).getInfoType();


        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.selectAgpayAvailable(infoType, infoId);
        if(feeScale == null) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_PASSAGE_NOT_EXIST);
        }
        Integer passageId = feeScale.getRefProductId();

        String batchNo = MySeq.getUUID();   // 代付批次号
        // 代付记录
        List<AgentpayRecord> mchAgentpayRecordList = new LinkedList<>();
        // 代付笔数
        Integer totalNum = 0;
        // 代付总金额
        Long totalAmount= 0l;
        // 代付总手续费
        Long totalFee = 0l;
        for(AgentpayRecord mchAgentpayRecord : applyAgentpayRecordList) {
            String accountName = mchAgentpayRecord.getAccountName();
            String accountNo = mchAgentpayRecord.getAccountNo();
            Long agentpayAmount = mchAgentpayRecord.getAmount();

            if(agentpayAmount <= 0) {
                throw new ServiceException(RetEnum.RET_SERVICE_AMOUNT_ERROR, "代付金额必须大于0");
            }
            // 判断单笔限额
            if(maxEveryAmount != null && maxEveryAmount >= 0 &&  agentpayAmount > maxEveryAmount) {
                throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_AMOUNT_MAX_LIMIT, "单笔限额" + maxEveryAmount/100 + "元");
            }
            mchAgentpayRecord.setAgentpayOrderId(MySeq.getAgentpay());
            mchAgentpayRecord.setBatchNo(batchNo);
            mchAgentpayRecord.setAccountName(accountName);      // 账户名
            mchAgentpayRecord.setAccountNo(accountNo);          // 账号
            mchAgentpayRecord.setAmount(agentpayAmount);        // 代付金额
            mchAgentpayRecord.setFee(XXPayUtil.calSingleOrderFee(feeScale, agentpayAmount));                         // 单笔代付手续费
            mchAgentpayRecord.setRemitAmount(agentpayAmount);      // 打款金额

            mchAgentpayRecord.setSubAmount(agentpayAmount + mchAgentpayRecord.getFee());  // 扣减账户金额
            if(mchAgentpayRecord.getInfoType() == MchConstant.INFO_TYPE_MCH && mchAgentpayRecord.getMchType() == MchConstant.MCH_TYPE_PRIVATE){
                mchAgentpayRecord.setSubAmount(mchAgentpayRecord.getFee());  // 扣减账户金额
            }

            if(StringUtils.isBlank(mchAgentpayRecord.getRemark())) mchAgentpayRecord.setRemark("代付:" + agentpayAmount/100 + "元");
            mchAgentpayRecord.setPassageId(passageId);
            totalNum++;
            totalAmount += agentpayAmount;
            totalFee += mchAgentpayRecord.getFee();
            mchAgentpayRecordList.add(mchAgentpayRecord);
        }
        // 如果账户记录为空
        if(mchAgentpayRecordList.size() == 0) {
            throw new ServiceException(RetEnum.RET_MCH_SETT_BATCH_APPLY_EMPTY);
        }
        if((mchAgentpayRecordList.size() > 1000)) {
            throw new ServiceException(RetEnum.RET_MCH_SETT_BATCH_APPLY_LIMIT, "最多1000条");
        }

        // 查看是否有重复代付申请记录
        AgentpayRecord mchAgentpayRecord = agentpayService.isRepeatAgentpay(mchAgentpayRecordList);
        if(mchAgentpayRecord != null) {
            throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_ACCOUNTNO_REPEAT, "重复申请,卡号:" + mchAgentpayRecord.getAccountNo() + ",金额:" + mchAgentpayRecord.getAmount()/100.0 + "元");
        }

        // 判断代付申请,是否都满足时间间隔
        for(AgentpayRecord record : mchAgentpayRecordList) {
            // 判断代付申请时间间隔
            if(agentpayService.isHasAgentpay(record)) {
                throw new ServiceException(RetEnum.RET_MCH_AGENTPAY_INTERVAL_SHORT, "频繁代付,卡号:" + record.getAccountNo() + ",金额:" + record.getAmount()/100.0 + "元");
            }
        }
        // 将每个申请设置到redis
        for(AgentpayRecord record : mchAgentpayRecordList) {
            agentpayService.setAgentpay2Redis(record);
        }

        // 批量提交代付
        int batchInertCount = rpcCommonService.rpcAgentpayService.applyAgentpayBatch(mchAgentpayRecordList);

        // 提交批量任务
        agentpayService.asyncBatchTrans(mchAgentpayRecordList, key);

        JSONObject object= new JSONObject();
        object.put("totalNum", totalNum);
        object.put("totalAmount", totalAmount);
        object.put("totalFee", totalFee);
        object.put("batchInertCount", batchInertCount);
        return object;
    }

}
