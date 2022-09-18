package org.xxpay.mch.message.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysMessage;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/25
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/message")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MessageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询系统消息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        SysMessage sysMessage = rpcCommonService.rpcSysMessageService.findById(id);
        if(sysMessage == null || !sysMessage.getMchStatus().equals(MchConstant.PUB_YES)){  //判断是否可查看
            throw ServiceException.build(RetEnum.RET_COMM_RECORD_NOT_EXIST);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysMessage));
    }

    /** 系统消息列表 */
    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        SysMessage sysMessage = getObject( SysMessage.class);
        sysMessage.setMchStatus(MchConstant.PUB_YES);  //仅查询商户系统显示的内容
        int count = rpcCommonService.rpcSysMessageService.count(sysMessage);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysMessage> sysMessageList = rpcCommonService.rpcSysMessageService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), sysMessage);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(sysMessageList, count));
    }

}
