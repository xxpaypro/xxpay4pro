package org.xxpay.mch.wx.ctrl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchWxauthInfo;
import org.xxpay.mch.common.ctrl.BaseController;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 商户小程序配置信息
 * </p>
 *
 * @author zx
 * @since 2020-08-17
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_mini_auth")
public class MchMiniAuthController extends BaseController {


    /**
     * 获取商户已授权小程序列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long mchId = getUser().getBelongInfoId();

        List<MchWxauthInfo> list = rpcCommonService.rpcMchWxauthInfoService.list(new QueryWrapper<MchWxauthInfo>().lambda()
                .eq(MchWxauthInfo::getMchId, mchId)
                .eq(MchWxauthInfo::getAuthStatus, MchConstant.MCH_WXAUTH_AUTHSTATUS_SUCCESS)
                .eq(MchWxauthInfo::getAuthType, (byte) 2)
        );

        List authfromList = new LinkedList();
        for (MchWxauthInfo mchWxauthInfo : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("authFrom", mchWxauthInfo.getAuthFrom());
            if (mchWxauthInfo.getAuthFrom() == MchConstant.MCH_INDUSTRY_TYPE_FOOD) {
                jsonObject.put("name", "点餐小程序");
            } else if (mchWxauthInfo.getAuthFrom() == MchConstant.MCH_POST_TYPE_MALL) {
                jsonObject.put("name", "商城小程序");
            }

            authfromList.add(jsonObject);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(authfromList));
    }


}
