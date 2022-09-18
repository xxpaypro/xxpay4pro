package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户商品属性表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-30
 */
@TableName("t_mch_goods_props")
public class MchGoodsProps extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品属性ID
     */
    @TableId(value = "PropsId", type = IdType.AUTO)
    private Long propsId;

    /**
     * 商品属性名称
     */
    @TableField("PropsName")
    private String propsName;

    /**
     * 商品属性值
     */
    @TableField("PropsValue")
    private String propsValue;

    /**
     * 商品属性分类ID
     */
    @TableField("PropsCategoryId")
    private Long propsCategoryId;

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

    public Long getPropsId() {
        return propsId;
    }

    public void setPropsId(Long propsId) {
        this.propsId = propsId;
    }
    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName;
    }
    public String getPropsValue() {
        return propsValue;
    }

    public void setPropsValue(String propsValue) {
        this.propsValue = propsValue;
    }
    public Long getPropsCategoryId() {
        return propsCategoryId;
    }

    public void setPropsCategoryId(Long propsCategoryId) {
        this.propsCategoryId = propsCategoryId;
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
        return "MchGoodsProps{" +
            "PropsId=" + propsId +
            ", propsName=" + propsName +
            ", propsValue=" + propsValue +
            ", propsCategoryId=" + propsCategoryId +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
