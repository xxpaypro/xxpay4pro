package org.xxpay.pay.channel.unionpay.utils;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.util.MyLog;

import java.util.HashMap;
import java.util.Map;

/**
 * 银联接口 返回结果工具 参考sdk
 * create date: 20191205
 * Author: terrfly
 */
public class UniResultUtils {


    private static final MyLog logger = MyLog.getLog(UniResultUtils.class);

    /** 将形如key=value&key=value的字符串转换为相应的JSONObject对象 */
    public static JSONObject convertResultStringToJSON(String result) {
        Map<String, String> map = null;

        if (result != null && !"".equals(result.trim())) {
            if (result.startsWith("{") && result.endsWith("}")) {
                result = result.substring(1, result.length() - 1);
            }
            map = parseQString(result);
        }

        return (JSONObject)JSONObject.toJSON(map);
    }


    /** 解析应答字符串，生成应答要素  参考sdk */
    private static Map<String, String> parseQString(String str) {

        Map<String, String> map = new HashMap<String, String>();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if(len>0){
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else  {// 如果当前生成的是value
                    if(isOpen){
                        if(curChar == openName){
                            isOpen = false;
                        }

                    }else{//如果没开启嵌套
                        if(curChar == '{'){//如果碰到，就开启嵌套
                            isOpen = true;
                            openName ='}';
                        }
                        if(curChar == '['){
                            isOpen = true;
                            openName =']';
                        }
                    }

                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    /** putKeyValueToMap 参考sdk **/
    private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key, Map<String, String> map) {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new RuntimeException("QString format illegal");
            }
            map.put(key, temp.toString());
        }
    }

}