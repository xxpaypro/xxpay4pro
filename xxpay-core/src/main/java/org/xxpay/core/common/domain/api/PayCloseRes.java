package org.xxpay.core.common.domain.api;

/**
 * 支付关单接口api 实体Bean
 */
public class PayCloseRes extends AbstractRes {

    /** 1-关闭成功,0-关闭失败 **/
    private ResultCode resultCode;

    private String resultMsg;

    public enum ResultCode {
        SUCCESS, // 成功
        FAIL   // 失败
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

}


