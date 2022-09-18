package org.xxpay.manage.merchant.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.AgentpayPassageAccount;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.MchInfo;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 18/05/05
 * @description: 商户代付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_agentpay_passage")
public class MchAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));

        List<FeeScale> result =
                rpcCommonService.rpcFeeScaleService.agpayPassageJoinFeeScale(MchConstant.INFO_TYPE_MCH, mchId, "leftJoin", true);

        //列表显示 风控设置金额属性
        for(FeeScale f: result){
            f.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(f.getId()));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        
        //验证查询参数合法性
        Long mchId = getValLongRequired( "mchId");
        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));
        
        //获取数据载体
        FeeScale mchAgentpayPassage = rpcCommonService.rpcFeeScaleService.getAgpayFeeByMch(mchId, agentpayPassageId);
        if(mchAgentpayPassage == null) {
            mchAgentpayPassage = new FeeScale();
        }

        mchAgentpayPassage.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(mchAgentpayPassage.getId()));
        
        mchAgentpayPassage.setPsVal("passageName", agentpayPassage.getPassageName()); //代付通道名称
        mchAgentpayPassage.setPsVal("passageFee", agentpayPassage.getFeeEvery()); //代付通道费用
        
        //代理商 可见通道子账户列表
        if(mchInfo.getAgentId() != null) {

            FeeScale parentFeeScale = rpcCommonService.rpcFeeScaleService.getAgpayFeeByAgent(mchInfo.getAgentId(), agentpayPassageId);
            if(parentFeeScale == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));
            mchAgentpayPassage.setPsVal("parentAgentFee", parentFeeScale.getFee());
        }
        
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayPassage));
    }

    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "配置商户代付通道" )
    public ResponseEntity<?> addOrUpd() {

        // 将参数有字符元转为长整型分
        handleParamAmount( "fee", "feeRiskConfig.maxEveryAmount");

        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoType(MchConstant.INFO_TYPE_MCH);  //商户类型
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
