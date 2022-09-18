package org.xxpay.pay.channel.unionpay.utils;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.util.MyLog;

import java.util.Map;

/**
 * 银联接口签名工具类
 * create date: 20191203
 * Auth: terrfly
 */
public class UniHttpUtils {

    private static final MyLog logger = MyLog.getLog(UniHttpUtils.class);

    public static String post(JSONObject reqData,String reqUrl) {
        //发送后台请求数据
        UniHttpClient hc = new UniHttpClient(reqUrl, 30000, 30000);//连接超时时间，读超时时间（可自行判断，修改）
        try {
            int status = hc.send(reqData, "UTF-8");
            if (200 == status) {
                return hc.getResult();

            }else{
                logger.error("银联HTTP请求失败！[reqUrl = {}, code = {}]", reqUrl, status);
                return null;
            }
        } catch (Exception e) {
            logger.error("银联HTTP请求失败！[reqUrl = {}]", reqUrl, e);
            return null;
        }
    }

}
