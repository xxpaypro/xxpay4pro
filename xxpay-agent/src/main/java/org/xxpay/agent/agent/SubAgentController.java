package org.xxpay.agent.agent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.SysUser;

import java.util.List;
/**
 * <p><b>Title: </b>SubAgentController.java
 * <p><b>Description: </b>下级代理商ctrl
 * @author terrfly
 * @version V1.0
 * <p>
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/sub_agent")
public class SubAgentController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**
     * 查询代理商信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long agentId = getValLongRequired("agentId");
        AgentInfo agentInfo = rpc.rpcAgentInfoService.findByAgentId(agentId);
        if(!getUser().getBelongInfoId().equals(agentInfo.getPid())){ //禁止越权查询
        	 return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentInfo));
    }

    /**
     * 新增代理商信息
     * @return
     */
    @RequestMapping("/add")
    @MethodLog( remark = "新增下级代理商" )
    public ResponseEntity<?> add() {

        AgentInfo current = rpc.rpcAgentInfoService.findByAgentId(getUser().getBelongInfoId());

        if(current.getAllowAddSubAgent() != MchConstant.PUB_YES){ //代理商配置 不允许新增代理商
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_NOT_ALLOW_ADD_AGENT));
        }

        if(current.getAgentLevel() >= MchConstant.MAX_AGENTS_LEVEL){  // 超过最大等级限制
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_NOT_ALLOW_ADD_AGENT));
        }

        //获取请求参数并转换为对象类型
        AgentInfo agentInfo = getObject(AgentInfo.class);

        if(!XXPayUtil.checkLoginUserName(agentInfo.getLoginUserName())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR));
        }

        //判断登录名是否重复
        if(rpc.rpcAgentInfoService.count(agentInfo.lambda().eq(AgentInfo::getLoginUserName, agentInfo.getLoginUserName())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_USERNAME_USED));
        }

        //判断手机号是否重复
        if(rpc.rpcAgentInfoService.count(agentInfo.lambda().eq(AgentInfo::getMobile, agentInfo.getMobile())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        //判断邮箱是否重复
        if(rpc.rpcAgentInfoService.count(agentInfo.lambda().eq(AgentInfo::getEmail, agentInfo.getEmail())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }

        agentInfo.setAgentLevel(current.getAgentLevel() + 1);  //等级 = 当前等级  + 1
        agentInfo.setIsvId(current.getIsvId()); //设置当前服务商ID
        agentInfo.setPid(current.getAgentId()); //pid为当前代理商
        agentInfo.setStatus(MchConstant.STATUS_AUDIT_ING);  //待审核

        int count = rpc.rpcAgentInfoService.add(agentInfo);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        AgentInfo agentInfo = getObject(AgentInfo.class);
        agentInfo.setPid(getUser().getBelongInfoId()); //查询当前代理商的下辖 子代理商
        long count = rpc.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpc.rpcAgentInfoService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), agentInfo);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentInfoList, count));
    }

    /** 重置登录密码 **/
    @MethodLog( remark = "重置代理商登录密码" )
    @RequestMapping("/resetLoginPwd")
    public XxPayResponse resetLoginPwd() {

        Long agentId = getValLongRequired( "agentId");

        //判断越权操作
        AgentInfo agentInfo = rpc.rpcAgentInfoService.findByAgentId(agentId);
        if(agentInfo == null || !agentInfo.getPid().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }

        if(!rpc.rpcSysService.resetLoginPwd(MchConstant.INFO_TYPE_AGENT, agentId)){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }
        return XxPayResponse.buildSuccess();
    }


}