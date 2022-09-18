package org.xxpay.isv.common.config;

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

	private String payUrl;
	private String loginAgentUrl;
	private String mchRegUrl;
	private String isvApiUrl;
	private String mbrUrl;
	private String uploadMchStaticDir;
	private String uploadMchStaticViewUrl;
	private String uploadIsvCertRootDir;
	private String miniExperienceQrcodeDir;

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

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getLoginAgentUrl() {
		return loginAgentUrl;
	}

	public void setLoginAgentUrl(String loginAgentUrl) {
		this.loginAgentUrl = loginAgentUrl;
	}

	public String getMchRegUrl() {
		return mchRegUrl;
	}

	public void setMchRegUrl(String mchRegUrl) {
		this.mchRegUrl = mchRegUrl;
	}

	public String getIsvApiUrl() {
		return isvApiUrl;
	}

	public void setIsvApiUrl(String isvApiUrl) {
		this.isvApiUrl = isvApiUrl;
	}

	public String getMbrUrl() {
		return mbrUrl;
	}

	public void setMbrUrl(String mbrUrl) {
		this.mbrUrl = mbrUrl;
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

	public String getUploadIsvCertRootDir() {
		return uploadIsvCertRootDir;
	}

	public void setUploadIsvCertRootDir(String uploadIsvCertRootDir) {
		this.uploadIsvCertRootDir = uploadIsvCertRootDir;
	}

	public String getMiniExperienceQrcodeDir() {
		return miniExperienceQrcodeDir;
	}

	public void setMiniExperienceQrcodeDir(String miniExperienceQrcodeDir) {
		this.miniExperienceQrcodeDir = miniExperienceQrcodeDir;
	}
}