package org.xxpay.mch.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;

public class BaiduAuthToken {

    private static final MyLog _log = MyLog.getLog(BaiduAuthToken.class);

    //请求地址
    private static final String authHost  = "https://aip.baidubce.com/oauth/2.0/token?";

    public static String getAccessToken(String appKey, String appSecret){
        String param = "grant_type=client_credentials&client_id="+ appKey + "&client_secret=" + appSecret;
        _log.info("百度语音请求地址 url = {}", authHost + param);
        try {
            String result = XXPayUtil.call4Post(authHost + param);
            if (StringUtils.isNotBlank(result)) {
                JSONObject obj = (JSONObject) JSONObject.parse(result);
                String access_token = obj.getString("access_token");
                return access_token;
            }
        }catch(Exception e){
            _log.error("获取token失败", e);
        }
        return null;
    }

}
