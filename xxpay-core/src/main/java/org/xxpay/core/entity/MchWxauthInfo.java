package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * <p>
 * 商户第三方授权信息表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-05-12
 */
@TableName("t_mch_wxauth_info")
public class MchWxauthInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "Id", type = IdType.AUTO)
    private Long id;

    /**
     * mchId
     */
    @TableField("MchId")
    private Long mchId;

    /**
     * isvId
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 公众号/小程序授权appId
     */
    @TableField("AuthAppId")
    private String authAppId;

    /**
     * 公众号/小程序授权刷新accessToken
     */
    @TableField("AuthRefreshToken")
    private String authRefreshToken;

    /**
     * 公众号/小程序授权集合列表 格式： [1,2,3]
     */
    @TableField("AuthFuncInfo")
    private String authFuncInfo;

    /**
     * 公众号/小程序授权状态：0-已取消授权, 1-已授权, 3-授权不符合要求，4-已注册
     */
    @TableField("AuthStatus")
    private Byte authStatus;

    /**
     * 公众号/小程序授权来源，1-点餐小程序
     */
    @TableField("AuthFrom")
    private Byte authFrom;

    /**
     * 授权账号类型：1-公众号，2-小程序
     */
    @TableField("AuthType")
    private Byte authType;

    /**
     * 开放平台AppId
     */
    @TableField("OpenAppId")
    private String openAppId;

    /**
     * 法人姓名
     */
    @TableField("LegalPersonaName")
    private String legalPersonaName;

    /**
     * 法人微信
     */
    @TableField("LegalPersonaWechat")
    private String legalPersonaWechat;

    /**
     * 企业名称
     */
    @TableField("BussinessName")
    private String bussinessName;

    /**
     * 信用代码
     */
    @TableField("BussinessCode")
    private String bussinessCode;

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
     * 配置二维码规则，微信校验文件名
     */
    @TableField("WxCheckFileName")
    private String wxCheckFileName;

    /**
     * 配置二维码规则，微信校验文件内容
     */
    @TableField("WxCheckFileValue")
    private String wxCheckFileValue;

    /**
     * 二维码规则
     */
    @TableField("Prefix")
    private String prefix;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public String getAuthAppId() {
        return authAppId;
    }

    public void setAuthAppId(String authAppId) {
        this.authAppId = authAppId;
    }
    public String getAuthRefreshToken() {
        return authRefreshToken;
    }

    public void setAuthRefreshToken(String authRefreshToken) {
        this.authRefreshToken = authRefreshToken;
    }
    public String getAuthFuncInfo() {
        return authFuncInfo;
    }

    public void setAuthFuncInfo(String authFuncInfo) {
        this.authFuncInfo = authFuncInfo;
    }
    public Byte getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Byte authStatus) {
        this.authStatus = authStatus;
    }
    public Byte getAuthFrom() {
        return authFrom;
    }

    public void setAuthFrom(Byte authFrom) {
        this.authFrom = authFrom;
    }
    public Byte getAuthType() {
        return authType;
    }

    public void setAuthType(Byte authType) {
        this.authType = authType;
    }
    public String getOpenAppId() {
        return openAppId;
    }

    public void setOpenAppId(String openAppId) {
        this.openAppId = openAppId;
    }
    public String getLegalPersonaName() {
        return legalPersonaName;
    }

    public void setLegalPersonaName(String legalPersonaName) {
        this.legalPersonaName = legalPersonaName;
    }
    public String getLegalPersonaWechat() {
        return legalPersonaWechat;
    }

    public void setLegalPersonaWechat(String legalPersonaWechat) {
        this.legalPersonaWechat = legalPersonaWechat;
    }
    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }
    public String getBussinessCode() {
        return bussinessCode;
    }

    public void setBussinessCode(String bussinessCode) {
        this.bussinessCode = bussinessCode;
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
    public String getWxCheckFileName() {
        return wxCheckFileName;
    }

    public void setWxCheckFileName(String wxCheckFileName) {
        this.wxCheckFileName = wxCheckFileName;
    }
    public String getWxCheckFileValue() {
        return wxCheckFileValue;
    }

    public void setWxCheckFileValue(String wxCheckFileValue) {
        this.wxCheckFileValue = wxCheckFileValue;
    }
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        return "MchWxauthInfo{" +
            "Id=" + id +
            ", mchId=" + mchId +
            ", isvId=" + isvId +
            ", authAppId=" + authAppId +
            ", authRefreshToken=" + authRefreshToken +
            ", authFuncInfo=" + authFuncInfo +
            ", authStatus=" + authStatus +
            ", authFrom=" + authFrom +
            ", authType=" + authType +
            ", openAppId=" + openAppId +
            ", legalPersonaName=" + legalPersonaName +
            ", legalPersonaWechat=" + legalPersonaWechat +
            ", bussinessName=" + bussinessName +
            ", bussinessCode=" + bussinessCode +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", wxCheckFileName=" + wxCheckFileName +
            ", wxCheckFileValue=" + wxCheckFileValue +
            ", prefix=" + prefix +
        "}";
    }
}
