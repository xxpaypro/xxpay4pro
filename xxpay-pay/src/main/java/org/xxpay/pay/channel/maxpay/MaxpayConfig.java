package org.xxpay.pay.channel.maxpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/5/27
 * @description: 拉卡拉配置
 */
@Component
public class MaxpayConfig {

    // 商户ID
    private String mchId;
    // 商户私钥
    private String privateKey;
    // Paymax提供给商户的SecretKey
    private String secretKey;
    // Paymax提供给商户的公钥
    private String paymaxPublicKey;

    public MaxpayConfig(){}

    public MaxpayConfig(String payParam) {
        Assert.notNull(payParam, "init maxpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.privateKey = object.getString("privateKey");
        this.secretKey = object.getString("secretKey");
        this.paymaxPublicKey = object.getString("paymaxPublicKey");
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPaymaxPublicKey() {
        return paymaxPublicKey;
    }

    public void setPaymaxPublicKey(String paymaxPublicKey) {
        this.paymaxPublicKey = paymaxPublicKey;
    }
}
