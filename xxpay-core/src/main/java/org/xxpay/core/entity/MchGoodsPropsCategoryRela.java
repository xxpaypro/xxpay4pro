package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户商品与属性分类关联表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_goods_props_category_rela")
public class MchGoodsPropsCategoryRela extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId("GoodsId")
    private Long goodsId;

    /**
     * 属性分类ID
     */
    @TableField("PropsCategoryId")
    private Long propsCategoryId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Long getPropsCategoryId() {
        return propsCategoryId;
    }

    public void setPropsCategoryId(Long propsCategoryId) {
        this.propsCategoryId = propsCategoryId;
    }

    @Override
    public String toString() {
        return "MchGoodsPropsCategoryRela{" +
            "GoodsId=" + goodsId +
            ", propsCategoryId=" + propsCategoryId +
        "}";
    }
}
