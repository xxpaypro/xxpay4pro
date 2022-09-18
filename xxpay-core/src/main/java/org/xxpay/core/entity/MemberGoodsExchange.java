package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 积分商品会员兑换表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_member_goods_exchange")
public class MemberGoodsExchange extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 兑换ID
     */
    @TableId(value = "ExchangeId", type = IdType.AUTO)
    private Long exchangeId;

    /**
     * 提货码
     */
    @TableField("ExchangeNo")
    private String exchangeNo;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 会员卡号
     */
    @TableField("MemberNo")
    private String memberNo;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 积分商品ID
     */
    @TableField("GoodsId")
    private Long goodsId;

    /**
     * 商品名称
     */
    @TableField("GoodsName")
    private String goodsName;

    /**
     * 使用积分
     */
    @TableField("Points")
    private Long points;

    /**
     * 状态:0-未兑换 1-已兑换, 2-已作废
     */
    @TableField("Status")
    private Byte status;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

    /**
     * 操作员ID
     */
    @TableField("OperatorId")
    private String operatorId;

    /**
     * 操作员名称
     */
    @TableField("OperatorName")
    private String operatorName;

    /**
     * 兑换时间
     */
    @TableField("ExchangeTime")
    private Date exchangeTime;

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

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
    public String getExchangeNo() {
        return exchangeNo;
    }

    public void setExchangeNo(String exchangeNo) {
        this.exchangeNo = exchangeNo;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
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
    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public Date getExchangeTime() {
        return exchangeTime;
    }

    public void setExchangeTime(Date exchangeTime) {
        this.exchangeTime = exchangeTime;
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
        return "MemberGoodsExchange{" +
            "ExchangeId=" + exchangeId +
            ", exchangeNo=" + exchangeNo +
            ", memberId=" + memberId +
            ", memberNo=" + memberNo +
            ", mchId=" + mchId +
            ", goodsId=" + goodsId +
            ", goodsName=" + goodsName +
            ", points=" + points +
            ", status=" + status +
            ", remark=" + remark +
            ", operatorId=" + operatorId +
            ", operatorName=" + operatorName +
            ", exchangeTime=" + exchangeTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
