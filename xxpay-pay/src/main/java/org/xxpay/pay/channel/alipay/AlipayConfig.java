package org.xxpay.pay.channel.alipay;

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
    private String encryptionType; // 接口加密方式 key-普通加密方式 ,cert-证书加密方式
    private String appPublicCert; // 应用公钥证书
    private String alipayPublicCert; // 支付宝公钥证书
    private String alipayRootCert; // 支付宝根证书
    private String sandboxKey;   // 使用沙箱秘钥

    private JSONObject convertJSON; //转换为JSON格式
    // RSA2
    public static String SIGNTYPE = "RSA2";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";

    public AlipayConfig(){}

    public AlipayConfig(String payParam) {
        Assert.notNull(payParam, "init alipay config error");
        convertJSON = JSON.parseObject(payParam);
        this.pid = convertJSON.getString("pid");
        this.appId = convertJSON.getString("appId");
        this.privateKey = convertJSON.getString("privateKey");
        this.alipayPublicKey = convertJSON.getString("alipayPublicKey");
        this.encryptionType = convertJSON.getString("encryptionType");
        this.appPublicCert = convertJSON.getString("appPublicCert");
        this.alipayPublicCert = convertJSON.getString("alipayPublicCert");
        this.alipayRootCert = convertJSON.getString("alipayRootCert");
        this.sandboxKey = convertJSON.getString("sandboxKey");
    }

    public AlipayConfig(JSONObject object) {
        convertJSON = object;
        this.pid = convertJSON.getString("pid");
        this.appId = convertJSON.getString("appId");
        this.privateKey = convertJSON.getString("privateKey");
        this.alipayPublicKey = convertJSON.getString("alipayPublicKey");
        this.encryptionType = convertJSON.getString("encryptionType");
        this.appPublicCert = convertJSON.getString("appPublicCert");
        this.alipayPublicCert = convertJSON.getString("alipayPublicCert");
        this.alipayRootCert = convertJSON.getString("alipayRootCert");
        this.sandboxKey = convertJSON.getString("sandboxKey");
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

    public String getSandboxKey() {
        return sandboxKey;
    }

    public void setSandboxKey(String sandboxKey) {
        this.sandboxKey = sandboxKey;
    }

    public JSONObject getConvertJSON() {
        return convertJSON;
    }

    public void setConvertJSON(JSONObject convertJSON) {
        this.convertJSON = convertJSON;
    }

    public String getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public String getAppPublicCert() {
        return appPublicCert;
    }

    public void setAppPublicCert(String appPublicCert) {
        this.appPublicCert = appPublicCert;
    }

    public String getAlipayPublicCert() {
        return alipayPublicCert;
    }

    public void setAlipayPublicCert(String alipayPublicCert) {
        this.alipayPublicCert = alipayPublicCert;
    }

    public String getAlipayRootCert() {
        return alipayRootCert;
    }

    public void setAlipayRootCert(String alipayRootCert) {
        this.alipayRootCert = alipayRootCert;
    }
}

