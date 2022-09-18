package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户会员优惠配置信息
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_mch_member_config")
public class MchMemberConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * mchId
     */
    @TableId("MchId")
    private Long mchId;

    /**
     * 消费金额
     */
    @TableField("ConsumeAmount")
    private Long consumeAmount;

    /**
     * 消费金额满足时赠送积分
     */
    @TableField("ConsumeGivePoints")
    private Long consumeGivePoints;

    /**
     * 储值消费是否可积分 0-否, 1-是
     */
    @TableField("BalancePointsType")
    private Byte balancePointsType;

    /**
     * 会员卡领取方式, 1-免激活领取, 2-人工录入
     */
    @TableField("NewMemberType")
    private Byte newMemberType;

    /**
     * 首次领取赠送规则, 0-无优惠, 1-赠余额, 2-赠积分
     */
    @TableField("NewMemberRule")
    private Byte newMemberRule;

    /**
     * 首次领取返点, 余额/积分
     */
    @TableField("NewMemberGivePoints")
    private Long newMemberGivePoints;

    /**
     * 会员卡封面背景颜色
     */
    @TableField("MemberCardColor")
    private String memberCardColor;

    /**
     * 会员卡名称
     */
    @TableField("MemberCardName")
    private String memberCardName;

    /**
     * 会员卡期限
     */
    @TableField("MemberCardValidTime")
    private String memberCardValidTime;

    /**
     * 会员卡联系电话
     */
    @TableField("MemberCardTel")
    private String memberCardTel;

    /**
     * 会员卡特权说明
     */
    @TableField("MemberCardRoleDesc")
    private String memberCardRoleDesc;

    /**
     * 会员卡使用须知
     */
    @TableField("MemberCardUseDesc")
    private String memberCardUseDesc;

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

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getConsumeAmount() {
        return consumeAmount;
    }

    public void setConsumeAmount(Long consumeAmount) {
        this.consumeAmount = consumeAmount;
    }
    public Long getConsumeGivePoints() {
        return consumeGivePoints;
    }

    public void setConsumeGivePoints(Long consumeGivePoints) {
        this.consumeGivePoints = consumeGivePoints;
    }
    public Byte getBalancePointsType() {
        return balancePointsType;
    }

    public void setBalancePointsType(Byte balancePointsType) {
        this.balancePointsType = balancePointsType;
    }
    public Byte getNewMemberType() {
        return newMemberType;
    }

    public void setNewMemberType(Byte newMemberType) {
        this.newMemberType = newMemberType;
    }
    public Byte getNewMemberRule() {
        return newMemberRule;
    }

    public void setNewMemberRule(Byte newMemberRule) {
        this.newMemberRule = newMemberRule;
    }
    public Long getNewMemberGivePoints() {
        return newMemberGivePoints;
    }

    public void setNewMemberGivePoints(Long newMemberGivePoints) {
        this.newMemberGivePoints = newMemberGivePoints;
    }
    public String getMemberCardColor() {
        return memberCardColor;
    }

    public void setMemberCardColor(String memberCardColor) {
        this.memberCardColor = memberCardColor;
    }
    public String getMemberCardName() {
        return memberCardName;
    }

    public void setMemberCardName(String memberCardName) {
        this.memberCardName = memberCardName;
    }
    public String getMemberCardValidTime() {
        return memberCardValidTime;
    }

    public void setMemberCardValidTime(String memberCardValidTime) {
        this.memberCardValidTime = memberCardValidTime;
    }
    public String getMemberCardTel() {
        return memberCardTel;
    }

    public void setMemberCardTel(String memberCardTel) {
        this.memberCardTel = memberCardTel;
    }
    public String getMemberCardRoleDesc() {
        return memberCardRoleDesc;
    }

    public void setMemberCardRoleDesc(String memberCardRoleDesc) {
        this.memberCardRoleDesc = memberCardRoleDesc;
    }
    public String getMemberCardUseDesc() {
        return memberCardUseDesc;
    }

    public void setMemberCardUseDesc(String memberCardUseDesc) {
        this.memberCardUseDesc = memberCardUseDesc;
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
        return "MchMemberConfig{" +
            "MchId=" + mchId +
            ", consumeAmount=" + consumeAmount +
            ", consumeGivePoints=" + consumeGivePoints +
            ", balancePointsType=" + balancePointsType +
            ", newMemberType=" + newMemberType +
            ", newMemberRule=" + newMemberRule +
            ", newMemberGivePoints=" + newMemberGivePoints +
            ", memberCardColor=" + memberCardColor +
            ", memberCardName=" + memberCardName +
            ", memberCardValidTime=" + memberCardValidTime +
            ", memberCardTel=" + memberCardTel +
            ", memberCardRoleDesc=" + memberCardRoleDesc +
            ", memberCardUseDesc=" + memberCardUseDesc +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
