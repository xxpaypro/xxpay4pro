package org.xxpay.pay.channel.transfarpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/7/22
 * @description: 传化支付配置
 */
@Component
public class TransfarpayConfig {

    // 商户编码
    private String enterpriseCode;
    // 支付账号
    private String accountNumber;
    // 应用ID,调用方在doggy系统中的appid
    private String appId;
    // doggy系统私钥
    private String dogSk;
    // 接口请求地址
    private String reqUrl;

    public TransfarpayConfig(){}

    public TransfarpayConfig(String payParam) {
        Assert.notNull(payParam, "init transfarpay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.enterpriseCode = object.getString("enterpriseCode");
        this.accountNumber = object.getString("accountNumber");
        this.appId = object.getString("appId");
        this.dogSk = object.getString("dogSk");
        this.reqUrl= object.getString("reqUrl");
    }

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDogSk() {
        return dogSk;
    }

    public void setDogSk(String dogSk) {
        this.dogSk = dogSk;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
