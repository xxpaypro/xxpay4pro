package org.xxpay.agent.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.FeeScale;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/09/13
 * @description: 代理商代付通道配置
 */
@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/agent_agentpay_passage")
public class AgentAgentpayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        Long agentId = getUser().getBelongInfoId();
        // 得到代理商已经配置的代付通道
        List<FeeScale> agentAgentpayPassageList = rpcCommonService.rpcFeeScaleService.selectAll(MchConstant.INFO_TYPE_AGENT, agentId, MchConstant.FEE_SCALE_PTYPE_AGPAY);
        // 得到所有代付通道
        AgentpayPassage queryAgentpayPassage = new AgentpayPassage();
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAllPlatPassage(queryAgentpayPassage);
        Map<String, AgentpayPassage> agentpayPassageMap = new HashMap<>();
        for(AgentpayPassage agentpayPassage : agentpayPassageList) {
            agentpayPassageMap.put(String.valueOf(agentpayPassage.getId()), agentpayPassage);
        }
        List<JSONObject> objects = new LinkedList<>();
        for(FeeScale agentAgentpayPassage : agentAgentpayPassageList) {
            AgentpayPassage ap = agentpayPassageMap.get(String.valueOf(agentAgentpayPassage.getRefProductId()));
            // 如果通道已关闭,则不显示
            if(ap.getStatus() != MchConstant.PUB_YES) continue;
            JSONObject object = (JSONObject) JSON.toJSON(agentAgentpayPassage);
            object.put("passageName", ap.getPassageName());
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(objects));
    }

}
