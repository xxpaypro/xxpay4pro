package org.xxpay.pay.channel.maxpay.sign;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by xiaowei.wang on 2016/4/27.
 */
public class RSA {

	public static String ALGORITHM = "RSA";

	public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算饭

	private static String log = "RSAUtil";

	public static String CHAR_SET = "UTF-8";

	/**
	 * 数据签名
	 * 
	 * @param content
	 *            签名内容
	 * @param privateKey
	 *            私钥
	 * @return 返回签名数据
	 */
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(CHAR_SET));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 签名验证
	 *
	 * @param content
	 * @param sign
	 * @param public_key
	 * @return
	 */
	public static boolean verify(String content, String sign,
			String public_key) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(public_key);
			PublicKey pubKey = keyFactory
					.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(CHAR_SET));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 通过公钥解密
	 *
	 * @param content 待解密数据
	 * @param pk 公钥
	 * @return 返回 解密后的数据
	 */
	protected static byte[] decryptByPublicKey(String content, PublicKey pk) {

		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.DECRYPT_MODE, pk);
			InputStream ins = new ByteArrayInputStream(Base64.decode(content));
			ByteArrayOutputStream writer = new ByteArrayOutputStream();
			// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
			byte[] buf = new byte[128];
			int bufl;
			while ((bufl = ins.read(buf)) != -1) {
				byte[] block = null;

				if (buf.length == bufl) {
					block = buf;
				} else {
					block = new byte[bufl];
					for (int i = 0; i < bufl; i++) {
						block[i] = buf[i];
					}
				}

				writer.write(ch.doFinal(block));
			}

			return writer.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 通过私钥加密
	 *
	 * @param content
	 * @param pk
	 * @return,加密数据，未进行base64进行加密
	 */
	protected static byte[] encryptByPrivateKey(String content, PrivateKey pk) {

		try {
			Cipher ch = Cipher.getInstance(ALGORITHM);
			ch.init(Cipher.ENCRYPT_MODE, pk);
			return ch.doFinal(content.getBytes(CHAR_SET));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("通过私钥加密出错");
		}
		return null;

	}

	/**
	 * 解密数据，接收端接收到数据直接解密
	 *
	 * @param content
	 * @param publicKey
	 * @return
	 */
	public static String decrypt(String content, String publicKey) {
		System.out.println(log + "：decrypt方法中key=" + publicKey);
		if (null == publicKey || "".equals(publicKey)) {
			System.out.println(log + "：decrypt方法中key=" + publicKey);
			return null;
		}
		PublicKey pk = getPublicKey(publicKey);
		byte[] data = decryptByPublicKey(content, pk);
		String res = null;
		try {
			res = new String(data, CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 对内容进行加密
	 *
	 * @param content
	 * @param privateKey 私钥
	 * @return
	 */
	public static String encrypt(String content, String privateKey) {
		PrivateKey pk = getPrivateKey(privateKey);
		byte[] data = encryptByPrivateKey(content, pk);
		String res = null;
		try {
			res = Base64.encode(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}

	/**
	 * 得到私钥对象
	 * @param privateKey 密钥字符串（经过base64编码的秘钥字节）
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKey)  {
		try {
			byte[] keyBytes;

			keyBytes = Base64.decode(privateKey);

			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PrivateKey privatekey = keyFactory.generatePrivate(keySpec);

			return privatekey;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 获取公钥对象
	 * @param publicKey 密钥字符串（经过base64编码秘钥字节）
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKey) {

		try {

			byte[] keyBytes;

			keyBytes = Base64.decode(publicKey);

			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");

			PublicKey publickey = keyFactory.generatePublic(keySpec);

			return publickey;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public static void main(String[] args) {
		String str = Base64.encode("{\"amount\":\"1\",\"body\":\"Your Body\",\"subject\":\"Your Subject\",\"channel\":\"alipay_app\",\"client_ip\":\"127.0.0.1\",\"order_no\":\"123456789\",\"appKey\":\"app_49b0f1dd741646d2b277524de2785836\",\"currency\":\"cny\"}".getBytes());
		System.out.println("Base64加密-->" + str);
		String aaa = sign(
				str,"MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK8dhzC5qTt5atWgvK6gqYvfURwNuqcbFIz8OUPk8vRw4GiG/bkvnzXOac14Jd/0gsk3o5eWuka5konSFAZb1X9xOy/5ay4pzUmE+lIBlRLUpQ9TYPgy2/kpRFMfGdD0Lh6TLqVISDRULSIjjpBfqZEo8rjnN2ZsXYj+vJplR3oJAgMBAAECgYEAl/Zf4wC6w0nSVCBwnHn/p8nbcSTY3Tq60r3uU+TSkR2DIDJE6/dPijndxtiExZxCAqut41aBv+46JC+SARKgadA0Ds/tE5/SEXV9mowuKyXxlGwBZ39IdcQirrN2WCjA5/Zfr9ZvzZYEilEcgrsWPo5BAZ8KP0h4HQhn345myqECQQDVaFcEOdN/bIJcJF3wj5cOXP3+LuxmZiL1S3L7wC8jPbb8jNYsPP7qcZAUIw/SoVjtqBV4jOyoLMqtaBb3EWnbAkEA0hC34c0xRGf5brNiCPzhRSlyrjlEun28gbechLNITDLpxTIKNic09GZ5zLKIsO4tMSytlkvY275+JPFgSrZK6wJAMNgo0nbJyah0TdKlDaJP2KHAIKVsxiMqbSpPrv9VyqMddsBlZMqxLcfASlucC4GScRK/l0/p+rQFz5q7ZuE9cQJBAKBr9hGyAzrrcHiS835LqLmJsrPMtmj5Vvl1QEmzJLZUd2SKF3RNssombEPf4DXGXqrcJODPoZgHPQx3bYMA/esCQBC9AR0PCpdpd1GdlTecHtUQxf7CAdANFnPSdq2L15LbSkWAVsAkYhXbsdym5zo/Kx/lBaoGOrgtJFKwZQoIeno=");
		System.out.println("加签-->" + aaa);
		System.out
				.println("验签-->"
						+ verify(
								str,
								aaa,
								"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCvHYcwuak7eWrVoLyuoKmL31EcDbqnGxSM/DlD5PL0cOBohv25L581zmnNeCXf9ILJN6OXlrpGuZKJ0hQGW9V/cTsv+WsuKc1JhPpSAZUS1KUPU2D4Mtv5KURTHxnQ9C4eky6lSEg0VC0iI46QX6mRKPK45zdmbF2I/ryaZUd6CQIDAQAB"));
		System.out.println("原文-->" + new String(Base64.decode(str)));
	}

}
