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

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


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
    @MethodLog( remark = "私有商户修改代付通道" )
    public ResponseEntity<?> update() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        handleParamAmount( "maxDayAmount"); // 元转分
        AgentpayPassageAccount agentpayPassageAccount = getObject( AgentpayPassageAccount.class);
        int count = rpcCommonService.rpcAgentpayPassageAccountService.update(agentpayPassageAccount);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "私有商户新增代付通道" )
    public ResponseEntity<?> add() {
//
//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


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

//        // 校验当前商户是否为私有账户
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
            _log.error("", e);
        }
        return result;
    }


    /**
     * 根据passage查询子账户信息
     * @return
     */
    @RequestMapping("/get_account_list")
    @ResponseBody
    public ResponseEntity<?> getAccountList() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));

        long mchId = getUser().getBelongInfoId();

        Integer passageId = getValIntegerRequired( "passageId");

        AgentpayPassage agentpayPassage = rpcCommonService.rpcAgentpayPassageService.findById(passageId);
        //判断是否越权查看/操作
        if(agentpayPassage.getBelongInfoType() != MchConstant.INFO_TYPE_MCH || agentpayPassage.getBelongInfoId() != mchId) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        //查询所有通道
        AgentpayPassageAccount selectCondition = new AgentpayPassageAccount();
        selectCondition.setAgentpayPassageId(passageId);
        selectCondition.setStatus(MchConstant.PUB_YES);

        List<AgentpayPassageAccount> result = rpcCommonService.rpcAgentpayPassageAccountService.selectAccBaseInfo(selectCondition);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }

}
