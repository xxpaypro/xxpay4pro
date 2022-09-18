package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户授权小程序版本管理记录表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-29
 */
@TableName("t_mch_mini_version")
public class MchMiniVersion extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户Id
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商Id
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 小程序授权来源，1-点餐小程序
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 授权小程序AppId
     */
    @TableField("AuthAppId")
    private String authAppId;

    /**
     * 版本号
     */
    @TableField("MiniVersion")
    private String miniVersion;

    /**
     * 版本描述
     */
    @TableField("MiniDesc")
    private String miniDesc;

    /**
     * 开发者
     */
    @TableField("Develop")
    private String develop;

    /**
     * 代码提交时间
     */
    @TableField("CommitTime")
    private Date commitTime;

    /**
     * 提交审核时间
     */
    @TableField("AuditTime")
    private Date auditTime;

    /**
     * 发布时间
     */
    @TableField("ReleaseTime")
    private Date releaseTime;

    /**
     * 审核编号
     */
    @TableField("AuditId")
    private String auditId;

    /**
     * 小程序状态:  1-开发版本 2-审核版本 3-线上版本
     */
    @TableField("VersionStatus")
    private Byte versionStatus;

    /**
     * 审核状态:  0-审核成功 1-审核被拒绝 2-审核中 3-已撤回 4-审核延后
     */
    @TableField("AuditStatus")
    private Byte auditStatus;

    /**
     * 拒绝/延后原因
     */
    @TableField("RefusedReason")
    private String refusedReason;

    /**
     * 审核失败的小程序截图示例
     */
    @TableField("RefusedScreenShot")
    private String refusedScreenShot;

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
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public String getAuthAppId() {
        return authAppId;
    }

    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }
    public String getMiniVersion() {
        return miniVersion;
    }

    public void setMiniVersion(String miniVersion) {
        this.miniVersion = miniVersion;
    }
    public String getMiniDesc() {
        return miniDesc;
    }

    public void setMiniDesc(String miniDesc) {
        this.miniDesc = miniDesc;
    }
    public String getDevelop() {
        return develop;
    }

    public void setDevelop(String develop) {
        this.develop = develop;
    }
    public Date getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }
    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }
    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
    public String getAuditId() {
        return auditId;
    }

    public void setAuditId(String auditId) {
        this.auditId = auditId;
    }
    public Byte getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(Byte versionStatus) {
        this.versionStatus = versionStatus;
    }
    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getRefusedReason() {
        return refusedReason;
    }

    public void setRefusedReason(String refusedReason) {
        this.refusedReason = refusedReason;
    }
    public String getRefusedScreenShot() {
        return refusedScreenShot;
    }

    public void setRefusedScreenShot(String refusedScreenShot) {
        this.refusedScreenShot = refusedScreenShot;
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
        return "MchMiniVersion{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", authFrom=" + authFrom +
            ", authAppId=" + authAppId +
            ", miniVersion=" + miniVersion +
            ", miniDesc=" + miniDesc +
            ", develop=" + develop +
            ", commitTime=" + commitTime +
            ", auditTime=" + auditTime +
            ", releaseTime=" + releaseTime +
            ", auditId=" + auditId +
            ", versionStatus=" + versionStatus +
            ", auditStatus=" + auditStatus +
            ", refusedReason=" + refusedReason +
            ", refusedScreenShot=" + refusedScreenShot +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
