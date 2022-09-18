package org.xxpay.isv.config.ctrl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.IsvPrinterConfig;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

/**
 * <p>
 * 服务商与打印机配置表 前端控制器
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/printer_config")
public class IsvPrinterConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;



    /**
     * 查询配置信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long isvId = getUser().getBelongInfoId();
        IsvPrinterConfig config = rpcCommonService.rpcIsvPrinterConfigService.getById(isvId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(config));
    }

    /**
     * 添加或保存配置信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {
        IsvPrinterConfig config = getObject(IsvPrinterConfig.class);
        //获取服务商ID
        Long isvId = getUser().getBelongInfoId();
        //更新或保存配置
        config.setIsvId(isvId);
        boolean result = rpcCommonService.rpcIsvPrinterConfigService.saveOrUpdate(config);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
