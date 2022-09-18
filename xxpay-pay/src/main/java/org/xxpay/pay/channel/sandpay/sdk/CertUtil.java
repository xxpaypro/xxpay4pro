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
 *   企业产品团队       2016-10-12       证书工具类.
 * =============================================================================
 */
package org.xxpay.pay.channel.sandpay.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ClassName: CertUtil
 * @Description: sdk证书工具类，主要用于对证书的加载和使用
 * @version 2.0.0
 */
public class CertUtil {
	
	public static final String PUBLIC_KEY = "public_key";
	public static final String PRIVATE_KEY = "private_key";

	private static final Logger logger = LoggerFactory.getLogger(CertUtil.class);

	private static final ConcurrentHashMap<String, Object> keys = new ConcurrentHashMap<String, Object>();

	public static void init(String mchId, String publicKeyPath, String privateKeyPath, String keyPassword) throws Exception {
		// 加载私钥
		initPulbicKey(publicKeyPath);
		// 加载公钥
		initPrivateKey(mchId, privateKeyPath, keyPassword);
	}

	public static PublicKey getPublicKey() {
		return (PublicKey) keys.get(PUBLIC_KEY);
	}

	public static PrivateKey getPrivateKey() {
		return (PrivateKey) keys.get(PRIVATE_KEY);
	}

	public static PrivateKey getPrivateKey(String mchId) {
		return (PrivateKey) keys.get(mchId + PRIVATE_KEY);
	}

	private static void initPulbicKey(String publicKeyPath) throws Exception {
		if(keys.get(PUBLIC_KEY) == null) {
			String classpathKey = "classpath:";
			if (publicKeyPath != null) {
				try {
					InputStream inputStream = null;
					if (publicKeyPath.startsWith(classpathKey)) {
						inputStream = CertUtil.class.getClassLoader()
								.getResourceAsStream(publicKeyPath.substring(classpathKey.length()));
					} else {
						inputStream = new FileInputStream(publicKeyPath);
					}
					PublicKey publicKey = CertUtil.getPublicKey(inputStream);
					keys.put(PUBLIC_KEY, publicKey);
				} catch (Exception e) {
					logger.error("无法加载公钥[{}]", new Object[] { publicKeyPath });
					logger.error(e.getMessage(), e);
					throw e;
				}
			}
		}
	}

	private static void initPrivateKey(String mchId, String privateKeyPath, String keyPassword) throws Exception {
		if(keys.get(mchId+ PRIVATE_KEY) == null) {
			String classpathKey = "classpath:";

			try {
				InputStream inputStream = null;
				if (privateKeyPath.startsWith(classpathKey)) {
					inputStream = CertUtil.class.getClassLoader()
							.getResourceAsStream(privateKeyPath.substring(classpathKey.length()));
				} else {
					inputStream = new FileInputStream(privateKeyPath);
				}
				PrivateKey privateKey = CertUtil.getPrivateKey(inputStream, keyPassword);
				keys.put(mchId+ PRIVATE_KEY, privateKey);
			} catch (Exception e) {
				logger.error("无法加载本地私银[" + privateKeyPath + "]");
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	
	public static PublicKey getPublicKey(InputStream inputStream) throws Exception {
		try {

			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate oCert = (X509Certificate) cf.generateCertificate(inputStream);
			PublicKey publicKey = oCert.getPublicKey();
			return publicKey;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("读取公钥异常");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

	
	/**
	 * 获取私钥对象
	 * 
	 * @param inputStream
	 *            私钥输入流
	 * @return 私钥对象
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(InputStream inputStream, String password) throws Exception {
		try {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			char[] nPassword = null;
			if ((password == null) || password.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = password.toCharArray();
			}

			ks.load(inputStream, nPassword);
			Enumeration<String> enumas = ks.aliases();
			String keyAlias = null;
			if (enumas.hasMoreElements()) {
				keyAlias = (String) enumas.nextElement();
			}

			PrivateKey privateKey = (PrivateKey) ks.getKey(keyAlias, nPassword);
			return privateKey;
		} catch (FileNotFoundException e) {
			throw new Exception("私钥路径文件不存在");
		} catch (IOException e) {
			throw new Exception(e);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("生成私钥对象异常");
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
