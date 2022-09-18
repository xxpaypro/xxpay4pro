package org.xxpay.pay.channel.wxpay.utils;

import com.github.binarywang.wxpay.bean.result.WxPayCommonResult;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.xxpay.core.common.util.HttpClientUtil;
import org.xxpay.core.common.util.MD5Util;
import org.xxpay.core.common.util.MyLog;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.util.Map;

public class WxHttpsUtil {

    private static final MyLog logger = MyLog.getLog(WxHttpsUtil.class);

    private final static String FORM_CONTENT = "multipart/form-data;charset=utf-8";
    private final static String WX_IMG_UPLOAD_URL = "https://api.mch.weixin.qq.com/secapi/mch/uploadmedia";


    //  IOUtils.toByteArray(fis);  //图片byte数组
    public static String uploadFile(String wxMchId, String certPath, String wxMchKey, String imgHttpUrl) {

        try{

            //获取文件名称
            String imgFileName =imgHttpUrl.substring(imgHttpUrl.lastIndexOf("/") + 1);

            //下载文件 & 转换为byte数组格式
            byte[] imgFileByteArray = IOUtils.toByteArray(new URL(imgHttpUrl));

            //文件签名
            String fileMd5 =  DigestUtils.md5Hex(imgFileByteArray);

            //待签名串， 字典值排序
            String waitSignStr = "mch_id=" + wxMchId + "&media_hash=" + fileMd5 + "&key=" + wxMchKey;

            //参数签名值 & 大写
            String paramsSign = MD5Util.string2MD5(waitSignStr).toUpperCase();

            //组装参数
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addTextBody("mch_id", wxMchId);
            multipartEntityBuilder.addTextBody("media_hash", fileMd5);
            multipartEntityBuilder.addBinaryBody("media", imgFileByteArray, ContentType.DEFAULT_BINARY, imgFileName);
            multipartEntityBuilder.addTextBody("sign", paramsSign);

            HttpPost uploadFile = new HttpPost(WX_IMG_UPLOAD_URL);


            FileInputStream pkcfile = new FileInputStream(new File(certPath));
            //指定读取证书格式为PKCS12
            KeyStore keyStore = KeyStore.getInstance(HttpClientUtil.PKCS12);
            keyStore.load(pkcfile, wxMchId.toCharArray());  //证书密码为商户编号
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, wxMchId.toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext); //指定TLS版本

            HttpEntity multipart = multipartEntityBuilder.build();
            uploadFile.setEntity(multipart);
            uploadFile.setHeader("Content-Type", FORM_CONTENT);

            CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            CloseableHttpResponse response = client.execute(uploadFile);

            StringBuilder stringBuilder = new StringBuilder();
            if (response != null && response.getEntity() != null) {
                HttpEntity entity = response.getEntity();
                InputStream in = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                reader.lines().forEach(line -> stringBuilder.append(line));
            }

            String responseContent = stringBuilder.toString();

            WxPayCommonResult result = WxPayCommonResult.fromXML(responseContent, WxPayCommonResult.class);
            Map resultMap = result.toMap();
            return (String) resultMap.get("media_id");

        } catch (Exception e) {
            logger.error("wxHttpsError:", e);
            return "";
        }
    }
}
