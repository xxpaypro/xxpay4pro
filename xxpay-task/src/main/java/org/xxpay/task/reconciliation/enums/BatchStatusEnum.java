package org.xxpay.task.reconciliation.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对账状态枚举
 */
public enum BatchStatusEnum {

	SUCCESS((byte) 1, "对账操作成功"),

	FAIL((byte) 2, "对账操作失败"),

	ERROR((byte) 3, "银行返回错误信息"),

	NOBILL((byte) 4, "银行没有订单信息");

	private Byte code;
	private String desc;

	private BatchStatusEnum(Byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Byte getCode() {
		return code;
	}

	public void setCode(Byte code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public static BatchStatusEnum getEnum(String name) {
		BatchStatusEnum[] arry = BatchStatusEnum.values();
		for (int i = 0; i < arry.length; i++) {
			if (arry[i].name().equals(name)) {
				return arry[i];
			}
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List toList() {
		MistakeHandleStatusEnum[] ary = MistakeHandleStatusEnum.values();
		List list = new ArrayList();
		for (int i = 0; i < ary.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("desc", ary[i].getDesc());
			map.put("name", ary[i].name());
			list.add(map);
		}
		return list;
	}
}
