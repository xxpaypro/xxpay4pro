package org.xxpay.pay.channel.suixingpay.utils;

import java.io.Serializable;


public class ApiRequestBean<T>  implements Serializable{

	private static final long serialVersionUID = -9073910802005694017L;

	/**
	 * 合作机构id
	 */
	private String orgId;

	/**
	 * 请求id
	 */
	private String reqId;

	/**
	 * 请求接口版本1.0
	 */
	private String version;

	/**
	 * 代理商身份标识
	 */
	private String agentId;

	/**
	 * 业务员编号
	 */
	private String salesCode;

	/**
	 * 业务数据
	 */
	private T reqData;

	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 签名类型
	 */
	private String signType;

	/**
	 * 请求时间
	 */
	private String timestamp;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getSalesCode() {
		return salesCode;
	}

	public void setSalesCode(String salesCode) {
		this.salesCode = salesCode;
	}

	public T getReqData() {
		return reqData;
	}

	public void setReqData(T reqData) {
		this.reqData = reqData;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
