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
import org.xxpay.core.entity.PayInterfaceType;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/05/03
 * @description: 支付接口类型
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_interface_type")
public class PayInterfaceTypeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayInterfaceType payInterfaceType = getObject( PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.count(payInterfaceType);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayInterfaceType> payInterfaceTypeList = rpcCommonService.rpcPayInterfaceTypeService.select((getPageIndex() -1) * getPageSize(), getPageSize(), payInterfaceType);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(payInterfaceTypeList, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String ifTypeCode = getValStringRequired( "ifTypeCode");
        PayInterfaceType payInterfaceType = rpcCommonService.rpcPayInterfaceTypeService.findByCode(ifTypeCode);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payInterfaceType));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付接口类型" )
    public ResponseEntity<?> update() {

        PayInterfaceType payInterfaceType = getObject( PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.update(payInterfaceType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付接口类型" )
    public ResponseEntity<?> add() {

        PayInterfaceType payInterfaceType = getObject( PayInterfaceType.class);
        int count = rpcCommonService.rpcPayInterfaceTypeService.add(payInterfaceType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }



}
