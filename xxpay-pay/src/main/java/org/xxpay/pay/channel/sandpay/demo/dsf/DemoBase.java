package org.xxpay.pay.channel.sandpay.demo.dsf;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.com.sand.online.agent.service.sdk.CryptoUtil;
import cn.com.sand.online.agent.service.sdk.RandomStringGenerator;
import cn.com.sand.online.agent.service.sdk.SDKUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.pay.channel.sandpay.sdk.CertUtil;
import org.xxpay.pay.channel.sandpay.sdk.HttpClient;
import org.xxpay.pay.channel.sandpay.sdk.SDKConfig;

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
	public static String version = "01";	
	//http连接超时时间
	public static int connectTimeout = 300000;
	//http响应超时时间
	public static int readTimeout = 600000;
	
	public static String CURRENCY_CODE = "156";
	
	//产品编码
	public static String PRODUCTID_COLLECTION_TOB = "00000001";     // 代收对公
	public static String PRODUCTID_COLLECTION_TOC = "00000002";     // 代收对私
	public static String PRODUCTID_AGENTPAY_TOB = "00000003";       // 代付对公
	public static String PRODUCTID_AGENTPAY_TOC = "00000004";       // 代付对私
	
	//交易码
	public static String ORDER_QUERY = "ODQU";					//订单查询
	public static String AGENT_PAY = "RTPM";					//实时代付
	public static String MER_BALANCE_QUERY = "MBQU";			//商户余额查询
	public static String AGENT_PAY_FEE_QUERY = "PTFQ";			//代付手续费查询
	public static String COLLECTION = "RTCO";					//实时代收
	public static String REALNAME_AUTH = "RNAU";				//实名认证
	public static String REALNAME_POLICE_AUTH = "RNPA";			//实名公安认证
	public static String CLEAR_FILE_CONTEXT = "CFCT";			//对账单申请

	//请求时间 格式:YYYYMMDDhhmmss
	public static String getCurrentDate() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
		
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
	
	/**
	 * 获取接入类型 0-商户接入，默认   1-平台接入
	* 
	* @param plId
	* @param merId
	* @return
	 */
	public static String getAccessType(String plId, String merId) {
		
		if(StringUtils.isNotBlank(plId)) {
			return "1";
		}
		return "0";		
	}
	
	
	
	public static JSONObject requestServer(JSONObject request, String reqAddr, String transCode, String merId, String plId) {
		
		String reqData = request.toJSONString();
		logger.info("请求数据：\n"+reqData);	
		
		try {
			
			String aesKey = RandomStringGenerator.getRandomStringByLength(16);
			byte[] aesKeyBytes = aesKey.getBytes("UTF-8");
			
			byte[] plainBytes = reqData.getBytes("UTF-8");
			String encryptData = new String(Base64.encodeBase64(
			        CryptoUtil.AESEncrypt(plainBytes, aesKeyBytes, "AES",
			        "AES/ECB/PKCS5Padding", null)), 
			        "UTF-8");
			
			String sign = new String(Base64.encodeBase64(
				        CryptoUtil.digitalSign(plainBytes, CertUtil.getPrivateKey(),
				        "SHA1WithRSA")), "UTF-8");
			
			String encryptKey = new String(Base64.encodeBase64(
			        CryptoUtil.RSAEncrypt(aesKeyBytes, CertUtil.getPublicKey(), 2048, 11, 
			        "RSA/ECB/PKCS1Padding")), "UTF-8");
			
			String accessType = DemoBase.getAccessType(plId, merId);
			
			Map<String, String> reqMap = new HashMap<String, String>();
			//整体报文格式
			reqMap.put("transCode", transCode); // 交易码
			reqMap.put("accessType", accessType); // 接入类型
			reqMap.put("merId", merId); // 合作商户ID	杉德系统分配，唯一标识
			reqMap.put("plId", plId);  // 平台商户ID	平台接入必填，商户接入为空
			reqMap.put("encryptKey", encryptKey); // 加密后的AES秘钥
			reqMap.put("encryptData", encryptData); // 加密后的请求/应答报文
			reqMap.put("sign", sign); // 签名
			reqMap.put("extend", ""); // 扩展域
			
			String result;
			try {
				logger.info("请求报文：\n"+reqMap);
				result = HttpClient.doPost(SDKConfig.getConfig().getUrl() + reqAddr, reqMap, connectTimeout, readTimeout);
				result = URLDecoder.decode(result, encoding);
			} catch (IOException e) {
				logger.error(e.getMessage());
				return null;
			}
			
			logger.info("响应报文：\n"+result);
			Map<String, String> responseMap = SDKUtil.convertResultStringToMap(result);
			
		    String retEncryptKey = (String)responseMap.get("encryptKey");
		    String retEncryptData = (String)responseMap.get("encryptData");
		    String retSign = (String)responseMap.get("sign");

		    logger.debug("retEncryptKey:[" + retEncryptKey + "]");
		    logger.debug("retEncryptData:[" + retEncryptData + "]");
		    logger.debug("retSign:[" + retSign + "]");

		    byte[] decodeBase64KeyBytes = Base64.decodeBase64(retEncryptKey
		      .getBytes("UTF-8"));

		    byte[] merchantAESKeyBytes = CryptoUtil.RSADecrypt(
		      decodeBase64KeyBytes, CertUtil.getPrivateKey(), 2048, 11, 
		      "RSA/ECB/PKCS1Padding");

		    byte[] decodeBase64DataBytes = Base64.decodeBase64(retEncryptData
		      .getBytes("UTF-8"));

		    byte[] respDataBytes = CryptoUtil.AESDecrypt(decodeBase64DataBytes, 
		      merchantAESKeyBytes, "AES", "AES/ECB/PKCS5Padding", null);

		    String respData = new String(respDataBytes, "UTF-8");
		    logger.info("retData:[" + respData + "]");
		    
		    byte[] signBytes = Base64.decodeBase64(retSign
		    	      .getBytes("UTF-8"));

    	    boolean isValid = CryptoUtil.verifyDigitalSign(respDataBytes, signBytes, 
    	      CertUtil.getPublicKey(), "SHA1WithRSA");
			
			if(!isValid) {
				logger.error("verify sign fail.");
				return null;
			}			
			logger.info("verify sign success");
			
			JSONObject respJson = JSONObject.parseObject(respData);
			return respJson;
			
		}  catch (Exception e) {
			
			logger.error(e.getMessage());
			return null;
		}
	}
	
	
	
	

}
