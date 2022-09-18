package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.AlipayMchSnapshot;

/**
 * 支付宝开放平台接口
 */
public interface IXxPayAlipayApiService {

    /**
     * 刷脸支付初始化
     * @param mchId
     * @param metaInfo
     * @return
     */
    JSONObject authenticationSmilepayInitialize(Long mchId, String metaInfo);

    /** 押金消费 */
    void depositConsume(String mchTradeOrderId, Long consumeAmount);

    /** 押金退还 **/
    void depositReverse(String mchTradeOrderId);

    /** 支付宝进件接口 **/
    void mchApply(AlipayMchSnapshot alipayMchSnapshot);

    /** 支付宝进件查询 **/
    void queryMchApply(Long applyNo);

}
