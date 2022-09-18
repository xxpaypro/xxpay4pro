package org.xxpay.core.common.domain.api;

/**
 * 代付接口api 实体Bean
 */
public class AgentPayRes extends AbstractRes {

    /** 代付订单号 **/
    private String agentpayOrderId;

    /** 状态 **/
    private Byte status;

    /** 手续费, 单位：分 **/
    private Long fee;

    /** 转账返回消息  **/
    private String transMsg;

    /** 扩展域 **/
    private String extra;

    public String getAgentpayOrderId() {
        return agentpayOrderId;
    }

    public void setAgentpayOrderId(String agentpayOrderId) {
        this.agentpayOrderId = agentpayOrderId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public String getTransMsg() {
        return transMsg;
    }

    public void setTransMsg(String transMsg) {
        this.transMsg = transMsg;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}


