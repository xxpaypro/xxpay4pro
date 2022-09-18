package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商结算配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-21
 */
@TableName("t_isv_sett_config")
public class IsvSettConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * isvId
     */
    @TableId("IsvId")
    private Long isvId;

    /**
     * 结算周期类型 1-按照时间, 2-指定每月日期
     */
    @TableField("SettDateType")
    private Byte settDateType;

    /**
     * 结算设置天数
     */
    @TableField("SettSetDay")
    private Integer settSetDay;

    /**
     * 上次跑批时间
     */
    @TableField("PrevSettDate")
    private Date prevSettDate;

    /**
     * 下次跑批时间
     */
    @TableField("NextSettDate")
    private Date nextSettDate;

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

    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Byte getSettDateType() {
        return settDateType;
    }

    public void setSettDateType(Byte settDateType) {
        this.settDateType = settDateType;
    }
    public Integer getSettSetDay() {
        return settSetDay;
    }

    public void setSettSetDay(Integer settSetDay) {
        this.settSetDay = settSetDay;
    }
    public Date getPrevSettDate() {
        return prevSettDate;
    }

    public void setPrevSettDate(Date prevSettDate) {
        this.prevSettDate = prevSettDate;
    }
    public Date getNextSettDate() {
        return nextSettDate;
    }

    public void setNextSettDate(Date nextSettDate) {
        this.nextSettDate = nextSettDate;
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
        return "IsvSettConfig{" +
            "IsvId=" + isvId +
            ", settDateType=" + settDateType +
            ", settSetDay=" + settSetDay +
            ", prevSettDate=" + prevSettDate +
            ", nextSettDate=" + nextSettDate +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
