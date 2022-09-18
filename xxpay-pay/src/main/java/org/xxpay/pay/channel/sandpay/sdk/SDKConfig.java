/**
 *
 * Licensed Property to Sand Co., Ltd.
 * 
 * (C) Copyright of Sand Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author           Date           Description
 *   ------------ ---------- ---------------------------------------------------
 *   企业产品团队       2016-10-12       基本参数工具类.
 * =============================================================================
 */
package org.xxpay.pay.channel.sandpay.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @ClassName: SDKConfig
 * @Description: 
 * @version 2.0.0
 */
public class SDKConfig {
	
	public static  Logger logger = LoggerFactory.getLogger(SDKConfig.class);
	
	public static final String FILE_NAME = "sand_sdk.properties";
	
	/**通讯地址. */
	private String url;
	/**商户号. */
	private String mid;
	/**平台商户号. */
	private String plMid;
	/** 商户私钥证书路径. */
	private String signCertPath;
	/** 商户私钥证书密码. */
	private String signCertPwd;
	/** 杉德证书路径. */
	private String sandCertPath;
	
	/** 配置文件中的通讯地址常量. */
	public static final String SDK_URL = "sandsdk.url";
	/** 配置文件中的商户号常量. */
	public static final String SDK_MID = "sandsdk.mid";
	/** 配置文件中的平台商户号常量. */
	public static final String SDK_PL_MID = "sandsdk.plMid";
	/** 配置文件中的商户私钥证书路径常量. */
	public static final String SDK_SIGN_CERT_PATH = "sandsdk.signCert.path";
	/** 配置文件中的商户私钥证书密码常量. */
	public static final String SDK_SIGN_CERT_PWD = "sandsdk.signCert.pwd";
	/** 配置文件中的杉德证书路径常量. */
	public static final String SDK_SNAD_CERT_PATH = "sandsdk.sandCert.path";

	/** 操作对象. */
	private static SDKConfig config = new SDKConfig();
	/** 属性文件对象. */
	private Properties properties;

	private SDKConfig() {
		super();
	}
	
	/**
	 * 获取config对象.
	 * @return
	 */
	public static SDKConfig getConfig() {
		return config;
	}
	
	/**
	 * 从properties文件加载
	 * 
	 * @param rootPath
	 *            不包含文件名的目录.
	 */
	public void loadPropertiesFromPath(String rootPath) {
		if (StringUtils.isNotBlank(rootPath)) {
			logger.info("从路径读取配置文件: " + rootPath+File.separator+FILE_NAME);
			File file = new File(rootPath + File.separator + FILE_NAME);
			InputStream in = null;
			if (file.exists()) {
				try {
					in = new FileInputStream(file);
					properties = new Properties();
					properties.load(in);
					loadProperties(properties);
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage(), e);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} finally {
					if (null != in) {
						try {
							in.close();
						} catch (IOException e) {
							logger.error(e.getMessage(), e);
						}
					}
				}
			} else {
				// 由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
				logger.error(rootPath + FILE_NAME + "不存在,加载参数失败");
			}
		} else {
			loadPropertiesFromSrc();
		}

	}
	
	/**
	 * 从classpath路径下加载配置参数
	 */
	public void loadPropertiesFromSrc() {
		InputStream in = null;
		try {
			logger.info("从classpath: " +SDKConfig.class.getClassLoader().getResource("").getPath()+" 获取属性文件"+FILE_NAME);
			in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (null != in) {
				properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			} else {
				logger.error(FILE_NAME + "属性文件未能在classpath指定的目录下 "+SDKConfig.class.getClassLoader().getResource("").getPath()+" 找到!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 根据传入的 {@link #load(Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public void loadProperties(Properties pro) {
		logger.info("开始从属性文件中加载配置项");
		String value = null;
		
		value = pro.getProperty(SDK_URL);
		if (!StringUtils.isEmpty(value)) {
			this.url = value.trim();
			logger.info("配置项：通讯地址==>"+SDK_URL +"==>"+ value+" 已加载");
		}
		value = pro.getProperty(SDK_MID);
		if (!StringUtils.isEmpty(value)) {
			this.mid = value.trim();
			logger.info("配置项：商户号==>"+ SDK_MID+"==>"+ value+" 已加载");
		}
		value = pro.getProperty(SDK_PL_MID);
		if (!StringUtils.isEmpty(value)) {
			this.plMid = value.trim();
			logger.info("配置项：平台商户号==>"+SDK_PL_MID +"==>"+ value+" 已加载");
		}
		value = pro.getProperty(SDK_SIGN_CERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.signCertPath = value.trim();
			logger.info("配置项：商户私钥证书路径==>"+SDK_SIGN_CERT_PATH +"==>"+ value+" 已加载");
		}
		value = pro.getProperty(SDK_SIGN_CERT_PWD);
		if (!StringUtils.isEmpty(value)) {
			this.signCertPwd = value.trim();
			logger.info("配置项：商户私钥证书密码==>"+SDK_SIGN_CERT_PWD +"==>"+ value+" 已加载");
		}
		value = pro.getProperty(SDK_SNAD_CERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.sandCertPath = value.trim();
			logger.info("配置项：杉德公钥证书路径==>"+SDK_SNAD_CERT_PATH +"==>"+ value+" 已加载");
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getPlMid() {
		return plMid;
	}

	public void setPlMid(String plMid) {
		this.plMid = plMid;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSandCertPath() {
		return sandCertPath;
	}

	public void setSandCertPath(String sandCertPath) {
		this.sandCertPath = sandCertPath;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	

}
