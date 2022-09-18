package org.xxpay.pay.channel.wxpay.request;

import java.time.OffsetDateTime;

public class PlainCertificateItem {
    private String serialNo;

    private OffsetDateTime effectiveTime;

    private OffsetDateTime expireTime;

    private String plainCertificate;

    public PlainCertificateItem(String serialNo, OffsetDateTime effectiveTime, OffsetDateTime expireTime, String plainCertificate) {
        this.serialNo = serialNo;
        this.effectiveTime = effectiveTime;
        this.expireTime = expireTime;
        this.plainCertificate = plainCertificate;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public OffsetDateTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(OffsetDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public OffsetDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(OffsetDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public String getPlainCertificate() {
        return plainCertificate;
    }

    public void setPlainCertificate(String plainCertificate) {
        this.plainCertificate = plainCertificate;
    }
}
