package org.xxpay.pay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

    public static String post(String urlPath, String paramsStr) throws IOException
    {
        //1, 得到URL对象
        URL url = new URL(urlPath);
        //2, 打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //3, 设置提交类型
        conn.setRequestMethod("POST");
        //4, 设置允许写出数据,默认是不允许 false
        conn.setDoOutput(true);
        conn.setDoInput(true);//当前的连接可以从服务器读取内容, 默认是true
        //5, 获取向服务器写出数据的流
        OutputStream os = conn.getOutputStream();
        //参数是键值队  , 不以"?"开始
        os.write(paramsStr.getBytes());
        os.flush();
        //6, 获取响应的数据
        //得到服务器写回的响应数据
        BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        String str = br.readLine();
        return  str;
    }
}
