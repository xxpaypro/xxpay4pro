package org.xxpay.core.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class AddressUtil {

    public static Map<String, BigDecimal> getLatAndLngByAddress(String addr) {
        String address = "";
        try {
            address = java.net.URLEncoder.encode(addr,"UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String url = String.format("http://api.map.baidu.com/place/v2/search?" + "ak=b0V7g4lS6RDLh5rYlOl290D7p29gnT2n&output=json&query=%s&region=全国", address);
        URL myURL = null;
        URLConnection httpsConn = null;
        //进行转码
        try {
            myURL = new URL(url);
        } catch (MalformedURLException e) {

        }
        StringBuffer sb = new StringBuffer();
        try {
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();;
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
                insr.close();
            }
        } catch (IOException e) {

        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        JSONObject resultJson = JSON.parseObject(sb.toString());
        //resultJson  {"message":"ok","results":[{"uid":"30e1d0bb0c0014f8b6147fe6","name":"攀枝花市","location":{"lng":101.725544,"lat":26.588034}}],"status":0}
        JSONArray jsonArray = (JSONArray)resultJson.get("results");
        JSONObject results0Obj = (JSONObject)jsonArray.get(0);
        JSONObject locationObj = (JSONObject)results0Obj.get("location");
        //纬度
        BigDecimal lat = (BigDecimal)locationObj.get("lat");
        //经度
        BigDecimal lng = (BigDecimal)locationObj.get("lng");
        map.put("lat", lat);
        map.put("lng", lng);
        return map;
    }

    /**
     * 补充：计算两点之间真实距离
     * @return 米
     */
    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        // 维度
        double lat1 = (Math.PI / 180) * latitude1;
        double lat2 = (Math.PI / 180) * latitude2;

        // 经度
        double lon1 = (Math.PI / 180) * longitude1;
        double lon2 = (Math.PI / 180) * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;

        return d * 1000;
    }

    public static void main(String[] args) {
        Map<String, BigDecimal> map = getLatAndLngByAddress("河北省沧州市运河区渤海紫信大厦");
        System.out.println(map);
    }
}
