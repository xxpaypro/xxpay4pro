package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_check_batch")
public class CheckBatch implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Long id;

    /**
     * 对账批次号
     *
     * @mbggenerated
     */
    @TableField("BatchNo")
    private String batchNo;

    /**
     * 账单时间(账单交易发生时间)
     *
     * @mbggenerated
     */
    @TableField("BillDate")
    private Date billDate;

    /**
     * 账单类型(默认全部是交易成功)
     *
     * @mbggenerated
     */
    @TableField("BillType")
    private Byte billType;

    /**
     * 类型:0-未处理,1-已处理
     *
     * @mbggenerated
     */
    @TableField("HandleStatus")
    private Byte handleStatus;

    /**
     * 银行类型,wxpay:微信,alipay:支付宝
     *
     * @mbggenerated
     */
    @TableField("BankType")
    private String bankType;

    /**
     * 渠道商户ID
     *
     * @mbggenerated
     */
    @TableField("ChannelMchId")
    private String channelMchId;

    /**
     * 所有差错总单数
     *
     * @mbggenerated
     */
    @TableField("MistakeCount")
    private Integer mistakeCount;

    /**
     * 待处理的差错总单数
     *
     * @mbggenerated
     */
    @TableField("UnhandleMistakeCount")
    private Integer unhandleMistakeCount;

    /**
     * 平台总交易单数
     *
     * @mbggenerated
     */
    @TableField("TradeCount")
    private Integer tradeCount;

    /**
     * 银行总交易单数
     *
     * @mbggenerated
     */
    @TableField("BankTradeCount")
    private Integer bankTradeCount;

    /**
     * 平台交易总金额
     *
     * @mbggenerated
     */
    @TableField("TradeAmount")
    private Long tradeAmount;

    /**
     * 银行交易总金额
     *
     * @mbggenerated
     */
    @TableField("BankTradeAmount")
    private Long bankTradeAmount;

    /**
     * 平台退款总金额
     *
     * @mbggenerated
     */
    @TableField("RefundAmount")
    private Long refundAmount;

    /**
     * 银行退款总金额
     *
     * @mbggenerated
     */
    @TableField("BankRefundAmount")
    private Long bankRefundAmount;

    /**
     * 平台总手续费
     *
     * @mbggenerated
     */
    @TableField("Fee")
    private Long fee;

    /**
     * 银行总手续费
     *
     * @mbggenerated
     */
    @TableField("BankFee")
    private Long bankFee;

    /**
     * 原始对账文件存放地址
     *
     * @mbggenerated
     */
    @TableField("OrgCheckFilePath")
    private String orgCheckFilePath;

    /**
     * 解析后文件存放地址
     *
     * @mbggenerated
     */
    @TableField("ReleaseCheckFilePath")
    private String releaseCheckFilePath;

    /**
     * 解析状态:0-失败,1-成功
     *
     * @mbggenerated
     */
    @TableField("ReleaseStatus")
    private Byte releaseStatus;

    /**
     * 解析检查失败的描述信息
     *
     * @mbggenerated
     */
    @TableField("CheckFailMsg")
    private String checkFailMsg;

    /**
     * 银行返回的错误信息
     *
     * @mbggenerated
     */
    @TableField("BankErrMsg")
    private String bankErrMsg;

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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Byte getBillType() {
        return billType;
    }

    public void setBillType(Byte billType) {
        this.billType = billType;
    }

    public Byte getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Byte handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getChannelMchId() {
        return channelMchId;
    }

    public void setChannelMchId(String channelMchId) {
        this.channelMchId = channelMchId;
    }

    public Integer getMistakeCount() {
        return mistakeCount;
    }

    public void setMistakeCount(Integer mistakeCount) {
        this.mistakeCount = mistakeCount;
    }

    public Integer getUnhandleMistakeCount() {
        return unhandleMistakeCount;
    }

    public void setUnhandleMistakeCount(Integer unhandleMistakeCount) {
        this.unhandleMistakeCount = unhandleMistakeCount;
    }

    public Integer getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(Integer tradeCount) {
        this.tradeCount = tradeCount;
    }

    public Integer getBankTradeCount() {
        return bankTradeCount;
    }

    public void setBankTradeCount(Integer bankTradeCount) {
        this.bankTradeCount = bankTradeCount;
    }

    public Long getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Long tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Long getBankTradeAmount() {
        return bankTradeAmount;
    }

    public void setBankTradeAmount(Long bankTradeAmount) {
        this.bankTradeAmount = bankTradeAmount;
    }

    public Long getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Long getBankRefundAmount() {
        return bankRefundAmount;
    }

    public void setBankRefundAmount(Long bankRefundAmount) {
        this.bankRefundAmount = bankRefundAmount;
    }

    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    public Long getBankFee() {
        return bankFee;
    }

    public void setBankFee(Long bankFee) {
        this.bankFee = bankFee;
    }

    public String getOrgCheckFilePath() {
        return orgCheckFilePath;
    }

    public void setOrgCheckFilePath(String orgCheckFilePath) {
        this.orgCheckFilePath = orgCheckFilePath;
    }

    public String getReleaseCheckFilePath() {
        return releaseCheckFilePath;
    }

    public void setReleaseCheckFilePath(String releaseCheckFilePath) {
        this.releaseCheckFilePath = releaseCheckFilePath;
    }

    public Byte getReleaseStatus() {
        return releaseStatus;
    }

    public void setReleaseStatus(Byte releaseStatus) {
        this.releaseStatus = releaseStatus;
    }

    public String getCheckFailMsg() {
        return checkFailMsg;
    }

    public void setCheckFailMsg(String checkFailMsg) {
        this.checkFailMsg = checkFailMsg;
    }

    public String getBankErrMsg() {
        return bankErrMsg;
    }

    public void setBankErrMsg(String bankErrMsg) {
        this.bankErrMsg = bankErrMsg;
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
        sb.append(", batchNo=").append(batchNo);
        sb.append(", billDate=").append(billDate);
        sb.append(", billType=").append(billType);
        sb.append(", handleStatus=").append(handleStatus);
        sb.append(", bankType=").append(bankType);
        sb.append(", channelMchId=").append(channelMchId);
        sb.append(", mistakeCount=").append(mistakeCount);
        sb.append(", unhandleMistakeCount=").append(unhandleMistakeCount);
        sb.append(", tradeCount=").append(tradeCount);
        sb.append(", bankTradeCount=").append(bankTradeCount);
        sb.append(", tradeAmount=").append(tradeAmount);
        sb.append(", bankTradeAmount=").append(bankTradeAmount);
        sb.append(", refundAmount=").append(refundAmount);
        sb.append(", bankRefundAmount=").append(bankRefundAmount);
        sb.append(", fee=").append(fee);
        sb.append(", bankFee=").append(bankFee);
        sb.append(", orgCheckFilePath=").append(orgCheckFilePath);
        sb.append(", releaseCheckFilePath=").append(releaseCheckFilePath);
        sb.append(", releaseStatus=").append(releaseStatus);
        sb.append(", checkFailMsg=").append(checkFailMsg);
        sb.append(", bankErrMsg=").append(bankErrMsg);
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
        CheckBatch other = (CheckBatch) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getBatchNo() == null ? other.getBatchNo() == null : this.getBatchNo().equals(other.getBatchNo()))
            && (this.getBillDate() == null ? other.getBillDate() == null : this.getBillDate().equals(other.getBillDate()))
            && (this.getBillType() == null ? other.getBillType() == null : this.getBillType().equals(other.getBillType()))
            && (this.getHandleStatus() == null ? other.getHandleStatus() == null : this.getHandleStatus().equals(other.getHandleStatus()))
            && (this.getBankType() == null ? other.getBankType() == null : this.getBankType().equals(other.getBankType()))
            && (this.getChannelMchId() == null ? other.getChannelMchId() == null : this.getChannelMchId().equals(other.getChannelMchId()))
            && (this.getMistakeCount() == null ? other.getMistakeCount() == null : this.getMistakeCount().equals(other.getMistakeCount()))
            && (this.getUnhandleMistakeCount() == null ? other.getUnhandleMistakeCount() == null : this.getUnhandleMistakeCount().equals(other.getUnhandleMistakeCount()))
            && (this.getTradeCount() == null ? other.getTradeCount() == null : this.getTradeCount().equals(other.getTradeCount()))
            && (this.getBankTradeCount() == null ? other.getBankTradeCount() == null : this.getBankTradeCount().equals(other.getBankTradeCount()))
            && (this.getTradeAmount() == null ? other.getTradeAmount() == null : this.getTradeAmount().equals(other.getTradeAmount()))
            && (this.getBankTradeAmount() == null ? other.getBankTradeAmount() == null : this.getBankTradeAmount().equals(other.getBankTradeAmount()))
            && (this.getRefundAmount() == null ? other.getRefundAmount() == null : this.getRefundAmount().equals(other.getRefundAmount()))
            && (this.getBankRefundAmount() == null ? other.getBankRefundAmount() == null : this.getBankRefundAmount().equals(other.getBankRefundAmount()))
            && (this.getFee() == null ? other.getFee() == null : this.getFee().equals(other.getFee()))
            && (this.getBankFee() == null ? other.getBankFee() == null : this.getBankFee().equals(other.getBankFee()))
            && (this.getOrgCheckFilePath() == null ? other.getOrgCheckFilePath() == null : this.getOrgCheckFilePath().equals(other.getOrgCheckFilePath()))
            && (this.getReleaseCheckFilePath() == null ? other.getReleaseCheckFilePath() == null : this.getReleaseCheckFilePath().equals(other.getReleaseCheckFilePath()))
            && (this.getReleaseStatus() == null ? other.getReleaseStatus() == null : this.getReleaseStatus().equals(other.getReleaseStatus()))
            && (this.getCheckFailMsg() == null ? other.getCheckFailMsg() == null : this.getCheckFailMsg().equals(other.getCheckFailMsg()))
            && (this.getBankErrMsg() == null ? other.getBankErrMsg() == null : this.getBankErrMsg().equals(other.getBankErrMsg()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getBatchNo() == null) ? 0 : getBatchNo().hashCode());
        result = prime * result + ((getBillDate() == null) ? 0 : getBillDate().hashCode());
        result = prime * result + ((getBillType() == null) ? 0 : getBillType().hashCode());
        result = prime * result + ((getHandleStatus() == null) ? 0 : getHandleStatus().hashCode());
        result = prime * result + ((getBankType() == null) ? 0 : getBankType().hashCode());
        result = prime * result + ((getChannelMchId() == null) ? 0 : getChannelMchId().hashCode());
        result = prime * result + ((getMistakeCount() == null) ? 0 : getMistakeCount().hashCode());
        result = prime * result + ((getUnhandleMistakeCount() == null) ? 0 : getUnhandleMistakeCount().hashCode());
        result = prime * result + ((getTradeCount() == null) ? 0 : getTradeCount().hashCode());
        result = prime * result + ((getBankTradeCount() == null) ? 0 : getBankTradeCount().hashCode());
        result = prime * result + ((getTradeAmount() == null) ? 0 : getTradeAmount().hashCode());
        result = prime * result + ((getBankTradeAmount() == null) ? 0 : getBankTradeAmount().hashCode());
        result = prime * result + ((getRefundAmount() == null) ? 0 : getRefundAmount().hashCode());
        result = prime * result + ((getBankRefundAmount() == null) ? 0 : getBankRefundAmount().hashCode());
        result = prime * result + ((getFee() == null) ? 0 : getFee().hashCode());
        result = prime * result + ((getBankFee() == null) ? 0 : getBankFee().hashCode());
        result = prime * result + ((getOrgCheckFilePath() == null) ? 0 : getOrgCheckFilePath().hashCode());
        result = prime * result + ((getReleaseCheckFilePath() == null) ? 0 : getReleaseCheckFilePath().hashCode());
        result = prime * result + ((getReleaseStatus() == null) ? 0 : getReleaseStatus().hashCode());
        result = prime * result + ((getCheckFailMsg() == null) ? 0 : getCheckFailMsg().hashCode());
        result = prime * result + ((getBankErrMsg() == null) ? 0 : getBankErrMsg().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
