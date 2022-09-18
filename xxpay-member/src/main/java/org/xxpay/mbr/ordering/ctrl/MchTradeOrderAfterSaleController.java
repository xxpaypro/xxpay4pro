package org.xxpay.mbr.ordering.ctrl;


import com.alibaba.fastjson.JSON;
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
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchReceiveAddress;
import org.xxpay.core.entity.MchTradeOrderAfterSale;
import org.xxpay.mbr.common.ctrl.BaseController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 售后表 前端控制器
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-16
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/after_sale")
public class MchTradeOrderAfterSaleController extends BaseController {

    /**
     * 会员售后订单列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {
        Byte authFrom = getValByteRequired("authFrom");
        String statusArr = getValString("statusArr");

        LambdaQueryWrapper<MchTradeOrderAfterSale> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MchTradeOrderAfterSale::getMchId, getUser().getMchId());
        queryWrapper.eq(MchTradeOrderAfterSale::getMemberId, getUser().getMemberId());
        if (authFrom != null) queryWrapper.eq(MchTradeOrderAfterSale::getAuthFrom, authFrom);
        if (statusArr != null) {
            List statusList = Arrays.asList(statusArr.split(","));
            queryWrapper.in(MchTradeOrderAfterSale::getStatus, statusList);
        }
        queryWrapper.orderByDesc(MchTradeOrderAfterSale::getCreateTime);

        IPage<MchTradeOrderAfterSale> page = rpcCommonService.rpcMchTradeOrderAfterSaleService.page(getIPage(), queryWrapper);
        return ResponseEntity.ok(PageRes.buildSuccess(page));
    }

    /**
     * 会员售后订单详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {
        Long afterSaleId = getValLongRequired("afterSaleId");

        MchTradeOrderAfterSale afterSale = rpcCommonService.rpcMchTradeOrderAfterSaleService.getOne(new LambdaQueryWrapper<MchTradeOrderAfterSale>()
                .eq(MchTradeOrderAfterSale::getMemberId, getUser().getMemberId())
                .eq(MchTradeOrderAfterSale::getAfterSaleId, afterSaleId)
        );
        JSONObject object = (JSONObject) JSON.toJSON(afterSale);

        if (StringUtils.isNotBlank(afterSale.getAddressInfo())) {
            JSONObject address = (JSONObject) JSON.parse(afterSale.getAddressInfo());
            object.put("contactName", address.getString("contactName"));
            object.put("tel", address.getString("tel"));
            object.put("address", address.getString("areaInfo") + address.getString("address"));
        }

        return ResponseEntity.ok(PageRes.buildSuccess(object));
    }

    /**
     * 会员售后申请
     */
    @RequestMapping("/apply")
    @ResponseBody
    public ResponseEntity<?> apply() {

        Long memberId = getUser().getMemberId();//会员ID
        Long mchId = getUser().getMchId(); //商户ID
        Long isvId = rpcCommonService.rpcMchInfoService.getById(mchId).getIsvId();

        MchTradeOrderAfterSale afterSale = getObject(MchTradeOrderAfterSale.class);
        afterSale.setMemberId(memberId);
        afterSale.setMchId(mchId);
        afterSale.setIsvId(isvId);
        afterSale.setStatus(MchConstant.STATUS_AUDIT_ING);//待审核

        boolean result = rpcCommonService.rpcMchTradeOrderAfterSaleService.apply(afterSale);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 会员售后更新运单号
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        Long afterOrderId = getValLongRequired("afterOrderId");
        String backTransportNo = getValStringRequired("backTransportNo");

        LambdaUpdateWrapper<MchTradeOrderAfterSale> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MchTradeOrderAfterSale::getMemberId, getUser().getMemberId());
        updateWrapper.eq(MchTradeOrderAfterSale::getAfterSaleId, afterOrderId);
        updateWrapper.set(MchTradeOrderAfterSale::getBackTransportNo, backTransportNo);
        updateWrapper.set(MchTradeOrderAfterSale::getBackTransportTime, new Date());
        updateWrapper.set(MchTradeOrderAfterSale::getStatus, MchConstant.MCH_AFTER_SALE_STATUS_POST_ING);

        boolean result = rpcCommonService.rpcMchTradeOrderAfterSaleService.update(updateWrapper);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

    /**
     * 确认收到退款
     */
    @RequestMapping("/confirm")
    @ResponseBody
    public ResponseEntity<?> confirm() {

        Long afterOrderId = getValLongRequired("afterOrderId");
        MchTradeOrderAfterSale afterSale = rpcCommonService.rpcMchTradeOrderAfterSaleService.getById(afterOrderId);
        if (afterSale == null || !afterSale.getMemberId().equals(getUser().getMemberId())) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
        }

        boolean result = rpcCommonService.rpcMchTradeOrderAfterSaleService.updateComplete(afterOrderId);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));

        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }

}
