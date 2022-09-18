package org.xxpay.pay.channel.maxpay.config;

/**
 * Created by xiaowei.wang on 2016/4/26.
 */
public class PaymaxConfig {
    //Paymax服务器地址
    public static final String API_BASE_URL = "https://www.paymax.cc/merchant-api/";
    //请求method
    public static final String PAY_REAL_TIME = "v1/real_time/pay";
    //头寸查询
    public static final String PAY_REAL_TIME_BALANCE = "v1/real_time/pay/balance";
    //对账单下载
    public static final String STATEMENT_URI = "v1/statement/download";

    //编码集
    public static final String CHARSET = "UTF-8";
    //签名后数据的key
    public static final String SIGN = "sign";
    //SDK版本
    public static final String SDK_VERSION = "1.0.0";
}
