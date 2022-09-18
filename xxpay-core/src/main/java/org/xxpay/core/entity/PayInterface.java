package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_pay_interface")
public class PayInterface implements Serializable {
    /**
     * 接口代码
     *
     * @mbggenerated
     */
    @TableField("IfCode")
    private String ifCode;

    /**
     * 接口名称
     *
     * @mbggenerated
     */
    @TableField("IfName")
    private String ifName;

    /**
     * 接口类型代码
     *
     * @mbggenerated
     */
    @TableField("IfTypeCode")
    private String ifTypeCode;

    /**
     * 支付类型:10:网银支付,11:快捷支付,12:微信扫码支付,13:微信H5支付,14:微信公众号支付,15:微信小程序支付,16:支付宝扫码支付,17:支付宝H5支付,18:支付宝服务窗支付,19:QQ钱包扫码,20:QQ钱包H5支付,21:京东扫码支付,22:京东H5支付,23:百度钱包,24:银联二维码
     *
     * @mbggenerated
     */
    @TableField("PayType")
    private String payType;

    /**
     * 应用场景,1:移动APP,2:移动网页,3:PC网页,4:微信公众平台,5:手机扫码
     *
     * @mbggenerated
     */
    @TableField("Scene")
    private Byte scene;

    /**
     * 接口状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 配置参数,json字符串
     *
     * @mbggenerated
     */
    @TableField("Param")
    private String param;

    /**
     * 备注
     *
     * @mbggenerated
     */
    @TableField("Remark")
    private String remark;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    @TableField("UpdateTime")
    private Date updateTime;

    /**
     * 扩展参数
     *
     * @mbggenerated
     */
    @TableField("Extra")
    private String extra;

    private static final long serialVersionUID = 1L;

    public String getIfCode() {
        return ifCode;
    }

    public void setIfCode(String ifCode) {
        this.ifCode = ifCode;
    }

    public String getIfName() {
        return ifName;
    }

    public void setIfName(String ifName) {
        this.ifName = ifName;
    }

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Byte getScene() {
        return scene;
    }

    public void setScene(Byte scene) {
        this.scene = scene;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", ifCode=").append(ifCode);
        sb.append(", ifName=").append(ifName);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", payType=").append(payType);
        sb.append(", scene=").append(scene);
        sb.append(", status=").append(status);
        sb.append(", param=").append(param);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", extra=").append(extra);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PayInterface other = (PayInterface) that;
        return (this.getIfCode() == null ? other.getIfCode() == null : this.getIfCode().equals(other.getIfCode()))
            && (this.getIfName() == null ? other.getIfName() == null : this.getIfName().equals(other.getIfName()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getScene() == null ? other.getScene() == null : this.getScene().equals(other.getScene()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getParam() == null ? other.getParam() == null : this.getParam().equals(other.getParam()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getExtra() == null ? other.getExtra() == null : this.getExtra().equals(other.getExtra()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getIfCode() == null) ? 0 : getIfCode().hashCode());
        result = prime * result + ((getIfName() == null) ? 0 : getIfName().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getScene() == null) ? 0 : getScene().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getParam() == null) ? 0 : getParam().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getExtra() == null) ? 0 : getExtra().hashCode());
        return result;
    }
}
