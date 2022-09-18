package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_sett_record")
public class SettRecord implements Serializable {
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
     * 结算商类型:1-代理商,2-商户
     *
     * @mbggenerated
     */
    @TableField("InfoType")
    private Byte infoType;

    /**
     * 结算发起类型:1-手工结算,2-自动结算
     *
     * @mbggenerated
     */
    @TableField("SettType")
    private Byte settType;

    /**
     * 结算日期
     *
     * @mbggenerated
     */
    @TableField("SettDate")
    private Date settDate;

    /**
     * 申请结算金额
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
     * 账户属性:0-对私,1-对公,默认对私
     *
     * @mbggenerated
     */
    @TableField("AccountAttr")
    private Byte accountAttr;

    /**
     * 账户类型:1-银行卡转账,2-微信转账,3-支付宝转账
     *
     * @mbggenerated
     */
    @TableField("AccountType")
    private Byte accountType;

    /**
     * 开户行名称
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
     * 账户名
     *
     * @mbggenerated
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 账户号
     *
     * @mbggenerated
     */
    @TableField("AccountNo")
    private String accountNo;

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
     * 操作用户ID
     *
     * @mbggenerated
     */
    @TableField("OperatorId")
    private Long operatorId;

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
     * 结算单号
     *
     * @mbggenerated
     */
    @TableField("SettOrderId")
    private String settOrderId;

    /**
     * 转账订单号
     *
     * @mbggenerated
     */
    @TableField("TransOrderId")
    private String transOrderId;

    /**
     * 转账返回消息
     *
     * @mbggenerated
     */
    @TableField("TransMsg")
    private String transMsg;

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

    public Byte getSettType() {
        return settType;
    }

    public void setSettType(Byte settType) {
        this.settType = settType;
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

    public Byte getAccountAttr() {
        return accountAttr;
    }

    public void setAccountAttr(Byte accountAttr) {
        this.accountAttr = accountAttr;
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

    public String getSettOrderId() {
        return settOrderId;
    }

    public void setSettOrderId(String settOrderId) {
        this.settOrderId = settOrderId;
    }

    public String getTransOrderId() {
        return transOrderId;
    }

    public void setTransOrderId(String transOrderId) {
        this.transOrderId = transOrderId;
    }

    public String getTransMsg() {
        return transMsg;
    }

    public void setTransMsg(String transMsg) {
        this.transMsg = transMsg;
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
        sb.append(", settType=").append(settType);
        sb.append(", settDate=").append(settDate);
        sb.append(", settAmount=").append(settAmount);
        sb.append(", settFee=").append(settFee);
        sb.append(", remitAmount=").append(remitAmount);
        sb.append(", accountAttr=").append(accountAttr);
        sb.append(", accountType=").append(accountType);
        sb.append(", bankName=").append(bankName);
        sb.append(", bankNetName=").append(bankNetName);
        sb.append(", accountName=").append(accountName);
        sb.append(", accountNo=").append(accountNo);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", settStatus=").append(settStatus);
        sb.append(", remark=").append(remark);
        sb.append(", remitRemark=").append(remitRemark);
        sb.append(", operatorId=").append(operatorId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", settOrderId=").append(settOrderId);
        sb.append(", transOrderId=").append(transOrderId);
        sb.append(", transMsg=").append(transMsg);
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
        SettRecord other = (SettRecord) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInfoId() == null ? other.getInfoId() == null : this.getInfoId().equals(other.getInfoId()))
            && (this.getInfoType() == null ? other.getInfoType() == null : this.getInfoType().equals(other.getInfoType()))
            && (this.getSettType() == null ? other.getSettType() == null : this.getSettType().equals(other.getSettType()))
            && (this.getSettDate() == null ? other.getSettDate() == null : this.getSettDate().equals(other.getSettDate()))
            && (this.getSettAmount() == null ? other.getSettAmount() == null : this.getSettAmount().equals(other.getSettAmount()))
            && (this.getSettFee() == null ? other.getSettFee() == null : this.getSettFee().equals(other.getSettFee()))
            && (this.getRemitAmount() == null ? other.getRemitAmount() == null : this.getRemitAmount().equals(other.getRemitAmount()))
            && (this.getAccountAttr() == null ? other.getAccountAttr() == null : this.getAccountAttr().equals(other.getAccountAttr()))
            && (this.getAccountType() == null ? other.getAccountType() == null : this.getAccountType().equals(other.getAccountType()))
            && (this.getBankName() == null ? other.getBankName() == null : this.getBankName().equals(other.getBankName()))
            && (this.getBankNetName() == null ? other.getBankNetName() == null : this.getBankNetName().equals(other.getBankNetName()))
            && (this.getAccountName() == null ? other.getAccountName() == null : this.getAccountName().equals(other.getAccountName()))
            && (this.getAccountNo() == null ? other.getAccountNo() == null : this.getAccountNo().equals(other.getAccountNo()))
            && (this.getProvince() == null ? other.getProvince() == null : this.getProvince().equals(other.getProvince()))
            && (this.getCity() == null ? other.getCity() == null : this.getCity().equals(other.getCity()))
            && (this.getSettStatus() == null ? other.getSettStatus() == null : this.getSettStatus().equals(other.getSettStatus()))
            && (this.getRemark() == null ? other.getRemark() == null : this.getRemark().equals(other.getRemark()))
            && (this.getRemitRemark() == null ? other.getRemitRemark() == null : this.getRemitRemark().equals(other.getRemitRemark()))
            && (this.getOperatorId() == null ? other.getOperatorId() == null : this.getOperatorId().equals(other.getOperatorId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getSettOrderId() == null ? other.getSettOrderId() == null : this.getSettOrderId().equals(other.getSettOrderId()))
            && (this.getTransOrderId() == null ? other.getTransOrderId() == null : this.getTransOrderId().equals(other.getTransOrderId()))
            && (this.getTransMsg() == null ? other.getTransMsg() == null : this.getTransMsg().equals(other.getTransMsg()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInfoId() == null) ? 0 : getInfoId().hashCode());
        result = prime * result + ((getInfoType() == null) ? 0 : getInfoType().hashCode());
        result = prime * result + ((getSettType() == null) ? 0 : getSettType().hashCode());
        result = prime * result + ((getSettDate() == null) ? 0 : getSettDate().hashCode());
        result = prime * result + ((getSettAmount() == null) ? 0 : getSettAmount().hashCode());
        result = prime * result + ((getSettFee() == null) ? 0 : getSettFee().hashCode());
        result = prime * result + ((getRemitAmount() == null) ? 0 : getRemitAmount().hashCode());
        result = prime * result + ((getAccountAttr() == null) ? 0 : getAccountAttr().hashCode());
        result = prime * result + ((getAccountType() == null) ? 0 : getAccountType().hashCode());
        result = prime * result + ((getBankName() == null) ? 0 : getBankName().hashCode());
        result = prime * result + ((getBankNetName() == null) ? 0 : getBankNetName().hashCode());
        result = prime * result + ((getAccountName() == null) ? 0 : getAccountName().hashCode());
        result = prime * result + ((getAccountNo() == null) ? 0 : getAccountNo().hashCode());
        result = prime * result + ((getProvince() == null) ? 0 : getProvince().hashCode());
        result = prime * result + ((getCity() == null) ? 0 : getCity().hashCode());
        result = prime * result + ((getSettStatus() == null) ? 0 : getSettStatus().hashCode());
        result = prime * result + ((getRemark() == null) ? 0 : getRemark().hashCode());
        result = prime * result + ((getRemitRemark() == null) ? 0 : getRemitRemark().hashCode());
        result = prime * result + ((getOperatorId() == null) ? 0 : getOperatorId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getSettOrderId() == null) ? 0 : getSettOrderId().hashCode());
        result = prime * result + ((getTransOrderId() == null) ? 0 : getTransOrderId().hashCode());
        result = prime * result + ((getTransMsg() == null) ? 0 : getTransMsg().hashCode());
        return result;
    }
}
