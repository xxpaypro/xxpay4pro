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
import org.xxpay.core.entity.PayType;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/08/28
 * @description: 支付类型
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_type")
public class PayTypeController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayType payType = getObject( PayType.class);
        int count = rpcCommonService.rpcPayTypeService.count(payType);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayType> payTypeList = rpcCommonService.rpcPayTypeService.select((getPageIndex() -1) * getPageSize(), getPageSize(), payType);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(payTypeList, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String payTypeCode = getValStringRequired( "payTypeCode");
        PayType payType = rpcCommonService.rpcPayTypeService.findByPayTypeCode(payTypeCode);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payType));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付类型" )
    public ResponseEntity<?> update() {

        PayType payType = getObject( PayType.class);
        int count = rpcCommonService.rpcPayTypeService.update(payType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付类型" )
    public ResponseEntity<?> add() {

        PayType payType = getObject( PayType.class);
        int count = rpcCommonService.rpcPayTypeService.add(payType);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

}
