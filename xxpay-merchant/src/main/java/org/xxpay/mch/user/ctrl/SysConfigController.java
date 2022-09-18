package org.xxpay.mch.user.ctrl;


import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/config")
public class SysConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询app配置信息
     *
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String type = getValStringRequired( "type");
        JSONObject obj = rpcCommonService.rpcSysConfigService.getSysConfigObj(type);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }

    /**
     * 修改app配置信息
     *
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        rpcCommonService.rpcSysConfigService.update(getReqParamJSON());
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}