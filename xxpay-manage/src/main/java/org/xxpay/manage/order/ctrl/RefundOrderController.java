package org.xxpay.manage.order.ctrl;

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
import org.xxpay.core.entity.RefundOrder;
import org.xxpay.manage.common.service.RpcCommonService;
import org.xxpay.manage.common.ctrl.BaseController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/refund_order")
public class RefundOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条退款记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String refundOrderId = getValStringRequired( "refundOrderId");
        RefundOrder refundOrder = rpcCommonService.rpcRefundOrderService.findByRefundOrderId(refundOrderId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(refundOrder));
    }

    /**
     * 退款订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        RefundOrder refundOrder = getObject( RefundOrder.class);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        int count = rpcCommonService.rpcRefundOrderService.count(refundOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<RefundOrder> refundOrderList = rpcCommonService.rpcRefundOrderService.select((getPageIndex() -1) * getPageSize(),
                getPageSize(), refundOrder, createTimeStart, createTimeEnd);
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(refundOrderList, count));
    }

}
