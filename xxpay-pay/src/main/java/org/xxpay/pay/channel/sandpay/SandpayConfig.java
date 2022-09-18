package org.xxpay.pay.channel.sandpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/4/20
 * @description: 杉德配置
 */
@Component
public class SandpayConfig {

    // 商户ID
    private String mchId;
    // 平台商户号(平台接入模式存在)
    private String plMid;
    // 杉德公钥文件
    private String publicKeyFile;
    // 商户私钥文件
    private String privateKeyFile;
    // 证书密码
    private String password;
    // 接口请求地址
    private String reqUrl;

    public SandpayConfig(){}

    public SandpayConfig(String payParam) {
        Assert.notNull(payParam, "init sandpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.mchId = object.getString("mchId");
        this.plMid = object.getString("plMid");
        this.publicKeyFile = object.getString("publicKeyFile");
        this.privateKeyFile = object.getString("privateKeyFile");
        this.password = object.getString("password");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getPlMid() {
        return plMid;
    }

    public void setPlMid(String plMid) {
        this.plMid = plMid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getPublicKeyFile() {
        return publicKeyFile;
    }

    public void setPublicKeyFile(String publicKeyFile) {
        this.publicKeyFile = publicKeyFile;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public void setPrivateKeyFile(String privateKeyFile) {
        this.privateKeyFile = privateKeyFile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
