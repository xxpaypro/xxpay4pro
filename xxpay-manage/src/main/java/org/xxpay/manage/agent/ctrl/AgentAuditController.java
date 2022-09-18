package org.xxpay.manage.agent.ctrl;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import com.alibaba.fastjson.JSONObject;

/**
 * <p><b>Title: </b>AgentAuditController.java
 * <p><b>Description: </b>下级代理商审核ctrl
 * @author terrfly
 * @version V1.0
 * <p>
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_audit")
public class AgentAuditController extends BaseController {

    private final static MyLog _log = MyLog.getLog(AgentAuditController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        AgentInfo agentInfo = getObject( AgentInfo.class);
        
        agentInfo.setPsVal("statusIn",Arrays.asList(new Byte[]{MchConstant.STATUS_AUDIT_ING,MchConstant.STATUS_AUDIT_NOT}));
        long count =  rpcCommonService.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpcCommonService.rpcAgentInfoService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), agentInfo);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentInfoList, count));
    }

    @RequestMapping(value = "/audit")
    @ResponseBody
    @MethodLog( remark = "代理商审核" )
    public ResponseEntity<?> audit() {

        // 返回对象
        Long agentId = getValLongRequired( "agentId");
        Byte status = getValByteRequired( "status");
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(agentId);
        agentInfo.setStatus(status);
//        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
//        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}