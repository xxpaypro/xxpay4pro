package org.xxpay.mch.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchMemberConfig;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.net.URLEncoder;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/member_config")
public class MemberConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员卡配置详情
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        MchMemberConfig config = rpcCommonService.rpcMchMemberConfigService.getById(getUser().getBelongInfoId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(config));
    }

    /**
     * 会员充值规则详情
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchMemberConfig memberConfig = getObject(MchMemberConfig.class);
        memberConfig.setMchId(getUser().getBelongInfoId());
        boolean result = rpcCommonService.rpcMchMemberConfigService.saveOrUpdate(memberConfig);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 会员入口
     * @return
     */
    @RequestMapping("/getMbrEntrance")
    @ResponseBody
    public ResponseEntity<?> getMbrEntrance() {

        Long mchId = getUser().getBelongInfoId();
        String codeUrl = String.format("%s?mchId=%s", mainConfig.getMbrAddUrl(), mchId);

        JSONObject data = new JSONObject();
        data.put("codeUrl", codeUrl);
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(codeUrl) + "&widht=200&height=200");
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }


}
