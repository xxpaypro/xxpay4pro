package org.xxpay.mbr.mch.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.JsonUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.*;

/**
 * @author: zx
 * @date: 19/09/18
 * @description: 商户优惠券
 */
@RestController
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/coupon")
public class MchCouponController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MchCouponController.class);

    /**
     * 商户可领优惠券列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long storeId = getValLong("storeId");
        Date currentDate = new Date();

        //商户可用优惠券
        MchCoupon mchCoupon = new MchCoupon();
        mchCoupon.setMchId(getUser().getMchId());
        mchCoupon.setStatus(MchConstant.PUB_YES);
        IPage<MchCoupon> mchCouponList = rpcCommonService.rpcMchCouponService.list(mchCoupon, getIPage(), currentDate);

        //会员已领取优惠券
        List<MemberCoupon> memberCouponList = rpcCommonService.rpcMemberCouponService.list(new QueryWrapper<MemberCoupon>().lambda()
                .eq(MemberCoupon::getMchId, getUser().getMchId())
                .eq(MemberCoupon::getMemberId, getUser().getMemberId()));

        //门店限制
        List<MchCouponStoreRela> relaList = null;
        if (storeId != null){
            MchStore mchStore = rpcCommonService.rpcMchStoreService.getById(storeId);
            if (mchStore == null || mchStore.getStatus() == MchConstant.PUB_NO || !mchStore.getMchId().equals(getUser().getMchId())) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_NOT_EXIST));
            }
            relaList = rpcCommonService.rpcMchCouponStoreRelaService.list(new QueryWrapper<MchCouponStoreRela>().lambda()
                .eq(MchCouponStoreRela::getStoreId, storeId));
        }

        //返回可领列表
        List<MchCoupon> respList = new LinkedList<>();
        int result;
        for (MchCoupon coupon : mchCouponList.getRecords()) {
            //校验优惠券是否可领
            result = rpcCommonService.rpcMchCouponService.checkCoupon(coupon, memberCouponList, relaList);
            if (result == 1){
                respList.add(coupon);
            }
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(respList));
    }

    /**
     * 商户优惠券信息查询
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long couponId = getValLongRequired( "couponId");
        MchCoupon coupon = rpcCommonService.rpcMchCouponService.getOne(
                new QueryWrapper<MchCoupon>().lambda()
                .eq(MchCoupon::getCouponId, couponId)
                .eq(MchCoupon::getMchId, getUser().getMchId())
        );
        //转换成json
        JSONObject json = JsonUtil.getJSONObjectFromObj(coupon);
        //获取设置门店对应的门店ID
        ArrayList<Long> list = new ArrayList<>();
        list.add(couponId);
        String storeIds = "";
        Collection<MchCouponStoreRela> listByIds = rpcCommonService.rpcMchCouponStoreRelaService.listByIds(list);
        if (listByIds.size() != 0){
            for (MchCouponStoreRela rela: listByIds) {
                storeIds += rela.getStoreId().toString() + ",";
            }
            json.put("storeIds", storeIds.substring(0, storeIds.length() - 1));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess(json));
    }

}
