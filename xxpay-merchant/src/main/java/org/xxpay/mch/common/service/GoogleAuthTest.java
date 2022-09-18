package org.xxpay.mch.common.service;

import org.xxpay.core.common.util.GoogleAuthenticator;

/**
 * @author: dingzhiwei
 * @date: 2018/7/13
 * @description:
 */
public class GoogleAuthTest {

    private static String key = "4Y57UR4GSTZXAR3P";


    public static void main(String[] args) {

        String secret = GoogleAuthenticator.generateSecretKey();
        String qrcode = GoogleAuthenticator.getQRBarcode("XxPay聚合支付(18610582396)", secret);
        System.out.println("qrcode:" + qrcode + ",key:" + secret);
        verifyTest(333513);
    }

    public static void verifyTest(long code) {

        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);    // 最多可偏移的时间
        boolean r = ga.check_code(key, code, t);
        System.out.println("检查code是否正确？" + r);

    }


}
