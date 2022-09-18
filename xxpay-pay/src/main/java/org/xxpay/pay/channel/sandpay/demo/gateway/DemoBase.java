package org.xxpay.pay.channel.sandpay.demo.gateway;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xxpay.pay.channel.sandpay.sdk.*;

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
public class DemoBase {
	
	public static  Logger logger = LoggerFactory.getLogger(DemoBase.class);
	
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
	
	
	
	public static JSONObject requestServer(JSONObject header,JSONObject body,String reqAddr) {
		
		Map<String, String> reqMap = new HashMap<String, String>();
		JSONObject reqJson=new JSONObject();
		reqJson.put("head", header);
		reqJson.put("body", body);
		String reqStr=reqJson.toJSONString();
		String reqSign;
		// 签名
		try {
			reqSign = new String(Base64.encodeBase64(CryptoUtil.digitalSign(reqStr.getBytes(encoding), CertUtil.getPrivateKey(), "SHA1WithRSA")));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
		//整体报文格式
		reqMap.put("charset", encoding);
		reqMap.put("data", reqStr);
		reqMap.put("signType", signType);
		reqMap.put("sign", reqSign);
		reqMap.put("extend", "");
		
		String result;
		try {
			logger.info("请求报文：\n"+JSONObject.toJSONString(reqJson));	
			result = HttpClient.doPost(SDKConfig.getConfig().getUrl()+reqAddr, reqMap, connectTimeout, readTimeout);
			logger.info("响应报文：\n" + result);
			result = URLDecoder.decode(result, encoding);
		} catch (IOException e) {
			logger.error(e.getMessage());
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
				logger.error("verify sign fail.");
				return null;
			}			
			logger.info("verify sign success");
			JSONObject respJson=JSONObject.parseObject(respData);
			if(respJson!=null) {
				logger.info("响应码：["+respJson.getJSONObject("head").getString("respCode")+"]");	
				logger.info("响应描述：["+respJson.getJSONObject("head").getString("respMsg")+"]");	
				logger.info("响应报文：\n"+JSONObject.toJSONString(respJson,true));				
			}else {
				logger.error("服务器请求异常！！！");	
			}			
			return respJson;
			
		}  catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	
	

}
