package org.xxpay.mch.user.ctrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchAppConfig;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

/**
 * <p>
 * 商户用户配置表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-12
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH +"/mch/appConfig")
public class MchAppConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询用户app配置信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long userId = getUser().getUserId();
        MchAppConfig appConfig = rpcCommonService.rpcMchAppConfigService.getById(userId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(appConfig));
    }

    /**
     * 修改用户app配置信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        Long userId = getUser().getUserId();
        MchAppConfig appConfig = getObject(MchAppConfig.class);
        appConfig.setUserId(userId);
        Boolean result = rpcCommonService.rpcMchAppConfigService.updateById(appConfig);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
