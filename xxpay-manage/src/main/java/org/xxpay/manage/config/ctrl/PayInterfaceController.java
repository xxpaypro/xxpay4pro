package org.xxpay.manage.config.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.annotation.MethodLog;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.PayInterface;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.config.service.CommonConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 18/05/03
 * @description: 支付接口
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/config/pay_interface")
public class PayInterfaceController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private CommonConfigService commonConfigService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        PayInterface payInterface = getObject( PayInterface.class);
        int count = rpcCommonService.rpcPayInterfaceService.count(payInterface);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<PayInterface> payInterfaceList = rpcCommonService.rpcPayInterfaceService.select((getPageIndex() -1) * getPageSize(), getPageSize(), payInterface);
        // 支付接口类型Map
        Map payInterfaceTypeMap = commonConfigService.getPayInterfaceTypeMap();
        // 支付类型Map
        Map payTypeMap = commonConfigService.getPayTypeMap();
        // 转换前端显示
        List<JSONObject> objects = new LinkedList<>();
        for(PayInterface info : payInterfaceList) {
            JSONObject object = (JSONObject) JSON.toJSON(info);
            object.put("payTypeName", payTypeMap.get(info.getPayType()));               // 转换支付类型名称
            object.put("ifTypeName", payInterfaceTypeMap.get(info.getIfTypeCode()));   // 转换接口类型名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String ifCode = getValStringRequired( "ifCode");
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByCode(ifCode);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(payInterface));
    }

    @RequestMapping("/update")
    @ResponseBody
    @MethodLog( remark = "修改支付接口信息" )
    public ResponseEntity<?> update() {

        PayInterface payInterface = getObject( PayInterface.class);
        int count = rpcCommonService.rpcPayInterfaceService.update(payInterface);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    @RequestMapping("/add")
    @ResponseBody
    @MethodLog( remark = "新增支付接口信息" )
    public ResponseEntity<?> add() {

        PayInterface payInterface = getObject( PayInterface.class);
        int count = rpcCommonService.rpcPayInterfaceService.add(payInterface);
        if(count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }
}
