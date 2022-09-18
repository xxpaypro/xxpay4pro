package org.xxpay.mbr.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config")
public class MainConfig {


	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.expiration}")
	private Long jwtExpiration;

	public String getJwtSecret() {
		return jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public Long getJwtExpiration() {
		return jwtExpiration;
	}

	public void setJwtExpiration(Long jwtExpiration) {
		this.jwtExpiration = jwtExpiration;
	}

	private String mbrApiUrl;

	private String payUrl;

	private String mchUrl;

	private String notifyUrl;

	private String uploadMchStaticDir;

	private String uploadMchStaticViewUrl;
	
	public String getMbrApiUrl() {
		return mbrApiUrl;
	}

	public void setMbrApiUrl(String mbrApiUrl) {
		this.mbrApiUrl = mbrApiUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getMchUrl() {
		return mchUrl;
	}

	public void setMchUrl(String mchUrl) {
		this.mchUrl = mchUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getUploadMchStaticDir() {
		return uploadMchStaticDir;
	}

	public void setUploadMchStaticDir(String uploadMchStaticDir) {
		this.uploadMchStaticDir = uploadMchStaticDir;
	}

	public String getUploadMchStaticViewUrl() {
		return uploadMchStaticViewUrl;
	}

	public void setUploadMchStaticViewUrl(String uploadMchStaticViewUrl) {
		this.uploadMchStaticViewUrl = uploadMchStaticViewUrl;
	}
}