package org.xxpay.mbr.common.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.xxpay.core.common.ctrl.AbstractController;
import org.xxpay.core.common.util.DateUtil;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.Member;
import org.xxpay.mbr.common.config.MainConfig;
import org.xxpay.mbr.common.service.RpcCommonService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author: dingzhiwei
 * @date: 17/12/6
 * @description:
 */
@Controller
public class BaseController {

    protected static final MyLog logger = MyLog.getLog(AbstractController.class);

    private static final String REQ_PARAM_JSON_KEY = "REQ_CONTEXT_REQ_PARAM_JSON";  //将转换好的json对象保存在 reqContext对象中的key
    private static final String PAGE_INDEX_PARAM_NAME = "page";  //分页页码 参数名
    private static final String PAGE_SIZE_PARAM_NAME = "limit";  //分页条数 参数名
    private static final int DEFAULT_PAGE_INDEX = 1;  // 默认页码： 第一页
    private static final int DEFAULT_PAGE_SIZE = 20;  // 默认条数： 20

    /** 错误页面跳转位置 **/
    protected static final String PAGE_COMMON_ERROR = "common/error";

    @Autowired
    protected HttpServletRequest request;   //自动注入request

    @Autowired
    protected HttpServletResponse response;  //自动注入response

    @Autowired
    protected MainConfig mainConfig;

    @Autowired
    protected RpcCommonService rpcCommonService;

    /**
     * 输出excel数据
     * @param fileName
     * @param data
     */
    protected void writeExcelStream(String fileName, List<List> data) {

        try {
            response.setHeader("Content-disposition", "attachment;filename="
                    + new String((fileName + ".xlsx").getBytes("gb2312"), "ISO8859-1"));//设置文件头编码格式
            response.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型

            Workbook workbook = new XSSFWorkbook(); //生成excel 2007格式
            Sheet sheet = workbook.createSheet();

            for(int i = 0; i < data.size(); i++){
                Row row = sheet.createRow(i);

                for(int j = 0; j< data.get(i).size(); j++){
                    Cell cell = row.createCell(j);
                    if(data.get(i).get(j) != null) {
                        cell.setCellValue(data.get(i).get(j).toString());
                    }
                }
            }
            workbook.write(response.getOutputStream());
        } catch (Exception e) {
            logger.error("writeExcelStream", e);
        }
    }

    /**request.getParameter 获取参数 并转换为JSON格式 **/
    private JSONObject reqParam2JSON() {
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

            if(!name.contains("[")){
                returnObject.put(name, value);
                continue;
            }
            //添加对json对象解析的支持  example: {ps[abc] : 1}
            String mainKey = name.substring(0, name.indexOf("["));
            String subKey = name.substring(name.indexOf("[") + 1 , name.indexOf("]"));
            JSONObject subJson = new JSONObject();
            if(returnObject.get(mainKey) != null) {
                subJson = (JSONObject)returnObject.get(mainKey);
            }
            subJson.put(subKey, value);
            returnObject.put(mainKey, subJson);
        }
        return returnObject;
    }

    /** 获取json格式的请求参数 **/
    protected JSONObject getReqParamJSON(){

        //将转换好的reqParam JSON格式的对象保存在当前请求上下文对象中进行保存；
        // 注意1： springMVC的CTRL默认单例模式， 不可使用局部变量保存，会出现线程安全问题；
        // 注意2： springMVC的请求模式为线程池，如果采用ThreadLocal保存对象信息，可能会出现不清空或者被覆盖的问题。
        Object reqParamObject = RequestContextHolder.getRequestAttributes().getAttribute(REQ_PARAM_JSON_KEY, RequestAttributes.SCOPE_REQUEST);
        if(reqParamObject == null){
            JSONObject reqParam = reqParam2JSON();
            RequestContextHolder.getRequestAttributes().setAttribute(REQ_PARAM_JSON_KEY, reqParam, RequestAttributes.SCOPE_REQUEST);
            return reqParam;
        }
        return (JSONObject) reqParamObject;
    }

    /** 获取页码 **/
    protected int getPageIndex() {
        Integer pageIndex = getReqParamJSON().getInteger(PAGE_INDEX_PARAM_NAME);
        if(pageIndex == null) return DEFAULT_PAGE_INDEX;
        return pageIndex;
    }

    /** 获取条数， 默认不允许查询全部数据 **/
    protected int getPageSize() {
        return getPageSize(false);
    }

    /** 获取条数,  加入条件：是否允许获取全部数据 **/
    protected int getPageSize(boolean allowQueryAll) {
        Integer pageSize = getReqParamJSON().getInteger(PAGE_SIZE_PARAM_NAME);

        if(allowQueryAll && pageSize != null && pageSize == -1) return Integer.MAX_VALUE; // -1代表获取全部数据，查询int最大值的数据
        if(pageSize == null || pageSize < 0) return DEFAULT_PAGE_SIZE;
        return pageSize;
    }


    /** 获取Ipage分页信息, 默认不允许获取全部数据 **/
    protected IPage getIPage(){
        return new Page(getPageIndex(), getPageSize());
    }

    /** 获取Ipage分页信息, 加入条件：是否允许获取全部数据 **/
    protected IPage getIPage(boolean allowQueryAll){
        return new Page(getPageIndex(), getPageSize(allowQueryAll));
    }

    /** 获取请求参数值 [ T 类型 ], [ 非必填 ] **/
    protected <T> T getVal(String key, Class<T> cls) {
        return getReqParamJSON().getObject(key, cls);
    }

    /** 获取请求参数值 [ T 类型 ], [ 必填 ] **/
    protected <T> T getValRequired(String key, Class<T> cls) {
        T value = getVal(key, cls);
        if(ObjectUtils.isEmpty(value)) {
            throw new RuntimeException(genParamRequiredMsg(key));
        }
        return value;
    }

    /** 获取请求参数值 [ T 类型 ], [ 如为null返回默认值 ] **/
    protected  <T> T getValDefault(String key, T defaultValue, Class<T> cls) {
        T value = getVal(key, cls);
        if(value == null) return defaultValue;
        return value;
    }

    /** 获取请求参数值 String 类型相关函数 **/
    protected String getValString(String key) {
        return getVal(key, String.class);
    }
    protected String getValStringRequired(String key) {
        return getValRequired(key, String.class);
    }
    protected String getValStringDefault(String key, String defaultValue) {
        return getValDefault(key, defaultValue, String.class);
    }

    /** 获取请求参数值 Byte 类型相关函数 **/
    protected Byte getValByte(String key) {
        return getVal(key, Byte.class);
    }
    protected Byte getValByteRequired(String key) {
        return getValRequired(key, Byte.class);
    }
    protected Byte getValByteDefault(String key, Byte defaultValue) {
        return getValDefault(key, defaultValue, Byte.class);
    }

    /** 获取请求参数值 Integer 类型相关函数 **/
    protected Integer getValInteger(String key) {
        return getVal(key, Integer.class);
    }
    protected Integer getValIntegerRequired(String key) {
        return getValRequired(key, Integer.class);
    }
    protected Integer getValIntegerDefault(String key, Integer defaultValue) {
        return getValDefault(key, defaultValue, Integer.class);
    }

    /** 获取请求参数值 Long 类型相关函数 **/
    protected Long getValLong(String key) {
        return getVal(key, Long.class);
    }
    protected Long getValLongRequired(String key) {
        return getValRequired(key, Long.class);
    }
    protected Long getValLongDefault(String key, Long defaultValue) {
        return getValDefault(key, defaultValue, Long.class);
    }

    /** 获取对象类型 **/
    protected <T> T getObject(Class<T> clazz) {
        return getReqParamJSON().toJavaObject(clazz);
    }

    /** 生成参数必填错误信息 **/
    private String genParamRequiredMsg(String key) {
        return "参数" + key + "必填";
    }

    /** 校验参数值不能为空 */
    protected void checkRequired(String... keys) {

        for(String key : keys) {
            String value = getReqParamJSON().getString(key);
            if(StringUtils.isEmpty(value)) throw new RuntimeException(genParamRequiredMsg(key));
        }
    }

    /** 得到前端传入的金额元,转换成长整型分 **/
    public Long getRequiredAmountL(String name) {
        String amountStr = getValStringRequired(name);  // 前端填写的为元,可以为小数点2位
        Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
        return amountL;
    }

    /**
     * 处理参数中的金额(将前端传入金额元转成分)
     * modify: 20181206 添加JSON对象中的对象属性转换为分 格式[xxx.xxx]
     * @param names
     */
    public void handleParamAmount(String... names) {
        for(String name : names) {
            String amountStr = getValString(name);  // 前端填写的为元,可以为小数点2位
            if(StringUtils.isNotBlank(amountStr)) {
                Long amountL = new BigDecimal(amountStr.trim()).multiply(new BigDecimal(100)).longValue(); // // 转成分
                if(name.indexOf(".") < 0 ){
                    getReqParamJSON().put(name, amountL);
                    continue;
                }
                getReqParamJSON().getJSONObject(name.substring(0, name.indexOf("."))).put(name.substring(name.indexOf(".")+1), amountL);
            }
        }
    }

    /** 请求参数转换为map格式 **/
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

    /** 生成公共查询参训 */
    public JSONObject getQueryObj() {
        // 订单起止时间
        Date createTimeStart = null;
        Date createTimeEnd = null;
        String createTimeStartStr = getValString("createTimeStart");
        if(StringUtils.isNotBlank(createTimeStartStr)) createTimeStart = DateUtil.str2date(createTimeStartStr);
        String createTimeEndStr = getValString("createTimeEnd");
        if(StringUtils.isNotBlank(createTimeEndStr)) createTimeEnd = DateUtil.str2date(createTimeEndStr);
        JSONObject queryObj = new JSONObject();
        queryObj.put("createTimeStart", createTimeStart);
        queryObj.put("createTimeEnd", createTimeEnd);
        return queryObj;
    }

    protected Member getUser() {
        Member member = new Member();

        String mchIdStr = request.getHeader("mchId");
        if (StringUtils.isNotBlank(mchIdStr)){
            member.setMchId(Long.valueOf(mchIdStr));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            member =  (Member) authentication.getPrincipal();
        }

        return member;
    }

}
