package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户商品与门店关联表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_goods_store_rela")
public class MchGoodsStoreRela extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId("GoodsId")
    private Long goodsId;

    /**
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "MchGoodsStoreRela{" +
            "GoodsId=" + goodsId +
            ", storeId=" + storeId +
        "}";
    }
}
