package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户交易按日跑批表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-08-19
 */
@TableName("t_mch_trade_order_batch")
public class MchTradeOrderBatch extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商户交易跑批ID
     */
    @TableId("BatchId")
    private String batchId;

    /**
     * 跑批日期, 字符型, 格式: yyyy-MM-dd
     */
    @TableField("BatchDate")
    private String batchDate;

    /**
     * 跑批月份, 格式: yyyy-MM
     */
    @TableField("BatchMonth")
    private String batchMonth;

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
     * 医院ID
     */
    @TableField("HospitalId")
    private Long hospitalId;

    /**
     * 行政地区编号，省
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    /**
     * 行政地区编号， 市
     */
    @TableField("CityCode")
    private Integer cityCode;

    /**
     * 行政地区编号， 县
     */
    @TableField("AreaCode")
    private Integer areaCode;

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

    public String getBatchMonth() {
        return batchMonth;
    }

    public void setBatchMonth(String batchMonth) {
        this.batchMonth = batchMonth;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Integer getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(Integer provinceCode) {
        this.provinceCode = provinceCode;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Integer areaCode) {
        this.areaCode = areaCode;
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
                ", BatchMonth=" + batchMonth +
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
                ", HospitalId=" + hospitalId +
                ", ProvinceCode=" + provinceCode +
                ", CityCode=" + cityCode +
                ", AreaCode=" + areaCode +
                ", Remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}

