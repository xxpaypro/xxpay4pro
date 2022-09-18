package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户用户配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-12
 */
@TableName("t_mch_app_config")
public class MchAppConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("UserId")
    private Long userId;

    /**
     * app通知(0-关闭,1-开启)
     */
    @TableField("AppPush")
    private Byte appPush;

    /**
     * app语音提醒(0-关闭,1-开启)
     */
    @TableField("AppVoice")
    private Byte appVoice;

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
    public Byte getAppPush() {
        return appPush;
    }

    public void setAppPush(Byte appPush) {
        this.appPush = appPush;
    }
    public Byte getAppVoice() {
        return appVoice;
    }

    public void setAppVoice(Byte appVoice) {
        this.appVoice = appVoice;
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
        return "MchAppConfig{" +
            "UserId=" + userId +
            ", appPush=" + appPush +
            ", appVoice=" + appVoice +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
