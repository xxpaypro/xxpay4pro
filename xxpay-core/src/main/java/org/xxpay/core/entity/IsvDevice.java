package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商硬件设备表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-31
 */
@TableName("t_isv_device")
public class IsvDevice extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 设备名称
     */
    @TableField("DeviceName")
    private String deviceName;

    /**
     * 设备编号
     */
    @TableField("DeviceNo")
    private String deviceNo;

    /**
     * 设备类型 1-微信 2-支付宝
     */
    @TableField("DeviceType")
    private Byte deviceType;

    /**
     * 绑定商户ID， 不绑定为0
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 商户名称
     */
    @TableField("MchName")
    private String mchName;

    /**
     * 绑定门店ID， 不绑定为0
     */
    @TableField("StoreId")
    private Long storeId;

    /**
     * 门店名称
     */
    @TableField("StoreName")
    private String storeName;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 状态:0-设备停用, 1-设备启用
     */
    @TableField("Status")
    private Byte status;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
    public Byte getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Byte deviceType) {
        this.deviceType = deviceType;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }
    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        return "IsvDevice{" +
                "id=" + id +
                ", deviceName=" + deviceName +
                ", deviceNo=" + deviceNo +
                ", deviceType=" + deviceType +
                ", mchId=" + mchId +
                ", mchName=" + mchName +
                ", storeId=" + storeId +
                ", storeName=" + storeName +
                ", isvId=" + isvId +
                ", status=" + status +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
