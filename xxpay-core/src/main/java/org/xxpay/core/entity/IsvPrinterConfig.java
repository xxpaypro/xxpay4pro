package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 服务商与打印机配置表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@TableName("t_isv_printer_config")
public class IsvPrinterConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 服务商ID
     */
    @TableId("IsvId")
    private Long isvId;

    /**
     * 打印机key
     */
    @TableField("AccessKey")
    private String accessKey;

    /**
     * 打印机秘钥
     */
    @TableField("AccessSecret")
    private String accessSecret;

    /**
     * 状态,0-关闭,1-开启
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
     * token失效时间
     */
    @TableField("TokenExpire")
    private Date tokenExpire;

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

    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
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
    public Date getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Date tokenExpire) {
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
        return "IsvPrinterConfig{" +
            "IsvId=" + isvId +
            ", accessKey=" + accessKey +
            ", accessSecret=" + accessSecret +
            ", status=" + status +
            ", token=" + token +
            ", manufacturer=" + manufacturer +
            ", tokenExpire=" + tokenExpire +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
