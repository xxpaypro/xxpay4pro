package org.xxpay.pay.ctrl.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xxpay.core.common.constant.MchConstant;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.constant.PayEnum;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.MySeq;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.*;
import org.xxpay.pay.channel.PaymentInterface;
import org.xxpay.pay.ctrl.common.BaseController;
import org.xxpay.pay.service.PayOrderService;
import org.xxpay.pay.service.RpcCommonService;
import org.xxpay.pay.util.SpringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description: 支付订单,包括:统一下单,订单查询,补单等接口
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
@RestController
public class PayOrderController extends BaseController {

    private final MyLog _log = MyLog.getLog(PayOrderController.class);

    @Autowired
    private RpcCommonService rpcCommonService;

    @Autowired
    private PayOrderService payOrderService;


    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/pay/create_order")
    public AbstractRes payOrder(HttpServletRequest request) {
        _log.info("###### 开始接收商户统一下单请求 ######");
        String logPrefix = "【商户统一下单】";
        try {
            JSONObject po = getJsonParam(request);
            _log.info("{}请求参数:{}", logPrefix, po);
            JSONObject payContext = new JSONObject();
            PayOrder payOrder = null;
            // 验证参数有效性
            Object object = validateParams(po, payContext, request);
            if (object instanceof String) {
                _log.info("{}参数校验不通过:{}", logPrefix, object);
                return ApiBuilder.bizError(object.toString());
            }
            
            if (object instanceof PayOrder) payOrder = (PayOrder) object;
            if(payOrder == null) return ApiBuilder.bizError("支付中心下单失败");
            
            //订单存在并且已过期
            boolean hasOrderAndExpire = rpcCommonService.rpcPayOrderService.hasOrderAndExpire(payOrder.getMchId(), payOrder.getMchOrderNo());
            if(hasOrderAndExpire){
                return ApiBuilder.bizError("支付中心下单失败");
            }
            
            //查询是否支持重复下单
        	String retData = rpcCommonService.rpcPayOrderExtService.queryCanRepeatPlaceOrderExt(payOrder.getMchId(), payOrder.getMchOrderNo());
        	if(StringUtils.isNotEmpty(retData)){
                return JSONObject.parseObject(retData, PayRes.class);
        	}
            
            payOrder.setPayOrderId(MySeq.getPay()); //所有校验通过后 新增订单序列号，避免验证失败浪费序列号
            int result = rpcCommonService.rpcPayOrderService.createPayOrder(payOrder);
            _log.info("{}创建支付订单,结果:{}", logPrefix, result);
            if(result != 1) {
                return ApiBuilder.bizError("支付中心下单失败");
            }
            String channelId = payOrder.getChannelId();
            String channelName = channelId.substring(0, channelId.indexOf("_"));
            PaymentInterface paymentInterface;
            try {
                paymentInterface = (PaymentInterface) SpringUtil.getBean(channelName.toLowerCase() +  "PaymentService");
            }catch (BeansException e) {
                _log.error(e, "");
                return ApiBuilder.bizError("调用支付渠道失败");
            }

            // 执行支付
            AbstractRes res = paymentInterface.pay(payOrder);
            if(res == null) return ApiBuilder.bizError("调用支付渠道失败");

            //插入订单扩展信息
            rpcCommonService.rpcPayOrderExtService.addExtInfo(payOrder.getPayOrderId(), payOrder.getMchId(),
                    payOrder.getMchOrderNo(), res.toJSONString());

            //处理签名
            res.autoGenSign(payContext.getString("key"));

            return res;

        }catch (Exception e) {
            _log.error(e, "");
            return ApiBuilder.bizError(PayEnum.ERR_0010);
        }
    }

    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject payContext, HttpServletRequest request) {
        String riskLog = "[支付风控]";
        // 验证请求参数,参数有问题返回错误提示
        String errorMessage;
        // 支付参数
        String mchId = params.getString("mchId"); 			    // 商户ID
        String appId = params.getString("appId");              // 应用ID
        String productId = params.getString("productId");      // 支付产品ID
        String mchOrderNo = params.getString("mchOrderNo"); 	// 商户订单号
        String amount = params.getString("amount"); 		    // 支付金额（单位分） /  押金金额
        String currency = params.getString("currency");        // 币种
        String clientIp = params.getString("clientIp");	    // 客户端IP
        String device = params.getString("device"); 	        // 设备
        String channelUserId = params.getString("channelUserId"); 	        //渠道用户号
        String extra = params.getString("extra");		        // 特定渠道发起时额外参数
        String param1 = params.getString("param1"); 		    // 扩展参数1
        String param2 = params.getString("param2"); 		    // 扩展参数2
        String notifyUrl = params.getString("notifyUrl"); 		// 支付结果回调URL
        String returnUrl = params.getString("returnUrl"); 		// 支付结果同步请求url
        String sign = params.getString("sign"); 				// 签名
        String subject = params.getString("subject");	        // 商品主题
        String body = params.getString("body");	            // 商品描述信息
        String depositMode = params.getString("depositMode");  //是否押金模式 1.押金 其他为非押金模式
        boolean isDepositMode = "1".equals(depositMode);

        // 验证请求参数有效性（必选项）
        Long mchIdL;
        if(StringUtils.isBlank(mchId) || !NumberUtils.isDigits(mchId)) {
            errorMessage = "请求参数[mchId]不能为空且为数值类型.";
            return errorMessage;
        }
        mchIdL = Long.parseLong(mchId);
        // 查询商户信息
        MchInfo mchInfo = rpcCommonService.rpcMchInfoService.findByMchId(mchIdL);
        if(mchInfo == null) {
            errorMessage = "商户不存在[mchId="+mchId+"].";
            return errorMessage;
        }
        if(mchInfo.getStatus() != MchConstant.PUB_YES) {
            errorMessage = "商户状态不可用[mchId="+mchId+"].";
            return errorMessage;
        }

        if(StringUtils.isBlank(productId) || !NumberUtils.isDigits(productId)) {
            errorMessage = "请求参数[productId]不能为空且为数值类型.";
            return errorMessage;
        }

        Integer productIdI = Integer.parseInt(productId);  //产品ID
        if(StringUtils.isBlank(mchOrderNo)) return "请求参数[mchOrderNo]不能为空.";
        if(!NumberUtils.isDigits(amount)) return "请求参数[amount]应为数值类型.";
        if(StringUtils.isBlank(currency)) return "请求参数[currency]不能为空.";
        if(StringUtils.isBlank(notifyUrl)) return "请求参数[notifyUrl]不能为空.";
        if(StringUtils.isBlank(subject)) return "请求参数[subject]不能为空.";
        if(StringUtils.isBlank(body)) return "请求参数[body]不能为空.";
        if (StringUtils.isEmpty(sign)) return "请求参数[sign]不能为空.";

        Long amountL = Long.parseLong(amount);
        if(amountL <= 0) return "请求参数[amount]必须大于0.";


        Integer mchOrderCount = rpcCommonService.rpcPayOrderService.count(new QueryWrapper<PayOrder>().lambda()
                .eq(PayOrder::getMchId, mchIdL)
                .eq(PayOrder::getMchOrderNo, mchOrderNo));
        if(mchOrderCount > 0){
            return "订单号["+mchOrderNo+"]已存在.";
        }

        // 查询应用信息
        // TODO 暂时不用该商户应用表
        /*if(StringUtils.isNotBlank(appId)) {
            MchApp mchApp = rpcCommonService.rpcMchAppService.findByMchIdAndAppId(mchIdL, appId);
            if(mchApp == null) return "应用不存在[appId=" + appId + "]";
            if(mchApp.getStatus() != MchConstant.PUB_YES) return "应用状态不可用[appId=" + appId + "]";
        }*/

        String key = mchInfo.getPrivateKey();
        if (StringUtils.isBlank(key))  return "商户私钥为空,请配置商户私钥[mchId="+mchId+"].";

        payContext.put("key", key);


        //查询服务商的配置信息
        IsvInfo isvInfo = rpcCommonService.rpcIsvInfoService.getById(mchInfo.getIsvId());
        FeeScale isvFeeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByIsv(mchInfo.getIsvId(), productIdI);
        FeeScale mchFeeScale = rpcCommonService.rpcFeeScaleService.getPayFeeByMch(mchInfo.getMchId(), productIdI);

        if(isvFeeScale == null) {
            return "服务商没有该产品的支付通道[productId="+productId+",isvId="+mchInfo.getIsvId()+"]";
        }
        if(isvFeeScale.getStatus() != MchConstant.PUB_YES) {
            return "服务商该产品的支付通道[productId="+productId+",isvId="+mchInfo.getIsvId()+"]已关闭";
        }

        if(mchFeeScale == null) {
            return "商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+"]";
        }
        if(mchFeeScale.getStatus() != MchConstant.PUB_YES) {
            return "商户该产品的支付通道[productId="+productId+",mchId="+mchId+"]已关闭";
        }

        String ifTypeCode = isvFeeScale.getExtConfig(); //获取支付接口类型

        PayPassage isvPassage = rpcCommonService.rpcPayPassageService.selectByIsv(mchInfo.getIsvId(), ifTypeCode);

        PayProduct payProduct = rpcCommonService.rpcPayProductService.findById(productIdI);

        //获取商户支付通道
        PayPassage mchPassage = rpcCommonService.rpcPayPassageService.selectByMch(mchIdL, ifTypeCode);
        if(mchPassage == null) {
            return "商户没有配置支付通道[ifTypeCode="+ifTypeCode+"]";
        }

        //根据支付接口类型 和 支付类型 查询唯一的支付接口
        PayInterface payInterface = rpcCommonService.rpcPayInterfaceService.findByTypeCodeAndPayType(ifTypeCode, payProduct.getPayType());
        String ifCode = payInterface.getIfCode(); //支付接口代码

        if(ifCode == null) {
            return "商户没有该产品的支付通道[productId="+productId+",mchId="+mchId+",channelId="+ifCode+"]";
        }

        //条码支付需要使用到用户条码信息， extra必填
        if (PayConstant.PAY_PRODUCT_ALIPAY_BAR == productIdI || PayConstant.PAY_PRODUCT_WX_BAR == productIdI ) {
            if(StringUtils.isBlank(extra)) {
                return "请求参数[extra]不能为空.";
            }
        }

        // 根据不同渠道,判断extra参数
        if(PayConstant.PAY_CHANNEL_WX_JSAPI.equalsIgnoreCase(ifCode)) {
            if(StringUtils.isEmpty(channelUserId)) {
                return "请求参数[channelUserId]不能为空.";
            }
            if(StringUtils.isBlank(clientIp)) {
                return "请求参数[clientIp]不能为空.";
            }
        }
        if(PayConstant.PAY_CHANNEL_ALIPAY_JSAPI.equalsIgnoreCase(ifCode)) {
            if(StringUtils.isEmpty(channelUserId)) {
                return "request params[channelUserId] error.";
            }
        }
        if(PayConstant.PAY_CHANNEL_WX_MINI.equalsIgnoreCase(ifCode)) {
            if(StringUtils.isEmpty(channelUserId)) {
                return "请求参数[channelUserId]不能为空.";
            }
            if(StringUtils.isBlank(clientIp)) {
                return "请求参数[clientIp]不能为空.";
            }
        }
        if(PayConstant.PAY_CHANNEL_WX_APP.equalsIgnoreCase(ifCode)) {
            if(StringUtils.isBlank(appId)) {
                return "请求参数[appId]不能为空.";
            }
        }

        // 验证签名数据
        boolean verifyFlag = XXPayUtil.verifyPaySign(params, key);
        if(!verifyFlag) {
            return "验证签名失败.";
        }

        // 验证参数通过,返回JSONObject对象
        PayOrder payOrder = new PayOrder();
        payOrder.setMchId(mchIdL);
        payOrder.setMchType(mchInfo.getType());
        // 该appId指微信支付下的子商户appId，在公众号或小程序支付下，如果传了该值
        // 那么openId指的是该appId下的，否则指的是服务商appId下的
        payOrder.setAppId(appId);
        payOrder.setMchOrderNo(mchOrderNo);
        payOrder.setAgentId(mchInfo.getAgentId());
        payOrder.setProductId(productIdI);                        // 支付产品ID
        payOrder.setProductType(isvFeeScale.getProductType());    // 产品类型

        payOrder.setMchPassageId(mchPassage.getId()); // 商户支付通道ID
        payOrder.setIsvPassageId(isvPassage.getId()); //服务商支付通道ID
        payOrder.setIsvId(isvInfo.getIsvId()); //isvId
        payOrder.setChannelType(ifTypeCode);
        payOrder.setChannelId(ifCode);
        payOrder.setAmount(amountL);
        payOrder.setCurrency(currency);
        payOrder.setClientIp(clientIp);
        payOrder.setDevice(device);
        payOrder.setSubject(subject);
        payOrder.setBody(body);
        payOrder.setExtra(extra);
        payOrder.setChannelMchId("");   //以前存在支付通道子账号表
        payOrder.setChannelUser(channelUserId);
        payOrder.setParam1(param1);
        payOrder.setParam2(param2);
        payOrder.setNotifyUrl(notifyUrl);
        payOrder.setReturnUrl(returnUrl);

        if(isDepositMode){ //押金支付模式
            payOrder.setDepositMode(MchConstant.PUB_YES);
            payOrder.setDepositAmount(amountL);
            payOrder.setAmount(0L);
        }

        //使用程序时间，保证创建时间与 过期时间的时间差计算结果一致
        Date nowTime = new Date();
        payOrder.setCreateTime(nowTime);
        payOrder.setExpireTime(DateUtils.addMinutes(nowTime, PayConstant.PAY_EXPIRE_TIME));
        
        return payOrder;
    }
}
