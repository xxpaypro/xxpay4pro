package org.xxpay.mch.common.config;

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

	private String appId;

	private String appSecret;

	private String token;

	private String aesKey;

	private String mchApiUrl;

	private String payUrl;

	private String notifyUrl;
	
	private String downloadDemoPath;

	private String uploadMchStaticDir;

	private String uploadMchStaticViewUrl;

	private String uploadAppUpdateDir;

	private String mbrAddUrl;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getAesKey() {
		return aesKey;
	}

	public void setAesKey(String aesKey) {
		this.aesKey = aesKey;
	}

	public String getMchApiUrl() {
		return mchApiUrl;
	}

	public void setMchApiUrl(String mchApiUrl) {
		this.mchApiUrl = mchApiUrl;
	}

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getDownloadDemoPath() {
		return downloadDemoPath;
	}

	public void setDownloadDemoPath(String downloadDemoPath) {
		this.downloadDemoPath = downloadDemoPath;
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

	public String getMbrAddUrl() {
		return mbrAddUrl;
	}

	public void setMbrAddUrl(String mbrAddUrl) {
		this.mbrAddUrl = mbrAddUrl;
	}

	public String getUploadAppUpdateDir() {
		return uploadAppUpdateDir;
	}

	public void setUploadAppUpdateDir(String uploadAppUpdateDir) {
		this.uploadAppUpdateDir = uploadAppUpdateDir;
	}
}