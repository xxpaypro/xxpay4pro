/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xxpay.pay.channel.transfarpay.util.DateUtils;
import org.xxpay.pay.channel.transfarpay.util.HttpClient4Utils;
import org.xxpay.pay.channel.transfarpay.util.ParamUtil;

import java.util.HashMap;
import java.util.Map;

public class QueryForCustomerDemo {

	private final static Logger logger = LoggerFactory.getLogger(QueryForCustomerDemo.class);

	private final static String charset = "UTF-8";

	private static void params1(Map<String, Object> map) {
/*
		appid :appid= 1495001
		dog_sk = 8f65H555J4O1uW8E4VfA
		商户编号：enterpriseCode =' 5688250133016393 '
		支付账号：accountnumber = 8802000087147
*/


		/*map.put("service_id", "tf56pay.enterprise.payForCustomer");
		map.put("appid", "1495001");
		map.put("bankaccounttype", "储蓄卡");
		map.put("bankcardnumber", "6214830126778025");//
		map.put("bankcardname", "丁志伟");
		map.put("bankcardtype", "个人");
		map.put("bankname", "招商银行");
		map.put("businessnumber", "test" + System.currentTimeMillis());
		map.put("fromaccountnumber", "8802000087147");
		map.put("subject", "提现");
		map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
		map.put("transactionamount", "500");
		map.put("dog_sk", "8f65H555J4O1uW8E4VfA");
		map.put("sign_type", "MD5");*/


		map.put("service_id", "tf56pay.enterprise.queryTradeStatus");
		map.put("appid", "1812001");
		map.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));

		map.put("businessnumber", "T01201808160933005930023");

		map.put("dog_sk", "2x2B75H88kXlt0Gml8r6");
		map.put("sign_type", "MD5");

	}

	public static void main(String[] args) {
		//String url = "https://openapitest.tf56.com/service/api";	// 测试
		String url = "https://openapi.tf56.com/service/api";	// 生产
		Map<String, Object> map = new HashMap<>();
		params1(map);
		try {
			map.put("tf_sign", ParamUtil.map2MD5(map));
			System.out.println("-------------------------" + map.get("tf_sign") + "");
			map.remove("dog_sk");
			 String request = HttpClient4Utils.sendHttpRequest(url, map, charset, false);
			 System.out.println("request:" + request);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void params2(Map<String, Object> map) {

		map.put("service_id", "tf56pay.enterprise.queryEnterpriseAccountBanlanc");
		map.put("appid", "1495001");
		map.put("tf_timestamp", "20171124150613");
		map.put("accountnumber", "8802000087147");
		map.put("dog_sk", "8f65H555J4O1uW8E4VfA");
	}
}
