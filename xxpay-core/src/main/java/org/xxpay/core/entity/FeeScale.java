package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import org.xxpay.core.common.vo.ExtConfigVO;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_fee_scale")
public class FeeScale extends BaseModel {

    @TableField(exist = false)
    private FeeRiskConfig feeRiskConfig;
    public FeeRiskConfig getFeeRiskConfig() {
        return feeRiskConfig;
    }
    public void setFeeRiskConfig(FeeRiskConfig feeRiskConfig) {
        this.feeRiskConfig = feeRiskConfig;
    }

    @TableField(exist = false)
    private ExtConfigVO extConfigVO;
    public ExtConfigVO getExtConfigVO() {
        return extConfigVO;
    }
    public void setExtConfigVO(ExtConfigVO extConfigVO) {
        this.extConfigVO = extConfigVO;
    }

    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 代理商或商户ID
     *
     * @mbggenerated
     */
    @TableField("InfoId")
    private Long infoId;

    /**
     * 类型: 1-商户 2-代理商 3-平台
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 收费产品类型 1-支付 2-代付 3-线下充值
     *
     * @mbggenerated
     */
    @TableField("ProductType")
    private Byte productType;

    /**
     * 关联来源产品ID, 支付类关联产品表；代付类关联代付通道表
     *
     * @mbggenerated
     */
    @TableField("RefProductId")
    private Integer refProductId;

    /**
     * 收费标准 1-每笔 2-年 3-月 4-季度
     *
     * @mbggenerated
     */
    @TableField("FeeScale")
    private Byte feeScale;

    /**
     * 收费标准梯度
     *
     * @mbggenerated
     */
    @TableField("FeeScaleStep")
    private Integer feeScaleStep;

    /**
     * 按每笔收费标准的收费方式 1-按固定金额 2-按比例
     *
     * @mbggenerated
     */
    @TableField("SingleFeeType")
    private Byte singleFeeType;

    /**
     * 收费金额或比例 金额单位：分， 比例单位：%
     *
     * @mbggenerated
     */
    @TableField("Fee")
    private BigDecimal fee;

    /**
     * 扩展配置 可设置获取支付子账户的获取方式等参数
     *
     * @mbggenerated
     */
    @TableField("ExtConfig")
    private String extConfig;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

    /**
     * 是否默认,0-否,1-是
     *
     * @mbggenerated
     */
    @TableField("IsDefault")
    private Byte isDefault;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    @TableField("UpdateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getInfoId() {
        return infoId;
    }

    public void setInfoId(Long infoId) {
        this.infoId = infoId;
    }

    public Byte getInfoType() {
        return infoType;
    }

    public void setInfoType(Byte infoType) {
        this.infoType = infoType;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
        this.setExtConfigVO(ExtConfigVO.getExtConfigVO(this.extConfig, this.productType));
    }

    public Integer getRefProductId() {
        return refProductId;
    }

    public void setRefProductId(Integer refProductId) {
        this.refProductId = refProductId;
    }

    public Byte getFeeScale() {
        return feeScale;
    }

    public void setFeeScale(Byte feeScale) {
        this.feeScale = feeScale;
    }

    public Integer getFeeScaleStep() {
        return feeScaleStep;
    }

    public void setFeeScaleStep(Integer feeScaleStep) {
        this.feeScaleStep = feeScaleStep;
    }

    public Byte getSingleFeeType() {
        return singleFeeType;
    }

    public void setSingleFeeType(Byte singleFeeType) {
        this.singleFeeType = singleFeeType;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getExtConfig() {
        return extConfig;
    }

    public void setExtConfig(String extConfig) {
        this.extConfig = extConfig;
        this.setExtConfigVO(ExtConfigVO.getExtConfigVO(this.extConfig, this.productType));
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", productType=").append(productType);
        sb.append(", refProductId=").append(refProductId);
        sb.append(", feeScale=").append(feeScale);
        sb.append(", feeScaleStep=").append(feeScaleStep);
        sb.append(", singleFeeType=").append(singleFeeType);
        sb.append(", fee=").append(fee);
        sb.append(", extConfig=").append(extConfig);
        sb.append(", status=").append(status);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        FeeScale other = (FeeScale) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
            && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
            && (this.getProductType() == null ? other.getProductType() == null : this.getProductType().equals(other.getProductType()))
            && (this.getRefProductId() == null ? other.getRefProductId() == null : this.getRefProductId().equals(other.getRefProductId()))
            && (this.getFeeScale() == null ? other.getFeeScale() == null : this.getFeeScale().equals(other.getFeeScale()))
            && (this.getFeeScaleStep() == null ? other.getFeeScaleStep() == null : this.getFeeScaleStep().equals(other.getFeeScaleStep()))
            && (this.getSingleFeeType() == null ? other.getSingleFeeType() == null : this.getSingleFeeType().equals(other.getSingleFeeType()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getExtConfig() == null ? other.getExtConfig() == null : this.getExtConfig().equals(other.getExtConfig()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getProductType() == null) ? 0 : getProductType().hashCode());
        result = prime * result + ((getRefProductId() == null) ? 0 : getRefProductId().hashCode());
        result = prime * result + ((getFeeScale() == null) ? 0 : getFeeScale().hashCode());
        result = prime * result + ((getFeeScaleStep() == null) ? 0 : getFeeScaleStep().hashCode());
        result = prime * result + ((getSingleFeeType() == null) ? 0 : getSingleFeeType().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getExtConfig() == null) ? 0 : getExtConfig().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
