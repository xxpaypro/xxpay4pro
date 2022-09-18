/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2012 All Rights Reserved.
 */
package org.xxpay.pay.channel.hflpay.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author runzhi
 */
public class Signature {


    public static String generateMD5Sign(Map<String, String> params, String secret){
        return md5(getSignContent(params) + "&key=" + secret);
    }

    public static boolean md5SignVerify(Map<String, String> params, String secret) throws Exception{
        String sign = params.get("sign");
        params.remove("sign");
        params.remove("sign_type");
        return sign.equals(generateMD5Sign(params, secret));
    }



    public static String getSignContent(Map<String, String> params){
        Map<String, String> treeMap = new TreeMap<>(params);
        StringBuilder sb = new StringBuilder();
        for (String key : treeMap.keySet()) {// 排序后的字典，将所有参数按"key=value"格式拼接在一起
            String val = treeMap.get(key);
            if(val == null || "".equals(val.trim())){ continue;}

            sb.append(key).append("=").append(val).append("&");
        }

        return new String(sb.substring(0, sb.length() - 1).getBytes());
    }

    /**
     * 方法描述:将字符串MD5加码 生成32位md5码
     *
     * @author leon 2016年10月10日 下午3:02:30
     * @param inStr
     * @return
     */
    public static String md5(String inStr) {
        try {
            return DigestUtils.md5Hex(inStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误");
        }
    }

}
