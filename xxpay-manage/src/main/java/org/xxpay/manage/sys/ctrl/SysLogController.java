package org.xxpay.manage.sys.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.entity.SysLog;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/sys/syslog")
public class SysLogController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 列表
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        SysLog sysLog = getObject( SysLog.class);

        int count = rpcCommonService.rpcSysLogService.count(sysLog);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<SysLog> result = rpcCommonService.rpcSysLogService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), sysLog);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(result, count));
    }
}