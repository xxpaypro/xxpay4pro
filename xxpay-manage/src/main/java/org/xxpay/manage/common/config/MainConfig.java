package org.xxpay.manage.common.config;

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
	private String loginMchUrl;
	private String loginAgentUrl;
	private String loginIsvUrl;
	private String settNotifyUrl;
	private String mchRegUrl;
	private String uploadIsvCertRootDir;

	public String getPayUrl() {
		return payUrl;
	}

	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}

	public String getLoginMchUrl() {
		return loginMchUrl;
	}

	public void setLoginMchUrl(String loginMchUrl) {
		this.loginMchUrl = loginMchUrl;
	}

	public String getLoginAgentUrl() {
		return loginAgentUrl;
	}

	public void setLoginAgentUrl(String loginAgentUrl) {
		this.loginAgentUrl = loginAgentUrl;
	}

	public String getSettNotifyUrl() {
		return settNotifyUrl;
	}

	public void setSettNotifyUrl(String settNotifyUrl) {
		this.settNotifyUrl = settNotifyUrl;
	}

	public String getMchRegUrl() {
		return mchRegUrl;
	}

	public void setMchRegUrl(String mchRegUrl) {
		this.mchRegUrl = mchRegUrl;
	}

	public String getLoginIsvUrl() {
		return loginIsvUrl;
	}

	public void setLoginIsvUrl(String loginIsvUrl) {
		this.loginIsvUrl = loginIsvUrl;
	}

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

	public String getUploadIsvCertRootDir() {
		return uploadIsvCertRootDir;
	}

	public void setUploadIsvCertRootDir(String uploadIsvCertRootDir) {
		this.uploadIsvCertRootDir = uploadIsvCertRootDir;
	}
}