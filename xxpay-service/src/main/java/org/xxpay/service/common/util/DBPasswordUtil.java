package org.xxpay.service.common.util;


import org.jasypt.util.text.BasicTextEncryptor;
import org.xxpay.core.common.constant.MchConstant;

public class DBPasswordUtil {

    public static void main(String[] args) {

        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(MchConstant.DB_PASSWORD_DECRYPT_KEY);  //设置秘钥

        try {
            System.out.println("加密结果： " + basicTextEncryptor.encrypt("nc"));
            System.out.println("加密结果2： " + basicTextEncryptor.encrypt("abcd1234"));
        } catch (Exception e) {
            System.out.println("加密失败！！！！！");
        }

        try {
            System.out.println("解密结果： " + basicTextEncryptor.decrypt("待解密密文"));
        } catch (Exception e) {
            System.out.println("解密失败！！！！！");
        }

    }

}
