package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 支付宝商户入驻快照信息
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-27
 */
@TableName("t_alipay_mch_snapshot")
public class AlipayMchSnapshot extends BaseModel {

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
     * 支付宝申请状态: 0-初始态, 1-暂存, 2-支付宝审核中, 3-待商户确认, 4-商户确认成功, 5-商户超时未确认, 6-审核失败或商户拒绝
     */
    @TableField("ApplyStatus")
    private Byte applyStatus;

    /**
     * 支付宝审核信息
     */
    @TableField("AuditInfo")
    private String auditInfo;

    /**
     * 支付宝账号
     */
    @TableField("AlipayAccount")
    private String alipayAccount;

    /**
     * 联系人姓名
     */
    @TableField("ContactName")
    private String contactName;

    /**
     * 联系人手机号码
     */
    @TableField("ContactMobile")
    private String contactMobile;

    /**
     * 联系人邮箱
     */
    @TableField("ContactEmail")
    private String contactEmail;

    /**
     * mcc编号
     */
    @TableField("MccCode")
    private String mccCode;

    /**
     * 费率
     */
    @TableField("Rate")
    private String rate;

    /**
     * 营业执照编号
     */
    @TableField("BusinessLicenseNo")
    private String businessLicenseNo;

    /**
     * 营业执照截止日期: 格式为：yyyy-MM-dd
     */
    @TableField("DateLimitation")
    private String dateLimitation;

    /**
     * 是否长期有效0-否, 1-是
     */
    @TableField("LongTerm")
    private Byte longTerm;

    /**
     * 营业执照图片
     */
    @TableField("BusinessLicensePic")
    private String businessLicensePic;

    /**
     * 企业特殊资质图片
     */
    @TableField("SpecialLicensePic")
    private String specialLicensePic;

    /**
     * 门店门口照片
     */
    @TableField("ShopSignBoardPic")
    private String shopSignBoardPic;

    /**
     * 支付宝返回的事务ID
     */
    @TableField("BatchNo")
    private String batchNo;

    /**
     * 支付宝返回的商户UID
     */
    @TableField("UserId")
    private String userId;

    /**
     * 支付宝返回的商户appid
     */
    @TableField("AuthAppId")
    private String authAppId;

    /**
     * 支付宝返回的商户appAuthToken
     */
    @TableField("AppAuthToken")
    private String appAuthToken;

    /**
     * 支付宝返回的商户刷新令牌
     */
    @TableField("AppRefreshToken")
    private String appRefreshToken;

    /**
     * 支付宝应用授权有效期
     */
    @TableField("ExpiresIn")
    private String expiresIn;

    /**
     * 支付宝刷新令牌有效期
     */
    @TableField("ReExpiresIn")
    private String reExpiresIn;

    /**
     * 支付宝代理创建的appId
     */
    @TableField("AgentAppId")
    private String agentAppId;

    /**
     * 支付宝返回的商户pid
     */
    @TableField("MerchantPid")
    private String merchantPid;

    /**
     * 支付宝确认签约链接
     */
    @TableField("ConfirmUrl")
    private String confirmUrl;

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
    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getMccCode() {
        return mccCode;
    }

    public void setMccCode(String mccCode) {
        this.mccCode = mccCode;
    }
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }
    public String getDateLimitation() {
        return dateLimitation;
    }

    public void setDateLimitation(String dateLimitation) {
        this.dateLimitation = dateLimitation;
    }
    public Byte getLongTerm() {
        return longTerm;
    }

    public void setLongTerm(Byte longTerm) {
        this.longTerm = longTerm;
    }
    public String getBusinessLicensePic() {
        return businessLicensePic;
    }

    public void setBusinessLicensePic(String businessLicensePic) {
        this.businessLicensePic = businessLicensePic;
    }
    public String getSpecialLicensePic() {
        return specialLicensePic;
    }

    public void setSpecialLicensePic(String specialLicensePic) {
        this.specialLicensePic = specialLicensePic;
    }
    public String getShopSignBoardPic() {
        return shopSignBoardPic;
    }

    public void setShopSignBoardPic(String shopSignBoardPic) {
        this.shopSignBoardPic = shopSignBoardPic;
    }
    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAuthAppId() {
        return authAppId;
    }

    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }
    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }
    public String getAppRefreshToken() {
        return appRefreshToken;
    }

    public void setAppRefreshToken(String appRefreshToken) {
        this.appRefreshToken = appRefreshToken;
    }
    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }
    public String getReExpiresIn() {
        return reExpiresIn;
    }

    public void setReExpiresIn(String reExpiresIn) {
        this.reExpiresIn = reExpiresIn;
    }
    public String getAgentAppId() {
        return agentAppId;
    }

    public void setAgentAppId(String agentAppId) {
        this.agentAppId = agentAppId;
    }
    public String getMerchantPid() {
        return merchantPid;
    }

    public void setMerchantPid(String merchantPid) {
        this.merchantPid = merchantPid;
    }
    public String getConfirmUrl() {
        return confirmUrl;
    }

    public void setConfirmUrl(String confirmUrl) {
        this.confirmUrl = confirmUrl;
    }

    @Override
    public String toString() {
        return "AlipayMchSnapshot{" +
            "ApplyNo=" + applyNo +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", applyStatus=" + applyStatus +
            ", auditInfo=" + auditInfo +
            ", alipayAccount=" + alipayAccount +
            ", contactName=" + contactName +
            ", contactMobile=" + contactMobile +
            ", contactEmail=" + contactEmail +
            ", mccCode=" + mccCode +
            ", rate=" + rate +
            ", businessLicenseNo=" + businessLicenseNo +
            ", dateLimitation=" + dateLimitation +
            ", longTerm=" + longTerm +
            ", businessLicensePic=" + businessLicensePic +
            ", specialLicensePic=" + specialLicensePic +
            ", shopSignBoardPic=" + shopSignBoardPic +
            ", batchNo=" + batchNo +
            ", userId=" + userId +
            ", authAppId=" + authAppId +
            ", appAuthToken=" + appAuthToken +
            ", appRefreshToken=" + appRefreshToken +
            ", expiresIn=" + expiresIn +
            ", reExpiresIn=" + reExpiresIn +
            ", agentAppId=" + agentAppId +
            ", merchantPid=" + merchantPid +
            ", confirmUrl=" + confirmUrl +
        "}";
    }
}
