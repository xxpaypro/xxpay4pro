package org.xxpay.core.common.domain.api;

/**
 * 收银台api 实体Bean
 */
public class CashierRes extends AbstractRes {

    /**  **/
    private String payUrl;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
}


