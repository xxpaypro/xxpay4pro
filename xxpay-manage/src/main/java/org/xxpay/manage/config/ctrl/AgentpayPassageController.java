package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/05/05
 * @description: 代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/agentpay_passage")
public class AgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        AgentpayPassage agentpayPassage = getObject( AgentpayPassage.class);
        agentpayPassage.setBelongInfoId(0L);
        agentpayPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        int count = rpcCommonService.rpcAgentpayPassageService.count(agentpayPassage);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.select((getPageIndex() -1) * getPageSize(), getPageSize(), agentpayPassage);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();

        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassage info : agentpayPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Integer id = getValIntegerRequired( "id");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentpayPassage));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道" )
    public ResponseEntity<?> update() {

        int id = getValInteger("id");
        AgentpayPassage agentpayPassage = new AgentpayPassage();
        agentpayPassage.setId(id);
        String remark = getValString( "remark");
        if(StringUtils.isNotEmpty(remark)) agentpayPassage.setRemark(remark);

        String passageName = getValString( "passageName");
        if(StringUtils.isNotEmpty(passageName)) agentpayPassage.setPassageName(passageName);

        agentpayPassage.setStatus(getValByteRequired( "status"));
        String ifCode = getValString( "ifCode");
        if(StringUtils.isNotEmpty(ifCode)){
            PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
            if(payInterface == null) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
            }
            agentpayPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型
        }

        Byte feeType = getValByte( "feeType");
        if(feeType != null){
            if(feeType == 1) {
                agentpayPassage.setFeeEvery(null);
            }else if(feeType == 2){
                Long feeEvery = getRequiredAmountL("feeEvery");
                agentpayPassage.setFeeEvery(feeEvery);
                agentpayPassage.setFeeRate(null);
            }
        }

        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/risk_update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道风控" )
    public ResponseEntity<?> updateRisk() {

        handleParamAmount( "maxDayAmount"); // 元转分
        AgentpayPassage agentpayPassage = getObject( AgentpayPassage.class);
        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/rate_update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道费率" )
    public ResponseEntity<?> updateRate() {

        Integer id = getValIntegerRequired( "id");
        Byte feeType = getValByteRequired( "feeType");
        AgentpayPassage agentpayPassage = new AgentpayPassage();
        agentpayPassage.setId(id);
        agentpayPassage.setFeeType(feeType);
        if(feeType == 1) {
            String feeRate = getValStringRequired( "feeRate");
            agentpayPassage.setFeeRate(new BigDecimal(feeRate));
        }else if(feeType == 2) {
            Long feeEvery = getValLongRequired( "feeEvery");
            agentpayPassage.setFeeEvery(feeEvery * 100);
        }
        int count = rpcCommonService.rpcAgentpayPassageService.update(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代付通道" )
    public ResponseEntity<?> add() {

        AgentpayPassage agentpayPassage = getObject( AgentpayPassage.class);
        agentpayPassage.setBelongInfoId(0L);
        agentpayPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);

        String ifCode = agentpayPassage.getIfCode();
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface == null) {
            ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_INTERFACE_NOT_EXIST));
        }
        agentpayPassage.setIfTypeCode(payInterface.getIfTypeCode()); // 设置支付接口类型
        if(agentpayPassage.getFeeType() == 1) {
            agentpayPassage.setFeeEvery(null);
        }else if(agentpayPassage.getFeeType() == 2){
            Long feeEvery = getValLongRequired( "feeEvery");
            agentpayPassage.setFeeEvery(feeEvery * 100);
            agentpayPassage.setFeeRate(null);
        }
        int count = rpcCommonService.rpcAgentpayPassageService.add(agentpayPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 根据代付通道ID,获取支付账号配置定义描述
     * @return
     */
    @RequestMapping("/pay_config_get")
    @ResponseBody
    public ResponseEntity<?> getPayConfig() {

        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        String ifCode = agentpayPassage.getIfCode();
        String ifTypeCode = agentpayPassage.getIfTypeCode();

        // 如果接口配置了定义描述,则使用接口
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface != null && StringUtils.isNotBlank(payInterface.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterface);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterface.getIfTypeCode()));
            return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
        }
        // 使用接口类型配置的定义描述
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        if(payInterfaceType != null && StringUtils.isNotBlank(payInterfaceType.getMchParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterfaceType);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterfaceType.getIfTypeCode()));
            return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
        }
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
    }



}
