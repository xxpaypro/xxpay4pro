package org.xxpay.pay.channel.yykpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.xxpay.core.common.constant.PayConstant;
import org.xxpay.core.common.domain.api.AbstractRes;
import org.xxpay.core.common.domain.api.ApiBuilder;
import org.xxpay.core.common.domain.api.PayRes;
import org.xxpay.core.common.domain.api.msg.QueryRetMsg;
import org.xxpay.core.common.util.AmountUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.core.entity.PayOrder;
import org.xxpay.pay.channel.BasePayment;
import org.xxpay.pay.channel.yykpay.util.PaymentUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author: dingzhiwei
 * @date: 18/8/20
 * @description: 易游酷支付接口
 */
@Service
public class YykpayPaymentService extends BasePayment {

    private static final MyLog _log = MyLog.getLog(YykpayPaymentService.class);

    @Override
    public String getChannelName() {
        return PayConstant.CHANNEL_NAME_YYKPAY;
    }

    /**
     * 支付
     * @param payOrder
     * @return
     */
    @Override
    public AbstractRes pay(PayOrder payOrder) {

        // 先校验是否有extra参数,并解析校验卡信息
        String extra = payOrder.getExtra();
        if(StringUtils.isBlank(extra)) return ApiBuilder.bizError("参数extra为空");

        JSONObject extraObj = JSONObject.parseObject(extra);
        String cardAmt = extraObj.getString("cardAmt");
        String cardNo = extraObj.getString("cardNo");
        String cardPwd = extraObj.getString("cardPwd");
        String cardCode = extraObj.getString("cardCode");
        if(StringUtils.isBlank(cardAmt) || StringUtils.isBlank(cardNo)
                || StringUtils.isBlank(cardPwd) || StringUtils.isBlank(cardCode) ) {
            return ApiBuilder.bizError("参数extra错误,缺少必要属性");
        }

        YykpayConfig yykpayConfig = new YykpayConfig(getPayParam(payOrder));
        PayRes retObj = ApiBuilder.buildSuccess(PayRes.class);
        String bizType = "PROFESSION";
        String merchantNo = yykpayConfig.getMchId();
        String merchantOrderNo = payOrder.getPayOrderId();
        String requestAmount = AmountUtil.convertCent2Dollar(payOrder.getAmount()+"");
        String url = payConfig.getNotifyUrl(getChannelName());

        String productName = payOrder.getSubject() == null ? "" : payOrder.getSubject();
        String productType = payOrder.getSubject() == null ? "" : payOrder.getSubject();
        String productDesc = payOrder.getBody() == null ? "" : payOrder.getBody();
        String extInfo = "";
        String keyValue = yykpayConfig.getKey();
        // 生成签名
        String hmac = PaymentUtil.buildPayHmac(bizType, merchantNo, merchantOrderNo, requestAmount, url,
                cardAmt, cardNo, cardPwd, cardCode, productName, productType, productDesc, extInfo, keyValue);

        // 支付参数
        Map<String, String> param = new HashMap<>();
        param.put("bizType", bizType);                		// 业务类型
        param.put("merchantNo", merchantNo);                // 商户编号
        param.put("merchantOrderNo", merchantOrderNo);      // 商户订单号
        param.put("requestAmount", requestAmount);          // 订单金额,精确到分
        param.put("url", url);                    		    // 回调地址
        param.put("cardAmt", cardAmt);                		// 卡面额组
        param.put("cardNo", cardNo);                    	// 卡号组
        param.put("cardPwd", cardPwd);                    	// 卡密组
        param.put("cardCode", cardCode);                	// 支付渠道编码
        param.put("productName", productName);            	// 产品名称
        param.put("productType", productType);            	// 产品类型
        param.put("productDesc", productDesc);              // 产品描述
        param.put("extInfo", extInfo);                    	// 扩展信息
        param.put("hmac", hmac);

        _log.info("[{}]下单请求参数:{}", getChannelName(), param);
        String params = XXPayUtil.genUrlParams2(param);
        String reqUrl = yykpayConfig.getReqUrl() + "?" + params;
        // 请求接口
        String resStr = XXPayUtil.call4Post(reqUrl);
        _log.info("[{}]下单返回结果:{}", getChannelName(), resStr);
        String code = "";
        String message = "";
        if(resStr != null) {
            JSONObject resObj = JSONObject.parseObject(resStr);
            code = resObj.getString("code");
            message = resObj.getString("message");

            // 收单成功
            if("000000".equals(code)) {
                int result = rpcCommonService.rpcPayOrderService.updateStatus4Ing(payOrder.getPayOrderId(), null);
                _log.info("[{}]更新订单状态为支付中:payOrderId={},result={}", getChannelName(), payOrder.getPayOrderId(), result);
            }else {
                // 收单失败

            }
        }

        retObj.setPayOrderId(payOrder.getPayOrderId()); // 设置支付订单ID

        JSONObject payParams = new JSONObject();
        payParams.put("code", code);
        payParams.put("message", message);
        retObj.setPayParams(payParams); //自定义参数
        retObj.setPayMethod(PayConstant.PAY_METHOD_OTHER); //其他支付方式
        return retObj;
    }

    /**
     * 查询订单
     * @param payOrder
     * @return
     */
    @Override
    public QueryRetMsg query(PayOrder payOrder) {
        YykpayConfig yykpayConfig = new YykpayConfig(getPayParam(payOrder));
        String keyValue = yykpayConfig.getKey();
        String queryUrl = yykpayConfig.getQueryUrl();

        String merchantNo = yykpayConfig.getMchId();
        String merchantOrderNo = payOrder.getPayOrderId();

        SortedMap<String,String> param = new TreeMap();
        param.put("merchantNo", merchantNo);              // 商户号
        param.put("merchantOrderNo", merchantOrderNo);    // 商户订单号

        // 生成签名
        String hmac = PaymentUtil.buildQueryHmac(merchantNo, merchantOrderNo, keyValue);

        param.put("hmac", hmac);

        try {
            _log.info("[{}]查单请求参数:{}", getChannelName(), param);
            String params = XXPayUtil.genUrlParams2(param);
            String reqUrl = yykpayConfig.getReqUrl() + "?" + params;
            // 请求接口
            String resStr = XXPayUtil.call4Post(reqUrl);
            _log.info("[{}]查单返回结果:{}", getChannelName(), resStr);
            String code = "";
            String data = "";
            if(resStr != null) {
                JSONObject resObj = JSONObject.parseObject(resStr);
                code = resObj.getString("code");
                data = resObj.getString("data");

                // 收单成功
                if("000000".equals(code)) {
                    JSONObject dataObj = JSONObject.parseObject(data);
                    // 支付结果
                    String result = dataObj.getString("result");
                    if("SUCCESS".equals(result)) {  // 支付成功
                        return QueryRetMsg.confirmSuccess(null);
                    }else {
                        return QueryRetMsg.waiting();
                    }
                }else {
                    // 收单失败
                    return QueryRetMsg.confirmFail();

                }
            }else {
                return QueryRetMsg.unknown();
            }
        } catch (Exception e) {
            _log.error(e, "");
            return QueryRetMsg.sysError();
        }
    }

}
