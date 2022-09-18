package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户运费模版表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_freight_template")
public class MchFreightTemplate extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 运费模版ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 模版名称
     */
    @TableField("TemplateName")
    private String templateName;

    /**
     * 计价方式 1-统一收费 2-按体积收费 3-按重量收费
     */
    @TableField("ValuationType")
    private Byte valuationType;

    /**
     * 运费价格，单位：分
     */
    @TableField("Valuation")
    private Long valuation;

    /**
     * 初始体积
     */
    @TableField("FirstNum")
    private Integer firstNum;

    /**
     * 初始体积收费，单位：分
     */
    @TableField("FirstNumValuation")
    private Long firstNumValuation;

    /**
     * 超出体积
     */
    @TableField("OtherNum")
    private Integer otherNum;

    /**
     * 超出体积收费，单位：分
     */
    @TableField("OtherNumValuation")
    private Long otherNumValuation;

    /**
     * 首重重量
     */
    @TableField("FirstWeight")
    private Integer firstWeight;

    /**
     * 首重收费，单位：分
     */
    @TableField("FirstWeightValuation")
    private Long firstWeightValuation;

    /**
     * 续重重量
     */
    @TableField("OtherWeight")
    private Integer otherWeight;

    /**
     * 续重收费，单位：分
     */
    @TableField("OtherWeightValuation")
    private Long otherWeightValuation;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    public Byte getValuationType() {
        return valuationType;
    }

    public void setValuationType(Byte valuationType) {
        this.valuationType = valuationType;
    }
    public Long getValuation() {
        return valuation;
    }

    public void setValuation(Long valuation) {
        this.valuation = valuation;
    }
    public Integer getFirstNum() {
        return firstNum;
    }

    public void setFirstNum(Integer firstNum) {
        this.firstNum = firstNum;
    }
    public Long getFirstNumValuation() {
        return firstNumValuation;
    }

    public void setFirstNumValuation(Long firstNumValuation) {
        this.firstNumValuation = firstNumValuation;
    }
    public Integer getOtherNum() {
        return otherNum;
    }

    public void setOtherNum(Integer otherNum) {
        this.otherNum = otherNum;
    }
    public Long getOtherNumValuation() {
        return otherNumValuation;
    }

    public void setOtherNumValuation(Long otherNumValuation) {
        this.otherNumValuation = otherNumValuation;
    }
    public Integer getFirstWeight() {
        return firstWeight;
    }

    public void setFirstWeight(Integer firstWeight) {
        this.firstWeight = firstWeight;
    }
    public Long getFirstWeightValuation() {
        return firstWeightValuation;
    }

    public void setFirstWeightValuation(Long firstWeightValuation) {
        this.firstWeightValuation = firstWeightValuation;
    }
    public Integer getOtherWeight() {
        return otherWeight;
    }

    public void setOtherWeight(Integer otherWeight) {
        this.otherWeight = otherWeight;
    }
    public Long getOtherWeightValuation() {
        return otherWeightValuation;
    }

    public void setOtherWeightValuation(Long otherWeightValuation) {
        this.otherWeightValuation = otherWeightValuation;
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
        return "MchFreightTemplate{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", templateName=" + templateName +
            ", valuationType=" + valuationType +
            ", valuation=" + valuation +
            ", firstNum=" + firstNum +
            ", firstNumValuation=" + firstNumValuation +
            ", otherNum=" + otherNum +
            ", otherNumValuation=" + otherNumValuation +
            ", firstWeight=" + firstWeight +
            ", firstWeightValuation=" + firstWeightValuation +
            ", otherWeight=" + otherWeight +
            ", otherWeightValuation=" + otherWeightValuation +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
