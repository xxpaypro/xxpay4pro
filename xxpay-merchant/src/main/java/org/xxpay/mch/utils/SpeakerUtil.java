package org.xxpay.mch.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

public class SpeakerUtil {

    public static String url = "https://api.gateway.letoiot.com";

    /** 日志 logger */
    private final static MyLog _log = MyLog.getLog(SpeakerUtil.class);

    public static String httpPostWithJson(JSONObject jsonObj, String url){
        return httpPostWithJson( jsonObj, url,null);
    }
    public static String httpPostWithJson(JSONObject jsonObj, String url,String signature){
        String result= null;
        HttpPost post = null;
        try {
            HttpClient httpClient = HttpClients.createDefault();
            post = new HttpPost(url);
            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            post.setConfig(requestConfig);
            // 构造消息头
            post.setHeader("Content-type", "application/json; charset=utf-8");
            if(!StringUtils.isEmpty(signature)){
                post.setHeader("Authorization",signature);
            }
            // 构建消息实体
            StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送 Json 格式的数据请求
            entity.setContentType("application/json");
            post.setEntity(entity);
            HttpResponse response = httpClient.execute(post);
            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
                _log.info("请求出错:{} ", statusCode);
            }else{
                result= EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(post != null){
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    public static String httpGet(String url,String signature){
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        String result = null;
        try {
            // 设置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
            get.setConfig(requestConfig);
            get.setHeader("Authorization",signature);
            get.addHeader("Content-Type", "application/x-www-formurlencoded");
            HttpResponse response = httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取云喇叭服务器token
     * @param appCustId
     * @param appCustPwd
     */
    public static JSONObject getSignature(String appCustId, String appCustPwd) {
        String reqUrl = url + "/gateway/api/v2/getSignature";
        JSONObject params = JSONObject.parseObject("{'app_cust_id':'"+appCustId+"','app_cust_pwd':'"+appCustPwd+"'}");
        String json = SpeakerUtil.httpPostWithJson(params, reqUrl);
        JSONObject jsonObject = JsonUtil.getJSONObjectFromJson(json);
        if (!"0".equals(jsonObject.getString("code"))) {
            _log.error("获取token失败");
        }
        JSONObject data = jsonObject.getJSONObject("data");
        String signature = data.getString("signature");
        return data;
    }

    /**
     * 在原日期的基础上增加小时数
     * @param date
     * @param i
     * @return
     */
    public static Date addHourOfDate(Date date, int i){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, i);
        Date newDate = c.getTime();
        return newDate;
    }

    /**
     * 发送消息
     * @param signature
     * @param speakerId
     * @param amount
     */
    public static JSONObject postMsg(String signature, Long speakerId, Long amount, int payType) {
        String reqUrl = url + "/speaker/add-v2.php?id=" + speakerId + "&price=" + amount + "&pt=" + payType;
        String s = httpGet(reqUrl, signature);
        JSONObject json = JsonUtil.getJSONObjectFromJson(s);
        if (!"0".equals(json.getString("code"))){
            _log.info("发送消息失败");
        }
        return json;
    }
    /**
     * 绑定/解绑设备
     * @param relieve
     * @param speakerId
     * @param uid
     */
    public static JSONObject bindOrRelieve(String signature, String relieve, Long speakerId, String uid) {
        String reqUrl = url + "/speaker/bind-v2.php?id="+speakerId+"&m="+relieve+"&uid="+uid;
        String s = httpGet(reqUrl, signature);
        JSONObject json = JsonUtil.getJSONObjectFromJson(s);
        if (!"0".equals(json.getString("code"))){
            _log.info("绑定或解绑失败");
        }
        return json;
    }

}

