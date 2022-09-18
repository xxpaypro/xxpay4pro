package org.xxpay.pay.channel.sandpay;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.pay.channel.sandpay.sdk.CertUtil;
import org.xxpay.pay.channel.sandpay.sdk.CryptoUtil;
import org.xxpay.pay.channel.sandpay.sdk.HttpClient;
import org.xxpay.pay.channel.sandpay.sdk.SDKUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 名称： demo中用到的方法<br>
 * 日期： 2018-04<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class SandpayBase {
	
	public static MyLog _log = MyLog.getLog(SandpayBase.class);
	
	//默认配置的是UTF-8
	public static String encoding = "UTF-8";
	//签名类型,默认01-SHA1+RSA
	public static String signType = "01";
	//版本号 默认 1.0
	public static String version = "1.0";	
	//http连接超时时间
	public static int connectTimeout = 300000;
	//http响应超时时间
	public static int readTimeout = 600000;

	// 扫码方式
	public static final String PAY_TOOL_QR_ALIPAY = "0401"; 	// 支付宝扫码
	public static final String PAY_TOOL_QR_WXPAY = "0402";  	// 微信扫码
	public static final String PAY_TOOL_QR_UNIONPAY = "0403"; 	// 银联扫码
	
	//产品编码
	public static String PRODUCTID_WXPAY = "00000005";		//微信扫码
	public static String PRODUCTID_ALIPAY = "00000006";		//支付宝扫码
	public static String PRODUCTID_UNIONPAY = "00000012";	// 银联正扫
	public static String PRODUCTID_B2C = "00000007";		//网银B2C
	public static String PRODUCTID_B2B = "00000028";		//网银B2B
	
	//接口名称
	public static String QR_ORDERPAY = "sandpay.trade.barpay";					//统一下单并支付
	public static String QR_ORDERCREATE = "sandpay.trade.precreate";			//预下单
	public static String QR_ORDERQUERY = "sandpay.trade.query";					//订单查询
	public static String QR_ORDERREFUND = "sandpay.trade.refund";				//退货
	public static String QR_CLEARFILEDOWNLOAD = "sandpay.trade.download";		//对账单下载

	//接口名称
	public static String ORDERPAY = "sandpay.trade.pay";					//统一下单
	public static String ORDERQUERY = "sandpay.trade.query";				//订单查询
	public static String ORDERREFUND = "sandpay.trade.refund";				//退货
	public static String CLEARFILEDOWNLOAD = "sandpay.trade.download";		//对账单下载
	
	//请求时间 格式:YYYYMMDDhhmmss
	public static String getCurrentTime() {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	}
	
	//获取当前时间24小时后的时间
	public static String getNextDayTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
	}
	
	//商户订单号
	public static String getOrderCode() {
		Random rand = new Random();
		int num=rand.nextInt(100)+1;
		return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+num;
	}
	
	
	
	public static JSONObject requestServer(JSONObject header,JSONObject body, String mchId, String publicKeyPath, String privateKeyPath, String keyPassword, String reqUrl) {
		
		Map<String, String> reqMap = new HashMap<String, String>();
		JSONObject reqJson=new JSONObject();
		reqJson.put("head", header);
		reqJson.put("body", body);
		String reqStr=reqJson.toJSONString();
		String reqSign;

		// 签名
		try {

			CertUtil.init(mchId, publicKeyPath, privateKeyPath, keyPassword);
			reqSign = new String(Base64.encodeBase64(CryptoUtil.digitalSign(reqStr.getBytes(encoding), CertUtil.getPrivateKey(mchId), "SHA1WithRSA")));
		} catch (Exception e) {
			_log.error(e.getMessage(), "");
			return null;
		}
		// 整体报文格式
		reqMap.put("charset", encoding);
		reqMap.put("data", reqStr);
		reqMap.put("signType", signType);
		reqMap.put("sign", reqSign);
		reqMap.put("extend", "");
		
		String result;
		try {
			_log.info("[杉德商户:{}]请求URL:{}, 报文：\n{}", mchId, reqUrl, JSONObject.toJSONString(reqJson, true));
			result = HttpClient.doPost(reqUrl, reqMap, connectTimeout, readTimeout);
			result = URLDecoder.decode(result, encoding);
		} catch (IOException e) {
			_log.error(e.getMessage());
			return null;
		}
		
		Map<String, String> respMap = SDKUtil.convertResultStringToMap(result);
		String respData = respMap.get("data");
		String respSign = respMap.get("sign");
		
		// 验证签名
		boolean valid;
		try {
			valid = CryptoUtil.verifyDigitalSign(respData.getBytes(encoding), Base64.decodeBase64(respSign), CertUtil.getPublicKey(),"SHA1WithRSA");
			if(!valid) {
				_log.error("verify sign fail.");
				return null;
			}			
			_log.info("verify sign success");
			JSONObject respJson=JSONObject.parseObject(respData);
			if(respJson!=null) {
				_log.info("[杉德商户:{}]响应码：[{}]", mchId, respJson.getJSONObject("head").getString("respCode"));
				_log.info("[杉德商户:{}]响应描述：[{}]", mchId, respJson.getJSONObject("head").getString("respMsg"));
				_log.info("[杉德商户:{}]响应报文：\n{}", mchId, JSONObject.toJSONString(respJson, true));
			}else {
				_log.error("服务器请求异常！！！");	
			}			
			return respJson;
			
		}  catch (Exception e) {
			_log.error(e.getMessage());
			return null;
		}
	}
	
}
