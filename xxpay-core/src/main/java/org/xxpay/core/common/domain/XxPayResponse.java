package org.xxpay.core.common.domain;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.common.util.ObjectValidUtil;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: dingzhiwei
 * @date: 17/11/29
 * @description:
 */
public class XxPayResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;
    private static final MyLog _log = MyLog.getLog(XxPayResponse.class);

    public int code;     // 返回码
    public String msg;     // 返回消息
    public Object data;    // 返回数据

    public XxPayResponse(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        _log.info(this.toString());
    }

    public XxPayResponse(RetEnum retEnum, Object data) {
        this.code = retEnum.getCode();
        this.msg = retEnum.getMessage();
        this.data = data;
        _log.info(this.toString());
    }

    public static XxPayResponse build(RetEnum retEnum) {
        XxPayResponse xxPayResponse = new XxPayResponse(retEnum.getCode(), retEnum.getMessage(), null);
        return xxPayResponse;
    }

    public static XxPayResponse buildErr(String errMsg) {
        XxPayResponse xxPayResponse = new XxPayResponse(99999, errMsg, null);
        return xxPayResponse;
    }


    public static XxPayResponse build(RetEnum retEnum, Object data) {
        XxPayResponse xxPayResponse = new XxPayResponse(retEnum.getCode(), retEnum.getMessage(), data);
        return xxPayResponse;
    }

    public static XxPayResponse buildSuccess() {
        return buildSuccess(null);
    }

    public static XxPayResponse buildSuccess(Object data) {
        XxPayResponse xxPayResponse = new XxPayResponse(RetEnum.RET_COMM_SUCCESS, data);
        return xxPayResponse;
    }

    public static XxPayResponse buildSuccess(Object data, JSONObject param) {
        if(param != null && param.getBooleanValue("returnArray")) {
            List<Object> objectList = new LinkedList<Object>();
            objectList.add(data);
            return new XxPayResponse(RetEnum.RET_COMM_SUCCESS, objectList);
        }else {
            return new XxPayResponse(RetEnum.RET_COMM_SUCCESS, data);
        }
    }

    public String getMsg() {
        return msg;
    }

    public XxPayResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getData() {
        return data;
    }

    public XxPayResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "XxPayResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
