package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户会员表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-08
 */
@TableName("t_member")
public class Member extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(value = "MemberId", type = IdType.AUTO)
    private Long memberId;

    /**
     * 卡号
     */
    @TableField("MemberNo")
    private String memberNo;

    /**
     * 会员名称
     */
    @TableField("MemberName")
    private String memberName;

    /**
     * 手机号
     */
    @TableField("Tel")
    private String tel;

    /**
     * 性别:0-女 1-男
     */
    @TableField("Sex")
    private Byte sex;

    /**
     * 状态:0-停用 1-启用 2-未绑定手机号
     */
    @TableField("Status")
    private Byte status;

    /**
     * 生日
     */
    @TableField("Birthday")
    private Date birthday;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 微信OpenId
     */
    @TableField("WxOpenId")
    private String wxOpenId;

    /**
     * 支付宝UserId
     */
    @TableField("AlipayUserId")
    private String alipayUserId;

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
     * 头像路径
     */
    @TableField("Avatar")
    private String avatar;

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
    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
    public String getAlipayUserId() {
        return alipayUserId;
    }

    public void setAlipayUserId(String alipayUserId) {
        this.alipayUserId = alipayUserId;
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
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Member{" +
            "MemberId=" + memberId +
            ", memberNo=" + memberNo +
            ", memberName=" + memberName +
            ", tel=" + tel +
            ", sex=" + sex +
            ", status=" + status +
            ", birthday=" + birthday +
            ", remark=" + remark +
            ", mchId=" + mchId +
            ", wxOpenId=" + wxOpenId +
            ", alipayUserId=" + alipayUserId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", avatar=" + avatar +
        "}";
    }
}
