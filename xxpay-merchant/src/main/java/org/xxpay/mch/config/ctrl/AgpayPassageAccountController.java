package org.xxpay.mch.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.domain.api.msg.AgentPayBalanceRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.AgentpayPassage;
import org.xxpay.core.entity.AgentpayPassageAccount;
import org.xxpay.core.entity.FeeScale;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.config.service.CommonConfigService;
import org.xxpay.mch.paydemo.ctrl.PaydemoController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/agpay_passage_account")
@PreAuthorize("hasRole('" + MchConstant.MCH_ROLE_NORMAL + "')")
public class AgpayPassageAccountController extends BaseController {

    private static final Logger _log = LoggerFactory.getLogger(AgpayPassageAccountController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

//        // ???????????????????????????????????????
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.count(agentpayPassageAccount);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<AgentpayPassageAccount> agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.select((getPageIndex() -1) * getPageSize(), getPageSize(), agentpayPassageAccount);
        // ??????????????????Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // ????????????Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // ??????????????????
        List<JSONObject> objects = new LinkedList<>();
        for(AgentpayPassageAccount info : agentpayPassageAccountList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // ????????????????????????
            object.put("ifName", payInterfaceMap.get(info.getIfCode()));                // ????????????????????????
            // TODO ???????????????,??????????????????????????????????????????????????????
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
    @MethodLog( remark = "??????????????????????????????" )
    public ResponseEntity<?> update() {

//        // ???????????????????????????????????????
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        handleParamAmount( "maxDayAmount"); // ?????????
        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.update(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "??????????????????????????????" )
    public ResponseEntity<?> add() {
//
//        // ???????????????????????????????????????
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        Integer agentpayPassageId = getValIntegerRequired( "agentpayPassageId");
        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(agentpayPassageId);
        if(agentpayPassage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_PASSAGE_NOT_EXIST));
        }
        agentpayPassageAccount.setIfCode(agentpayPassage.getIfCode());            // ????????????????????????
        agentpayPassageAccount.setIfTypeCode(agentpayPassage.getIfTypeCode());    // ????????????????????????
        int count = rpcCommonService.rpcAgentpayPassageAccountService.add(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/balance")
    @ResponseBody
    public ResponseEntity<?> balance() {

//        // ???????????????????????????????????????
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


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
                sb.append("[????????????:").append(cashBalance).append("???]");
            }
            if(StringUtils.isNotBlank(payBalance)) {
                sb.append("[???????????????:").append(payBalance).append("???]");
            }
        }else {
            sb.append("????????????");
        }
        return sb.toString();
    }

    String queryBalance2(String channelType, String payParam) {
        String result = "????????????";
        try{
            AgentPayBalanceRetMsg resObj = rpcCommonService.rpcXxPayTransService.queryBalance(channelType, payParam);
            if(resObj != null && resObj.getQueryState() == AgentPayBalanceRetMsg.QueryState.SUCCESS) {
                return AmountUtil.convertCent2Dollar(resObj.getCashBalance() + ""); //????????????, ??? -??? ???
            }
        }catch (Exception e) {
            _log.error("", e);
        }
        return result;
    }


    /**
     * ??????passage?????????????????????
     * @return
     */
    @RequestMapping("/get_account_list")
    @ResponseBody
    public ResponseEntity<?> getAccountList() {

//        // ???????????????????????????????????????
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));

        long mchId = getUser().getBelongInfoId();

        Integer passageId = getValIntegerRequired( "passageId");

        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(passageId);
        //????????????????????????/??????
        if(agentpayPassage.getBelongInfoType() != MchConstant.INFO_TYPE_MCH || agentpayPassage.getBelongInfoId() != mchId) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        //??????????????????
        AgentpayPassageAccount selectCondition = new AgentpayPassageAccount();
        selectCondition.setAgentpayPassageId(passageId);
        selectCondition.setStatus(MchConstant.PUB_YES);

        List<AgentpayPassageAccount> result = rpcCommonService.rpcAgentpayPassageAccountService.selectAccBaseInfo(selectCondition);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }

}
