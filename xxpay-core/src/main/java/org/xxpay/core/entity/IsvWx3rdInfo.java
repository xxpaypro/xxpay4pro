package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商第三方配置表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-10-30
 */
@TableName("t_isv_wx3rd_info")
public class IsvWx3rdInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * isvId
     */
    @TableId("IsvId")
    private Long isvId;

    /**
     * 服务商配置项：授权发起页域名
     */
    @TableField("ConfigAuthHost")
    private String configAuthHost;

    /**
     * 服务商配置项：授权测试公众号列表
     */
    @TableField("ConfigTestMpAccount")
    private String configTestMpAccount;

    /**
     * 服务商配置项：授权事件接收URL
     */
    @TableField("ConfigAuthMsgUrl")
    private String configAuthMsgUrl;

    /**
     * 服务商配置项：消息校验Token
     */
    @TableField("ConfigMsgToken")
    private String configMsgToken;

    /**
     * 服务商配置项：消息加解密Key
     */
    @TableField("ConfigAesKey")
    private String configAesKey;

    /**
     * 服务商配置项：消息与事件接收URL
     */
    @TableField("ConfigNormalMsgUrl")
    private String configNormalMsgUrl;

    /**
     * 服务商配置项：小程序服务器域名
     */
    @TableField("ConfigMiniHost")
    private String configMiniHost;

    /**
     * 服务商配置项：小程序业务域名
     */
    @TableField("ConfigMiniBizHost")
    private String configMiniBizHost;

    /**
     * 服务商配置项：白名单IP地址列表
     */
    @TableField("ConfigWhiteIp")
    private String configWhiteIp;

    /**
     * 服务商配置项：服务商上传的微信校验文件名
     */
    @TableField("ConfigWxCheckFileName")
    private String configWxCheckFileName;

    /**
     * 服务商配置项：服务商上传的微信校验文件内容
     */
    @TableField("ConfigWxCheckFileValue")
    private String configWxCheckFileValue;

    /**
     * 开放平台appId
     */
    @TableField("ComponentAppId")
    private String componentAppId;

    /**
     * 开放平台appSecret
     */
    @TableField("ComponentAppSecret")
    private String componentAppSecret;

    /**
     * 开放平台最新有效的ticket
     */
    @TableField("ComponentVerifyTicket")
    private String componentVerifyTicket;

    /**
     * 状态:0-暂停使用, 1-待录入信息, 2-账号信息待验证, 3-验证通过, 4-验证错误
     */
    @TableField("Status")
    private Byte status;

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

    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getConfigAuthHost() {
        return configAuthHost;
    }

    public void setConfigAuthHost(String configAuthHost) {
        this.configAuthHost = configAuthHost;
    }
    public String getConfigTestMpAccount() {
        return configTestMpAccount;
    }

    public void setConfigTestMpAccount(String configTestMpAccount) {
        this.configTestMpAccount = configTestMpAccount;
    }
    public String getConfigAuthMsgUrl() {
        return configAuthMsgUrl;
    }

    public void setConfigAuthMsgUrl(String configAuthMsgUrl) {
        this.configAuthMsgUrl = configAuthMsgUrl;
    }
    public String getConfigMsgToken() {
        return configMsgToken;
    }

    public void setConfigMsgToken(String configMsgToken) {
        this.configMsgToken = configMsgToken;
    }
    public String getConfigAesKey() {
        return configAesKey;
    }

    public void setConfigAesKey(String configAesKey) {
        this.configAesKey = configAesKey;
    }
    public String getConfigNormalMsgUrl() {
        return configNormalMsgUrl;
    }

    public void setConfigNormalMsgUrl(String configNormalMsgUrl) {
        this.configNormalMsgUrl = configNormalMsgUrl;
    }
    public String getConfigMiniHost() {
        return configMiniHost;
    }

    public void setConfigMiniHost(String configMiniHost) {
        this.configMiniHost = configMiniHost;
    }
    public String getConfigMiniBizHost() {
        return configMiniBizHost;
    }

    public void setConfigMiniBizHost(String configMiniBizHost) {
        this.configMiniBizHost = configMiniBizHost;
    }
    public String getConfigWhiteIp() {
        return configWhiteIp;
    }

    public void setConfigWhiteIp(String configWhiteIp) {
        this.configWhiteIp = configWhiteIp;
    }
    public String getConfigWxCheckFileName() {
        return configWxCheckFileName;
    }

    public void setConfigWxCheckFileName(String configWxCheckFileName) {
        this.configWxCheckFileName = configWxCheckFileName;
    }
    public String getConfigWxCheckFileValue() {
        return configWxCheckFileValue;
    }

    public void setConfigWxCheckFileValue(String configWxCheckFileValue) {
        this.configWxCheckFileValue = configWxCheckFileValue;
    }
    public String getComponentAppId() {
        return componentAppId;
    }

    public void setComponentAppId(String componentAppId) {
        this.componentAppId = componentAppId;
    }
    public String getComponentAppSecret() {
        return componentAppSecret;
    }

    public void setComponentAppSecret(String componentAppSecret) {
        this.componentAppSecret = componentAppSecret;
    }
    public String getComponentVerifyTicket() {
        return componentVerifyTicket;
    }

    public void setComponentVerifyTicket(String componentVerifyTicket) {
        this.componentVerifyTicket = componentVerifyTicket;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
        return "IsvWx3rdInfo{" +
                "IsvId=" + isvId +
                ", configAuthHost=" + configAuthHost +
                ", configTestMpAccount=" + configTestMpAccount +
                ", configAuthMsgUrl=" + configAuthMsgUrl +
                ", configMsgToken=" + configMsgToken +
                ", configAesKey=" + configAesKey +
                ", configNormalMsgUrl=" + configNormalMsgUrl +
                ", configMiniHost=" + configMiniHost +
                ", configMiniBizHost=" + configMiniBizHost +
                ", configWhiteIp=" + configWhiteIp +
                ", configWxCheckFileName=" + configWxCheckFileName +
                ", configWxCheckFileValue=" + configWxCheckFileValue +
                ", componentAppId=" + componentAppId +
                ", componentAppSecret=" + componentAppSecret +
                ", componentVerifyTicket=" + componentVerifyTicket +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
