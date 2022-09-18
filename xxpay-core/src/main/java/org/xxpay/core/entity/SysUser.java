package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-29
 */
@TableName("t_mgr_sys_user")
public class SysUser extends BaseModel<SysUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "UserId", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称, 仅做展示
     */
    @TableField("NickName")
    private String nickName;

    /**
     * 用户登录名
     */
    @TableField("LoginUserName")
    private String loginUserName;

    /**
     * 登录邮箱
     */
    @TableField("Email")
    private String email;

    /**
     * 登录手机号
     */
    @TableField("Mobile")
    private String mobile;

    /**
     * 登录密码
     */
    @TableField("LoginPassword")
    private String loginPassword;

    /**
     * 状态 0：禁用, 1：正常
     */
    @TableField("Status")
    private Byte status;

    /**
     * 是否超级管理员/是否主角色（商户） 0：否, 1：是
     */
    @TableField("IsSuperAdmin")
    private Byte isSuperAdmin;

    /**
     * 所属角色ID 商户ID / 代理商ID / 0(平台)
     */
    @TableField("BelongInfoId")
    private Long belongInfoId;

    /**
     * 所属角色类型:1-商户 2-代理商 3-平台 4-服务商
     */
    @TableField("BelongInfoType")
    private Byte belongInfoType;

    /**
     * 头像路径
     */
    @TableField("Avatar")
    private String avatar;

    /**
     * 性别:0-未知 1-男 2-女
     */
    @TableField("Sex")
    private Byte sex;

    /**
     * 所属门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 0-未工作, 1-工作中
     */
    @TableField("WorkStatus")
    private Byte workStatus;

    /**
     * 工作开始时间
     */
    @TableField("WorkStartTime")
    private Date workStartTime;

    /**
     * 创建者ID
     */
    @TableField("CreateUserId")
    private Long createUserId;

    /**
     * 最后一次登录时间
     */
    @TableField("LastLoginTime")
    private Date lastLoginTime;

    /**
     * 最后一次登录IP
     */
    @TableField("LastLoginIp")
    private String lastLoginIp;

    /**
     * 最后一次重置密码时间
     */
    @TableField("LastPasswordResetTime")
    private Date lastPasswordResetTime;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Byte getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(Byte isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
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
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public Byte getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Byte workStatus) {
        this.workStatus = workStatus;
    }
    public Date getWorkStartTime() {
        return workStartTime;
    }

    public void setWorkStartTime(Date workStartTime) {
        this.workStartTime = workStartTime;
    }
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    public Date getLastPasswordResetTime() {
        return lastPasswordResetTime;
    }

    public void setLastPasswordResetTime(Date lastPasswordResetTime) {
        this.lastPasswordResetTime = lastPasswordResetTime;
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
        return "MgrSysUser{" +
                "UserId=" + userId +
                ", nickName=" + nickName +
                ", loginUserName=" + loginUserName +
                ", email=" + email +
                ", mobile=" + mobile +
                ", loginPassword=" + loginPassword +
                ", status=" + status +
                ", isSuperAdmin=" + isSuperAdmin +
                ", belongInfoId=" + belongInfoId +
                ", belongInfoType=" + belongInfoType +
                ", avatar=" + avatar +
                ", sex=" + sex +
                ", storeId=" + storeId +
                ", workStatus=" + workStatus +
                ", workStartTime=" + workStartTime +
                ", createUserId=" + createUserId +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp=" + lastLoginIp +
                ", lastPasswordResetTime=" + lastPasswordResetTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
