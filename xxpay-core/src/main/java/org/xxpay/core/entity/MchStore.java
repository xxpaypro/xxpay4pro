package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户门店表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-04-27
 */
@TableName("t_mch_store")
public class MchStore extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 门店ID
     */
    @TableId(value = "StoreId", type = IdType.AUTO)
    private Long storeId;

    /**
     * 门店编号
     */
    @TableField("StoreNo")
    private String storeNo;

    /**
     * 门店名称
     */
    @TableField("StoreName")
    private String storeName;

    /**
     * 退款密码
     */
    @TableField("RefundPassword")
    private String refundPassword;

    /**
     * 所在省
     */
    @TableField("ProvinceCode")
    private Integer provinceCode;

    /**
     * 所在市
     */
    @TableField("CityCode")
    private Integer cityCode;

    /**
     * 县/地区
     */
    @TableField("AreaCode")
    private Integer areaCode;

    /**
     * 省市县名称描述
     */
    @TableField("AreaInfo")
    private String areaInfo;

    /**
     * 具体位置
     */
    @TableField("Address")
    private String address;

    /**
     * 经度
     */
    @TableField("Lot")
    private String lot;

    /**
     * 纬度
     */
    @TableField("Lat")
    private String lat;

    /**
     * 状态:0-暂停营业,1-正常营业
     */
    @TableField("Status")
    private Byte status;

    /**
     * 门店电话
     */
    @TableField("Tel")
    private String tel;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 备注
     */
    @TableField("Remark")
    private String remark;

    /**
     * 门店图片, 本地相对路径
     */
    @TableField("StoreImgPath")
    private String storeImgPath;

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

    /**
     * 小程序首页顶部背景图, 本地相对路径
     */
    @TableField("MiniImgPath")
    private String miniImgPath;

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    public String getRefundPassword() {
        return refundPassword;
    }

    public void setRefundPassword(String refundPassword) {
        this.refundPassword = refundPassword;
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
    public String getAreaInfo() {
        return areaInfo;
    }

    public void setAreaInfo(String areaInfo) {
        this.areaInfo = areaInfo;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getStoreImgPath() {
        return storeImgPath;
    }

    public void setStoreImgPath(String storeImgPath) {
        this.storeImgPath = storeImgPath;
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
    public String getMiniImgPath() {
        return miniImgPath;
    }

    public void setMiniImgPath(String miniImgPath) {
        this.miniImgPath = miniImgPath;
    }

    @Override
    public String toString() {
        return "MchStore{" +
            "StoreId=" + storeId +
            ", storeNo=" + storeNo +
            ", storeName=" + storeName +
            ", refundPassword=" + refundPassword +
            ", provinceCode=" + provinceCode +
            ", cityCode=" + cityCode +
            ", areaCode=" + areaCode +
            ", areaInfo=" + areaInfo +
            ", address=" + address +
            ", lot=" + lot +
            ", lat=" + lat +
            ", status=" + status +
            ", tel=" + tel +
            ", mchId=" + mchId +
            ", remark=" + remark +
            ", storeImgPath=" + storeImgPath +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", miniImgPath=" + miniImgPath +
        "}";
    }
}
