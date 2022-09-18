/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.demo;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xxpay.pay.channel.transfarpay.util.HttpClient4Utils;
import org.xxpay.pay.channel.transfarpay.util.ParamUtil;


public class BatchPayToCustomerDemo {

	private final static Logger logger = LoggerFactory.getLogger(BatchPayToCustomerDemo.class);

	private final static String charset = "UTF-8";

	public void batchpayToCustomer() {
		// String url = "http://web-enterprisewebapi-vip/enterpriseWebApi/trans/SkipEeterprisec/batchPayToCustomer";
		// ehuodi dpbPolXa26J5mls3ibUm
		String url = "http://openapitest.tf56.com/service/api";
		// ClientUtil cu = new ClientUtil(url, false);
		Map<String, Object> paramsMap = new HashMap<>();
		params1(paramsMap);
		try {
			paramsMap.put("tf_sign", ParamUtil.map2MD5(paramsMap));
			System.out.println("----------tf_sign---------------" + paramsMap.get("tf_sign") + "");
			paramsMap.remove("dog_sk");
			String request = HttpClient4Utils.sendHttpRequest(url, paramsMap, charset, false);
			System.out.println("request:" + request);
			// String retMsg = cu.get(url, paramsMap).toString();
			// JsonResult result = new Gson().fromJson(retMsg, JsonResult.class);
			// System.out.print("------"+retMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void params1(Map<String, Object> paramsMap) {
		// paramsMap.put("tf_timestamp", DateUtils.strDate("yyyyMMddHHmmss"));
		// 流水号0^产品名称1^收款方银行卡号2^收款账号姓名3^付款金额4^银行卡类型5
		// ^银行卡借贷类型6^银行名称7^支行8^省9^市10^联行号11^描述12^备注说明13
		// 1表示对公，0表示对私
		// String paydetail="30123457^批量代付测试^2121211121^4555 555
		// 5^0.01^企业^储蓄卡^中国人民银行^钱江世纪城工行^浙江省^杭州市^300001^描述下这是测试批量代付^ " ;//+
		// "|32323656565656111111111^批量代付测试^2121211121^大牛^102^个人^信用卡^中国人民银行^钱江世纪城工行^浙江省^杭州市^300001^描述下这是测试批量代付^备注这是批量代付|";
		// String businessnumeber = String.valueOf(RandomUtils.nextInt());
		// System.out.println("----------------businessnumeber---------"+businessnumeber);
		// map.put("businessnumber", businessnumeber); // 外部流水号
		String extendparam = "fddfdf^批量代付测试|fdffdf1^fdfdf";
		paramsMap.put("tf_timestamp", "20171109144906");
		paramsMap.put("service_id", "tf56enterprise.batchPay.payApply");
		paramsMap.put("sign_type", "MD5");
		String paydetail = "ON80000201710260953500001^分润代付^6214835894246071^白芳芳^289.00^个人^储蓄卡^招商银行股份有限公司杭州钱塘支行^^^^^激活分润^|ON80000201710271223070001^分润代付^6217001540013424112^韩思念^99.00^个人^储蓄卡^中国建设银行杭州宝石支行留下分理处^^^^^激活分润^";
		paramsMap.put("paydetail", paydetail);
		paramsMap.put("paydate", "20171109");
		paramsMap.put("enterprisecode", "6688150133014814");
		paramsMap.put("batchnum", "2");
		paramsMap.put("batchno", "20171109144906");
		paramsMap.put("batchamount", "388");
		paramsMap.put("backurl", "http://ccadmin.tugou.ren/api/notify");
		paramsMap.put("appid", "1351001");
		paramsMap.put("accountnumber", "8800009860257");
		paramsMap.put("dog_sk", "Pg0qZ1W324yd4F03dQ66");
		// paramsMap.put("dog_ak", "w98ea6tuAeA2460r");
		// paramsMap.put("sign_type", "MD5");
		// paramsMap.put("extendparam", extendparam);
		// map.put("backurl", "www.hao123.com");
	}

	public static void main(String[] args) {
		BatchPayToCustomerDemo batch=new BatchPayToCustomerDemo();
		batch.batchpayToCustomer();
	}

}
