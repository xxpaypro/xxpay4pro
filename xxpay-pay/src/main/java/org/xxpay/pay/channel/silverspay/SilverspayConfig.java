package org.xxpay.pay.channel.silverspay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/6/29
 * @description: 睿联支付配置
 */
@Component
public class SilverspayConfig {

    // 商户ID
    private String mchId;
    // 私钥
    private String key;
    // 请求地址
    private String reqUrl;

    public SilverspayConfig(){}

    public SilverspayConfig(String payParam) {
        Assert.notNull(payParam, "init silverspay config error");
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
