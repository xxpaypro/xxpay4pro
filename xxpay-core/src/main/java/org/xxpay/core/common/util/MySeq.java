package org.xxpay.core.common.util;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description: 生成全局唯一序列号工具类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class MySeq {

	private static AtomicLong pay_seq = new AtomicLong(0L);
	private static String pay_seq_prefix = "P";
	private static AtomicLong trans_seq = new AtomicLong(0L);
	private static String trans_seq_prefix = "T";
	private static AtomicLong refund_seq = new AtomicLong(0L);
	private static String refund_seq_prefix = "R";
	private static AtomicLong trade_seq = new AtomicLong(0L);
	private static String trade_seq_prefix = "E";
	private static AtomicLong check_seq = new AtomicLong(0L);
	private static String check_seq_prefix = "C";
	private static AtomicLong agentpay_seq = new AtomicLong(0L);
	private static String agentpay_seq_prefix = "G";
	private static AtomicLong changeaccount_seq = new AtomicLong(0L);
	private static String changeaccount_seq_prefix = "Z";
	private static AtomicLong sett_seq = new AtomicLong(0L);
	private static String sett_seq_prefix = "S";
	private static AtomicLong mch_refund_seq = new AtomicLong(0L);
	private static String mch_refund_seq_prefix = "MR";
	//会员卡号
	private static AtomicLong mbr_cardNo_seq = new AtomicLong(0L);

	private static String node = "00";
	static {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("application.yml");
			Properties properties = new Properties();
			properties.load(url.openStream());
			node = properties.getProperty("node");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getPay() {
		return getSeq(pay_seq_prefix, pay_seq);
	}

	public static String getTrans() {
		return getSeq(trans_seq_prefix, trans_seq);
	}

	public static String getRefund() {
		return getSeq(refund_seq_prefix, refund_seq);
	}

	public static String getMchRefund() {
		return getSeq(mch_refund_seq_prefix, mch_refund_seq);
	}

	public static String getTrade() {
		return getSeq(trade_seq_prefix, trade_seq);
	}

	public static String getAgentpay() {
		return getSeq(agentpay_seq_prefix, agentpay_seq);
	}

	public static String getChangeAccount() {
		return getSeq(changeaccount_seq_prefix, changeaccount_seq);
	}

	public static String getSett() {
		return getSeq(sett_seq_prefix, sett_seq);
	}

	public static String getMbrCardNoSeq() {
		String time = String.valueOf(System.currentTimeMillis());
		return String.format("%s%04d", time.substring(time.length()-10), (int) mbr_cardNo_seq.getAndIncrement() % 10000);
	}

	private static String getSeq(String prefix, AtomicLong seq) {
		prefix += node;
		return String.format("%s%s%04d", prefix, getSeqString(), (int) seq.getAndIncrement() % 10000);
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getCheck() {
		return String.format("%04d%s", (int) check_seq.getAndIncrement() % 10000, getSeqString());
	}

	public static String getSeqString() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		return fm.format(new Date());
	}

	public static void main(String[] args) {
		for (int i = 0; i < 1; i++) {
			System.out.println("pay=" + getSeqString());
			System.out.println("pay=" + getPay());
			System.out.println("trans=" + getTrans());
			System.out.println("refund=" + getRefund());
			System.out.println("uuid=" + getUUID());
		}

	}

}