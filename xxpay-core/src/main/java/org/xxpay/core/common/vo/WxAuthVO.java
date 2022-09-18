package org.xxpay.core.common.vo;

/** 微信认证相关 VO */
public class WxAuthVO {

    public WxAuthVO(String appId, String accessToken){
        this.appId = appId;
        this.accessToken = accessToken;
    }

    private String appId = "";
    private String accessToken = "";

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
