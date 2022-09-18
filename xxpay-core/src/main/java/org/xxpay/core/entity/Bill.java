package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_bill")
public class Bill extends BaseModel<Bill> {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 代理商或商户ID
     *
     * @mbggenerated
     */
    @TableField("InfoId")
    private Long infoId;

    /**
     * 账号类型:1-代理商,2-商户
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 名称
     *
     * @mbggenerated
     */
    @TableField("InfoName")
    private String infoName;

    /**
     * 类型:1-平台账户,2-私有账户
     *
     * @mbggenerated
     */
    @TableField("MchType")
    private Byte mchType;

    /**
     * 账单类型:1-支付对账单,2-代付对账单
     *
     * @mbggenerated
     */
    @TableField("BillType")
    private Byte billType;

    /**
     * 账单时间
     *
     * @mbggenerated
     */
    @TableField("BillDate")
    private Date billDate;

    /**
     * 账单状态:0-未生成,1--已生成
     *
     * @mbggenerated
     */
    private Byte status;

    /**
     * 账单地址
     *
     * @mbggenerated
     */
    @TableField("BillPath")
    private String billPath;

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

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    public Byte getMchType() {
        return mchType;
    }

    public void setMchType(Byte mchType) {
        this.mchType = mchType;
    }

    public Byte getBillType() {
        return billType;
    }

    public void setBillType(Byte billType) {
        this.billType = billType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getBillPath() {
        return billPath;
    }

    public void setBillPath(String billPath) {
        this.billPath = billPath;
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
        sb.append(", infoName=").append(infoName);
        sb.append(", mchType=").append(mchType);
        sb.append(", billType=").append(billType);
        sb.append(", billDate=").append(billDate);
        sb.append(", status=").append(status);
        sb.append(", billPath=").append(billPath);
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
        Bill other = (Bill) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
            && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
            && (this.getInfoName() == null ? other.getInfoName() == null : this.getInfoName().equals(other.getInfoName()))
            && (this.getMchType() == null ? other.getMchType() == null : this.getMchType().equals(other.getMchType()))
            && (this.getBillType() == null ? other.getBillType() == null : this.getBillType().equals(other.getBillType()))
            && (this.getBillDate() == null ? other.getBillDate() == null : this.getBillDate().equals(other.getBillDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getBillPath() == null ? other.getBillPath() == null : this.getBillPath().equals(other.getBillPath()))
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
        result = prime * result + ((getInfoName() == null) ? 0 : getInfoName().hashCode());
        result = prime * result + ((getMchType() == null) ? 0 : getMchType().hashCode());
        result = prime * result + ((getBillType() == null) ? 0 : getBillType().hashCode());
        result = prime * result + ((getBillDate() == null) ? 0 : getBillDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getBillPath() == null) ? 0 : getBillPath().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}