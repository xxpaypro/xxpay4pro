package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.PayInterfaceTypeTemplate;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_interface_type/template")
public class PayInterfaceTypeTemplateController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayInterfaceTypeTemplate payInterfaceTypeTemplate = getObject( PayInterfaceTypeTemplate.class);
        int count = rpcCommonService.rpcPayInterfaceTypeTemplateService.count(payInterfaceTypeTemplate);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayInterfaceTypeTemplate> payInterfaceTypeTemplateList = rpcCommonService.rpcPayInterfaceTypeTemplateService.select((getPageIndex() -1) * getPageSize(), getPageSize(), payInterfaceTypeTemplate);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(payInterfaceTypeTemplateList, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        PayInterfaceTypeTemplate payInterfaceTypeTemplate = rpcCommonService.rpcPayInterfaceTypeTemplateService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payInterfaceTypeTemplate));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付配置模板" )
    public ResponseEntity<?> update() {

        PayInterfaceTypeTemplate payInterfaceTypeTemplate = getObject( PayInterfaceTypeTemplate.class);
        int count = rpcCommonService.rpcPayInterfaceTypeTemplateService.update(payInterfaceTypeTemplate);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付配置模板" )
    public ResponseEntity<?> add() {

        PayInterfaceTypeTemplate payInterfaceTypeTemplate = getObject( PayInterfaceTypeTemplate.class);
        int count = rpcCommonService.rpcPayInterfaceTypeTemplateService.add(payInterfaceTypeTemplate);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
    
    
    @RequestMapping("/syncAccountParam")
    @ResponseBody
    @MethodLog( remark = "同步支付配置" )
    public ResponseEntity<?> syncAccountParam() {

        
        Long id = getValLongRequired( "id");

        //TODO 删除查询通道子账号信息
        return ResponseEntity.ok(XxPayResponse.buildSuccess(0));
    }



}
