package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 商户基础信息表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-11-13
 */
@TableName("t_mch_info")
public class MchInfo extends BaseModel<MchInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 商户ID
     */
    @TableId(value = "MchId", type = IdType.AUTO)
    private Long mchId;

    /**
     * 商户全称
     */
    @TableField("MchName")
    private String mchName;

    /**
     * 商户简称
     */
    @TableField("MchShortName")
    private String mchShortName;

    /**
     * 联系人真实姓名
     */
    @TableField("ContactRealName")
    private String contactRealName;

    /**
     * 登录用户名
     */
    @TableField("LoginUserName")
    private String loginUserName;

    /**
     * 登录手机号（联系人手机号）
     */
    @TableField("LoginMobile")
    private String loginMobile;

    /**
     * 登录邮箱（联系人邮箱）
     */
    @TableField("LoginEmail")
    private String loginEmail;

    /**
     * 注册密码，仅在商户注册时使用
     */
    @TableField("RegisterPassword")
    private String registerPassword;

    /**
     * 类型:1-平台账户,2-私有账户
     */
    @TableField("Type")
    private Byte type;

    /**
     * 私钥
     */
    @TableField("PrivateKey")
    private String privateKey;

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
     * 商户地址
     */
    @TableField("Address")
    private String address;

    /**
     * 代理商ID
     */
    @TableField("AgentId")
    private Long agentId;

    /**
     * 商户级别， 0-服务商直接商户
     */
    @TableField("MchLevel")
    private Integer mchLevel;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 商户状态: -2审核不通过, -1-待审核, 0-停用, 1-正常
     */
    @TableField("Status")
    private Byte status;

    /**
     * 签约状态: 0-待补充资料, 1-待签约, 2-已签约
     */
    @TableField("SignStatus")
    private Byte signStatus;

    /**
     * 小程序对应的商户子角色
     */
    @TableField("MiniRole")
    private Byte miniRole;

    /**
     * 小程序对应的商户对应的父节点Id
     */
    @TableField("ParentId")
    private Long parentId;

    /**
     * 医院ID
     */
    @TableField("HospitalId")
    private String hospitalId;

    /**
     * 是否支持押金模式:0-否,1-是
     */
    @TableField("DepositModeStatus")
    private Byte depositModeStatus;

    /**
     * 审核信息
     */
    @TableField("AuditInfo")
    private String auditInfo;

    /**
     * 商户备注
     */
    @TableField("Remark")
    private String remark;

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
    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
    }
    public String getMchShortName() {
        return mchShortName;
    }

    public void setMchShortName(String mchShortName) {
        this.mchShortName = mchShortName;
    }
    public String getContactRealName() {
        return contactRealName;
    }

    public void setContactRealName(String contactRealName) {
        this.contactRealName = contactRealName;
    }
    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }
    public String getLoginMobile() {
        return loginMobile;
    }

    public void setLoginMobile(String loginMobile) {
        this.loginMobile = loginMobile;
    }
    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
    }
    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }
    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
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
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public Integer getMchLevel() {
        return mchLevel;
    }

    public void setMchLevel(Integer mchLevel) {
        this.mchLevel = mchLevel;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Byte getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Byte signStatus) {
        this.signStatus = signStatus;
    }
    public Byte getDepositModeStatus() {
        return depositModeStatus;
    }

    public void setDepositModeStatus(Byte depositModeStatus) {
        this.depositModeStatus = depositModeStatus;
    }
    public String getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(String auditInfo) {
        this.auditInfo = auditInfo;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public Byte getMiniRole() {
        return miniRole;
    }

    public void setMiniRole(Byte miniRole) {
        this.miniRole = miniRole;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public String toString() {
        return "MchInfo{" +
                "MchId=" + mchId +
                ", mchName=" + mchName +
                ", mchShortName=" + mchShortName +
                ", contactRealName=" + contactRealName +
                ", loginUserName=" + loginUserName +
                ", loginMobile=" + loginMobile +
                ", loginEmail=" + loginEmail +
                ", registerPassword=" + registerPassword +
                ", type=" + type +
                ", privateKey=" + privateKey +
                ", provinceCode=" + provinceCode +
                ", cityCode=" + cityCode +
                ", areaCode=" + areaCode +
                ", areaInfo=" + areaInfo +
                ", address=" + address +
                ", agentId=" + agentId +
                ", mchLevel=" + mchLevel +
                ", isvId=" + isvId +
                ", status=" + status +
                ", signStatus=" + signStatus +
                ", miniRole=" + miniRole +
                ", parentId=" + parentId +
                ", hospitalId=" + hospitalId +
                ", depositModeStatus=" + depositModeStatus +
                ", auditInfo=" + auditInfo +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
