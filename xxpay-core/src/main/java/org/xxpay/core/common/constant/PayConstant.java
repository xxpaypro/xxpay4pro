package org.xxpay.core.common.constant;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 支付常量类
 * @author dingzhiwei jmdhappy@126.com
 * @date 2017-07-05
 * @version V1.0
 * @Copyright: www.xxpay.org
 */
public class PayConstant {

	public final static String PAY_CHANNEL_WX_MICROPAY = "wxpay_micropay"; 			// 微信刷卡支付
	public final static String PAY_CHANNEL_WX_JSAPI = "wxpay_jsapi"; 				// 微信公众号支付
	public final static String PAY_CHANNEL_WX_NATIVE = "wxpay_native";				// 微信原生扫码支付
	public final static String PAY_CHANNEL_WX_APP = "wxpay_app";					// 微信APP支付
	public final static String PAY_CHANNEL_WX_MWEB = "wxpay_mweb";					// 微信H5支付
	public final static String PAY_CHANNEL_WX_BAR = "wxpay_bar";					// 微信条码支付（被扫）
	public final static String PAY_CHANNEL_WX_MINI = "wxpay_mini"; 					// 微信小程序支付
	public final static String PAY_CHANNEL_IAP = "iap";							// 苹果应用内支付
	public final static String PAY_CHANNEL_ALIPAY_MOBILE = "alipay_mobile";		// 支付宝移动支付
	public final static String PAY_CHANNEL_ALIPAY_PC = "alipay_pc";	    		// 支付宝PC支付
	public final static String PAY_CHANNEL_ALIPAY_WAP = "alipay_wap";	    	// 支付宝WAP支付
	public final static String PAY_CHANNEL_ALIPAY_QR = "alipay_qr";	    		// 支付宝当面付之扫码支付
	public final static String PAY_CHANNEL_ALIPAY_BAR = "alipay_bar";	    	// 支付宝当面付之条码支付（被扫）
	public final static String PAY_CHANNEL_ALIPAY_JSAPI = "alipay_jsapi";	    	//支付宝当面付之服务窗支付  ( 开通产品为支付宝当面付， 使用api为[alipay.trade.create], 然后调用支付宝jsapi支付。 一般用于聚合码支付宝钱包内支付）

	public final static String PAY_CHANNEL_UNIONPAY_BAR = "unionpay_bar";	    	// 银联二维码消费（被扫）
	public final static String PAY_CHANNEL_UNIONPAY_JSAPI = "unionpay_jsapi";	    //银联主扫支付

	public final static String PAY_CHANNEL_SUIXINGPAY_WX_BAR = "suixingpay_wx_bar";	    	//【随行付】微信条码支付（被扫）
	public final static String PAY_CHANNEL_SUIXINGPAY_WX_JSAPI = "suixingpay_wx_jsapi";	    //【随行付】微信公众号支付
	public final static String PAY_CHANNEL_SUIXINGPAY_ALIPAY_BAR = "suixingpay_alipay_bar";	    	// 【随行付】支付宝当面付之条码支付（被扫）
	public final static String PAY_CHANNEL_SUIXINGPAY_ALIPAY_JSAPI = "suixingpay_alipay_jsapi";	    //【随行付】支付宝当面付之服务窗支付

	public final static String PAY_CHANNEL_DLBPAY_WX_BAR = "dlbpay_wx_bar";	    	//【哆啦宝】微信条码支付（被扫）
	public final static String PAY_CHANNEL_DLBPAY_WX_JSAPI = "dlbpay_wx_jsapi";	    //【哆啦宝】微信公众号支付
	public final static String PAY_CHANNEL_DLBPAY_ALIPAY_BAR = "dlbpay_alipay_bar";	    	// 【哆啦宝】支付宝当面付之条码支付（被扫）
	public final static String PAY_CHANNEL_DLBPAY_ALIPAY_JSAPI = "dlbpay_alipay_jsapi";	    //【哆啦宝】支付宝当面付之服务窗支付
	public final static String PAY_CHANNEL_DLBPAY_JD_H5 = "dlbpay_jd_h5";	    //【哆啦宝】京东H5支付



	public final static String CHANNEL_NAME_WXPAY = "wxpay"; 				// 渠道名称:微信
	public final static String CHANNEL_NAME_ALIPAY = "alipay"; 				// 渠道名称:支付宝
	public final static String CHANNEL_NAME_JDPAY = "jdpay"; 				// 渠道名称:京东
	public final static String CHANNEL_NAME_KQPAY = "kqpay"; 				// 渠道名称:快钱
	public final static String CHANNEL_NAME_SWIFTPAY = "swiftpay"; 			// 渠道名称:威富通
	public final static String CHANNEL_NAME_GOMEPAY = "gomepay"; 			// 渠道名称:银盈通
	public final static String CHANNEL_NAME_ACCOUNTPAY = "accountpay"; 	    // 渠道名称:账户支付
	public final static String CHANNEL_NAME_SANDPAY = "sandpay"; 	    	// 渠道名称:杉德支付
	public final static String CHANNEL_NAME_SICPAY = "sicpay"; 	    		// 渠道名称:高汇通支付
	public final static String CHANNEL_NAME_MAXPAY = "maxpay"; 	    		// 渠道名称:拉卡拉支付
	public final static String CHANNEL_NAME_SILVERSPAY = "silverspay"; 		// 渠道名称:睿联支付
	public final static String CHANNEL_NAME_TRANSFARPAY = "transfarpay"; 	// 渠道名称:传化支付
	public final static String CHANNEL_NAME_HCPAY = "hcpay"; 				// 渠道名称:汇潮支付
	public final static String CHANNEL_NAME_YYKPAY = "yykpay"; 				// 渠道名称:易游酷支付
	public final static String CHANNEL_NAME_UNIONPAY = "unionpay"; 				// 渠道名称:银联支付
	public final static String CHANNEL_NAME_SUIXINGPAY = "suixingpay"; 		// 渠道名称:随行付
	public final static String CHANNEL_NAME_DLBPAY = "dlbpay"; 		// 渠道名称:哆啦宝


	public final static String PAY_CHANNEL_SWIFTPAY_WXPAY_NATIVE = CHANNEL_NAME_SWIFTPAY + "_wxpay_native";			// 威富通微信扫码
	public final static String PAY_CHANNEL_SWIFTPAY_ALIPAY_NATIVE = CHANNEL_NAME_SWIFTPAY + "_alipay_native";		// 威富通微支付宝扫码
	public final static String PAY_CHANNEL_SWIFTPAY_MICROPAY = CHANNEL_NAME_SWIFTPAY + "_micropay";					// 威富通统一刷卡

	public final static String PAY_CHANNEL_ACCOUNTPAY_BALANCE = CHANNEL_NAME_ACCOUNTPAY + "_balance";	    		// 账户支付余额支付

	public final static String PAY_CHANNEL_SANDPAY_AGENTPAY = CHANNEL_NAME_SANDPAY + "_agentpay";					// 杉德代付
	public final static String PAY_CHANNEL_SICPAY_AGENTPAY = CHANNEL_NAME_SICPAY + "_agentpay";						// 高汇通代付
	public final static String PAY_CHANNEL_MAXPAY_AGENTPAY = CHANNEL_NAME_MAXPAY + "_agentpay";						// 拉卡拉代付
	public final static String PAY_CHANNEL_TRANSFARPAY_AGENTPAY = CHANNEL_NAME_TRANSFARPAY + "_agentpay";			// 传化代付

	public final static String PAY_CHANNEL_SILVERSPAY_GATEWAY = CHANNEL_NAME_SILVERSPAY + "_gateway"; 				// 睿联支付(跳转网关快捷)
	public final static String PAY_CHANNEL_HCPAY_GATEWAY = CHANNEL_NAME_HCPAY + "_gateway"; 						// 汇潮支付(跳转网关快捷)
	public final static String PAY_CHANNEL_YYKPAY_CARD = CHANNEL_NAME_YYKPAY + "_card"; 							// 易游酷充值卡支付


	public final static byte PAY_STATUS_EXPIRED = -2; 	// 订单过期
	public final static byte PAY_STATUS_FAILED = -1; 	// 支付失败
	public final static byte PAY_STATUS_INIT = 0; 		// 初始态
	public final static byte PAY_STATUS_PAYING = 1; 	// 支付中
	public final static byte PAY_STATUS_SUCCESS = 2; 	// 支付成功
	public final static byte PAY_STATUS_REFUND = 4; 	// 已退款
	public final static byte PAY_STATUS_CLOSED = 5; 	// 订单关闭
	public final static byte PAY_STATUS_DEPOSIT_ING = 6; 	// 押金未结算（押金中）
	public final static byte PAY_STATUS_DEPOSIT_REVERSE = 7; 	// 押金退还（撤销订单）

	public final static byte TRANS_STATUS_INIT = 0; 		// 初始态
	public final static byte TRANS_STATUS_TRANING = 1; 		// 转账中
	public final static byte TRANS_STATUS_SUCCESS = 2; 		// 成功
	public final static byte TRANS_STATUS_FAIL = 3; 		// 失败

	public final static byte TRANS_RESULT_INIT = 0; 		// 不确认结果
	public final static byte TRANS_RESULT_REFUNDING = 1; 	// 等待手动处理
	public final static byte TRANS_RESULT_SUCCESS = 2; 		// 确认成功
	public final static byte TRANS_RESULT_FAIL = 3; 		// 确认失败

	public final static byte REFUND_STATUS_INIT = 0; 		// 初始态
	public final static byte REFUND_STATUS_REFUNDING = 1; 	// 转账中
	public final static byte REFUND_STATUS_SUCCESS = 2; 	// 成功
	public final static byte REFUND_STATUS_FAIL = 3; 		// 失败

	public final static byte REFUND_RESULT_INIT = 0; 		// 不确认结果
	public final static byte REFUND_RESULT_REFUNDING = 1; 	// 等待手动处理
	public final static byte REFUND_RESULT_SUCCESS = 2; 	// 确认成功
	public final static byte REFUND_RESULT_FAIL = 3; 		// 确认失败

	public final static byte AGENTPAY_STATUS_INIT = 0; 		// 待处理(初始态)
	public final static byte AGENTPAY_STATUS_ING = 1; 		// 代付中
	public final static byte AGENTPAY_STATUS_SUCCESS = 2; 	// 成功
	public final static byte AGENTPAY_STATUS_FAIL = 3; 		// 失败

	public final static String MCH_NOTIFY_TYPE_PAY = "1";		// 商户通知类型:支付订单
	public final static String MCH_NOTIFY_TYPE_TRANS = "2";		// 商户通知类型:转账订单
	public final static String MCH_NOTIFY_TYPE_REFUND = "3";	// 商户通知类型:退款订单
	public final static String MCH_NOTIFY_TYPE_AGENTPAY = "4";	// 商户通知类型:代付订单

	public final static byte MCH_NOTIFY_STATUS_NOTIFYING = 1;	// 通知中
	public final static byte MCH_NOTIFY_STATUS_SUCCESS = 2;		// 通知成功
	public final static byte MCH_NOTIFY_STATUS_FAIL = 3;		// 通知失败


	public final static String RESP_UTF8 = "UTF-8";			// 通知业务系统使用的编码

	public static final String RETURN_PARAM_RETCODE = "retCode";	// 通讯返回码
	public static final String RETURN_PARAM_RETMSG = "retMsg";
	public static final String RESULT_PARAM_RESCODE = "resCode";
	public static final String RESULT_PARAM_ERRCODE = "errCode";
	public static final String RESULT_PARAM_ERRDES = "errDes";
	public static final String RESULT_PARAM_SIGN = "sign";

	public static final String RETURN_VALUE_SUCCESS = "SUCCESS";
	public static final String RETURN_VALUE_FAIL = "FAIL";
	public static final Integer RESULT_VALUE_SUCCESS = 0;
	public static final Integer RESULT_VALUE_FAIL = -1;

	public static final String RESPONSE_RESULT = "resResult";
	public static final String JUMP_URL = "jumpUrl";

	public static final String RETURN_ALIPAY_VALUE_SUCCESS = "success";
	public static final String RETURN_ALIPAY_VALUE_FAIL = "fail";

	public static final String RETURN_SWIFTPAY_VALUE_SUCCESS = "success";
	public static final String RETURN_SWIFTPAY_VALUE_FAIL = "fail";
	public static final String RETURN_SILVERSPAY_VALUE_SUCCESS = "ok";
	public static final String RETURN_HCPAY_VALUE_SUCCESS = "ok";
	public static final String RETURN_YYKPAY_VALUE_SUCCESS = "SUCCESS";

	public static final String RETURN_UNION_VALUE_SUCCESS = "ok";

	public static class JdConstant {
		public final static String CONFIG_PATH = "jd" + File.separator + "jd";	// 京东支付配置文件路径
	}

	public static class WxConstant {
		public final static String TRADE_TYPE_APP = "APP";									// APP支付
		public final static String TRADE_TYPE_JSPAI = "JSAPI";								// 公众号支付或小程序支付
		public final static String TRADE_TYPE_NATIVE = "NATIVE";							// 原生扫码支付
		public final static String TRADE_TYPE_MWEB = "MWEB";								// H5支付
		public final static String TRADE_TYPE_MINI = "MINI";								// 小程序支付

	}

	public static class IapConstant {
		public final static String CONFIG_PATH = "iap" + File.separator + "iap";		// 苹果应用内支付
	}

	public static class AlipayConstant {
		public final static String CONFIG_PATH = "alipay" + File.separator + "alipay";	// 支付宝移动支付
		public final static String TRADE_STATUS_WAIT = "WAIT_BUYER_PAY";		// 交易创建,等待买家付款
		public final static String TRADE_STATUS_CLOSED = "TRADE_CLOSED";		// 交易关闭
		public final static String TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";		// 交易成功
		public final static String TRADE_STATUS_FINISHED = "TRADE_FINISHED";	// 交易成功且结束
	}

	public static final String NOTIFY_BUSI_PAY = "NOTIFY_VV_PAY_RES";
	public static final String NOTIFY_BUSI_TRANS = "NOTIFY_VV_TRANS_RES";

	public static final String PAY_METHOD_FORM_JUMP = "formJump";	// 表单跳转
	public static final String PAY_METHOD_CODE_IMG = "codeImg";		// 二维码图片
	public static final String PAY_METHOD_WX_APP = "wxApp";		// 微信app支付
	public static final String PAY_METHOD_ALIPAY_APP = "alipayApp";		// 支付宝app支付
	public static final String PAY_METHOD_WX_JSAPI = "wxJSApi";		// 支付宝公众号支付
	public static final String PAY_METHOD_OTHER = "other";		// 其他支付方式，无法统一的支付方式，需要商户自行处理

	public static boolean retIsSuccess(JSONObject retObj) {
		if(retObj == null) return false;
		String value = retObj.getString(PayConstant.RETURN_PARAM_RETCODE);
		if(StringUtils.isBlank(value)) return false;
		return "success".equalsIgnoreCase(value);
	}


	public static final Integer PAY_EXPIRE_TIME = 120 ; //支付订单超时时间单位：分钟


	/** 定义固定支付产品 常量 **/
	public static final int PAY_PRODUCT_WX_JSAPI = 8004;  //微信公众号支付
	public static final int PAY_PRODUCT_ALIPAY_JSAPI = 8008;	//支付宝服务窗支付
	public static final int PAY_PRODUCT_WX_BAR = 8020;  //微信条码支付（被扫）
	public static final int PAY_PRODUCT_ALIPAY_BAR = 8021; //支付宝条码支付（被扫）
	public static final int PAY_PRODUCT_UNIONPAY_BAR = 8022; //云闪付条码支付（被扫）
	public static final int PAY_PRODUCT_WX_MINI_PROGRAM = 8023; //微信小程序支付
	public static final int PAY_PRODUCT_JD_H5 = 8012; //京东H5支付

	/** 支付宝 - oauth2 获取用户UserID信息接口地址 **/
    public static final String ALIPAY_OAUTH2URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
	public static final String ALIPAY_OAUTH2URL_SANDBOX = "https://openauth.alipaydev.com/oauth2/publicAppAuthorize.htm";     //沙箱环境地址

	/** 支付宝 -  支付网关地址 **/
    public static final String ALIPAY_GATEWAY_URL = "https://openapi.alipay.com/gateway.do";
	public static final String ALIPAY_GATEWAY_URL_SANDBOX = "https://openapi.alipaydev.com/gateway.do";

	/**  支付宝 - 商户授权请求地址 **/
	public static final String ALIPAY_MCH_AUTH_URL = "https://openauth.alipay.com/oauth2/appToAppAuth.htm";
	public static final String ALIPAY_MCH_AUTH_URL_SANDBOX = "https://openauth.alipaydev.com/oauth2/appToAppAuth.htm";

	/**  银联 请求地址 **/
	public static final String UNIONPAY_HOST = "https://gateway.95516.com";
	public static final String UNIONPAY_HOST_TEST = "https://gateway.test.95516.com";

}
