package org.xxpay.mbr.ordering.ctrl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchTradeOrder;
import org.xxpay.core.entity.MchTradeOrderDetail;
import org.xxpay.mbr.common.ctrl.BaseController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单详情表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/tradeOrderDetail")
public class MchTradeOrderDetailController extends BaseController {

    /**
     * 订单详情列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list(int page, int limit) {

        MchTradeOrderDetail mchTradeOrderDetail = new MchTradeOrderDetail();
        mchTradeOrderDetail.setMchId(getUser().getMchId());
        mchTradeOrderDetail.setMemberId(getUser().getMemberId());

        //List<Map> mchTradeOrderDetailList = rpcCommonService.rpcMchTradeOrderDetailService.selectList((getPageIndex() - 1) * getPageSize(), getPageSize(), mchTradeOrderDetail);

        IPage<MchTradeOrderDetail> mchTradeOrderDetailList = rpcCommonService.rpcMchTradeOrderDetailService.list(mchTradeOrderDetail, new Page<>(page, limit), null);
        return ResponseEntity.ok(PageRes.buildSuccess(mchTradeOrderDetailList));
    }

    /**
     * 订单详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        String tradeOrderId = getValStringRequired("tradeOrderId");

        MchTradeOrder mchTradeOrder = rpcCommonService.rpcMchTradeOrderService.getOne(new QueryWrapper<MchTradeOrder>().lambda()
                .eq(MchTradeOrder::getTradeOrderId, tradeOrderId)
                .eq(MchTradeOrder::getMchId, getUser().getMchId())
                .eq(MchTradeOrder::getDeleteFlag, MchConstant.PUB_NO)
        );

        JSONObject respJson = (JSONObject) JSON.toJSON(mchTradeOrder);

        //查询优惠券信息
        if (mchTradeOrder.getMchCouponId() != null) {
            MchCoupon mchCoupon = rpcCommonService.rpcMchCouponService.getById(mchTradeOrder.getMchCouponId());
            respJson.put("mchCouponLimit", mchCoupon.getPayAmountLimit());
            respJson.put("mchCouponAmount", mchCoupon.getCouponAmount());
        }

        //查询订单商品详情
        LambdaQueryWrapper<MchTradeOrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrderDetail::getMemberId, getUser().getMemberId());
        queryWrapper.eq(MchTradeOrderDetail::getMchId, getUser().getMchId());
        queryWrapper.eq(MchTradeOrderDetail::getTradeOrderId, tradeOrderId);
        queryWrapper.orderByDesc(MchTradeOrderDetail::getCreateTime);

        List<MchTradeOrderDetail> list = rpcCommonService.rpcMchTradeOrderDetailService.list(queryWrapper);

        respJson.put("mchTradeDetail", list);

        return ResponseEntity.ok(PageRes.buildSuccess(respJson));
    }


}
