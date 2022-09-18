package org.xxpay.isv.config.ctrl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.IsvAdvertConfig;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

/**
 * <p>
 * 服务商广告配置表 前端控制器
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-26
 */
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/advert_config")
public class IsvAdvertConfigController extends BaseController {
    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(IsvAdvertConfigController.class);

    @RequestMapping("/list")
    public ResponseEntity<?> list(Integer page, Integer limit) {

        IsvAdvertConfig config = getObject(IsvAdvertConfig.class);
        config.setIsvId(getUser().getBelongInfoId());
        IPage<IsvAdvertConfig> list = rpcCommonService.rpcIsvAdvertConfigService.getList(config, new Page<>(page, limit));
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    @RequestMapping("/add")
    public ResponseEntity<?> add() {
        IsvAdvertConfig config = getObject(IsvAdvertConfig.class);
        config.setIsvId(getUser().getBelongInfoId());
        boolean result = rpcCommonService.rpcIsvAdvertConfigService.save(config);
        if (!result) return  ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/get")
    public ResponseEntity<?> get() {
        Long id = getValLong("id");
        IsvAdvertConfig config = rpcCommonService.rpcIsvAdvertConfigService.getById(id);
        config.setPsVal("areaCodeInfo", rpcCommonService.rpcSysAreaCodeService.getById(config.getAreaCode()));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(config));
    }

    @RequestMapping("/update")
    public ResponseEntity<?> update() {
        IsvAdvertConfig config = getObject( IsvAdvertConfig.class);
        IsvAdvertConfig advertConfigServiceById = rpcCommonService.rpcIsvAdvertConfigService.getById(config.getId());
        if (!advertConfigServiceById.getIsvId().equals(getUser().getBelongInfoId())){
            _log.info("当前操作人非该广告信息发布人");
            ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        config.setIsvId(getUser().getBelongInfoId());
        boolean result = rpcCommonService.rpcIsvAdvertConfigService.saveOrUpdate(config);
        if (!result) return  ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/delete")
    public ResponseEntity<?> delete() {
        String idsString = getValString("ids");

        String[] ids = idsString.split(",");
        for(String id : ids) {
            rpcCommonService.rpcIsvAdvertConfigService.removeById(id);
        }
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    }
