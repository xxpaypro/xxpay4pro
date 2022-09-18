package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@TableName("t_pay_product")
public class PayProduct extends BaseModel {
    /**
     * 产品ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 产品名称
     *
     * @mbggenerated
     */
    @TableField("ProductName")
    private String productName;

    /**
     * 支付类型
     *
     * @mbggenerated
     */
    @TableField("PayType")
    private String payType;

    /**
     * 状态,0-关闭,1-开启
     *
     * @mbggenerated
     */
    @TableField("Status")
    private Byte status;

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

    /**
     * 代理商费率,百分比
     *
     * @mbggenerated
     */
    @TableField("AgentRate")
    private BigDecimal agentRate;

    /**
     * 商户费率,百分比
     *
     * @mbggenerated
     */
    @TableField("MchRate")
    private BigDecimal mchRate;

    /**
     * 接口模式,1-单独,2-轮询
     *
     * @mbggenerated
     */
    @TableField("IfMode")
    private Byte ifMode;

    /**
     * 支付通道ID
     *
     * @mbggenerated
     */
    @TableField("PayPassageId")
    private Integer payPassageId;

    /**
     * 支付通道账户ID
     *
     * @mbggenerated
     */
    @TableField("PayPassageAccountId")
    private Integer payPassageAccountId;

    /**
     * 轮询配置参数,json字符串
     *
     * @mbggenerated
     */
    @TableField("PollParam")
    private String pollParam;

    /**
     * 产品类型:1-收款,2-充值
     *
     * @mbggenerated
     */
    @TableField("ProductType")
    private Byte productType;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    public BigDecimal getMchRate() {
        return mchRate;
    }

    public void setMchRate(BigDecimal mchRate) {
        this.mchRate = mchRate;
    }

    public Byte getIfMode() {
        return ifMode;
    }

    public void setIfMode(Byte ifMode) {
        this.ifMode = ifMode;
    }

    public Integer getPayPassageId() {
        return payPassageId;
    }

    public void setPayPassageId(Integer payPassageId) {
        this.payPassageId = payPassageId;
    }

    public Integer getPayPassageAccountId() {
        return payPassageAccountId;
    }

    public void setPayPassageAccountId(Integer payPassageAccountId) {
        this.payPassageAccountId = payPassageAccountId;
    }

    public String getPollParam() {
        return pollParam;
    }

    public void setPollParam(String pollParam) {
        this.pollParam = pollParam;
    }

    public Byte getProductType() {
        return productType;
    }

    public void setProductType(Byte productType) {
        this.productType = productType;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", productName=").append(productName);
        sb.append(", payType=").append(payType);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", agentRate=").append(agentRate);
        sb.append(", mchRate=").append(mchRate);
        sb.append(", ifMode=").append(ifMode);
        sb.append(", payPassageId=").append(payPassageId);
        sb.append(", payPassageAccountId=").append(payPassageAccountId);
        sb.append(", pollParam=").append(pollParam);
        sb.append(", productType=").append(productType);
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
        PayProduct other = (PayProduct) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getProductName() == null ? other.getProductName() == null : this.getProductName().equals(other.getProductName()))
            && (this.getPayType() == null ? other.getPayType() == null : this.getPayType().equals(other.getPayType()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getAgentRate() == null ? other.getAgentRate() == null : this.getAgentRate().equals(other.getAgentRate()))
            && (this.getMchRate() == null ? other.getMchRate() == null : this.getMchRate().equals(other.getMchRate()))
            && (this.getIfMode() == null ? other.getIfMode() == null : this.getIfMode().equals(other.getIfMode()))
            && (this.getPayPassageId() == null ? other.getPayPassageId() == null : this.getPayPassageId().equals(other.getPayPassageId()))
            && (this.getPayPassageAccountId() == null ? other.getPayPassageAccountId() == null : this.getPayPassageAccountId().equals(other.getPayPassageAccountId()))
            && (this.getPollParam() == null ? other.getPollParam() == null : this.getPollParam().equals(other.getPollParam()))
            && (this.getProductType() == null ? other.getProductType() == null : this.getProductType().equals(other.getProductType()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getProductName() == null) ? 0 : getProductName().hashCode());
        result = prime * result + ((getPayType() == null) ? 0 : getPayType().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getAgentRate() == null) ? 0 : getAgentRate().hashCode());
        result = prime * result + ((getMchRate() == null) ? 0 : getMchRate().hashCode());
        result = prime * result + ((getIfMode() == null) ? 0 : getIfMode().hashCode());
        result = prime * result + ((getPayPassageId() == null) ? 0 : getPayPassageId().hashCode());
        result = prime * result + ((getPayPassageAccountId() == null) ? 0 : getPayPassageAccountId().hashCode());
        result = prime * result + ((getPollParam() == null) ? 0 : getPollParam().hashCode());
        result = prime * result + ((getProductType() == null) ? 0 : getProductType().hashCode());
        return result;
    }
}
