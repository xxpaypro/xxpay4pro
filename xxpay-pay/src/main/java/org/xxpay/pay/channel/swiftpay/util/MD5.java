package org.xxpay.pay.channel.swiftpay.util;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import org.apache.commons.codec.digest.DigestUtils;

/** 
* 功能：MD5签名
* 版本：3.3
* 修改日期：2012-08-17
* */
public class MD5 {
    public static void main(String[] args) {
        String str = "appId=wx8632a91376b81e24&callback_url=http://u.shdplan.com/usr/details.html?paySuc=true&awaId=AID201508100946430003&money=1.99&nonceStr=1441076469693&package=prepay_id=wx2015090111010993f0dd52c80787614418&signType=MD5&status=0&timeStamp=1441076469693";
        System.out.println(MD5.sign(str, "&key=8748fd966d0ffc47abd59e97", "utf-8"));
        
    }

    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
    	System.out.println(text);
        return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
    }
    
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String input_charset) {
    	text = text + key;
    	String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
    	if(mysign.equals(sign)) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

}