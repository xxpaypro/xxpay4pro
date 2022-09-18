package org.xxpay.pay.channel.wxpay.request;

import java.util.ArrayList;
import java.util.List;

public class CertificateList {

    private List<CertificateItem> certs = new ArrayList<>();

    public CertificateList(List<CertificateItem> certs) {
        this.certs = certs;
    }

    public List<CertificateItem> getCerts() {
        return certs;
    }

    public void setCerts(List<CertificateItem> certs) {
        this.certs = certs;
    }

}
