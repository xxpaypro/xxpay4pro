package org.xxpay.mch.shoppingMall.ctrl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.MchTradeOrderAfterSale;
import org.xxpay.mch.common.ctrl.BaseController;

import java.util.Date;

/**
 * <p>
 * 售后表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-16
 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/after_sale")
public class MchTradeOrderAfterSaleController extends BaseController {

    /**
     * 会员售后订单列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        MchTradeOrderAfterSale mchTradeOrderAfterSale = getObject( MchTradeOrderAfterSale.class);

        // 订单起止时间
        Date[] queryDate = getQueryDateRange();
        Date createTimeStart = queryDate[0];
        Date createTimeEnd =  queryDate[1];

        LambdaQueryWrapper<MchTradeOrderAfterSale> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId());
        if (mchTradeOrderAfterSale.getMemberId() != null) queryWrapper.eq(MchTradeOrderAfterSale::getMemberId, mchTradeOrderAfterSale.getMemberId());
        if (StringUtils.isNotEmpty(mchTradeOrderAfterSale.getTradeOrderId())) queryWrapper.eq(MchTradeOrderAfterSale::getTradeOrderId, mchTradeOrderAfterSale.getTradeOrderId());
        if (mchTradeOrderAfterSale.getStatus() != null) queryWrapper.eq(MchTradeOrderAfterSale::getStatus, mchTradeOrderAfterSale.getStatus());
        if (mchTradeOrderAfterSale.getAfterSaleType() != null) queryWrapper.eq(MchTradeOrderAfterSale::getAfterSaleType, mchTradeOrderAfterSale.getAfterSaleType());
        if (mchTradeOrderAfterSale.getAuthFrom() != null) queryWrapper.eq(MchTradeOrderAfterSale::getAuthFrom, mchTradeOrderAfterSale.getAuthFrom());

        queryWrapper.orderByDesc(MchTradeOrderAfterSale::getCreateTime);
        if(createTimeStart != null) queryWrapper.ge(MchTradeOrderAfterSale::getCreateTime, createTimeStart);
        if(createTimeEnd != null) queryWrapper.le(MchTradeOrderAfterSale::getCreateTime, createTimeEnd);

        IPage<MchTradeOrderAfterSale> page = rpcCommonService.rpcMchTradeOrderAfterSaleService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(page));
    }

    /**
     * 会员售后订单列表
     */
    @RequestMapping("/getGoods")
    @ResponseBody
    public ResponseEntity<?> getGoods() {

        Long afterSaleId = getValLongRequired("afterSaleId");

        MchTradeOrderAfterSale afterSale = rpcCommonService.rpcMchTradeOrderAfterSaleService.getOne(new LambdaQueryWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId())
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
        );

        if (afterSale == null || StringUtils.isBlank(afterSale.getGoodsDesc())) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        JSONArray jsonArray = JSONArray.parseArray(afterSale.getGoodsDesc());
        return ResponseEntity.ok(XxPayPageRes.buildSuccess(jsonArray, jsonArray.size()));
    }

    /**
     * 会员售后订单详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long afterSaleId = getValLongRequired("afterSaleId");

        MchTradeOrderAfterSale afterSale = rpcCommonService.rpcMchTradeOrderAfterSaleService.getOne(new LambdaQueryWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId())
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
        );
        JSONObject object = (JSONObject) JSON.toJSON(afterSale);

        if (StringUtils.isNotBlank(afterSale.getAddressInfo())) {
            JSONObject address = (JSONObject) JSON.parse(afterSale.getAddressInfo());
            object.put("contactName", address.getString("contactName"));
            object.put("tel", address.getString("tel"));
            object.put("address", address.getString("areaInfo") + address.getString("address"));
        }

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getById(afterSale.getTradeOrderId());
        if (mchTradeOrder != null) {
            object.put("orderAmount", mchTradeOrder.getOrderAmount());
            object.put("discountAmount", mchTradeOrder.getDiscountAmount());
            object.put("amount", mchTradeOrder.getAmount());
        }

        return ResponseEntity.ok(PageRes.buildSuccess(object));
    }

    /**
     * 会员售后订单审核
     */
    @RequestMapping("/audit")
    @ResponseBody
    public ResponseEntity<?> audit() {
        Long afterSaleId = getValLongRequired("afterSaleId");
        Byte status = getValByteRequired("status");
        String auditRemark = getValString("auditRemark");

        boolean updateResult = rpcCommonService.rpcMchTradeOrderAfterSaleService.update(new LambdaUpdateWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
                .eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId())
                .set(MchTradeOrderAfterSale::getStatus, status)
                .set(MchTradeOrderAfterSale::getAuditRemark, auditRemark)
                .set(MchTradeOrderAfterSale::getAuditTime, new Date())
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(updateResult));
    }

    /**
     * 会员售后订单商家处理完成--寄回买家
     */
    @RequestMapping("/transport")
    @ResponseBody
    public ResponseEntity<?> back() {
        Long afterSaleId = getValLongRequired("afterSaleId");
        String transportNo = getValStringRequired("transportNo");
        Byte status = getValByteRequired("status");

        LambdaUpdateWrapper<MchTradeOrderAfterSale> updateWrapper = new LambdaUpdateWrapper();
        updateWrapper.eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId);
        updateWrapper.eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId());
        updateWrapper.set(MchTradeOrderAfterSale::getStatus, status);
        updateWrapper.set(MchTradeOrderAfterSale::getTransportNo, transportNo);
        if (MchConstant.MCH_AFTER_SALE_STATUS_POST_TO_BUYER == status) {
            updateWrapper.set(MchTradeOrderAfterSale::getTransportTime, new Date());
        }else if (MchConstant.MCH_AFTER_SALE_STATUS_BACK == status) {
            updateWrapper.set(MchTradeOrderAfterSale::getCompleteTime, new Date());
        }else {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MGR_STATUS_ERROR));
        }

        boolean updateResult = rpcCommonService.rpcMchTradeOrderAfterSaleService.update(updateWrapper);
        if (!updateResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 会员售后订单售后完成
     */
    @RequestMapping("/complete")
    @ResponseBody
    public ResponseEntity<?> complete() {
        Long afterSaleId = getValLongRequired("afterSaleId");

        boolean updateResult = rpcCommonService.rpcMchTradeOrderAfterSaleService.update(new LambdaUpdateWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
                .eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId())
                .set(MchTradeOrderAfterSale::getStatus, MchConstant.MCH_AFTER_SALE_STATUS_COMPLETE)
                .set(MchTradeOrderAfterSale::getCompleteTime, new Date())
        );
        if (!updateResult) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 会员售后订单原物寄回
     */
    @RequestMapping("/backToBuyer")
    @ResponseBody
    public ResponseEntity<?> backToBuyer() {
        Long afterSaleId = getValLongRequired("afterSaleId");

        boolean updateResult = rpcCommonService.rpcMchTradeOrderAfterSaleService.update(new LambdaUpdateWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
                .eq(MchTradeOrderAfterSale::getMchId, getUser().getBelongInfoId())
                .set(MchTradeOrderAfterSale::getStatus, MchConstant.MCH_AFTER_SALE_STATUS_BACK)
        );

        return ResponseEntity.ok(XxPayResponse.buildSuccess(updateResult));
    }

}
