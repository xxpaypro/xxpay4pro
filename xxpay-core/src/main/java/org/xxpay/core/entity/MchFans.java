package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户粉丝表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-23
 */
@TableName("t_mch_fans")
public class MchFans extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * mchId
     */
    @TableId("MchId")
    private Long mchId;

    /**
     * 微信openId
     */
    @TableField("WxOpenId")
    private String wxOpenId;

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
    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
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
        return "MchFans{" +
            "MchId=" + mchId +
            ", wxOpenId=" + wxOpenId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
