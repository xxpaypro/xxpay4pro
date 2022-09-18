package org.xxpay.pay.channel.hflpay;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author: zhuxiao
 * @date: 18/11/14
 * @description: 惠付拉配置
 */
@Component
public class HflpayConfig {

    public final static String CHANNEL_NAME_HFLPAY = "hflpay"; 				                            // 渠道名称:惠付拉支付
    public final static String PAY_CHANNEL_HFL_ALIPAY_WAP = CHANNEL_NAME_HFLPAY + "_alipay_wap";		// 惠付拉支付宝wap支付

    public static final String RETURN_VALUE_SUCCESS = "success";
    public static final String RETURN_VALUE_FAIL = "fail";

    public static final String RETURN_PARAM_RETCODE = "retCode";	// 通讯返回码

    // 商户ID
    private String mchId;
    // 私钥
    private String key;
    // 支付请求地址
    private String reqUrl;


    public HflpayConfig(){}

    public HflpayConfig(String payParam) {
        Assert.notNull(payParam, "init hfl config error");
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
