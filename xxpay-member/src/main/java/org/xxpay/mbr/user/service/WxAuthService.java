package org.xxpay.mbr.user.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenComponentService;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.api.impl.WxOpenInMemoryConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import me.chanjar.weixin.open.bean.WxOpenComponentAccessToken;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.vo.WxAuthVO;
import org.xxpay.core.entity.IsvWx3rdInfo;
import org.xxpay.mbr.common.service.RpcCommonService;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.TimeUnit;

@Service
public class WxAuthService {

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final MyLog _log = MyLog.getLog(WxAuthService.class);

    private static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";//获取小程序token
    private static final String GET_LIVE_INFO_URL = "https://api.weixin.qq.com/wxa/business/getliveinfo";//获取直播间列表url
    private static final String appId = "wxba1aa40ee83ca46a";
    private static final String secret = "1290bbe71c708886137bf4cfb33a2262";


    /**获取  小程序 直播间列表 */
    public JSONArray getLiveInfo(int start, int limit) throws Exception{

        try {
            String access_token = getAccessToken();
            String getLiveInfoUrl = GET_LIVE_INFO_URL + "?access_token=" +  access_token;

            //获取直播间列表url
            _log.info("获取直播间列表   url：{}", getLiveInfoUrl);

            //请求参数
            JSONObject reqParams = new JSONObject();
            reqParams.put("start", start);
            reqParams.put("limit", limit);

            JSONObject liveInfoJSON = callPost(getLiveInfoUrl, reqParams.toString());

            JSONArray roomInfo = JSONArray.parseArray(liveInfoJSON.getString("room_info"));

            return roomInfo;
        } catch (Exception e) {
            _log.error(e,"请求失败");
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**获取  小程序 access_token 接口调用凭证  */
    public String getAccessToken() throws Exception{

        //当前accessToken
        String access_token = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_PREFIX_MBR_MINI_ACCESS_TOKEN + appId);

        if (StringUtils.isBlank(access_token)){
            String url = String.format(GET_ACCESS_TOKEN + "?grant_type=client_credential&appid=%s&secret=%s", appId, secret);
            _log.info("获取access_token   url：{}", url);

            JSONObject jsonObject = callGet(url);
            access_token = jsonObject.getString("access_token");
            Integer expires_in = jsonObject.getInteger("expires_in");

            //重新放置redis，提前5分钟失效
            stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_PREFIX_MBR_MINI_ACCESS_TOKEN + appId, access_token, expires_in - 300, TimeUnit.SECONDS);
        }

        return access_token;
    }


    public JSONObject callGet(String reqUrl) {

        HttpGet httpGet = new HttpGet(reqUrl);
        httpGet.addHeader("Content-Type", "application/json");

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200){
                throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }

            String body = EntityUtils.toString(response.getEntity());
            _log.info("get请求结果：" + body);

            JSONObject object = (JSONObject) JSON.parse(body);
            return object;
        }catch (IOException e) {
            _log.error(e,"请求失败");
            throw new RuntimeException(e);
        }
    }

    public JSONObject callPost(String reqUrl, String reqParams) {

        HttpPost httpPost = new HttpPost(reqUrl);
        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(reqParams, "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200){
                throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
            }

            String body = EntityUtils.toString(response.getEntity());
            _log.info("get请求结果：" + body);

            JSONObject object = (JSONObject) JSON.parse(body);
            return object;
        }catch (IOException e) {
            _log.error(e, "请求失败");
            throw new RuntimeException(e);
        }
    }


}
