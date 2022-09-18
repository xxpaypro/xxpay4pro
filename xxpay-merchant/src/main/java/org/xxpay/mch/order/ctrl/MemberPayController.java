package org.xxpay.mch.order.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.*;
import org.xxpay.core.common.domain.BizResponse;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.*;
import org.xxpay.mch.common.ctrl.BaseController;
import org.xxpay.mch.common.service.RpcCommonService;

import java.util.Date;

/** 会员支付相关接口 */
@RestController
@RequestMapping(Constant.MCH_CONTROLLER_ROOT_PATH + "/memberPay")
public class MemberPayController extends BaseController {

    private static final MyLog _log = MyLog.getLog(MemberPayController.class);

    @Autowired
    private RpcCommonService rpc;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /** 收款（商扫客） **/
    @RequestMapping("/bar")
    public ResponseEntity<?> bar() {

        //[1.] 获取当前登录用户的基本信息
        Long mchId = getUser().getBelongInfoId(); //获取mchId
        MchInfo currentMchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 获取请求参数并验证参数是否合法
        Long requiredAmount = getRequiredAmountL("requiredAmount"); //应付金额
        Long realAmount = getRequiredAmountL("realAmount");  //实付金额
        String barCode = getValStringRequired("barCode").trim();   //条码
        String isvDeviceNo = getUser().getLoginDeviceNo();   //服务商设备编号

        //是否押金模式
        Byte depositMode = "1".equals(getValString("isDepositMode")) ? MchConstant.PUB_YES : MchConstant.PUB_NO;
        boolean isDepositMode = depositMode == MchConstant.PUB_YES;

        //金额是否合法
        if(requiredAmount <= 0 || realAmount <= 0) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

        //实付金额 > 应付金额
        if(realAmount > requiredAmount) throw ServiceException.build(RetEnum.RET_MCH_REAL_AMOUNT_GT_REQUIRED);

        //获取支付产品，并校验barCode是否可用
        int productId = XXPayUtil.getProductIdByBarCode(barCode);
        Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType

        //押金模式
        if(isDepositMode){

            //不允许使用优惠金额
            if(!realAmount.equals(requiredAmount)){
                throw ServiceException.build(RetEnum.RET_MCH_ORDER_DEPOSIT_ORDER_AMOUNT_ERROR);
            }

            //商户是否开通了押金支付
            if(currentMchInfo.getDepositModeStatus() != MchConstant.PUB_YES){
                throw ServiceException.build(RetEnum.RET_MCH_DEPOSIT_STATUS_ERROR);
            }

            //产品是否被支持
            if(productId != PayConstant.PAY_PRODUCT_WX_BAR && productId != PayConstant.PAY_PRODUCT_ALIPAY_BAR){
                throw ServiceException.build(RetEnum.RET_MCH_DEPOSIT_PRODUCT_ERROR);
            }
        }

        //[3.] 创建交易订单
        String ipInfo = IPUtility.getClientIp(request);
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrderJWT(
                currentMchInfo, MchConstant.TRADE_TYPE_PAY, requiredAmount, realAmount, tradeProductType, productId, ipInfo,
                (mchTradeOrder) -> {
                    mchTradeOrder.setUserId(barCode); //用户ID
                    mchTradeOrder.setIsvDeviceNo(isvDeviceNo); //服务商设备编号
                    mchTradeOrder.setDepositMode(depositMode); //是否押金模式

                    //押金模式
                    if(isDepositMode){
                        mchTradeOrder.setOrderAmount(0L);
                        mchTradeOrder.setAmount(0L);
                        mchTradeOrder.setDiscountAmount(0L);
                        mchTradeOrder.setDepositAmount(realAmount);
                    }
        });

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

        //tradeOrderId
        String tradeOrderId = addMchTradeOrder.getTradeOrderId();

        //[4.] 调起 [支付网关] 接口
        JSONObject retMsg = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, currentMchInfo, mainConfig.getPayUrl(), mainConfig.getNotifyUrl());
        if(retMsg == null) { //支付网关返回结果验证失败
            rpc.rpcMchTradeOrderService.updateStatus4Fail(tradeOrderId);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL));
        }

        //无论接口返回确认成功状态，还是支付中，所有支付成功逻辑均在回调函数中实现。
        String channelOrderStatus = retMsg.getString("orderStatus"); //支付网关 返回的订单状态, 1-支付中 2-支付成功
        String channelPayOrderId = retMsg.getString("payOrderId"); //支付网关返回的支付订单号, 用于记录更新

        //[6.] 更新订单号
        MchTradeOrder updateRecord = new MchTradeOrder();

        //如果押金模式 && 押金未结算状态下
        if(isDepositMode && Byte.parseByte(channelOrderStatus) == PayConstant.PAY_STATUS_DEPOSIT_ING){
            updateRecord.setStatus(MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING);
        }

        updateRecord.setTradeOrderId(tradeOrderId);
        updateRecord.setPayOrderId(channelPayOrderId);
        rpc.rpcMchTradeOrderService.updateById(updateRecord);

        //[7.] 返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("tradeOrderId", tradeOrderId);
        result.put("orderStatus", channelOrderStatus);
        result.put("productType", tradeProductType);
        result.put("amount", addMchTradeOrder.getAmount());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /** 现金收款 **/
    @RequestMapping("/cash")
    public ResponseEntity<?> cash() {

        //[1.] 获取当前登录用户的基本信息
        Long mchId = getUser().getBelongInfoId(); //获取mchId
        MchInfo currentMchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 获取请求参数并验证参数是否合法
        Long requiredAmount = getRequiredAmountL("requiredAmount"); //应付金额
        Long realAmount = getRequiredAmountL("realAmount");  //实付金额

        //金额是否合法
        if(requiredAmount <= 0 || realAmount <= 0) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

        //实付金额 > 应付金额
        if(realAmount > requiredAmount) throw ServiceException.build(RetEnum.RET_MCH_REAL_AMOUNT_GT_REQUIRED);

        //[3.] 创建交易订单
        String ipInfo = IPUtility.getClientIp(request);
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrderJWT(
                currentMchInfo, MchConstant.TRADE_TYPE_PAY, requiredAmount, realAmount, MchConstant.MCH_TRADE_PRODUCT_TYPE_CASH, null, ipInfo,
                (mchTradeOrder) -> {
                    mchTradeOrder.setSubject("消费|现金支付"); //商品标题
                    mchTradeOrder.setBody("消费|现金支付"); //商品描述信息
                    mchTradeOrder.setStatus(MchConstant.TRADE_ORDER_STATUS_SUCCESS); //订单状态, 默认成功
                });

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

        //[4.] 返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId());
        result.put("orderStatus", addMchTradeOrder.getStatus());
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /** 会员卡消费 **/
    @RequestMapping("/balance")
    public ResponseEntity<?> balancePay() {

        //[1.] 获取当前登录用户的基本信息
        Long mchId = getUser().getBelongInfoId(); //获取mchId
        MchInfo currentMchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 获取请求参数并验证参数是否合法
        Long requiredAmount = getRequiredAmountL("requiredAmount"); //应付金额
        Long realAmount = getRequiredAmountL("realAmount");  //实付金额
        String memberNo = getValString("memberNo");   //memberNo 与 memberPayCode 二选一， 以memberNo为准  (支持手机号查询)
        String memberPayCode = getValString("memberPayCode");   //memberPayCode 与 memberNo 二选一， 以memberNo为准
        String couponNo = getValString("couponNo"); //会员优惠券核销码

        //金额是否合法
        if(requiredAmount <= 0 || realAmount <= 0) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

        //实付金额 > 应付金额
        if(realAmount > requiredAmount) throw ServiceException.build(RetEnum.RET_MCH_REAL_AMOUNT_GT_REQUIRED);

        if(StringUtils.isAllEmpty(memberNo, memberPayCode)){ //如果 参数不存在
            throw new ServiceException(RetEnum.RET_COMM_PARAM_ERROR);
        }

        Member member ;
        if(StringUtils.isNotEmpty(memberNo)){ //如果存在会员卡号
            member = rpc.rpcMemberService.getOne(
                    new QueryWrapper<Member>().lambda()
                            .eq(Member::getMchId, mchId)
                            .and(i -> i.eq(Member::getMemberNo, memberNo).or().eq(Member::getTel, memberNo))  //支持手机号查询
            );

        }else{
            String memberIdStr = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_TOKEN_PREFIX_MBR_CODE + memberPayCode);
            if(StringUtils.isEmpty(memberIdStr)){
                throw new ServiceException(RetEnum.RET_MCH_NOT_EXISTS_PAYCODE); //无法获取会员付款码
            }
            member = rpc.rpcMemberService.getById(Long.parseLong(memberIdStr));
        }

        if(member == null) throw new ServiceException(RetEnum.RET_MCH_NOT_EXISTS_PAYCODE); //无法获取会员付款码

        Long memberId = member.getMemberId();  //会员ID

        Long discountAmount = 0L;  //优惠金额
        Long mchCouponId = null;

        if(StringUtils.isNotEmpty(couponNo)){ //选择了优惠券

            MemberCoupon memberCoupon = rpc.rpcMemberCouponService.getOne(new QueryWrapper<MemberCoupon>()
                    .lambda().eq(MemberCoupon::getCouponNo, couponNo).eq(MemberCoupon::getMemberId, memberId));

            //判断优惠券是否可用
            if(memberCoupon == null || memberCoupon.getStatus() != MchConstant.PUB_NO){
                throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
            }

            MchCoupon mchCoupon = rpc.rpcMchCouponService.getById(memberCoupon.getCouponId());

            //检查优惠券是否 满足使用条件
            if(rpc.rpcMemberCouponService.useCouponPreCheck(mchCoupon, realAmount, getUser().getStoreId()) != null){
                throw ServiceException.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS);
            }
            mchCouponId = mchCoupon.getCouponId();
            discountAmount = mchCoupon.getCouponAmount();
        }

        if (!discountAmount.equals(requiredAmount - realAmount)){
            throw ServiceException.build(RetEnum.RET_MCH_STORE_SPEAKER_CODE_ERROR);
        }

        final Long finalMchCouponId = mchCouponId;
        //[3.] 创建交易订单
        String ipInfo = IPUtility.getClientIp(request);
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrderJWT(
                currentMchInfo, MchConstant.TRADE_TYPE_PAY, requiredAmount, realAmount, MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD, null, ipInfo,
                (mchTradeOrder) -> {
                    mchTradeOrder.setUserId(member.getMemberId() + ""); //会员ID
                    mchTradeOrder.setMemberId(member.getMemberId()); //会员ID
                    mchTradeOrder.setMemberTel(member.getTel()); //会员手机号
                    mchTradeOrder.setMchCouponId(finalMchCouponId); //优惠券ID
                    mchTradeOrder.setMemberCouponNo(couponNo); //会员优惠券核销码
                });

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

        //[4.] 更新会员账户信息数据 & 积分数据 & 优惠券
        int updatRow = rpc.rpcMchTradeOrderService.updateSuccess4MemberBalance(addMchTradeOrder.getTradeOrderId(), member.getMemberId(), couponNo);

        //[5.] 返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId());
        result.put("orderStatus", addMchTradeOrder.getStatus());

        if(updatRow > 0){
            result.put("orderStatus", MchConstant.TRADE_ORDER_STATUS_SUCCESS);
        }

        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /**  商户查单接口 **/
    @RequestMapping("/queryStatus")
    public ResponseEntity<?> queryStatus() {

        Long mchId = getUser().getBelongInfoId(); //获取mchId

        String tradeOrderId = getValStringRequired("tradeOrderId"); //商户交易单号
        MchTradeOrder mchTradeOrder = rpc.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(mchId, tradeOrderId);
        if(mchTradeOrder != null){
            JSONObject result = new JSONObject();
            result.put("tradeOrderId", tradeOrderId);
            result.put("orderStatus", mchTradeOrder.getStatus());
            result.put("productType", mchTradeOrder.getProductType());
            result.put("amount", mchTradeOrder.getAmount());
            return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
        }
        return ResponseEntity.ok(XxPayResponse.buildSuccess());
    }


    /** 订单同步接口 */
    @RequestMapping("/orderSync")
    public ResponseEntity<?> orderSync() {

        //[1.] 获取当前登录用户的基本信息
        Long mchId = getUser().getBelongInfoId(); //获取mchId
        MchInfo currentMchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 获取请求参数并验证参数是否合法
        String tradeOrderId = getValStringRequired("tradeOrderId"); //商户交易单号

        MchTradeOrder mchTradeOrder = rpc.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(mchId, tradeOrderId);
        if(mchTradeOrder == null) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_RECORD_NOT_EXIST));

        // 订单状态不属于[支付中]，无需同步； 仅支付中订单需要同步状态
        if(mchTradeOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_ING){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TRADE_NOT_NEED_SYNC));
        }

        //充值订单 无需同步
        if(mchTradeOrder.getTradeType() != MchConstant.TRADE_TYPE_PAY){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TRADE_NOT_NEED_SYNC));
        }

        //现金收款 || 会员卡支付订单， 无需同步
        if (mchTradeOrder.getProductType() == null){
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TRADE_TYPE_NOT_NEED_SYNC));
        }else if(mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_CASH ||
                mchTradeOrder.getProductType() == MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD )
        {
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_TRADE_TYPE_NOT_NEED_SYNC));
        }


        JSONObject sendReqParam = new JSONObject();  //发送参数
        sendReqParam.put("mchId", mchTradeOrder.getMchId());                    //商户ID
        sendReqParam.put("mchOrderNo", mchTradeOrder.getTradeOrderId());        // 商户交易单号
        sendReqParam.put("sign", PayDigestUtil.getSign(sendReqParam, currentMchInfo.getPrivateKey()));   // 签名
        String reqData = "params=" + sendReqParam.toJSONString();
        _log.info("xxpay_req:{}", reqData);
        String url = mainConfig.getPayUrl() + "/pay/query_order?";
        String retResult = XXPayUtil.call4Post(url + reqData);
        _log.info("xxpay_res:{}", retResult);

        JSONObject retResultJSON = JSON.parseObject(retResult);
        String iSign = PayDigestUtil.getSign(retResultJSON, currentMchInfo.getPrivateKey(), "sign");

        //[5.]  验签
        if(!iSign.equals(retResultJSON.getString("sign"))) { //验签失败
            _log.error("验签失败 iSign={}, sign={}", iSign, retResultJSON.getString("sign"));
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_OPERATION_FAIL));
        }

        Byte channelOrderStatus = retResultJSON.getByte("status"); //支付网关 返回的订单状态, 1-支付中 2-支付成功

        if(channelOrderStatus == PayConstant.PAY_STATUS_SUCCESS){ //上游订单支付状态为成功

            //TODO 需要与支付回调接口 处理业务逻辑相同！
            if(MchConstant.TRADE_TYPE_PAY == mchTradeOrder.getTradeType()
                    && "8020".equals(mchTradeOrder.getProductId())){ //交易类型为收款 ，且产品ID为8020表示微信被扫支付， 需要判断会员逻辑；

                JSONObject openIdJSON = (JSONObject) JSON.parse(retResultJSON.get("channelAttach").toString());

                rpc.rpcMchTradeOrderService.updateSuccess4Member(tradeOrderId, retResultJSON.getString("payOrderId"), mchTradeOrder.getMchIncome(),openIdJSON.getString("openId"));

            }else{ //保持原有逻辑不变
                rpc.rpcMchTradeOrderService.updateStatus4Success(tradeOrderId, retResultJSON.getString("payOrderId"), mchTradeOrder.getMchIncome(), 0L);
            }
        }else if(channelOrderStatus == PayConstant.PAY_STATUS_FAILED){  //上游返回支付订单状态为失败
            rpc.rpcMchTradeOrderService.updateStatus4Fail(tradeOrderId);
        }

        JSONObject result = new JSONObject();
        result.put("tradeOrderId", tradeOrderId);
        result.put("channelOrderStatus", channelOrderStatus);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /** 会员充值接口*/
    @RequestMapping("/recharge")
    public ResponseEntity<?> recharge() {

        //[1.] 获取当前登录用户的基本信息
        Long mchId = getUser().getBelongInfoId();
        MchInfo currentMchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

        //[2.] 获取请求参数并验证参数是否合法
        Long memberId = getValLongRequired( "memberId");  //会员ID
        Long ruleId = getValLong( "ruleId");        //充值规则
        String amountStr = getValStringRequired("amount");  //充值金额
        Long amount = Long.parseLong(AmountUtil.convertDollar2Cent(amountStr));
        String barCode = getValStringRequired( "barCode");   //条码

        if (ruleId != null) {
            MchMemberRechargeRule rule = rpc.rpcMchMemberRechargeRuleService.getById(ruleId);
            amount = rule.getRechargeAmount();
        }

        //金额有误
        if(amount <= 0) return ResponseEntity.ok(BizResponse.build(RetEnum.RET_COMM_PARAM_ERROR));

        //获取支付产品，并校验barCode是否可用
        int productId = XXPayUtil.getProductIdByBarCode(barCode);
        Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType

        Member member = rpc.rpcMemberService.getOne(new QueryWrapper<Member>().lambda().eq(Member::getMchId, mchId).eq(Member::getMemberId, memberId));
        if(member == null) throw new ServiceException(RetEnum.RET_MCH_NOT_EXISTS_PAYCODE); //没有该会员

        //TODO    如果充值赠优惠券，需验证优惠券是否可领取

        String ipInfo = IPUtility.getClientIp(request);
        MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrderJWT(
                currentMchInfo, MchConstant.TRADE_TYPE_RECHARGE, amount, amount, tradeProductType, productId, ipInfo, (mchTradeOrder -> {
                            mchTradeOrder.setMemberId(memberId);//会员ID
                            mchTradeOrder.setMemberTel(member.getTel());//会员手机号
                            mchTradeOrder.setRuleId(ruleId);//会员充值规则ID
                            mchTradeOrder.setUserId(barCode); //用户ID
                }));

        //入库失败
        if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

        //tradeOrderId
        String tradeOrderId = addMchTradeOrder.getTradeOrderId();

        //[4.] 调起 [支付网关] 接口
        JSONObject retMsg = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, currentMchInfo, mainConfig.getPayUrl(), mainConfig.getNotifyUrl());
        if(retMsg == null) { //支付网关返回结果验证失败
            rpc.rpcMchTradeOrderService.updateStatus4Fail(tradeOrderId);
            return ResponseEntity.ok(BizResponse.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL));
        }

        //无论接口返回确认成功状态，还是支付中，所有支付成功逻辑均在回调函数中实现。
        String channelOrderStatus = retMsg.getString("orderStatus"); //支付网关 返回的订单状态, 1-支付中 2-支付成功
        String channelPayOrderId = retMsg.getString("payOrderId"); //支付网关返回的支付订单号, 用于记录更新

        //[6.] 更新订单号
        MchTradeOrder updateRecord = new MchTradeOrder();
        updateRecord.setTradeOrderId(tradeOrderId);
        updateRecord.setPayOrderId(channelPayOrderId);
        rpc.rpcMchTradeOrderService.updateById(updateRecord);

        //[7.] 返回交易订单号 & 状态
        JSONObject result = new JSONObject();
        result.put("tradeOrderId", tradeOrderId);
        result.put("orderStatus", channelOrderStatus);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /** 订单退款接口 **/
    @RequestMapping("/refund")
    public ResponseEntity<?> refund() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");   //申请退款的 [商户交易单号]
        Long refundAmount = getRequiredAmountL( "refundAmount");  //退款金额
        String refundDesc = getValString( "refundDesc"); //退款原因

        //校验订单是否合法, 校验退款金额是否合法
        MchTradeOrder dbOrder = rpc.rpcMchTradeOrderService.findByTradeOrderId(tradeOrderId);
        //调用退款流程
        JSONObject result = refund(dbOrder, refundAmount, tradeOrderId, refundDesc);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }

    /** 订单退款验证退款密码接口 **/
    @RequestMapping("/refund2")
    public ResponseEntity<?> refund2() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");   //申请退款的 [商户交易单号]
        Long refundAmount = getRequiredAmountL( "refundAmount");  //退款金额
        String refundDesc = getValString( "refundDesc"); //退款原因
        String refundPassword = getValString( "refundPassword"); //退款密码

        //校验订单是否合法, 校验退款金额是否合法
        MchTradeOrder dbOrder = rpc.rpcMchTradeOrderService.findByTradeOrderId(tradeOrderId);
        if(dbOrder == null){
            throw ServiceException.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST); //没有该订单
        }

        //校验退款密码是否正确
        MchStore mchStore = rpc.rpcMchStoreService.getById(dbOrder.getStoreId()); //所属门店
        if (mchStore == null) throw ServiceException.build(RetEnum.RET_MCH_STORE_NOT_EXIST); //无门店

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(refundPassword, mchStore.getRefundPassword())) {
            throw ServiceException.build(RetEnum.RET_MCH_REFUNDS_PASSWORD_ERROR); //验证密码错误
        }
        //调用退款流程
        JSONObject result = refund(dbOrder, refundAmount, tradeOrderId, refundDesc);
        return ResponseEntity.ok(XxPayResponse.buildSuccess(result));
    }


    /** 押金订单， 押金消费 **/
    @RequestMapping("/depositConsume")
    public XxPayResponse depositConsume() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");   //[商户交易单号]
        Long consumeAmount = getRequiredAmountL( "consumeAmount");  //消费金额

        //校验订单是否合法, 校验退款金额是否合法
        MchTradeOrder dbOrder = rpc.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(getUser().getBelongInfoId(), tradeOrderId);
        if(dbOrder == null){
            throw ServiceException.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST); //没有该订单
        }

        //验证是否为 [押金模式] 订单
        if(dbOrder.getDepositMode() != MchConstant.PUB_YES){
            throw ServiceException.build(RetEnum.RET_MCH_ORDER_MODE_NOT_DEPOSIT_ERROR);
        }

        //不等于 [押金未结算] 状态
        if(dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING){
            throw ServiceException.build(RetEnum.RET_MCH_ORDER_STATUS_NOT_DEPOSIT_ING_ERROR);
        }

        //消费金额 不允许为0， 并且需要小于等于押金金额
        if(consumeAmount <= 0 || consumeAmount > dbOrder.getDepositAmount() ) {
            throw ServiceException.build(RetEnum.RET_MCH_ORDER_DEPOSIT_AMOUNT_ERROR);
        }

        if(dbOrder.getProductId() == PayConstant.PAY_PRODUCT_WX_BAR){
            //调起微信接口 & 处理订单状态
            rpcCommonService.rpcXxPayWxpayApiService.wxDepositConsume(tradeOrderId, consumeAmount);
        }else if(dbOrder.getProductId() == PayConstant.PAY_PRODUCT_ALIPAY_BAR){
            //调起支付宝接口 & 处理订单状态
            rpcCommonService.rpcXxPayAlipayApiService.depositConsume(tradeOrderId, consumeAmount);
        }

        return XxPayResponse.buildSuccess();
    }

    /** 押金订单撤销 **/
    @RequestMapping("/depositReverse")
    public XxPayResponse depositReverse() {

        String tradeOrderId = getValStringRequired( "tradeOrderId");   //[商户交易单号]

        //校验订单是否合法, 校验退款金额是否合法
        MchTradeOrder dbOrder = rpc.rpcMchTradeOrderService.findByMchIdAndTradeOrderId(getUser().getBelongInfoId(), tradeOrderId);
        if(dbOrder == null){
            throw ServiceException.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST); //没有该订单
        }

        //验证是否为 [押金模式] 订单
        if(dbOrder.getDepositMode() != MchConstant.PUB_YES){
            throw ServiceException.build(RetEnum.RET_MCH_ORDER_MODE_NOT_DEPOSIT_ERROR);
        }

        //不等于 [押金未结算] 状态
        if(dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_DEPOSIT_ING){
            throw ServiceException.build(RetEnum.RET_MCH_ORDER_STATUS_NOT_DEPOSIT_ING_ERROR);
        }

        if(dbOrder.getProductId() == PayConstant.PAY_PRODUCT_WX_BAR){
            //调起微信接口 & 处理订单状态
            rpcCommonService.rpcXxPayWxpayApiService.wxDepositReverse(tradeOrderId);
        }else if(dbOrder.getProductId() == PayConstant.PAY_PRODUCT_ALIPAY_BAR){
            //调起微信接口 & 处理订单状态
            rpcCommonService.rpcXxPayAlipayApiService.depositReverse(tradeOrderId);
        }

        return XxPayResponse.buildSuccess();
    }


    /** 创建退款订单 **/
    private JSONObject createRefundOrder(MchTradeOrder mchTradeOrder, MchRefundOrder mchRefundOrder) {
        JSONObject paramMap = new JSONObject();
        paramMap.put("mchId", mchTradeOrder.getMchId());                    //商户ID
        paramMap.put("payOrderId", mchTradeOrder.getPayOrderId());                    // 支付订单号
        paramMap.put("mchOrderNo", mchTradeOrder.getTradeOrderId());        // 商户交易单号
        paramMap.put("mchRefundNo", mchRefundOrder.getMchRefundOrderId());            // 商户退款单号
        paramMap.put("amount", mchRefundOrder.getRefundAmount());                  // 退款金额,单位分
        paramMap.put("currency", "cny");                                    // 币种, cny-人民币
        paramMap.put("clientIp", mchTradeOrder.getClientIp());              // 用户地址,IP或手机号
        paramMap.put("device", mchTradeOrder.getDevice());                  // 设备
        paramMap.put("notifyUrl","");               // 回调URL
        paramMap.put("remarkInfo", mchRefundOrder.getRefundDesc());                                         // 退款原因
        paramMap.put("channelUser", mchTradeOrder.getUserId());                   // openId

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(getUser().getBelongInfoId()); //获取商户信息
        String reqSign = PayDigestUtil.getSign(paramMap, mchInfo.getPrivateKey());
        paramMap.put("sign", reqSign);   // 签名
        String reqData = "params=" + paramMap.toJSONString();
        _log.info("xxpay_req:{}", reqData);

        String url = mainConfig.getPayUrl() + "/refund/create_order?";
        String result = XXPayUtil.call4Post(url + reqData);
        _log.info("xxpay_res:{}", result);
        JSONObject retMsg = JSON.parseObject(result);
        return retMsg;
    }

    /** 退款验证执行流程 **/
    private JSONObject refund(MchTradeOrder dbOrder, Long refundAmount, String tradeOrderId, String refundDesc) {

        if(dbOrder == null){
            throw ServiceException.build(RetEnum.RET_MCH_TRADE_ORDER_NOT_EXIST); //没有该订单
        }

        //订单状态 不是支付成功， 也不是部分退款状态时，不允许发起退款
        if(dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_SUCCESS && dbOrder.getStatus() != MchConstant.TRADE_ORDER_STATUS_REFUND_PART){ //支付成功状态
            throw ServiceException.build(RetEnum.RET_MCH_REFUND_STATUS_NOT_SUPPORT);
        }

        if(dbOrder.getAmount() <= dbOrder.getRefundTotalAmount()  ) { //订单支付金额 <= 订单总退金额
            throw ServiceException.build(RetEnum.RET_MCH_ALREADY_REFUNDS);
        }

        if(refundAmount >  ( dbOrder.getAmount() - dbOrder.getRefundTotalAmount() ) ) { //退款金额 > （订单金额 - 总退款金额）
            throw ServiceException.build(RetEnum.RET_MCH_REFUNDAMOUNT_GT_PAYAMOUNT);
        }

        //判断当前订单 是否存在[退款中] 订单
        int ingRefundOrder = rpc.rpcMchRefundOrderService.count(
                new QueryWrapper<MchRefundOrder>().lambda().eq(MchRefundOrder::getTradeOrderId, tradeOrderId)
                        .eq(MchRefundOrder::getStatus, MchConstant.MCH_REFUND_STATUS_ING));
        if(ingRefundOrder > 0){
            throw ServiceException.build(RetEnum.RET_MCH_TRADE_HAS_REFUNDING_ORDER);
        }

        //插入商户退款表
        MchRefundOrder mchRefundOrder = new MchRefundOrder();
        mchRefundOrder.setMchRefundOrderId(MySeq.getMchRefund()); //商户退款订单号
        mchRefundOrder.setTradeOrderId(dbOrder.getTradeOrderId()); //支付订单号
        mchRefundOrder.setMchId(dbOrder.getMchId());  //商户ID
        mchRefundOrder.setPayAmount(dbOrder.getAmount());  //支付订单号
        mchRefundOrder.setRefundAmount(refundAmount); //退款金额
        mchRefundOrder.setCurrency("CNY");  //币种
        mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_ING);  //默认退款单状态： 退款中
        mchRefundOrder.setRefundDesc(refundDesc); //退款原因
        mchRefundOrder.setCreateTime(new Date());

        rpc.rpcMchRefundOrderService.save(mchRefundOrder);   //插入商户退款表记录

        if(dbOrder.getProductId() == null){ //无需调用上游接口， 直接为退款成功
            mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_SUCCESS);

        } else{ //调起上游退款接口

            JSONObject payResData = createRefundOrder(dbOrder, mchRefundOrder); //向上游发起退款申请

            //返回code不等于0 ， 退款异常
            if(PayEnum.OK.getCode().equals(payResData.getString("retCode"))) {

                if(PayConstant.REFUND_STATUS_FAIL == payResData.getByte("status")){  //退款失败
                    mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_FAIL);
                }else if(PayConstant.REFUND_STATUS_SUCCESS == payResData.getByte("status")){  //退款成功
                    mchRefundOrder.setStatus(MchConstant.MCH_REFUND_STATUS_SUCCESS);
                } //其他情况，认为退款中
            }
        }

        //退款成功
        if(mchRefundOrder.getStatus() == MchConstant.MCH_REFUND_STATUS_SUCCESS){

            //更新订单状态
            rpc.rpcMchRefundOrderService.refundSuccess(mchRefundOrder, dbOrder);

        }else if(mchRefundOrder.getStatus() == MchConstant.MCH_REFUND_STATUS_FAIL){ //退款失败
            rpc.rpcMchRefundOrderService.refundFail(mchRefundOrder.getMchRefundOrderId());
        }

        JSONObject result = new JSONObject();
        result.put("mchRefundOrderId", mchRefundOrder.getMchRefundOrderId());  //退款申请成功， 返回退款订单号
        result.put("status", mchRefundOrder.getStatus());  //状态
        return result;
    }
}
