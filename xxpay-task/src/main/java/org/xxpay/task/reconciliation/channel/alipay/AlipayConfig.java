package org.xxpay.task.reconciliation.channel.alipay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 17/8/21
 * @description:
 */
@Component
public class AlipayConfig {

    private String pid;             // 合作伙伴身份partner
    private String appId;           // 应用App ID
    private String privateKey;      // 应用私钥
    private String alipayPublicKey; // 支付宝公钥
    private String alipayAccount;   // 支付宝账号
    private String reqUrl;          // 请求网关地址
    // RSA2
    public static String SIGNTYPE = "RSA2";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";

    public AlipayConfig(){}

    public AlipayConfig(String payParam) {
        Assert.notNull(payParam, "init alipay config error");
        JSONObject object = JSON.parseObject(payParam);
        this.pid = object.getString("pid");
        this.appId = object.getString("appId");
        this.privateKey = object.getString("privateKey");
        this.alipayPublicKey = object.getString("alipayPublicKey");
        this.alipayAccount = object.getString("alipayAccount");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public void setAlipayPublicKey(String alipayPublicKey) {
        this.alipayPublicKey = alipayPublicKey;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}


