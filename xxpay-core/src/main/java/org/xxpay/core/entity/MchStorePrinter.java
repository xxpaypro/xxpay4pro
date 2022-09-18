package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 门店与打印机关联表
 * </p>
 *
 * @author pangxiaoyu
 * @since 2019-09-11
 */
@TableName("t_mch_store_printer")
public class MchStorePrinter extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 门店ID
     */
    @TableId("StoreId")
    private Long storeId;

    /**
     * 打印机ID
     */
    @TableField("PrinterId")
    private String printerId;

    /**
     * 状态,0-关闭,1-开启
     */
    @TableField("Status")
    private Byte status;

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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getPrinterId() {
        return printerId;
    }

    public void setPrinterId(String printerId) {
        this.printerId = printerId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        return "MchStorePrinter{" +
            "StoreId=" + storeId +
            ", printerId=" + printerId +
            ", status=" + status +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
