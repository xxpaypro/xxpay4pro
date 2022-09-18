package org.xxpay.pay.channel.kqpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/1/29
 * @description: 快钱配置
 */
@Component
public class KqpayConfig {

    // 商户ID
    private String merchantAcctId;
    // 商户私钥证书路径
    private String privateCertPath;
    // 快钱公钥证书路径
    private String publicCertPath;
    // 证书密码
    private String keyPwd;
    // 快捷支付链接地址
    private String reqUrl;

    private String showUrl;

    public KqpayConfig(){}

    public KqpayConfig (String payParam) {
        Assert.notNull(payParam, "init kqpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.merchantAcctId = object.getString("merchantAcctId");
        this.privateCertPath = object.getString("privateCertPath");
        this.publicCertPath = object.getString("publicCertPath");
        this.keyPwd = object.getString("keyPwd");
        this.reqUrl = object.getString("reqUrl");
        this.showUrl = object.getString("showUrl");
    }

    public String getMerchantAcctId() {
        return merchantAcctId;
    }
    public void setMerchantAcctId(String merchantAcctId) {
        this.merchantAcctId = merchantAcctId;
    }
    public String getPrivateCertPath() {
        return privateCertPath;
    }

    public void setPrivateCertPath(String privateCertPath) {
        this.privateCertPath = privateCertPath;
    }

    public String getPublicCertPath() {
        return publicCertPath;
    }

    public void setPublicCertPath(String publicCertPath) {
        this.publicCertPath = publicCertPath;
    }

    public String getKeyPwd() {
        return keyPwd;
    }

    public void setKeyPwd(String keyPwd) {
        this.keyPwd = keyPwd;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }
}
