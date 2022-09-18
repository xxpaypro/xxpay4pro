package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 微信商户入驻快照信息
 * </p>
 *
 * @author xxpay generator
 * @since 2020-03-04
 */
@TableName("t_wx_mch_snapshot")
public class WxMchSnapshot extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 申请编号
     */
    @TableId(value = "ApplyNo", type = IdType.AUTO)
    private Long applyNo;

    /**
     * 商户ID
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 小微商户状态：0-未入驻, 1-小微商户审核中, 2-小微商户审核通过, 3-待商户签约, 4-请求异常/微信驳回, 5-待账户验证, 6-已作废, 7-编辑中, 8-已冻结
     */
    @TableField("ApplyStatus")
    private Byte applyStatus;

    /**
     * 小微商户审核信息
     */
    @TableField("AuditInfo")
    private String auditInfo;

    /**
     * 身份证人像面照片
     */
    @TableField("IdCardCopy")
    private String idCardCopy;

    /**
     * 身份证国徽面照片
     */
    @TableField("IdCardNational")
    private String idCardNational;

    /**
     * 身份证姓名
     */
    @TableField("IdCardName")
    private String idCardName;

    /**
     * 身份证号码
     */
    @TableField("IdCardNumber")
    private String idCardNumber;

    /**
     * 开户名称
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 开户银行
     */
    @TableField("AccountBank")
    private String accountBank;

    /**
     * 开户银行省市编码
     */
    @TableField("BankAddressCode")
    private String bankAddressCode;

    /**
     * 开户银行全称（含支行）
     */
    @TableField("BankName")
    private String bankName;

    /**
     * 银行账号
     */
    @TableField("AccountNumber")
    private String accountNumber;

    /**
     * 门店名称
     */
    @TableField("StoreName")
    private String storeName;

    /**
     * 门店省市编码
     */
    @TableField("StoreAddressCode")
    private String storeAddressCode;

    /**
     * 门店街道名称
     */
    @TableField("StoreStreet")
    private String storeStreet;

    /**
     * 门店门口照片
     */
    @TableField("StoreEntrancePic")
    private String storeEntrancePic;

    /**
     * 店内环境照片
     */
    @TableField("IndoorPic")
    private String indoorPic;

    /**
     * 商户简称
     */
    @TableField("MerchantShortName")
    private String merchantShortName;

    /**
     * 客服电话
     */
    @TableField("ServicePhone")
    private String servicePhone;

    /**
     * 补充说明
     */
    @TableField("BusinessAdditionDesc")
    private String businessAdditionDesc;

    /**
     * 补充材料
     */
    @TableField("BusinessAdditionPics")
    private String businessAdditionPics;

    /**
     * 超级管理员姓名
     */
    @TableField("Contact")
    private String contact;

    /**
     * 手机号码
     */
    @TableField("ContactPhone")
    private String contactPhone;

    /**
     * 联系邮箱
     */
    @TableField("ContactEmail")
    private String contactEmail;

    /**
     * 微信返回申请单号
     */
    @TableField("WxApplymentId")
    private String wxApplymentId;

    /**
     * 微信返回商户进件二维码图片地址
     */
    @TableField("WxApplymentMchQrImg")
    private String wxApplymentMchQrImg;

    /**
     * 微信返回商户编号
     */
    @TableField("WxMchId")
    private String wxMchId;

    /**
     * 主体类型 SUBJECT_TYPE_INDIVIDUAL（个体户） SUBJECT_TYPE_ENTERPRISE（企业）
     */
    @TableField("OrganizationType")
    private String organizationType;

    /**
     * 营业执照图片
     */
    @TableField("BusinessLicenseCopy")
    private String businessLicenseCopy;

    /**
     * 营业执照注册号
     */
    @TableField("BusinessLicenseNumber")
    private String businessLicenseNumber;

    /**
     * 营业执照上的商户名称
     */
    @TableField("BusinessMerchantName")
    private String businessMerchantName;

    /**
     * 经营者姓名/法定代表人
     */
    @TableField("LegalPerson")
    private String legalPerson;

    /**
     * 经营场景类型 线下门店：SALES_SCENES_STORE
     */
    @TableField("SalesSceneType")
    private String salesSceneType;

    /**
     * 公众号APPID
     */
    @TableField("MpAppid")
    private String mpAppid;

    /**
     * 入驻结算规则ID
     */
    @TableField("SettlementId")
    private String settlementId;

    /**
     * 所属行业
     */
    @TableField("QualificationType")
    private String qualificationType;

    /**
     * 账户类型 BANK_ACCOUNT_TYPE_CORPORATE：对公银行账户  BANK_ACCOUNT_TYPE_PERSONAL：经营者个人银行卡
     */
    @TableField("BankAccountType")
    private String bankAccountType;

    /**
     * 身份证有效期开始时间
     */
    @TableField("IdCardValidStartTime")
    private String idCardValidStartTime;

    /**
     * 身份证有效期结束时间
     */
    @TableField("IdCardValidEndTime")
    private String idCardValidEndTime;

    /**
     * 超级管理员类型
     */
    @TableField("ContactType")
    private String contactType;

    /**
     * 特殊资质， 图片集合 []
     */
    @TableField("Qualifications")
    private String qualifications;

    /**
     * 超级管理员身份证号
     */
    @TableField("ContactIdCardNo")
    private String contactIdCardNo;

    /**
     * 店铺链接
     */
    @TableField("StoreUrl")
    private String storeUrl;

    /**
     * 进件商户类型：1-特约商户进件，2-收付通小微进件
     */
    @TableField("ApplyType")
    private Byte applyType;

    public Long getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(Long applyNo) {
        this.applyNo = applyNo;
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
    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }
    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo;
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
    public String getWxApplymentId() {
        return wxApplymentId;
    }

    public void setWxApplymentId(String wxApplymentId) {
        this.wxApplymentId = wxApplymentId;
    }
    public String getWxApplymentMchQrImg() {
        return wxApplymentMchQrImg;
    }

    public void setWxApplymentMchQrImg(String wxApplymentMchQrImg) {
        this.wxApplymentMchQrImg = wxApplymentMchQrImg;
    }
    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }
    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }
    public String getBusinessLicenseCopy() {
        return businessLicenseCopy;
    }

    public void setBusinessLicenseCopy(String businessLicenseCopy) {
        this.businessLicenseCopy = businessLicenseCopy;
    }
    public String getBusinessLicenseNumber() {
        return businessLicenseNumber;
    }

    public void setBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
    }
    public String getBusinessMerchantName() {
        return businessMerchantName;
    }

    public void setBusinessMerchantName(String businessMerchantName) {
        this.businessMerchantName = businessMerchantName;
    }
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
    public String getSalesSceneType() {
        return salesSceneType;
    }

    public void setSalesSceneType(String salesSceneType) {
        this.salesSceneType = salesSceneType;
    }
    public String getMpAppid() {
        return mpAppid;
    }

    public void setMpAppid(String mpAppid) {
        this.mpAppid = mpAppid;
    }
    public String getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
    }
    public String getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(String qualificationType) {
        this.qualificationType = qualificationType;
    }
    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }
    public String getIdCardValidStartTime() {
        return idCardValidStartTime;
    }

    public void setIdCardValidStartTime(String idCardValidStartTime) {
        this.idCardValidStartTime = idCardValidStartTime;
    }
    public String getIdCardValidEndTime() {
        return idCardValidEndTime;
    }

    public void setIdCardValidEndTime(String idCardValidEndTime) {
        this.idCardValidEndTime = idCardValidEndTime;
    }
    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    public String getContactIdCardNo() {
        return contactIdCardNo;
    }

    public void setContactIdCardNo(String contactIdCardNo) {
        this.contactIdCardNo = contactIdCardNo;
    }
    public String getStoreUrl() {
        return storeUrl;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }
    public Byte getApplyType() {
        return applyType;
    }

    public void setApplyType(Byte applyType) {
        this.applyType = applyType;
    }

    @Override
    public String toString() {
        return "WxMchSnapshot{" +
            "ApplyNo=" + applyNo +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", applyStatus=" + applyStatus +
            ", auditInfo=" + auditInfo +
            ", idCardCopy=" + idCardCopy +
            ", idCardNational=" + idCardNational +
            ", idCardName=" + idCardName +
            ", idCardNumber=" + idCardNumber +
            ", accountName=" + accountName +
            ", accountBank=" + accountBank +
            ", bankAddressCode=" + bankAddressCode +
            ", bankName=" + bankName +
            ", accountNumber=" + accountNumber +
            ", storeName=" + storeName +
            ", storeAddressCode=" + storeAddressCode +
            ", storeStreet=" + storeStreet +
            ", storeEntrancePic=" + storeEntrancePic +
            ", indoorPic=" + indoorPic +
            ", merchantShortName=" + merchantShortName +
            ", servicePhone=" + servicePhone +
            ", businessAdditionDesc=" + businessAdditionDesc +
            ", businessAdditionPics=" + businessAdditionPics +
            ", contact=" + contact +
            ", contactPhone=" + contactPhone +
            ", contactEmail=" + contactEmail +
            ", wxApplymentId=" + wxApplymentId +
            ", wxApplymentMchQrImg=" + wxApplymentMchQrImg +
            ", wxMchId=" + wxMchId +
            ", organizationType=" + organizationType +
            ", businessLicenseCopy=" + businessLicenseCopy +
            ", businessLicenseNumber=" + businessLicenseNumber +
            ", businessMerchantName=" + businessMerchantName +
            ", legalPerson=" + legalPerson +
            ", salesSceneType=" + salesSceneType +
            ", mpAppid=" + mpAppid +
            ", settlementId=" + settlementId +
            ", qualificationType=" + qualificationType +
            ", bankAccountType=" + bankAccountType +
            ", idCardValidStartTime=" + idCardValidStartTime +
            ", idCardValidEndTime=" + idCardValidEndTime +
            ", contactType=" + contactType +
            ", qualifications=" + qualifications +
            ", contactIdCardNo=" + contactIdCardNo +
            ", storeUrl=" + storeUrl +
            ", applyType=" + applyType +
        "}";
    }
}
