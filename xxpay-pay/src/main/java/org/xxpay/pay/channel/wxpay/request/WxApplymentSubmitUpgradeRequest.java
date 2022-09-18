package org.xxpay.pay.channel.wxpay.request;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/** 微信小微升级接口  **/
@XStreamAlias("xml")
public class WxApplymentSubmitUpgradeRequest extends BaseWxPayRequest {


    /** 接口版本号 */
    @XStreamAlias("version")
    private String version;

    /** 平台证书序列号 */
    @XStreamAlias("cert_sn")
    private String certSn;

    /** 主体类型 */
    @XStreamAlias("organization_type")
    private String organizationType;


    /** 营业执照扫描件 */
    @XStreamAlias("business_license_copy")
    private String businessLicenseCopy;


    /** 营业执照注册号 */
    @XStreamAlias("business_license_number")
    private String businessLicenseNumber;


    /** 商户名称 */
    @XStreamAlias("merchant_name")
    private String merchantName;


    /** 注册地址 */
    @XStreamAlias("company_address")
    private String companyAddress;


    /** 经营者姓名/法定代表人 */
    @XStreamAlias("legal_person")
    private String legalPerson;


    /** 营业期限 */
    @XStreamAlias("business_time")
    private String businessTime;


    /** 营业执照类型 */
    @XStreamAlias("business_licence_type")
    private String businessLicenceType;


    /** 组织机构代码证照片 */
    @XStreamAlias("organization_copy")
    private String organizationCopy;


    /** 组织机构代码 */
    @XStreamAlias("organization_number")
    private String organizationNumber;


    /** 组织机构代码有效期限 */
    @XStreamAlias("organization_time")
    private String organizationTime;


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


    /** 银行卡号 */
    @XStreamAlias("account_number")
    private String accountNumber;


    /** 商户简称 */
    @XStreamAlias("merchant_shortname")
    private String merchantShortname;


    /** 费率结算规则ID */
    @XStreamAlias("business")
    private String business;


    /** 特殊资质 */
    @XStreamAlias("qualifications")
    private String qualifications;


    /** 经营场景 */
    @XStreamAlias("business_scene")
    private String businessScene;


    /** 补充说明 */
    @XStreamAlias("business_addition_desc")
    private String businessAdditionDesc;


    /** 补充材料 */
    @XStreamAlias("business_addition_pics")
    private String businessAdditionPics;


    /** 联系邮箱 */
    @XStreamAlias("contact_email")
    private String contactEmail;


    /** 公众号APPID */
    @XStreamAlias("mp_appid")
    private String mpAppid;

    /** 公众号页面截图 */
    @XStreamAlias("mp_app_screen_shots")
    private String mpAppScreenShots;

    /** 小程序APPID */
    @XStreamAlias("miniprogram_appid")
    private String miniprogramAppid;

    /** 小程序页面截图 */
    @XStreamAlias("miniprogram_screen_shots")
    private String miniprogramScreenShots;

    /** 应用APPID */
    @XStreamAlias("app_appid")
    private String appAppid;

    /** APP截图 */
    @XStreamAlias("app_screen_shots")
    private String appScreenShots;

    /** APP下载链接 */
    @XStreamAlias("app_download_url")
    private String appDownloadUrl;

    /** PC网站域名 */
    @XStreamAlias("web_url")
    private String webUrl;

    /** 网站授权函 */
    @XStreamAlias("web_authoriation_letter")
    private String webAuthoriationLetter;

    /** PC网站对应的公众号APPID */
    @XStreamAlias("web_appid")
    private String webAppid;

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

    @Override
    protected void checkConstraints() throws WxPayException {

    }
}
