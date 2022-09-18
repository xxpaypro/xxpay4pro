package org.xxpay.pay.channel.maxpay.config;

/**
 * Created by xiaowei.wang on 2016/4/26.
 */
public class SignConfig {

  	// 商户自己的私钥【用com.Paymax.sign.RSAKeyGenerateUtil生成RSA秘钥对，公钥通过Paymax网站上传到Paymax，私钥设置到下面的变量中】
	public String privateKey;
	// Paymax提供给商户的SecretKey，登录网站后查看
	public String secretKey;
	// Paymax提供给商户的公钥，登录网站后查看
	public String paymaxPublicKey;

	public SignConfig(){}

	public SignConfig(String privateKey, String secretKey, String paymaxPublicKey) {
		this.privateKey = privateKey;
		this.secretKey = secretKey;
		this.paymaxPublicKey = paymaxPublicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getPaymaxPublicKey() {
		return paymaxPublicKey;
	}

	public void setPaymaxPublicKey(String paymaxPublicKey) {
		this.paymaxPublicKey = paymaxPublicKey;
	}
}
