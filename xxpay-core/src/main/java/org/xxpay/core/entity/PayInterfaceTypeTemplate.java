package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_pay_interface_type_template")
public class PayInterfaceTypeTemplate implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 模板名称
     *
     * @mbggenerated
     */
    @TableField("TemplateName")
    private String templateName;

    /**
     * 接口类型代码
     *
     * @mbggenerated
     */
    @TableField("IfTypeCode")
    private String ifTypeCode;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 通道商户ID模板 不影响实际配置参数
     *
     * @mbggenerated
     */
    @TableField("PassageMchIdTemplate")
    private String passageMchIdTemplate;

    /**
     * 账户配置参数模板 不影响实际配置参数,json字符串
     *
     * @mbggenerated
     */
    @TableField("ParamTemplate")
    private String paramTemplate;

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

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getPassageMchIdTemplate() {
        return passageMchIdTemplate;
    }

    public void setPassageMchIdTemplate(String passageMchIdTemplate) {
        this.passageMchIdTemplate = passageMchIdTemplate;
    }

    public String getParamTemplate() {
        return paramTemplate;
    }

    public void setParamTemplate(String paramTemplate) {
        this.paramTemplate = paramTemplate;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", templateName=").append(templateName);
        sb.append(", ifTypeCode=").append(ifTypeCode);
        sb.append(", status=").append(status);
        sb.append(", passageMchIdTemplate=").append(passageMchIdTemplate);
        sb.append(", paramTemplate=").append(paramTemplate);
        sb.append(", remark=").append(remark);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        PayInterfaceTypeTemplate other = (PayInterfaceTypeTemplate) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getTemplateName() == null ? other.getTemplateName() == null : this.getTemplateName().equals(other.getTemplateName()))
            && (this.getIfTypeCode() == null ? other.getIfTypeCode() == null : this.getIfTypeCode().equals(other.getIfTypeCode()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getPassageMchIdTemplate() == null ? other.getPassageMchIdTemplate() == null : this.getPassageMchIdTemplate().equals(other.getPassageMchIdTemplate()))
            && (this.getParamTemplate() == null ? other.getParamTemplate() == null : this.getParamTemplate().equals(other.getParamTemplate()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getTemplateName() == null) ? 0 : getTemplateName().hashCode());
        result = prime * result + ((getIfTypeCode() == null) ? 0 : getIfTypeCode().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getPassageMchIdTemplate() == null) ? 0 : getPassageMchIdTemplate().hashCode());
        result = prime * result + ((getParamTemplate() == null) ? 0 : getParamTemplate().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
