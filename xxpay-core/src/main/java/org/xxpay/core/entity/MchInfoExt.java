package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户扩展信息表（进件）
 * </p>
 *
 * @author xxpay generator
 * @since 2020-07-30
 */
@TableName("t_mch_info_ext")
public class MchInfoExt extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @TableId("MchId")
    private Long mchId;

    /**
     * 代理商ID
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 法人姓名
     */
    @TableField("LegalPersonName")
    private String legalPersonName;

    /**
     * 法人电话
     */
    @TableField("LegalPersonPhone")
    private String legalPersonPhone;

    /**
     * 法人身份证号码
     */
    @TableField("LegalPersonIdCardNo")
    private String legalPersonIdCardNo;

    /**
     * 法人身份证有效期: 格式为：[yyyy-MM-dd_yyyy-MM-dd] 0_0表示长期有效
     */
    @TableField("LegalPersonIdCardDate")
    private String legalPersonIdCardDate;

    /**
     * 营业执照编号
     */
    @TableField("BusinessLicenseNo")
    private String businessLicenseNo;

    /**
     * 营业执照有效期: 格式为：[yyyy-MM-dd_yyyy-MM-dd] 0_0表示长期有效
     */
    @TableField("BusinessLicenseDate")
    private String businessLicenseDate;

    /**
     * 客服电话
     */
    @TableField("ServicePhone")
    private String servicePhone;

    /**
     * 行业编码
     */
    @TableField("IndustryCode")
    private Integer industryCode;

    /**
     * 账户属性:0-对私,1-对公,默认对私
     */
    @TableField("SettAccAttr")
    private Byte settAccAttr;

    /**
     * 银行卡号
     */
    @TableField("SettAccNo")
    private String settAccNo;

    /**
     * 开户名称(持卡人姓名)
     */
    @TableField("SettAccName")
    private String settAccName;

    /**
     * 银行名称
     */
    @TableField("SettBankName")
    private String settBankName;

    /**
     * 支行名称
     */
    @TableField("SettBankNetName")
    private String settBankNetName;

    /**
     * 开户地区编号，省
     */
    @TableField("SettBankProvinceCode")
    private Integer settBankProvinceCode;

    /**
     * 开户地区编号， 市
     */
    @TableField("SettBankCityCode")
    private Integer settBankCityCode;

    /**
     * 开户地区编号， 县
     */
    @TableField("SettBankAreaCode")
    private Integer settBankAreaCode;

    /**
     * 开户省市县名称描述
     */
    @TableField("SettBankAreaInfo")
    private String settBankAreaInfo;

    /**
     * 门头照
     */
    @TableField("StoreOuterImg")
    private String storeOuterImg;

    /**
     * 经营场所照
     */
    @TableField("StoreInnerImg")
    private String storeInnerImg;

    /**
     * 收银台照
     */
    @TableField("CashierImg")
    private String cashierImg;

    /**
     * 法人身份证正面
     */
    @TableField("LegalIdCard1Img")
    private String legalIdCard1Img;

    /**
     * 法人身份证反面
     */
    @TableField("LegalIdCard2Img")
    private String legalIdCard2Img;

    /**
     * 开户许可证照
     */
    @TableField("OpenAccountImg")
    private String openAccountImg;

    /**
     * 营业执照
     */
    @TableField("BusinessLicenseImg")
    private String businessLicenseImg;

    /**
     * 特殊资质
     */
    @TableField("Qualifications")
    private String qualifications;

    /**
     * 结算人身份证正面
     */
    @TableField("SettIdCard1Img")
    private String settIdCard1Img;

    /**
     * 结算人身份证反面
     */
    @TableField("SettIdCard2Img")
    private String settIdCard2Img;

    /**
     * 手持身份证照片
     */
    @TableField("SettIdCard3Img")
    private String settIdCard3Img;

    /**
     * 非法人对私授权函
     */
    @TableField("AuthorizeImg")
    private String authorizeImg;

    /**
     * 租房合同
     */
    @TableField("LeaseImg")
    private String leaseImg;

    /**
     * 银行卡照
     */
    @TableField("BankCardImg")
    private String bankCardImg;

    /**
     * 其他资料-1
     */
    @TableField("Other1Img")
    private String other1Img;

    /**
     * 其他资料-2
     */
    @TableField("Other2Img")
    private String other2Img;

    /**
     * 申请费率
     */
    @TableField("ApplyRateInfo")
    private String applyRateInfo;

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

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }
    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }
    public String getLegalPersonIdCardNo() {
        return legalPersonIdCardNo;
    }

    public void setLegalPersonIdCardNo(String legalPersonIdCardNo) {
        this.legalPersonIdCardNo = legalPersonIdCardNo;
    }
    public String getLegalPersonIdCardDate() {
        return legalPersonIdCardDate;
    }

    public void setLegalPersonIdCardDate(String legalPersonIdCardDate) {
        this.legalPersonIdCardDate = legalPersonIdCardDate;
    }
    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }
    public String getBusinessLicenseDate() {
        return businessLicenseDate;
    }

    public void setBusinessLicenseDate(String businessLicenseDate) {
        this.businessLicenseDate = businessLicenseDate;
    }
    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }
    public Integer getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(Integer industryCode) {
        this.industryCode = industryCode;
    }
    public Byte getSettAccAttr() {
        return settAccAttr;
    }

    public void setSettAccAttr(Byte settAccAttr) {
        this.settAccAttr = settAccAttr;
    }
    public String getSettAccNo() {
        return settAccNo;
    }

    public void setSettAccNo(String settAccNo) {
        this.settAccNo = settAccNo;
    }
    public String getSettAccName() {
        return settAccName;
    }

    public void setSettAccName(String settAccName) {
        this.settAccName = settAccName;
    }
    public String getSettBankName() {
        return settBankName;
    }

    public void setSettBankName(String settBankName) {
        this.settBankName = settBankName;
    }
    public String getSettBankNetName() {
        return settBankNetName;
    }

    public void setSettBankNetName(String settBankNetName) {
        this.settBankNetName = settBankNetName;
    }
    public Integer getSettBankProvinceCode() {
        return settBankProvinceCode;
    }

    public void setSettBankProvinceCode(Integer settBankProvinceCode) {
        this.settBankProvinceCode = settBankProvinceCode;
    }
    public Integer getSettBankCityCode() {
        return settBankCityCode;
    }

    public void setSettBankCityCode(Integer settBankCityCode) {
        this.settBankCityCode = settBankCityCode;
    }
    public Integer getSettBankAreaCode() {
        return settBankAreaCode;
    }

    public void setSettBankAreaCode(Integer settBankAreaCode) {
        this.settBankAreaCode = settBankAreaCode;
    }
    public String getSettBankAreaInfo() {
        return settBankAreaInfo;
    }

    public void setSettBankAreaInfo(String settBankAreaInfo) {
        this.settBankAreaInfo = settBankAreaInfo;
    }
    public String getStoreOuterImg() {
        return storeOuterImg;
    }

    public void setStoreOuterImg(String storeOuterImg) {
        this.storeOuterImg = storeOuterImg;
    }
    public String getStoreInnerImg() {
        return storeInnerImg;
    }

    public void setStoreInnerImg(String storeInnerImg) {
        this.storeInnerImg = storeInnerImg;
    }
    public String getCashierImg() {
        return cashierImg;
    }

    public void setCashierImg(String cashierImg) {
        this.cashierImg = cashierImg;
    }
    public String getLegalIdCard1Img() {
        return legalIdCard1Img;
    }

    public void setLegalIdCard1Img(String legalIdCard1Img) {
        this.legalIdCard1Img = legalIdCard1Img;
    }
    public String getLegalIdCard2Img() {
        return legalIdCard2Img;
    }

    public void setLegalIdCard2Img(String legalIdCard2Img) {
        this.legalIdCard2Img = legalIdCard2Img;
    }
    public String getOpenAccountImg() {
        return openAccountImg;
    }

    public void setOpenAccountImg(String openAccountImg) {
        this.openAccountImg = openAccountImg;
    }
    public String getBusinessLicenseImg() {
        return businessLicenseImg;
    }

    public void setBusinessLicenseImg(String businessLicenseImg) {
        this.businessLicenseImg = businessLicenseImg;
    }
    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }
    public String getSettIdCard1Img() {
        return settIdCard1Img;
    }

    public void setSettIdCard1Img(String settIdCard1Img) {
        this.settIdCard1Img = settIdCard1Img;
    }
    public String getSettIdCard2Img() {
        return settIdCard2Img;
    }

    public void setSettIdCard2Img(String settIdCard2Img) {
        this.settIdCard2Img = settIdCard2Img;
    }
    public String getSettIdCard3Img() {
        return settIdCard3Img;
    }

    public void setSettIdCard3Img(String settIdCard3Img) {
        this.settIdCard3Img = settIdCard3Img;
    }
    public String getAuthorizeImg() {
        return authorizeImg;
    }

    public void setAuthorizeImg(String authorizeImg) {
        this.authorizeImg = authorizeImg;
    }
    public String getLeaseImg() {
        return leaseImg;
    }

    public void setLeaseImg(String leaseImg) {
        this.leaseImg = leaseImg;
    }
    public String getBankCardImg() {
        return bankCardImg;
    }

    public void setBankCardImg(String bankCardImg) {
        this.bankCardImg = bankCardImg;
    }
    public String getOther1Img() {
        return other1Img;
    }

    public void setOther1Img(String other1Img) {
        this.other1Img = other1Img;
    }
    public String getOther2Img() {
        return other2Img;
    }

    public void setOther2Img(String other2Img) {
        this.other2Img = other2Img;
    }
    public String getApplyRateInfo() {
        return applyRateInfo;
    }

    public void setApplyRateInfo(String applyRateInfo) {
        this.applyRateInfo = applyRateInfo;
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
        return "MchInfoExt{" +
                "MchId=" + mchId +
                ", agentId=" + agentId +
                ", isvId=" + isvId +
                ", legalPersonName=" + legalPersonName +
                ", legalPersonPhone=" + legalPersonPhone +
                ", legalPersonIdCardNo=" + legalPersonIdCardNo +
                ", legalPersonIdCardDate=" + legalPersonIdCardDate +
                ", businessLicenseNo=" + businessLicenseNo +
                ", businessLicenseDate=" + businessLicenseDate +
                ", servicePhone=" + servicePhone +
                ", industryCode=" + industryCode +
                ", settAccAttr=" + settAccAttr +
                ", settAccNo=" + settAccNo +
                ", settAccName=" + settAccName +
                ", settBankName=" + settBankName +
                ", settBankNetName=" + settBankNetName +
                ", settBankProvinceCode=" + settBankProvinceCode +
                ", settBankCityCode=" + settBankCityCode +
                ", settBankAreaCode=" + settBankAreaCode +
                ", settBankAreaInfo=" + settBankAreaInfo +
                ", storeOuterImg=" + storeOuterImg +
                ", storeInnerImg=" + storeInnerImg +
                ", cashierImg=" + cashierImg +
                ", legalIdCard1Img=" + legalIdCard1Img +
                ", legalIdCard2Img=" + legalIdCard2Img +
                ", openAccountImg=" + openAccountImg +
                ", businessLicenseImg=" + businessLicenseImg +
                ", qualifications=" + qualifications +
                ", settIdCard1Img=" + settIdCard1Img +
                ", settIdCard2Img=" + settIdCard2Img +
                ", settIdCard3Img=" + settIdCard3Img +
                ", authorizeImg=" + authorizeImg +
                ", leaseImg=" + leaseImg +
                ", bankCardImg=" + bankCardImg +
                ", other1Img=" + other1Img +
                ", other2Img=" + other2Img +
                ", applyRateInfo=" + applyRateInfo +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
