package org.xxpay.pay.task;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.DateUtils;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.service.AgentpayService;
import org.xxpay.pay.service.RpcCommonService;

import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/09/09
 * @description:
 */
@Component
public class ReissueAgentpayScheduled extends ReissuceBase {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private AgentpayService agentpayService;

    private static final MyLog _log = MyLog.getLog(ReissueAgentpayScheduled.class);

    /**
     * 代付订单批量补单任务
     */
    //@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void ingTask() {
        String logPrefix = "【代付补单_处理中】";
        // 代付补单开关
        if(!isExcuteReissueTask(reissueAgentpayTaskSwitch, reissueAgentpayTaskIp)) {
            _log.info("{}当前机器不执行代付补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        AgentpayRecord queryAgentpayRecord = new AgentpayRecord();
        queryAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_ING);  // 代付中
        // 查询比当前时间小60秒,状态为处理中的订单
        Date createTimeEnd = new Date(System.currentTimeMillis() - 60 * 1000);
        JSONObject queyrObj = new JSONObject();
        queyrObj.put("createTimeEnd", createTimeEnd);
        boolean flag = true;
        _log.info("{}开始查询需要处理的代付订单,查询订单创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有
        while (flag) {
            List<AgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.select((pageIndex - 1) * limit, limit, queryAgentpayRecord, queyrObj);
            _log.info("{}查询需要处理的代付订单,共:{}条", logPrefix, mchAgentpayRecordList == null ? 0 : mchAgentpayRecordList.size());
            for(AgentpayRecord mchAgentpayRecord : mchAgentpayRecordList) {
                long startTime = System.currentTimeMillis();
                String transOrderId = mchAgentpayRecord.getTransOrderId();
                if(StringUtils.isEmpty(transOrderId)) { // 如果转账订单为空,一般为发起转账失败导致
                    // 通过代付订单去查询转账表
                    TransOrder transOrder = rpcCommonService.rpcTransOrderService.selectByInfoIdAndMchTransNo(mchAgentpayRecord.getInfoId(), mchAgentpayRecord.getInfoType(), mchAgentpayRecord.getAgentpayOrderId());
                    if(transOrder != null) {
                        // 不为空,则表示该笔代付已经发起过转账,但是更新转账订单号失败.
                        // 更新转账订单号,下次定时任务执行时会继续处理
                        rpcCommonService.rpcAgentpayService.updateTrans(mchAgentpayRecord.getAgentpayOrderId(), transOrder.getTransOrderId(), null);
                    }else {
                        _log.info("{}查询转账订单ID为空,发起转账请求.agentpayOrderId={}", logPrefix, mchAgentpayRecord.getAgentpayOrderId());
                        // 向支付中心发起转账申请

                        String key = "";
                        if(mchAgentpayRecord.getInfoType() == MchConstant.INFO_TYPE_MCH){
                            MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchAgentpayRecord.getInfoId());
                            key = mchInfo.getPrivateKey();
                        }else{
                            AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(mchAgentpayRecord.getInfoId());
                        }

                        JSONObject resultObj = agentpayService.createTransOrder(mchAgentpayRecord, key);
                        _log.info("{}发起转账请求,resultObj={}.agentpayOrderId={}", logPrefix, resultObj, mchAgentpayRecord.getAgentpayOrderId());
                        if(resultObj == null) {
                            _log.info("{}发起转账失败.agentpayOrderId={}", logPrefix, mchAgentpayRecord.getAgentpayOrderId());
                            // 更新为代付失败
                            rpcCommonService.rpcAgentpayService.updateStatus4Fail(mchAgentpayRecord.getAgentpayOrderId(),mchAgentpayRecord.getSubAmountFrom(), null, null);
                            _log.info("{}代付订单agentpayOrderId={},处理完毕.更新为失败,耗时:{} ms", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), System.currentTimeMillis() - startTime);
                        }else {
                            // 处理转账结果
                            transOrderId = resultObj.getString("transOrderId");
                            String transStatusStr = resultObj.getString("status");
                            int result = agentpayService.handleAgentpayResult(mchAgentpayRecord, transOrderId, transStatusStr);
                            _log.info("{}代付订单agentpayOrderId={},处理完毕.结果:{},耗时:{} ms", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), result, System.currentTimeMillis() - startTime);
                        }
                    }
                }else {
                    TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                    if(transOrder == null) {
                        _log.info("{}查询转账订单为空,停止处理.agentpayOrderId={},transOrderId={}", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), transOrderId);
                        continue;
                    }
                    _log.info("{}查询到代付订单agentpayOrderId={},对应的转账订单transOrderId={},状态为:{}", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), transOrderId, transOrder.getStatus());
                    int result = agentpayService.handleAgentpayResult(mchAgentpayRecord, transOrderId, transOrder.getStatus()+"");
                    _log.info("{}代付订单agentpayOrderId={},处理完毕.结果:{},耗时:{} ms", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), result, System.currentTimeMillis() - startTime);
                }
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(mchAgentpayRecordList) || mchAgentpayRecordList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次处理完成,", logPrefix);
    }

    /**
     * 代付订单批量补单任务
     */
    //@Scheduled(cron="0 0/1 * * * ?") //每分钟执行一次
    public void initTask() {
        String logPrefix = "【代付补单_待处理】";
        // 代付补单开关
        if(!isExcuteReissueTask(reissueAgentpayTaskSwitch, reissueAgentpayTaskIp)) {
            _log.info("{}当前机器不执行代付补单任务", logPrefix);
            return;
        }
        int pageIndex = 1;
        int limit = 100;
        AgentpayRecord queryAgentpayRecord = new AgentpayRecord();
        queryAgentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_INIT);  // 待处理
        // 查询比当前时间小60秒,状态为处理中的订单
        Date createTimeEnd = new Date(System.currentTimeMillis() - 60 * 1000);
        JSONObject queyrObj = new JSONObject();
        queyrObj.put("createTimeEnd", createTimeEnd);
        boolean flag = true;
        _log.info("{}开始查询需要处理的代付订单,查询订单创建时间<={}", logPrefix, DateUtils.getTimeStr(createTimeEnd, "yyyy-MM-dd HH:mm:ss"));
        // 循环查询所有商户账户
        while (flag) {
            List<AgentpayRecord> mchAgentpayRecordList = rpcCommonService.rpcAgentpayService.select((pageIndex - 1) * limit, limit, queryAgentpayRecord, queyrObj);
            _log.info("{}查询需要处理的代付订单,共:{}条", logPrefix, mchAgentpayRecordList == null ? 0 : mchAgentpayRecordList.size());
            for(AgentpayRecord mchAgentpayRecord : mchAgentpayRecordList) {
                long startTime = System.currentTimeMillis();
                String transOrderId = mchAgentpayRecord.getTransOrderId();
                if(StringUtils.isBlank(transOrderId)) {

                    String key = "";
                    if(mchAgentpayRecord.getInfoType() == MchConstant.INFO_TYPE_MCH){
                        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchAgentpayRecord.getInfoId());
                        key = mchInfo.getPrivateKey();
                    }else{
                        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(mchAgentpayRecord.getInfoId());
                    }

                    agentpayService.excuteTrans(mchAgentpayRecord, key);
                    _log.info("{}代付订单ID为空,发起转账.agentpayOrderId={},处理完毕.耗时:{} ms", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), System.currentTimeMillis() - startTime);
                }else {
                    TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
                    if(transOrder == null) {
                        _log.info("{}查询转账订单为空,停止处理.agentpayOrderId={},transOrderId={}", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), transOrderId);
                        continue;
                    }
                    _log.info("{}查询到代付订单agentpayOrderId={},对应的转账订单transOrderId={},状态为:{}", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), transOrderId, transOrder.getStatus());
                    int result = rpcCommonService.rpcAgentpayService.updateStatus4Ing(mchAgentpayRecord.getAgentpayOrderId(), null);
                    if(result != 1) continue;
                    result = agentpayService.handleAgentpayResult(mchAgentpayRecord, transOrderId, transOrder.getStatus()+"");
                    _log.info("{}代付订单agentpayOrderId={},处理完毕.结果:{},耗时:{} ms", logPrefix, mchAgentpayRecord.getAgentpayOrderId(), result, System.currentTimeMillis() - startTime);
                }
            }
            pageIndex++;
            if(CollectionUtils.isEmpty(mchAgentpayRecordList) || mchAgentpayRecordList.size() < limit) {
                flag = false;
            }
        }
        _log.info("{}本次处理完成,", logPrefix);
    }

}
