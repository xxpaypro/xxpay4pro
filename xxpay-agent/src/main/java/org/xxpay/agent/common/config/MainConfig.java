package org.xxpay.agent.common.config;

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
	
	private String mchRegUrl;

	private String uploadMchStaticDir;

	private String uploadMchStaticViewUrl;

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

	public String getMchRegUrl() {
		return mchRegUrl;
	}

	public void setMchRegUrl(String mchRegUrl) {
		this.mchRegUrl = mchRegUrl;
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