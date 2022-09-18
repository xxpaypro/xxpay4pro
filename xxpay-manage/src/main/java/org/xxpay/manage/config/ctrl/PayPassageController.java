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
import org.xxpay.core.common.vo.PayExtConfigVO;
import org.xxpay.core.entity.*;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 18/05/04
 * @description: 支付通道
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_passage")
public class PayPassageController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayPassage payPassage = getObject( PayPassage.class);
        payPassage.setBelongInfoId(0L);
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
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

        Integer id = getValIntegerRequired( "id");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payPassage));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道" )
    public ResponseEntity<?> update() {

        PayPassage payPassage = getObject( PayPassage.class);

        int count = rpcCommonService.rpcPayPassageService.update(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/risk_update")
    @ResponseBody
    @MethodLog( remark = "修改支付通道风控" )
    public ResponseEntity<?> updateRisk() {

        PayPassage payPassage = getObject( PayPassage.class);
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
    @MethodLog( remark = "修改支付通道费率" )
    public ResponseEntity<?> updateRate() {

        Integer id = getValIntegerRequired( "id");
        String passageRate = getValStringRequired( "passageRate");
        PayPassage payPassage = new PayPassage();
        payPassage.setId(id);
        int count = rpcCommonService.rpcPayPassageService.updateRate(payPassage);
        if(count > 0) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付通道" )
    public ResponseEntity<?> add() {

        PayPassage payPassage = getObject( PayPassage.class);
        payPassage.setBelongInfoId(0L);
        payPassage.setBelongInfoType(MchConstant.INFO_TYPE_PLAT);
        int count = rpcCommonService.rpcPayPassageService.add(payPassage);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 根据支付通道ID,获取支付账号配置定义描述
     * @param request
     * @return
     */
    @RequestMapping("/pay_config_get")
    @ResponseBody
    public ResponseEntity<?> getPayConfig() {

        Integer payPassageId = getValIntegerRequired( "payPassageId");
        PayPassage payPassage = rpcCommonService.rpcPayPassageService.findById(payPassageId);
        if(payPassage == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        String ifTypeCode = payPassage.getIfTypeCode();

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


    /**
     * 查询 商户 /代理商 的所有可配置支付通道
     * @param request
     * @return
     */
    @RequestMapping("/can_set_passage_list")
    @ResponseBody
    public ResponseEntity<?> canSetPassageList() {

        byte infoType = getValByteRequired( "infoType");
        Long infoId = getValLongRequired( "infoId");
        Integer payProductId = getValIntegerRequired( "payProductId");

        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(payProductId);
        if(payProduct == null) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_PAY_PRODUCT_NOT_EXIST));

        Long parentAgentId = null;
        if(MchConstant.INFO_TYPE_MCH == infoType) parentAgentId = rpcCommonService.rpcMchInfoService.findByMchId(infoId).getAgentId();
        if(MchConstant.INFO_TYPE_AGENT == infoType) parentAgentId = rpcCommonService.rpcAgentInfoService.findByAgentId(infoId).getPid();

        if(parentAgentId == null || parentAgentId == 0) { //平台商户 || 总代理商
            //查询该平台的全部通道信息
            List<PayPassage> result = rpcCommonService.rpcPayPassageService.selectPlatAllByPayType(payProduct.getPayType());
            return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
        }

        FeeScale feeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByAgent(parentAgentId, payProductId);
        if(feeScale == null)
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));

        PayExtConfigVO payExtConfigVO = (PayExtConfigVO) feeScale.getExtConfigVO();
        if(payExtConfigVO == null)  return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_MCH4AGENT_NOT_SET_PASSAGE));

        PayPassage selectRecord = new PayPassage();
        selectRecord.setPsVal("ids", payExtConfigVO.getVisiblePassageList());
        selectRecord.setBelongInfoType(MchConstant.INFO_TYPE_PLAT); //查询平台 可用支付通道
        List<PayPassage> result = rpcCommonService.rpcPayPassageService.selectAll(selectRecord);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));

    }

}
