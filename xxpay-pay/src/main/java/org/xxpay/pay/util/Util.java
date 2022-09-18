package org.xxpay.pay.util;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.constant.PayConstant;

import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/25
 * @description:
 */
public class Util {

    public static boolean retIsSuccess(JSONObject retObj) {
        if(retObj == null) return false;
        return retObj.getBooleanValue(PayConstant.RETURN_PARAM_RETCODE);
    }

    // 构建威富通数据包
    public static String buildSwiftpayAttach(Map params) {
        JSONObject object = new JSONObject();
        object.put("bank_type", params.get("bank_type"));
        object.put("trade_type", params.get("trade_type"));
        return object.toString();
    }

}
