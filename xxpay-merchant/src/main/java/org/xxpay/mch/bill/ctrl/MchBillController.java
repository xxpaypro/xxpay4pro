package org.xxpay.mch.bill.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.Bill;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/02/07
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/bill")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class MchBillController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Bill mchBill = getObject( Bill.class);
        if(mchBill == null) mchBill = new Bill();
        mchBill.setInfoId(getUser().getBelongInfoId());
        mchBill.setInfoType(MchConstant.INFO_TYPE_MCH);
        int count = rpcCommonService.rpcMchBillService.count(mchBill);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Bill> mchBillList = rpcCommonService.rpcMchBillService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), mchBill);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchBillList, count));
    }

    /**
     * 查询应用信息
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired( "id");
        Bill mchBill = rpcCommonService.rpcMchBillService.findByInfoIdAndId(getUser().getBelongInfoId(), MchConstant.INFO_TYPE_MCH, id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBill));
    }

}
