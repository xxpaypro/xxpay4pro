package org.xxpay.mbr.mch.ctrl;

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
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_config")
public class MchMemberConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员卡配置详情
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        MchMemberConfig config = rpcCommonService.rpcMchMemberConfigService.getById(getUser().getMchId());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(config));
    }

    /**
     * 会员充值规则详情
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        MchMemberConfig memberConfig = getObject( MchMemberConfig.class);
        memberConfig.setMchId(getUser().getMchId());
        boolean result = rpcCommonService.rpcMchMemberConfigService.saveOrUpdate(memberConfig);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


}
