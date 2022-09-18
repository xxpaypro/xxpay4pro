package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户商品属性分类表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_goods_props_category")
public class MchGoodsPropsCategory extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品属性分类ID
     */
    @TableId(value = "PropsCategoryId", type = IdType.AUTO)
    private Long propsCategoryId;

    /**
     * 商品属性分类名称
     */
    @TableField("PropsCategoryName")
    private String propsCategoryName;

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
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UpdateTime")
    private Date updateTime;

    public Long getPropsCategoryId() {
        return propsCategoryId;
    }

    public void setPropsCategoryId(Long propsCategoryId) {
        this.propsCategoryId = propsCategoryId;
    }
    public String getPropsCategoryName() {
        return propsCategoryName;
    }

    public void setPropsCategoryName(String propsCategoryName) {
        this.propsCategoryName = propsCategoryName;
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
        return "MchGoodsPropsCategory{" +
            "PropsCategoryId=" + propsCategoryId +
            ", propsCategoryName=" + propsCategoryName +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
