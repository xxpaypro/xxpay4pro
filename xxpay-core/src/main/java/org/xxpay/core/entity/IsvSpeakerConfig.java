package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 服务商云喇叭配置表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-20
 */
@TableName("t_isv_speaker_config")
public class IsvSpeakerConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 客户编号
     */
    @TableField("UserId")
    private String userId;

    /**
     * 客户密码
     */
    @TableField("UserPassword")
    private String userPassword;

    /**
     * 状态,0-关闭，1-启用
     */
    @TableField("Status")
    private Byte status;

    /**
     * 携带token
     */
    @TableField("Token")
    private String token;

    /**
     * 厂商
     */
    @TableField("Manufacturer")
    private String manufacturer;

    /**
     * token过期时间 分钟
     */
    @TableField("TokenExpire")
    private Integer tokenExpire;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public Integer getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Integer tokenExpire) {
        this.tokenExpire = tokenExpire;
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
        return "IsvSpeakerConfig{" +
            "id=" + id +
            ", isvId=" + isvId +
            ", userId=" + userId +
            ", userPassword=" + userPassword +
            ", status=" + status +
            ", token=" + token +
            ", manufacturer=" + manufacturer +
            ", tokenExpire=" + tokenExpire +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
