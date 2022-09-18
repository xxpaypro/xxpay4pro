package org.xxpay.pay.channel.swiftpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/3/1
 * @description: 威富通配置
 */
@Component
public class SwiftpayConfig {

    // 商户ID
    private String mchId;
    // 商户Key
    private String key;
    // 请求地址
    private String reqUrl;

    public SwiftpayConfig(){}

    public SwiftpayConfig(String payParam) {
        Assert.notNull(payParam, "init swiftpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.key = object.getString("key");
        this.reqUrl = object.getString("reqUrl");
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
}
