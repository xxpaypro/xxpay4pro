package org.xxpay.pay.channel.hcpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/8/9
 * @description: 汇潮支付配置
 */
@Component
public class HcpayConfig {

    // 商户ID
    private String mchId;
    // 私钥
    private String key;
    // 支付请求地址
    private String reqUrl;
    // 订单查询接口地址
    private String queryUrl;

    public HcpayConfig(){}

    public HcpayConfig(String payParam) {
        Assert.notNull(payParam, "init hcpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.key = object.getString("key");
        this.reqUrl = object.getString("reqUrl");
        this.queryUrl = object.getString("queryUrl");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getQueryUrl() {
        return queryUrl;
    }

    public void setQueryUrl(String queryUrl) {
        this.queryUrl = queryUrl;
    }
}
