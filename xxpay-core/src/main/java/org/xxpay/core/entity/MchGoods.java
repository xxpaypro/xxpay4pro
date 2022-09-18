package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户商品表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-03
 */
@TableName("t_mch_goods")
public class MchGoods extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商品ID
     */
    @TableId(value = "GoodsId", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField("GoodsName")
    private String goodsName;

    /**
     * 商品分类ID
     */
    @TableField("CategoryId")
    private Long categoryId;

    /**
     * 商品分类名称
     */
    @TableField("CategoryName")
    private String categoryName;

    /**
     * 子商品ID
     */
    @TableField("SubGoodsId")
    private String subGoodsId;

    /**
     * 所属行业 1-餐饮商品 2-电商商品
     */
    @TableField("IndustryType")
    private Byte industryType;

    /**
     * 商品类型 1-单一商品 2-组合商品
     */
    @TableField("GoodsType")
    private Byte goodsType;

    /**
     * 商品所属模块，0-普通商品 1-新品 2-热卖 3-促销 4-限时抢购 5-会员专享
     */
    @TableField("GoodsModule")
    private String goodsModule;

    /**
     * 商品描述
     */
    @TableField("GoodsDesc")
    private String goodsDesc;

    /**
     * 图文详情
     */
    @TableField("GraphicDesc")
    private String graphicDesc;

    /**
     * 商品售价，单位：分
     */
    @TableField("CellingPrice")
    private Long cellingPrice;

    /**
     * 商品进价，单位：分
     */
    @TableField("BuyingPrice")
    private Long buyingPrice;

    /**
     * 商品会员价，单位：分
     */
    @TableField("MemberPrice")
    private Long memberPrice;

    /**
     * 单位
     */
    @TableField("Unit")
    private String unit;

    /**
     * 是否限制库存 1-不限库存 2-限制
     */
    @TableField("StockLimitType")
    private Byte stockLimitType;

    /**
     * 库存数量
     */
    @TableField("StockNum")
    private Long stockNum;

    /**
     * 实际销量
     */
    @TableField("ActualSaleNum")
    private Long actualSaleNum;

    /**
     * 虚拟数量
     */
    @TableField("VirtualSaleNum")
    private Long virtualSaleNum;

    /**
     * 浏览量
     */
    @TableField("BrowseNumber")
    private Integer browseNumber;

    /**
     * 评价数
     */
    @TableField("EvaluationNumber")
    private Integer evaluationNumber;

    /**
     * 生产日期
     */
    @TableField("ProducedBeginTime")
    private Date producedBeginTime;

    /**
     * 保质期，单位：天
     */
    @TableField("Expiration")
    private Integer expiration;

    /**
     * 供应商
     */
    @TableField("Supplier")
    private String supplier;

    /**
     * 主图图片路径
     */
    @TableField("ImgPathMain")
    private String imgPathMain;

    /**
     * 其他图片路径,最多四张
     */
    @TableField("ImgPathMore")
    private String imgPathMore;

    /**
     * 状态 0-下架 1-上架 2-售罄
     */
    @TableField("Status")
    private Byte status;

    /**
     * 品牌
     */
    @TableField("Brand")
    private String brand;

    /**
     * 商品标签
     */
    @TableField("GoodsTag")
    private String goodsTag;

    /**
     * 商品条码
     */
    @TableField("BarCode")
    private String barCode;

    /**
     * 是否为精品推荐商品，0-否 1-是
     */
    @TableField("IsRecommend")
    private Byte isRecommend;

    /**
     * 精品推荐商品排序值
     */
    @TableField("RecommendSort")
    private Integer recommendSort;

    /**
     * 是否添加属性 0-未添加 1-已添加,详见属性关联表
     */
    @TableField("GoodsPropsType")
    private Byte goodsPropsType;

    /**
     * 是否限制门店 0-不限门店 1-限制门店,详见门店关联表
     */
    @TableField("StoreLimitType")
    private Byte storeLimitType;

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

    /**
     * 小程序商品库的商品ID
     */
    @TableField("MiniGoodsId")
    private Long miniGoodsId;

    /**
     * 小程序商品库的商品审核单ID
     */
    @TableField("MiniAuditId")
    private Long miniAuditId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
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
    public String getSubGoodsId() {
        return subGoodsId;
    }

    public void setSubGoodsId(String subGoodsId) {
        this.subGoodsId = subGoodsId;
    }
    public Byte getIndustryType() {
        return industryType;
    }

    public void setIndustryType(Byte industryType) {
        this.industryType = industryType;
    }
    public Byte getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Byte goodsType) {
        this.goodsType = goodsType;
    }
    public String getGoodsModule() {
        return goodsModule;
    }

    public void setGoodsModule(String goodsModule) {
        this.goodsModule = goodsModule;
    }
    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }
    public String getGraphicDesc() {
        return graphicDesc;
    }

    public void setGraphicDesc(String graphicDesc) {
        this.graphicDesc = graphicDesc;
    }
    public Long getCellingPrice() {
        return cellingPrice;
    }

    public void setCellingPrice(Long cellingPrice) {
        this.cellingPrice = cellingPrice;
    }
    public Long getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Long buyingPrice) {
        this.buyingPrice = buyingPrice;
    }
    public Long getMemberPrice() {
        return memberPrice;
    }

    public void setMemberPrice(Long memberPrice) {
        this.memberPrice = memberPrice;
    }
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Byte getStockLimitType() {
        return stockLimitType;
    }

    public void setStockLimitType(Byte stockLimitType) {
        this.stockLimitType = stockLimitType;
    }
    public Long getStockNum() {
        return stockNum;
    }

    public void setStockNum(Long stockNum) {
        this.stockNum = stockNum;
    }
    public Long getActualSaleNum() {
        return actualSaleNum;
    }

    public void setActualSaleNum(Long actualSaleNum) {
        this.actualSaleNum = actualSaleNum;
    }
    public Long getVirtualSaleNum() {
        return virtualSaleNum;
    }

    public void setVirtualSaleNum(Long virtualSaleNum) {
        this.virtualSaleNum = virtualSaleNum;
    }
    public Integer getBrowseNumber() {
        return browseNumber;
    }

    public void setBrowseNumber(Integer browseNumber) {
        this.browseNumber = browseNumber;
    }
    public Integer getEvaluationNumber() {
        return evaluationNumber;
    }

    public void setEvaluationNumber(Integer evaluationNumber) {
        this.evaluationNumber = evaluationNumber;
    }
    public Date getProducedBeginTime() {
        return producedBeginTime;
    }

    public void setProducedBeginTime(Date producedBeginTime) {
        this.producedBeginTime = producedBeginTime;
    }
    public Integer getExpiration() {
        return expiration;
    }

    public void setExpiration(Integer expiration) {
        this.expiration = expiration;
    }
    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getImgPathMain() {
        return imgPathMain;
    }

    public void setImgPathMain(String imgPathMain) {
        this.imgPathMain = imgPathMain;
    }
    public String getImgPathMore() {
        return imgPathMore;
    }

    public void setImgPathMore(String imgPathMore) {
        this.imgPathMore = imgPathMore;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getGoodsTag() {
        return goodsTag;
    }

    public void setGoodsTag(String goodsTag) {
        this.goodsTag = goodsTag;
    }
    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
    public Byte getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Byte isRecommend) {
        this.isRecommend = isRecommend;
    }
    public Integer getRecommendSort() {
        return recommendSort;
    }

    public void setRecommendSort(Integer recommendSort) {
        this.recommendSort = recommendSort;
    }
    public Byte getGoodsPropsType() {
        return goodsPropsType;
    }

    public void setGoodsPropsType(Byte goodsPropsType) {
        this.goodsPropsType = goodsPropsType;
    }
    public Byte getStoreLimitType() {
        return storeLimitType;
    }

    public void setStoreLimitType(Byte storeLimitType) {
        this.storeLimitType = storeLimitType;
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
    public Long getMiniGoodsId() {
        return miniGoodsId;
    }

    public void setMiniGoodsId(Long miniGoodsId) {
        this.miniGoodsId = miniGoodsId;
    }
    public Long getMiniAuditId() {
        return miniAuditId;
    }

    public void setMiniAuditId(Long miniAuditId) {
        this.miniAuditId = miniAuditId;
    }

    @Override
    public String toString() {
        return "MchGoods{" +
            "GoodsId=" + goodsId +
            ", goodsName=" + goodsName +
            ", categoryId=" + categoryId +
            ", categoryName=" + categoryName +
            ", subGoodsId=" + subGoodsId +
            ", industryType=" + industryType +
            ", goodsType=" + goodsType +
            ", goodsModule=" + goodsModule +
            ", goodsDesc=" + goodsDesc +
            ", graphicDesc=" + graphicDesc +
            ", cellingPrice=" + cellingPrice +
            ", buyingPrice=" + buyingPrice +
            ", memberPrice=" + memberPrice +
            ", unit=" + unit +
            ", stockLimitType=" + stockLimitType +
            ", stockNum=" + stockNum +
            ", actualSaleNum=" + actualSaleNum +
            ", virtualSaleNum=" + virtualSaleNum +
            ", browseNumber=" + browseNumber +
            ", evaluationNumber=" + evaluationNumber +
            ", producedBeginTime=" + producedBeginTime +
            ", expiration=" + expiration +
            ", supplier=" + supplier +
            ", imgPathMain=" + imgPathMain +
            ", imgPathMore=" + imgPathMore +
            ", status=" + status +
            ", brand=" + brand +
            ", goodsTag=" + goodsTag +
            ", barCode=" + barCode +
            ", isRecommend=" + isRecommend +
            ", recommendSort=" + recommendSort +
            ", goodsPropsType=" + goodsPropsType +
            ", storeLimitType=" + storeLimitType +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", miniGoodsId=" + miniGoodsId +
            ", miniAuditId=" + miniAuditId +
        "}";
    }
}
