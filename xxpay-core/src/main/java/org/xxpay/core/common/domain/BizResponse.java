package org.xxpay.core.common.domain;

import org.apache.commons.lang3.StringUtils;
import org.xxpay.core.common.constant.RetEnum;
import org.xxpay.core.common.util.MyLog;

import java.io.Serializable;

/**
 * @author: dingzhiwei
 * @date: 17/11/29
 * @description:
 */
public class BizResponse implements Serializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private static final MyLog _log = MyLog.getLog(BizResponse.class);

    private int code;     // 返回码
    private String msg;     // 返回消息

    public BizResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
        _log.info(this.toString());
    }

    public BizResponse(RetEnum retEnum) {
        this.code = retEnum.getCode();
        this.msg = retEnum.getMessage();
    }

    public static BizResponse build(RetEnum retEnum) {
        BizResponse bizResponse = new BizResponse(retEnum.getCode(), retEnum.getMessage());
        return bizResponse;
    }

    public static BizResponse build(RetEnum retEnum, String tip) {
        String msg = retEnum.getMessage();
        if(StringUtils.isNotBlank(tip)) {
            msg += '[' + tip + "]";
        }
        BizResponse bizResponse = new BizResponse(retEnum.getCode(), msg);
        return bizResponse;
    }

    public static BizResponse buildSuccess() {
        BizResponse bizResponse = new BizResponse(RetEnum.RET_COMM_SUCCESS);
        return bizResponse;
    }

    public static BizResponse build(String msg) {
        BizResponse bizResponse = new BizResponse(0, msg);
        return bizResponse;
    }

    public String getMsg() {
        return msg;
    }

    public BizResponse setMsg(String msg) {
        this.msg = msg;
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
        return "BizResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
