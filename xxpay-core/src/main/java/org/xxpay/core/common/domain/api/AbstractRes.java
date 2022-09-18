package org.xxpay.core.common.domain.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.util.PayDigestUtil;

import java.io.Serializable;

/**
 *  接口抽象对象, 本身无需实例化
 */
public abstract class AbstractRes implements Serializable {

    /** 接口处理状态 , SUCCESS/FAIL  **/
    private String retCode;

    /** 返回信息，如错误描述等 **/
    private String retMsg;

    /** sign **/
    private String sign;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /** 传入商户KEY, 自动签名 **/
    public void autoGenSign(String key){

        JSONObject jsonObject = (JSONObject) JSON.toJSON(this);
        this.sign = PayDigestUtil.getSign(jsonObject, key);
    }

    public String toJSONString(){
        return JSON.toJSONString(this);
    }

}
