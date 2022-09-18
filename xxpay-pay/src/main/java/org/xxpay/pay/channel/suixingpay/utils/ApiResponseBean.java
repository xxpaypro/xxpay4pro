package org.xxpay.pay.channel.suixingpay.utils;

import java.io.Serializable;

public class ApiResponseBean<T>  implements Serializable {

	private static final long serialVersionUID = -6027021300935759741L;

	/**
	 * 系统响应码
	 */
	private String code;
	/**
	 * 系统消息
	 */
	private String msg;

	/**
	 * 请求时id
	 */
	private String reqId;

	/**
	 * 请求时orgId
	 */
	private String orgId;

	private String signType;


	/**
	 * 返回消息
	 */
	private T respData;

	/**
	 * 签名
	 */
	private String sign;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public T getRespData() {
		return respData;
	}

	public void setRespData(T respData) {
		this.respData = respData;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
