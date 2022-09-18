package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.xxpay.core.entity.TransOrder;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/trans_order")
@PreAuthorize("hasRole('"+ MchConstant.MCH_ROLE_NORMAL+"')")
public class TransOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条转账记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String transOrderId = getValStringRequired( "transOrderId");
        TransOrder transOrder = new TransOrder();
        transOrder.setInfoId(getUser().getBelongInfoId());
        transOrder.setInfoType(MchConstant.INFO_TYPE_MCH);
        transOrder.setTransOrderId(transOrderId);
        transOrder = rpcCommonService.rpcTransOrderService.find(transOrder);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(transOrder));
    }

    /**
     * 退款订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        TransOrder transOrder = getObject( TransOrder.class);
        if(transOrder == null) transOrder = new TransOrder();
        transOrder.setInfoId(getUser().getBelongInfoId());
        transOrder.setInfoType(MchConstant.INFO_TYPE_MCH);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcTransOrderService.count(transOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<TransOrder> transOrderList = rpcCommonService.rpcTransOrderService.select((getPageIndex() -1) * getPageSize(),
                getPageSize(), transOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(transOrderList, count));
    }

}
