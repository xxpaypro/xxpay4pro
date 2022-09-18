package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.ChannelConfig;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/01/17
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/channel")
public class ChannelConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        ChannelConfig channelConfig = getObject( ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.count(channelConfig);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<ChannelConfig> channelConfigList = rpcCommonService.rpcChannelConfigService.select((getPageIndex() -1) * getPageSize(), getPageSize(), channelConfig);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(channelConfigList, count));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Integer id = getValIntegerRequired( "id");
        ChannelConfig channelConfig = rpcCommonService.rpcChannelConfigService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(channelConfig));
    }

    /**
     * 查询渠道信息
     * @return
     */
    @RequestMapping("/get/channelId")
    @ResponseBody
    public ResponseEntity<?> getChanenelId() {

        String channelId = getValStringRequired( "channelId");
        ChannelConfig channelConfig = rpcCommonService.rpcChannelConfigService.findByChannelId(channelId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(channelConfig));
    }

    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        ChannelConfig channelConfig = getObject( ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.update(channelConfig);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {

        ChannelConfig channelConfig = getObject( ChannelConfig.class);
        int count = rpcCommonService.rpcChannelConfigService.add(channelConfig);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
