package org.xxpay.core.service;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.entity.WxMchSnapshot;
import org.xxpay.core.entity.WxMchUpgradeSnapshot;

/**
 * 微信开放平台接口
 */
public interface IXxPayWxpayApiService {

    /**
     * 微信刷脸获取调用凭证
     * @param mchId
     * @param storeId
     * @param storeName
     * @param deviceId
     * @param rawdata
     * @return
     */
    JSONObject getWxpayFaceAuthInfo(Long mchId, String storeId, String storeName, String deviceId, String rawdata);

    /** 微信押金消费 **/
    void wxDepositConsume(String mchTradeOrderId, Long consumeAmount);

    /** 微信押金撤销 **/
    void wxDepositReverse(String mchTradeOrderId);

    /** 微信进件接口 **/
    void wxMicroApply(WxMchSnapshot wxMchSnapshot);

    /** 查询进件状态 **/
    JSONObject wxQueryMicroApply(Long applyNo);

    /** 小微商户升级接口 **/
    void wxMicroApplyUpgrade(WxMchUpgradeSnapshot wxMchUpgradeSnapshot);

    /** 小微商户升级接口查询 **/
    JSONObject wxQueryMicroApplyUpgrade(Long applyNo);

    /** 微信小微进件接口 **/
    void microApply(WxMchSnapshot wxMchSnapshot);

    /** 查询小微进件状态 **/
    JSONObject queryMicroApply(Long applyNo);

}
