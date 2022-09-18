package org.xxpay.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.AgentpayRecord;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.core.entity.TransOrder;
import org.xxpay.pay.mq.BaseNotify4MchAgentpay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: dingzhiwei
 * @date: 18/10/02
 * @description: 代付业务
 */
@Service
public class AgentpayService {

    private static final MyLog _log = MyLog.getLog(AgentpayService.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private TransOrderService transOrderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public BaseNotify4MchAgentpay baseNotify4MchAgentpay;

    private static final long agentpayTimeout = 10; // 同一账号和金额的代付申请间隔时间10分钟

    @Value("${config.agentpayNotifyUrl}")
    protected String agentpayNotifyUrl;

    static ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(10);

    /**
     * 执行转账
     * @param agentpayRecord
     * @return
     */
    public void excuteTrans(AgentpayRecord agentpayRecord, String key) {
        // 1. 更新为处理中
        int result = rpcCommonService.rpcAgentpayService.updateStatus4Ing(agentpayRecord.getAgentpayOrderId(), null);
        if(result != 1) return;

        // 2. 向支付中心发起转账申请
        JSONObject resultObj = createTransOrder(agentpayRecord, key);
        if(resultObj == null) {
            _log.info("发起转账失败.agentpayOrderId={}", agentpayRecord.getAgentpayOrderId());
            // 更新为代付失败
            rpcCommonService.rpcAgentpayService.updateStatus4Fail(agentpayRecord.getAgentpayOrderId(), agentpayRecord.getSubAmountFrom(), null, null);
            deleteKey(agentpayRecord);
            return;
        }
        // 3. 处理转账结果
        String transOrderId = resultObj.getString("transOrderId");
        String  transStatusStr = resultObj.getString("status");
        handleAgentpayResult(agentpayRecord, transOrderId, transStatusStr);
    }

    /**
     * 异步批量转账
     * @param agentpayRecordList
     */
    public void asyncBatchTrans(List<AgentpayRecord> agentpayRecordList, String key) {
        scheduledThreadPool.execute(new Runnable() {
            public void run() {
                for(AgentpayRecord agentpayRecord : agentpayRecordList) {
                    excuteTrans(agentpayRecord, key);
                }
            }
        });
    }

    /**
     * 处理代付结果
     * @param agentpayRecord
     * @param transOrderId
     * @param transStatusStr
     */
    public int handleAgentpayResult(AgentpayRecord agentpayRecord, String transOrderId, String transStatusStr) {
        Byte transStatus = Byte.parseByte(transStatusStr);
        int result;
        if(PayConstant.REFUND_STATUS_FAIL == transStatus) {  // 明确转账失败
            // 更新为代付失败
            TransOrder transOrder = rpcCommonService.rpcTransOrderService.findByTransOrderId(transOrderId);
            result = rpcCommonService.rpcAgentpayService.updateStatus4Fail(agentpayRecord.getAgentpayOrderId(), agentpayRecord.getSubAmountFrom(), transOrderId, StrUtil.toString(transOrder.getChannelErrMsg(), ""));
            deleteKey(agentpayRecord);
            if(result == 1 && agentpayRecord.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_API
                    && StringUtils.isNotBlank(agentpayRecord.getNotifyUrl())) {
                agentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_FAIL);   // 状态为失败
                baseNotify4MchAgentpay.doNotify(agentpayRecord, true);
            }
        }else if(PayConstant.TRANS_STATUS_SUCCESS == transStatus ) { // 明确转账成功
            // 更新为代付成功
            result = rpcCommonService.rpcAgentpayService.updateStatus4Success(agentpayRecord.getAgentpayOrderId(), transOrderId, agentpayRecord.getPassageId());
            if(result == 1 && agentpayRecord.getAgentpayChannel() == MchConstant.AGENTPAY_CHANNEL_API
                    && StringUtils.isNotBlank(agentpayRecord.getNotifyUrl())) {
                agentpayRecord.setStatus(PayConstant.AGENTPAY_STATUS_SUCCESS);   // 状态为成功
                baseNotify4MchAgentpay.doNotify(agentpayRecord, true);
            }
        }else {
            result = rpcCommonService.rpcAgentpayService.updateTrans(agentpayRecord.getAgentpayOrderId(), transOrderId, null);
        }
        return result;
    }

    /**
     * 创建转账订单
     * @param agentpayRecord
     * @param key
     * @return
     */
    public JSONObject createTransOrder(AgentpayRecord agentpayRecord, String key) {

        JSONObject paramMap = new JSONObject();
        Byte infoType = agentpayRecord.getInfoType();

        if(infoType == MchConstant.INFO_TYPE_MCH){
            paramMap.put("mchId", agentpayRecord.getInfoId());   // 商户ID
        }else{
            paramMap.put("agentId", agentpayRecord.getInfoId());
        }
        paramMap.put("mchTransNo", agentpayRecord.getAgentpayOrderId());      // 商户代付单号
        paramMap.put("channelId", agentpayRecord.getChannelId());    // 代付渠道
        paramMap.put("passageId", agentpayRecord.getPassageId());    // 支付通道,平台账户需要传递
        paramMap.put("amount", agentpayRecord.getRemitAmount());     // 转账金额,单位分
        paramMap.put("currency", "cny");                                // 币种, cny-人民币
        paramMap.put("clientIp", IPUtility.getLocalIP());               // 用户地址,IP或手机号
        paramMap.put("device", "WEB");                                  // 设备
        paramMap.put("notifyUrl", agentpayNotifyUrl);                   // 异步回调URL
        paramMap.put("param1", "");                                     // 扩展参数1
        paramMap.put("param2", "");                                     // 扩展参数2
        paramMap.put("remarkInfo", agentpayRecord.getRemark());
        paramMap.put("accountName", agentpayRecord.getAccountName());
        paramMap.put("accountNo", agentpayRecord.getAccountNo());

        String reqSign = PayDigestUtil.getSign(paramMap, key);
        paramMap.put("sign", reqSign);   // 签名

        _log.info("[trans_req]{}", paramMap);
        String result = transOrderService.createTransOrder(paramMap, infoType == MchConstant.INFO_TYPE_MCH);
        _log.info("[trans_res]{}", result);
        JSONObject retObj = JSON.parseObject(result);
        if(XXPayUtil.isSuccess(retObj)) {
            // 验签
            String checkSign = PayDigestUtil.getSign(retObj, key, "sign");
            String retSign = (String) retObj.get("sign");
            if(checkSign.equals(retSign)) return retObj;
            _log.info("验签失败:retSign={},checkSign={}", retSign, checkSign);
        }
        return null;
    }

    /**
     * 查询代付订单
     * @param mchId
     * @param agentpayOrderId
     * @param mchOrderNo
     * @param executeNotify
     * @return
     */
    public AgentpayRecord query(Long mchId, String agentpayOrderId, String mchOrderNo, boolean executeNotify) {
        AgentpayRecord agentpayRecord;
        if(StringUtils.isNotBlank(agentpayOrderId)) {
            agentpayRecord = rpcCommonService.rpcAgentpayService.findByMchIdAndAgentpayOrderId(mchId, agentpayOrderId);
        }else {
            agentpayRecord = rpcCommonService.rpcAgentpayService.findByMchIdAndMchOrderNo(mchId, mchOrderNo);
        }
        if(agentpayRecord == null) return null;

        if(executeNotify && (PayConstant.AGENTPAY_STATUS_SUCCESS == agentpayRecord.getStatus() || PayConstant.AGENTPAY_STATUS_FAIL == agentpayRecord.getStatus())) {
            baseNotify4MchAgentpay.doNotify(agentpayRecord, false);
            _log.info("业务查单完成,并再次发送业务代付通知.发送完成");
        }
        return agentpayRecord;
    }

    /**
     * 判断代付申请在10分钟内是否发起过,如果发起过那么返回true,否则返回false,并将写入redis,设置超时时间10分钟
     * @param agentpayRecord
     * @return
     */
    public boolean isHasAgentpay(AgentpayRecord agentpayRecord) {
        String key = "mch_agentpay_" + agentpayRecord.getInfoId() + "_" + agentpayRecord.getInfoType() + agentpayRecord.getAccountNo() + "_" + agentpayRecord.getAmount();
        // 判断redis中是否有该笔转账
        if(stringRedisTemplate.hasKey(key)) {
            _log.info("[Redis]存在代付申请key={}", key);
            return true;
        }
        return false;
    }

    /**
     * 将代付申请设置在redis,并设置超时时间
     * @param agentpayRecord
     */
    public void setAgentpay2Redis(AgentpayRecord agentpayRecord) {
        String key = "mch_agentpay_" + agentpayRecord.getInfoId() + "_" + agentpayRecord.getInfoType() + agentpayRecord.getAccountNo() + "_" + agentpayRecord.getAmount();
        // 向redis里存入数据并设置超时时间
        stringRedisTemplate.opsForValue().set(key, agentpayRecord.getAccountNo(), agentpayTimeout * 60, TimeUnit.SECONDS);
        _log.info("[Redis]设置代付申请key={},timeout={}分", key, agentpayTimeout);
    }

    /**
     * 删除代付申请间隔key
     * 当代付失败时,需要清除该key
     * @param agentpayRecord
     */
    public void deleteKey(AgentpayRecord agentpayRecord) {
        // 删除key
        String key = "mch_agentpay_" + agentpayRecord.getInfoId() + "_" + agentpayRecord.getInfoType() + agentpayRecord.getAccountNo() + "_" + agentpayRecord.getAmount();
        stringRedisTemplate.delete(key);
        _log.info("[Redis]删除代付申请key={}", key);
    }

    /**
     * 判断是否有重复的代付申请(卡号+金额),只要有重复即返回
     * @param agentpayRecordList
     * @return
     */
    public AgentpayRecord isRepeatAgentpay(List<AgentpayRecord> agentpayRecordList) {
        Map<String, AgentpayRecord> map = new HashMap<>();
        if(agentpayRecordList == null || agentpayRecordList.size() <= 1) return null;
        for(AgentpayRecord record : agentpayRecordList) {
            String key = record.getAccountNo() + record.getAmount();
            if(map.get(key) != null) {
                return map.get(key);
            }
            map.put(key, record);
        }
        return null;
    }
}
