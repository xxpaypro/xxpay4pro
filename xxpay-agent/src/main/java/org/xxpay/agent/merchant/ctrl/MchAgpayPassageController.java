package org.xxpay.agent.merchant.ctrl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p><b>Title: </b>MchAgpayPassageController.java
 * <p><b>Description: </b>代理商配置商户代付通道 ctrl类
 * @author terrfly
 * @version V1.0
 * <p>
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/mch_agpay_passage")
public class MchAgpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

   
    /**
     * <p><b>Description: </b>查询商户代付通道列表
     * <p>2018年10月11日 下午2:24:08
     * @author terrfly
     * @param request
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {


        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
//
//        Long mchId = getValLongRequired("mchId");
//
//        Long currentAgentId = getUser().getBelongInfoId();;
//
//        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
//        if(mchInfo == null) {
//            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
//        }
//
//        //当前代理商已经配置的代付通道
//        List<AgentAgentpayPassage> agentAgentpayPassageList = rpcCommonService.rpcAgentAgentpayPassageService.selectAllByAgentId(currentAgentId);
//
//        //商户已经配置的代付通道
//        List<MchAgentpayPassage> mchAgentpayPassageList = rpcCommonService.rpcMchAgentpayPassageService.selectAllByMchId(mchId);
//
//
//        Map<Integer, AgentAgentpayPassage> agentAgentpayPassageMap = new HashMap<>();
//        for(AgentAgentpayPassage agentAgentpayPassage : agentAgentpayPassageList) {
//        	AgentpayPassage passage = rpcCommonService.rpcAgentpayPassageService.findById(agentAgentpayPassage.getAgentpayPassageId());
//        	agentAgentpayPassage.setPsVal("passageName", passage.getPassageName());
//            agentAgentpayPassageMap.put(agentAgentpayPassage.getAgentpayPassageId(), agentAgentpayPassage);
//        }
//
//        for(MchAgentpayPassage mchRecord : mchAgentpayPassageList) {
//        	AgentAgentpayPassage agentPassage = agentAgentpayPassageMap.get(mchRecord.getAgentpayPassageId());
//        	if(agentPassage != null){
//        		agentPassage.setPsVal("mchFeeEvery", mchRecord.getMchFeeEvery());
//        		agentPassage.setPsVal("mchStatus", mchRecord.getStatus());
//        		agentPassage.setPsVal("maxEveryAmount", mchRecord.getMaxEveryAmount());
//        		agentPassage.setPsVal("isDefault", mchRecord.getIsDefault());
//        	}
//        }
//
//        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentAgentpayPassageMap.values()));
    }

    /**
     * 查询商户代付 通道手续费等配置信息。
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

//
//        Long mchId = getValLongRequired("mchId");
//        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
//        if(mchInfo == null) {
//            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
//        }
//        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
//        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
//        if(agentpayPassage == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));
//
//        //当前代理商 代付通道配置信息
//        AgentAgentpayPassage agentAgentpayPassage = rpcCommonService.rpcAgentAgentpayPassageService.findByAgentIdAndPassageId(getUser().getBelongInfoId(), agentpayPassageId);
//
//        //商户配置信息
//        MchAgentpayPassage mchAgentpayPassage = rpcCommonService.rpcMchAgentpayPassageService.findByMchIdAndAgentpayPassageId(mchId, agentpayPassageId);
//
//        if(mchAgentpayPassage == null) {
//            mchAgentpayPassage = new MchAgentpayPassage();
//            mchAgentpayPassage.setMchId(mchId);
//            mchAgentpayPassage.setAgentpayPassageId(agentpayPassageId);
//            mchAgentpayPassage.setIsDefault(MchConstant.PUB_NO);
//            mchAgentpayPassage.setStatus(MchConstant.PUB_NO);
//        }
//        mchAgentpayPassage.setPsVal("passageName", agentpayPassage.getPassageName());
//        mchAgentpayPassage.setPsVal("agentFeeEvery", agentAgentpayPassage.getFeeEvery());
//
//        List<String> accountIdList = new ArrayList<>();
//        if(StringUtils.isNotEmpty(agentAgentpayPassage.getCanSetAccountId())){
//        	accountIdList = Arrays.asList(agentAgentpayPassage.getCanSetAccountId().split("##"));
//        }
//        JSONArray accountSelectData = new JSONArray();
//
//        for(String accountId : accountIdList){
//        	JSONObject obj = new JSONObject();
//        	obj.put("id", accountId);
//        	AgentpayPassageAccount account = rpcCommonService.rpcAgentpayPassageAccountService.findById(Integer.parseInt(accountId));
//        	obj.put("accountName", account.getAccountName());
//        	if(accountId.equals(mchAgentpayPassage.getAgentpayPassageAccountId() + "")){
//        		obj.put("selected", true);
//        	}
//        	accountSelectData.add(obj);
//        }
//        mchAgentpayPassage.setPsVal("accountSelectData", accountSelectData);
//        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayPassage));
    }

    /**
     * <p><b>Description: </b>修改/新增 商户代付 通道手续费、限额等配置。
     * <p>2018年10月11日 下午2:23:00
     * @author terrfly
     * @param request
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户代付配置" )
    public ResponseEntity<?> update() {

        //屏蔽代理商设置下级商户/代理商的通道配置
        if(true){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

//
//        // 将参数有字符元转为长整型分
//        handleParamAmount(param, "maxEveryAmount", "mchFeeEvery", "inFeeEvery", "onBtbInMaxEveryAmount", "onBtbInMinEveryAmount", "onBtcInMaxEveryAmount", "onBtcInMinEveryAmount");
//        MchAgentpayPassage mchAgentpayPassage = getObject( MchAgentpayPassage.class);
//        // 修改
//        int count;
//        if(mchAgentpayPassage.getId() != null) {
//            // 修改
//            mchAgentpayPassage.setMchFeeType((byte) 2);
//            if(mchAgentpayPassage.getAgentpayPassageAccountId() == null) mchAgentpayPassage.setAgentpayPassageAccountId(0);
//            if(mchAgentpayPassage.getIsDefault() == 1) {
//                // 修改该商户其他代付通道为非默认
//                MchAgentpayPassage updateMchAgentpayPassage1 = new MchAgentpayPassage();
//                updateMchAgentpayPassage1.setIsDefault(MchConstant.PUB_NO);
//                rpcCommonService.rpcMchAgentpayPassageService.updateByMchId(updateMchAgentpayPassage1, mchAgentpayPassage.getMchId());
//            }
//            count = rpcCommonService.rpcMchAgentpayPassageService.update(mchAgentpayPassage);
//        }else {
//            // 新增
//            mchAgentpayPassage.setMchFeeType((byte) 2);
//            if(mchAgentpayPassage.getIsDefault() == 1) {
//                // 修改该商户其他代付通道为非默认
//                MchAgentpayPassage updateMchAgentpayPassage1 = new MchAgentpayPassage();
//                updateMchAgentpayPassage1.setIsDefault(MchConstant.PUB_NO);
//                rpcCommonService.rpcMchAgentpayPassageService.updateByMchId(updateMchAgentpayPassage1, mchAgentpayPassage.getMchId());
//            }
//            count = rpcCommonService.rpcMchAgentpayPassageService.add(mchAgentpayPassage);
//        }
//        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
//        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
    
    
    
}
