
package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("xml")
public class WxDepositConsumeResult extends BaseWxPayResult {

    /** 微信订单号 */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /** 商户订单号 */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /** 押金总金额 */
    @XStreamAlias("total_fee")
    private Integer totalFee;

    /** 消费金额 */
    @XStreamAlias("consume_fee")
    private Integer consumeFee;

    /** 货币类型 */
    @XStreamAlias("fee_type")
    private String feeType;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public Integer getConsumeFee() {
        return consumeFee;
    }

    public void setConsumeFee(Integer consumeFee) {
        this.consumeFee = consumeFee;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
