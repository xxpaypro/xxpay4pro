package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户会员积分表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@TableName("t_member_points")
public class MemberPoints extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId("MemberId")
    private Long memberId;

    /**
     * 会员卡号
     */
    @TableField("MemberNo")
    private String memberNo;

    /**
     * 账户可用积分
     */
    @TableField("Points")
    private Long points;

    /**
     * 实际总累计积分
     */
    @TableField("TotalAddPoints")
    private Long totalAddPoints;

    /**
     * 系统赠送积分总额
     */
    @TableField("TotalGivePoints")
    private Long totalGivePoints;

    /**
     * 总支出积分
     */
    @TableField("TotalConsumePoints")
    private Long totalConsumePoints;

    /**
     * 总消费退款积分
     */
    @TableField("TotalRefundPoints")
    private Long totalRefundPoints;

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
    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }
    public Long getTotalAddPoints() {
        return totalAddPoints;
    }

    public void setTotalAddPoints(Long totalAddPoints) {
        this.totalAddPoints = totalAddPoints;
    }
    public Long getTotalGivePoints() {
        return totalGivePoints;
    }

    public void setTotalGivePoints(Long totalGivePoints) {
        this.totalGivePoints = totalGivePoints;
    }
    public Long getTotalConsumePoints() {
        return totalConsumePoints;
    }

    public void setTotalConsumePoints(Long totalConsumePoints) {
        this.totalConsumePoints = totalConsumePoints;
    }
    public Long getTotalRefundPoints() {
        return totalRefundPoints;
    }

    public void setTotalRefundPoints(Long totalRefundPoints) {
        this.totalRefundPoints = totalRefundPoints;
    }

    @Override
    public String toString() {
        return "MemberPoints{" +
            "MemberId=" + memberId +
            ", memberNo=" + memberNo +
            ", points=" + points +
            ", totalAddPoints=" + totalAddPoints +
            ", totalGivePoints=" + totalGivePoints +
            ", totalConsumePoints=" + totalConsumePoints +
            ", totalRefundPoints=" + totalRefundPoints +
        "}";
    }
}
