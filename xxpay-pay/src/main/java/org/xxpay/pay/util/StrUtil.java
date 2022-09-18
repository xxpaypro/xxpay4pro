package org.xxpay.pay.util;

/**
 * @author: dingzhiwei
 * @date: 17/12/24
 * @description:
 */
public class StrUtil {

    public static String getPaymentClass(String channel) {
        channel = channel.toLowerCase();
        return "org.xxpay.pay.channel." + channel + "." + toUpperCaseFirstOne(channel) + "PaymentService";
    }

    public static String getTransClass(String channel) {
        channel = channel.toLowerCase();
        return "org.xxpay.pay.channel." + channel + "." + toUpperCaseFirstOne(channel) + "TransService";
    }

    public static String getRefundClass(String channel) {
        channel = channel.toLowerCase();
        return "org.xxpay.pay.channel." + channel + "." + toUpperCaseFirstOne(channel) + "RefundService";
    }

    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
