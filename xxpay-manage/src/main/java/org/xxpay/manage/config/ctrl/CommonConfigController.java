package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 18/1/17
 * @description: 通用配置
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/common")
public class CommonConfigController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 所有支付类型列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_type_all")
    @ResponseBody
    public ResponseEntity<?> payTypeaAll() {

        PayType payType = getObject( PayType.class);
        List<PayType> payTypeList = rpcCommonService.rpcPayTypeService.selectAll(payType);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payTypeList));
    }

    /**
     * 所有支付产品列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_product_all")
    @ResponseBody
    public ResponseEntity<?> payProductAll() {
        PayProduct payProduct = new PayProduct();
        payProduct.setStatus(MchConstant.PUB_YES);
        List<PayProduct> payProductList = rpcCommonService.rpcPayProductService.selectAll(payProduct);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payProductList));
    }

    /**
     * 所有支付接口类型列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_interface_type_all")
    @ResponseBody
    public ResponseEntity<?> payInterfaceTypeAll() {

        PayInterfaceType payInterfaceType = getObject( PayInterfaceType.class);
        List<PayInterfaceType> payInterfaceTypeList = rpcCommonService.rpcPayInterfaceTypeService.selectAll(payInterfaceType);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payInterfaceTypeList));
    }

    /**
     * 所有支付接口列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_interface_all")
    @ResponseBody
    public ResponseEntity<?> payInterfaceAll() {

        PayInterface payInterface = getObject( PayInterface.class);
        List<PayInterface> payInterfaceList = rpcCommonService.rpcPayInterfaceService.selectAll(payInterface);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payInterfaceList));
    }

    /**
     * 所有支付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_all")
    @ResponseBody
    public ResponseEntity<?> payPassageAll() {

        PayPassage payPassage = getObject( PayPassage.class);
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.selectAll(payPassage);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payPassageList));
    }

    /**
     * 所有代付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/agentpay_passage_all")
    @ResponseBody
    public ResponseEntity<?> agentpayPassageAll() {

        AgentpayPassage agentpayPassage = getObject( AgentpayPassage.class);
        agentpayPassage.setStatus(MchConstant.PUB_YES);
        List<AgentpayPassage> agentpayPassageList = rpcCommonService.rpcAgentpayPassageService.selectAllPlatPassage(agentpayPassage);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentpayPassageList));
    }

    /**
     * 根据产品ID得到对应的支付通道列表
     * @param request
     * @return
     */
    @RequestMapping("/pay_passage_product")
    @ResponseBody
    public ResponseEntity<?> payPassage4ProductId() {

        Integer productId = getValInteger( "productId");
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));
        }
        String payType = payProduct.getPayType();
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.selectPlatAllByPayType(payType);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payPassageList));
    }


    /**
     * 根据代付通道ID得到子账户列表
     * @param request
     * @return
     */
    @RequestMapping("/agentpay_passage_account")
    @ResponseBody
    public ResponseEntity<?> agentpayPassageAccount4PassageId() {

        Integer agentpayPassageId = getValInteger( "agentpayPassageId");
        List<AgentpayPassageAccount> agentpayPassageAccountList = new LinkedList<>();
        if(agentpayPassageId != null) {
            agentpayPassageAccountList = rpcCommonService.rpcAgentpayPassageAccountService.selectAllByPassageId(agentpayPassageId);
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(agentpayPassageAccountList));
    }

}
