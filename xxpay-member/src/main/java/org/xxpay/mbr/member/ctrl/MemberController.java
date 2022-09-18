package org.xxpay.mbr.member.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.*;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member")
public class MemberController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 获取会员详情  账户余额  积分余额  优惠券总数
     */
    @RequestMapping("/getAllMemberInfo")
    @ResponseBody
    public ResponseEntity<?> getAllMemberInfo() {

        Long memberId = getUser().getMemberId();
        Long mchId = getUser().getMchId();

        //会员基本信息
        Member member = rpcCommonService.rpcMemberService.getById(memberId);
        member.setAvatar(StringUtils.isNotBlank(member.getAvatar()) ? member.getAvatar() : MchConstant.MCH_MEMBER_DEFAULT_AVATAR);
        //会员余额
        Long balance = rpcCommonService.rpcMemberBalanceService.getById(memberId).getBalance();
        //会员积分
        Long point = rpcCommonService.rpcMemberPointsService.getById(memberId).getPoints();
        //优惠券总数   同一个商户 同一个会员ID 未使用的
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, memberId);
        lambda.eq(MemberCoupon::getMchId, getUser().getMchId());
        lambda.eq(MemberCoupon::getStatus, MchConstant.MEMBER_COUPON_NOT_USED);
        lambda.ge(MemberCoupon::getValidateEnd, new Date());
        List<MemberCoupon> couponList = rpcCommonService.rpcMemberCouponService.list(lambda);
        //查询会员已领取  未使用的优惠券
        int couponCount = 0;
        for (MemberCoupon memberCoupon : couponList) {

            MchCoupon coupon = rpcCommonService.rpcMchCouponService.getOne(
                    new QueryWrapper<MchCoupon>().lambda()
                            .eq(MchCoupon::getCouponId, memberCoupon.getCouponId())
                            .eq(MchCoupon::getStatus, MchConstant.MCH_COUPON_STATUS_NORMAL)
            );

            if (coupon != null){
                couponCount ++;
            }
        }
        //获取会员卡信息
        MchMemberConfig config = rpcCommonService.rpcMchMemberConfigService.getById(mchId);
        //商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);

        //前端返回
        JSONObject object = (JSONObject) JSON.toJSON(member);
        object.put("balance", balance);//会员余额
        object.put("point", point);//会员积分
        object.put("couponCount", couponCount);//会员优惠券总数
        object.put("memberCardColor", config.getMemberCardColor());//会员卡配色
        object.put("memberCardName", config.getMemberCardName());//会员卡名称
        object.put("memberCardValidTime", config.getMemberCardValidTime());//会员卡有效期
        object.put("mchName", mchInfo.getMchName());//商户名称

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 小程序端 获取会员详情  账户余额  积分余额  优惠券总数，
     * 无需获取会员卡信息
     */
    @RequestMapping("/getAllMemberInfo/withOutCard")
    @ResponseBody
    public ResponseEntity<?> getMemberWithOutCard() {

        Long memberId = getUser().getMemberId();
        Long mchId = getUser().getMchId();

        //会员基本信息
        Member member = rpcCommonService.rpcMemberService.getById(memberId);
        member.setAvatar(StringUtils.isNotBlank(member.getAvatar()) ? member.getAvatar() : MchConstant.MCH_MEMBER_DEFAULT_AVATAR);
        //会员余额
        Long balance = rpcCommonService.rpcMemberBalanceService.getById(memberId).getBalance();
        //会员积分
        Long point = rpcCommonService.rpcMemberPointsService.getById(memberId).getPoints();
        //优惠券总数   同一个商户 同一个会员ID 未使用的
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, memberId);
        lambda.eq(MemberCoupon::getMchId, getUser().getMchId());
        lambda.eq(MemberCoupon::getStatus, MchConstant.MEMBER_COUPON_NOT_USED);
        lambda.ge(MemberCoupon::getValidateEnd, new Date());
        int couponCount = rpcCommonService.rpcMemberCouponService.count(lambda);
        //商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.getById(mchId);

        //前端返回
        JSONObject object = (JSONObject) JSON.toJSON(member);
        object.put("balance", balance);//会员余额
        object.put("point", point);//会员积分
        object.put("couponCount", couponCount);//会员优惠券总数
        object.put("mchName", mchInfo.getMchName());//商户名称

        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 获取会员详情
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long memberId = getUser().getMemberId();
        Member member = rpcCommonService.rpcMemberService.getById(memberId);
        member.setAvatar(StringUtils.isNotBlank(member.getAvatar()) ? member.getAvatar() : MchConstant.MCH_MEMBER_DEFAULT_AVATAR);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(member));
    }

    /**
     * 修改会员信息
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {

        Member member = getObject( Member.class);
        member.setMemberId(getUser().getMemberId());

        boolean result = rpcCommonService.rpcMemberService.updateById(member);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 根据会员手机号获取会员详情和积分
     * @return
     */
    @RequestMapping("/getPointsByTel")
    @ResponseBody
    public ResponseEntity<?> getPointsByTel() {


        String tel = getValStringRequired( "tel");
        Long mchId = getUser().getMchId();

        Member member = rpcCommonService.rpcMemberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getTel,tel));
        if (member == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        //查询积分
        MemberPoints points = rpcCommonService.rpcMemberPointsService.getById(member.getMemberId());

        JSONObject object = (JSONObject) JSON.toJSON(member);
        object.put("points", points.getPoints());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

}
