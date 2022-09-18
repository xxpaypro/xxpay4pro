package org.xxpay.mch.utils.printerUtils.consts;

public class consts {
	/**
	 * 执行网络请求时候出现问题
	 */
	public static final int IO_ERROR 		= -1;
	/**
	 * 操作成功
	 */
	public static final int SUCCESS 		= 0;
	/*
	 * 未知错误
	 */
	public static final int UNKNOW 			= 1;
	/**
	 * 参数错误
	 */
	public static final int PARAM_ERROR 	= 2;
	/**
	 * 错误请求
	 */
	public static final int BADREQUEST 		= 400;
	/**
	 * 未登陆
	 */
	public static final int UNLOGIN 		= 401;
	/**
	 * 用户不存在
	 */
	public static final int NO_USER 		= 1001;
	/**
	 * 用户密码错误
	 */
	public static final int ERR_PASS 		= 1002;
	/**
	 * 用户校验失败
	 */
	public static final int ERR_VERIFY  	= 1003;
	/**
	 * access token过期
	 */
	public static final int TOKEN_EXPIRE 	= 1004;
	/**
	 * 用户没有打印机的权限
	 */
	public static final int NO_PERMISSION	= 1005;
	/**
	 * businessId重复
	 */
	public static final int BUSSID_REPEAT	= 1006;
	/**
	 * 打印任务不存在
	 */
	public static final int NO_JOB			= 1007;
	/**
	 * 用户重复
	 */
	public static final int USER_REPEAT		= 1008;
	/**
	 * 打印机重复
	 */
	public static final int PRINTER_REPEAT	= 1009;
	/**
	 * 打印机已经被绑定
	 */
	public static final int PRINTER_BIND	= 1010;
	/**
	 * 权限错误
	 */
	public static final int ERR_PERMISSION	= 1011;
	/**
	 * 打印机不存在
	 */
	public static final int NO_PRINTER		= 1012;
	/**
	 * 打印机未被绑定
	 */
	public static final int UNBIND			= 1013;
}
