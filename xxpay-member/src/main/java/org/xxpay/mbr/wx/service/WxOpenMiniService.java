package org.xxpay.mbr.wx.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.util.MyLog;

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

    /**
     * 获取直播间列表接口
     */
    String GET_LIVE_INFO = "https://api.weixin.qq.com/wxa/business/getliveinfo";

    /**
     * 获取直播间列表接口
     * @return
     * @throws Exception
     */
    public JSONObject getLiveInfo(int page, int limit, Long mchId, Byte authFrom) throws Exception {

        JSONObject params = new JSONObject();
        params.put("start", page);
        params.put("limit", limit);

        JSONObject response = xxPayWxComponentService.reqWXByMch(mchId, authFrom, GET_LIVE_INFO, params);
        return response;
    }


}
