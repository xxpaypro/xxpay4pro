package org.xxpay.mch.member.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
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
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MemberCoupon;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/member_coupon")
public class MemberCouponController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    private static final MyLog _log = MyLog.getLog(MemberCouponController.class);

    /**
     * 会员优惠券领取记录
     */
    @RequestMapping("/getCouponRecord")
    @ResponseBody
    public ResponseEntity<?> getCouponRecord( Integer page, Integer limit) {

        MemberCoupon memberCoupon = getObject( MemberCoupon.class);

        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMchId, getUser().getBelongInfoId());
        if (memberCoupon.getCouponId() != null) lambda.eq(MemberCoupon::getCouponId, memberCoupon.getCouponId());
        if (memberCoupon.getStatus() != null) lambda.eq(MemberCoupon::getStatus, memberCoupon.getStatus());
        if (StringUtils.isNotEmpty(memberCoupon.getCouponNo())) lambda.eq(MemberCoupon::getCouponNo, memberCoupon.getCouponNo());
        if (memberCoupon.getMemberId() != null) lambda.eq(MemberCoupon::getMemberId, memberCoupon.getMemberId());
        lambda.orderByDesc(MemberCoupon::getCreateTime);

        IPage<MemberCoupon> list = rpcCommonService.rpcMemberCouponService.page(new Page<>(page, limit), lambda);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 会员优惠券核销
     *
     */
    @RequestMapping("/checkCoupon")
    @ResponseBody
    public ResponseEntity<?> checkCoupon() {

        String couponNo = getValStringRequired( "couponNo");
        String money = getValStringRequired("amount");
        _log.info("开始核销couponNo={}，money={}", couponNo, money);
        Long amount = Long.valueOf(AmountUtil.convertDollar2Cent(money));
        MemberCoupon memberCoupon = rpcCommonService.rpcMemberCouponService.getById(couponNo);
        if (memberCoupon == null) {
            _log.info("该优惠券不存在，couponNo={}", couponNo);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        if (memberCoupon.getStatus() != MchConstant.MEMBER_COUPON_NOT_USED) {
            _log.info("该优惠券状态错误，memberCoupon={}", memberCoupon);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR));
        }
        //获取当前登录人商户ID
        Long mchId = getUser().getBelongInfoId();
        //判断当前优惠券与当前门店商户是否一致
        if (!memberCoupon.getMchId().equals(mchId)) {
            _log.info("该优惠券非{}商户下优惠券，memberCoupon={}", mchId, memberCoupon);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        //查询优惠券信息
        MchCoupon mchCoupon = rpcCommonService.rpcMchCouponService.getById(memberCoupon.getCouponId());
        if (mchCoupon == null) {
            _log.info("优惠券信息不存在");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }
        //判断活动是否已结束
        if (mchCoupon.getStatus() == MchConstant.MEMBER_COUPON_TIME_OUT) {
            _log.info("活动已过期{}", mchCoupon.getStatus());
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR));
        }
        //判断金额是否符合条件
        if (mchCoupon.getPayAmountLimit() > amount){
            _log.info("优惠券金额小于最低消费金额");
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_LIMIT_AMOUNT_ERROR));
        }

        Boolean result = rpcCommonService.rpcMemberCouponService.checkCoupon(memberCoupon);

        JSONObject resultJSON = new JSONObject();
        resultJSON.put("couponAmount", mchCoupon.getCouponAmount());
        if (result) return ResponseEntity.ok(XxPayResponse.buildSuccess(resultJSON));
        return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
    }

    /**
     * 会员优惠券核销记录
     */
    @RequestMapping("/checkRecord")
    @ResponseBody
    public ResponseEntity<?> checkRecord( Integer page, Integer limit) {

        //优惠券列表
        LambdaQueryWrapper<MchCoupon> mchCouponLambda = new QueryWrapper<MchCoupon>().lambda();
        mchCouponLambda.orderByDesc(MchCoupon::getCreateTime);
        IPage<MchCoupon> list = rpcCommonService.rpcMchCouponService.page(new Page<>(page, limit), mchCouponLambda);

        //统计核销张数
        List checkList = new LinkedList();
        for (MchCoupon mchCoupon : list.getRecords()) {
            LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
            lambda.eq(MemberCoupon::getMchId, getUser().getBelongInfoId());
            lambda.eq(MemberCoupon::getStatus, MchConstant.PUB_YES);
            lambda.eq(MemberCoupon::getCouponId, mchCoupon.getCouponId());
            int count = rpcCommonService.rpcMemberCouponService.count(lambda);
            JSONObject json = (JSONObject) JSONObject.toJSON(mchCoupon);
            json.put("checkCount", count);
            checkList.add(json);
        }

        return ResponseEntity.ok(PageRes.buildSuccess(checkList));
    }


}
