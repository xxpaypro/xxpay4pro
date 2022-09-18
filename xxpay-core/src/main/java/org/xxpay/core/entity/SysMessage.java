package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 系统消息表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-10-17
 */
@TableName("t_sys_message")
public class SysMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 消息名称
     */
    private String title;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 状态 0：隐藏, 1：显示
     */
    private Byte status;

    /**
     * 创建者ID
     */
    @TableField("createUserId")
    private Long createUserId;

    /**
     * 服务商消息状态：0：隐藏, 1：显示
     */
    @TableField("isvStatus")
    private Byte isvStatus;

    /**
     * 代理商消息状态：0：隐藏, 1：显示
     */
    @TableField("agentStatus")
    private Byte agentStatus;

    /**
     * 商户消息状态：0：隐藏, 1：显示
     */
    @TableField("mchStatus")
    private Byte mchStatus;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }
    public Byte getIsvStatus() {
        return isvStatus;
    }

    public void setIsvStatus(Byte isvStatus) {
        this.isvStatus = isvStatus;
    }
    public Byte getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Byte agentStatus) {
        this.agentStatus = agentStatus;
    }
    public Byte getMchStatus() {
        return mchStatus;
    }

    public void setMchStatus(Byte mchStatus) {
        this.mchStatus = mchStatus;
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
        return "SysMessage{" +
            "id=" + id +
            ", title=" + title +
            ", message=" + message +
            ", status=" + status +
            ", createUserId=" + createUserId +
            ", isvStatus=" + isvStatus +
            ", agentStatus=" + agentStatus +
            ", mchStatus=" + mchStatus +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
