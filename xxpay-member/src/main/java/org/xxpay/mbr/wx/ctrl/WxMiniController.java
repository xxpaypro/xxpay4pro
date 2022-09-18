package org.xxpay.mbr.wx.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.wx.service.WxOpenMiniService;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/wx_mini")
public class WxMiniController extends BaseController {

    private static final MyLog logger = MyLog.getLog(WxMiniController.class);

    @Autowired
    private WxOpenMiniService wxOpenMiniService;

    /**
     *  获取直播间列表
     * @return
     */
    @RequestMapping("/get_live_info")
    public XxPayResponse getLiveInfo() {
        Long mchId = getUser().getMchId();
        Byte authFrom = getValByteRequired("authFrom");

        try {
            JSONObject respJSON = wxOpenMiniService.getLiveInfo((getPageIndex() -1) * getPageSize(), getPageSize(), mchId, authFrom);
            if (respJSON.getInteger("errcode") != 0) return XxPayResponse.buildSuccess();
            JSONArray roomInfo = respJSON.getJSONArray("room_info");

            return XxPayPageRes.buildSuccess(roomInfo, respJSON.getInteger("total"));
        } catch (WxErrorException wxError) {
            logger.error(wxError, "请求失败");
            return XxPayResponse.buildSuccess();
        } catch (Exception e) {
            logger.error(e, "操作失败");
            return XxPayResponse.buildSuccess();
        }
    }


}