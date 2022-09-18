package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 商户通知表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2020-03-04
 */
@TableName("t_mch_notify")
public class MchNotify implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    @TableId("OrderId")
    private String orderId;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

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
     * 订单类型:1-支付,2-转账,3-退款
     */
    @TableField("OrderType")
    private String orderType;

    /**
     * 通知地址
     */
    @TableField("NotifyUrl")
    private String notifyUrl;

    /**
     * 通知次数
     */
    @TableField("NotifyCount")
    private Byte notifyCount;

    /**
     * 通知响应结果
     */
    @TableField("Result")
    private String result;

    /**
     * 通知状态,1-通知中,2-通知成功,3-通知失败
     */
    @TableField("Status")
    private Byte status;

    /**
     * 最后一次通知时间
     */
    @TableField("LastNotifyTime")
    private Date lastNotifyTime;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
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
    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
    public Byte getNotifyCount() {
        return notifyCount;
    }

    public void setNotifyCount(Byte notifyCount) {
        this.notifyCount = notifyCount;
    }
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Date getLastNotifyTime() {
        return lastNotifyTime;
    }

    public void setLastNotifyTime(Date lastNotifyTime) {
        this.lastNotifyTime = lastNotifyTime;
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

    @Override
    public String toString() {
        return "MchNotify{" +
                "OrderId=" + orderId +
                ", mchId=" + mchId +
                ", isvId=" + isvId +
                ", appId=" + appId +
                ", mchOrderNo=" + mchOrderNo +
                ", orderType=" + orderType +
                ", notifyUrl=" + notifyUrl +
                ", notifyCount=" + notifyCount +
                ", result=" + result +
                ", status=" + status +
                ", lastNotifyTime=" + lastNotifyTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
