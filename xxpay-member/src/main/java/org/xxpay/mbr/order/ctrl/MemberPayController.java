package org.xxpay.mbr.order.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.Constant;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.*;
import org.xxpay.mbr.common.ctrl.BaseController;
import org.xxpay.mbr.common.service.RpcCommonService;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 会员支付接口
 */
@Controller
@RequestMapping(Constant.MBR_CONTROLLER_ROOT_PATH + "/memberPay")
public class MemberPayController extends BaseController {

    private static final MyLog _log = MyLog.getLog(MemberPayController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RpcCommonService rpc;

    private static AtomicLong seq = new AtomicLong(0L);

    /**
     * 会员充值接口(仅微信支付)
     */
    @RequestMapping("/recharge")
    @ResponseBody
    public ResponseEntity<?> recharge() {

        //1. 判断请求是否来自微信
        String userAgent = request.getHeader("User-Agent");
        if (StringUtils.isBlank(userAgent)  || userAgent.toLowerCase().indexOf("micromessenger") == -1) {  // 不是来自微信客户端
            throw ServiceException.build(RetEnum.RET_MBR_USE_WECHAT);
        }

        //2. 获取参数
        Long memberId = getUser().getMemberId();//会员ID
        Long mchId = getUser().getMchId(); //商户ID
        String wxOpenId = getUser().getWxOpenId(); //微信OpenId
        String amountStr = getValStringRequired("amount");  //充值金额
        Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(amountStr));
        Long ruleId = getValLong("ruleId"); //充值规则ID
        Byte authFrom = getValByte("authFrom");//第三方平台充值入口，1-餐饮小程序 2-商城小程序

        // 默认是微信公众号支付
        int productId = PayConstant.PAY_PRODUCT_WX_JSAPI;
        String appId = null;

        if (authFrom != null && (authFrom == MchConstant.MEMBER_OPENID_FROM_MINI_FOOD || authFrom == MchConstant.MEMBER_OPENID_FROM_MINI_MALL
                || authFrom == MchConstant.MEMBER_OPENID_FROM_MINI_PAY)){
            LambdaQueryWrapper<MemberOpenidRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MemberOpenidRela::getMemberId, memberId);
            queryWrapper.eq(MemberOpenidRela::getWxOpenIdFrom, authFrom);
            queryWrapper.eq(MemberOpenidRela::getMchId, mchId);
            MemberOpenidRela openidRela = rpc.rpcMemberOpenidRelaService.getOne(queryWrapper);
            if (openidRela == null){
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
            }
            wxOpenId = openidRela.getWxOpenId();
            productId = PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM;

            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
            if (mchWxauthInfo == null || mchWxauthInfo.getAuthStatus() != 1){
                _log.error("=================商户小程序未授权{}=================", mchId);
                throw new ServiceException(RetEnum.RET_ISV_WX_MCH_NOT_AUTH);
            }
            appId = mchWxauthInfo.getAuthAppId();// 得到微信小程序appId

        }

        final String finalWxOpenId = wxOpenId;
        final String finalAppId = appId;

        if (ruleId != null) {
            MchMemberRechargeRule rule = rpc.rpcMchMemberRechargeRuleService.getById(ruleId);
            amount = rule.getRechargeAmount();
        }

        //金额有误
        if(amount <= 0) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
        //[3.] 创建交易订单
        String ipInfo = IPUtility.getClientIp(request);
        Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrder(
                mchInfo, MchConstant.TRADE_TYPE_RECHARGE, amount, amount, tradeProductType, productId, ipInfo, null, null,
                (mchTradeOrder) -> {
                    mchTradeOrder.setUserId(memberId + ""); //用户ID
                    mchTradeOrder.setChannelUserId(finalWxOpenId); //支付渠道用户ID
                    mchTradeOrder.setAppId(finalAppId); // 交易场景下的appID
                    mchTradeOrder.setRuleId(ruleId); //充值规则ID
                    mchTradeOrder.setMemberId(memberId); //记录memberId
                    mchTradeOrder.setMemberTel(getUser().getTel()); //会员手机号
                });

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

        JSONObject result = new JSONObject();

        //[4.] 调起 [支付网关] 接口
        JSONObject retJSON = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, mchInfo, mainConfig.getPayUrl(), mainConfig.getNotifyUrl());
        if(retJSON == null) { //支付网关返回结果验证失败
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_PAY_ERROR));
        }else{
            result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", addMchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", addMchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", addMchTradeOrder.getAmount()); //实际支付金额
            result.put("payParam", retJSON.getString("payParams")); //支付参数
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /**
     * 会员付款码
     */
    @RequestMapping("/payCode")
    @ResponseBody
    public ResponseEntity<?> payCode() {

        Long memberId = getUser().getMemberId();
        String payCode = getPayCode();

        //付款码条码和二维码地址
        String barCodeImgUrl = String.format("%s/payment/barCode?barNo=%s", mainConfig.getMbrApiUrl(), payCode);
        String codeImgUrl = String.format("%s/payment/code?codeNo=%s", mainConfig.getMbrApiUrl(), payCode);

        //付款码添加到redis, 有效期5分钟
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR_CODE + payCode, String.valueOf(memberId),
                MchConstant.CACHE_MBR_CODE_TIMEOUT, TimeUnit.SECONDS);

        //前端返回
        JSONObject data = new JSONObject();
        data.put("payCode", payCode);
        data.put("barCodeImgUrl", barCodeImgUrl);
        data.put("codeImgUrl", codeImgUrl);

        return ResponseEntity.ok(XxPayResponse.buildSuccess(data));
    }

    /**
     * 微信支付
     */
    @RequestMapping("/order_pay_wx")
    @ResponseBody
    public ResponseEntity<?> orderPayWx() {

        Long mchId = getUser().getMchId();//商户ID
        Long memberId = getUser().getMemberId();//会员ID
        String wxOpenId = getUser().getWxOpenId(); //微信OpenId
        String tradeOrderId = getValStringRequired("tradeOrderId"); //交易单号
        Byte authFrom = getValByteRequired("authFrom");//来源小程序，1-餐饮小程序 2-商城小程序

        //校验订单
        MchTradeOrder mchTradeOrder = rpc.rpcMchTradeOrderService.getById(tradeOrderId);
        if(mchTradeOrder == null || !mchTradeOrder.getMemberId().equals(getUser().getMemberId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }
        if (mchTradeOrder.getStatus() == MchConstant.TRADE_ORDER_STATUS_SUCCESS) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_TRADEORDER_ALREADY_SUCCESS));
        }

        // 默认是微信公众号支付
        int productId = PayConstant.PAY_PRODUCT_WX_JSAPI;
        String appId = null;

        if (authFrom == MchConstant.MEMBER_OPENID_FROM_MINI_FOOD || authFrom == MchConstant.MEMBER_OPENID_FROM_MINI_MALL){
            LambdaQueryWrapper<MemberOpenidRela> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MemberOpenidRela::getMemberId, memberId);
            queryWrapper.eq(MemberOpenidRela::getWxOpenIdFrom, authFrom);
            queryWrapper.eq(MemberOpenidRela::getMchId, mchId);
            MemberOpenidRela openidRela = rpc.rpcMemberOpenidRelaService.getOne(queryWrapper);
            if (openidRela == null){
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_COMM_PARAM_ERROR));
            }
            wxOpenId = openidRela.getWxOpenId();
            productId = PayConstant.PAY_PRODUCT_WX_MINI_PROGRAM;

            MchWxauthInfo mchWxauthInfo = rpc.rpcMchWxauthInfoService.getOneByMchIdAndAuthFrom(mchId, authFrom);
            if (mchWxauthInfo == null || mchWxauthInfo.getAuthStatus() != 1){
                _log.error("=================商户小程序未授权{}=================", mchId);
                throw new ServiceException(RetEnum.RET_ISV_WX_MCH_NOT_AUTH);
            }
            appId = mchWxauthInfo.getAuthAppId();// 得到微信小程序appId

        }

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(mchTradeOrder.getTradeOrderId());
        updateRecord.setChannelUserId(wxOpenId); //支付渠道用户ID
        updateRecord.setProductType(MchConstant.MCH_TRADE_PRODUCT_TYPE_WX);
        updateRecord.setProductId(productId);
        updateRecord.setAppId(appId);
        rpc.rpcMchTradeOrderService.updateById(updateRecord);

        //数据库最新订单记录
        MchTradeOrder mchTradeOrderLastest = rpc.rpcMchTradeOrderService.getById(tradeOrderId);

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        JSONObject result = new JSONObject();

        //调起 [支付网关] 接口
        JSONObject retJSON = rpc.rpcMchTradeOrderService.callPayInterface(mchTradeOrderLastest, mchInfo, mainConfig.getPayUrl(), mainConfig.getNotifyUrl());
        if(retJSON == null) { //支付网关返回结果验证失败
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_PAY_ERROR));
        }else{
            result.put("tradeOrderId", mchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", mchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", mchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", mchTradeOrder.getAmount()); //实际支付金额
            result.put("payParam", retJSON.getString("payParams")); //支付参数
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /**
     * 会员余额支付
     */
    @RequestMapping("/order_pay_balance")
    @ResponseBody
    public ResponseEntity<?> orderPayBalance() {

        String tradeOrderId = getValStringRequired("tradeOrderId"); //交易单号

        //校验订单
        MchTradeOrder mchTradeOrder = rpc.rpcMchTradeOrderService.getById(tradeOrderId);
        if(mchTradeOrder == null || !mchTradeOrder.getMemberId().equals(getUser().getMemberId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }
        if (mchTradeOrder.getStatus() == MchConstant.TRADE_ORDER_STATUS_SUCCESS) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_TRADEORDER_ALREADY_SUCCESS));
        }

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(mchTradeOrder.getTradeOrderId());
        updateRecord.setProductType(MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD);
        rpc.rpcMchTradeOrderService.updateById(updateRecord);

        //更新会员账户信息数据 & 积分数据 & 优惠券
        int updatRow = rpc.rpcMchTradeOrderService.updateSuccess4MemberBalance(mchTradeOrder.getTradeOrderId(), getUser().getMemberId(), mchTradeOrder.getMemberCouponNo());

        //返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("orderStatus", mchTradeOrder.getStatus());

        if(updatRow > 0){
            result.put("orderStatus", MchConstant.TRADE_ORDER_STATUS_SUCCESS);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /**
     * 货到付款购买商品
     */
    @RequestMapping("/payByCod")
    @ResponseBody
    public ResponseEntity<?> payByCod() {

        String tradeOrderId = getValStringRequired("tradeOrderId"); //交易单号

        //校验订单
        MchTradeOrder mchTradeOrder = rpc.rpcMchTradeOrderService.getById(tradeOrderId);
        if(mchTradeOrder == null || !mchTradeOrder.getMemberId().equals(getUser().getMemberId())){
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST));
        }
        if (mchTradeOrder.getStatus() == MchConstant.TRADE_ORDER_STATUS_SUCCESS) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_TRADEORDER_ALREADY_SUCCESS));
        }

        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(mchTradeOrder.getTradeOrderId());
        updateRecord.setProductType(MchConstant.MCH_TRADE_PRODUCT_TYPE_COD);
        rpc.rpcMchTradeOrderService.updateById(updateRecord);

        //更新会员账户信息数据 & 积分数据 & 优惠券
        int updatRow = rpc.rpcMchTradeOrderService.updateSuccess4Cod(mchTradeOrder.getTradeOrderId(), getUser().getMemberId(), mchTradeOrder.getMemberCouponNo());

        //返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("orderStatus", mchTradeOrder.getStatus());

        if(updatRow > 0){
            result.put("orderStatus", MchConstant.TRADE_ORDER_STATUS_SUCCESS);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /**
     * 小程序提交订单
     */
    @RequestMapping("/create_order")
    @ResponseBody
    public ResponseEntity<?> createOrder() {

        //1. 获取参数
        Long memberId = getUser().getMemberId();//会员ID
        Long mchId = getUser().getMchId(); //商户ID
        Long storeId = getValLong("storeId");//门店ID
        Long requiredAmount = getRequiredAmountL("requiredAmount"); //应付金额
        Long realAmount = getRequiredAmountL("realAmount");  //实付金额
        Byte industryType = getValByteRequired("industryType");//所属行业 1-餐饮 2-电商
        Byte postType = getValByteRequired("postType");//1-堂食 2-外卖
        Long storeAreaId = getValLong("storeAreaId");//餐桌号ID
        Long addressId = getValLong("addressId");//收货地址ID
        String goodsDesc = getValStringRequired("goodsDesc");//商品信息
        String couponNo = getValString("couponNo"); //会员优惠券核销码
        String appointmentTime = getValString("appointmentTime");//预约时间
        String remark = getValString("remark");//备注

        //金额有误
        if(requiredAmount <= 0 || realAmount <= 0) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));

        //商品金额

        Long discountAmount = 0L;  //优惠金额
        Long mchCouponId = null;

        //选择了优惠券
        if(StringUtils.isNotEmpty(couponNo)){

            MemberCoupon memberCoupon = rpc.rpcMemberCouponService.getOne(new QueryWrapper<MemberCoupon>()
                    .lambda().eq(MemberCoupon::getCouponNo, couponNo).eq(MemberCoupon::getMemberId, memberId));

            //判断优惠券是否可用
            if(memberCoupon == null || memberCoupon.getStatus() != MchConstant.PUB_NO){
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR));
            }

            MchCoupon mchCoupon = rpc.rpcMchCouponService.getById(memberCoupon.getCouponId());

            // 判断商户优惠券状态是否可用
            if (mchCoupon.getStatus() != MchConstant.MCH_COUPON_STATUS_NORMAL) {
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS));
            }

            //检查优惠券是否 满足使用条件
            String checkResult = rpc.rpcMemberCouponService.useCouponPreCheck(mchCoupon, requiredAmount, storeId);
            if(StringUtils.isNotBlank(checkResult)){
                return ResponseEntity.ok(XxPayResponse.buildErr(checkResult));
            }
            mchCouponId = mchCoupon.getCouponId();
            discountAmount = mchCoupon.getCouponAmount();
        }

        //应付金额 = 实付金额 + 优惠金额 = 商品总价 + 运费
        if (requiredAmount != realAmount + discountAmount) {
            return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MCH_STORE_SPEAKER_CODE_ERROR));
        }

        final Long finalMchCouponId = mchCouponId;

        //运费，未配置按0计算
        //堂食运费为0
        Long postPrice = 0L;
        if (industryType == MchConstant.MCH_INDUSTRY_TYPE_FOOD && postType == MchConstant.MCH_POST_TYPE_WAIMAI) {

            MchStoreTakeout mchStoreTakeout = rpcCommonService.rpcMchStoreTakeoutService.getOne(
                    new QueryWrapper<MchStoreTakeout>().lambda()
                            .eq(MchStoreTakeout::getMchId, getUser().getMchId())
            );
            if (mchStoreTakeout == null) {
                postPrice = 0L;
            }else if(mchStoreTakeout.getDeliveryCost() > requiredAmount){
                return ResponseEntity.ok(XxPayResponse.build(RetEnum.RET_MBR_ORDERAMOUNT_LESS_THAN_DELIVERY));
            }else if (mchStoreTakeout.getFreeDistribution() != null && mchStoreTakeout.getFreeDistribution() <= requiredAmount){
                postPrice = 0L;
            }else {
                postPrice = mchStoreTakeout.getDistributionCost();
            }
        }

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 创建交易订单
        String ipInfo = IPUtility.getClientIp(request);
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrderMiniProgram(mchInfo, MchConstant.TRADE_TYPE_PAY,
                requiredAmount, realAmount, postType, postPrice, null, null, ipInfo, storeId, addressId, storeAreaId, goodsDesc,
                (mchTradeOrder) -> {
                    mchTradeOrder.setUserId(memberId + ""); //用户ID
                    mchTradeOrder.setMchCouponId(finalMchCouponId); //优惠券ID
                    mchTradeOrder.setMemberCouponNo(couponNo); //会员优惠券核销码

                    mchTradeOrder.setMemberId(memberId); //记录memberId
                    mchTradeOrder.setMemberTel(getUser().getTel()); //会员手机号
                    mchTradeOrder.setIndustryType(industryType);//所属行业 1-餐饮 2-电商
                    mchTradeOrder.setAppointmentTime(DateUtil.str2date(appointmentTime));//预约时间
                    mchTradeOrder.setRemark(remark);//备注
                });

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_PAY_ORDER_FAIL);

        //[3.] 返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId());

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    private String getPayCode() {

        String prefix = "6";

        int random = (int) (Math.random()*9000 + 1000);

        String currTime = String.valueOf(System.currentTimeMillis());
        String time = currTime.substring(currTime.length()-10);

        return String.format("%s%s%03d%s", prefix, random, seq.getAndIncrement() % 100, time);
    }


}
