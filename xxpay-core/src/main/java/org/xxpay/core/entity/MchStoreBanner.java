package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户小程序轮播图配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-25
 */
@TableName("t_mch_store_banner")
public class MchStoreBanner extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "BannerId", type = IdType.AUTO)
    private Long bannerId;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 轮播图名称
     */
    @TableField("BannerName")
    private String bannerName;

    /**
     * 所属小程序：1-点餐  2-商城
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 显示位置：1-商户小程序首页
     */
    @TableField("ShowType")
    private Byte showType;

    /**
     * 状态,0-关闭，1-启用
     */
    @TableField("Status")
    private Byte status;

    /**
     * 投放门店 0-全部门店 1-部分门店投放，详见门店关联表
     */
    @TableField("PutStoreLimitType")
    private Byte putStoreLimitType;

    /**
     * 资源路径
     */
    @TableField("MediaPath")
    private String mediaPath;

    /**
     * 跳转链接
     */
    @TableField("AdvertUrl")
    private String advertUrl;

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

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public String getBannerName() {
        return bannerName;
    }

    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Byte getShowType() {
        return showType;
    }

    public void setShowType(Byte showType) {
        this.showType = showType;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Byte getPutStoreLimitType() {
        return putStoreLimitType;
    }

    public void setPutStoreLimitType(Byte putStoreLimitType) {
        this.putStoreLimitType = putStoreLimitType;
    }
    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    public String getAdvertUrl() {
        return advertUrl;
    }

    public void setAdvertUrl(String advertUrl) {
        this.advertUrl = advertUrl;
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
        return "MchStoreBanner{" +
                "BannerId=" + bannerId +
                ", mchId=" + mchId +
                ", bannerName=" + bannerName +
                ", authFrom=" + authFrom +
                ", showType=" + showType +
                ", status=" + status +
                ", putStoreLimitType=" + putStoreLimitType +
                ", mediaPath=" + mediaPath +
                ", advertUrl=" + advertUrl +
                ", sortNum=" + sortNum +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
