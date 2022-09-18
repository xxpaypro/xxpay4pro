package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户统一扫码配置
 * </p>
 *
 * @author xxpay generator
 * @since 2019-08-27
 */
@TableName("t_mch_qr_code")
public class MchQrCode extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 状态,启用(1),停止(0)
     */
    @TableField("Status")
    private Byte status;

    /**
     * 统一扫码名称
     */
    @TableField("CodeName")
    private String codeName;

    /**
     * 二维码支付金额, 单位：分
     */
    @TableField("PayAmount")
    private Long payAmount;

    /**
     * 绑定门店ID
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 绑定操作员ID
     */
    @TableField("OperatorId")
    private String operatorId;

    /**
     * 备注信息
     */
    @TableField("Remark")
    private String remark;

    /**
     * 创建人ID
     */
    @TableField("CreateOperatorId")
    private String createOperatorId;

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
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCreateOperatorId() {
        return createOperatorId;
    }

    public void setCreateOperatorId(String createOperatorId) {
        this.createOperatorId = createOperatorId;
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
        return "MchQrCode{" +
                "Id=" + id +
                ", mchId=" + mchId +
                ", status=" + status +
                ", codeName=" + codeName +
                ", payAmount=" + payAmount +
                ", storeId=" + storeId +
                ", operatorId=" + operatorId +
                ", remark=" + remark +
                ", createOperatorId=" + createOperatorId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
