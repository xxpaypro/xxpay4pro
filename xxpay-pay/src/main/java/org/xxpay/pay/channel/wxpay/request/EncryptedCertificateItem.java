package org.xxpay.pay.channel.wxpay.request;

import com.google.gson.annotations.SerializedName;

public class EncryptedCertificateItem {
    @SerializedName("algorithm")
    private String algorithm;

    @SerializedName("nonce")
    private String nonce;

    @SerializedName("associated_data")
    private String associatedData;

    @SerializedName("ciphertext")
    private String ciphertext;

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getAssociatedData() {
        return associatedData;
    }

    public void setAssociatedData(String associatedData) {
        this.associatedData = associatedData;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }
}
