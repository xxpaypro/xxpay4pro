package org.xxpay.pay.channel.gomepay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: dingzhiwei
 * @date: 18/3/20
 * @description: 银盈通配置
 */
@Component
public class GomepayConfig {

    // 商户号
    private String merchno;
    // 商户名称
    private String merchname;
    // 电商钱包ID
    private String mediumno;
    // 钱包密钥
    private String mediumkey;
    // 接口请求地址
    private String reqUrl;

    public GomepayConfig(){}

    public GomepayConfig(String payParam) {
        Assert.notNull(payParam, "init gomepay config error");
        JSONObject object = JSONObject.parseObject(payParam);
        this.merchno = object.getString("merchno");
        this.merchname = object.getString("merchname");
        this.mediumno = object.getString("mediumno");
        this.mediumkey = object.getString("mediumkey");
        this.reqUrl = object.getString("reqUrl");
    }

    public String getMerchno() {
        return merchno;
    }

    public void setMerchno(String merchno) {
        this.merchno = merchno;
    }

    public String getMerchname() {
        return merchname;
    }

    public void setMerchname(String merchname) {
        this.merchname = merchname;
    }

    public String getMediumno() {
        return mediumno;
    }

    public void setMediumno(String mediumno) {
        this.mediumno = mediumno;
    }

    public String getMediumkey() {
        return mediumkey;
    }

    public void setMediumkey(String mediumkey) {
        this.mediumkey = mediumkey;
    }

    public String getReqUrl() {
        return reqUrl;
    }

    public void setReqUrl(String reqUrl) {
        this.reqUrl = reqUrl;
    }
}
