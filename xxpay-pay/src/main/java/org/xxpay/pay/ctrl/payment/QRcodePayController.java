package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.Exception.ServiceException;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.domain.XxPayResponse;
import org.xxpay.core.common.util.*;
import org.xxpay.core.entity.*;
import org.xxpay.pay.channel.PayConfig;
import org.xxpay.pay.channel.alipay.AlipayApiService;
import org.xxpay.pay.channel.alipay.AlipayConfig;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.RpcCommonService;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/** 聚合码支付 ctrl */
@RestController
@RequestMapping("/api/qrcode")
public class QRcodePayController extends BaseController {

    private final MyLog _log = MyLog.getLog(QRcodePayController.class);

    @Autowired
    private RpcCommonService rpc;

    @Value("${config.wxOauth2CodeUrl}")
    private String wxOauth2CodeUrl;

    @Value("${config.qrcodeUrl}")
    private String qrcodeUrl;

    @Value("${config.mchNotifyUrl}")
    private String mchNotifyUrl;

    @Value("${config.toNewMemberUrl}")
    private String toNewMemberUrl;

    @Autowired
    private PayConfig payConfig;

    @Autowired
    private AlipayApiService alipayApiService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * ctrlA
     * 用于：商户二维码真实跳转地址
     * 场景：会员扫描商户的二维码跳转到该Ctrl
     * 参数：商户ID, 门店ID, 操作员ID, 金额
     * 逻辑：
     *     判断支付类型：
     *          微信 - 跳转到静默授权地址，用于获取用户openId；
     *          支付宝 - 跳转到静默授权地址，用于获取用户userId；
     *          其他 - 暂时无法识别的客户端；
     **/
    @RequestMapping("/toQRCodePage")
    public String toQRCodePage(){

        try {
            // 根据浏览器User-Agent 获取对应产品信息
            Integer productId = this.getProductIdByUA();

            // 无法识别扫码客户端
            if (productId == null) throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_SUPPORT);

            JSONObject param = getJsonParam(); //请求参数
            Long mchId = getLongRequired(param, "mchId"); //商户ID
            String tradeOrderId = param.getString("tradeOrderId"); //交易订单号ID

            if(StringUtils.isNotEmpty(tradeOrderId)){  //如果订单号存在

                int rowCount = rpc.rpcMchTradeOrderService.count(new QueryWrapper<MchTradeOrder>().lambda().eq(MchTradeOrder::getTradeOrderId, tradeOrderId));
                if(rowCount > 0 ){  //该二维码仅可扫码一次
                    throw ServiceException.build(RetEnum.RET_SERVICE_TRADE_ID_EXISTS);
                }
            }

            JSONObject mainParam = getMainParam(mchId, productId);  //主发起方的请求参数
            String ifTypeCode = getMainIfTypeCode(mchId, productId);

            String returnParamStr = MyBase64.encode(param.toJSONString().getBytes()); //base64
            String redirectUrl = null;  //重定向地址

            if(productId == PayConstant.PAY_PRODUCT_WX_JSAPI){ //微信, 先获取openId

                //微信返回地址
                String wxReturnUrl = String.format("%s/qrcode/wxRedirectUrl/%s", payConfig.getPayUrl(), returnParamStr);
                String wxAppId = null;
                if(PayConstant.CHANNEL_NAME_WXPAY.equals(ifTypeCode)){ //微信官方
                    wxAppId = mainParam.getString("appId");
                } else if(PayConstant.CHANNEL_NAME_SUIXINGPAY.equals(ifTypeCode) || PayConstant.CHANNEL_NAME_DLBPAY.equals(ifTypeCode)){ //随行付 or 哆啦宝
                    wxAppId = mainParam.getString("wxAuthAppId");
                }

                redirectUrl = String.format(wxOauth2CodeUrl + "?appid=%s&scope=snsapi_base&state=&redirect_uri=%s",
                                            wxAppId, StrUtil.urlEnodeUTF8(wxReturnUrl));

            }else if(productId == PayConstant.PAY_PRODUCT_ALIPAY_JSAPI){ //支付宝， 获取用户的userId

                //支付宝返回地址
                String alipayReturnUrl = String.format("%s/qrcode/alipayRedirectUrl/%s", payConfig.getPayUrl(), returnParamStr);

                String alipayAppId = null;
                if(PayConstant.CHANNEL_NAME_ALIPAY.equals(ifTypeCode)){ //支付宝官方
                    alipayAppId = mainParam.getString("appId");
                } else if(PayConstant.CHANNEL_NAME_SUIXINGPAY.equals(ifTypeCode)  || PayConstant.CHANNEL_NAME_DLBPAY.equals(ifTypeCode) ){ //随行付 or 哆啦宝
                    alipayAppId = mainParam.getString("alipayAuthAppId");
                }
                redirectUrl = String.format(XXPayUtil.getAlipayUrl4env(2, mainParam)+"?app_id=%s&scope=auth_base&state=&redirect_uri=%s",
                        alipayAppId, StrUtil.urlEnodeUTF8(alipayReturnUrl));

            }else if(productId == PayConstant.PAY_PRODUCT_JD_H5){//京东H5支付

                //封装跳转到h5参数
                Long h5MchId = mchId; //mchId (必填)
                String h5PayAmount = StringUtils.defaultString(param.getString("payAmount"), ""); //支付金额（非必填） 单位： 分
                String h5StoreId = StringUtils.defaultString(param.getString("storeId"), ""); //门店ID（非必填）
                String h5OperatorId = StringUtils.defaultString(param.getString("operatorId"), ""); //操作员ID（非必填）
                String h5TradeOrderId = StringUtils.defaultString(param.getString("tradeOrderId"), ""); //支付订单号（非必填）
                String payToken = this.genPayToken(); //生成token(必填)

                String h5Url = String.format(qrcodeUrl + "/index.html#/jd/paySelect?payToken=%s&mchId=%s&payAmount=%s&storeId=%s&operatorId=%s&tradeOrderId=%s"
                        ,payToken, h5MchId, h5PayAmount, h5StoreId, h5OperatorId, h5TradeOrderId);

                response.sendRedirect(h5Url); //跳转到页面， 带入相关参数

                return null;

            }
            _log.info("----跳转地址----{}", redirectUrl);
            response.sendRedirect(redirectUrl); //请求微信/支付宝的oauth接口
            return null;
        } catch (ServiceException e) {
            return toErrorPage(e.getRetEnum());
        }catch (Exception e) {
            _log.error("系统异常：", e);
            return toErrorPage(RetEnum.RET_COMM_PARAM_ERROR);
        }
    }


    /**
     * ctrlB
     * 请求微信oauth接口获取用户信息的回调地址（包含code参数）
     * @param returnParamStr 上一步封装的原样参数（base64格式）
     * @return
     * @throws Exception
     */
    @RequestMapping("/wxRedirectUrl/{returnParamStr}")
    public void wxRedirectUrl(@PathVariable("returnParamStr") String returnParamStr) {

        try {
            String wxReturnCode = request.getParameter("code"); //微信服务器返回的code, 可以通过code换取accessToken
            JSONObject returnParamObj = JSONObject.parseObject(new String(MyBase64.decode(returnParamStr)));

            Long mchId = returnParamObj.getLong("mchId");

            //通过code 换取openId
            WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
            JSONObject configJSON = getMainParam(mchId, PayConstant.PAY_PRODUCT_WX_JSAPI);
            String ifTypeCode = getMainIfTypeCode(mchId, PayConstant.PAY_PRODUCT_WX_JSAPI);

            if(PayConstant.CHANNEL_NAME_WXPAY.equals(ifTypeCode)){ //微信官方
                wxMpConfigStorage.setAppId(configJSON.getString("appId"));
                wxMpConfigStorage.setSecret(configJSON.getString("appSecret"));

            } else if(PayConstant.CHANNEL_NAME_SUIXINGPAY.equals(ifTypeCode) || PayConstant.CHANNEL_NAME_DLBPAY.equals(ifTypeCode) ) { //随行付 or 哆啦宝

                wxMpConfigStorage.setAppId(configJSON.getString("wxAuthAppId"));
                wxMpConfigStorage.setSecret(configJSON.getString("wxAuthAppSecret"));
            }

            WxMpService wxMpService = new WxMpServiceImpl();
            wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
            WxMpOAuth2AccessToken accessToken = wxMpService.oauth2getAccessToken(wxReturnCode);
            String wxOpenId = accessToken.getOpenId();

            //封装跳转到h5参数
            Long h5MchId = mchId; //mchId (必填)
            String h5WxOpenId = wxOpenId; //wxOpenId (必填)
            String h5PayAmount = StringUtils.defaultString(returnParamObj.getString("payAmount"), ""); //支付金额（非必填） 单位： 分
            String h5StoreId = StringUtils.defaultString(returnParamObj.getString("storeId"), "");//门店ID（非必填）
            String h5OperatorId = StringUtils.defaultString(returnParamObj.getString("operatorId"), ""); //操作员ID（非必填）
            String h5TradeOrderId = StringUtils.defaultString(returnParamObj.getString("tradeOrderId"), ""); //支付订单号（非必填）
            String h5ToNewMemberUrl = URLEncoder.encode(toNewMemberUrl);
            //会员开卡地址

            String payToken = this.genPayToken(); //生成token(必填)
            String h5Url = String.format(qrcodeUrl + "/index.html#/wx/paySelect?mchId=%s&wxOpenId=%s&payAmount=%s&storeId=%s&operatorId=%s&tradeOrderId=%s&payToken=%s&toNewMemberUrl=%s"
                    ,h5MchId, wxOpenId, h5PayAmount, h5StoreId, h5OperatorId, h5TradeOrderId, payToken, h5ToNewMemberUrl);
            response.sendRedirect(h5Url); //跳转到页面， 带入相关参数

        }catch (ServiceException e){
            toErrorPage(e.getRetEnum());
        }
        catch (WxErrorException e) {
            _log.error("微信请求异常：", e);
            toErrorPage(RetEnum.RET_COMM_OPERATION_FAIL);
        } catch (IOException e) {
            _log.error("系统异常：", e);
            toErrorPage(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /**
     * ctrlB
     * 请求支付宝oauth接口获取用户信息的回调地址（包含code参数）
     * @param returnParamStr 上一步封装的原样参数（base64格式）
     * @return
     * @throws Exception
     */
    @RequestMapping("/alipayRedirectUrl/{returnParamStr}")
    public void alipayRedirectUrl(@PathVariable("returnParamStr") String returnParamStr){

        try {
            String alipayReturnCode = request.getParameter("auth_code"); //支付宝服务器返回的auth_code, 可以通过code换取accessToken
            JSONObject returnParamObj = JSONObject.parseObject(new String(MyBase64.decode(returnParamStr)));

            Long mchId = returnParamObj.getLong("mchId");

            JSONObject mainParam = getMainParam(mchId, PayConstant.PAY_PRODUCT_ALIPAY_JSAPI);

            String ifTypeCode = getMainIfTypeCode(mchId, PayConstant.PAY_PRODUCT_ALIPAY_JSAPI);

            AlipayConfig alipayConfig = null;
            if(PayConstant.CHANNEL_NAME_ALIPAY.equals(ifTypeCode)){ //支付宝官方
                alipayConfig = new AlipayConfig(mainParam);
            } else if(PayConstant.CHANNEL_NAME_SUIXINGPAY.equals(ifTypeCode) || PayConstant.CHANNEL_NAME_DLBPAY.equals(ifTypeCode) ){ //随行付 or 哆啦宝
                alipayConfig = new AlipayConfig();
                alipayConfig.setAppId(mainParam.getString("alipayAuthAppId"));
                alipayConfig.setPrivateKey(mainParam.getString("alipayAuthPrivateKey"));
                alipayConfig.setAlipayPublicKey(mainParam.getString("alipayAuthPublicKey"));
            }

            String alipayUserId = alipayApiService.getUserId(alipayConfig, alipayReturnCode);

            //封装跳转到h5参数
            Long h5MchId = mchId; //mchId (必填)
            String h5AlipayUserId = alipayUserId; //alipayUserId (必填)
            String h5PayAmount = StringUtils.defaultString(returnParamObj.getString("payAmount"), ""); //支付金额（非必填） 单位： 分
            String h5StoreId = StringUtils.defaultString(returnParamObj.getString("storeId"), ""); //门店ID（非必填）
            String h5OperatorId = StringUtils.defaultString(returnParamObj.getString("operatorId"), ""); //操作员ID（非必填）
            String h5TradeOrderId = StringUtils.defaultString(returnParamObj.getString("tradeOrderId"), ""); //支付订单号（非必填）
            String payToken = this.genPayToken(); //生成token(必填)

            String h5Url = String.format(qrcodeUrl + "/index.html#/zfb/paySelect?payToken=%s&mchId=%s&alipayUserId=%s&payAmount=%s&storeId=%s&operatorId=%s&tradeOrderId=%s"
                    ,payToken, h5MchId, h5AlipayUserId, h5PayAmount, h5StoreId, h5OperatorId, h5TradeOrderId);
            response.sendRedirect(h5Url); //跳转到页面， 带入相关参数
        } catch (ServiceException e) {
            toErrorPage(e.getRetEnum());
        }catch (Exception e) {
            _log.error("系统异常", e);
            toErrorPage(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 根据openId  查询会员信息， 包括，会员卡余额， 会员优惠券列表，  **/
    @RequestMapping("/memberInfoAndCoupons")
    public XxPayResponse memberInfoAndCoupons() {

        JSONObject param = getJsonParam();
        Long currentMchId = getLongRequired(param, "mchId");  //当前商户ID
        Long currentStoreId = getLongRequired(param, "storeId"); //当前用户的门店ID
        String wxOpenId = getStringRequired(param, "wxOpenId");  //wxOpenId
        handleParamAmount(param, "payAmount");
        Long payAmount = getLong(param, "payAmount");  //支付金额, 单位元， 自动转换为分

        try {

            checkPayToken(param); //验证token

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
            _log.error("error: wxOpenId={}, payAmount={} ", wxOpenId, payAmount, e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }

    /** 查询商户信息  **/
    @RequestMapping("/mchInfo")
    public XxPayResponse mchInfo() {

        JSONObject param = getJsonParam();
        Long currentMchId = getLongRequired(param, "mchId");  //当前商户ID
        Long currentStoreId = getLong(param, "storeId"); //当前用户的门店ID
        Long currentOperatorId = getLong(param, "operatorId"); //当前用户的操作员ID

        try {

            checkPayToken(param); //验证token

            MchInfo mchInfo = null;
            MchStore mchStore = null;
            SysUser sysUser = null;

            if(currentMchId != null){
                mchInfo = rpc.rpcMchInfoService.findByMchId(currentMchId);
            }
            if(currentStoreId != null){
                mchStore = rpc.rpcMchStoreService.getById(currentStoreId);
            }
            if(currentOperatorId != null){
                sysUser = rpc.rpcSysService.findByUserId(currentOperatorId);
            }

            JSONObject result = new JSONObject();
            result.put("mchName",  mchInfo != null ? mchInfo.getMchName() : "");
            result.put("storeName",  mchStore != null ? mchStore.getStoreName() : "");
            result.put("operatorName",  sysUser != null ? sysUser.getNickName() : "");

            return XxPayResponse.buildSuccess(result);
        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            _log.error("error:", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 会员卡支付 */
    @RequestMapping("/memberPay")
    public XxPayResponse memberPay(){

        Integer productId = this.getProductIdByUA();
        if (productId == null || productId != PayConstant.PAY_PRODUCT_WX_JSAPI) { //仅支持微信客户端微信支付方式
            return XxPayResponse.build(RetEnum.RET_MCH_UA_NOT_SUPPORT);
        }

        //2. 判断必传参数
        JSONObject requestParam = getJsonParam(); //通过request获取参数，并转换为json格式

        String wxOpenId = getStringRequired(requestParam, "wxOpenId"); //微信OpenId
        Long payAmount = getLongRequired(requestParam, "payAmount");  //支付金额
        String couponNo = getString(requestParam, "couponNo"); //会员优惠券ID
        Long storeId = getLong(requestParam, "storeId"); //门店ID
        Long operatorId = getLong(requestParam, "operatorId"); //操作员ID
        Long mchId = getLongRequired(requestParam, "mchId"); //商户ID
        String tradeOrderId = getString(requestParam, "tradeOrderId"); //商户交易订单号

        //3. 验证数据合法性
        if( payAmount <= 0 ) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

        Long discountAmount = 0L;  //优惠金额
        Long mchCouponId = null;

        Member member = rpc.rpcMemberService.getOne(new QueryWrapper<Member>().lambda()
                .eq(Member::getMchId, mchId).eq(Member::getWxOpenId, wxOpenId));

        try {

            checkPayToken(requestParam); //验证token

            if(member == null) throw ServiceException.build(RetEnum.RET_MCH_NOT_EXISTS_PAYCODE);
            Long memberId = member.getMemberId();  //会员ID

            if(StringUtils.isNotEmpty(couponNo)){ //选择了优惠券

                MemberCoupon memberCoupon = rpc.rpcMemberCouponService.getOne(new QueryWrapper<MemberCoupon>()
                        .lambda().eq(MemberCoupon::getCouponNo, couponNo).eq(MemberCoupon::getMemberId, memberId));

                //判断优惠券是否可用
                if(memberCoupon == null || memberCoupon.getStatus() != MchConstant.PUB_NO){
                    throw ServiceException.build(RetEnum.RET_MCH_COUPON_STATUS_ERROR);
                }

                MchCoupon mchCoupon = rpc.rpcMchCouponService.getById(memberCoupon.getCouponId());
                if(payAmount >= mchCoupon.getPayAmountLimit()){  //如果金额满足
                    mchCouponId = mchCoupon.getCouponId();
                    discountAmount = mchCoupon.getCouponAmount();
                }
            }

            MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

            final Long finalMchCouponId = mchCouponId;

            //[3.] 创建交易订单
            String ipInfo = IPUtility.getClientIp(request);
            MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrder(
                    mchInfo, MchConstant.TRADE_TYPE_PAY, payAmount, (payAmount - discountAmount), MchConstant.MCH_TRADE_PRODUCT_TYPE_MEMBER_CARD, null, ipInfo, operatorId, storeId,
                    (mchTradeOrder) -> {

                        mchTradeOrder.setTradeOrderId(tradeOrderId); //tradeOrderId
                        mchTradeOrder.setUserId(memberId + ""); //用户ID
                        mchTradeOrder.setChannelUserId(wxOpenId); //支付渠道用户ID
                        mchTradeOrder.setMchCouponId(finalMchCouponId); //优惠券ID
                        mchTradeOrder.setMemberCouponNo(couponNo); //会员优惠券核销码
                        mchTradeOrder.setMemberId(memberId); //记录memberId
                    });

            //入库失败
            if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

            //更新会员账户信息数据 & 积分数据 & 优惠券
            rpc.rpcMchTradeOrderService.updateSuccess4MemberBalance(addMchTradeOrder.getTradeOrderId(), member.getMemberId(), couponNo);

            JSONObject result = new JSONObject();
            result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", addMchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", addMchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", addMchTradeOrder.getAmount()); //实际支付金额
            this.clearPayToken(requestParam); //调起支付后清空token
            return XxPayResponse.buildSuccess(result);
        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }
        catch (Exception e) {
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }

    }


    /** 微信支付 */
    @RequestMapping("/wxPay")
    public XxPayResponse wxPay(){

        try {
            //1. 判断是否来自微信/支付宝的内嵌浏览器
            String ua = request.getHeader("User-Agent");
            if (StringUtils.isBlank(ua)) {  // 无法识别扫码客户端
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_EXIST);
            }

            Integer productId = this.getProductIdByUA();
            if (productId == null || productId != PayConstant.PAY_PRODUCT_WX_JSAPI) { //仅支持微信客户端微信支付方式
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_SUPPORT);
            }

            //2. 判断必传参数
            JSONObject requestParam = getJsonParam(); //通过request获取参数，并转换为json格式

            checkPayToken(requestParam); //验证token

            String wxOpenId = getStringRequired(requestParam, "wxOpenId"); //微信OpenId
            Long payAmount = getLongRequired(requestParam, "payAmount");  //支付金额, 单位：分
            String couponNo = getString(requestParam, "couponNo"); //会员优惠券ID
            Long storeId = getLong(requestParam, "storeId"); //门店ID
            Long operatorId = getLong(requestParam, "operatorId"); //操作员ID
            Long mchId = getLongRequired(requestParam, "mchId"); //商户ID
            String tradeOrderId = getString(requestParam, "tradeOrderId"); //商户交易订单号

            //3. 验证数据合法性
            if( payAmount <= 0 ) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

            //优惠金额 默认0
            Long discountAmount = 0L;
            Long mchCouponId = null;


            //仅微信用户需要判断 优惠券是否合法，并是否使用优惠券
            Member member = rpc.rpcMemberService.getOne(new QueryWrapper<Member>().lambda()
                    .eq(Member::getMchId, mchId).eq(Member::getWxOpenId, wxOpenId));

            Long memberId = member != null ? member.getMemberId() : null;
            if(StringUtils.isNotEmpty(couponNo) && memberId != null){ //查询优惠券是否可用

                MemberCoupon memberCoupon = rpc.rpcMemberCouponService.getOne(new QueryWrapper<MemberCoupon>()
                        .lambda().eq(MemberCoupon::getCouponNo, couponNo).eq(MemberCoupon::getMemberId, memberId));

                //判断优惠券是否可用
                if(memberCoupon == null || memberCoupon.getStatus() != MchConstant.PUB_NO){
                    throw ServiceException.build(RetEnum.RET_MBR_COUPON_NOT_EXISTS);
                }

                MchCoupon mchCoupon = rpc.rpcMchCouponService.getById(memberCoupon.getCouponId());
                if(payAmount >= mchCoupon.getPayAmountLimit()){  //如果金额满足
                    mchCouponId = mchCoupon.getCouponId();
                    discountAmount = mchCoupon.getCouponAmount();
                }
            }

            MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);

            //[3.] 创建交易订单
            final Long finalMchCouponId = mchCouponId;
            String ipInfo = IPUtility.getClientIp(request);
            Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType
            MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrder(
                    mchInfo, MchConstant.TRADE_TYPE_PAY, payAmount, (payAmount - discountAmount), tradeProductType, productId, ipInfo, operatorId, storeId,
                    (mchTradeOrder) -> {
                        mchTradeOrder.setTradeOrderId(tradeOrderId);  //tradeOrderId
                        mchTradeOrder.setUserId(memberId + ""); //用户ID
                        mchTradeOrder.setChannelUserId(wxOpenId); //支付渠道用户ID

                        mchTradeOrder.setMchCouponId(finalMchCouponId); //优惠券ID
                        mchTradeOrder.setMemberCouponNo(couponNo); //会员优惠券核销码
                        mchTradeOrder.setMemberId(memberId); //记录memberId
                    });

            //入库失败
            if (addMchTradeOrder == null) throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);

            JSONObject retJSON = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, mchInfo, payUrl, mchNotifyUrl);

            JSONObject result = new JSONObject();
            result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", addMchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", addMchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", addMchTradeOrder.getAmount()); //实际支付金额
            result.put("payParam", retJSON.getString("payParams")); //支付参数

            this.clearPayToken(requestParam); //调起支付后清空token
            return XxPayResponse.buildSuccess(result);

        } catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 支付宝支付 **/
    @RequestMapping("/alipayPay")
    public XxPayResponse alipayPay(){

        try {
            //1. 判断是否来自微信/支付宝的内嵌浏览器
            String ua = request.getHeader("User-Agent");
            if (StringUtils.isBlank(ua)) {  // 无法识别扫码客户端
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_EXIST);
            }

            Integer productId = this.getProductIdByUA();
            if (productId == null ||productId != PayConstant.PAY_PRODUCT_ALIPAY_JSAPI) { // 无法识别扫码客户端
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_SUPPORT);
            }

            //2. 判断必传参数
            JSONObject requestParam = getJsonParam(); //通过request获取参数，并转换为json格式

            checkPayToken(requestParam); //验证token

            String alipayUserId = getStringRequired(requestParam, "alipayUserId"); //支付宝userId
            Long payAmount = getLongRequired(requestParam, "payAmount");  //支付金额
            Long storeId = getLong(requestParam, "storeId"); //门店ID
            Long operatorId = getLong(requestParam, "operatorId"); //操作员ID
            Long mchId = getLongRequired(requestParam, "mchId"); //商户ID
            String tradeOrderId = getString(requestParam, "tradeOrderId"); //商户交易订单号

            //3. 验证数据合法性
            if( payAmount <= 0 ) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

            MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
            Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType
            //[3.] 创建交易订单
            String ipInfo = IPUtility.getClientIp(request);
            MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrder(
                    mchInfo, MchConstant.TRADE_TYPE_PAY, payAmount, payAmount, tradeProductType, productId, ipInfo, operatorId, storeId,
                    (mchTradeOrder) -> {

                        mchTradeOrder.setTradeOrderId(tradeOrderId);  //tradeOrderId
                        mchTradeOrder.setChannelUserId(alipayUserId); //支付渠道用户ID
                    });

            //调起上游支付接口
            JSONObject retJSON = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, mchInfo, payUrl, mchNotifyUrl);
            if(retJSON == null) { //支付网关返回结果验证失败
                throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);
            }

            JSONObject result = new JSONObject();
            result.put("status", "ok");
            result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", addMchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", addMchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", addMchTradeOrder.getAmount()); //实际支付金额
            result.put("payParam", retJSON.getString("payParams")); //支付参数
            this.clearPayToken(requestParam); //调起支付后清空token
            return XxPayResponse.buildSuccess(result);
        }catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 京东支付 **/
    @RequestMapping("/jdPay")
    public XxPayResponse jdPay(){

        try {
            //1. 判断是否来自微信/支付宝的内嵌浏览器
            String ua = request.getHeader("User-Agent");
            if (StringUtils.isBlank(ua)) {  // 无法识别扫码客户端
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_EXIST);
            }

            Integer productId = this.getProductIdByUA();
            if (productId == null ||productId != PayConstant.PAY_PRODUCT_JD_H5) { // 无法识别扫码客户端
                throw ServiceException.build(RetEnum.RET_MCH_UA_NOT_SUPPORT);
            }

            //2. 判断必传参数
            JSONObject requestParam = getJsonParam(); //通过request获取参数，并转换为json格式

            checkPayToken(requestParam); //验证token

            Long payAmount = getLongRequired(requestParam, "payAmount");  //支付金额
            Long storeId = getLong(requestParam, "storeId"); //门店ID
            Long operatorId = getLong(requestParam, "operatorId"); //操作员ID
            Long mchId = getLongRequired(requestParam, "mchId"); //商户ID
            String tradeOrderId = getString(requestParam, "tradeOrderId"); //商户交易订单号

            //3. 验证数据合法性
            if( payAmount <= 0 ) throw ServiceException.build(RetEnum.RET_COMM_PARAM_ERROR);

            MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
            Byte tradeProductType = XXPayUtil.getTradeProductTypeByProductId(productId);  //mchTradeOrder中的 productType
            //[3.] 创建交易订单
            String ipInfo = IPUtility.getClientIp(request);
            MchTradeOrder addMchTradeOrder = rpc.rpcMchTradeOrderService.insertTradeOrder(
                    mchInfo, MchConstant.TRADE_TYPE_PAY, payAmount, payAmount, tradeProductType, productId, ipInfo, operatorId, storeId,
                    (mchTradeOrder) -> {

                        mchTradeOrder.setTradeOrderId(tradeOrderId);  //tradeOrderId
                    });

            //调起上游支付接口
            JSONObject retJSON = rpc.rpcMchTradeOrderService.callPayInterface(addMchTradeOrder, mchInfo, payUrl, mchNotifyUrl);
            if(retJSON == null) { //支付网关返回结果验证失败
                throw ServiceException.build(RetEnum.RET_MCH_CREATE_TRADE_ORDER_FAIL);
            }

            JSONObject result = new JSONObject();
            result.put("status", "ok");
            result.put("tradeOrderId", addMchTradeOrder.getTradeOrderId()); //支付订单号
            result.put("orderAmount", addMchTradeOrder.getOrderAmount()); //订单金额
            result.put("discountAmount", addMchTradeOrder.getDiscountAmount()); //订单优惠金额金额
            result.put("amount", addMchTradeOrder.getAmount()); //实际支付金额
            result.put("payParam", retJSON.getString("payParams")); //支付参数
            this.clearPayToken(requestParam); //调起支付后清空token
            return XxPayResponse.buildSuccess(result);
        }catch (ServiceException e) {
            return XxPayResponse.build(e.getRetEnum());
        }catch (Exception e) {
            _log.error("系统异常", e);
            return XxPayResponse.build(RetEnum.RET_COMM_OPERATION_FAIL);
        }
    }


    /** 查询主发起方的通道参数 **/
    private JSONObject getMainParam(Long mchId, Integer productId){

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE){ //TODO 私有商户

            return null;
        }

        //服务商ID
        Long isvId = mchInfo.getIsvId();

        //查询服务商的产品对应的通道信息
        FeeScale feeScale = rpc.rpcFeeScaleService.getPayFeeByIsv(isvId, productId);
        if(feeScale == null) throw ServiceException.build(RetEnum.RET_ISV_PASSAGE_CONFIG_ERROR);  //服务商未配置该支付通道

        //查询该产品对应的 支付接口类型
        String ifTypeCode = feeScale.getExtConfig();

        //根据支付接口类型 与 isvId 查询到该支付接口的配置参数
        PayPassage isvPayPassage = rpc.rpcPayPassageService.selectByIsv(isvId, ifTypeCode);
        return JSONObject.parseObject(isvPayPassage.getIsvParam());
    }

    /** 查询主发起方的接口类型 **/
    private String getMainIfTypeCode(Long mchId, Integer productId){

        MchInfo mchInfo = rpc.rpcMchInfoService.findByMchId(mchId);
        if(mchInfo.getType() == MchConstant.MCH_TYPE_PRIVATE){ //TODO 私有商户

            return null;
        }

        //服务商ID
        Long isvId = mchInfo.getIsvId();

        //查询服务商的产品对应的通道信息
        FeeScale feeScale = rpc.rpcFeeScaleService.getPayFeeByIsv(isvId, productId);
        if(feeScale == null) throw ServiceException.build(RetEnum.RET_ISV_PASSAGE_CONFIG_ERROR);  //服务商未配置该支付通道

        return feeScale.getExtConfig();
    }



    /** 根据UA获取支付产品 */
    private Integer getProductIdByUA() {

        String ua = request.getHeader("User-Agent");

        // 无法识别扫码客户端
        if (StringUtils.isBlank(ua)) return null;

        if(ua.contains("Alipay")) {
            return PayConstant.PAY_PRODUCT_ALIPAY_JSAPI;  //支付宝服务窗支付
        }else if(ua.contains("MicroMessenger")) {
            return PayConstant.PAY_PRODUCT_WX_JSAPI; //微信公众号jsapi支付
        }else if(ua.contains("jdapp") || ua.contains("JDJR")) { //京东app || 京东金融app
            return PayConstant.PAY_PRODUCT_JD_H5; //京东H5
        }

        _log.error("getProductIdByUA is null, User-Agent = {}", ua);
        return null;
    }


    /** 验证token是否有效, 当验证失败向外抛出异常  **/
    private void checkPayToken(JSONObject requestParam){

        String payToken = getString(requestParam, "payToken");
        if(StringUtils.isEmpty(payToken)){
            throw ServiceException.build(RetEnum.RET_MCH_MEMBER_TOKEN_EXPIRED);
        }

        String redisVal = stringRedisTemplate.opsForValue().get(MchConstant.CACHEKEY_QR_PAY + payToken);
        if(StringUtils.isEmpty(redisVal)){
            throw ServiceException.build(RetEnum.RET_MCH_MEMBER_TOKEN_EXPIRED);
        }
    }

    /** 生成新的支付token  **/
    private String genPayToken(){
        String payToken = MySeq.getUUID();
        stringRedisTemplate.opsForValue().set(MchConstant.CACHEKEY_QR_PAY + payToken, "1", MchConstant.CACHE_QR_PAY_TIMEOUT, TimeUnit.MINUTES);
        return payToken;
    }

    /** 清空 token  **/
    private void clearPayToken(JSONObject param){
        stringRedisTemplate.delete(MchConstant.CACHEKEY_QR_PAY + param.getString("payToken"));
    }


    /** 跳转到错误页面 **/
    private String toErrorPage(RetEnum retEnum){

//        qrcodeUrl = "http://192.168.0.112:8080";   //TODO 测试页面
        String errorPageUrl = String.format(qrcodeUrl + "/index.html#/payError?errCode=%s&errMsg=%s",
                retEnum.getCode(), URLEncoder.encode(retEnum.getMessage()));
        try {
            response.sendRedirect(errorPageUrl); //跳转到页面， 带入相关参数
        } catch (IOException e) {
            _log.error("跳转失败, message={}", e.getMessage());
        }

        return null;
    }
}
