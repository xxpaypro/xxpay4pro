package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户积分商品表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_mch_points_goods")
public class MchPointsGoods extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 积分商品ID
     */
    @TableId(value = "GoodsId", type = IdType.AUTO)
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField("GoodsName")
    private String goodsName;

    /**
     * 所需积分
     */
    @TableField("Points")
    private Long points;

    /**
     * 商品价格，单位：分
     */
    @TableField("GoodsPrice")
    private Long goodsPrice;

    /**
     * 图片路径
     */
    @TableField("ImgPath")
    private String imgPath;

    /**
     * 状态 0-下架 1-上架
     */
    @TableField("Status")
    private Byte status;

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
     * 单用户兑换限制数量 -1不限制
     */
    @TableField("SingleMemberLimit")
    private Integer singleMemberLimit;

    /**
     * 活动开始时间
     */
    @TableField("BeginTime")
    private Date beginTime;

    /**
     * 活动结束时间
     */
    @TableField("EndTime")
    private Date endTime;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 是否限制门店 0-不限门店 1-限制门店,详见门店关联表
     */
    @TableField("StoreLimitType")
    private Byte storeLimitType;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

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
    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
    public Long getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Long goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
    public Integer getSingleMemberLimit() {
        return singleMemberLimit;
    }

    public void setSingleMemberLimit(Integer singleMemberLimit) {
        this.singleMemberLimit = singleMemberLimit;
    }
    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Byte getStoreLimitType() {
        return storeLimitType;
    }

    public void setStoreLimitType(Byte storeLimitType) {
        this.storeLimitType = storeLimitType;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "MchPointsGoods{" +
            "GoodsId=" + goodsId +
            ", goodsName=" + goodsName +
            ", points=" + points +
            ", goodsPrice=" + goodsPrice +
            ", imgPath=" + imgPath +
            ", status=" + status +
            ", stockLimitType=" + stockLimitType +
            ", stockNum=" + stockNum +
            ", singleMemberLimit=" + singleMemberLimit +
            ", beginTime=" + beginTime +
            ", endTime=" + endTime +
            ", mchId=" + mchId +
            ", storeLimitType=" + storeLimitType +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
