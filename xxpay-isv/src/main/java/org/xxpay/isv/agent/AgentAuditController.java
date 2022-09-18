package org.xxpay.isv.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/agent_audit")
public class AgentAuditController extends BaseController {

    private final static MyLog logger = MyLog.getLog(AgentAuditController.class);

    @Autowired
    private RpcCommonService rpc;

    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        AgentInfo agentInfo = getObject(AgentInfo.class);
        agentInfo.setIsvId(getUser().getBelongInfoId()); //查询当前所属服务商的代理商信息
        agentInfo.setPsVal("statusIn", null);
        if (agentInfo.getStatus() == null) {
            agentInfo.setPsVal("statusIn",Arrays.asList(new Byte[]{MchConstant.STATUS_AUDIT_ING,MchConstant.STATUS_AUDIT_NOT}));
        }
        long count =  rpc.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpc.rpcAgentInfoService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), agentInfo);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentInfoList, count));
    }

    @RequestMapping(value = "/audit")
    @MethodLog( remark = "代理商审核" )
    public ResponseEntity<?> audit() {

        // 返回对象
        Long agentId = getValLongRequired("agentId");
        Byte status = getValByteRequired("status");
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(agentId);
        agentInfo.setStatus(status);
        boolean result = rpc.rpcAgentInfoService.updateById(agentInfo);
        if(!result) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}