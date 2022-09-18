package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户会员积分流水表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_member_points_history")
public class MemberPointsHistory extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 积分流水ID
     */
    @TableId(value = "PointsHistoryId", type = IdType.AUTO)
    private Long pointsHistoryId;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 卡号
     */
    @TableField("MemberNo")
    private String memberNo;

    /**
     * 变动积分, +增加, -减少
     */
    @TableField("ChangePoints")
    private Long changePoints;

    /**
     * 变更前积分
     */
    @TableField("Points")
    private Long points;

    /**
     * 变更后积分
     */
    @TableField("AfterPoints")
    private Long afterPoints;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 平台订单号
     */
    @TableField("BizOrderId")
    private String bizOrderId;

    /**
     * 积分类型:1-积分商品兑换, 2-充值赠送, 3-消费, 4-退款, 5-开卡赠送, 6-导入
     */
    @TableField("BizType")
    private Byte bizType;

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
     * 创建时间
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UpdateTime")
    private Date updateTime;

    public Long getPointsHistoryId() {
        return pointsHistoryId;
    }

    public void setPointsHistoryId(Long pointsHistoryId) {
        this.pointsHistoryId = pointsHistoryId;
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
    public Long getChangePoints() {
        return changePoints;
    }

    public void setChangePoints(Long changePoints) {
        this.changePoints = changePoints;
    }
    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
    public Long getAfterPoints() {
        return afterPoints;
    }

    public void setAfterPoints(Long afterPoints) {
        this.afterPoints = afterPoints;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public String getBizOrderId() {
        return bizOrderId;
    }

    public void setBizOrderId(String bizOrderId) {
        this.bizOrderId = bizOrderId;
    }
    public Byte getBizType() {
        return bizType;
    }

    public void setBizType(Byte bizType) {
        this.bizType = bizType;
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
        return "MemberPointsHistory{" +
            "PointsHistoryId=" + pointsHistoryId +
            ", memberId=" + memberId +
            ", memberNo=" + memberNo +
            ", changePoints=" + changePoints +
            ", points=" + points +
            ", afterPoints=" + afterPoints +
            ", mchId=" + mchId +
            ", bizOrderId=" + bizOrderId +
            ", bizType=" + bizType +
            ", operatorId=" + operatorId +
            ", operatorName=" + operatorName +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
