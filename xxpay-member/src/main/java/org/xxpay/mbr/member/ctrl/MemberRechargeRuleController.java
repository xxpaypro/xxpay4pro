package org.xxpay.mbr.member.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayPageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchCoupon;
import org.xxpay.core.entity.MchMemberRechargeRule;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/member_recharge_rule")
public class MemberRechargeRuleController extends BaseController {

    @Autowired
    private RpcCommonService rpcCommonService;

    /**
     * 会员充值规则列表
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public ResponseEntity<?> list() {

        LambdaQueryWrapper<MchMemberRechargeRule> lambda = new QueryWrapper<MchMemberRechargeRule>().lambda();
        lambda.eq(MchMemberRechargeRule::getMchId, getUser().getMchId());
        lambda.eq(MchMemberRechargeRule::getStatus, MchConstant.PUB_YES);
        lambda.orderByDesc(MchMemberRechargeRule::getFirstFlag);
        IPage<MchMemberRechargeRule> list = rpcCommonService.rpcMchMemberRechargeRuleService.page(getIPage(true), lambda);

        List respList = new LinkedList();

        for (MchMemberRechargeRule rechargeRule : list.getRecords()) {
            JSONObject object = (JSONObject) JSON.toJSON(rechargeRule);

            if (rechargeRule.getRuleType() == MchConstant.MEMBER_RECHARGE_RULE_TYPE_COUPON && rechargeRule.getGiveCouponId() != null){
                MchCoupon coupon = rpcCommonService.rpcMchCouponService.getById(rechargeRule.getGiveCouponId());
                if (coupon != null && coupon.getStatus() == MchConstant.PUB_YES) {
                    object.put("payAmountLimit", coupon.getPayAmountLimit());
                    object.put("couponAmount", coupon.getCouponAmount());
                }
            }
            respList.add(object);
        }

        return ResponseEntity.ok(XxPayPageRes.buildSuccess(respList, respList.size()));
    }

    /**
     * 会员充值规则详情
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long ruleId = getValLong("ruleId");

        MchMemberRechargeRule rule = null;
        String couponName = "";
        if (ruleId != null) {
            rule = rpcCommonService.rpcMchMemberRechargeRuleService.getById(ruleId);
            if (rule == null || rule.getStatus() != MchConstant.PUB_YES){
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_NO_SUCH_RECHARGE_RULE));
            }

            //如果为赠优惠券
            if (rule.getRuleType() == MchConstant.MEMBER_RECHARGE_RULE_TYPE_COUPON) {
                if (rule.getGiveCouponId() != null) {
                    MchCoupon coupon = rpcCommonService.rpcMchCouponService.getById(rule.getGiveCouponId());
                    if (coupon != null){
                        couponName = coupon.getCouponName();
                    }
                }
            }
        }

        //前端返回
        JSONObject object = (JSONObject) JSONObject.toJSON(rule);
        object.put("couponName", couponName);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(object));
    }

}
