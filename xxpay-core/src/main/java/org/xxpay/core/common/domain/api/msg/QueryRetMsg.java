package org.xxpay.core.common.domain.api.msg;

import java.io.Serializable;

/**
 * 封装对象查单返回信息
 **/
public class QueryRetMsg implements Serializable {

    /** 上游渠道返回状态 **/
    private ChannelState channelState;

    /** 渠道订单号 **/
    private String channelOrderId;

    /** 渠道错误码 **/
    private String channelErrCode;

    /** 渠道错误描述 **/
    private String channelErrMsg;

    /** 渠道支付数据包, 一般用于支付订单的继续支付操作 **/
    private String channelAttach;

    /** 上游渠道返回的原始报文, 一般用于[运营平台的查询上游结果]功能 **/
    private String channelOriginResponse;

    public QueryRetMsg(){}
    public QueryRetMsg(ChannelState channelState, String channelOrderId, String channelErrCode, String channelErrMsg) {
        this.channelState = channelState;
        this.channelOrderId = channelOrderId;
        this.channelErrCode = channelErrCode;
        this.channelErrMsg = channelErrMsg;
    }

    /** getter / setter **/
    public ChannelState getChannelState() {
        return channelState;
    }

    public void setChannelState(ChannelState channelState) {
        this.channelState = channelState;
    }

    public String getChannelOrderId() {
        return channelOrderId;
    }

    public void setChannelOrderId(String channelOrderId) {
        this.channelOrderId = channelOrderId;
    }

    public String getChannelErrCode() {
        return channelErrCode;
    }

    public void setChannelErrCode(String channelErrCode) {
        this.channelErrCode = channelErrCode;
    }

    public String getChannelErrMsg() {
        return channelErrMsg;
    }

    public void setChannelErrMsg(String channelErrMsg) {
        this.channelErrMsg = channelErrMsg;
    }

    public String getChannelOriginResponse() {
        return channelOriginResponse;
    }

    public void setChannelOriginResponse(String channelOriginResponse) {
        this.channelOriginResponse = channelOriginResponse;
    }

    public String getChannelAttach() {
        return channelAttach;
    }

    public void setChannelAttach(String channelAttach) {
        this.channelAttach = channelAttach;
    }

    //渠道状态枚举值
    public enum ChannelState {
        CONFIRM_SUCCESS, //明确成功
        CONFIRM_FAIL, //明确失败
        WAITING, //上游处理中， 需通过定时查询/回调进行下一步处理
        UNKNOWN, //状态不明确 ( 上游接口变更, 无法确定状态值 )
        SYS_ERROR //出现异常( 接口调起异常/服务异常)
    }

    //静态初始函数

    /** 明确成功 **/
    public static QueryRetMsg confirmSuccess(String channelOrderId){
        return new QueryRetMsg(ChannelState.CONFIRM_SUCCESS, channelOrderId, null, null);
    }

    /** 明确失败 **/
    public static QueryRetMsg confirmFail(String channelOrderId, String channelErrCode, String channelErrMsg){
        return new QueryRetMsg(ChannelState.CONFIRM_FAIL, channelOrderId, channelErrCode, channelErrMsg);
    }

    /** 明确失败 **/
    public static QueryRetMsg confirmFail(String channelOrderId){
        return new QueryRetMsg(ChannelState.CONFIRM_FAIL, channelOrderId, null, null);
    }

    /** 明确失败 **/
    public static QueryRetMsg confirmFail(){
        return new QueryRetMsg(ChannelState.CONFIRM_FAIL, null, null, null);
    }

    /** 处理中 **/
    public static QueryRetMsg waiting(){
        return new QueryRetMsg(ChannelState.WAITING, null, null, null);
    }


    /** 异常的情况 **/
    public static QueryRetMsg sysError(){
        return new QueryRetMsg(ChannelState.SYS_ERROR, null, null, null);
    }

    /** 状态未知的情况 **/
    public static QueryRetMsg unknown(){
        return new QueryRetMsg(ChannelState.UNKNOWN, null, null, null);
    }

    @Override
    public String toString() {
        return "QueryRetMsg{" +
                "channelState=" + channelState +
                ", channelOrderId='" + channelOrderId + '\'' +
                ", channelErrCode='" + channelErrCode + '\'' +
                ", channelErrMsg='" + channelErrMsg + '\'' +
                ", channelOriginResponse='" + channelOriginResponse + '\'' +
                '}';
    }
}





