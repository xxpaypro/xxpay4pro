package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 购物车表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-25
 */
@TableName("t_mch_shopping_cart")
public class MchShoppingCart extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车ID
     */
    @TableId(value = "CartId", type = IdType.AUTO)
    private Long cartId;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 商品ID
     */
    @TableField("GoodsId")
    private Long goodsId;

    /**
     * 购物车商品是否选中 0-否 1-是
     */
    @TableField("IsGoodsSelected")
    private Byte isGoodsSelected;

    /**
     * 商品数量
     */
    @TableField("GoodsNum")
    private Integer goodsNum;

    /**
     * 选择的属性ID
     */
    @TableField("GoodsProps")
    private String goodsProps;

    /**
     * 所属行业 1-餐饮 2-电商
     */
    @TableField("IndustryType")
    private Byte industryType;

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

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Byte getIsGoodsSelected() {
        return isGoodsSelected;
    }

    public void setIsGoodsSelected(Byte isGoodsSelected) {
        this.isGoodsSelected = isGoodsSelected;
    }
    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }
    public String getGoodsProps() {
        return goodsProps;
    }

    public void setGoodsProps(String goodsProps) {
        this.goodsProps = goodsProps;
    }
    public Byte getIndustryType() {
        return industryType;
    }

    public void setIndustryType(Byte industryType) {
        this.industryType = industryType;
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
        return "MchShoppingCart{" +
            "CartId=" + cartId +
            ", memberId=" + memberId +
            ", goodsId=" + goodsId +
            ", isGoodsSelected=" + isGoodsSelected +
            ", goodsNum=" + goodsNum +
            ", goodsProps=" + goodsProps +
            ", industryType=" + industryType +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
