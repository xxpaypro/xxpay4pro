package org.xxpay.pay.channel.hflpay.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5 {

	/**
	 * @Purpose  MD5加密
	 * @version  1.0
	 * @author   lizhun
	 * @param    str
	 * @return   String
	 */
	public static String code(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] data = digest.digest(str.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				String result = Integer.toHexString(data[i] & 0xff);
				if (result.length() == 1) {
					result = "0" + result;
				}
				sb.append(result);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}
    public static void  main(String[] args){
		System.out.println(		MD5.code("qianxin001"));
		System.out.println(		MD5.code("qianxin002"));
	}
}
