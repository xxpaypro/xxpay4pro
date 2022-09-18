package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户用户登录设备信息
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-05
 */
@TableName("t_mch_device")
public class MchDevice extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 设备号CID
     */
    @TableId("Cid")
    private String cid;

    /**
     * 设备信息
     */
    @TableField("DeviceInfo")
    private String deviceInfo;

    /**
     * 用户ID
     */
    @TableField("UserId")
    private Long userId;

    /**
     * 登录时间
     */
    @TableField("LoginTime")
    private Date loginTime;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "MchDevice{" +
            "Cid=" + cid +
            ", deviceInfo=" + deviceInfo +
            ", userId=" + userId +
            ", loginTime=" + loginTime +
        "}";
    }
}
