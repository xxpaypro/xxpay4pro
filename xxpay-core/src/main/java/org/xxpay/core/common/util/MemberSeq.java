package org.xxpay.core.common.util;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 生成全局唯一序列号工具类
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class MemberSeq {

	private static AtomicLong couponNo_seq = new AtomicLong(0L);
	private static String couponNo_seq_prefix = "1";
	private static AtomicLong exchangeNo_seq = new AtomicLong(0L);
	private static String exchangeNo_seq_prefix = "2";

	public static String getCouponNo() {
		return getSeq(couponNo_seq_prefix, couponNo_seq);
	}

	public static String getExchangeNo() {
		return getSeq(exchangeNo_seq_prefix, exchangeNo_seq);
	}

	private static String getSeq(String prefix, AtomicLong seq) {
		return String.format("%s%s%02d", prefix, getSeqString(), (int) seq.getAndIncrement() % 100);
	}

	public static String getSeqString() {
		SimpleDateFormat fm = new SimpleDateFormat("yyMMddhhmmss");
		return fm.format(new Date());
	}

}