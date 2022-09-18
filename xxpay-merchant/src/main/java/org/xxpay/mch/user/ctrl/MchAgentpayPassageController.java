package org.xxpay.mch.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/05/05
 * @description: 商户代付通道
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/mch_agentpay_passage")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        Long mchId = getUser().getBelongInfoId();

        // 得到商户已经配置的代付通道
        List<FeeScale> result = rpcCommonService.rpcFeeScaleService.agpayPassageJoinFeeScale(MchConstant.INFO_TYPE_MCH, mchId, "innerJoin", false);

        for(FeeScale fee: result){
            fee.setFeeRiskConfig(rpcCommonService.rpcFeeRiskConfigService.findById(fee.getId()));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

}
