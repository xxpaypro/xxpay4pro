package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户餐饮店区域管理表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-05-09
 */
@TableName("t_mch_store_area_manage")
public class MchStoreAreaManage extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 区域ID
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
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 区域名
     */
    @TableField("AreaName")
    private String areaName;

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
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
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
        return "MchStoreAreaManage{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", storeId=" + storeId +
            ", areaName=" + areaName +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
