package org.xxpay.pay.channel.maxpay.model;

/**
 * 实时代付交易结果状态
 * Created by yujinshui on 2017/12/01.
 */
public enum OrderStatus {

    /**
     * 交易成功
     */
    SUCCEED,

    /**
     * 处理中
     */
    PROCESSING,

    /**
     * 交易失败
     */
    FAILED,

    /**
     * 处理中转失败
     */
    PROCESSING_TO_FAIL,

    /**
     * 处理中转成功
     */
     PROCESSING_TO_SUCCESS;
}
