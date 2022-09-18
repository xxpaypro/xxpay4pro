package org.xxpay.pay.channel.yykpay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
 
/**
 * MD5-hmac加密类
 * @see HMAC-MD5是一种秘密的密钥验证算法。HMAC提供的数据完整性和源身份验证完全取决于秘密密钥分配的范围
 * @see 如果只有发起者和接收者知道HAMC密钥，那么这就对两者间发送的数据提供了源身份验证和完整性保证
 */
public class DigestUtil {
	/**
	 * 加密源数据
	 * @see 这是针对多条字符串（即数组）进行加密的方法。它会把数组元素拼成新字符串，然后再加密
	 * @see 本文暂未用到该方法
	 * @param aValue 加密的原文，即源数据
	 * @param aKey   密钥
	 */
	public static String getHmac(String[] args, String key) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmacSign(str.toString(), key));
	}
	
	/**
	 * 加密源数据
	 * @see 这是针对一条字符串进行加密的方法
	 * @param aValue 加密的原文，即源数据
	 * @param aKey   密钥
	 */
	public static String hmacSign(String aValue, String aKey) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = aKey.getBytes("UTF-8");
			value = aValue.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyb = aKey.getBytes();
			value = aValue.getBytes();
		}
 
		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}
 
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}
 
	public static String toHex(byte input[]) {
		if (input == null){
			return null;
		}
		StringBuffer output = new StringBuffer(input.length * 2);
		for(int i=0; i<input.length; i++){
			int current = input[i] & 0xff;
			if (current<16){
				output.append("0");
			}
			output.append(Integer.toString(current, 16));
		}
		return output.toString();
	}
	
//	/**
//	 * 本文暂未用到该方法
//	 */
//	public static String digest(String aValue) {
//		aValue = aValue.trim();
//		byte value[];
//		try {
//			value = aValue.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			value = aValue.getBytes();
//		}
//		MessageDigest md = null;
//		try {
//			md = MessageDigest.getInstance("SHA");
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			return null;
//		}
//		return toHex(md.digest(value));
//	}
//	
//	public static void main(String[] args) {
//		String value = "adpoga234lkdsngoiuayv1111wekng123123korhjtg";
//		String[] values = {"adpoga234lkdsngoiuayv11", "11wekng123123korhjtg"};
//		String key = "abcdjadyer";
//		System.out.println(hmacSign(value, key));
//		System.out.println(getHmac(values, key));
//	}
}