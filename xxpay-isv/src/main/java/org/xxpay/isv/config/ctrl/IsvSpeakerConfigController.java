package org.xxpay.isv.config.ctrl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.IsvSpeakerConfig;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

/**
 * <p>
 * 服务商云喇叭配置表 前端控制器
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-09
 */
@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/speaker_config")
public class IsvSpeakerConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;


    /**
     * 查询配置信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        //获取当前登录服务商ID
        Long isvId = getUser().getBelongInfoId();
        //查询该服务商云喇叭配置信息
        IsvSpeakerConfig config = rpcCommonService.rpcIsvSpeakerConfigService.getOne(
                new QueryWrapper<IsvSpeakerConfig>().lambda()
                        .eq(IsvSpeakerConfig::getIsvId, isvId)
        );
        return ResponseEntity.ok(XxPayResponse.buildSuccess(config));
    }

    /**
     * 添加或保存配置信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {
        IsvSpeakerConfig config = getObject( IsvSpeakerConfig.class);
        //查询是否已配置
        IsvSpeakerConfig speakerConfig = rpcCommonService.rpcIsvSpeakerConfigService.getById(config.getId());
        if (speakerConfig != null && !speakerConfig.getIsvId().equals(getUser().getBelongInfoId())) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        config.setIsvId(getUser().getBelongInfoId());
        boolean result = rpcCommonService.rpcIsvSpeakerConfigService.saveOrUpdate(config);
        if (!result ) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


}
