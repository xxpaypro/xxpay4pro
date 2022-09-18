package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户小程序轮播图-门店关联表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-27
 */
@TableName("t_mch_store_banner_rela")
public class MchStoreBannerRela extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 轮播图ID
     */
    @TableId("BannerId")
    private Long bannerId;

    /**
     * 门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    public Long getBannerId() {
        return bannerId;
    }

    public void setBannerId(Long bannerId) {
        this.bannerId = bannerId;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "MchStoreBannerRela{" +
            "BannerId=" + bannerId +
            ", storeId=" + storeId +
        "}";
    }
}
