package org.xxpay.pay.channel.dlbpay;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class DlbHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(DlbHttpUtils.class);

    public static JSONObject connect(String method, String urlPath, JSONObject reqData, String secretKey, String accessKey) throws Exception{

        //生成当前时间戳
        final String timestamp = String.valueOf(System.currentTimeMillis());

        URL url = new URL(DlbpayConfig.REQ_URL + urlPath);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("accessKey", accessKey);
        urlConnection.setRequestProperty("timestamp", timestamp);
        urlConnection.setRequestProperty("token",getSignStr(secretKey, timestamp, urlPath, reqData != null ? reqData.toJSONString(): null));
        urlConnection.connect();

        if(reqData != null){
            urlConnection.getOutputStream().write(reqData.toJSONString().getBytes());
        }

        InputStream in = null;
        try{
            in = urlConnection.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] data = new byte[1024];
            int count = -1;
            while((count = in.read(data,0,1024)) != -1){
                out.write(data, 0, count);
            }
            data = null;

            String text = new String(out.toByteArray());
            logger.info("哆啦宝res: {}" , text);
            return JSONObject.parseObject(text);

        }catch (Exception e) {
            logger.error("请求哆啦宝服务器异常！", e);
            return null;

        }finally {

            try {
                if(in != null) in.close();
                urlConnection.disconnect();
            } catch (Exception e) {
                logger.info("关闭异常:" , e);
            }
        }
    }

    /**
     *
     * {生成签名1}
     *
     * @param msg
     * @return
     * @author: zhaoyz
     */
    public static String getSHA1(String msg){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("SHA-1");
            md5.update(msg.getBytes());
            byte[] buffer=md5.digest();
            StringBuffer sb=new StringBuffer(buffer.length*2);
            for(int i=0;i<buffer.length;i++){
                sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 生成签名2
     * @param secretKey 密钥
     * @param timestamp 时间戳
     * @param path 路径
     * @param body 消息体 {can null}
     * @return sha1签名
     */
    private static String getSignStr(String secretKey, String timestamp, String path, String body) throws Exception {
        StringBuilder signStr = new StringBuilder();
        signStr.append("secretKey=").append(secretKey)
                .append("&timestamp=").append(timestamp)
                .append("&path=").append(path);
        if(body!=null && !body.isEmpty())
            signStr.append("&body=").append(body);
        return sha1(signStr.toString()).toUpperCase();
    }

    /** 获取回调签名值 **/
    public static String getSignStrByNotify(String secretKey, String timestamp) {

        try {
            StringBuilder signStr = new StringBuilder();
            signStr.append("secretKey=").append(secretKey)
                    .append("&timestamp=").append(timestamp);
            return sha1(signStr.toString()).toUpperCase();
        } catch (Exception e) {
            logger.error("签名异常！", e);
            return null;
        }
    }


    /**
     * SHA-1加密
     * @param sourceStr
     * @return
     */
    public static final String sha1(String sourceStr) throws Exception {
        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sourceStr.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static final String byteToHex(byte[] bytes){
        StringBuffer sb=new StringBuffer(bytes.length*2);
        for(int i=0;i<bytes.length;i++){
            sb.append(Character.forDigit((bytes[i] & 0xf0) >> 4, 16));
            sb.append(Character.forDigit(bytes[i] & 0x0f, 16));
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {

        String secretKey = "XXXXXX";
        String accessKey = "XXXXXX";
        String urlPath = "/v1/agent/order/payurl/create";
        String agentNum = "XXXXXX";
        String customerNum = "XXXXXX";
        String shopNum = "XXXXXX";

        JSONObject reqData = new JSONObject();
        reqData.put("agentNum", agentNum);
        reqData.put("customerNum", customerNum);
        reqData.put("shopNum", shopNum);
        reqData.put("requestNum", new Date().getTime());
        reqData.put("amount", "0.01");
        reqData.put("source", "API");

        JSONObject respJSON = connect("POST", urlPath, reqData, secretKey, accessKey);

        System.out.println(respJSON.toJSONString());

    }













}
