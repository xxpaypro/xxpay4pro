package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 微信小微进件接口  **/
@XStreamAlias("xml")
public class WxApplymentSubmitRequest extends BaseWxPayRequest {


    /** 接口版本号 */
    @XStreamAlias("version")
    private String version;

    /** 平台证书序列号 */
    @XStreamAlias("cert_sn")
    private String certSn;

    /** 业务申请编号 */
    @XStreamAlias("business_code")
    private String businessCode;

    /** 身份证人像面照片 */
    @XStreamAlias("id_card_copy")
    private String idCardCopy;

    /** 身份证国徽面照片 */
    @XStreamAlias("id_card_national")
    private String idCardNational;

    /** 身份证姓名 */
    @XStreamAlias("id_card_name")
    private String idCardName;

    /** 身份证号码 */
    @XStreamAlias("id_card_number")
    private String idCardNumber;

    /** 身份证有效期限 */
    @XStreamAlias("id_card_valid_time")
    private String idCardValidTime;

    /** 开户名称 */
    @XStreamAlias("account_name")
    private String accountName;

    /** 开户银行 */
    @XStreamAlias("account_bank")
    private String accountBank;

    /** 开户银行省市编码 */
    @XStreamAlias("bank_address_code")
    private String bankAddressCode;

    /** 开户银行全称（含支行） */
    @XStreamAlias("bank_name")
    private String bankName;

    /** 银行账号 */
    @XStreamAlias("account_number")
    private String accountNumber;

    /** 门店名称 */
    @XStreamAlias("store_name")
    private String storeName;

    /** 门店省市编码 */
    @XStreamAlias("store_address_code")
    private String storeAddressCode;

    /** 门店街道名称 */
    @XStreamAlias("store_street")
    private String storeStreet;


    /** 门店经度 */
    @XStreamAlias("store_longitude")
    private String storeLongitude;


    /** 门店纬度 */
    @XStreamAlias("store_latitude")
    private String storeLatitude;


    /** 门店门口照片 */
    @XStreamAlias("store_entrance_pic")
    private String storeEntrancePic;

    /** 店内环境照片 */
    @XStreamAlias("indoor_pic")
    private String indoorPic;

    /** 经营场地证明 */
    @XStreamAlias("address_certification")
    private String addressCertification;

    /** 商户简称 */
    @XStreamAlias("merchant_shortname")
    private String merchantShortName;

    /** 客服电话 */
    @XStreamAlias("service_phone")
    private String servicePhone;

    /** 售卖商品/提供服务描述 */
    @XStreamAlias("product_desc")
    private String productDesc;

    /** 费率 */
    @XStreamAlias("rate")
    private String rate;

    /** 补充说明 */
    @XStreamAlias("business_addition_desc")
    private String businessAdditionDesc;

    /** 补充材料 */
    @XStreamAlias("business_addition_pics")
    private String businessAdditionPics;

    /** 超级管理员姓名 */
    @XStreamAlias("contact")
    private String contact;

    /** 手机号码 */
    @XStreamAlias("contact_phone")
    private String contactPhone;

    /** 联系邮箱 */
    @XStreamAlias("contact_email")
    private String contactEmail;


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCertSn() {
        return certSn;
    }

    public void setCertSn(String certSn) {
        this.certSn = certSn;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getIdCardCopy() {
        return idCardCopy;
    }

    public void setIdCardCopy(String idCardCopy) {
        this.idCardCopy = idCardCopy;
    }

    public String getIdCardNational() {
        return idCardNational;
    }

    public void setIdCardNational(String idCardNational) {
        this.idCardNational = idCardNational;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardValidTime() {
        return idCardValidTime;
    }

    public void setIdCardValidTime(String idCardValidTime) {
        this.idCardValidTime = idCardValidTime;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountBank() {
        return accountBank;
    }

    public void setAccountBank(String accountBank) {
        this.accountBank = accountBank;
    }

    public String getBankAddressCode() {
        return bankAddressCode;
    }

    public void setBankAddressCode(String bankAddressCode) {
        this.bankAddressCode = bankAddressCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddressCode() {
        return storeAddressCode;
    }

    public void setStoreAddressCode(String storeAddressCode) {
        this.storeAddressCode = storeAddressCode;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }

    public String getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(String storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public String getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(String storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public String getStoreEntrancePic() {
        return storeEntrancePic;
    }

    public void setStoreEntrancePic(String storeEntrancePic) {
        this.storeEntrancePic = storeEntrancePic;
    }

    public String getIndoorPic() {
        return indoorPic;
    }

    public void setIndoorPic(String indoorPic) {
        this.indoorPic = indoorPic;
    }

    public String getAddressCertification() {
        return addressCertification;
    }

    public void setAddressCertification(String addressCertification) {
        this.addressCertification = addressCertification;
    }

    public String getMerchantShortName() {
        return merchantShortName;
    }

    public void setMerchantShortName(String merchantShortName) {
        this.merchantShortName = merchantShortName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBusinessAdditionDesc() {
        return businessAdditionDesc;
    }

    public void setBusinessAdditionDesc(String businessAdditionDesc) {
        this.businessAdditionDesc = businessAdditionDesc;
    }

    public String getBusinessAdditionPics() {
        return businessAdditionPics;
    }

    public void setBusinessAdditionPics(String businessAdditionPics) {
        this.businessAdditionPics = businessAdditionPics;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    protected void checkConstraints() throws WxPayException {

    }
}
