package org.xxpay.pay.channel.transfarpay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能：md5签名 修改日期：2016-9-20 10:19:23 说明： 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 */

public class MD5Util {

	public static final String DEFAULT_CHARSET = "utf-8";

	public static final String TF_TIME_PARAM = "tf_timestamp";

	public static final String TF_SIGN_PARAM = "tf_sign";

	public static final String TF_DOG_SK = "dog_sk";


	/**
	 * 签名字符串，外部传入时间戳，手动传入dog_sk
	 *
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String signWithTime(Map<String, String> params, String charset) {
		// 1. tf_sign不加入签名
		params.remove(TF_SIGN_PARAM);
		// 2. 对请求参数的值进行排序
		String keyString = createLinkString(params);
		// 3. 生成md5签名
		return generateMd5(keyString, charset);
	}

	/**
	 * 生成md5
	 *
	 * @param charset
	 * @return
	 */
	public static String generateMd5(String keyString, String charset) {
		String result = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(keyString.getBytes(charset));
			byte[] temp;
			temp = md5.digest("".getBytes(charset));
			for (int i = 0; i < temp.length; i++) {
				result += Integer.toHexString((0x000000ff & temp[i]) | 0xffffff00).substring(6);
			}
			result = result.toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5签名过程中出现错误" + e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
		return result;
	}

	/**
	 * 除去数组中的空值和签名参数
	 *
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(Map<String, String> sArray) {

		Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0) {
			return result;
		}

		for (String key : sArray.keySet()) {
			String value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	/**
	 * 把数组所有元素排序，并参数值拼接成字符串
	 *
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuffer sb = new StringBuffer();
		for (int i = keys.length - 1; i >= 0; i--) {
			sb.append(params.get(keys[i]));
		}
		return sb.toString();
	}

	/**
	 * 校验签名
	 * 
	 * @param params
	 * @param sign
	 * @param key
	 * @param charset
	 * @return
	 */
	public static boolean verify(Map<String, String> params, String sign, String key, String charset) {
		// 1. tf_sign不加入签名
		params.remove(TF_SIGN_PARAM);
		// 2. 加入sk
		params.put(TF_DOG_SK, key);
		String keyString = createLinkString(params);
		System.out.println("verify | 拼装结果createLinkString:" + keyString);
		// 3. 生成md5
		String md5 = generateMd5(keyString, charset);
		System.out.println("verify | 生成generateMd5:" + md5);
		if (md5.equals(sign)) {
			System.out.println("true");
			return true;
		} else {
			System.out.println("false");
			return false;
		}
	}

	/* 盘东网络验签1 */
	private static void buildParams1() {
		String tf_sign = "E666D1CC722D83D867F0D158342C3E34";
		String dog_sk = "77GQ70964zA4DO8f8Cv8";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1123001");
		params.put("tf_timestamp", "20170623100700");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("terminal", "PC");
		params.put("version", "01");
		params.put("backurl", "http://www.alfapayment.com/pay/bankPay/umpay/serverResult.aspx");
		params.put("fronturl", "http://www.alfapayment.com/pay/bankPay/umpay/result.aspx");
		params.put("cashiermode", "02");
		params.put("subject", "Attach1");
		params.put("defaultbank", "zg");
		params.put("businessnumber", "201706221543023934613332");
		params.put("billamount", "0.01");
		params.put("transactionamount", "0.01");
		params.put("businesstype", "cc");
		params.put("kind", "ccc");
		params.put(TF_DOG_SK, dog_sk);
		params.put("toaccountnumber", "8800003096032");
		//
		String keyString = createLinkString(params);
		System.out.println("1---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("1----请求加签--结果:" + md5);
		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("1校验结果：" + verify);
	}

	/* 光银网络---请求加签--2tEvSwQ84T2C20l5UF7u */
	private static void buildParams2() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1051001");
		params.put("backurl", "http://test.guangyinwangluo.com/swPayInterface/CHpayNotify");
		params.put("billamount", "1");
		params.put("businessnumber", "1554946");
		params.put("businesstype", "手机充值");
		params.put("cashiermode", "02");
		params.put("defaultbank", "中国邮储银行");
		params.put("kind", "增值服务");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("subject", "移动话费1元");
		params.put("terminal", "PC");
		params.put("toaccountnumber", "8800003499129");
		params.put("transactionamount", "1");
		params.put("version", "01");
		params.put(TF_DOG_SK, "2tEvSwQ84T2C20l5UF7u");

		String keyString = createLinkString(params);
		System.out.println("光银网络---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("光银网络---请求加签--md5:" + md5);
	}

	/* 传化商城--生产环境---加签失败 */
	private static void buildParams3() {

		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1044001");
		params.put("tf_timestamp", "20170323150840");
		params.put(TF_DOG_SK, "nB29o097W629187fk213");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("terminal", "IOS");
		params.put("backurl", "https://chsc.d.chuanhua.lgjcdn.net/index.php/inside_payment/gtresponse");
		params.put("fronturl", "https://chsc.d.chuanhua.lgjcdn.net/index.php/inside_payment/new_paysuccess");
		params.put("subject", "自有-0.02");
		params.put("description", "23021");
		params.put("businessnumber", "C323529166260758");
		params.put("billamount", "6.02");
		params.put("transactionamount", "6.02");
		params.put("businesstype", "商城购物");
		params.put("kind", "传化商城");
		params.put("topartyid", "3336167");
		params.put("toaccountnumber", "8800003347221");
		//
		String keyString = createLinkString(params);
		System.out.println("传化商城---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("传化商城----请求加签--md5:" + md5);
		String sign = "61C5AA783A01F76F8C0B6B24E2DC87F8";
		String key = "nB29o097W629187fk213";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("校验结果：" + verify);
	}

	/* 传化商城--生产环境---加签失败2 */
	private static void buildParams4() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1044001");
		params.put("tf_timestamp", "20170324131910");
		params.put(TF_DOG_SK, "nB29o097W629187fk213");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("terminal", "IOS");
		// params.put("tf_sign", "B8C6342E0B8075BD6321B159171C6154");
		params.put("backurl", "https://chsc.d.chuanhua.lgjcdn.net/index.php/inside_payment/gtresponse");
		params.put("fronturl", "https://chsc.d.chuanhua.lgjcdn.net/index.php/inside_payment/new_paysuccess");
		params.put("subject", "平台-1");
		params.put("description", "22900");
		params.put("businessnumber", "C324327492409715");
		params.put("billamount", "1.21");
		params.put("transactionamount", "1.21");
		params.put("businesstype", "商城购物");
		params.put("cashiermode", "02");
		params.put("kind", "传化商城");
		params.put("topartyid", "3336167");
		params.put("toaccountnumber", "8800003347221");
		//
		String keyString = createLinkString(params);
		System.out.println("4传化商城---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("4传化商城----请求加签--结果:" + md5);
		String sign = "0BBBCFF1AB6F812092B4CE2768186868";
		String key = "nB29o097W629187fk213";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("4校验结果：" + verify);
	}

	/* 御付代付--验签失败：这个是错误的验签 */
	private static void buildParams5() {
		Map<String, String> params = new HashMap<String, String>();
		//
		params.put(TF_DOG_SK, "a1tJlf7WGjo568Q77h46");
		params.put("fromaccountnumber", "8801000078413");
		params.put("status", "处理中");
		params.put("frompartyid", "567944183");
		params.put("subject", "电子产品");
		params.put("inputdate", "2017-04-01 10:13:03");
		params.put("transactiontype", "代付");
		params.put("appid", "1059001");
		params.put("transactionamount", "1.00");
		params.put("businessnumber", "201704011000180962");
		params.put("transactiondate", "2017-04-01 10:13:03");
		params.put("businessrecordnumber", "2917040110131320001");
		params.put("backurl", "http://hometest.tunnel.qydev.com/service-api/api/chgpaynotify");
		//
		String keyString = createLinkString(params);
		System.out.println("5御付代付---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("5御付代付----请求加签--结果:" + md5);
		String sign = "066EC22FF828F93F427C10559DF60570";
		String key = "a1tJlf7WGjo568Q77h46";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("5御付代付|校验结果：" + verify);
	}

	/* 传化支付银行批量转账-代付业务对接 */
	private static void buildParams6() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "8801000079032");
		params.put("subject", "名称");
		params.put("appid", "1013001");
		params.put("transactionamount", "0.02");
		params.put("businessnumber", "359107815");
		params.put("bankcardtype", "个人");
		params.put("bankcardname", "张三");
		params.put("version", "01");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("bankcardnumber", "6228480402564890018");
		params.put("terminal", "PC");
		params.put("description", "商品描述");
		params.put("backurl", "http://203.171.225.3:2014/PAYTOBANK/Pages/BackNoticeHandler.aspx");
		params.put("tf_timestamp", "20170421141230");
		params.put(TF_DOG_SK, "34z031Im3591p5FYIK9A");
		//
		String keyString = createLinkString(params);
		System.out.println("6传化支付银行批量转账---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("6传化支付银行批量转账----请求加签--结果:" + md5);
		String sign = "5881C0F10C8D888287A746E1D03F2EB1";
		String key = "34z031Im3591p5FYIK9A";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("6校验结果：" + verify);
	}

	/* 传化支付银行批量转账-代付业务对接2 */
	private static void buildParams7() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "8801000079032");
		params.put("subject", "名称");
		params.put("appid", "1013001");
		params.put("transactionamount", "0.02");
		params.put("businessnumber", "359107815");
		params.put("bankcardtype", "个人");
		params.put("bankcardname", "张三");
		params.put("version", "01");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("bankcardnumber", "6228480402564890018");
		params.put("terminal", "PC");
		params.put("description", "商品描述");
		params.put("backurl", "http://203.171.225.3:2014/PAYTOBANK/Pages/BackNoticeHandler.aspx");
		params.put("tf_timestamp", "20170421174138");
		params.put(TF_DOG_SK, "34z031Im3591p5FYIK9A");
		//
		String keyString = createLinkString(params);
		System.out.println("7传化支付银行批量转账---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("7传化支付银行批量转账----请求加签--结果:" + md5);
		String sign = "77E356C0C43D07023F8ED01F7EDD5943";
		String key = "34z031Im3591p5FYIK9A";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("7校验结果：" + verify);
	}

	/* 光银代付----appid==中付支付 */
	private static void buildParams8() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "8801000078413");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("bankcardnumber", "6214920600959272");
		params.put("terminal", "PC");
		params.put("subject", "0424测试");
		params.put("tf_timestamp", "20170425132046");
		params.put("transactionamount", "0.10");
		params.put("appid", "1054001");
		params.put("businessnumber", "310107199602242500");
		params.put("bankcardtype", "个人");
		params.put("bankcardname", "陈伯舜");
		params.put(TF_DOG_SK, "j4D9rNT57bzBpLl5h6Qh");
		//
		String keyString = createLinkString(params);
		System.out.println("8光银代付---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("8光银代付----请求加签--结果:" + md5);
		String sign = "";
		String key = "j4D9rNT57bzBpLl5h6Qh";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("8校验结果：" + verify);
	}

	// 内部钱包调用
	private static void buildParams9() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("service_id", "tf56pay.cashier.creditPayBack");
		params.put("subject", "信用卡还款");
		params.put("businesstype", "信用卡还款");
		params.put("transactiontype", "转账");
		params.put("appid", "1004001");
		params.put("transactionamount", "1");
		params.put("businessnumber", "1117042816574722957");
		params.put("bankcardtype", "个人");
		params.put("bankcardname", "qianduan_qiye");
		params.put("version", "01");
		params.put("billamount", "1");
		params.put("bankcardnumber", "6227612145830440");
		params.put("terminal", "PC");
		params.put("tf_timestamp", "20170428165737");
		params.put(TF_DOG_SK, "k9vcp2ht9k4L5rrA84X0");
		//
		String keyString = createLinkString(params);
		System.out.println("9---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("9----请求加签--结果:" + md5);
		String sign = "04D93B59F00948FC5A432042EEC81CF0";
		String key = "k9vcp2ht9k4L5rrA84X0";
	}

	/* 诚通物流1 */
	private static void buildParams10() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1068001");
		params.put("tf_timestamp", "20170509144658");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("terminal", "PC");
		params.put("version", "01");
		params.put("backurl", "http://203.171.225.3:2014/PAYTOBANK/Pages/BackNoticeHandler.aspx");
		params.put("subject", "洁具+盆(2纸-2)");
		params.put("businessnumber", "106800104170509144658255300494740");
		params.put("transactionamount", "299");
		params.put("fromaccountnumber", "8800003749515");
		params.put("bankcardnumber", "6227002553360403942");
		params.put("bankcardname", "刘彦锋");
		params.put("bankcardtype", "个人");
		params.put("bankname", "建设银行");
		params.put("province", "北京市");
		params.put("city", "北京市");
		params.put("branchbankname", "建设银行");
		params.put("tf_sign", "7B2C99B832AF7F898ECECDDD51E5B1B6");
		params.put(TF_DOG_SK, "0849r94P171sJJ915189");
		//
		String keyString = createLinkString(params);
		System.out.println("10---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("10----请求加签--结果:" + md5);
		String sign = "7B2C99B832AF7F898ECECDDD51E5B1B6";
		String key = "0849r94P171sJJ915189";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("10校验结果：" + verify);
	}

	/* 诚通物流2 */
	private static void buildParams11() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1068001");
		params.put("tf_timestamp", "20170510110656");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("terminal", "PC");
		params.put("version", "01");
		params.put("backurl", "http://203.171.225.3:2014/PAYTOBANK/Pages/BackNoticeHandler.aspx");
		params.put("subject", "洁具+盆(6纸-6)");
		params.put("description", "00497913");
		params.put("businessnumber", "106800104170510110656501100497913");
		params.put("transactionamount", "1514");
		params.put("fromaccountnumber", "8800003749515");
		params.put("bankcardnumber", "622908466203680019");
		params.put("bankcardname", "岳欢欢");
		params.put("bankcardtype", "个人");
		params.put("bankname", "兴业银行");
		params.put("province", "北京市");
		params.put("city", "北京市");
		params.put("branchbankname", "兴业银行");
		params.put("tf_sign", "6BB653CDDFBD904459D07C26D6E9DC77");
		params.put(TF_DOG_SK, "0849r94P171sJJ915189");
		//
		String keyString = createLinkString(params);
		System.out.println("11---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("11----请求加签--结果:" + md5);
		String sign = "6BB653CDDFBD904459D07C26D6E9DC77";
		String key = "0849r94P171sJJ915189";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("11校验结果：" + verify);
	}

	/* 传化狻子松网关代付扫码1 */
	private static void buildParams12() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1000149");
		params.put("clientip", "114");
		params.put("tf_timestamp", "20170510110656");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("terminal", "PC");
		params.put("version", "01");
		params.put("backurl", "http://203.171.225.3:2014/PAYTOBANK/Pages/BackNoticeHandler.aspx");
		params.put("subject", "晨光笔记本");
		params.put("description", "00497913");
		params.put("businessnumber", "2017052314371408169");
		params.put("transactionamount", "10");
		params.put("fromaccountnumber", "8800003749515");
		params.put("bankcardnumber", "622908466203680019");
		params.put("bankcardname", "岳欢欢");
		params.put("bankcardtype", "个人");
		params.put("bankname", "兴业银行");
		params.put("province", "北京市");
		params.put("city", "北京市");
		params.put("branchbankname", "兴业银行");
		params.put("businesstype", "停车费");
		params.put("kind", "电子小票");
		params.put("tf_sign", "6BB653CDDFBD904459D07C26D6E9DC77");
		params.put(TF_DOG_SK, "734d60b7SE2X9d684h98");
	}

	/**/
	private static void buildParams13() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1054001");
		params.put("tf_timestamp", "20170527100228");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("terminal", "H5");
		params.put("version", "01");
		params.put("backurl", "http://paygw.sanwing.com/swPayInterface/CHpayNotify");
		params.put("subject", "余额充值");
		params.put("businessnumber", "15776529");
		params.put("transactionamount", "100");
		params.put("fronturl", "http://ldtmobile.ledui365.com/page/html/yeczjl.html");
		params.put("businesstype", "外部消费");
		params.put("kind", "中付代付");
		params.put("billamount", "100");
		params.put("cashiermode", "02");
		params.put("latitude", "22.98585673731344");
		params.put("defaultbank", "中国工商银行");
		params.put("toaccountnumber", "8800003499129");
		params.put("longitude", "113.70028854944896");
		params.put(TF_DOG_SK, "T2RQmBLd38i7VnX1Z3L0");
		//
		String keyString = createLinkString(params);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		/*
		 * 961E763D4A3E6EF70D46E91985280532 3344215A9EEE158DE248F3FEA85E7F92
		 */
		String sign = "961E763D4A3E6EF70D46E91985280532";
		String key = "T2RQmBLd38i7VnX1Z3L0";
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("13校验结果：" + verify);
	}

	/**/
	private static void buildParams14() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1000153");
		params.put("tf_timestamp", "20170613113049");
		params.put("bankcardtype", "个人");
		params.put("subject", "笔记本");
		params.put("fromaccountnumber", "8802000084479");
		params.put("bankcardname", "王晓东");
		params.put("terminal", "PC");
		params.put("transactionamount", "0.01");
		params.put("bankcardnumber", "6217000010084514952");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("businessnumber", "1497324165978");
		params.put(TF_SIGN_PARAM, "61F3EAD2CD2A20EEFD404FBB0A9BFE6C");
		String key = "YhCQ6nQ1Y7z7oMZ9FB72";
		params.put(TF_DOG_SK, key);
		//
		String sign = "61F3EAD2CD2A20EEFD404FBB0A9BFE6C";
		String keyString = createLinkString(params);
		System.out.println("14----请求加签--拼接字符串:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("14----请求加签--结果:" + md5);
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("14校验结果：" + verify);

	}

	/*
	 * {tf_timestamp=20170613142138, bankcardtype=个人, subject=笔记本, fromaccountnumber=8802000084479, bankcardname=王晓东,
	 * terminal=PC, transactionamount=0.01, bankcardnumber=6217000010084514952, appid=1000153,
	 * service_id=tf56pay.cashier.payToBank, businessnumber=1497334888595, tf_sign=3DEA7CB3BC4364553710517DC23B9448}
	 */
	private static void buildParams15() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1000153");
		params.put("tf_timestamp", "20170613142138");
		params.put("bankcardtype", "个人");
		params.put("subject", "笔记本");
		params.put("fromaccountnumber", "8802000084479");
		params.put("bankcardname", "王晓东");
		params.put("terminal", "PC");
		params.put("transactionamount", "0.01");
		params.put("bankcardnumber", "6217000010084514952");
		params.put("service_id", "tf56pay.cashier.payToBank");
		params.put("businessnumber", "1497334888595");
		// params.put(TF_SIGN_PARAM, "61F3EAD2CD2A20EEFD404FBB0A9BFE6C");
		String key = "YhCQ6nQ1Y7z7oMZ9FB72";
		params.put(TF_DOG_SK, key);
		//
		String sign = "3DEA7CB3BC4364553710517DC23B9448";
		String keyString = createLinkString(params);
		System.out.println("15----请求加签--拼接字符串:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("15----请求加签--结果:" + md5);
		boolean verify = verify(params, sign, key, DEFAULT_CHARSET);
		System.out.println("15校验结果：" + verify);

	}

	/*
	 * str:appid---->1110001 str:backurl---->http://pay.s6s66.com/chpay/OrderReturn.php str:billamount---->100.00
	 * str:businessnumber---->ch20170620095450155897 str:businesstype---->1 str:cashiermode---->01
	 * str:description---->消费 str:fronturl---->http://pay.s6s66.com/chpay/OrderReturn.php str:kind---->1
	 * str:service_id---->tf56pay.cashier.pay str:subject---->消费 str:terminal---->PC str:tf_timestamp---->20170620095450
	 * str:toaccountnumber---->8800005196439 str:transactionamount---->100.00
	 */
	private static void buildParams16() {
		String tf_sign = "B2E19B35708DDB2780E8B30A7B9C2544";
		String dog_sk = "w799C0q564qv92owD0W8";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1110001");
		params.put("backurl", "http://pay.s6s66.com/chpay/OrderReturn.php");
		params.put("billamount", "100.00");
		params.put("businessnumber", "ch20170620095450155897");
		params.put("businesstype", "1");
		params.put("cashiermode", "01");
		params.put("description", "消费");
		params.put("fronturl", "http://pay.s6s66.com/chpay/OrderReturn.php");
		params.put("kind", "1");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("subject", "消费");
		params.put("tf_timestamp", "20170620095450");
		params.put("toaccountnumber", "8800005196439");
		params.put("transactionamount", "100.00");
		params.put(TF_DOG_SK, dog_sk);
		//
		String keyString = createLinkString(params);
		System.out.println("16---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("16----请求加签--结果:" + md5);

		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("16校验结果：" + verify);
	}

	/*
	 * http://openapitest.tf56.com/service/api?tf_timestamp=20170620144349&backurl=http://web-billSiteView-vip/
	 * billSiteView/pay/skipAllowanceRechargec/updateAllowanceRechargeOrder&subject=餐补充值&kind=餐补&topartyid=565656700&
	 * defaultbank=&app_stoken=d63eb547e4558eb23ad15afee10f3d49_567961247&description=餐补充值&terminal=Android&
	 * transactionamount=100.00&version=01&businesstype=餐补充值&toaccountnumber=8801000012480&billamount=100.00&appid=
	 * 1004001&service_id=tf56pay.cashier.pay&businessnumber=1017062014440810002&cashiermode=02&tf_sign=
	 * 8825936FAFAEE90DD9F321DDD966C677
	 */
	private static void buildParams17() {
		String tf_sign = "790CEAE0A11B8D736676304DE133DFAE";
		String dog_sk = "k9vcp2ht9k4L5rrA84X0";
		Map<String, String> params = new HashMap<String, String>();
		params.put("tf_timestamp", "20170620151304");
		params.put("backurl", "http://web-billSiteView-vip/billSiteView/pay/skipAllowanceRechargec/updateAllowanceRechargeOrder");
		params.put("subject", "餐补充值");
		params.put("kind", "餐补");
		params.put("topartyid", "565656700");
		params.put("app_stoken", "d63eb547e4558eb23ad15afee10f3d49_567961247");
		params.put("description", "餐补充值");
		params.put("terminal", "Android");
		params.put("transactionamount", "100.00");
		params.put("version", "01");
		params.put("businesstype", "餐补充值");
		params.put("toaccountnumber", "8801000012480");
		params.put("billamount", "100.00");
		params.put("appid", "1004001");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("businessnumber", "1017062015131920002");
		params.put("cashiermode", "02");
		params.put("defaultbank", null);
		params.put(TF_DOG_SK, dog_sk);
		//
		String keyString = createLinkString(params);
		System.out.println("17---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("17----请求加签--结果:" + md5);
		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("17校验结果：" + verify);
	}

	
	
	/**/
	private static void buildParams18() {
		String dog_sk="b2bK59C18392Y583k98v";
		String tf_sign="9BC28A315B09B59FD3A0673694C60A9D";
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "990305001001");
		params.put("remark", "授予,frompartyid:null,fromaccountnumber:990305001001");
		params.put("status", "成功");
		params.put("inputdate", "2017-07-17 15:39:33");
		params.put("subject", "支付");
		params.put("transactiontype", "消费");
		params.put("appid", "1174001");
		params.put("transactionamount", "2.50");
		params.put("businessnumber", "biz15002771537650001");
		params.put("topartyid", "9610641");
		params.put("transactiondate", "2017-07-17 15:40:44");
		params.put("billamount", "2.50");
		params.put("terminal", "PC");
		params.put("toaccountnumber", "8800009628323");
		params.put("businessrecordnumber", "1417071715393320004");
		params.put("backurl", "http://123.206.220.140:20000/gateway/ntchgw.htm");
		params.put(TF_DOG_SK, "b2bK59C18392Y583k98v");
		//
		String keyString = createLinkString(params);
		System.out.println("18---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("18----请求加签--结果:" + md5);
		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("18校验结果：" + verify);
		
	}

	
	/**/
	private static void buildParams19() {
		String dog_sk="b2bK59C18392Y583k98v";
		String tf_sign="9BC28A315B09B59FD3A0673694C60A9D";
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "990305001001");
		params.put("remark", "授予,frompartyid:null,fromaccountnumber:990305001001");
		params.put("status", "成功");
		params.put("inputdate", "2017-07-17 15:39:33");
		params.put("subject", "支付");
		params.put("businesstype", "停车费");
		params.put("transactiontype", "消费");
		params.put("appid", "1174001");
		params.put("transactionamount", "2.50");
		params.put("businessnumber", "biz15002771537650001");
		params.put("topartyid", "9610641");
		params.put("transactiondate", "2017-07-17 15:40:44");
		params.put("billamount", "2.50");
		params.put("terminal", "PC");
		params.put("toaccountnumber", "8800009628323");
		params.put("transactionnumber", "1617071715404420001");
		params.put("businessrecordnumber", "1417071715393320004");
		params.put("updatedate", "2017-07-17 15:40:44");
		params.put("backurl", "http://123.206.220.140:20000/gateway/ntchgw.htm");
		params.put(TF_DOG_SK, dog_sk);
		//
		String keyString = createLinkString(params);
		System.out.println("19---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("19----请求加签--结果:" + md5);
		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("19校验结果：" + verify);
		
	}
	
	
	/*扫码对接*/
	private static void buildParams20() {
		String dog_sk="k9vcp2ht9k4L5rrA84X0";
		String tf_sign="3CF098DC6B27CA07724944924B205523";
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", "1004001");
		params.put("tf_timestamp", "20170720173051");
		params.put("service_id", "tf56pay.cashier.pay");
		params.put("sign_type", "md5");
		params.put("terminal", "Android");
		params.put("fronturl", "https://cspay.zongs365.com/Home/PaySuccess");
		params.put("backurl", "https://cspay.zongs365.com/ChPay/PaySuccessDo");
		params.put("subject", "娃哈哈冰红茶");
		params.put("businessnumber", "900200010020170720172710");
		params.put("billamount", "0.5");
		params.put("transactionamount", "0.5");
		params.put("businesstype", "商品订单交易");
		params.put("kind", "二维码支付");
		params.put("toaccountnumber", "8801000078413");
//		params.put("tf_sign", tf_sign);
		params.put(TF_DOG_SK, dog_sk);
		//
		String keyString = createLinkString(params);
		System.out.println("20---请求加签--拼装:" + keyString);
		String md5 = generateMd5(keyString, DEFAULT_CHARSET);
		System.out.println("20----请求加签--结果:" + md5);
		boolean verify = verify(params, tf_sign, dog_sk, DEFAULT_CHARSET);
		System.out.println("20校验结果：" + verify);
	}
	
	/**/
	private static void buildParams21() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fromaccountnumber", "990305001001");
		params.put("remark", "授予,frompartyid:null,fromaccountnumber:990305001001");
		params.put("status", "成功");
		params.put("inputdate", "2017-07-17 15:39:33");
		params.put("subject", "支付");
		params.put("transactiontype", "消费");
		params.put("appid", "1174001");
		params.put("transactionamount", "2.50");
		params.put("businessnumber", "biz15002771537650001");
		params.put("topartyid", "9610641");
		params.put("clientip", "");
		params.put("tf_timestamp", "");
		params.put("service_id", "");
		params.put("terminal", "");
		params.put("version", "");
		params.put("backurl", "");
		params.put("description", "");
		params.put("bankcardnumber", "");
		params.put("bankcardname", "");
		params.put("bankcardtype", "");
		params.put("bankname", "");
		params.put("province", "");
		params.put("city", "");
		params.put("branchbankname", "");
		params.put("businesstype", "");
		params.put("kind", "");
		params.put("tf_sign", "");
		params.put(TF_DOG_SK, "");
	}
	
	
	public static void main(String[] args) {
		// String time = DateUtils.getDateStr(new Date(), DateUtils.FORMAT_YYYYMMDDHHMMSS);
		buildParams20();
	}

}