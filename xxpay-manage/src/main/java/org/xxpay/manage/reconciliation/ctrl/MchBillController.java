package org.xxpay.manage.reconciliation.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.Bill;
import org.xxpay.manage.common.ctrl.BaseController;
import org.xxpay.manage.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/02/07
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bill/mch")
public class MchBillController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Bill mchBill = getObject( Bill.class);
        if(mchBill == null) mchBill = new Bill();
        int count = rpcCommonService.rpcMchBillService.count(mchBill);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Bill> mchBillList = rpcCommonService.rpcMchBillService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), mchBill);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchBillList, count));
    }

    /**
     *
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        Bill mchBill = rpcCommonService.rpcMchBillService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBill));
    }

}