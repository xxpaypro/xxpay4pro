package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 会员与微信openid关联表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-23
 */
@TableName("t_member_openid_rela")
public class MemberOpenidRela extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId("MemberId")
    private Long memberId;

    /**
     * 微信OpenId
     */
    @TableField("WxOpenId")
    private String wxOpenId;

    /**
     * 微信OpenId来源，1-餐饮小程序 
     */
    @TableField("WxOpenIdFrom")
    private Byte wxOpenIdFrom;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
    public Byte getWxOpenIdFrom() {
        return wxOpenIdFrom;
    }

    public void setWxOpenIdFrom(Byte wxOpenIdFrom) {
        this.wxOpenIdFrom = wxOpenIdFrom;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    @Override
    public String toString() {
        return "MemberOpenidRela{" +
            "MemberId=" + memberId +
            ", wxOpenId=" + wxOpenId +
            ", wxOpenIdFrom=" + wxOpenIdFrom +
            ", mchId=" + mchId +
        "}";
    }
}
