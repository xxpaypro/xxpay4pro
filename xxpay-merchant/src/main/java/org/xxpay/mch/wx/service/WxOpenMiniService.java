package org.xxpay.mch.wx.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.common.vo.WxAuthVO;
import org.xxpay.core.entity.MchGoods;
import org.xxpay.core.entity.MchMiniLive;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.mch.common.service.RpcCommonService;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 *     微信开放平台代小程序实现服务能力
 * </pre>
 *
 */
@Service
public class WxOpenMiniService {

    private static final MyLog logger = MyLog.getLog(WxOpenMiniService.class);

    @Autowired
    private XxPayWxComponentService xxPayWxComponentService;

    @Autowired
    private RpcCommonService rpc;

    /**
     * 创建直播间接口
     */
    String ROOM_CREATE = "https://api.weixin.qq.com/wxaapi/broadcast/room/create";

    /**
     * 上传图片接口
     */
    String MEDIA_UPLOAD = "https://api.weixin.qq.com/cgi-bin/media/upload";

    /**
     * 获取直播间列表接口
     */
    String GET_LIVE_INFO = "https://api.weixin.qq.com/wxa/business/getliveinfo";

    /**
     * 直播间导入商品接口
     */
    String ROOM_ADD_GOODS = "https://api.weixin.qq.com/wxaapi/broadcast/room/addgoods";

    /**
     * 获取商品列表接口
     */
    String GOODS_GETAPPROVED = "https://api.weixin.qq.com/wxaapi/broadcast/goods/getapproved";

    /**
     * 获取商品列表接口
     */
    String GOODS_GETGOODSWAREHOUSE = "https://api.weixin.qq.com/wxa/business/getgoodswarehouse";

    /**
     * 商品提交审核至小程序直播商品库
     */
    String GOODS_ADD = "https://api.weixin.qq.com/wxaapi/broadcast/goods/add";

    /**
     * 更新商品
     */
    String GOODS_UPDATE = "https://api.weixin.qq.com/wxaapi/broadcast/goods/update";

    /**
     * 审核撤回
     */
    String GOODS_RESET_AUDIT = "https://api.weixin.qq.com/wxaapi/broadcast/goods/resetaudit";

    /**
     * 重新提交撤回
     */
    String GOODS_AUDIT = "https://api.weixin.qq.com/wxaapi/broadcast/goods/audit";

    /**
     * 删除商品
     */
    String GOODS_DELETE = "https://api.weixin.qq.com/wxaapi/broadcast/goods/delete";


    /**
     * 创建直播间接口
     * @return
     * @throws Exception
     */
    public JSONObject createRoom(Long mchId, MchMiniLive mchMiniLive) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", mchMiniLive.getName());
        jsonObject.put("coverImg", mchMiniLive.getCoverImg());
        jsonObject.put("startTime", mchMiniLive.getStartTime());
        jsonObject.put("endTime", mchMiniLive.getEndTime());
        jsonObject.put("anchorName", mchMiniLive.getAnchorName());
        jsonObject.put("anchorWechat", mchMiniLive.getAnchorWechat());
        jsonObject.put("anchorImg", mchMiniLive.getAnchorImg());
        jsonObject.put("type", mchMiniLive.getType());
        jsonObject.put("screenType", mchMiniLive.getScreenType());
        jsonObject.put("closeLike", mchMiniLive.getCloseLike());
        jsonObject.put("closeGoods", mchMiniLive.getCloseGoods());
        jsonObject.put("closeComment", mchMiniLive.getCloseComment());

        JSONObject response = reqJsonByMch(mchId, mchMiniLive.getAuthFrom(), ROOM_CREATE, jsonObject);
        // 组装post请求
        /*HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxaapi/broadcast/room/create?access_token="+accessToken);

        // 设置请求的header
        httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 执行请求
        String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
        JSONObject result = JSONObject.parseObject(json2);
*/
        return response;
    }

    //header：Content-Type为application/json;charset=utf-8
    public JSONObject reqJsonByMch(Long mchId, Byte authFrom, String url, JSONObject params) {

        try{
            WxAuthVO wxAuthVO = xxPayWxComponentService.getMchAccessToken(mchId, authFrom);
            String accessToken = wxAuthVO.getAccessToken();
            // 组装post请求
            HttpPost httpPost = new HttpPost(url + "?access_token=" + accessToken);

            // 设置请求的header
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            StringEntity entity = new StringEntity(params.toString(), "utf-8");
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            response = httpClient.execute(httpPost);
            // 执行请求
            String json2 = EntityUtils.toString(response.getEntity(), "utf-8");
            JSONObject result = JSONObject.parseObject(json2);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * 上传素材
     * @return
     * @throws Exception
     */
    public JSONObject uploadMedia(Long mchId, Byte authFrom, String savePath) throws Exception {

        WxAuthVO wxAuthVO = xxPayWxComponentService.getMchAccessToken(mchId, authFrom);
        String accessToken = wxAuthVO.getAccessToken();

        File file = new File(savePath);

        // 组装post请求
        String url = String.format(MEDIA_UPLOAD + "?access_token=%s&type=image", accessToken);
        HttpPost httpPost = new HttpPost(url);

        FileBody fileBody = new FileBody(file);
        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("media", fileBody)
                .build();
        httpPost.setEntity(reqEntity);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 200){
            throw new ServiceException(RetEnum.RET_COMM_OPERATION_FAIL);
        }

        String body = EntityUtils.toString(response.getEntity());
        logger.info("上传图片：" + body);

        return (JSONObject) JSON.parse(body);
    }

    /**
     * 获取直播间列表接口
     * @return
     * @throws Exception
     */
    public JSONObject getliveinfo(int page, int limit, Long mchId, Byte authFrom) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("start", page);
        jsonObject.put("limit", limit);

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, GET_LIVE_INFO, jsonObject);
        return response;
    }

    /**
     * 直播间导入商品
     * @return
     * @throws Exception
     */
    public JSONObject roomAddGoods(Long mchId, Byte authFrom, Long roomId, List goodsIds) throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("roomId", roomId);
        jsonObject.put("ids", goodsIds);

        JSONObject response = reqJsonByMch(mchId, authFrom, ROOM_ADD_GOODS, jsonObject);
        return response;
    }

    /**
     * 获取商品列表接口
     * @return
     * @throws Exception
     */
    public JSONObject getapproved(int offset, int limit, Long mchId, Byte authFrom, Byte status) throws Exception {

        Map<String, Object> map = new TreeMap<>();
        map.put("offset", offset);
        map.put("limit", limit);
        map.put("status", status);

        String params = XXPayUtil.genUrlParams(map);

        JSONObject response = xxPayWxComponentService.reqWXByMchGET(mchId, authFrom, GOODS_GETAPPROVED, params);
        //JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_GETAPPROVED, params);
        return response;
    }

    /**
     * 获取商品状态
     * @return
     * @throws Exception
     */
    public JSONObject goodsWareHouse(Long mchId, Byte authFrom, List goodsIds) {
        JSONObject params = new JSONObject();
        params.put("goods_ids", goodsIds);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_GETGOODSWAREHOUSE, params);
        return response;
    }

    /**
     *  更新商品
     * @return
     */
    public JSONObject updateGoods(Long mchId, Byte authFrom, JSONObject reqParams) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodsInfo", reqParams);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_UPDATE, jsonObject);
        return response;
    }

    /**
     *  商品提交审核至小程序直播商品库
     * @return
     */
    public JSONObject addGoods(Long mchId, Byte authFrom, Long goodsId, JSONObject reqParams) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodsInfo", reqParams);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_ADD, jsonObject);

        //获取到的文件名和文件内容更新到商户第三方授权信息表
        if (response.getInteger("errcode") == 0){
            Long miniGoodsId = response.getLong("goodsId");
            Long miniAuditId = response.getLong("miniAuditId");

            MchGoods updateRecord = new MchGoods();
            updateRecord.setGoodsId(goodsId);
            updateRecord.setMiniGoodsId(miniGoodsId);
            updateRecord.setMiniAuditId(miniAuditId);
            rpc.rpcMchGoodsService.updateById(updateRecord);
        }

        return response;
    }

    /**
     *  撤回商品审核
     * @return
     */
    public JSONObject resetAudit(Long mchId, Byte authFrom, Long goodsId, Long auditId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodsId", goodsId);
        jsonObject.put("auditId", auditId);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_RESET_AUDIT, jsonObject);
        return response;
    }

    /**
     *  重新提交审核
     * @return
     */
    public JSONObject audit(Long mchId, Byte authFrom, Long goodsId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodsId", goodsId);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_AUDIT, jsonObject);
        return response;
    }

    /**
     *  删除商品
     * @return
     */
    public JSONObject delete(Long mchId, Byte authFrom, Long goodsId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("goodsId", goodsId);

        JSONObject response = reqJsonByMch(mchId, authFrom, GOODS_DELETE, jsonObject);
        return response;
    }


}
