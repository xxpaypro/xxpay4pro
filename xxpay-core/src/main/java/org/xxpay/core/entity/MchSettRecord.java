package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_mch_sett_record")
public class MchSettRecord implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 名称
     *
     * @mbggenerated
     */
    @TableField("Name")
    private String name;

    /**
     * 结算发起方式:1-手工结算,2-自动结算
     *
     * @mbggenerated
     */
    @TableField("SettMode")
    private Byte settMode;

    /**
     * 类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    @TableField("MchType")
    private Byte mchType;

    /**
     * 结算日期
     *
     * @mbggenerated
     */
    @TableField("SettDate")
    private Date settDate;

    /**
     * 结算金额
     *
     * @mbggenerated
     */
    @TableField("SettAmount")
    private Long settAmount;

    /**
     * 结算手续费
     *
     * @mbggenerated
     */
    @TableField("SettFee")
    private Long settFee;

    /**
     * 结算打款金额
     *
     * @mbggenerated
     */
    @TableField("RemitAmount")
    private Long remitAmount;

    /**
     * 打款类型:1-银行转账,2-微信转账,3-支付宝转账
     *
     * @mbggenerated
     */
    @TableField("RemitType")
    private Byte remitType;

    /**
     * 商户收款账号信息,json存储
     *
     * @mbggenerated
     */
    @TableField("MchRemitInfo")
    private String mchRemitInfo;

    /**
     * 结算状态:1-等待审核,2-已审核,3-审核不通过,4-打款中,5-打款成功,6-打款失败
     *
     * @mbggenerated
     */
    @TableField("SettStatus")
    private Byte settStatus;

    /**
     * 备注
     *
     * @mbggenerated
     */
    @TableField("Remark")
    private String remark;

    /**
     * 打款备注
     *
     * @mbggenerated
     */
    @TableField("RemitRemark")
    private String remitRemark;

    /**
     * 操作员ID
     *
     * @mbggenerated
     */
    @TableField("OperatorId")
    private Long operatorId;

    /**
     * 操作员姓名
     *
     * @mbggenerated
     */
    @TableField("OperatorName")
    private String operatorName;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getSettMode() {
        return settMode;
    }

    public void setSettMode(Byte settMode) {
        this.settMode = settMode;
    }

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public Date getSettDate() {
        return settDate;
    }

    public void setSettDate(Date settDate) {
        this.settDate = settDate;
    }

    public Long getSettAmount() {
        return settAmount;
    }

    public void setSettAmount(Long settAmount) {
        this.settAmount = settAmount;
    }

    public Long getSettFee() {
        return settFee;
    }

    public void setSettFee(Long settFee) {
        this.settFee = settFee;
    }

    public Long getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(Long remitAmount) {
        this.remitAmount = remitAmount;
    }

    public Byte getRemitType() {
        return remitType;
    }

    public void setRemitType(Byte remitType) {
        this.remitType = remitType;
    }

    public String getMchRemitInfo() {
        return mchRemitInfo;
    }

    public void setMchRemitInfo(String mchRemitInfo) {
        this.mchRemitInfo = mchRemitInfo;
    }

    public Byte getSettStatus() {
        return settStatus;
    }

    public void setSettStatus(Byte settStatus) {
        this.settStatus = settStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemitRemark() {
        return remitRemark;
    }

    public void setRemitRemark(String remitRemark) {
        this.remitRemark = remitRemark;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
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
        sb.append(", mchId=").append(mchId);
        sb.append(", name=").append(name);
        sb.append(", settMode=").append(settMode);
        sb.append(", mchType=").append(mchType);
        sb.append(", settDate=").append(settDate);
        sb.append(", settAmount=").append(settAmount);
        sb.append(", settFee=").append(settFee);
        sb.append(", remitAmount=").append(remitAmount);
        sb.append(", remitType=").append(remitType);
        sb.append(", mchRemitInfo=").append(mchRemitInfo);
        sb.append(", settStatus=").append(settStatus);
        sb.append(", remark=").append(remark);
        sb.append(", remitRemark=").append(remitRemark);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", operatorName=").append(operatorName);
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
        MchSettRecord other = (MchSettRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSettMode() == null ? other.getSettMode() == null : this.getSettMode().equals(other.getSettMode()))
            && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
            && (this.getSettDate() == null ? other.getSettDate() == null : this.getSettDate().equals(other.getSettDate()))
            && (this.getSettAmount() == null ? other.getSettAmount() == null : this.getSettAmount().equals(other.getSettAmount()))
            && (this.getSettFee() == null ? other.getSettFee() == null : this.getSettFee().equals(other.getSettFee()))
            && (this.getRemitAmount() == null ? other.getRemitAmount() == null : this.getRemitAmount().equals(other.getRemitAmount()))
            && (this.getRemitType() == null ? other.getRemitType() == null : this.getRemitType().equals(other.getRemitType()))
            && (this.getMchRemitInfo() == null ? other.getMchRemitInfo() == null : this.getMchRemitInfo().equals(other.getMchRemitInfo()))
            && (this.getSettStatus() == null ? other.getSettStatus() == null : this.getSettStatus().equals(other.getSettStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRemitRemark() == null ? other.getRemitRemark() == null : this.getRemitRemark().equals(other.getRemitRemark()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getOperatorName() == null ? other.getOperatorName() == null : this.getOperatorName().equals(other.getOperatorName()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSettMode() == null) ? 0 : getSettMode().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getSettDate() == null) ? 0 : getSettDate().hashCode());
        result = prime * result + ((getSettAmount() == null) ? 0 : getSettAmount().hashCode());
        result = prime * result + ((getSettFee() == null) ? 0 : getSettFee().hashCode());
        result = prime * result + ((getRemitAmount() == null) ? 0 : getRemitAmount().hashCode());
        result = prime * result + ((getRemitType() == null) ? 0 : getRemitType().hashCode());
        result = prime * result + ((getMchRemitInfo() == null) ? 0 : getMchRemitInfo().hashCode());
        result = prime * result + ((getSettStatus() == null) ? 0 : getSettStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRemitRemark() == null) ? 0 : getRemitRemark().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getOperatorName() == null) ? 0 : getOperatorName().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
