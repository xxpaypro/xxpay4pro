package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 随行付商户入驻快照信息
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-11
 */
@TableName("t_sxf_mch_snapshot")
public class SxfMchSnapshot extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 申请编号
     */
    @TableId(value = "ApplyNo", type= IdType.AUTO)
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
     * 商户简称
     */
    @TableField("MecDisNm")
    private String mecDisNm;

    /**
     * 商户联系电话
     */
    @TableField("MblNo")
    private String mblNo;

    /**
     * 客服联系电话
     */
    @TableField("CsTelNo")
    private String csTelNo;

    /**
     * 法人/负责人
     */
    @TableField("IdentityName")
    private String identityName;

    /**
     * 00-身份证，03-军人证，04-警察证，05-港澳通行证，06-台湾通行证，07护照，98-单位证件，99-其他
     */
    @TableField("IdentityTyp")
    private String identityTyp;

    /**
     * 法人/负责人证件号码
     */
    @TableField("IdentityNo")
    private String identityNo;

    /**
     * 法人身份证正面
     */
    @TableField("LegalPersonidPositivePic")
    private String legalPersonidPositivePic;

    /**
     * 法人身份证反面
     */
    @TableField("LegalPersonidOppositePic")
    private String legalPersonidOppositePic;

    /**
     * 经营类型：01-线下，02-线上，03非盈利类，04-缴费类，05-保险类，06-私立院校
     */
    @TableField("OperationalType")
    private String operationalType;

    /**
     * 资质类型：01-自然人，02-个体户，03-企业（线上商户必须为03）
     */
    @TableField("HaveLicenseNo")
    private String haveLicenseNo;

    /**
     * 商户类型：00-普通单店商户，01-连锁总店，02-连锁分店，03-1+n总店，04-1+n分店
     */
    @TableField("MecTypeFlag")
    private String mecTypeFlag;

    /**
     * 所属总店商户编号（商户类型为01/02必填）
     */
    @TableField("ParentMno")
    private String parentMno;

    /**
     * 二维码费率 json形式传入 01-微信，02-支付宝，06-银联单笔小于等于1000,07银联单笔大于1000
     */
    @TableField("QrcodeList")
    private String qrcodeList;

    /**
     * 线上产品类型：01-APP，02-网站，03-公众号/小程序（线上类型商户必传）
     */
    @TableField("OnlineType")
    private String onlineType;

    /**
     * 线上产品名称
     */
    @TableField("OnlineName")
    private String onlineName;

    /**
     * app下载地址及账号信息
     */
    @TableField("OnlineTypeInfo")
    private String onlineTypeInfo;

    /**
     * 营业执照注册名称（企业、个体户必传）
     */
    @TableField("CprRegNmCn")
    private String cprRegNmCn;

    /**
     * 营业执照注册号（企业、个体户必传）
     */
    @TableField("RegistCode")
    private String registCode;

    /**
     * 是否三证合一：00-是，01-否（企业必传）
     */
    @TableField("LicenseMatch")
    private String licenseMatch;

    /**
     * 商户经营详细地址
     */
    @TableField("CprRegAddr")
    private String cprRegAddr;

    /**
     * 门头照
     */
    @TableField("StorePic")
    private String storePic;

    /**
     * 经营场所照
     */
    @TableField("InsideScenePic")
    private String insideScenePic;

    /**
     * 商户经营地址省份（编码）
     */
    @TableField("RegProvCd")
    private String regProvCd;

    /**
     * 商户经营地址市（编码）
     */
    @TableField("RegCityCd")
    private String regCityCd;

    /**
     * 商户经营地址区（编码）
     */
    @TableField("RegDistCd")
    private String regDistCd;

    /**
     * 经营类目
     */
    @TableField("MccCd")
    private String mccCd;

    /**
     * 账户类型:00-对公,01-对私,默认对私
     */
    @TableField("ActTyp")
    private Byte actTyp;

    /**
     * 结算账户名
     */
    @TableField("ActNm")
    private String actNm;

    /**
     * 结算银行名称
     */
    @TableField("DepoBank")
    private String depoBank;

    /**
     * 银行卡正面照
     */
    @TableField("BankCardPositivePic")
    private String bankCardPositivePic;

    /**
     * 开户地区编号，省
     */
    @TableField("DepoProvCd")
    private Integer depoProvCd;

    /**
     * 开户地区编号， 市
     */
    @TableField("DepoCityCd")
    private Integer depoCityCd;

    /**
     * 开户支行联行行号
     */
    @TableField("LbnkNo")
    private String lbnkNo;

    /**
     * 结算卡号
     */
    @TableField("ActNo")
    private String actNo;

    /**
     * 结算人身份证号码（对私必传）
     */
    @TableField("StmManIdNo")
    private String stmManIdNo;

    /**
     * 结算人身份证正面
     */
    @TableField("SettlePersonIdcardPositive")
    private String settlePersonIdcardPositive;

    /**
     * 结算人身份证反面
     */
    @TableField("SettlePersonIdcardOpposite")
    private String settlePersonIdcardOpposite;

    /**
     * 营业执照
     */
    @TableField("LicensePic")
    private String licensePic;

    /**
     * 税务登记证照片（非三合一证必传）
     */
    @TableField("TaxRegistLicensePic")
    private String taxRegistLicensePic;

    /**
     * 组织机构代码证（非三合一证必传）
     */
    @TableField("OrgCodePic")
    private String orgCodePic;

    /**
     * 开户许可证（对公结算必传）
     */
    @TableField("OpeningAccountLicensePic")
    private String openingAccountLicensePic;

    /**
     * ICP许可证或公众号主体信息截图（线上网站类、公众号商户必传）
     */
    @TableField("IcpLicence")
    private String icpLicence;

    /**
     * 非法人对私授权函
     */
    @TableField("LetterOfAuthPic")
    private String letterOfAuthPic;

    /**
     * 商户入驻状态：-1-未入住，0-入驻审核中, 1-入驻通过, 2-入驻驳回, 3-入驻图片驳回
     */
    @TableField("ApplyStatus")
    private Byte applyStatus;

    /**
     * 其他资料-1
     */
    @TableField("OtherMaterialPictureOne")
    private String otherMaterialPictureOne;

    /**
     * 其他资料-2
     */
    @TableField("OtherMaterialPictureTwo")
    private String otherMaterialPictureTwo;

    /**
     * 随行付返回进件申请ID
     */
    @TableField("ApplicationId")
    private String applicationId;

    /**
     * 返回审核信息
     */
    @TableField("AuditInfo")
    private String auditInfo;

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
    public String getMecDisNm() {
        return mecDisNm;
    }

    public void setMecDisNm(String mecDisNm) {
        this.mecDisNm = mecDisNm;
    }
    public String getMblNo() {
        return mblNo;
    }

    public void setMblNo(String mblNo) {
        this.mblNo = mblNo;
    }
    public String getCsTelNo() {
        return csTelNo;
    }

    public void setCsTelNo(String csTelNo) {
        this.csTelNo = csTelNo;
    }
    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }
    public String getIdentityTyp() {
        return identityTyp;
    }

    public void setIdentityTyp(String identityTyp) {
        this.identityTyp = identityTyp;
    }
    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }
    public String getLegalPersonidPositivePic() {
        return legalPersonidPositivePic;
    }

    public void setLegalPersonidPositivePic(String legalPersonidPositivePic) {
        this.legalPersonidPositivePic = legalPersonidPositivePic;
    }
    public String getLegalPersonidOppositePic() {
        return legalPersonidOppositePic;
    }

    public void setLegalPersonidOppositePic(String legalPersonidOppositePic) {
        this.legalPersonidOppositePic = legalPersonidOppositePic;
    }
    public String getOperationalType() {
        return operationalType;
    }

    public void setOperationalType(String operationalType) {
        this.operationalType = operationalType;
    }
    public String getHaveLicenseNo() {
        return haveLicenseNo;
    }

    public void setHaveLicenseNo(String haveLicenseNo) {
        this.haveLicenseNo = haveLicenseNo;
    }
    public String getMecTypeFlag() {
        return mecTypeFlag;
    }

    public void setMecTypeFlag(String mecTypeFlag) {
        this.mecTypeFlag = mecTypeFlag;
    }
    public String getParentMno() {
        return parentMno;
    }

    public void setParentMno(String parentMno) {
        this.parentMno = parentMno;
    }
    public String getQrcodeList() {
        return qrcodeList;
    }

    public void setQrcodeList(String qrcodeList) {
        this.qrcodeList = qrcodeList;
    }
    public String getOnlineType() {
        return onlineType;
    }

    public void setOnlineType(String onlineType) {
        this.onlineType = onlineType;
    }
    public String getOnlineName() {
        return onlineName;
    }

    public void setOnlineName(String onlineName) {
        this.onlineName = onlineName;
    }
    public String getOnlineTypeInfo() {
        return onlineTypeInfo;
    }

    public void setOnlineTypeInfo(String onlineTypeInfo) {
        this.onlineTypeInfo = onlineTypeInfo;
    }
    public String getCprRegNmCn() {
        return cprRegNmCn;
    }

    public void setCprRegNmCn(String cprRegNmCn) {
        this.cprRegNmCn = cprRegNmCn;
    }
    public String getRegistCode() {
        return registCode;
    }

    public void setRegistCode(String registCode) {
        this.registCode = registCode;
    }
    public String getLicenseMatch() {
        return licenseMatch;
    }

    public void setLicenseMatch(String licenseMatch) {
        this.licenseMatch = licenseMatch;
    }
    public String getCprRegAddr() {
        return cprRegAddr;
    }

    public void setCprRegAddr(String cprRegAddr) {
        this.cprRegAddr = cprRegAddr;
    }
    public String getStorePic() {
        return storePic;
    }

    public void setStorePic(String storePic) {
        this.storePic = storePic;
    }
    public String getInsideScenePic() {
        return insideScenePic;
    }

    public void setInsideScenePic(String insideScenePic) {
        this.insideScenePic = insideScenePic;
    }
    public String getRegProvCd() {
        return regProvCd;
    }

    public void setRegProvCd(String regProvCd) {
        this.regProvCd = regProvCd;
    }
    public String getRegCityCd() {
        return regCityCd;
    }

    public void setRegCityCd(String regCityCd) {
        this.regCityCd = regCityCd;
    }
    public String getRegDistCd() {
        return regDistCd;
    }

    public void setRegDistCd(String regDistCd) {
        this.regDistCd = regDistCd;
    }
    public String getMccCd() {
        return mccCd;
    }

    public void setMccCd(String mccCd) {
        this.mccCd = mccCd;
    }
    public Byte getActTyp() {
        return actTyp;
    }

    public void setActTyp(Byte actTyp) {
        this.actTyp = actTyp;
    }
    public String getActNm() {
        return actNm;
    }

    public void setActNm(String actNm) {
        this.actNm = actNm;
    }
    public String getDepoBank() {
        return depoBank;
    }

    public void setDepoBank(String depoBank) {
        this.depoBank = depoBank;
    }
    public String getBankCardPositivePic() {
        return bankCardPositivePic;
    }

    public void setBankCardPositivePic(String bankCardPositivePic) {
        this.bankCardPositivePic = bankCardPositivePic;
    }
    public Integer getDepoProvCd() {
        return depoProvCd;
    }

    public void setDepoProvCd(Integer depoProvCd) {
        this.depoProvCd = depoProvCd;
    }
    public Integer getDepoCityCd() {
        return depoCityCd;
    }

    public void setDepoCityCd(Integer depoCityCd) {
        this.depoCityCd = depoCityCd;
    }
    public String getLbnkNo() {
        return lbnkNo;
    }

    public void setLbnkNo(String lbnkNo) {
        this.lbnkNo = lbnkNo;
    }
    public String getActNo() {
        return actNo;
    }

    public void setActNo(String actNo) {
        this.actNo = actNo;
    }
    public String getStmManIdNo() {
        return stmManIdNo;
    }

    public void setStmManIdNo(String stmManIdNo) {
        this.stmManIdNo = stmManIdNo;
    }
    public String getSettlePersonIdcardPositive() {
        return settlePersonIdcardPositive;
    }

    public void setSettlePersonIdcardPositive(String settlePersonIdcardPositive) {
        this.settlePersonIdcardPositive = settlePersonIdcardPositive;
    }
    public String getSettlePersonIdcardOpposite() {
        return settlePersonIdcardOpposite;
    }

    public void setSettlePersonIdcardOpposite(String settlePersonIdcardOpposite) {
        this.settlePersonIdcardOpposite = settlePersonIdcardOpposite;
    }
    public String getLicensePic() {
        return licensePic;
    }

    public void setLicensePic(String licensePic) {
        this.licensePic = licensePic;
    }
    public String getTaxRegistLicensePic() {
        return taxRegistLicensePic;
    }

    public void setTaxRegistLicensePic(String taxRegistLicensePic) {
        this.taxRegistLicensePic = taxRegistLicensePic;
    }
    public String getOrgCodePic() {
        return orgCodePic;
    }

    public void setOrgCodePic(String orgCodePic) {
        this.orgCodePic = orgCodePic;
    }
    public String getOpeningAccountLicensePic() {
        return openingAccountLicensePic;
    }

    public void setOpeningAccountLicensePic(String openingAccountLicensePic) {
        this.openingAccountLicensePic = openingAccountLicensePic;
    }
    public String getIcpLicence() {
        return icpLicence;
    }

    public void setIcpLicence(String icpLicence) {
        this.icpLicence = icpLicence;
    }
    public String getLetterOfAuthPic() {
        return letterOfAuthPic;
    }

    public void setLetterOfAuthPic(String letterOfAuthPic) {
        this.letterOfAuthPic = letterOfAuthPic;
    }
    public Byte getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(Byte applyStatus) {
        this.applyStatus = applyStatus;
    }
    public String getOtherMaterialPictureOne() {
        return otherMaterialPictureOne;
    }

    public void setOtherMaterialPictureOne(String otherMaterialPictureOne) {
        this.otherMaterialPictureOne = otherMaterialPictureOne;
    }
    public String getOtherMaterialPictureTwo() {
        return otherMaterialPictureTwo;
    }

    public void setOtherMaterialPictureTwo(String otherMaterialPictureTwo) {
        this.otherMaterialPictureTwo = otherMaterialPictureTwo;
    }
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo;
    }

    @Override
    public String toString() {
        return "SxfMchSnapshot{" +
                "ApplyNo=" + applyNo +
                ", mchId=" + mchId +
                ", isvId=" + isvId +
                ", mecDisNm=" + mecDisNm +
                ", mblNo=" + mblNo +
                ", csTelNo=" + csTelNo +
                ", identityName=" + identityName +
                ", identityTyp=" + identityTyp +
                ", identityNo=" + identityNo +
                ", legalPersonidPositivePic=" + legalPersonidPositivePic +
                ", legalPersonidOppositePic=" + legalPersonidOppositePic +
                ", operationalType=" + operationalType +
                ", haveLicenseNo=" + haveLicenseNo +
                ", mecTypeFlag=" + mecTypeFlag +
                ", parentMno=" + parentMno +
                ", qrcodeList=" + qrcodeList +
                ", onlineType=" + onlineType +
                ", onlineName=" + onlineName +
                ", onlineTypeInfo=" + onlineTypeInfo +
                ", cprRegNmCn=" + cprRegNmCn +
                ", registCode=" + registCode +
                ", licenseMatch=" + licenseMatch +
                ", cprRegAddr=" + cprRegAddr +
                ", storePic=" + storePic +
                ", insideScenePic=" + insideScenePic +
                ", regProvCd=" + regProvCd +
                ", regCityCd=" + regCityCd +
                ", regDistCd=" + regDistCd +
                ", mccCd=" + mccCd +
                ", actTyp=" + actTyp +
                ", actNm=" + actNm +
                ", depoBank=" + depoBank +
                ", bankCardPositivePic=" + bankCardPositivePic +
                ", depoProvCd=" + depoProvCd +
                ", depoCityCd=" + depoCityCd +
                ", lbnkNo=" + lbnkNo +
                ", actNo=" + actNo +
                ", stmManIdNo=" + stmManIdNo +
                ", settlePersonIdcardPositive=" + settlePersonIdcardPositive +
                ", settlePersonIdcardOpposite=" + settlePersonIdcardOpposite +
                ", licensePic=" + licensePic +
                ", taxRegistLicensePic=" + taxRegistLicensePic +
                ", orgCodePic=" + orgCodePic +
                ", openingAccountLicensePic=" + openingAccountLicensePic +
                ", icpLicence=" + icpLicence +
                ", letterOfAuthPic=" + letterOfAuthPic +
                ", applyStatus=" + applyStatus +
                ", otherMaterialPictureOne=" + otherMaterialPictureOne +
                ", otherMaterialPictureTwo=" + otherMaterialPictureTwo +
                ", applicationId=" + applicationId +
                ", auditInfo=" + auditInfo +
                "}";
    }
}
