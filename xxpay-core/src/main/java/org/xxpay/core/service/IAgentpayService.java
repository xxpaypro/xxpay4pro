package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.AgentpayRecord;

import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/04/21
 * @description: 代付操作
 */
public interface IAgentpayService {

    /**
     * 申请代付
     * @param agentpayRecord
     * @return
     */
    int applyAgentpay(AgentpayRecord agentpayRecord);

    /**
     * 批量申请代付
     * @param agentpayRecordList
     * @return
     */
    int applyAgentpayBatch(List<AgentpayRecord> agentpayRecordList);

    /**
     * 更新代付状态为处理中
     * @param agentpayOrderId
     * @param transOrderId
     * @return
     */
    int updateStatus4Ing(String agentpayOrderId, String transOrderId);

    /**
     * 更新代付状态为成功
     * @param agentpayOrderId
     * @return
     */
    int updateStatus4Success(String agentpayOrderId, String transOrderId, Integer agentpayPassageId);

    /**
     * 更新代付状态为失败
     * @param agentpayOrderId
     * @return
     */
    int updateStatus4Fail(String agentpayOrderId, Byte subAmountFrom, String transOrderId, String transMsg);

    /**
     * 查询代付列表
     * @param offset
     * @param limit
     * @param agentpayRecord
     * @return
     */
    List<AgentpayRecord> select(int offset, int limit, AgentpayRecord agentpayRecord, JSONObject queryObj);

    /**
     * 更新转账信息
     * @param transOrderId
     * @param transMsg
     * @return
     */
    int updateTrans(String agentpayOrderId, String transOrderId, String transMsg);

    /**
     * 查询个数
     * @param agentpayRecord
     * @return
     */
    int count(AgentpayRecord agentpayRecord, JSONObject queryObj);

    /**
     * 查询代付记录
     * @param agentpayRecord
     * @return
     */
    AgentpayRecord find(AgentpayRecord agentpayRecord);

    AgentpayRecord findByTransOrderId(String transOrderId);

    AgentpayRecord findByAgentpayOrderId(String agentpayOrderId);

    AgentpayRecord findByMchIdAndAgentpayOrderId(Long mchId, String agentpayOrderId);

    AgentpayRecord findByMchIdAndMchOrderNo(Long mchId, String mchOrderNo);

    List<AgentpayRecord> select(int offset, int limit, List<Byte> statusList, AgentpayRecord agentpayRecord);

    int count(List<Byte> statusList, AgentpayRecord agentpayRecord);

    Map count4All(Long infoId, String accountName, String agentpayOrderId, String transOrderId, Byte status, Byte agentpayChannel, Byte infoType, Byte subAmountFrom, String createTimeStart, String createTimeEnd);
    
    public List<AgentpayRecord> selectAllBill(int offset, int limit, AgentpayRecord agentpayRecord) ;

    List<Map> agentpayStatistics(int offset, int limit, Long infoId, Byte infoType, String createTimeStart, String createTimeEnd);

    List<Map> countAgentpayStatistics(Long infoId, Byte infoType, String createTimeStart, String createTimeEnd);

    public List<Map> agentpayCount(Long currentAgentId, Long searchInfoId, Byte searchInfoType, String accountName, String agentpayOrderId, Byte agentpayChannel, Byte status, String createTimeStart, String createTimeEnd);

    List<Map> agentpaySelect(int offset, int limit, Long currentAgentId, Long searchInfoId, Byte searchInfoType,  String accountName, String agentpayOrderId, Byte agentpayChannel, Byte status, String createTimeStart, String createTimeEnd);
}
