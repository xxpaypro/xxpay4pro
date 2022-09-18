package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_sys_role")
public class SysRole implements Serializable {
    @TableField("RoleId")
    private Long roleId;

    /**
     * 角色名称
     *
     * @mbggenerated
     */
    @TableField("RoleName")
    private String roleName;

    /**
     * 创建者ID
     *
     * @mbggenerated
     */
    @TableField("CreateUserId")
    private Long createUserId;

    /**
     * 所属角色ID 商户ID / 代理商ID / 0(平台)
     *
     * @mbggenerated
     */
    @TableField("BelongInfoId")
    private Long belongInfoId;

    /**
     * 所属角色类型:1-商户 2-代理商 3-平台
     *
     * @mbggenerated
     */
    @TableField("BelongInfoType")
    private Byte belongInfoType;

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

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getBelongInfoId() {
        return belongInfoId;
    }

    public void setBelongInfoId(Long belongInfoId) {
        this.belongInfoId = belongInfoId;
    }

    public Byte getBelongInfoType() {
        return belongInfoType;
    }

    public void setBelongInfoType(Byte belongInfoType) {
        this.belongInfoType = belongInfoType;
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
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", createUserId=").append(createUserId);
        sb.append(", belongInfoId=").append(belongInfoId);
        sb.append(", belongInfoType=").append(belongInfoType);
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
        SysRole other = (SysRole) that;
        return (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
                && (this.getRoleName() == null ? other.getRoleName() == null : this.getRoleName().equals(other.getRoleName()))
                && (this.getCreateUserId() == null ? other.getCreateUserId() == null : this.getCreateUserId().equals(other.getCreateUserId()))
                && (this.getBelongInfoId() == null ? other.getBelongInfoId() == null : this.getBelongInfoId().equals(other.getBelongInfoId()))
                && (this.getBelongInfoType() == null ? other.getBelongInfoType() == null : this.getBelongInfoType().equals(other.getBelongInfoType()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getRoleName() == null) ? 0 : getRoleName().hashCode());
        result = prime * result + ((getCreateUserId() == null) ? 0 : getCreateUserId().hashCode());
        result = prime * result + ((getBelongInfoId() == null) ? 0 : getBelongInfoId().hashCode());
        result = prime * result + ((getBelongInfoType() == null) ? 0 : getBelongInfoType().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
