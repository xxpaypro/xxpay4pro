package org.xxpay.pay.channel.suixingpay.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.pay.channel.suixingpay.SuixingpayConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.UUID;

/**
 * @description:
 * @date: 2019/08/22 10:32
 */

public class UpLoadPicture {

    private static final MyLog _log = MyLog.getLog(UpLoadPicture.class);

    /**
     * 随行付上传图片获取图片路径ID
     * @param fileUrl   文件路径
     * @param picType   图片类型
     * @param paramJSON 服务商配置参数
     * @return
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public static String getUploadPictureId(String fileUrl, String picType, JSONObject paramJSON) throws FileNotFoundException, UnsupportedEncodingException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            String uploadUrl = SuixingpayConfig.REQ_URL + "/merchant/uploadPicture";
            HttpPost httppost = new HttpPost(uploadUrl);
            //获取文件名称
            String imgFileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            //下载文件 & 转换为byte数组格式
            byte[] imgFileByteArray = IOUtils.toByteArray(new URL(fileUrl));
//            FileBody bin = new FileBody(new File(fileUrl));
            //替换成自己的orgId  67290416
            StringBody orgId = new StringBody(paramJSON.getString("orgId"), ContentType.TEXT_PLAIN);      // 机构编号
            StringBody pictureType = new StringBody(picType, ContentType.TEXT_PLAIN);       // 图片类型
            StringBody reqId = new StringBody(UUID.randomUUID().toString().replace("-", ""),    // 请求ID唯一不重复
                    ContentType.TEXT_PLAIN);
            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", new ByteArrayBody(imgFileByteArray, imgFileName))
                    .addPart("orgId", orgId)
                    .addPart("pictureType", pictureType)
                    .addPart("reqId", reqId).build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String result = EntityUtils.toString(resEntity, "UTF-8");
                    JSONObject resultJson = JSONObject.parseObject(result);
                    if ("SXF0000".equals(resultJson.getString("code"))) {
                        JSONObject respJson = resultJson.getJSONObject("respData");
                        String taskCode = respJson.getString("data");
                    }else if ("0000".equals(resultJson.getString("code"))) {       // 请求成功
                        JSONObject dataJson = JSONObject.parseObject(resultJson.getString("respData")); // 获取参数
                        if ("0000".equals(dataJson.getString("bizCode"))) {
                            String photoUrl = dataJson.getString("PhotoUrl");
                            _log.info("随行付图片上传成功code：{}， photoUrl：{}，cdnUrl:{}", dataJson.getString("bizCode"),
                                    photoUrl, dataJson.getString("cdnUrl"));
                            return photoUrl;
                        }
                    }
                    return null;
                } else {
                    System.out.println("上传图片异常：");
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        String pictureId = getUploadPictureId("C:\\Users\\Ahri\\Desktop\\番茄酱\\0d396e6d55fbb2fb89b7d415424a20a44723dc75.jpg", "10", null);
        System.out.println(pictureId);
    }
}
