package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户会员充值规则表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_mch_member_recharge_rule")
public class MchMemberRechargeRule extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 规则ID
     */
    @TableId(value = "RuleId", type = IdType.AUTO)
    private Long ruleId;

    /**
     * mchId
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 充值金额
     */
    @TableField("RechargeAmount")
    private Long rechargeAmount;

    /**
     * 赠送规则, 1-赠余额, 2-赠积分, 3-送优惠券
     */
    @TableField("RuleType")
    private Byte ruleType;

    /**
     * 赠送返点, 余额/积分
     */
    @TableField("GivePoints")
    private Long givePoints;

    /**
     * 赠送优惠券ID
     */
    @TableField("GiveCouponId")
    private Long giveCouponId;

    /**
     * 推荐标识: 0-否, 1-是
     */
    @TableField("FirstFlag")
    private Byte firstFlag;

    /**
     * 状态:0-暂停使用 1-正常 
     */
    @TableField("Status")
    private Byte status;

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

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(Long rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }
    public Byte getRuleType() {
        return ruleType;
    }

    public void setRuleType(Byte ruleType) {
        this.ruleType = ruleType;
    }
    public Long getGivePoints() {
        return givePoints;
    }

    public void setGivePoints(Long givePoints) {
        this.givePoints = givePoints;
    }
    public Long getGiveCouponId() {
        return giveCouponId;
    }

    public void setGiveCouponId(Long giveCouponId) {
        this.giveCouponId = giveCouponId;
    }
    public Byte getFirstFlag() {
        return firstFlag;
    }

    public void setFirstFlag(Byte firstFlag) {
        this.firstFlag = firstFlag;
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
        return "MchMemberRechargeRule{" +
            "RuleId=" + ruleId +
            ", mchId=" + mchId +
            ", rechargeAmount=" + rechargeAmount +
            ", ruleType=" + ruleType +
            ", givePoints=" + givePoints +
            ", giveCouponId=" + giveCouponId +
            ", firstFlag=" + firstFlag +
            ", status=" + status +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
