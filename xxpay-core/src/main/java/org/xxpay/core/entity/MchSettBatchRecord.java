package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_mch_sett_batch_record")
public class MchSettBatchRecord implements Serializable {
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
     * 结算记录ID
     *
     * @mbggenerated
     */
    @TableField("SettRecordId")
    private Long settRecordId;

    /**
     * 收款银行户名
     *
     * @mbggenerated
     */
    @TableField("BankAccountName")
    private String bankAccountName;

    /**
     * 收款银行账号
     *
     * @mbggenerated
     */
    @TableField("BankAccountNumber")
    private String bankAccountNumber;

    /**
     * 开户银行
     *
     * @mbggenerated
     */
    @TableField("BankName")
    private String bankName;

    /**
     * 开户网点名称
     *
     * @mbggenerated
     */
    @TableField("BankNetName")
    private String bankNetName;

    /**
     * 开户行所在省
     *
     * @mbggenerated
     */
    @TableField("BankProvince")
    private String bankProvince;

    /**
     * 开户行所在市
     *
     * @mbggenerated
     */
    @TableField("BankCity")
    private String bankCity;

    /**
     * 金额
     *
     * @mbggenerated
     */
    @TableField("Amount")
    private Long amount;

    /**
     * 对公私标识
     *
     * @mbggenerated
     */
    @TableField("PublicFlag")
    private Byte publicFlag;

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

    public Long getSettRecordId() {
        return settRecordId;
    }

    public void setSettRecordId(Long settRecordId) {
        this.settRecordId = settRecordId;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNetName() {
        return bankNetName;
    }

    public void setBankNetName(String bankNetName) {
        this.bankNetName = bankNetName;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Byte getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(Byte publicFlag) {
        this.publicFlag = publicFlag;
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
        sb.append(", settRecordId=").append(settRecordId);
        sb.append(", bankAccountName=").append(bankAccountName);
        sb.append(", bankAccountNumber=").append(bankAccountNumber);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNetName=").append(bankNetName);
        sb.append(", bankProvince=").append(bankProvince);
        sb.append(", bankCity=").append(bankCity);
        sb.append(", amount=").append(amount);
        sb.append(", publicFlag=").append(publicFlag);
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
        MchSettBatchRecord other = (MchSettBatchRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getMchId() == null ? other.getMchId() == null : this.getMchId().equals(other.getMchId()))
            && (this.getSettRecordId() == null ? other.getSettRecordId() == null : this.getSettRecordId().equals(other.getSettRecordId()))
            && (this.getBankAccountName() == null ? other.getBankAccountName() == null : this.getBankAccountName().equals(other.getBankAccountName()))
            && (this.getBankAccountNumber() == null ? other.getBankAccountNumber() == null : this.getBankAccountNumber().equals(other.getBankAccountNumber()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankNetName() == null ? other.getBankNetName() == null : this.getBankNetName().equals(other.getBankNetName()))
            && (this.getBankProvince() == null ? other.getBankProvince() == null : this.getBankProvince().equals(other.getBankProvince()))
            && (this.getBankCity() == null ? other.getBankCity() == null : this.getBankCity().equals(other.getBankCity()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getPublicFlag() == null ? other.getPublicFlag() == null : this.getPublicFlag().equals(other.getPublicFlag()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getMchId() == null) ? 0 : getMchId().hashCode());
        result = prime * result + ((getSettRecordId() == null) ? 0 : getSettRecordId().hashCode());
        result = prime * result + ((getBankAccountName() == null) ? 0 : getBankAccountName().hashCode());
        result = prime * result + ((getBankAccountNumber() == null) ? 0 : getBankAccountNumber().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNetName() == null) ? 0 : getBankNetName().hashCode());
        result = prime * result + ((getBankProvince() == null) ? 0 : getBankProvince().hashCode());
        result = prime * result + ((getBankCity() == null) ? 0 : getBankCity().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getPublicFlag() == null) ? 0 : getPublicFlag().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
