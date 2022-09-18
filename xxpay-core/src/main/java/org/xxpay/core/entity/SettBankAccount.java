package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_sett_bank_account")
public class SettBankAccount implements Serializable {
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
    @TableField("Name")
    private String name;

    /**
     * 账号类型:1-银行账号,2-微信,3-支付宝
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Byte accountType;

    /**
     * 银行名称
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
    @TableField("Province")
    private String province;

    /**
     * 开户行所在市
     *
     * @mbggenerated
     */
    @TableField("City")
    private String city;

    /**
     * 开户人
     *
     * @mbggenerated
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 账号
     *
     * @mbggenerated
     */
    @TableField("AccountNo")
    private String accountNo;

    /**
     * 账户属性:0-对私,1-对公,默认对私
     *
     * @mbggenerated
     */
    @TableField("AccountAttr")
    private Byte accountAttr;

    /**
     * 是否默认:0-不是默认,1-默认
     *
     * @mbggenerated
     */
    @TableField("IsDefault")
    private Byte isDefault;

    /**
     * 备注
     *
     * @mbggenerated
     */
    @TableField("Remark")
    private String remark;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getAccountType() {
        return accountType;
    }

    public void setAccountType(Byte accountType) {
        this.accountType = accountType;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Byte getAccountAttr() {
        return accountAttr;
    }

    public void setAccountAttr(Byte accountAttr) {
        this.accountAttr = accountAttr;
    }

    public Byte getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Byte isDefault) {
        this.isDefault = isDefault;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", infoId=").append(infoId);
        sb.append(", infoType=").append(infoType);
        sb.append(", name=").append(name);
        sb.append(", accountType=").append(accountType);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNetName=").append(bankNetName);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", accountAttr=").append(accountAttr);
        sb.append(", isDefault=").append(isDefault);
        sb.append(", remark=").append(remark);
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
        SettBankAccount other = (SettBankAccount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
            && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankNetName() == null ? other.getBankNetName() == null : this.getBankNetName().equals(other.getBankNetName()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
            && (this.getAccountAttr() == null ? other.getAccountAttr() == null : this.getAccountAttr().equals(other.getAccountAttr()))
            && (this.getIsDefault() == null ? other.getIsDefault() == null : this.getIsDefault().equals(other.getIsDefault()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
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
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNetName() == null) ? 0 : getBankNetName().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getAccountAttr() == null) ? 0 : getAccountAttr().hashCode());
        result = prime * result + ((getIsDefault() == null) ? 0 : getIsDefault().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
