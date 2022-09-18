package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 商户会员储值账户表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-21
 */
@TableName("t_member_balance")
public class MemberBalance extends BaseModel {

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
     * 账户可用余额
     */
    @TableField("Balance")
    private Long balance;

    /**
     * 实际储值金额
     */
    @TableField("TotalRechargeAmount")
    private Long totalRechargeAmount;

    /**
     * 系统赠送金额总额
     */
    @TableField("TotalGiveAmount")
    private Long totalGiveAmount;

    /**
     * 总消费金额
     */
    @TableField("TotalConsumeAmount")
    private Long totalConsumeAmount;

    /**
     * 总消费退款金额
     */
    @TableField("TotalRefundAmount")
    private Long totalRefundAmount;

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
    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
    public Long getTotalRechargeAmount() {
        return totalRechargeAmount;
    }

    public void setTotalRechargeAmount(Long totalRechargeAmount) {
        this.totalRechargeAmount = totalRechargeAmount;
    }
    public Long getTotalGiveAmount() {
        return totalGiveAmount;
    }

    public void setTotalGiveAmount(Long totalGiveAmount) {
        this.totalGiveAmount = totalGiveAmount;
    }
    public Long getTotalConsumeAmount() {
        return totalConsumeAmount;
    }

    public void setTotalConsumeAmount(Long totalConsumeAmount) {
        this.totalConsumeAmount = totalConsumeAmount;
    }
    public Long getTotalRefundAmount() {
        return totalRefundAmount;
    }

    public void setTotalRefundAmount(Long totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    @Override
    public String toString() {
        return "MemberBalance{" +
            "MemberId=" + memberId +
            ", memberNo=" + memberNo +
            ", balance=" + balance +
            ", totalRechargeAmount=" + totalRechargeAmount +
            ", totalGiveAmount=" + totalGiveAmount +
            ", totalConsumeAmount=" + totalConsumeAmount +
            ", totalRefundAmount=" + totalRefundAmount +
        "}";
    }
}
