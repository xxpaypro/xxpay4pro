package org.xxpay.manage.agent.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AgentInfo;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.AgentpayPassageAccount;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/08/30
 * @description: 代理商代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_agentpay_passage")
public class AgentAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long agentId = getValLongRequired( "agentId");

        List<FeeScale> result = rpcCommonService.rpcFeeScaleService.
                agpayPassageJoinFeeScale(MchConstant.INFO_TYPE_AGENT, agentId, "leftJoin", true);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long agentId = getValLongRequired( "agentId");
        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));

        FeeScale agentPassage = rpcCommonService.rpcFeeScaleService.getAgpayFeeByAgent(agentId, agentpayPassageId);
        if(agentPassage == null) {
            agentPassage = new FeeScale();
        }

        agentPassage.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(agentPassage.getId()));

        //赋值 - 支付产品名称
        agentPassage.setPsVal("passageName", agentpayPassage.getPassageName());
        agentPassage.setPsVal("passageFee", agentpayPassage.getFeeEvery());

        //赋值- 上级代理商 费率
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        if(agentInfo.getPid() != null && agentInfo.getPid() != 0 ){
            FeeScale parentFeeScale = rpcCommonService.rpcFeeScaleService.getAgpayFeeByAgent(agentInfo.getPid(), agentpayPassageId);
            if(parentFeeScale == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));
            agentPassage.setPsVal("parentAgentFee", parentFeeScale.getFee());
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentPassage));

    }

    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "配置代理商代付通道" )
    public ResponseEntity<?> addOrUpd() {

        // 将参数有字符元转为长整型分
        handleParamAmount( "fee", "feeRiskConfig.maxEveryAmount");

        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoType(MchConstant.INFO_TYPE_AGENT);  //代理商类型
        feeScale.setProductType(MchConstant.FEE_SCALE_PTYPE_AGPAY); //代付产品收费
        feeScale.setFeeScale(MchConstant.FEE_SCALE_SINGLE); //单笔收费
        feeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE_FIX); //每笔固定收费

        int count;
        if(feeScale.getId() != null) {
            count = rpcCommonService.rpcFeeScaleService.update(feeScale);
        }else {
            count = rpcCommonService.rpcFeeScaleService.add(feeScale);
        }
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
