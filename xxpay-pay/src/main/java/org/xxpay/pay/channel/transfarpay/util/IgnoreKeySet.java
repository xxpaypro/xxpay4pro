/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 描述说明
 * 
 * @version V1.0
 * @author huzz
 * @Date 2017年3月27日 下午2:26:04
 * @since JDK 1.7
 */
public class IgnoreKeySet {

	public static final Set<String> ignoreKeySet = new HashSet();
	static {
		ignoreKeySet.add("dog_ak");
		ignoreKeySet.add("appid");
		ignoreKeySet.add("dog_sk");
		ignoreKeySet.add("tf_sign");
		ignoreKeySet.add("dog_key");
		ignoreKeySet.add("tf_timestamp");
		ignoreKeySet.add("dog_value");
	}
}
