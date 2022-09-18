package org.xxpay.core.common.domain.api;

import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.AgentpayRecord;

/**
 * 代付订单查询接口api 实体Bean
 */
public class AgentpayQueryRes extends AbstractRes {

    /**  **/
    private String agentpayOrderId;

    /**  **/
    private String mchOrderNo;

    /**  **/
    private String amount;

    /**  **/
    private String fee;

    /**  **/
    private String status;

    /**  **/
    private String transMsg;

    public String getAgentpayOrderId() {
        return agentpayOrderId;
    }

    public void setAgentpayOrderId(String agentpayOrderId) {
        this.agentpayOrderId = agentpayOrderId;
    }

    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransMsg() {
        return transMsg;
    }

    public void setTransMsg(String transMsg) {
        this.transMsg = transMsg;
    }

    public void setByMchAgentpayRecord(AgentpayRecord mchAgentpayRecord){

       this.agentpayOrderId = StrUtil.toString(mchAgentpayRecord.getAgentpayOrderId());
        this.mchOrderNo = StrUtil.toString(mchAgentpayRecord.getMchOrderNo());
        this.amount = StrUtil.toString(mchAgentpayRecord.getAmount());
        this.fee = StrUtil.toString(mchAgentpayRecord.getFee());
        this.status = StrUtil.toString(mchAgentpayRecord.getStatus());
        this.transMsg = StrUtil.toString(mchAgentpayRecord.getTransMsg());
    }

}


