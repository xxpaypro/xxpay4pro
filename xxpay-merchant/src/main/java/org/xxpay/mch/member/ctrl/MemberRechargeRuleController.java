package org.xxpay.mch.member.ctrl;

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
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.PageRes;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.entity.MchMemberRechargeRule;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

@Controller
@RequestMapping(Constant.MGR_CONTROLLER_ROOT_PATH + "/member_recharge_rule")
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

        MchMemberRechargeRule rechargeRule = getObject( MchMemberRechargeRule.class);

        LambdaQueryWrapper<MchMemberRechargeRule> lambda = new QueryWrapper<MchMemberRechargeRule>().lambda();
        lambda.eq(MchMemberRechargeRule::getMchId, getUser().getBelongInfoId());

        if (getUser().getLoginType().equals(MchConstant.LOGIN_TYPE_WEB)){
            if (rechargeRule.getStatus() != null) lambda.eq(MchMemberRechargeRule::getStatus, rechargeRule.getStatus());
        }else {
            lambda.eq(MchMemberRechargeRule::getStatus, MchConstant.PUB_YES);
        }
        if (rechargeRule.getRechargeAmount() != null) lambda.eq(MchMemberRechargeRule::getRechargeAmount, rechargeRule.getRechargeAmount());
        if (rechargeRule.getGiveCouponId() != null) lambda.eq(MchMemberRechargeRule::getGiveCouponId, rechargeRule.getGiveCouponId());
        if (rechargeRule.getFirstFlag() != null) lambda.eq(MchMemberRechargeRule::getFirstFlag, rechargeRule.getFirstFlag());
        if (rechargeRule.getRuleType() != null) lambda.eq(MchMemberRechargeRule::getRuleType, rechargeRule.getRuleType());
        lambda.orderByDesc(MchMemberRechargeRule::getFirstFlag);
        IPage<MchMemberRechargeRule> list = rpcCommonService.rpcMchMemberRechargeRuleService.page(getIPage(), lambda);
        return ResponseEntity.ok(PageRes.buildSuccess(list));
    }

    /**
     * 会员充值规则详情
     * @return
     */
    @RequestMapping("/get")
    @ResponseBody
    public ResponseEntity<?> get() {

        Long ruleId = getValLongRequired( "ruleId");
        MchMemberRechargeRule rechargeRule = rpcCommonService.rpcMchMemberRechargeRuleService.getById(ruleId);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(rechargeRule));
    }

    /**
     * 新增会员充值规则
     */
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<?> add() {


        //Byte ruleType = getValByteRequired( "ruleType");
        MchMemberRechargeRule rechargeRule = getObject( MchMemberRechargeRule.class);
        //默认所属商户
        rechargeRule.setMchId(getUser().getBelongInfoId());

       /* if (ruleType == MchConstant.MEMBER_RECHARGE_RULE_TYPE_COUPON
                        && !rpcCommonService.rpcMchCouponService.checkCoupon(getValLongRequired( "giveCouponId"))) {
            //TODO
        }*/

        boolean result = rpcCommonService.rpcMchMemberRechargeRuleService.save(rechargeRule);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

    /**
     * 修改会员充值规则
     */
    @RequestMapping("/update")
    @ResponseBody
    public ResponseEntity<?> update() {


        Long ruleId = getValLongRequired( "ruleId");
        MchMemberRechargeRule rechargeRule = getObject( MchMemberRechargeRule.class);

        boolean result = rpcCommonService.rpcMchMemberRechargeRuleService.updateById(rechargeRule);
        if (!result) return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        return ResponseEntity.ok(BizResponse.buildSuccess());
    }

}
