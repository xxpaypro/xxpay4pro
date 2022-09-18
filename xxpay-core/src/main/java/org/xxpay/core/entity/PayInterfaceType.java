package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 支付接口类型表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-02
 */
@TableName("t_pay_interface_type")
public class PayInterfaceType extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 接口类型代码
     */
    @TableId("IfTypeCode")
    private String ifTypeCode;

    /**
     * 接口类型名称
     */
    @TableField("IfTypeName")
    private String ifTypeName;

    /**
     * 状态,0-关闭,1-开启
     */
    @TableField("Status")
    private Byte status;

    /**
     * ISV接口配置定义描述,json字符串
     */
    @TableField("IsvParam")
    private String isvParam;

    /**
     * 商户接口配置定义描述,json字符串
     */
    @TableField("MchParam")
    private String mchParam;

    /**
     * 私有商户接口配置定义描述,json字符串
     */
    @TableField("PrivateMchParam")
    private String privateMchParam;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

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

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }
    public String getIfTypeName() {
        return ifTypeName;
    }

    public void setIfTypeName(String ifTypeName) {
        this.ifTypeName = ifTypeName;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getIsvParam() {
        return isvParam;
    }

    public void setIsvParam(String isvParam) {
        this.isvParam = isvParam;
    }
    public String getMchParam() {
        return mchParam;
    }

    public void setMchParam(String mchParam) {
        this.mchParam = mchParam;
    }
    public String getPrivateMchParam() {
        return privateMchParam;
    }

    public void setPrivateMchParam(String privateMchParam) {
        this.privateMchParam = privateMchParam;
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

    @Override
    public String toString() {
        return "PayInterfaceType{" +
                "IfTypeCode=" + ifTypeCode +
                ", ifTypeName=" + ifTypeName +
                ", status=" + status +
                ", isvParam=" + isvParam +
                ", mchParam=" + mchParam +
                ", privateMchParam=" + privateMchParam +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
