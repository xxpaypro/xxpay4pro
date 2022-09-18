/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 上午11:13:56
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
 * 交易：代付接口<br>
 * 日期： 2018-04<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class AgentPayDemo {
	
	public static  Logger logger = LoggerFactory.getLogger(AgentPayDemo.class);
	
	
	JSONObject request = new JSONObject();
	
	
	/** 
	* 组织请求报文     
	*/
	private void setRequest() {
		
		request.put("version", DemoBase.version);								//版本号      
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);              //产品ID     
		request.put("tranTime", DemoBase.getCurrentTime());                     //交易时间     
		request.put("orderCode", DemoBase.getOrderCode());                      //订单号      
		request.put("timeOut", DemoBase.getNextDayTime());                      //订单超时时间   
		request.put("tranAmt", "000000000012");                                 //金额       
		request.put("currencyCode", DemoBase.CURRENCY_CODE);                    //币种       
		request.put("accAttr", "0");                                            //账户属性     0-对私   1-对公
		request.put("accType", "4");                                            //账号类型      3-公司账户  4-银行卡
		request.put("accNo", "6216261000000000018");                            //收款人账户号   
		request.put("accName", "全渠道");                                       	//收款人账户名   
		request.put("provNo", "");                                              //收款人开户省份编码
		request.put("cityNo", "");                                              //收款人开会城市编码
		request.put("bankName", "");                                            //收款账户开户行名称
		request.put("bankType", "123456123456");                                //收款人账户联行号 
		request.put("remark", "代付");                                          	//摘要       
		request.put("channelType", "");                                         //渠道类型   
		request.put("reqReserved", "");                                         //请求方保留域  
		request.put("extend", "");                                              //扩展域
		
	}
	
	public static void main(String[] args) throws Exception {
		
		AgentPayDemo demo = new AgentPayDemo();
		String reqAddr="/agentpay";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init("", SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID
		
		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.AGENT_PAY, merId, plMid);
		
		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
		}	

	}




	
}
