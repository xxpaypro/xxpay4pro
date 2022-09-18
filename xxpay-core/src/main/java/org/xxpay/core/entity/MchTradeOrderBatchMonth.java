package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/**
 * <p>
 * 商户交易按月跑批表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-19
 */
@TableName("t_mch_trade_order_batch_month")
public class MchTradeOrderBatchMonth extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商户交易跑批ID
     */
    @TableId("BatchId")
    private String batchId;

    /**
     * 跑批日期(按月), 字符型, 格式: yyyy-MM
     */
    @TableField("BatchDate")
    private String batchDate;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;


    /**
     * 微信收款金额,单位分
     */
    @TableField("WxSumRealAmount")
    private Long wxSumRealAmount;

    /**
     * 微信退款金额,单位分
     */
    @TableField("WxSumRefundAmount")
    private Long wxSumRefundAmount;

    /**
     * 微信收款笔数
     */
    @TableField("WxCuntTrade")
    private Long wxCuntTrade;

    /**
     * 微信退款笔数
     */
    @TableField("WxRefundCunt")
    private Long wxRefundCunt;


    /**
     * 支付宝收款金额,单位分
     */
    @TableField("AliPaySumRealAmount")
    private Long aliPaySumRealAmount;

    /**
     * 支付宝退款金额,单位分
     */
    @TableField("AliPaySumRefundAmount")
    private Long aliPaySumRefundAmount;

    /**
     * 支付宝收款笔数
     */
    @TableField("AliPayCuntTrade")
    private Long aliPayCuntTrade;

    /**
     * 支付宝退款笔数
     */
    @TableField("AliPayRefundCount")
    private Long aliPayRefundCount;

    /**
     * 跑批任务状态:
     */
    @TableField("BatchTaskStatus")
    private Byte batchTaskStatus;

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

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchDate() {
        return batchDate;
    }

    public void setBatchDate(String batchDate) {
        this.batchDate = batchDate;
    }

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public Long getWxSumRealAmount() {
        return wxSumRealAmount;
    }

    public void setWxSumRealAmount(Long wxSumRealAmount) {
        this.wxSumRealAmount = wxSumRealAmount;
    }

    public Long getWxSumRefundAmount() {
        return wxSumRefundAmount;
    }

    public void setWxSumRefundAmount(Long wxSumRefundAmount) {
        this.wxSumRefundAmount = wxSumRefundAmount;
    }

    public Long getWxCuntTrade() {
        return wxCuntTrade;
    }

    public void setWxCuntTrade(Long wxCuntTrade) {
        this.wxCuntTrade = wxCuntTrade;
    }

    public Long getWxRefundCunt() {
        return wxRefundCunt;
    }

    public void setWxRefundCunt(Long wxRefundCunt) {
        this.wxRefundCunt = wxRefundCunt;
    }

    public Long getAliPaySumRealAmount() {
        return aliPaySumRealAmount;
    }

    public void setAliPaySumRealAmount(Long aliPaySumRealAmount) {
        this.aliPaySumRealAmount = aliPaySumRealAmount;
    }

    public Long getAliPaySumRefundAmount() {
        return aliPaySumRefundAmount;
    }

    public void setAliPaySumRefundAmount(Long aliPaySumRefundAmount) {
        this.aliPaySumRefundAmount = aliPaySumRefundAmount;
    }

    public Long getAliPayCuntTrade() {
        return aliPayCuntTrade;
    }

    public void setAliPayCuntTrade(Long aliPayCuntTrade) {
        this.aliPayCuntTrade = aliPayCuntTrade;
    }

    public Long getAliPayRefundCount() {
        return aliPayRefundCount;
    }

    public void setAliPayRefundCount(Long aliPayRefundCount) {
        this.aliPayRefundCount = aliPayRefundCount;
    }

    public Byte getBatchTaskStatus() {
        return batchTaskStatus;
    }

    public void setBatchTaskStatus(Byte batchTaskStatus) {
        this.batchTaskStatus = batchTaskStatus;
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
        return "MchTradeOrderBatch{" +
                "BatchId=" + batchId +
                ", BatchDate=" + batchDate +
                ", MchId=" + mchId +
                ", WxSumRealAmount=" + wxSumRealAmount +
                ", WxSumRefundAmount=" + wxSumRefundAmount +
                ", WxCuntTrade=" + wxCuntTrade +
                ", WxRefundCunt=" + wxRefundCunt +
                ", AliPaySumRealAmount=" + aliPaySumRealAmount +
                ", AliPaySumRefundAmount=" + aliPaySumRefundAmount +
                ", AliPayCuntTrade=" + aliPayCuntTrade +
                ", AliPayRefundCount=" + aliPayRefundCount +
                ", BatchTaskStatus=" + batchTaskStatus +
                ", Remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
