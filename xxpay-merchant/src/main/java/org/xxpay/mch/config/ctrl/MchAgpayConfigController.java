package org.xxpay.mch.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/mch_agpay_config")
@PreAuthorize("hasRole('" + MchConstant.MCH_ROLE_NORMAL + "')")
public class MchAgpayConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));

        Long mchId = getUser().getBelongInfoId();
        //左连接 查询
        List<FeeScale> record = rpcCommonService.rpcFeeScaleService.agpayPassageJoinFeeScale(MchConstant.INFO_TYPE_MCH, mchId, "leftJoin", false);

        for(FeeScale feeScale: record){
            feeScale.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(feeScale.getId()));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(record));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        
        //验证查询参数合法性
        long mchId = getUser().getBelongInfoId();
        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");

        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_AGENTPAY_PASSAGE_NOT_EXIST));
        }

        //判断是否越权查看/操作
        if(agentpayPassage.getBelongInfoType() != MchConstant.INFO_TYPE_MCH || agentpayPassage.getBelongInfoId() != mchId){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        //获取数据载体
        FeeScale mchAgentpayPassage = rpcCommonService.rpcFeeScaleService.getAgpayFeeByMch(mchId, agentpayPassageId);
        if(mchAgentpayPassage == null) {
            mchAgentpayPassage = new FeeScale();
        }

        mchAgentpayPassage.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(mchAgentpayPassage.getId()));
        
        mchAgentpayPassage.setPsVal("passageName", agentpayPassage.getPassageName()); //代付通道名称
        mchAgentpayPassage.setPsVal("passageFee", agentpayPassage.getFeeEvery()); //代付通道费用

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchAgentpayPassage));
    }

    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "私有商户配置代付通道" )
    public ResponseEntity<?> addOrUpd() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        handleParamAmount( "feeRiskConfig.maxEveryAmount");
        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoId(getUser().getBelongInfoId());
        feeScale.setInfoType(MchConstant.INFO_TYPE_MCH);  //商户类型
        feeScale.setProductType(MchConstant.FEE_SCALE_PTYPE_AGPAY); //代付产品收费

        //获取私有账户的全局配置，赋值到对应的收费产品中。
        FeeScale templet = rpcCommonService.rpcFeeScaleService.getAgpayFeeByMch(getUser().getBelongInfoId(), 0);
        if(templet == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_FEE_TEMPLET_NOT_EXISTS));

        feeScale.setFeeScale(templet.getFeeScale());
        feeScale.setFeeScaleStep(templet.getFeeScaleStep());
        feeScale.setSingleFeeType(templet.getSingleFeeType());
        feeScale.setFee(templet.getFee());

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
