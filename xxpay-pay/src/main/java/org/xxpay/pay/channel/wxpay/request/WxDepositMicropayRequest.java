package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxDepositMicropayRequest extends WxPayMicropayRequest {

    /**
     * 是否押金支付，Y-是,N-普通付款码支付
     */
    @XStreamAlias("deposit")
    private String deposit;

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

}
