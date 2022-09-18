package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商品分类表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-03
 */
@TableName("t_mch_goods_category")
public class MchGoodsCategory extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品分类ID
     */
    @TableId(value = "CategoryId", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 商品分类名称
     */
    @TableField("CategoryName")
    private String categoryName;

    /**
     * 分类页广告图
     */
    @TableField("CategoryImg")
    private String categoryImg;

    /**
     * 跳转链接
     */
    @TableField("JumpUrl")
    private String jumpUrl;

    /**
     * 分类图标
     */
    @TableField("CategoryIcon")
    private String categoryIcon;

    /**
     * 父分类ID，0表示一级分类
     */
    @TableField("ParentCategoryId")
    private Long parentCategoryId;

    /**
     * 1-餐饮 2-电商
     */
    @TableField("AuthFrom")
    private Byte authFrom;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }
    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }
    public String getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(String categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
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
        return "MchGoodsCategory{" +
            "CategoryId=" + categoryId +
            ", categoryName=" + categoryName +
            ", categoryImg=" + categoryImg +
            ", jumpUrl=" + jumpUrl +
            ", categoryIcon=" + categoryIcon +
            ", parentCategoryId=" + parentCategoryId +
            ", authFrom=" + authFrom +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
