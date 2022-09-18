package org.xxpay.task.reconciliation.channel.alipay;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置属性
 */
@Component
@ConfigurationProperties(prefix="config.ali")
public class AlipayProperties {

	private String billPath;

	public String getBillPath() {
		return billPath;
	}

	public void setBillPath(String billPath) {
		this.billPath = billPath;
	}
}
