package org.xxpay.agent.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.agent.common.ctrl.BaseController;
import org.xxpay.agent.common.service.RpcCommonService;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchTradeOrder;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(Constant.AGENT_CONTROLLER_ROOT_PATH + "/trade_order")
public class MchTradeOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条记录
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByTradeOrderId(tradeOrderId);
        if(mchTradeOrder == null) return ResponseEntity.ok(XxPayResponse.buildSuccess(mchTradeOrder));
        JSONObject object = (JSONObject) JSON.toJSON(mchTradeOrder);
        object.put("productName", MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(mchTradeOrder.getProductType()));  // 产品名称
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));

    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    public ResponseEntity<?> list() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);

        List<Long> allAgentIds = rpcCommonService.rpcAgentInfoService.queryAllSubAgentIds(getUser().getBelongInfoId());
        mchTradeOrder.setPsVal("agentIdIn", allAgentIds);  //查询所有代理商的商户订单

        Date[] dateRange = getQueryDateRange();

        // 订单起止时间
        Date createTimeStart = dateRange[0];
        Date createTimeEnd = dateRange[1];

        long count = rpcCommonService.rpcMchTradeOrderService.count(mchTradeOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.select(
                (getPageIndex() -1) * getPageSize(), getPageSize(), mchTradeOrder, createTimeStart, createTimeEnd);
        List<JSONObject> objects = new LinkedList<>();
        for(MchTradeOrder order : mchTradeOrderList) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            object.put("productName", MchConstant.MCH_TRADE_PRODUCT_TYPE_MAP.get(order.getProductType()));  // 产品名称
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, count));
    }

}
