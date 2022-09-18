package org.xxpay.mch.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;
import org.xxpay.mch.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_passage")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class PayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        PayPassage payPassage = getObject( PayPassage.class);
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
        payPassage.setBelongInfoId(getUser().getBelongInfoId());
        int count = rpcCommonService.rpcPayPassageService.count(payPassage);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayPassage> payPassageList = rpcCommonService.rpcPayPassageService.select((getPageIndex() -1) * getPageSize(), getPageSize(), payPassage);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付接口Map
        Map payInterfaceMap = commonConfigService.getPayInterfaceMap();
        // 支付类型Map
        Map payTypeMap = commonConfigService.getPayTypeMap();

        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(PayPassage info : payPassageList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));    // 转换接口类型名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        Integer id = getValIntegerRequired( "id");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(id);

        //判断越权查询
        if(payPassage == null ||  !payPassage.getBelongInfoType().equals(MchConstant.INFO_TYPE_MCH) || !payPassage.getBelongInfoId().equals(getUser().getBelongInfoId()) ){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(payPassage));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "私有商户修改支付通道" )
    public ResponseEntity<?> update() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        PayPassage payPassage = getObject( PayPassage.class);

        //判断越权查询
        PayPassage selectRecord = rpcCommonService.rpcPayPassageService.findById(payPassage.getId());
        if(selectRecord == null ||  !selectRecord.getBelongInfoType().equals(MchConstant.INFO_TYPE_MCH) || !selectRecord.getBelongInfoId().equals(getUser().getBelongInfoId()) ){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }
        int count = rpcCommonService.rpcPayPassageService.update(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/risk_update")
    @ResponseBody
    @MethodLog( remark = "私有商户修改支付风控规则" )
    public ResponseEntity<?> updateRisk() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        PayPassage payPassage = getObject( PayPassage.class);

        //判断越权查询
        PayPassage selectRecord = rpcCommonService.rpcPayPassageService.findById(payPassage.getId());
        if(selectRecord == null ||  !selectRecord.getBelongInfoType().equals(MchConstant.INFO_TYPE_MCH) || !selectRecord.getBelongInfoId().equals(getUser().getBelongInfoId()) ){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        Long maxDayAmount = payPassage.getMaxDayAmount();
        Long maxEveryAmount = payPassage.getMaxEveryAmount();
        Long minEveryAmount = payPassage.getMinEveryAmount();
        // 元转分
        if(maxDayAmount != null) payPassage.setMaxDayAmount(maxDayAmount * 100);
        if(maxEveryAmount != null) payPassage.setMaxEveryAmount(maxEveryAmount * 100);
        if(minEveryAmount != null) payPassage.setMinEveryAmount(minEveryAmount * 100);
        int count = rpcCommonService.rpcPayPassageService.update(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/rate_update")
    @ResponseBody
    @MethodLog( remark = "私有商户修改支付费率" )
    public ResponseEntity<?> updateRate() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        Integer id = getValIntegerRequired( "id");
        String passageRate = getValStringRequired( "passageRate");

        //判断越权查询
        PayPassage selectRecord = rpcCommonService.rpcPayPassageService.findById(id);
        if(selectRecord == null ||  !selectRecord.getBelongInfoType().equals(MchConstant.INFO_TYPE_MCH) || !selectRecord.getBelongInfoId().equals(getUser().getBelongInfoId()) ){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        PayPassage payPassage = new PayPassage();
        payPassage.setId(id);
        int count = rpcCommonService.rpcPayPassageService.updateRate(payPassage);
        if(count > 0) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "私有商户新增支付通道" )
    public ResponseEntity<?> add() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        PayPassage payPassage = getObject( PayPassage.class);

        payPassage.setBelongInfoId(getUser().getBelongInfoId());
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_MCH);
        int count = rpcCommonService.rpcPayPassageService.add(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 根据支付通道ID,获取支付账号配置定义描述
     * @return
     */
    @RequestMapping("/pay_config_get")
    @ResponseBody
    public ResponseEntity<?> getPayConfig() {

//        // 校验当前商户是否为私有账户
//        if(!getUser().getIsPrivateAccount()) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));


        Integer payPassageId = getValIntegerRequired( "payPassageId");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(payPassageId);
        //判断越权查询
        if(payPassage == null ||  !payPassage.getBelongInfoType().equals(MchConstant.INFO_TYPE_MCH) || !payPassage.getBelongInfoId().equals(getUser().getBelongInfoId()) ){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_PERMISSION_ERROR));
        }

        String ifCode = "";
        String ifTypeCode = payPassage.getIfTypeCode();

        // 如果接口配置了定义描述,则使用接口
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        if(payInterface != null && StringUtils.isNotBlank(payInterface.getParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterface);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterface.getIfTypeCode()));
            return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
        }
        // 使用接口类型配置的定义描述
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        if(payInterfaceType != null && StringUtils.isNotBlank(payInterfaceType.getMchParam())) {
            // 支付接口类型Map
            Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
            JSONObject object = (JSONObject) JSON.toJSON(payInterfaceType);
            object.put("ifTypeName", payInterfaceTypeMap.get(payInterfaceType.getIfTypeCode()));
            return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
        }
        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
    }

}
