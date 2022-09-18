package org.xxpay.manage.config.ctrl;

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
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/05/05
 * @description: 代付通道账户
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/agentpay_passage_account")
public class AgentpayPassageAccountController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.count(agentpayPassageAccount);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentpayPassageAccount> agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.select((getPageIndex() -1) * getPageSize(), getPageSize(), agentpayPassageAccount);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassageAccount info : agentpayPassageAccountList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // 转换支付接口名称
            // TODO 列表中查询,数据量多时会因三方接口导致列表查询慢
            object.put("balance", queryBalance2(info.getIfTypeCode(), info.getParam()));
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Integer id = getValIntegerRequired( "id");
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentpayPassageAccount));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改代付通道子账户" )
    public ResponseEntity<?> update() {

        handleParamAmount( "maxDayAmount"); // 元转分
        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.update(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增代付通道子账户" )
    public ResponseEntity<?> add() {

        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST));
        }
        agentpayPassageAccount.setIfCode(agentpayPassage.getIfCode());            // 设置支付接口代码
        agentpayPassageAccount.setIfTypeCode(agentpayPassage.getIfTypeCode());    // 设置接口类型代码
        int count = rpcCommonService.rpcAgentpayPassageAccountService.add(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/balance")
    @ResponseBody
    public ResponseEntity<?> balance() {

        Integer id = getValIntegerRequired( "id");
        AgentpayPassageAccount agentpayPassageAccount = rpcCommonService.rpcAgentpayPassageAccountService.findById(id);
        String balanceStr = queryBalance(agentpayPassageAccount.getIfTypeCode(), agentpayPassageAccount.getParam());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(balanceStr));
    }

    String queryBalance(String channelType, String payParam) {
        AgentPayBalanceRetMsg resObj = rpcCommonService.rpcXxPayTransService.queryBalance(channelType, payParam);
        StringBuffer sb = new StringBuffer();
        if(resObj != null && resObj.getQueryState() == AgentPayBalanceRetMsg.QueryState.SUCCESS) {
            String cashBalance = AmountUtil.convertCent2Dollar(resObj.getCashBalance() + "");
            String payBalance = AmountUtil.convertCent2Dollar(resObj.getUsableBalance() + "");
            if(StringUtils.isNotBlank(cashBalance)) {
                sb.append("[现金余额:").append(cashBalance).append("元]");
            }
            if(StringUtils.isNotBlank(payBalance)) {
                sb.append("[可代付余额:").append(payBalance).append("元]");
            }
        }else {
            sb.append("查询失败");
        }
        return sb.toString();
    }

    String queryBalance2(String channelType, String payParam) {
        String result = "查询失败";
        try{
            AgentPayBalanceRetMsg resObj = rpcCommonService.rpcXxPayTransService.queryBalance(channelType, payParam);
            if(resObj != null && resObj.getQueryState() == AgentPayBalanceRetMsg.QueryState.SUCCESS) {
                return AmountUtil.convertCent2Dollar(resObj.getCashBalance() + ""); //账户余额, 分 -》 元
            }
        }catch (Exception e) {
        }
        return result;
    }


    /**
     * 根据passage查询子账户信息
     * @param request
     * @return
     */
    @RequestMapping("/get_account_list")
    @ResponseBody
    public ResponseEntity<?> getAccountList() {


        byte infoType = getValByteRequired( "infoType");
        Long infoId = getValLongRequired( "infoId");
        Integer passageId = getValIntegerRequired( "passageId");


        Long parentAgentId = null;
        if(MchConstant.INFO_TYPE_MCH == infoType) parentAgentId = rpcCommonService.rpcMchInfoService.findByMchId(infoId).getAgentId();
        if(MchConstant.INFO_TYPE_AGENT == infoType) parentAgentId = rpcCommonService.rpcAgentInfoService.findByAgentId(infoId).getPid();


        //查询所有通道
        AgentpayPassageAccount selectCondition = new AgentpayPassageAccount();
        selectCondition.setAgentpayPassageId(passageId);
        selectCondition.setStatus(MchConstant.PUB_YES);

        if(parentAgentId == null || parentAgentId == 0) { //平台商户 || 总代理商
        }else{

            FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getAgpayFeeByAgent(parentAgentId, passageId);
            if(feeScale == null)
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));

            JSONArray arr = JSON.parseObject(feeScale.getExtConfig()).getJSONArray("visibleAccList");
            List<Integer> ids =  new ArrayList<>();
            for(Object item : arr){
                String itemStr = item.toString();
                ids.add(Integer.parseInt(itemStr.substring(2, itemStr.length() - 1)));
            }

            selectCondition.setPsVal("ids", ids);

        }


        List<AgentpayPassageAccount> result = rpcCommonService.rpcAgentpayPassageAccountService.selectAccBaseInfo(selectCondition);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }


}
