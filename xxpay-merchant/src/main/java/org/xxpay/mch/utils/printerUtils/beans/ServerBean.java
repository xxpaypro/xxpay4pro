package org.xxpay.mch.utils.printerUtils.beans;

/**
 * 服务器标准返回节点
 * @param <T>
 */
public class ServerBean<T> {
    private Integer code;
    private String message;
    private String description;
    private T data;

    public Integer getCode() {
        return code;
    }
    public void setCode(Integer code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
