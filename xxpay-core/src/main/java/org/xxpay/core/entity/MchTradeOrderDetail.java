package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商城订单详情表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-09
 */
@TableName("t_mch_trade_order_detail")
public class MchTradeOrderDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 订单详情ID
     */
    @TableId(value = "OrderId", type = IdType.AUTO)
    private Long orderId;

    /**
     * 交易单号
     */
    @TableField("TradeOrderId")
    private String tradeOrderId;

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
     * 商品名称
     */
    @TableField("GoodsName")
    private String goodsName;

    /**
     * 商品描述
     */
    @TableField("GoodsDesc")
    private String goodsDesc;

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
     * 商品数量
     */
    @TableField("GoodsNum")
    private Integer goodsNum;

    /**
     * 选择的属性ID集合
     */
    @TableField("GoodsProps")
    private String goodsProps;

    /**
     * 选择的属性分类名称
     */
    @TableField("GoodsPropsName")
    private String goodsPropsName;

    /**
     * 选择的属性值
     */
    @TableField("GoodsPropsValue")
    private String goodsPropsValue;

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
     * 主图图片路径
     */
    @TableField("ImgPathMain")
    private String imgPathMain;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getTradeOrderId() {
        return tradeOrderId;
    }

    public void setTradeOrderId(String tradeOrderId) {
        this.tradeOrderId = tradeOrderId;
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
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
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
    public String getGoodsPropsName() {
        return goodsPropsName;
    }

    public void setGoodsPropsName(String goodsPropsName) {
        this.goodsPropsName = goodsPropsName;
    }
    public String getGoodsPropsValue() {
        return goodsPropsValue;
    }

    public void setGoodsPropsValue(String goodsPropsValue) {
        this.goodsPropsValue = goodsPropsValue;
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
    public String getImgPathMain() {
        return imgPathMain;
    }

    public void setImgPathMain(String imgPathMain) {
        this.imgPathMain = imgPathMain;
    }

    @Override
    public String toString() {
        return "MchTradeOrderDetail{" +
            "OrderId=" + orderId +
            ", tradeOrderId=" + tradeOrderId +
            ", memberId=" + memberId +
            ", goodsId=" + goodsId +
            ", goodsName=" + goodsName +
            ", goodsDesc=" + goodsDesc +
            ", cellingPrice=" + cellingPrice +
            ", buyingPrice=" + buyingPrice +
            ", memberPrice=" + memberPrice +
            ", goodsNum=" + goodsNum +
            ", goodsProps=" + goodsProps +
            ", goodsPropsName=" + goodsPropsName +
            ", goodsPropsValue=" + goodsPropsValue +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", imgPathMain=" + imgPathMain +
        "}";
    }
}
