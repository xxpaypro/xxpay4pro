package org.xxpay.pay.channel.yykpay;

import org.xxpay.core.common.util.XXPayUtil;
import org.xxpay.pay.channel.yykpay.util.PaymentUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 2018/8/20
 * @description:
 */
public class YykpayDemo {


    public static void main(String[] args) {

        String bizType = "PROFESSION";
        String merchantNo = "10001118347";
        String merchantOrderNo = "420180818235522633385";
        String requestAmount = "100.00";
        String url = "http://api.serverpay.net/Pay/Post-Callback.Aspx";
        String cardAmt = "100.00";
        String cardNo = "16068170448591540";
        String cardPwd = "341076429168611821";
        String cardCode = "MOBILE";
        String productName = "card中";
        String productType = "card";
        String productDesc = "card";
        String extInfo = "";
        String keyValue = "laizengzhiephia16e7e4e8uc0rifru4g09by1o9jy46os9m5g0u0gi0lefm";

        String hmac = PaymentUtil.buildPayHmac(bizType, merchantNo, merchantOrderNo, requestAmount, url,
                cardAmt, cardNo, cardPwd, cardCode, productName, productType, productDesc, extInfo, keyValue);

        /*提交POST参数=bizType=PROFESSION&merchantNo=10001118347&merchantOrderNo=K20180818235522633385&requestAmount=100.00&
        url=http://api.serverpay.net/Pay/Post-Callback.Aspx&cardAmt=100.00
        &cardNo=16068170448591540&cardPwd=341076429168611820&cardCode=MOBILE&productName=card&productType=card&productDesc=card&extInfo=huawei2017&hmac=2168a48c175ce1d55fcf78ff88cbdf5b*/

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

        String params = XXPayUtil.genUrlParams2(param);

        String reqUrl = "http://www.yeeyk.com/yeex-iface-app/acquiring?" + params;

        System.out.println("reqUrl=" + reqUrl);

        String result = XXPayUtil.call4Post(reqUrl);

        System.out.println(result);



    }


}
