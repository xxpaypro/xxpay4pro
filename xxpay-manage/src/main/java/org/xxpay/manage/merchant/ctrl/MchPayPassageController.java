package org.xxpay.manage.merchant.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
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
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author: dingzhiwei
 * @date: 18/05/05
 * @description: 商户支付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/mch_pay_passage")
public class MchPayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long mchId = getValLongRequired( "mchId");
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        }

        List<FeeScale> result = rpcCommonService.rpcFeeScaleService.productJoinFeeScale(MchConstant.INFO_TYPE_MCH, mchId, "leftJoin", true);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /**
     * 查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long mchId = getValLongRequired( "mchId");
        Integer productId = getValIntegerRequired( "productId");
        
        //校验信息合法性
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_SERVICE_MCH_NOT_EXIST));
        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productId);
        if(payProduct == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(null));

        //获取 数据载体对象
        FeeScale mchPayPassage = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchId, productId);

        //页面 查询商户/代理商 所有可配置支付通道
        if(mchPayPassage == null) {
        	mchPayPassage = new FeeScale();
        }
        mchPayPassage.setPsVal("productName", payProduct.getProductName());
        mchPayPassage.setPsVal("productType", payProduct.getProductType());

        //当存在上级代理商时
        if(mchInfo.getAgentId() != null){

            FeeScale parentFeeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByAgent(mchInfo.getAgentId(), productId);
            if(parentFeeScale == null) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));
            mchPayPassage.setPsVal("parentAgentFee", parentFeeScale.getFee());

        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchPayPassage));
    }

    @RequestMapping("/addOrUpd")
    @ResponseBody
    @MethodLog( remark = "配置商户支付通道" )
    public ResponseEntity<?> addOrUpd() {


        FeeScale feeScale = getObject( FeeScale.class);
        feeScale.setInfoType(MchConstant.INFO_TYPE_MCH);  //商户类型
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
