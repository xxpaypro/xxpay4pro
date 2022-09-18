package org.xxpay.pay.channel.hflpay.util;



import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * httpPost请求，application/json格式
 * @author lina
 * @date   2017-8-22 上午11:03:24
 */
public class HuifulaUtil {

    public static JSONObject createZhiFuUtil(JSONObject parmams) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        //String url = "http://ccb.lanmeibank.com/api/v1/order/add";
        String url = parmams.getString("reqUrl");
        HttpPost httpPost = new HttpPost(url);

        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(parmams.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject jsonObject = JSONObject.parseObject(json2);

        // 打印执行结果
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static JSONObject queryUtil(JSONObject parmams) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        String url = parmams.getString("queryUrl");
        HttpPost httpPost = new HttpPost(url);

        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(parmams.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        // 执行请求
        HttpResponse response = httpClient.execute(httpPost);
        String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject jsonObject = JSONObject.parseObject(json2);

        // 打印执行结果
        System.out.println(jsonObject);
        return jsonObject;
    }

}