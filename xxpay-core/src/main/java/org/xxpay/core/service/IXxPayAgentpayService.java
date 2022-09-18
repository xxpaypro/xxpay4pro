package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.AgentpayRecord;

import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 2018/5/29
 * @description: 代付接口
 */
public interface IXxPayAgentpayService {

    /**
     * 发起单笔代付申请
     * @param agentpayRecord
     * @return
     */
    int applyAgentpay(AgentpayRecord agentpayRecord, String key);

    /**
     * 发起批量代付申请
     * @param key
     * @param applyAgentpayRecordList     申请代付集合
     * @return
     */
    JSONObject batchApplyAgentpay(String key, List<AgentpayRecord> applyAgentpayRecordList);

}
