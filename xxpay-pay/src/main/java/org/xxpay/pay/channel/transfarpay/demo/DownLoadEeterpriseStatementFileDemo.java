/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xxpay.pay.channel.transfarpay.util.DateUtils;
import org.xxpay.pay.channel.transfarpay.util.HttpClient4Utils;
import org.xxpay.pay.channel.transfarpay.util.ParamUtil;

/**
 * 描述说明
 * 
 * @version V1.0
 * @author huzz
 * @Date 2017年5月13日 下午8:26:11
 * @since JDK 1.7
 */
public class DownLoadEeterpriseStatementFileDemo {

	private final static Logger logger = LoggerFactory.getLogger(DownLoadEeterpriseStatementFileDemo.class);

	private final static String charset = "UTF-8";

	public static void main(String[] args) {
		String url = "https://openapitest.tf56.com/service/api";
		Map<String, Object> map = new HashMap<>();
		map.put("service_id", "tf56pay.enterprise.payForCustomer");
		map.put("appid", "2013001");
		map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
		map.put("sign_type", "MD5");
		map.put("businessnumber", "201704140000000");
		map.put("subject", "YEEYK");
		map.put("transactionamount", "0.01");
		map.put("bankcardnumber", "1234234");
		map.put("bankcardname", "测试银行账户");
		map.put("bankname", "测试银行卡分行");
		map.put("bankcardtype", "个人");
		map.put("bankaccounttype", "储蓄卡");
		map.put("fromaccountnumber", "8801111111377");
		// map.put("dog_ak", "RU3Gv6584678xf8r");
		map.put("dog_sk", "08Oe4YI71I5S3e72qYT2");

		try {
			map.put("tf_sign", ParamUtil.map2MD5(map));
			System.out.println("-------------------------" + map.get("tf_sign") + "");
			map.remove("dog_sk");
			String request = HttpClient4Utils.sendHttpRequest(url, map, charset, false);
			System.out.println("request:" + request);
			// String param = HttpDownload.convertStringParamter(map);
			// HttpDownload.downLoadNew(url + "?" + param, "");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
