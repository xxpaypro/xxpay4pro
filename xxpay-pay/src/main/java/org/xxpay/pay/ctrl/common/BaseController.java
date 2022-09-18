package org.xxpay.pay.ctrl.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.xxpay.core.common.constant.RetEnum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@Controller
public class BaseController {

    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;
    protected static final String PAGE_COMMON_ERROR = "common/error";
    protected static final String PAGE_COMMON_PC_ERROR = "cashier/pc_error";

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    @Value("${config.payUrl}")
    protected String payUrl;

    protected int getPageIndex(JSONObject object) {
        if(object == null) return DEFAULT_PAGE_INDEX;
        Integer pageIndex = object.getInteger("page");
        if(pageIndex == null) return DEFAULT_PAGE_INDEX;
        return pageIndex;
    }

    protected int getPageSize(JSONObject object) {
        if(object == null) return DEFAULT_PAGE_SIZE;
        Integer pageSize = object.getInteger("limit");
        if(pageSize == null) return DEFAULT_PAGE_SIZE;
        return pageSize;
    }

    protected int getPageIndex(Integer page) {
        if(page == null) return DEFAULT_PAGE_INDEX;
        return page;
    }

    protected int getPageSize(Integer limit) {
        if(limit == null) return DEFAULT_PAGE_SIZE;
        return limit;
    }

    protected JSONObject getJsonParam(){
        return getJsonParam(request);
    }

    protected JSONObject getJsonParam(HttpServletRequest request) {
        String params = request.getParameter("params");
        if(StringUtils.isNotBlank(params)) {
            return JSON.parseObject(params);
        }
        // 参数Map
        Map properties = request.getParameterMap();
        // 返回值Map
        JSONObject returnObject = new JSONObject();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name;
        String value = "";
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if(null == valueObj){
                value = "";
            }else if(valueObj instanceof String[]){
                String[] values = (String[])valueObj;
                for(int i=0;i<values.length;i++){
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length()-1);
            }else{
                value = valueObj.toString();
            }
            returnObject.put(name, value);
        }
        return returnObject;
    }

    protected Boolean getBoolean(JSONObject param, String key) {
        if(param == null) return null;
        return param.getBooleanValue(key);
    }

    protected String getString(JSONObject param, String key) {
        if(param == null) return null;
        return param.getString(key);
    }

    protected String getStringRequired(JSONObject param, String key) {
        if(param == null ) {
            throw new RuntimeException(getErrMsg(key));
        }
        String value = param.getString(key);
        if(StringUtils.isBlank(value)) {
            throw new RuntimeException(getErrMsg(key));
        }
        return value;
    }

    protected String getStringDefault(JSONObject param, String key, String defaultValue) {
        String value = getString(param, key);
        if(value == null) return defaultValue;
        return value;
    }

    protected Byte getByte(JSONObject param, String key) {
        if(param == null) return null;
        return param.getByte(key);
    }

    protected Byte getByteRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Byte value = param.getByte(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected int getByteDefault(JSONObject param, String key, byte defaultValue) {
        Byte value = getByte(param, key);
        if(value == null) return defaultValue;
        return value.byteValue();
    }

    protected Integer getInteger(JSONObject param, String key) {
        if(param == null) return null;
        return param.getInteger(key);
    }

    protected Integer getIntegerRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Integer value = param.getInteger(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected int getIntegerDefault(JSONObject param, String key, int defaultValue) {
        Integer value = getInteger(param, key);
        if(value == null) return defaultValue;
        return value.intValue();
    }

    protected Long getLong(JSONObject param, String key) {
        if(param == null) return null;
        return param.getLong(key);
    }

    protected Long getLongRequired(JSONObject param, String key) {
        if(param == null ) throw new RuntimeException(getErrMsg(key));
        Long value = param.getLong(key);
        if(value == null) throw new RuntimeException(getErrMsg(key));
        return value;
    }

    protected long getLongDefault(JSONObject param, String key, long defaultValue) {
        Long value = getLong(param, key);
        if(value == null) return defaultValue;
        return value.longValue();
    }

    protected JSONObject getJSONObject(JSONObject param, String key) {
        if(param == null) return null;
        return param.getJSONObject(key);
    }

    protected <T> T getObject(JSONObject param, String key, Class<T> clazz) {
        JSONObject object = getJSONObject(param, key);
        if(object == null) return null;
        return JSON.toJavaObject(object, clazz);
    }

    protected <T> T getObject(JSONObject param, Class<T> clazz) {
        if(param == null) return null;
        return JSON.toJavaObject(param, clazz);
    }

    private String getErrMsg(String key) {
        return "参数" + key + "必填";
    }

    public Map<String, Object> request2payResponseMap(HttpServletRequest request, String[] paramArray) {
        Map<String, Object> responseMap = new HashMap<>();
        for (int i = 0;i < paramArray.length; i++) {
            String key = paramArray[i];
            String v = request.getParameter(key);
            if (v != null) {
                responseMap.put(key, v);
            }
        }
        return responseMap;
    }


    protected String returnErrorPage(RetEnum retEnum){
        request.setAttribute("errMsg",retEnum);
        return PAGE_COMMON_ERROR;
    }


    /**
     * 处理参数中的金额(将前端传入金额元转成分)
     * modify: 20181206 添加JSON对象中的对象属性转换为分 格式[xxx.xxx]
     * @param param
     * @param names
     */
    public void handleParamAmount(JSONObject param, String... names) {
        for(String name : names) {
            String amountStr = getString(param, name);  // 前端填写的为元,可以为小数点2位
            if(StringUtils.isNotBlank(amountStr)) {
                Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分

                if(name.indexOf(".") < 0 ){
                    param.put(name, amountL);
                    continue;
                }

                param.getJSONObject(name.substring(0, name.indexOf("."))).put(name.substring(name.indexOf(".")+1, name.length()), amountL);

            }
        }
    }



}
