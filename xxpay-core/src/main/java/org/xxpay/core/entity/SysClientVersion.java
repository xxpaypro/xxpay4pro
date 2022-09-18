package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 客户端更新表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-14
 */
@TableName("t_sys_client_version")
public class SysClientVersion extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * version ID
     */
    @TableId(value = "Vid", type = IdType.AUTO)
    private Integer vid;

    /**
     * 版本名称
     */
    @TableField("VersionName")
    private String versionName;

    /**
     * 版本序列号
     */
    @TableField("versionSN")
    private String versionSN;

    /**
     * 版本描述信息
     */
    @TableField("versionDesc")
    private String versionDesc;

    /**
     * 是否需要强制更新 1-是, 0-否
     */
    @TableField("ForceUpdate")
    private Byte forceUpdate;

    /**
     * 下载地址
     */
    @TableField("DownloadUrl")
    private String downloadUrl;

    /**
     * 文件大小，单位：M 
     */
    @TableField("FileSize")
    private String fileSize;

    /**
     * 客户端类型：FACE_CLIENT: 刷脸设备, MCH_APP: 商家手机客户端, PC_PLUGIN: 商家PC收银插件
     */
    @TableField("ClientType")
    private String clientType;

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

    public Integer getVid() {
        return vid;
    }

    public void setVid(Integer vid) {
        this.vid = vid;
    }
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public String getVersionSN() {
        return versionSN;
    }

    public void setVersionSN(String versionSN) {
        this.versionSN = versionSN;
    }
    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
    public Byte getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Byte forceUpdate) {
        this.forceUpdate = forceUpdate;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
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
        return "SysClientVersion{" +
            "Vid=" + vid +
            ", versionName=" + versionName +
            ", versionSN=" + versionSN +
            ", versionDesc=" + versionDesc +
            ", forceUpdate=" + forceUpdate +
            ", downloadUrl=" + downloadUrl +
            ", fileSize=" + fileSize +
            ", clientType=" + clientType +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
