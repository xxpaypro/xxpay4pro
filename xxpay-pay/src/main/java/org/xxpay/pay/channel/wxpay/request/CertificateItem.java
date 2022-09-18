package org.xxpay.pay.channel.wxpay.request;

import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;

public class CertificateItem {
    @SerializedName("serial_no")
    private String serialNo;

    @SerializedName("effective_time")
    private OffsetDateTime effectiveTime;

    @SerializedName("expire_time")
    private OffsetDateTime expireTime;

    @SerializedName("encrypt_certificate")
    private EncryptedCertificateItem encryptCertificate;

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

    public EncryptedCertificateItem getEncryptCertificate() {
        return encryptCertificate;
    }

    public void setEncryptCertificate(EncryptedCertificateItem encryptCertificate) {
        this.encryptCertificate = encryptCertificate;
    }
}
