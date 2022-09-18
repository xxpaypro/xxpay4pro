package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户退货原因表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_return_reason")
public class MchReturnReason extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 退货原因ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 名称
     */
    @TableField("Content")
    private String content;

    /**
     * 状态 0-禁用 1-启用
     */
    @TableField("Status")
    private Byte status;

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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        return "MchReturnReason{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", content=" + content +
            ", status=" + status +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
