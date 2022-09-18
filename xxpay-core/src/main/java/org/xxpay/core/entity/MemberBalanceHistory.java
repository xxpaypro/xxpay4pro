package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户会员储值流水表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_member_balance_history")
public class MemberBalanceHistory extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 账户流水ID
     */
    @TableId(value = "BalanceHistoryId", type = IdType.AUTO)
    private Long balanceHistoryId;

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
     * 变动金额, +增加, -减少
     */
    @TableField("ChangeAmount")
    private Long changeAmount;

    /**
     * 变更前账户余额
     */
    @TableField("Balance")
    private Long balance;

    /**
     * 变更后账户余额
     */
    @TableField("AfterBalance")
    private Long afterBalance;

    /**
     * 赠送金额（仅做记录）
     */
    @TableField("GiveAmount")
    private Long giveAmount;

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
     * 交易类型:1-充值, 2-消费, 3-退款, 4-导入, 5-赠送
     */
    @TableField("BizType")
    private Byte bizType;

    /**
     * 支付方式:1-微信, 2-支付宝, 3-储值卡, 4-导入
     */
    @TableField("PayType")
    private Byte payType;

    /**
     * 来源 1-PC, 2-ANDROID, 3-IOS, 4-H5
     */
    @TableField("PageOrigin")
    private Byte pageOrigin;

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

    public Long getBalanceHistoryId() {
        return balanceHistoryId;
    }

    public void setBalanceHistoryId(Long balanceHistoryId) {
        this.balanceHistoryId = balanceHistoryId;
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
    public Long getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(Long changeAmount) {
        this.changeAmount = changeAmount;
    }
    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }
    public Long getAfterBalance() {
        return afterBalance;
    }

    public void setAfterBalance(Long afterBalance) {
        this.afterBalance = afterBalance;
    }
    public Long getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(Long giveAmount) {
        this.giveAmount = giveAmount;
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
    public Byte getPayType() {
        return payType;
    }

    public void setPayType(Byte payType) {
        this.payType = payType;
    }
    public Byte getPageOrigin() {
        return pageOrigin;
    }

    public void setPageOrigin(Byte pageOrigin) {
        this.pageOrigin = pageOrigin;
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
        return "MemberBalanceHistory{" +
            "BalanceHistoryId=" + balanceHistoryId +
            ", memberId=" + memberId +
            ", memberNo=" + memberNo +
            ", changeAmount=" + changeAmount +
            ", balance=" + balance +
            ", afterBalance=" + afterBalance +
            ", giveAmount=" + giveAmount +
            ", mchId=" + mchId +
            ", bizOrderId=" + bizOrderId +
            ", bizType=" + bizType +
            ", payType=" + payType +
            ", pageOrigin=" + pageOrigin +
            ", operatorId=" + operatorId +
            ", operatorName=" + operatorName +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
