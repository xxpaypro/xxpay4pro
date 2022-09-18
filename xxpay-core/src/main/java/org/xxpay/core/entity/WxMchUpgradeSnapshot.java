package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 微信商户升级快照
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-28
 */
@TableName("t_wx_mch_upgrade_snapshot")
public class WxMchUpgradeSnapshot extends BaseModel {

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
     * 普通商户状态：0-未升级, 1-微信审核中, 2-普通商户升级完成, 3-待签约, 4-待账户验证, 5-请求异常/微信驳回
     */
    @TableField("ApplyStatus")
    private Byte applyStatus;

    /**
     * 普通商户审核信息
     */
    @TableField("AuditInfo")
    private String auditInfo;

    /**
     * 小微商户号
     */
    @TableField("SubMchId")
    private String subMchId;

    /**
     * 主体类型 2-企业 4-个体工商户 3-党政、机关及事业单位  1708-其他组织
     */
    @TableField("OrganizationType")
    private String organizationType;

    /**
     * 营业执照扫描件图片
     */
    @TableField("BusinessLicenseCopy")
    private String businessLicenseCopy;

    /**
     * 营业执照注册号
     */
    @TableField("BusinessLicenseNumber")
    private String businessLicenseNumber;

    /**
     * 商户名称
     */
    @TableField("MerchantName")
    private String merchantName;

    /**
     * 注册地址
     */
    @TableField("CompanyAddress")
    private String companyAddress;

    /**
     * 经营者姓名/法定代表人
     */
    @TableField("LegalPerson")
    private String legalPerson;

    /**
     * 营业期限
     */
    @TableField("BusinessTime")
    private String businessTime;

    /**
     * 营业执照类型 1762-已三证合一 1763-未三证合一
     */
    @TableField("BusinessLicenceType")
    private String businessLicenceType;

    /**
     * 组织机构代码证照片（未三证合一必填）
     */
    @TableField("OrganizationCopy")
    private String organizationCopy;

    /**
     * 组织机构代码（未三证合一必填）
     */
    @TableField("OrganizationNumber")
    private String organizationNumber;

    /**
     * 组织机构代码有效期限（未三证合一必填）
     */
    @TableField("OrganizationTime")
    private String organizationTime;

    /**
     * 对公账户开户名称
     */
    @TableField("AccountName")
    private String accountName;

    /**
     * 对公账户开户银行
     */
    @TableField("AccountBank")
    private String accountBank;

    /**
     * 开户银行省市编码
     */
    @TableField("BankAddressCode")
    private String bankAddressCode;

    /**
     * 开户银行全称 （含支行）
     */
    @TableField("BankName")
    private String bankName;

    /**
     * 银行卡号
     */
    @TableField("AccountNumber")
    private String accountNumber;

    /**
     * 商户简称
     */
    @TableField("MerchantShortname")
    private String merchantShortname;

    /**
     * 费率结算规则ID
     */
    @TableField("Business")
    private String business;

    /**
     * 特殊资质， 图片集合 []
     */
    @TableField("Qualifications")
    private String qualifications;

    /**
     * 经营场景 1721-线下  1837-公众号  1838-小程序  1724-APP  1840-PC网站
     */
    @TableField("BusinessScene")
    private String businessScene;

    /**
     * 补充说明
     */
    @TableField("BusinessAdditionDesc")
    private String businessAdditionDesc;

    /**
     * 补充材料 图片集合 []
     */
    @TableField("BusinessAdditionPics")
    private String businessAdditionPics;

    /**
     * 联系邮箱
     */
    @TableField("ContactEmail")
    private String contactEmail;

    /**
     * 公众号APPID
     */
    @TableField("MpAppid")
    private String mpAppid;

    /**
     * 公众号页面截图 图片集合 []
     */
    @TableField("MpAppScreenShots")
    private String mpAppScreenShots;

    /**
     * 小程序APPID
     */
    @TableField("MiniprogramAppid")
    private String miniprogramAppid;

    /**
     * 小程序页面截图 图片集合 []
     */
    @TableField("MiniprogramScreenShots")
    private String miniprogramScreenShots;

    /**
     * 应用APPID
     */
    @TableField("AppAppid")
    private String appAppid;

    /**
     * APP截图 图片集合 []
     */
    @TableField("AppScreenShots")
    private String appScreenShots;

    /**
     * APP下载链接
     */
    @TableField("AppDownloadUrl")
    private String appDownloadUrl;

    /**
     * PC网站域名
     */
    @TableField("WebUrl")
    private String webUrl;

    /**
     * 网站授权函 图片
     */
    @TableField("WebAuthoriationLetter")
    private String webAuthoriationLetter;

    /**
     * PC网站对应的公众号APPID
     */
    @TableField("WebAppid")
    private String webAppid;

    /**
     * 微信返回商户签约二维码图片地址
     */
    @TableField("WxApplymentMchQrImg")
    private String wxApplymentMchQrImg;

    /**
     * 账户验证信息
     */
    @TableField("AccountVerifyInfo")
    private String accountVerifyInfo;

    /**
     * 微信返回商户编号
     */
    @TableField("WxMchId")
    private String wxMchId;

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
    public String getSubMchId() {
        return subMchId;
    }

    public void setSubMchId(String subMchId) {
        this.subMchId = subMchId;
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
    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }
    public String getBusinessTime() {
        return businessTime;
    }

    public void setBusinessTime(String businessTime) {
        this.businessTime = businessTime;
    }
    public String getBusinessLicenceType() {
        return businessLicenceType;
    }

    public void setBusinessLicenceType(String businessLicenceType) {
        this.businessLicenceType = businessLicenceType;
    }
    public String getOrganizationCopy() {
        return organizationCopy;
    }

    public void setOrganizationCopy(String organizationCopy) {
        this.organizationCopy = organizationCopy;
    }
    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }
    public String getOrganizationTime() {
        return organizationTime;
    }

    public void setOrganizationTime(String organizationTime) {
        this.organizationTime = organizationTime;
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
    public String getMerchantShortname() {
        return merchantShortname;
    }

    public void setMerchantShortname(String merchantShortname) {
        this.merchantShortname = merchantShortname;
    }
    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }
    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    public String getBusinessScene() {
        return businessScene;
    }

    public void setBusinessScene(String businessScene) {
        this.businessScene = businessScene;
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
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getMpAppid() {
        return mpAppid;
    }

    public void setMpAppid(String mpAppid) {
        this.mpAppid = mpAppid;
    }
    public String getMpAppScreenShots() {
        return mpAppScreenShots;
    }

    public void setMpAppScreenShots(String mpAppScreenShots) {
        this.mpAppScreenShots = mpAppScreenShots;
    }
    public String getMiniprogramAppid() {
        return miniprogramAppid;
    }

    public void setMiniprogramAppid(String miniprogramAppid) {
        this.miniprogramAppid = miniprogramAppid;
    }
    public String getMiniprogramScreenShots() {
        return miniprogramScreenShots;
    }

    public void setMiniprogramScreenShots(String miniprogramScreenShots) {
        this.miniprogramScreenShots = miniprogramScreenShots;
    }
    public String getAppAppid() {
        return appAppid;
    }

    public void setAppAppid(String appAppid) {
        this.appAppid = appAppid;
    }
    public String getAppScreenShots() {
        return appScreenShots;
    }

    public void setAppScreenShots(String appScreenShots) {
        this.appScreenShots = appScreenShots;
    }
    public String getAppDownloadUrl() {
        return appDownloadUrl;
    }

    public void setAppDownloadUrl(String appDownloadUrl) {
        this.appDownloadUrl = appDownloadUrl;
    }
    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
    public String getWebAuthoriationLetter() {
        return webAuthoriationLetter;
    }

    public void setWebAuthoriationLetter(String webAuthoriationLetter) {
        this.webAuthoriationLetter = webAuthoriationLetter;
    }
    public String getWebAppid() {
        return webAppid;
    }

    public void setWebAppid(String webAppid) {
        this.webAppid = webAppid;
    }

    public String getWxApplymentMchQrImg() {
        return wxApplymentMchQrImg;
    }

    public void setWxApplymentMchQrImg(String wxApplymentMchQrImg) {
        this.wxApplymentMchQrImg = wxApplymentMchQrImg;
    }
    public String getAccountVerifyInfo() {
        return accountVerifyInfo;
    }

    public void setAccountVerifyInfo(String accountVerifyInfo) {
        this.accountVerifyInfo = accountVerifyInfo;
    }
    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }

    @Override
    public String toString() {
        return "WxMchUpgradeSnapshot{" +
            "ApplyNo=" + applyNo +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", applyStatus=" + applyStatus +
            ", auditInfo=" + auditInfo +
            ", subMchId=" + subMchId +
            ", organizationType=" + organizationType +
            ", businessLicenseCopy=" + businessLicenseCopy +
            ", businessLicenseNumber=" + businessLicenseNumber +
            ", merchantName=" + merchantName +
            ", companyAddress=" + companyAddress +
            ", legalPerson=" + legalPerson +
            ", businessTime=" + businessTime +
            ", businessLicenceType=" + businessLicenceType +
            ", organizationCopy=" + organizationCopy +
            ", organizationNumber=" + organizationNumber +
            ", organizationTime=" + organizationTime +
            ", accountName=" + accountName +
            ", accountBank=" + accountBank +
            ", bankAddressCode=" + bankAddressCode +
            ", bankName=" + bankName +
            ", accountNumber=" + accountNumber +
            ", merchantShortname=" + merchantShortname +
            ", business=" + business +
            ", qualifications=" + qualifications +
            ", businessScene=" + businessScene +
            ", businessAdditionDesc=" + businessAdditionDesc +
            ", businessAdditionPics=" + businessAdditionPics +
            ", contactEmail=" + contactEmail +
            ", mpAppid=" + mpAppid +
            ", mpAppScreenShots=" + mpAppScreenShots +
            ", miniprogramAppid=" + miniprogramAppid +
            ", miniprogramScreenShots=" + miniprogramScreenShots +
            ", appAppid=" + appAppid +
            ", appScreenShots=" + appScreenShots +
            ", appDownloadUrl=" + appDownloadUrl +
            ", webUrl=" + webUrl +
            ", webAuthoriationLetter=" + webAuthoriationLetter +
            ", webAppid=" + webAppid +
            ", wxApplymentMchQrImg=" + wxApplymentMchQrImg +
            ", accountVerifyInfo=" + accountVerifyInfo +
            ", wxMchId=" + wxMchId +
        "}";
    }
}
