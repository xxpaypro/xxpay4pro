package org.xxpay.pay.channel.yykpay.util;
 
/**
 * 支付工具类
 */
public class PaymentUtil {

	/**
	 * 对支付请求的数据，加密生成md5-hmac
	 *
	 * @param bizType
	 * @param merchantNo
	 * @param merchantOrderNo
	 * @param requestAmount
	 * @param url
	 * @param cardAmt
	 * @param cardNo
	 * @param cardPwd
	 * @param cardCode
	 * @param productName
	 * @param productType
	 * @param productDesc
	 * @param extInfo
	 * @param keyValue
	 * @return
	 */
	public static String buildPayHmac(String bizType, String merchantNo,
								   String merchantOrderNo, String requestAmount, String url, String cardAmt,
								   String cardNo, String cardPwd, String cardCode,
								   String productName, String productType, String productDesc, String extInfo,
								   String keyValue) {
		StringBuffer sb = new StringBuffer();
		sb.append(bizType);                		// 业务类型
		sb.append(merchantNo);                	// 商户编号
		sb.append(merchantOrderNo);            	// 商户订单号
		sb.append(requestAmount);            	// 订单金额,精确到分
		sb.append(url);                    		// 回调地址
		sb.append(cardAmt);                		// 卡面额组
		sb.append(cardNo);                    	// 卡号组
		sb.append(cardPwd);                    	// 卡密组
		sb.append(cardCode);                	// 支付渠道编码
		sb.append(productName);            		// 产品名称
		sb.append(productType);            		// 产品类型
		sb.append(productDesc);                	// 产品描述
		sb.append(extInfo);                    	// 扩展信息
		return DigestUtil.hmacSign(sb.toString(), keyValue);
	}

	/**
	 * 生成回调参数签名
	 * @param bizType
	 * @param result
	 * @param merchantNo
	 * @param merchantOrderNo
	 * @param successAmount
	 * @param cardCode
	 * @param noticeType
	 * @param extInfo
	 * @param cardNo
	 * @param cardStatus
	 * @param cardReturnInfo
	 * @param cardIsbalance
	 * @param cardBalance
     * @param cardSuccessAmount
     * @param keyValue
     * @return
     */
	public static String buildNotifyHmac(String bizType, String result,
										 String merchantNo, String merchantOrderNo, String successAmount, String cardCode,
										 String noticeType, String extInfo, String cardNo, String cardStatus,
										 String cardReturnInfo, String cardIsbalance, String cardBalance, String cardSuccessAmount, String keyValue) {
		StringBuffer sb = new StringBuffer();
		sb.append(bizType);
		sb.append(result);
		sb.append(merchantNo);
		sb.append(merchantOrderNo);
		sb.append(successAmount);
		sb.append(cardCode);
		sb.append(noticeType);
		sb.append(extInfo);
		sb.append(cardNo);
		sb.append(cardStatus);
		sb.append(cardReturnInfo);
		sb.append(cardIsbalance);
		sb.append(cardBalance);
		sb.append(cardSuccessAmount);
		return DigestUtil.hmacSign(sb.toString(), keyValue);
	}

	/**
	 * 生成订单查询参数签名值
	 * @param merchantNo
	 * @param merchantOrderNo
	 * @param keyValue
     * @return
     */
	public static String buildQueryHmac(String merchantNo, String merchantOrderNo, String keyValue) {
		StringBuffer sb = new StringBuffer();
		sb.append(merchantNo); 			// 商户编号
		sb.append(merchantOrderNo);   	// 商户订单号
		return DigestUtil.hmacSign(sb.toString(), keyValue);
	}


}