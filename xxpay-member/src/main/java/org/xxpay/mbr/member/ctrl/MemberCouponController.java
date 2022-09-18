package org.xxpay.mbr.member.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchCouponStoreRela;
import org.xxpay.core.entity.MchStore;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_coupon")
public class MemberCouponController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MemberCouponController.class);

    /**
     * 领取优惠券
     */
    @RequestMapping("/addMbrCoupon")
    @ResponseBody
    public ResponseEntity<?> addMbrCoupon() {

        Long couponId = getValLongRequired("couponId");
        Long memberId = getUser().getMemberId();

        //商户设置的优惠券
        MchCoupon mchCoupon = rpcCommonService.rpcMchCouponService.getById(couponId);
        if (mchCoupon == null || mchCoupon.getStatus() != MchConstant.PUB_YES) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS));
        }

        //获取当前登录人商户ID
        Long mchId = getUser().getMchId();
        //判断当前优惠券与当前会员所属商户是否一致
        if (!mchCoupon.getMchId().equals(mchId)) {
            _log.info("该优惠券非{}商户下优惠券，memberCoupon={}", mchId, mchCoupon);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        //判断库存数量
        if (mchCoupon.getTotalNum() - mchCoupon.getReceiveNum() <= 0) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_COUPON_GET_OVER));
        }

        //单用户领取数量
        if (mchCoupon.getSingleUserLimit() != MchConstant.MCH_COUPON_NO_SINGLEUSERLIMIT) {
            int couponCount = rpcCommonService.rpcMemberCouponService.count(new QueryWrapper<MemberCoupon>().lambda()
                    .eq(MemberCoupon::getCouponId, couponId)
                    .eq(MemberCoupon::getMemberId, memberId));
            if (mchCoupon.getSingleUserLimit() <= couponCount) {
                return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_COUPON_HAS_GET));
            }
        }

        int count = rpcCommonService.rpcMemberCouponService.addMbrCoupon(mchCoupon, memberId, mchId);
        if (count == 1) return ResponseEntity.ok(BizResponse.buildSuccess());
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 会员已领取的优惠券列表
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        Long memberId = getUser().getMemberId();
        Byte status = getValByteRequired("status");

        //查询会员优惠券领取记录   已领取未使用
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, memberId);
        lambda.eq(MemberCoupon::getStatus, status);
        lambda.eq(MemberCoupon::getMchId, getUser().getMchId());
        lambda.ge(MemberCoupon::getValidateEnd, new Date());
        IPage<MemberCoupon> list = rpcCommonService.rpcMemberCouponService.page(getIPage(), lambda);

        //查询会员已领取  未使用的优惠券
        List couponList = new LinkedList();
        for (MemberCoupon memberCoupon : list.getRecords()) {

            MchCoupon coupon = rpcCommonService.rpcMchCouponService.getOne(
                    new QueryWrapper<MchCoupon>().lambda()
                    .eq(MchCoupon::getCouponId, memberCoupon.getCouponId())
                    .eq(MchCoupon::getStatus, MchConstant.MCH_COUPON_STATUS_NORMAL)
            );

            if (coupon != null){
                JSONObject object = (JSONObject) JSON.toJSON(coupon);
                object.put("validateEnd", memberCoupon.getValidateEnd());
                object.put("couponNo", memberCoupon.getCouponNo());
                couponList.add(object);
            }
        }

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(couponList, couponList.size()));
    }

    /**
     * 会员已领取的优惠券详情
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get( ) {

        String couponNo = getValStringRequired("couponNo");
        //会员优惠券领取记录
        MemberCoupon memberCoupon = rpcCommonService.rpcMemberCouponService.getById(couponNo);
        if (memberCoupon == null || memberCoupon.getStatus() != MchConstant.MEMBER_COUPON_NOT_USED) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS));
        }

        //商户优惠券
        MchCoupon mchCoupon = rpcCommonService.rpcMchCouponService.getById(memberCoupon.getCouponId());
        if (mchCoupon == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS));
        }

        //优惠券所属门店
        String storeNames = "";
        if (mchCoupon.getStoreLimitType() != MchConstant.MCH_STORE_LIMIT_TYPE_NO) {
            Collection<MchCouponStoreRela> listByIds = rpcCommonService.rpcMchCouponStoreRelaService.list(
                    new QueryWrapper<MchCouponStoreRela>().lambda().eq(MchCouponStoreRela::getCouponId, mchCoupon.getCouponId()));
            List couponIdList = new LinkedList();
            if (listByIds.size() != 0){
                for (MchCouponStoreRela rela: listByIds) {
                    couponIdList.add(rela.getStoreId());
                }

                //添加门店名称
                List<MchStore> mchStoreList = rpcCommonService.rpcMchStoreService.list(new QueryWrapper<MchStore>().lambda().
                        in(MchStore::getStoreId, couponIdList));
                for (MchStore store : mchStoreList) {
                    storeNames += store.getStoreName() + "，";
                }
                storeNames = storeNames.substring(0, storeNames.length()-1);
            }
        }

        //前端返回
        JSONObject object = (JSONObject) JSONObject.toJSON(mchCoupon);
        object.put("validateEnd", memberCoupon.getValidateEnd());
        object.put("storeNames", storeNames);

        return ResponseEntity.ok(PageRes.buildSuccess(object));
    }

    /**
     * 会员可用优惠券列表
     */
    @RequestMapping("/canUseCoupon")
    @ResponseBody
    public ResponseEntity<?> canUseCoupon() {

        Long amount = getRequiredAmountL("amount");
        Long storeId = getValLongRequired("storeId");
        Long memberId = getUser().getMemberId();

        //查询会员优惠券领取记录   已领取未使用，在有效期内的
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, memberId);
        lambda.eq(MemberCoupon::getStatus, MchConstant.MEMBER_COUPON_NOT_USED);
        lambda.eq(MemberCoupon::getMchId, getUser().getMchId());
        lambda.ge(MemberCoupon::getValidateEnd, new Date());
        List<MemberCoupon> list = rpcCommonService.rpcMemberCouponService.list(lambda);

        //返回可用优惠券
        List couponList = new LinkedList<>();
        for (MemberCoupon memberCoupon : list) {
            MchCoupon mchCoupon = rpcCommonService.rpcMchCouponService.getOne(new QueryWrapper<MchCoupon>().lambda()
                    .eq(MchCoupon::getCouponId, memberCoupon.getCouponId())
                    .eq(MchCoupon::getStatus, MchConstant.MCH_COUPON_STATUS_NORMAL)
                    .le(MchCoupon::getPayAmountLimit, amount)
            );
            //检查优惠券是否 满足使用条件
            if(mchCoupon != null && rpcCommonService.rpcMemberCouponService.useCouponPreCheck(mchCoupon, amount, storeId) == null){
                JSONObject object = (JSONObject) JSON.toJSON(mchCoupon);
                object.put("couponNo", memberCoupon.getCouponNo());
                couponList.add(object);
            }
        }

        return ResponseEntity.ok(PageRes.buildSuccess(couponList));
    }


}
