package org.xxpay.isv.agent;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.SpringSecurityUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.SysUser;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping(Constant.ISV_CONTROLLER_ROOT_PATH + "/agent")
public class AgentController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    /**
     * 查询服务商信息
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long agentId = getValLongRequired("agentId");
        AgentInfo agentInfo = rpc.rpcAgentInfoService.findByAgentId(agentId);
        if(!this.getUser().getBelongInfoId().equals(agentInfo.getIsvId())){ //禁止越权查询
        	 return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentInfo));
    }

    /** 新增服务商的一级代理商信息  */
    @RequestMapping("/add")
    @MethodLog( remark = "新增代理商" )
    public ResponseEntity<?> add() {

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

        agentInfo.setAgentLevel(1);  //默认一级代理商
        agentInfo.setIsvId(getUser().getBelongInfoId()); //设置当前服务商ID
        agentInfo.setPid(0L); //顶级代理商上为0

        int count = rpc.rpcAgentInfoService.add(agentInfo);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    /** 更新代理商信息  */
    @RequestMapping("/update")
    @MethodLog( remark = "更新代理商" )
    public ResponseEntity<?> update() {

        //获取请求参数并转换为对象类型
        AgentInfo updateRecord = getObject(AgentInfo.class);
        Long updatedAgentId = updateRecord.getAgentId();  //需更新的agentId

        AgentInfo updatedDbRecord = rpc.rpcAgentInfoService.findByAgentId(updatedAgentId); //需更新的数据库信息
        if(updatedDbRecord == null || !updatedDbRecord.getIsvId().equals(getUser().getBelongInfoId())){ //越权操作
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_AGENT_NOT_EXIST));
        }

        if(updateRecord.getLoginUserName() != null && !XXPayUtil.checkLoginUserName(updateRecord.getLoginUserName())){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_SERVICE_LOGINUSERNAME_ERROR));
        }

        //判断登录名是否重复
        if(rpc.rpcAgentInfoService.count(updateRecord.lambda().ne(AgentInfo::getAgentId, updatedAgentId).eq(AgentInfo::getLoginUserName, updateRecord.getLoginUserName())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_USERNAME_USED));
        }

        //判断手机号是否重复
        if(rpc.rpcAgentInfoService.count(updateRecord.lambda().ne(AgentInfo::getAgentId, updatedAgentId).eq(AgentInfo::getMobile, updateRecord.getMobile())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        //判断邮箱是否重复
        if(rpc.rpcAgentInfoService.count(updateRecord.lambda().ne(AgentInfo::getAgentId, updatedAgentId).eq(AgentInfo::getEmail, updateRecord.getEmail())) > 0){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
        }

        updateRecord.setIsvId(getUser().getBelongInfoId()); //设置当前服务商ID


        if(updateRecord.getProfitRate() != null){ //如果设置分佣比例

            BigDecimal profitRate = updateRecord.getProfitRate();
            JSONObject profitSetInfo = getProfitSetInfo(updateRecord.getAgentId());  //查询上下级分佣比例设置信息

            if(profitRate.compareTo(new BigDecimal(0)) < 0 || profitRate.compareTo(new BigDecimal(100)) > 0){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_ISV_AGENT_PROFIT_ERROR));
            }

            // 设置规则： 下级最高 ≤ X ≤ 上级分佣比例

            //包含上级代理商费率
            if(profitSetInfo.getBigDecimal("parentProfit") != null && profitRate.compareTo(profitSetInfo.getBigDecimal("parentProfit")) > 0){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_ISV_AGENT_PROFIT_ERROR));
            }

            //包含下级最高费率
            if(profitSetInfo.getBigDecimal("maxSubsProfit") != null && profitRate.compareTo(profitSetInfo.getBigDecimal("maxSubsProfit")) < 0 ){
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_ISV_AGENT_PROFIT_ERROR));
            }
        }

        SysUser agentUser = null;
        //如果密码有变更 | 登录认证信息发生变化
        if((StringUtils.isNotEmpty(updateRecord.getLoginUserName()) && !updatedDbRecord.getLoginUserName().equals(updateRecord.getLoginUserName()))
                || (StringUtils.isNotEmpty(updateRecord.getMobile()) && !updatedDbRecord.getMobile().equals(updateRecord.getMobile()))
                || (StringUtils.isNotEmpty(updateRecord.getEmail()) && !updatedDbRecord.getEmail().equals(updateRecord.getEmail()))
                || (StringUtils.isNotEmpty(updateRecord.getAgentName()) && !updatedDbRecord.getAgentName().equals(updateRecord.getAgentName()))
        ){

            agentUser = new SysUser();
            agentUser.setLoginUserName(StringUtils.isNotEmpty(updateRecord.getLoginUserName()) ? updateRecord.getLoginUserName(): null);
            agentUser.setMobile(StringUtils.isNotEmpty(updateRecord.getMobile()) ? updateRecord.getMobile(): null);
            agentUser.setEmail(StringUtils.isNotEmpty(updateRecord.getEmail()) ? updateRecord.getEmail(): null);
            agentUser.setNickName(StringUtils.isNotEmpty(updateRecord.getAgentName()) ? updateRecord.getAgentName(): null);
        }

        int count = rpc.rpcAgentInfoService.updateAgent(updateRecord, agentUser);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }


    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        AgentInfo agentInfo = getObject(AgentInfo.class);
        agentInfo.setIsvId(this.getUser().getBelongInfoId()); //查询当前服务商的 一级代理商
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
        if(agentInfo == null || !agentInfo.getIsvId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }

        if(!rpc.rpcSysService.resetLoginPwd(MchConstant.INFO_TYPE_AGENT, agentId)){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }
        return XxPayResponse.buildSuccess();
    }

    /** 查询代理商分佣比例设置信息 **/
    @RequestMapping("/getProfitSetInfo")
    public XxPayResponse getProfitSetInfo() {
        Long currentAgentId = getValLongRequired("agentId");
        return XxPayResponse.buildSuccess(getProfitSetInfo(currentAgentId));
    }

    //查询分佣比例设置条件
    private JSONObject getProfitSetInfo(Long currentAgentId){

        //判断越权操作
        AgentInfo agentInfo = rpc.rpcAgentInfoService.findByAgentId(currentAgentId);
        if(agentInfo == null || !agentInfo.getIsvId().equals(getUser().getBelongInfoId())){
            throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);
        }

        //1. 查询当前代理商分佣比例
        BigDecimal currentProfit = agentInfo.getProfitRate();

        //2. 查询上级代理商信息
        BigDecimal parentProfit = null;
        if(agentInfo.getPid() != null && agentInfo.getPid() > 0){
            AgentInfo pAgentInfo = rpc.rpcAgentInfoService.findByAgentId(agentInfo.getPid());
            if(pAgentInfo != null) parentProfit = pAgentInfo.getProfitRate();
        }

        //3. 查询下级最高分佣比例
        BigDecimal maxSubsProfit = rpc.rpcAgentInfoService.selectSubAgentsMaxProfitRate(currentAgentId);


        //一级80%   二级70%  三级60%
        // 下级最高 ≤ X ≤ 上级分佣比例
        JSONObject result = new JSONObject();
        result.put("currentAgentId", currentAgentId);  //当前代理商ID
        result.put("currentProfit", currentProfit);    //当前分佣比例
        result.put("parentProfit", parentProfit);   //上级分佣比例
        result.put("maxSubsProfit", maxSubsProfit);  //下级最高分佣比例
        return result;
    }

}