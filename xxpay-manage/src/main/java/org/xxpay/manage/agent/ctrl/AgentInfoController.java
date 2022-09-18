package org.xxpay.manage.agent.ctrl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MD5Util;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_info")
public class AgentInfoController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询代理商信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long agentId = getValLongRequired( "agentId");
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        agentInfo.setPsVal("offRechargeFee", rpcCommonService.rpcFeeScaleService.getOffRechargeFeeByAgent(agentId));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentInfo));
    }

    /**
     * 新增代理商信息
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代理商" )
    public ResponseEntity<?> add() {

        AgentInfo agentInfo = getObject( AgentInfo.class);

        //默认状态为 可用。
        if(agentInfo.getStatus() == null){
            agentInfo.setStatus(MchConstant.PUB_YES);
        }

        //TODO 修改商户表结构0910

//        String rawPassword = agentInfo.getPassword();
//        if(StringUtils.isEmpty(rawPassword)){
//            rawPassword = MchConstant.MCH_DEFAULT_PASSWORD;;
//        }
//
//        // 设置默认登录密码
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        agentInfo.setPassword(encoder.encode(rawPassword));
//
//        agentInfo.setLastPasswordResetTime(new Date());
//
//        // 设置默认支付密码
//        String payPassword = MchConstant.MCH_DEFAULT_PAY_PASSWORD;
//        agentInfo.setPayPassword(MD5Util.string2MD5(payPassword));
//
//        // 确认用户名不能重复
//        if(rpcCommonService.rpcAgentInfoService.findByUserName(agentInfo.getUserName()) != null) {
//            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_AGENT_USERNAME_USED));
//        }
//        // 确认手机不能重复
//        if(agentInfo.getMobile() != null && rpcCommonService.rpcAgentInfoService.findByMobile(agentInfo.getMobile()) != null) {
//            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
//        }
//        // 确认邮箱不能重复
//        if(rpcCommonService.rpcAgentInfoService.findByEmail(agentInfo.getEmail()) != null) {
//            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
//        }
        agentInfo.setPid(0L); //父ID为0 


        int count = rpcCommonService.rpcAgentInfoService.add(agentInfo);
        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改代理商信息
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代理商" )
    public ResponseEntity<?> update() {

        Long agentId = getValLongRequired( "agentId");
        AgentInfo agentInfo = getObject( AgentInfo.class);
        AgentInfo dbAgentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        if(dbAgentInfo == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
//
//        if(agentInfo.getMobile() != null && agentInfo.getMobile().longValue() != dbAgentInfo.getMobile().longValue()) {
//            // 确认手机不能重复
//            if(rpcCommonService.rpcAgentInfoService.findByMobile(agentInfo.getMobile()) != null) {
//                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_MOBILE_USED));
//            }
//        }
//
//        if(agentInfo.getEmail() != null && !agentInfo.getEmail().equalsIgnoreCase(dbAgentInfo.getEmail())) {
//            // 确认邮箱不能重复
//            if(rpcCommonService.rpcAgentInfoService.findByEmail(agentInfo.getEmail()) != null) {
//                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_EMAIL_USED));
//            }
//        }

        //TODO 修改商户表结构0910

        // 商户信息只允许修改手机号,邮箱,费率,结算方式,状态,其他不允许修改
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(agentId);
        updateAgentInfo.setAgentName(agentInfo.getAgentName());
//        updateAgentInfo.setMobile(agentInfo.getMobile());
//        updateAgentInfo.setEmail(agentInfo.getEmail());
        updateAgentInfo.setStatus(agentInfo.getStatus());
        updateAgentInfo.setRealName(agentInfo.getRealName());
        updateAgentInfo.setIdCard(agentInfo.getIdCard());
        updateAgentInfo.setQq(agentInfo.getQq());
        updateAgentInfo.setAddress(agentInfo.getAddress());
        updateAgentInfo.setPs(agentInfo.getPs());


        
        updateAgentInfo.setAllowAddSubAgent(agentInfo.getAllowAddSubAgent());
        updateAgentInfo.setAllowAddMch(agentInfo.getAllowAddMch());
//        // 如果登录密码不为空,则修改登录密码
//        String rawPassword = agentInfo.getPassword();
//        if(StringUtils.isNotBlank(rawPassword)) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            // 判断新登录密码格式
//            if(!StrUtil.checkPassword(rawPassword)) {
//                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
//            }
//            updateAgentInfo.setPassword(encoder.encode(rawPassword));
//            updateAgentInfo.setLastPasswordResetTime(new Date());
//        }


//        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
//        if(count != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改结算设置
     * @return
     */
    @RequestMapping("/sett_update")
    @ResponseBody
    @MethodLog( remark = "修改代理商结算信息" )
    public ResponseEntity<?> updateSett() {

        handleParamAmount( "drawMaxDayAmount", "maxDrawAmount", "minDrawAmount", "drawFeeLimit");
        Long agentId = getValLongRequired( "agentId");
        AgentInfo agentInfo = getObject( AgentInfo.class);
        // 修改结算设置信息
        AgentInfo updateAgentInfo = new AgentInfo();
        updateAgentInfo.setAgentId(agentId);

//        int count = rpcCommonService.rpcAgentInfoService.update(updateAgentInfo);
//        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }    

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        AgentInfo agentInfo = getObject(AgentInfo.class);
        long count = rpcCommonService.rpcAgentInfoService.count(agentInfo);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentInfo> agentInfoList = rpcCommonService.rpcAgentInfoService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), agentInfo);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(agentInfoList, count));
    }

    /**
     * 重置商户密码
     * @return
     */
    @RequestMapping("/pwd_reset")
    @ResponseBody
    @MethodLog( remark = "重置代理商密码" )
    public ResponseEntity<?> resetPwd() {

        Long agentId = getValLongRequired( "agentId");
        String rawPassword = getValStringRequired( "password");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 判断新密码格式
        if(!StrUtil.checkPassword(rawPassword)) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_PASSWORD_FORMAT_FAIL));
        }
        AgentInfo agentInfo = new AgentInfo();
        agentInfo.setAgentId(agentId);
//        agentInfo.setPassword(encoder.encode(rawPassword));
//        agentInfo.setLastPasswordResetTime(new Date());

        //TODO 修改商户表结构0910

//        int count = rpcCommonService.rpcAgentInfoService.update(agentInfo);
//        if(count != 1) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }
   
    /**
     * <p><b>Description: </b>生成代理商登录url
     * <p>2018年10月26日 上午10:22:56
     * @author terrfly
     * @return
     */
    @RequestMapping("/genLoginUrl")
    @ResponseBody
    public ResponseEntity<?> genLoginUrl() {
    	

        Long agentId = getValLongRequired( "agentId");
    	
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        // 将商户ID+代理商登录名+密钥 做32位MD5加密转大写,作为token传递给商户系统
        String secret = "Abc%$G&!!!128G";
        
		String signTime = DateUtil.date2Str(new Date(), "ddHHmm");
        String rawToken = agentInfo.getAgentId() + agentInfo.getLoginUserName() + secret + signTime;
        String tokenMD5 = MD5Util.string2MD5(rawToken).toUpperCase();
        String loginAgentUrl = mainConfig.getLoginAgentUrl();
        
        JSONObject json = new JSONObject();
        json.put("loginUrl", String.format(loginAgentUrl, agentInfo.getAgentId(), tokenMD5));
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));
    }


}