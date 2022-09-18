package org.xxpay.isv.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.isv.common.ctrl.BaseController;
import org.xxpay.isv.common.service.RpcCommonService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/21
 * @description:
 */
@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/trade_order")
public class MchTradeOrderController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 查询单条记录
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<?> list() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);

        mchTradeOrder.setIsvId(getUser().getBelongInfoId());  //仅查询当前服务商订单
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

    /**
     * 查询统计数据
     * @return
     */
    @RequestMapping("/count")
    @ResponseBody
    public ResponseEntity<?> count() {

        Long mchId = getValLong( "mchId");
        String tradeOrderId = getValString( "tradeOrderId");
        String payOrderId = getValString( "payOrderId");
        Byte tradeType = getValByte( "tradeType");
        Byte status = getValByte( "status");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchTradeOrderService.count4All(mchId, null, tradeOrderId, payOrderId, tradeType, status, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        obj.put("allTotalMchIncome", allMap.get("totalMchIncome"));                 // 入账金额
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }

}
