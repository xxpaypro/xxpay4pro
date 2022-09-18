package org.xxpay.mbr.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.PayProduct;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

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
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/trade_order")
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
        if(StringUtils.isBlank(tradeOrderId)) ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(getUser().getMchId(), tradeOrderId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(mchTradeOrder));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchTradeOrder mchTradeOrder = getObject( MchTradeOrder.class);
        if(mchTradeOrder == null) mchTradeOrder = new MchTradeOrder();
        mchTradeOrder.setMchId(getUser().getMchId());

        Byte tradeType = getValByte("tradeType");
        if(tradeType != null) mchTradeOrder.setTradeType(tradeType);
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString( "createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString( "createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);

        long count = rpcCommonService.rpcMchTradeOrderService.count(mchTradeOrder, createTimeStart, createTimeEnd);
        if(count == 0) return ResponseEntity.ok(XxPayPageRes.buildSuccess());
        List<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.select(
                (getPageIndex() -1) * getPageSize(), getPageSize(), mchTradeOrder, createTimeStart, createTimeEnd);

        Map<String, PayProduct> payProductMap = rpcCommonService.rpcCommonService.getPayProdcutMap(null);
        List<JSONObject> objects = new LinkedList<>();
        for(MchTradeOrder order : mchTradeOrderList) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            PayProduct payProduct = payProductMap.get(String.valueOf(order.getProductId()));
            object.put("productName", payProduct == null ? "" : payProduct.getProductName());  // 产品名称
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

        Long mchId = getUser().getMchId();
        Long storeId = getValLong( "storeId");
        String tradeOrderId = getValString( "tradeOrderId");
        String payOrderId = getValString( "payOrderId");
        Byte tradeType = getValByte("tradeType");
        Byte status = getValByte("status");

        // 订单起止时间
        String createTimeStartStr = getValString( "createTimeStart");
        String createTimeEndStr = getValString( "createTimeEnd");
        Map allMap = rpcCommonService.rpcMchTradeOrderService.count4All(mchId, storeId, tradeOrderId, payOrderId, tradeType, status, createTimeStartStr, createTimeEndStr);

        JSONObject obj = new JSONObject();
        obj.put("allTotalCount", allMap.get("totalCount"));                         // 所有订单数
        obj.put("allTotalAmount", allMap.get("totalAmount"));                       // 金额
        return ResponseEntity.ok(XxPayResponse.buildSuccess(obj));
    }

    /**
     * 订单记录列表
     * @return
     */
    @RequestMapping("/mall_list")
    @ResponseBody
    public ResponseEntity<?> mallList() {

        Byte industryType = getValByte("authFrom");
        Byte postType = getValByte("postType");
        Byte status = getValByte("status");
        Byte postStatus = getValByte("postStatus");

        LambdaQueryWrapper<MchTradeOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrder::getMchId, getUser().getMchId());
        queryWrapper.eq(MchTradeOrder::getMemberId, getUser().getMemberId());
        queryWrapper.eq(MchTradeOrder::getDeleteFlag, MchConstant.PUB_NO);
        if (industryType != null) queryWrapper.eq(MchTradeOrder::getIndustryType, industryType);
        queryWrapper.orderByDesc(MchTradeOrder::getCreateTime);
        if(postType != null) queryWrapper.eq(MchTradeOrder::getPostType, postType);
        if(status != null) queryWrapper.eq(MchTradeOrder::getStatus, status);
        if(postStatus != null) queryWrapper.eq(MchTradeOrder::getPostStatus, postStatus);


        IPage<MchTradeOrder> mchTradeOrderList = rpcCommonService.rpcMchTradeOrderService.page(getIPage(), queryWrapper);

        List<JSONObject> objects = new LinkedList<>();
        int goodsNum;

        for(MchTradeOrder order : mchTradeOrderList.getRecords()) {
            JSONObject object = (JSONObject) JSON.toJSON(order);
            goodsNum = 0;

            if (StringUtils.isNotBlank(order.getGoodsDesc())){

                JSONArray jsonArray = JSONArray.parseArray(order.getGoodsDesc());
                for(int i = 0; i < jsonArray.size(); i++) {
                    JSONObject goodsJson = jsonArray.getJSONObject(i);
                    goodsNum += goodsJson.getInteger("goodsNum");
                }
            }

            object.put("goodsNum", goodsNum);
            objects.add(object);
        }
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(objects, mchTradeOrderList.getTotal()));
    }

    /**
     * 会员删除订单接口
     * @return
     */
    @RequestMapping("/delete_order")
    @ResponseBody
    public ResponseEntity<?> deleteOrder() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        Boolean delResult = rpcCommonService.rpcMchTradeOrderService.update(new LambdaUpdateWrapper<MchTradeOrder>()
                .eq(MchTradeOrder::getTradeOrderId, tradeOrderId)
                .eq(MchTradeOrder::getMemberId, getUser().getMemberId())
                .set(MchTradeOrder::getDeleteFlag, MchConstant.PUB_YES)
        );
        if (delResult) return ResponseEntity.ok(XxPayResponse.buildSuccess());

        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 订单确认收货
     * @return
     */
    @RequestMapping("/order_receive")
    @ResponseBody
    public ResponseEntity<?> orderReceive() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        Boolean result = rpcCommonService.rpcMchTradeOrderService.update(new LambdaUpdateWrapper<MchTradeOrder>()
                .eq(MchTradeOrder::getTradeOrderId, tradeOrderId)
                .eq(MchTradeOrder::getMemberId, getUser().getMemberId())
                .set(MchTradeOrder::getPostStatus, MchConstant.TRADE_ORDER_POST_STATUS_SUCCESS)
        );
        if (result) return ResponseEntity.ok(XxPayResponse.buildSuccess());

        return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 订单确认收货
     * @return
     */
    @RequestMapping("/status_count")
    @ResponseBody
    public ResponseEntity<?> statusCount() {

        Byte authFrom = getValByteRequired("authFrom");

        //待付款订单数量
        int waitPayCount = rpcCommonService.rpcMchTradeOrderService.countByMemberAndStatus(getUser().getMemberId(), authFrom, MchConstant.TRADE_ORDER_STATUS_ING, null);

        //待发货订单数量
        int waitPostCount = rpcCommonService.rpcMchTradeOrderService.countByMemberAndStatus(getUser().getMemberId(), authFrom, MchConstant.TRADE_ORDER_STATUS_SUCCESS, MchConstant.TRADE_ORDER_POST_STATUS_INIT);

        //待收货订单数量
        int waitConfirmCount = rpcCommonService.rpcMchTradeOrderService.countByMemberAndStatus(getUser().getMemberId(), authFrom, MchConstant.TRADE_ORDER_STATUS_SUCCESS, MchConstant.TRADE_ORDER_POST_STATUS_ING);

        //申请售后服务数量
        int afterSaleIngCount = rpcCommonService.rpcMchTradeOrderAfterSaleService.countAfterSaleIng(getUser().getMemberId(), authFrom);

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("waitPayCount", waitPayCount);
        resultJSON.put("waitPostCount", waitPostCount);
        resultJSON.put("waitConfirmCount", waitConfirmCount);
        resultJSON.put("afterSaleIngCount", afterSaleIngCount);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJSON));
    }

}
