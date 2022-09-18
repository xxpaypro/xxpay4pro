package org.xxpay.agent.bill;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.Bill;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 18/02/07
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/bill")
public class BillController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Bill mchBill = getObject( Bill.class);
        if(mchBill == null) mchBill = new Bill();
        mchBill.setInfoId(getUser().getBelongInfoId());
        mchBill.setInfoType(MchConstant.INFO_TYPE_AGENT);
        int count = rpcCommonService.rpcBillService.count(mchBill);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Bill> mchBillList = rpcCommonService.rpcBillService.select((getPageIndex() - 1) * getPageSize(), getPageSize(), mchBill);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchBillList, count));
    }

    /**
     * 查询下级商户 下级代理商的对账单列表
     * @param request
     * @return
     */
    @RequestMapping("/sub_list")
    @ResponseBody
    public ResponseEntity<?> subList() {

        Bill mchBill = getObject( Bill.class);
        if(mchBill == null) mchBill = new Bill();
        mchBill.setPsVal("currentAgentId", getUser().getBelongInfoId());
        int count = rpcCommonService.rpcBillService.countBySub(mchBill);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<Bill> mchBillList = rpcCommonService.rpcBillService.selectBySub((getPageIndex() - 1) * getPageSize(), getPageSize(), mchBill);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(mchBillList, count));
    }

    /**
     *
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long id = getValLongRequired("id");
        Bill mchBill = rpcCommonService.rpcBillService.findById(id);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchBill));
    }

}