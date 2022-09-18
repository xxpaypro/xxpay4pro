package org.xxpay.agent.merchant.ctrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <p><b>Title: </b>MchPayPassageController.java
 * <p><b>Description: </b>代理商配置商户支付通道 ctrl类
 * @author terrfly
 * @version V1.0
 * <p>
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/mch_pay_passage")
public class MchPayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询
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
//        Integer productId = getValIntegerRequired( "productId");
//        AgentPassage agentPassage = null;
//        if(mchInfo.getAgentId() != null) {
//            agentPassage = rpcCommonService.rpcAgentPassageService.findByAgentIdAndProductId(mchInfo.getAgentId(), productId);
//        }
//        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
//        if(payProduct == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));
//        MchPayPassage mchPayPassage = rpcCommonService.rpcMchPayPassageService.findByMchIdAndProductId(mchId, productId);
//        if(mchPayPassage == null) {
//            mchPayPassage = new MchPayPassage();
//            mchPayPassage.setMchId(mchId);
//            mchPayPassage.setProductId(productId);
//            mchPayPassage.setProductType(payProduct.getProductType());
//            mchPayPassage.setMchRate(payProduct.getMchRate());
//            mchPayPassage.setIfMode(payProduct.getIfMode() == null ? ((byte) 1) : payProduct.getIfMode());
//            mchPayPassage.setPayPassageId(payProduct.getPayPassageId());
//            mchPayPassage.setPayPassageAccountId(payProduct.getPayPassageAccountId());
//            mchPayPassage.setPollParam(payProduct.getPollParam());
//            mchPayPassage.setStatus(MchConstant.PUB_NO);
//        }
//        JSONObject object = (JSONObject) JSON.toJSON(mchPayPassage);
//        object.put("productName", payProduct.getProductName());
//        if(agentPassage != null) {
//            object.put("agentRate", agentPassage.getAgentRate());
//        }else if(payProduct.getAgentRate() != null) {
//            object.put("agentRate", payProduct.getAgentRate());
//        }else {
//            object.put("agentRate", "");
//        }
//        object.put("agentId", mchInfo.getAgentId() == null ? "" : mchInfo.getAgentId());
//        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改商户支付配置" )
    public ResponseEntity<?> update() {

        //屏蔽代理商设置下级商户/代理商的通道配置
        if(true){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

//
//        MchPayPassage mchPayPassage = getObject( MchPayPassage.class);
//        int count;
//        if(mchPayPassage.getId() != null) {
//            // 修改
//            if(mchPayPassage.getIfMode() == 1) { // 单独
//                mchPayPassage.setPollParam(null);
//                if(mchPayPassage.getPayPassageAccountId() == null) mchPayPassage.setPayPassageAccountId(0);
//            }else if(mchPayPassage.getIfMode() == 2) { // 轮询
//                mchPayPassage.setPayPassageId(null);
//                mchPayPassage.setPayPassageAccountId(null);
//            }
//            count = rpcCommonService.rpcMchPayPassageService.update(mchPayPassage);
//        }else {
//            // 新增
//            if(mchPayPassage.getIfMode() == 1) { // 单独
//                mchPayPassage.setPollParam(null);
//            }else if(mchPayPassage.getIfMode() == 2) { // 轮询
//                mchPayPassage.setPayPassageId(null);
//                mchPayPassage.setPayPassageAccountId(null);
//            }
//            count = rpcCommonService.rpcMchPayPassageService.add(mchPayPassage);
//        }
//        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
//        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
