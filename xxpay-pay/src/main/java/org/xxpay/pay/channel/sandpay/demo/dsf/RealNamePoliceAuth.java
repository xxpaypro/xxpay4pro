/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 下午6:17:14
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * pxl         2018-4-25        Initailized
 */
package org.xxpay.pay.channel.sandpay.demo.dsf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.alibaba.fastjson.JSONObject;
import org.xxpay.pay.channel.sandpay.sdk.CertUtil;
import org.xxpay.pay.channel.sandpay.sdk.SDKConfig;

/**
 * 产品：杉德代收付产品<br>
 * 交易：实名公安认证<br>
 * 日期： 2018-04<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class RealNamePoliceAuth {

public static  Logger logger = LoggerFactory.getLogger(RealNamePoliceAuth.class);
	
	
	JSONObject request = new JSONObject();
	
	
	public static void main(String[] args) throws Exception {
		
		RealNamePoliceAuth demo = new RealNamePoliceAuth();
		String reqAddr="/idCardVerify";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init("", SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID
		
		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.REALNAME_POLICE_AUTH, merId, plMid);
		
		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("认证状态(在respCode为0000时有值0-通过 1-认证失败)：["+resp.getString("validateStatus")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
		}	

	}


	/** 
	*      
	*/
	private void setRequest() {
		
		request.put("version", DemoBase.version);							// 版本号      
		request.put("productId", DemoBase.PRODUCTID_COLLECTION_TOC);      	// 产品ID      
		request.put("tranTime", DemoBase.getCurrentTime());               	// 交易时间    
		request.put("orderCode", DemoBase.getOrderCode());                	// 订单号      
		request.put("name", "罗福");                                      	// 姓名        
		request.put("certType", "0101");                                  	// 证件类型    
		request.put("certNo", "350424198806210053");                      	// 证件号码    
		request.put("returnPic", "1");                                    	// 是否返回图片
		request.put("channelType", "");                                   	// 渠道类型    
		request.put("extend", "");                                        	// 扩展域 
		
	}
}
