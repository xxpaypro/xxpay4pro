package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户餐饮店营业及配送信息表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_store_takeout")
public class MchStoreTakeout extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
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
     * 是否营业 0-否 1-是
     */
    @TableField("IsOpen")
    private Byte isOpen;

    /**
     * 营业时间
     */
    @TableField("ShopOpenTime")
    private String shopOpenTime;

    /**
     * 服务项目，1-堂食 2-外卖
     */
    @TableField("ServiceItem")
    private Byte serviceItem;

    /**
     * 起送费用，单位：分
     */
    @TableField("DeliveryCost")
    private Long deliveryCost;

    /**
     * 配送费用，单位：分
     */
    @TableField("DistributionCost")
    private Long distributionCost;

    /**
     * 免费配送所需订单金额，单位：分
     */
    @TableField("FreeDistribution")
    private Long freeDistribution;

    /**
     * 配送范围，最大半径 单位：KM
     */
    @TableField("DistributionScope")
    private String distributionScope;

    /**
     * 送达时间，单位：分钟
     */
    @TableField("DistributionTime")
    private String distributionTime;

    /**
     * 配送平台,1-自配送
     */
    @TableField("DistributioPlatform")
    private Byte distributioPlatform;

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
    public Byte getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Byte isOpen) {
        this.isOpen = isOpen;
    }
    public String getShopOpenTime() {
        return shopOpenTime;
    }

    public void setShopOpenTime(String shopOpenTime) {
        this.shopOpenTime = shopOpenTime;
    }
    public Byte getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(Byte serviceItem) {
        this.serviceItem = serviceItem;
    }
    public Long getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(Long deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
    public Long getDistributionCost() {
        return distributionCost;
    }

    public void setDistributionCost(Long distributionCost) {
        this.distributionCost = distributionCost;
    }
    public Long getFreeDistribution() {
        return freeDistribution;
    }

    public void setFreeDistribution(Long freeDistribution) {
        this.freeDistribution = freeDistribution;
    }
    public String getDistributionScope() {
        return distributionScope;
    }

    public void setDistributionScope(String distributionScope) {
        this.distributionScope = distributionScope;
    }
    public String getDistributionTime() {
        return distributionTime;
    }

    public void setDistributionTime(String distributionTime) {
        this.distributionTime = distributionTime;
    }
    public Byte getDistributioPlatform() {
        return distributioPlatform;
    }

    public void setDistributioPlatform(Byte distributioPlatform) {
        this.distributioPlatform = distributioPlatform;
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
        return "MchStoreTakeout{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", isOpen=" + isOpen +
            ", shopOpenTime=" + shopOpenTime +
            ", serviceItem=" + serviceItem +
            ", deliveryCost=" + deliveryCost +
            ", distributionCost=" + distributionCost +
            ", freeDistribution=" + freeDistribution +
            ", distributionScope=" + distributionScope +
            ", distributionTime=" + distributionTime +
            ", distributioPlatform=" + distributioPlatform +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
