package org.xxpay.core.common.domain.api;

import org.xxpay.core.entity.AccountBalance;

/**
 * 代付余额查询接口api 实体Bean
 */
public class AgentpayBalanceRes extends AbstractRes {

    /**  **/
    private String agentpayBalance;

    /**  **/
    private String availableAgentpayBalance;

    public String getAgentpayBalance() {
        return agentpayBalance;
    }

    public void setAgentpayBalance(String agentpayBalance) {
        this.agentpayBalance = agentpayBalance;
    }

    public String getAvailableAgentpayBalance() {
        return availableAgentpayBalance;
    }

    public void setAvailableAgentpayBalance(String availableAgentpayBalance) {
        this.availableAgentpayBalance = availableAgentpayBalance;
    }

    public void setByMchAccount(AccountBalance mchAccount){

        this.agentpayBalance = mchAccount.getAmount() + "";                    // 代付余额
        this.availableAgentpayBalance = (mchAccount.getAmount() - mchAccount.getUnAmount()) + "";  // 可用代付余额

    }


}


