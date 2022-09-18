package org.xxpay.manage.agent.ctrl;

import com.alibaba.fastjson.JSON;
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
import org.xxpay.core.entity.FeeScale;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/05/06
 * @description: 代理商通道配置
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/agent_passage")
public class AgentPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long agentId = getValLongRequired( "agentId");

        List<FeeScale> agentPassageList = rpcCommonService.rpcFeeScaleService.
                productJoinFeeScale(MchConstant.INFO_TYPE_AGENT, agentId, "leftJoin", true);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentPassageList));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long agentId = getValLongRequired( "agentId");
        Integer productId = getValIntegerRequired( "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));

        FeeScale agentPassage = rpcCommonService.rpcFeeScaleService.getPayFeeByAgent(agentId, productId);
        if(agentPassage == null) {
            agentPassage = new FeeScale();
        }

        //赋值 - 支付产品名称
        agentPassage.setPsVal("productName", payProduct.getProductName());

        //赋值- 上级代理商 费率
        AgentInfo agentInfo = rpcCommonService.rpcAgentInfoService.findByAgentId(agentId);
        if(agentInfo.getPid() != null && agentInfo.getPid() != 0 ){
            FeeScale parentFeeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByAgent(agentInfo.getPid(), productId);
            if(parentFeeScale == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));
            agentPassage.setPsVal("parentAgentFee", parentFeeScale.getFee());
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentPassage));
    }

    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "配置代理商支付通道" )
    public ResponseEntity<?> addOrUpd() {


        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoType(MchConstant.INFO_TYPE_AGENT);  //代理商类型
        feeScale.setProductType(MchConstant.FEE_SCALE_PTYPE_PAY); //支付产品收费
        feeScale.setFeeScale(MchConstant.FEE_SCALE_SINGLE); //单笔收费
        feeScale.setSingleFeeType(MchConstant.FEE_SCALE_SINGLE_RATE); //每笔费率收费

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
