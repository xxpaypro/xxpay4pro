package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 会员金刚区配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-17
 */
@TableName("t_mch_mini_vajra")
public class MchMiniVajra extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "VajraId", type = IdType.AUTO)
    private Long vajraId;

    /**
     * 金刚区名称
     */
    @TableField("VajraName")
    private String vajraName;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 所属小程序：1-点餐  2-商城
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 状态,0-关闭，1-启用
     */
    @TableField("Status")
    private Byte status;

    /**
     * icon资源路径
     */
    @TableField("MediaPath")
    private String mediaPath;

    /**
     * 跳转链接类型：1-内部链接  2-外部链接
     */
    @TableField("JumpUrlType")
    private Byte jumpUrlType;

    /**
     * 跳转链接
     */
    @TableField("JumpUrl")
    private String jumpUrl;

    /**
     * 排序数值, 降序顺序
     */
    @TableField("SortNum")
    private Integer sortNum;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

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

    public Long getVajraId() {
        return vajraId;
    }

    public void setVajraId(Long vajraId) {
        this.vajraId = vajraId;
    }
    public String getVajraName() {
        return vajraName;
    }

    public void setVajraName(String vajraName) {
        this.vajraName = vajraName;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    public Byte getJumpUrlType() {
        return jumpUrlType;
    }

    public void setJumpUrlType(Byte jumpUrlType) {
        this.jumpUrlType = jumpUrlType;
    }
    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "MchMiniVajra{" +
            "VajraId=" + vajraId +
            ", vajraName=" + vajraName +
            ", mchId=" + mchId +
            ", authFrom=" + authFrom +
            ", status=" + status +
            ", mediaPath=" + mediaPath +
            ", jumpUrlType=" + jumpUrlType +
            ", jumpUrl=" + jumpUrl +
            ", sortNum=" + sortNum +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
