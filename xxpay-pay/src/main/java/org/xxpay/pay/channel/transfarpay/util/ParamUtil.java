/**
 * Copyright © 2014-2017 TransfarPay.All Rights Reserved.
 */
package org.xxpay.pay.channel.transfarpay.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Http参数工具类
 * 
 * @version V1.0
 * @author huzz
 * @Date 2017年3月27日 下午9:15:31
 * @since JDK 1.7
 */
public class ParamUtil {

	/**
	 * 生成时间戳工具类
	 *
	 * @param par
	 * @return
	 */
	public static String strDate(String par) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(par);

		return dateFormat.format(new Date());
	}

	/**
	 * 参数按ansc码升序排列后做MD5转换
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String map2MD5(Map<String, Object> map) throws Exception {
		List ignoreList = null;
		if (map.get("tf_ignore") != null) {
			ignoreList = new ArrayList();
			String[] array = map.get("tf_ignore").toString().split(",");
			for (int i = 0; i < array.length; ++i) {
				ignoreList.add(array[i]);
			}
		}
		Object[] key = map.keySet().toArray();
		Arrays.sort(key);

		StringBuffer sb = new StringBuffer();
		for (int i = key.length - 1; i >= 0; --i) {
			if ("tf_ignore".equalsIgnoreCase(key[i].toString()))
				continue;
			if ((ignoreList != null) && (ignoreList.contains(key[i])) && (!(IgnoreKeySet.ignoreKeySet.contains(key[i])))) {
				continue;
			}

			sb.append(map.get(key[i]));
		}
		String keyString = sb.toString();
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(keyString.getBytes("utf-8"));

		String result = "";

		byte[] temp = md5.digest("".getBytes("utf-8"));
		for (int i = 0; i < temp.length; ++i) {
			result = result + Integer.toHexString(0xFF & temp[i] | 0xFFFFFF00).substring(6);
		}
		return result.toUpperCase();
	}

	/**
	 * 参数按ansc码降序排列后key1=value1&key2=value2
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String map2MapAndValueBydesc(Map<String, Object> map) throws Exception {
		List ignoreList = null;
		if (map.get("tf_ignore") != null) {
			ignoreList = new ArrayList();
			String[] array = map.get("tf_ignore").toString().split(",");
			for (int i = 0; i < array.length; ++i) {
				ignoreList.add(array[i]);
			}
		}
		Object[] key = map.keySet().toArray();
		Arrays.sort(key);

		StringBuffer sb = new StringBuffer();
		for (int i = key.length - 1; i >= 0; --i) {
			if ("tf_ignore".equalsIgnoreCase(key[i].toString()))
				continue;
			if ((ignoreList != null) && (ignoreList.contains(key[i])) && (!(IgnoreKeySet.ignoreKeySet.contains(key[i])))) {
				continue;
			}
			sb.append(key[i]).append("=");
			sb.append(map.get(key[i]));
			sb.append("&");
		}
		String keyString = sb.toString();
		return keyString;
	}

	public static String sortMapByKey(Map<String, Object> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		Map<String, Object> sortedMap = new TreeMap<String, Object>(new Comparator<String>() {

			public int compare(String obj1, String obj2) {
				// 降序排序
				return obj2.compareTo(obj1);
			}
		});
		sortedMap.putAll(map);
		StringBuffer result = new StringBuffer();
		for (Entry<String, Object> entry : sortedMap.entrySet()) {
			result.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		return result.toString();
	}

}
