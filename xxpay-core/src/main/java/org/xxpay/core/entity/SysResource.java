package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_sys_resource")
public class SysResource implements Serializable {
    @TableField("ResourceId")
    private Long resourceId;

    /**
     * 资源名称
     *
     * @mbggenerated
     */
    @TableField("Name")
    private String name;

    /**
     * 资源标题
     *
     * @mbggenerated
     */
    @TableField("Title")
    private String title;

    /**
     * 跳转URL
     *
     * @mbggenerated
     */
    @TableField("Jump")
    private String jump;

    /**
     * 授权名,以ROLE_开头,如ROLE_MCH
     *
     * @mbggenerated
     */
    @TableField("PermName")
    private String permName;

    /**
     * 授权URL,如:mch_info/**
     *
     * @mbggenerated
     */
    @TableField("PermUrl")
    private String permUrl;

    /**
     * 资源类型 1：菜单, 2：按钮
     *
     * @mbggenerated
     */
    @TableField("Type")
    private Byte type;

    /**
     * 所属系统 1：运营平台, 2：商户平台, 2:代理商系统
     *
     * @mbggenerated
     */
    @TableField("System")
    private Byte system;

    /**
     * 菜单图标
     *
     * @mbggenerated
     */
    @TableField("Icon")
    private String icon;

    /**
     * 排序
     *
     * @mbggenerated
     */
    @TableField("OrderNum")
    private Integer orderNum;

    /**
     * 父资源ID，一级为0
     *
     * @mbggenerated
     */
    @TableField("ParentId")
    private Long parentId;

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
     * 状态 0：禁用, 1：正常
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 属性,为空都可见.否则对应商户类型,如1 表示平台账户可见, 1,2 表示平台账户和私有账户都可见
     *
     * @mbggenerated
     */
    @TableField("Property")
    private String property;

    private static final long serialVersionUID = 1L;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getSystem() {
        return system;
    }

    public void setSystem(Byte system) {
        this.system = system;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", resourceId=").append(resourceId);
        sb.append(", name=").append(name);
        sb.append(", title=").append(title);
        sb.append(", jump=").append(jump);
        sb.append(", permName=").append(permName);
        sb.append(", permUrl=").append(permUrl);
        sb.append(", type=").append(type);
        sb.append(", system=").append(system);
        sb.append(", icon=").append(icon);
        sb.append(", orderNum=").append(orderNum);
        sb.append(", parentId=").append(parentId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", status=").append(status);
        sb.append(", property=").append(property);
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
        SysResource other = (SysResource) that;
        return (this.getResourceId() == null ? other.getResourceId() == null : this.getResourceId().equals(other.getResourceId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getJump() == null ? other.getJump() == null : this.getJump().equals(other.getJump()))
            && (this.getPermName() == null ? other.getPermName() == null : this.getPermName().equals(other.getPermName()))
            && (this.getPermUrl() == null ? other.getPermUrl() == null : this.getPermUrl().equals(other.getPermUrl()))
            && (this.getType() == null ? other.getType() == null : this.getType().equals(other.getType()))
            && (this.getSystem() == null ? other.getSystem() == null : this.getSystem().equals(other.getSystem()))
            && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()))
            && (this.getOrderNum() == null ? other.getOrderNum() == null : this.getOrderNum().equals(other.getOrderNum()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getProperty() == null ? other.getProperty() == null : this.getProperty().equals(other.getProperty()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getResourceId() == null) ? 0 : getResourceId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getJump() == null) ? 0 : getJump().hashCode());
        result = prime * result + ((getPermName() == null) ? 0 : getPermName().hashCode());
        result = prime * result + ((getPermUrl() == null) ? 0 : getPermUrl().hashCode());
        result = prime * result + ((getType() == null) ? 0 : getType().hashCode());
        result = prime * result + ((getSystem() == null) ? 0 : getSystem().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        result = prime * result + ((getOrderNum() == null) ? 0 : getOrderNum().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getProperty() == null) ? 0 : getProperty().hashCode());
        return result;
    }
}
