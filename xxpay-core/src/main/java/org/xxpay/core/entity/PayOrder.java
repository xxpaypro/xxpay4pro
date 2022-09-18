package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 支付订单表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-13
 */
@TableName("t_pay_order")
public class PayOrder extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 支付订单号
     */
    @TableId("PayOrderId")
    private String payOrderId;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 商户类型:1-平台账户,2-私有账户
     */
    @TableField("MchType")
    private Byte mchType;

    /**
     * 是否押金模式:0-否,1-是
     */
    @TableField("DepositMode")
    private Byte depositMode;

    /**
     * 应用ID
     */
    @TableField("AppId")
    private String appId;

    /**
     * 商户订单号
     */
    @TableField("MchOrderNo")
    private String mchOrderNo;

    /**
     * 商户所属代理商ID
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 商户所属服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 支付产品ID
     */
    @TableField("ProductId")
    private Integer productId;

    /**
     * 商户通道ID
     */
    @TableField("MchPassageId")
    private Integer mchPassageId;

    /**
     * 服务商通道ID
     */
    @TableField("IsvPassageId")
    private Integer isvPassageId;

    /**
     * 渠道类型,对接支付接口类型代码
     */
    @TableField("ChannelType")
    private String channelType;

    /**
     * 渠道ID,对应支付接口代码
     */
    @TableField("ChannelId")
    private String channelId;

    /**
     * 支付金额,单位分
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 押金金额,单位分
     */
    @TableField("DepositAmount")
    private Long depositAmount;

    /**
     * 三位货币代码,人民币:cny
     */
    @TableField("Currency")
    private String currency;

    /**
     * 支付状态,0-订单生成,1-支付中,2-支付成功, 4-已退款, 6-押金未结算, 7-押金退还
     */
    @TableField("Status")
    private Byte status;

    /**
     * 客户端IP
     */
    @TableField("ClientIp")
    private String clientIp;

    /**
     * 设备
     */
    @TableField("Device")
    private String device;

    /**
     * 商品标题
     */
    @TableField("Subject")
    private String subject;

    /**
     * 商品描述信息
     */
    @TableField("Body")
    private String body;

    /**
     * 特定渠道发起额外参数
     */
    @TableField("Extra")
    private String extra;

    /**
     * 渠道用户标识,如微信openId,支付宝账号
     */
    @TableField("ChannelUser")
    private String channelUser;

    /**
     * 渠道商户ID
     */
    @TableField("ChannelMchId")
    private String channelMchId;

    /**
     * 渠道订单号
     */
    @TableField("ChannelOrderNo")
    private String channelOrderNo;

    /**
     * 渠道数据包
     */
    @TableField("ChannelAttach")
    private String channelAttach;

    /**
     * 是否退款,0-未退款,1-退款
     */
    @TableField("IsRefund")
    private Byte isRefund;

    /**
     * 退款次数
     */
    @TableField("RefundTimes")
    private Integer refundTimes;

    /**
     * 成功退款金额,单位分
     */
    @TableField("SuccessRefundAmount")
    private Long successRefundAmount;

    /**
     * 渠道支付错误码
     */
    @TableField("ErrCode")
    private String errCode;

    /**
     * 渠道支付错误描述
     */
    @TableField("ErrMsg")
    private String errMsg;

    /**
     * 扩展参数1
     */
    @TableField("Param1")
    private String param1;

    /**
     * 扩展参数2
     */
    @TableField("Param2")
    private String param2;

    /**
     * 通知地址
     */
    @TableField("NotifyUrl")
    private String notifyUrl;

    /**
     * 跳转地址
     */
    @TableField("ReturnUrl")
    private String returnUrl;

    /**
     * 订单失效时间
     */
    @TableField("ExpireTime")
    private Date expireTime;

    /**
     * 订单支付成功时间
     */
    @TableField("PaySuccTime")
    private Date paySuccTime;

    /**
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UpdateTime")
    private Date updateTime;

    /**
     * 产品类型:1-收款,2-充值
     */
    @TableField("ProductType")
    private Byte productType;

    public String getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(String payOrderId) {
        this.payOrderId = payOrderId;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }
    public Byte getDepositMode() {
        return depositMode;
    }

    public void setDepositMode(Byte depositMode) {
        this.depositMode = depositMode;
    }
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    public String getMchOrderNo() {
        return mchOrderNo;
    }

    public void setMchOrderNo(String mchOrderNo) {
        this.mchOrderNo = mchOrderNo;
    }
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public Integer getMchPassageId() {
        return mchPassageId;
    }

    public void setMchPassageId(Integer mchPassageId) {
        this.mchPassageId = mchPassageId;
    }
    public Integer getIsvPassageId() {
        return isvPassageId;
    }

    public void setIsvPassageId(Integer isvPassageId) {
        this.isvPassageId = isvPassageId;
    }
    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
    public Long getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Long depositAmount) {
        this.depositAmount = depositAmount;
    }
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    public String getChannelUser() {
        return channelUser;
    }

    public void setChannelUser(String channelUser) {
        this.channelUser = channelUser;
    }
    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }
    public String getChannelOrderNo() {
        return channelOrderNo;
    }

    public void setChannelOrderNo(String channelOrderNo) {
        this.channelOrderNo = channelOrderNo;
    }
    public String getChannelAttach() {
        return channelAttach;
    }

    public void setChannelAttach(String channelAttach) {
        this.channelAttach = channelAttach;
    }
    public Byte getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(Byte isRefund) {
        this.isRefund = isRefund;
    }
    public Integer getRefundTimes() {
        return refundTimes;
    }

    public void setRefundTimes(Integer refundTimes) {
        this.refundTimes = refundTimes;
    }
    public Long getSuccessRefundAmount() {
        return successRefundAmount;
    }

    public void setSuccessRefundAmount(Long successRefundAmount) {
        this.successRefundAmount = successRefundAmount;
    }
    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }
    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    public Date getPaySuccTime() {
        return paySuccTime;
    }

    public void setPaySuccTime(Date paySuccTime) {
        this.paySuccTime = paySuccTime;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        return "PayOrder{" +
                "PayOrderId=" + payOrderId +
                ", mchId=" + mchId +
                ", mchType=" + mchType +
                ", depositMode=" + depositMode +
                ", appId=" + appId +
                ", mchOrderNo=" + mchOrderNo +
                ", agentId=" + agentId +
                ", isvId=" + isvId +
                ", productId=" + productId +
                ", mchPassageId=" + mchPassageId +
                ", isvPassageId=" + isvPassageId +
                ", channelType=" + channelType +
                ", channelId=" + channelId +
                ", amount=" + amount +
                ", depositAmount=" + depositAmount +
                ", currency=" + currency +
                ", status=" + status +
                ", clientIp=" + clientIp +
                ", device=" + device +
                ", subject=" + subject +
                ", body=" + body +
                ", extra=" + extra +
                ", channelUser=" + channelUser +
                ", channelMchId=" + channelMchId +
                ", channelOrderNo=" + channelOrderNo +
                ", channelAttach=" + channelAttach +
                ", isRefund=" + isRefund +
                ", refundTimes=" + refundTimes +
                ", successRefundAmount=" + successRefundAmount +
                ", errCode=" + errCode +
                ", errMsg=" + errMsg +
                ", param1=" + param1 +
                ", param2=" + param2 +
                ", notifyUrl=" + notifyUrl +
                ", returnUrl=" + returnUrl +
                ", expireTime=" + expireTime +
                ", paySuccTime=" + paySuccTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", productType=" + productType +
                "}";
    }
}
