package org.xxpay.pay.channel.transfarpay.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xxpay.pay.channel.swiftpay.util.MD5;
import org.xxpay.pay.channel.transfarpay.util.MD5Util;

import java.util.HashMap;
import java.util.Map;

/**
 * 说明：代付回调商户验签
 * createUser:20501 
 * updateUser:20501 
 * updateDate:20180507
 */
public class AttestaTion {

	private final static Logger logger = LoggerFactory.getLogger(AttestaTion.class);

	private final static String charset = "UTF-8";

	private static void params1(Map<String, String> map) {
		map.put("subject", "电商的供应商货款");
		map.put("businessnumber", "11111111");
		map.put("businessrecordnumber", "1111");
		map.put("status", "成功");
		map.put("transactiondate", "2018-06-24 10:41:59");
		map.put("billamount", "94.00");
		map.put("transactionamount", "94.00");
		map.put("transactiontype", "代付");
		map.put("appid", "111111");
		map.put("inputdate","2018-06-24 10:41:57");
		map.put("frompartyid", "9864879");
		map.put("fromaccountnumber", "sssssssssssss");
		map.put("remark", "Bill->status:成功,remark:null");
		map.put("backurl", "sssssssssssss");
		

	}
	//0E587715E2164A510736380E3589D0CF
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<>();
		params1(map);
		try {													
			boolean verify = MD5Util.verify(map, "AFA7F0A77A7B77CD6BDDF826B4E3DE8D", "sssssssssssss", charset);
			if(verify) {
				System.out.println("验签成功");
			}else {
				System.out.println("验签失败");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}