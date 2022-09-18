package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 门店与云喇叭关联表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-09
 */
@TableName("t_mch_store_speaker")
public class MchStoreSpeaker extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 门店ID
     */
    @TableId("StoreId")
    private Long storeId;

    /**
     * 云喇叭ID
     */
    @TableField("SpeakerId")
    private Long speakerId;

    /**
     * 状态,0-未绑定,1-已绑定, 2-已解绑
     */
    @TableField("Status")
    private Byte status;

    /**
     * 设备验证金额
     */
    @TableField("MoneyCode")
    private Long moneyCode;

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public Long getSpeakerId() {
        return speakerId;
    }

    public void setSpeakerId(Long speakerId) {
        this.speakerId = speakerId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Long getMoneyCode() {
        return moneyCode;
    }

    public void setMoneyCode(Long moneyCode) {
        this.moneyCode = moneyCode;
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
        return "MchStoreSpeaker{" +
            "StoreId=" + storeId +
            ", speakerId=" + speakerId +
            ", status=" + status +
            ", moneyCode=" + moneyCode +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
