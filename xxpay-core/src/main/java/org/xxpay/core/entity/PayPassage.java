package org.xxpay.core.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 支付通道表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-27
 */
@TableName("t_pay_passage")
public class PayPassage extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 支付通道ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属角色ID
     */
    @TableField("BelongInfoId")
    private Long belongInfoId;

    /**
     * 所属角色类型:1-商户 2-代理商 3-服务商
     */
    @TableField("BelongInfoType")
    private Byte belongInfoType;

    /**
     * 通道名称
     */
    @TableField("PassageName")
    private String passageName;

    /**
     * 接口类型代码
     */
    @TableField("IfTypeCode")
    private String ifTypeCode;

    /**
     * 通道状态,0-关闭,1-开启
     */
    @TableField("Status")
    private Byte status;

    /**
     * 当天交易金额,单位分
     */
    @TableField("MaxDayAmount")
    private Long maxDayAmount;

    /**
     * 单笔最大金额,单位分
     */
    @TableField("MaxEveryAmount")
    private Long maxEveryAmount;

    /**
     * 单笔最小金额,单位分
     */
    @TableField("MinEveryAmount")
    private Long minEveryAmount;

    /**
     * 交易开始时间
     */
    @TableField("TradeStartTime")
    private String tradeStartTime;

    /**
     * 交易结束时间
     */
    @TableField("TradeEndTime")
    private String tradeEndTime;

    /**
     * 风控状态,0-关闭,1-开启
     */
    @TableField("RiskStatus")
    private Byte riskStatus;

    /**
     * 签约状态,0-未开通,1-待审核,2-审核不通过,3-已签约
     */
    @TableField("ContractStatus")
    private Byte contractStatus;

    /**
     * isv账户配置参数,json字符串
     */
    @TableField("IsvParam")
    private String isvParam;

    /**
     * 商户账户配置参数,json字符串
     */
    @TableField("MchParam")
    private String mchParam;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getBelongInfoId() {
        return belongInfoId;
    }

    public void setBelongInfoId(Long belongInfoId) {
        this.belongInfoId = belongInfoId;
    }
    public Byte getBelongInfoType() {
        return belongInfoType;
    }

    public void setBelongInfoType(Byte belongInfoType) {
        this.belongInfoType = belongInfoType;
    }
    public String getPassageName() {
        return passageName;
    }

    public void setPassageName(String passageName) {
        this.passageName = passageName;
    }
    public String getIfTypeCode() {
        return ifTypeCode;
    }

    public void setIfTypeCode(String ifTypeCode) {
        this.ifTypeCode = ifTypeCode;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getMaxDayAmount() {
        return maxDayAmount;
    }

    public void setMaxDayAmount(Long maxDayAmount) {
        this.maxDayAmount = maxDayAmount;
    }
    public Long getMaxEveryAmount() {
        return maxEveryAmount;
    }

    public void setMaxEveryAmount(Long maxEveryAmount) {
        this.maxEveryAmount = maxEveryAmount;
    }
    public Long getMinEveryAmount() {
        return minEveryAmount;
    }

    public void setMinEveryAmount(Long minEveryAmount) {
        this.minEveryAmount = minEveryAmount;
    }
    public String getTradeStartTime() {
        return tradeStartTime;
    }

    public void setTradeStartTime(String tradeStartTime) {
        this.tradeStartTime = tradeStartTime;
    }
    public String getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(String tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }
    public Byte getRiskStatus() {
        return riskStatus;
    }

    public void setRiskStatus(Byte riskStatus) {
        this.riskStatus = riskStatus;
    }
    public Byte getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(Byte contractStatus) {
        this.contractStatus = contractStatus;
    }
    public String getIsvParam() {
        return isvParam;
    }

    public void setIsvParam(String isvParam) {
        this.isvParam = isvParam;
    }
    public String getMchParam() {
        return mchParam;
    }

    public void setMchParam(String mchParam) {
        this.mchParam = mchParam;
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
        return "PayPassage{" +
                "id=" + id +
                ", belongInfoId=" + belongInfoId +
                ", belongInfoType=" + belongInfoType +
                ", passageName=" + passageName +
                ", ifTypeCode=" + ifTypeCode +
                ", status=" + status +
                ", maxDayAmount=" + maxDayAmount +
                ", maxEveryAmount=" + maxEveryAmount +
                ", minEveryAmount=" + minEveryAmount +
                ", tradeStartTime=" + tradeStartTime +
                ", tradeEndTime=" + tradeEndTime +
                ", riskStatus=" + riskStatus +
                ", contractStatus=" + contractStatus +
                ", isvParam=" + isvParam +
                ", mchParam=" + mchParam +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
