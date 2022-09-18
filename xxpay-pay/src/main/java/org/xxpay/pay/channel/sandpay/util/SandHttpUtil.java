package org.xxpay.pay.channel.sandpay.util;

import cn.com.sand.online.agent.service.sdk.EncryptUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(SandHttpUtil.class);
    private EncryptUtil encyptUtil = new EncryptUtil();

    public SandHttpUtil() {
    }

    public SandHttpUtil(String publicKeyPath, String privateKeyPath, String keyPassword) {
        encyptUtil = new EncryptUtil(publicKeyPath, privateKeyPath, keyPassword);
    }

    public String post(String url, String merchId, String transCode, String data) throws Exception {
        String res = this.post(url, this.encyptUtil.genEncryptData(merchId, transCode, data));
        return res == null?null:this.encyptUtil.decryptRetData(res);
    }

    public String post(String url, String merchId, String transCode, String accessType, String plId, String data) throws Exception {
        String res = this.post(url, this.encyptUtil.genEncryptData(merchId, transCode, accessType, plId, data));
        return res == null?null:this.encyptUtil.decryptRetData(res);
    }

    private String post(String url, List<NameValuePair> formparams) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        try {
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            logger.info("executing request url:{} ", httppost.getURI());
            CloseableHttpResponse e = httpclient.execute(httppost);

            try {
                HttpEntity entity = e.getEntity();
                String res = EntityUtils.toString(entity, "UTF-8");
                res = URLDecoder.decode(res, "UTF-8");
                if(!StringUtils.isBlank(res)) {
                    logger.info("res:{}", res);
                    String var10 = res;
                    return var10;
                }

                logger.info("null response");
            } finally {
                e.close();
            }
        } catch (ClientProtocolException var32) {
            var32.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException var33) {
            var33.printStackTrace();
            return null;
        } catch (IOException var34) {
            var34.printStackTrace();
            return null;
        } finally {
            try {
                httpclient.close();
            } catch (IOException var30) {
                var30.printStackTrace();
            }

        }

        return null;
    }
}