package org.xxpay.mch.member.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.StrUtil;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/member")
public class MemberController extends BaseController {

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 会员列表
     * @return
     */
    @RequestMapping("/list")
    public ResponseEntity<?> list( Integer page, Integer limit) {


        String memberNo = getValString( "memberNo");

        LambdaQueryWrapper<Member> lambda = new QueryWrapper<Member>().lambda();

        lambda.eq(Member::getMchId, getUser().getBelongInfoId());
         if (StringUtils.isNotBlank(memberNo)) {
            lambda.and(i -> i.eq(Member::getMemberNo, memberNo)
                    .or().eq(Member::getTel, memberNo)
                    .or().like(Member::getMemberName, memberNo)
            );
        }
        
        lambda.orderByDesc(Member::getCreateTime);

        IPage<Member> list = rpc.rpcMemberService.page(new Page<>(page, limit), lambda);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 根据会员ID获取会员详情
     * @return
     */
    @RequestMapping("/get")
    public ResponseEntity<?> get() {

        Long memberId = getValLongRequired( "memberId");
        Member member = rpc.rpcMemberService.getById(memberId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(member));
    }

    /**
     * 获取会员详情  账户余额  积分余额  优惠券总数
     */
    @RequestMapping("/getAllMemberInfo")
    public ResponseEntity<?> getAllMemberInfo() {

        //查询会员所依据的类别【1或不传：会员ID；2：卡号或手机号；3：付款码】
        Integer type = getValInteger("type");
        //所依据类别的值
        Long memberId = getValLongRequired( "memberId");

        LambdaQueryWrapper<Member> memberLambda = new QueryWrapper<Member>().lambda();
        memberLambda.eq(Member::getMchId, getUser().getBelongInfoId());

        //根据不同类别生成不同的查询条件
        if (type == null || type == 1) {
            memberLambda.eq(Member::getMemberId, memberId);
        }else if(type == 2) {
            memberLambda.and(i -> i.eq(Member::getMemberNo, memberId)
                    .or().eq(Member::getTel, memberId)
            );
        }else if(type == 3) {
            String memberIdStr = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR_CODE + memberId);
            if(StringUtils.isEmpty(memberIdStr)){
                throw new ServiceException(RetEnum.RET_MCH_NOT_EXISTS_PAYCODE); //无法获取会员付款码
            }
            memberLambda.eq(Member::getMemberId, Long.parseLong(memberIdStr));
        }else{
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MEMBER_TYPE_NOT_EXISTS));
        }

        //会员基本信息
        Member member = rpc.rpcMemberService.getOne(memberLambda);
        if (member == null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MEMBER_NOT_EXISTS));
        }

        //会员余额
        Long balance = rpc.rpcMemberBalanceService.getById(member.getMemberId()).getBalance();
        //会员积分
        Long point = rpc.rpcMemberPointsService.getById(member.getMemberId()).getPoints();
        //优惠券总数   同一个商户 同一个会员ID 未使用的
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, member.getMemberId());
        lambda.eq(MemberCoupon::getMchId, getUser().getBelongInfoId());
        lambda.eq(MemberCoupon::getStatus, MchConstant.MEMBER_COUPON_NOT_USED);
        int couponCount = rpc.rpcMemberCouponService.count(lambda);

        //前端返回
        JSONObject object = (JSONObject) JSON.toJSON(member);
        object.put("balance", balance);
        object.put("point", point);
        object.put("couponCount", couponCount);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

    /**
     * 新增会员  会员储值账户  会员积分账户
     */
    @RequestMapping("/add")
    public ResponseEntity<?> add() {

        Member member = getObject( Member.class);

        if(!StrUtil.checkMobileNumber(member.getTel())) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_FORMAT_ERROR));
        }

        String amountStr = getValString("amount");  // 前端填写的为元,可以为小数点2位
        Long amount = 0L;
        Long points = getValLong("points") == null ? 0L : getValLong("points");//初始积分
        if (StringUtils.isNotBlank(amountStr)) {
            amount = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        }

        //默认所属商户
        member.setMchId(getUser().getBelongInfoId());
        //默认状态为 可用。
        member.setStatus(MchConstant.PUB_YES);
        //性别空  则加默认值
        if (member.getSex() == null)member.setSex(MchConstant.SEX_UNKNOWN);

        //同一商户下，会员手机号唯一
        if (member.getTel() != null && rpc.rpcMemberService.getByMchIdAndTel(member) != null) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
        }

        int updateCount = rpc.rpcMemberService.initMember(member, amount, points, getUser().getUserId(), getUser().getNickName());
        if (updateCount != 1) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改会员信息
     */
    @RequestMapping("/update")
    public ResponseEntity<?> update() {

	    Member member = getObject( Member.class);

	    //同一商户下，会员手机号唯一
	    member.setMchId(getUser().getBelongInfoId());
	    if (StringUtils.isNotBlank(member.getTel())) {
	        Member dbMember = rpc.rpcMemberService.getByMchIdAndTel(member);
	        if (dbMember != null && !dbMember.getMemberId().equals(member.getMemberId())){
	            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_MOBILE_USED));
	        }
	    }

	    boolean result = rpc.rpcMemberService.updateById(member);
	    if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
	    return ResponseEntity.ok(BizResponse.buildSuccess());
	}

    /**
     * 会员已领取的优惠券列表
     */
    @RequestMapping("/memberGetCouponList")
    public ResponseEntity<?> memberGetCouponList( Integer page, Integer limit) {

        Long memberId = getValLongRequired( "memberId");

        //查询会员优惠券领取记录   已领取未使用
        LambdaQueryWrapper<MemberCoupon> lambda = new QueryWrapper<MemberCoupon>().lambda();
        lambda.eq(MemberCoupon::getMemberId, memberId);
        lambda.eq(MemberCoupon::getStatus, MchConstant.MEMBER_COUPON_NOT_USED);
        lambda.eq(MemberCoupon::getMchId, getUser().getBelongInfoId());
        IPage<MemberCoupon> list = rpc.rpcMemberCouponService.page(new Page<>(page, limit), lambda);

        //查询会员已领取  未使用的优惠券
        List couponList = new LinkedList();
        for (MemberCoupon memberCoupon : list.getRecords()) {
            MchCoupon coupon = rpc.rpcMchCouponService.getById(memberCoupon.getCouponId());
            if (coupon != null){
                JSONObject object = (JSONObject) JSON.toJSON(coupon);
                object.put("validateEnd", memberCoupon.getValidateEnd());
                couponList.add(object);
            }
        }

        /*
        IPage<MchCoupon> memberCouponList = rpcCommonService.rpcMchCouponService.page(new Page<>(page, limit), lambda2);
        //返回
        List<Object> couponList = new LinkedList();

        //会员领取的多张相同优惠券的张数
        for (MchCoupon mchCoupon : memberCouponList.getRecords()) {
            JSONObject object = (JSONObject) JSON.toJSON(mchCoupon);
            //相同优惠券张数
            object.put("sameCouponCount", Collections.frequency(couponIdList, mchCoupon.getCouponId()));
            //会员优惠券有效时间
            for (MemberCoupon memberCoupon : list) {
                if (mchCoupon.getCouponId().equals(memberCoupon.getCouponId())) {
                    object
                }
            }
            couponList.add(object);
        }*/
        return ResponseEntity.ok(PageRes.buildSuccess(couponList));
    }

    /**
     * 根据会员手机号获取会员详情和积分
     * @return
     */
    @RequestMapping("/getPointsByTel")
    public ResponseEntity<?> getPointsByTel() {


        String tel = getValStringRequired( "tel");
        Long mchId = getUser().getBelongInfoId();

        Member member = rpc.rpcMemberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getTel,tel));
        if (member == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }
        //查询积分
        MemberPoints points = rpc.rpcMemberPointsService.getById(member.getMemberId());

        JSONObject object = (JSONObject) JSON.toJSON(member);
        object.put("points", points.getPoints());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }
	
	
	    /**
     * 1.获取会员系统领卡链接二维码
     * 2.开卡赠送信息
     */
    @RequestMapping("/getCodeImg")
    public ResponseEntity<?> getCodeImg() {

        Long mchId = getUser().getBelongInfoId();

        //获取会员卡配置信息
        String firstText = "";
        String secondText = "";
        MchMemberConfig memberConfig = rpcCommonService.rpcMchMemberConfigService.getById(mchId);
        if (memberConfig == null) {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));
        }

        if (memberConfig.getNewMemberRule() == MchConstant.MEMBER_RECHARGE_RULE_TYPE_BALANCE) {
            firstText = "首次领取会员卡赠送" + divide(memberConfig.getNewMemberGivePoints()) + "元";
        }else if (memberConfig.getNewMemberRule() == MchConstant.MEMBER_RECHARGE_RULE_TYPE_POINT) {
            firstText = "首次领取会员卡赠送" + memberConfig.getNewMemberGivePoints() + "积分";
        }

        secondText = "消费满" + divide(memberConfig.getConsumeAmount()) + "元赠送" + memberConfig.getConsumeGivePoints() + "积分";

        String codeUrl = String.format("%s?mchId=%s", mainConfig.getMbrAddUrl(), mchId);

        JSONObject data = new JSONObject();
        data.put("codeUrl", codeUrl);
        data.put("codeImgUrl", mainConfig.getMchApiUrl() +  "/payment/qrcode_img_get?url=" + URLEncoder.encode(codeUrl) + "&widht=200&height=200");
        data.put("firstText", firstText);
        data.put("secondText", secondText);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /** 根据openId  查询会员信息， 包括，会员卡余额， 会员优惠券列表，  **/
    @RequestMapping("/memberInfoAndCoupons")
    public XxPayResponse memberInfoAndCoupons() {

        Long currentMchId = getUser().getBelongInfoId();  //当前商户ID
        Long currentStoreId = getUser().getStoreId(); //当前用户的门店ID
        String wxOpenId = getValStringRequired("wxOpenId");  //wxOpenId
        Long payAmount = getAmountL("payAmount");  //支付金额, 单位元， 自动转换为分(非必填)

        try {

            //查询到的会员信息
            JSONObject memberInfo = rpc.rpcMemberService.selectMemberAllInfo(currentMchId, null, wxOpenId);
            Long memberId = memberInfo != null ? memberInfo.getLong("memberId") : null;

            if(memberId == null){ //不存在的会员信息
                return XxPayResponse.buildSuccess(new JSONObject());
            }

            JSONArray receiveCouponList = rpc.rpcMemberCouponService.selectMemberReceiveCouponList(memberId, currentMchId, currentStoreId, payAmount);
            memberInfo.put("receiveCouponList", receiveCouponList);
            return XxPayResponse.buildSuccess(memberInfo);
        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            logger.error("error: wxOpenId={}, payAmount={} ", wxOpenId, payAmount, e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    private BigDecimal divide(Long amount) {
        return new BigDecimal(amount).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
    }

}
