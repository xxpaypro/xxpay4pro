package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户小程序配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-16
 */
@TableName("t_mch_mini_config")
public class MchMiniConfig extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ConfigId", type = IdType.AUTO)
    private Long configId;

    /**
     * 配置键名
     */
    @TableField("ConfigCode")
    private String configCode;

    /**
     * 配置名称
     */
    @TableField("ConfigName")
    private String configName;

    /**
     * 图标
     */
    @TableField("Icon")
    private String icon;

    /**
     * 配置值
     */
    @TableField("Value")
    private String value;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 所属小程序：1-点餐  2-商城
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 状态,0-关闭，1-启用
     */
    @TableField("Status")
    private Byte status;

    /**
     * 排序数值
     */
    @TableField("SortNum")
    private Integer sortNum;

    /**
     * 存储的值是否为草稿，0-正式  1-草稿
     */
    @TableField("IsDraft")
    private Byte isDraft;

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

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }
    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }
    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }
    public Byte getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(Byte isDraft) {
        this.isDraft = isDraft;
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
        return "MchMiniConfig{" +
            "ConfigId=" + configId +
            ", configCode=" + configCode +
            ", configName=" + configName +
            ", icon=" + icon +
            ", value=" + value +
            ", mchId=" + mchId +
            ", authFrom=" + authFrom +
            ", status=" + status +
            ", sortNum=" + sortNum +
            ", isDraft=" + isDraft +
            ", remark=" + remark +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
