package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商广告配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-29
 */
@TableName("t_isv_advert_config")
public class IsvAdvertConfig extends BaseModel {

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
     * 广告名称
     */
    @TableField("AdvertName")
    private String advertName;

    /**
     * 显示位置：1-移动端支付成功页, 2-刷脸设备支付成功页, 3-商家APP轮播图片, 4-刷脸设备投屏广告
     */
    @TableField("ShowType")
    private Byte showType;

    /**
     * 状态,0-关闭，1-启用
     */
    @TableField("Status")
    private Byte status;

    /**
     * 投放商户,-1:所有商户，1-一级子商户，2-二级子商户，3-三级子商户
     */
    @TableField("PutMch")
    private Byte putMch;

    /**
     * 投放省 -1全部省
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    /**
     * 投放市 -1全部市
     */
    @TableField("CityCode")
    private Integer cityCode;

    /**
     * 投放县 -1全部县
     */
    @TableField("AreaCode")
    private Integer areaCode;

    /**
     * 1-图片, 2-视频
     */
    @TableField("MediaType")
    private Byte mediaType;

    /**
     * 资源路径
     */
    @TableField("MediaPath")
    private String mediaPath;

    /**
     * 广告跳转链接
     */
    @TableField("AdvertUrl")
    private String advertUrl;

    /**
     * 开始时间
     */
    @TableField("BeginTime")
    private Date beginTime;

    /**
     * 结束时间
     */
    @TableField("EndTime")
    private Date endTime;

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
    public String getAdvertName() {
        return advertName;
    }

    public void setAdvertName(String advertName) {
        this.advertName = advertName;
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
    public Byte getPutMch() {
        return putMch;
    }

    public void setPutMch(Byte putMch) {
        this.putMch = putMch;
    }
    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }
    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
    }
    public Byte getMediaType() {
        return mediaType;
    }

    public void setMediaType(Byte mediaType) {
        this.mediaType = mediaType;
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
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
        return "IsvAdvertConfig{" +
                "id=" + id +
                ", isvId=" + isvId +
                ", advertName=" + advertName +
                ", showType=" + showType +
                ", status=" + status +
                ", putMch=" + putMch +
                ", provinceCode=" + provinceCode +
                ", cityCode=" + cityCode +
                ", areaCode=" + areaCode +
                ", mediaType=" + mediaType +
                ", mediaPath=" + mediaPath +
                ", advertUrl=" + advertUrl +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", sortNum=" + sortNum +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
