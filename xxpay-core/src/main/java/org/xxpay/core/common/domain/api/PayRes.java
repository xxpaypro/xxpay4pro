package org.xxpay.core.common.domain.api;

import com.alibaba.fastjson.JSONObject;
import org.xxpay.core.common.constant.PayConstant;

/**
 * 支付接口api 实体Bean
 */
public class PayRes extends AbstractRes {

    /** 支付订单号 **/
    private String payOrderId;

    /** formJump / codeImg  **/
    private String payMethod;

    /** 支付跳转地址： 一般为HTML跳转形式 **/
    private String payUrl;

    /** 跳转URL  **/
    private String payJumpUrl;

    /** 支付方式 GET / POST  **/
    private String payAction;

    /** 二维码连接地址  **/
    private String codeUrl;

    /** 二维码图片地址  **/
    private String codeImgUrl;

    /** 跳转第三方支付 自定义参数	 **/
    private JSONObject payParams;

    /** 是否需要下游商户主动查询订单状态 **/
    private Boolean needQuery;

    /** 订单状态 **/
    private Byte orderStatus;


    /** 封装函数：设置二维码方式支付信息 **/
    public void setQrInfo(String codeUrl){
        setQrInfo(codeUrl);
    }
    public void setQrInfo(String codeUrl, String codeImgUrl){
        this.payMethod = PayConstant.PAY_METHOD_CODE_IMG;
        this.codeUrl = codeUrl;
        this.codeImgUrl = codeImgUrl;
    }

    /** 封装函数：设置url形式支付信息 **/
    public void setJumpInfo(String payUrl){
        setJumpInfo(payUrl, null, null);
    }
    public void setJumpInfo(String payUrl, String payJumpUrl){
        setJumpInfo(payUrl, payJumpUrl, null);
    }
    public void setJumpInfo(String payUrl, String payJumpUrl, String payAction){

        this.payMethod = PayConstant.PAY_METHOD_FORM_JUMP;
        this.payUrl = payUrl;
        this.payJumpUrl = payJumpUrl;
        this.payAction = payAction;
    }

    /** 封装函数：设置应用方式支付信息 **/
    public void setAppInfo(String payMethod, JSONObject payParams){
        this.payMethod = payMethod;
        this.payParams = payParams;
    }

    /** getter / setter **/
    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPayJumpUrl() {
        return payJumpUrl;
    }

    public void setPayJumpUrl(String payJumpUrl) {
        this.payJumpUrl = payJumpUrl;
    }

    public String getPayAction() {
        return payAction;
    }

    public void setPayAction(String payAction) {
        this.payAction = payAction;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getCodeImgUrl() {
        return codeImgUrl;
    }

    public void setCodeImgUrl(String codeImgUrl) {
        this.codeImgUrl = codeImgUrl;
    }

    public Boolean isNeedQuery() {
        return needQuery;
    }

    public void setNeedQuery(Boolean needQuery) {
        this.needQuery = needQuery;
    }

    public Byte getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }

    public JSONObject getPayParams() {
        return payParams;
    }

    public void setPayParams(JSONObject payParams) {
        this.payParams = payParams;
    }


}


