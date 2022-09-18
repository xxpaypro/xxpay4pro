package org.xxpay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.SysMessage;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/1/19
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/message")
public class SysMessageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询系统消息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        SysMessage sysMessage = rpcCommonService.rpcSysMessageService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(sysMessage));
    }

    /**
     * 新增系统消息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增系统消息" )
    public ResponseEntity<?> add() {
        SysMessage sysMessage = getObject(SysMessage.class);
        int i = rpcCommonService.rpcSysMessageService.add(sysMessage);
        if(i != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改系统消息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改系统消息" )
    public ResponseEntity<?> update() {

        SysMessage sysMessage = getObject( SysMessage.class);
        int count = rpcCommonService.rpcSysMessageService.update(sysMessage);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 系统消息列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SysMessage sysMessage = getObject( SysMessage.class);
        int count = rpcCommonService.rpcSysMessageService.count(sysMessage);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysMessage> sysMessageList = rpcCommonService.rpcSysMessageService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), sysMessage);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(sysMessageList, count));
    }

    /**
     * 删除系统消息
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    @MethodLog( remark = "删除系统消息" )
    public ResponseEntity<?> delete() {

        String ids = getValStringRequired( "ids");
        String[] idss = ids.split(",");
        List<Long> _ids = new LinkedList<>();
        for(String id : idss) {
            if(NumberUtils.isDigits(id)) _ids.add(Long.parseLong(id));
        }
        rpcCommonService.rpcSysMessageService.batchDelete(_ids);
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
