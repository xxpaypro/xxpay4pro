package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 收货地址表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-16
 */
@TableName("t_mch_receive_address")
public class MchReceiveAddress extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 收货地址ID
     */
    @TableId(value = "AddressId", type = IdType.AUTO)
    private Long addressId;

    /**
     * 会员ID
     */
    @TableField("MemberId")
    private Long memberId;

    /**
     * 联系人姓名
     */
    @TableField("ContactName")
    private String contactName;

    /**
     * 手机号
     */
    @TableField("Tel")
    private String tel;

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
     * 省市县名称描述
     */
    @TableField("AreaInfo")
    private String areaInfo;

    /**
     * 详细地址
     */
    @TableField("Address")
    private String address;

    /**
     * 是否为默认地址 0-否 1-是
     */
    @TableField("isDefaultAddress")
    private Byte isDefaultAddress;

    /**
     * 所属商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

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

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }
    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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
    public Byte getIsDefaultAddress() {
        return isDefaultAddress;
    }

    public void setIsDefaultAddress(Byte isDefaultAddress) {
        this.isDefaultAddress = isDefaultAddress;
    }
    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
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
        return "MchReceiveAddress{" +
            "AddressId=" + addressId +
            ", memberId=" + memberId +
            ", contactName=" + contactName +
            ", tel=" + tel +
            ", provinceCode=" + provinceCode +
            ", cityCode=" + cityCode +
            ", areaCode=" + areaCode +
            ", areaInfo=" + areaInfo +
            ", address=" + address +
            ", isDefaultAddress=" + isDefaultAddress +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
        "}";
    }
}
