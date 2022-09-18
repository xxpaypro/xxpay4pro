package org.xxpay.pay.channel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: dingzhiwei
 * @date: 18/3/28
 * @description:
 */
@Component
@ConfigurationProperties(prefix="config")
public class PayConfig {

    private String notifyUrl;  // 后台异步通知地址

    private String returnUrl;  // 前端同步跳转地址

    private String notifyTransUrl;  // 转账异步通知地址

    private String uploadIsvCertRootDir; // 服务商上传证书根路径

    private String payUrl; // 支付中心url

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public String getNotifyUrl(String channelName) {
        if(StringUtils.isBlank(channelName)) return getNotifyUrl();
        return String.format(notifyUrl, channelName);
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl(String channelName) {
        if(StringUtils.isBlank(channelName)) return getReturnUrl();
        return String.format(returnUrl, channelName);
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getNotifyTransUrl() {
        return notifyTransUrl;
    }

    public String getUploadIsvCertRootDir() {
        return uploadIsvCertRootDir;
    }

    public void setUploadIsvCertRootDir(String uploadIsvCertRootDir) {
        this.uploadIsvCertRootDir = uploadIsvCertRootDir;
    }

    public String getNotifyTransUrl(String channelName) {
        if(StringUtils.isBlank(channelName)) return getNotifyTransUrl();
        return String.format(notifyTransUrl, channelName);
    }

    public void setNotifyTransUrl(String notifyTransUrl) {
        this.notifyTransUrl = notifyTransUrl;
    }
}
