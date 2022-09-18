package org.xxpay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 服务商信息表
 * </p>
 *
 * @author xxpay generator
 * @since 2020-09-28
 */
@TableName("t_isv_info")
public class IsvInfo extends BaseModel<IsvInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 服务商ID
     */
    @TableId(value = "IsvId", type = IdType.AUTO)
    private Long isvId;

    /**
     * 服务商名称
     */
    @TableField("IsvName")
    private String isvName;

    /**
     * 登录用户名
     */
    @TableField("LoginUserName")
    private String loginUserName;

    /**
     * 登录手机号（联系人手机号）
     */
    @TableField("Mobile")
    private String mobile;

    /**
     * 登录邮箱（联系人邮箱）
     */
    @TableField("Email")
    private String email;

    /**
     * 注册密码，仅在服务商注册时使用
     */
    @TableField("RegisterPassword")
    private String registerPassword;

    /**
     * 用户真实姓名
     */
    @TableField("RealName")
    private String realName;

    /**
     * 联系人身份证号
     */
    @TableField("IdCard")
    private String idCard;

    /**
     * 联系人QQ号码
     */
    @TableField("Qq")
    private String qq;

    /**
     * 服务商通讯地址
     */
    @TableField("Address")
    private String address;

    /**
     * 域名
     */
    @TableField("Domain")
    private String domain;

    /**
     * 系统名称
     */
    @TableField("SystemTitle")
    private String systemTitle;

    /**
     * 技术支持姓名
     */
    @TableField("SupportName")
    private String supportName;

    /**
     * 技术支持电话
     */
    @TableField("SupportTel")
    private String supportTel;

    /**
     * 状态:0-停止使用,1-使用中, -1 等待审核, -2 审核不通过
     */
    @TableField("Status")
    private Byte status;

    /**
     * 备注
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

    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public String getIsvName() {
        return isvName;
    }

    public void setIsvName(String isvName) {
        this.isvName = isvName;
    }
    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getRegisterPassword() {
        return registerPassword;
    }

    public void setRegisterPassword(String registerPassword) {
        this.registerPassword = registerPassword;
    }
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getSystemTitle() {
        return systemTitle;
    }

    public void setSystemTitle(String systemTitle) {
        this.systemTitle = systemTitle;
    }
    public String getSupportName() {
        return supportName;
    }

    public void setSupportName(String supportName) {
        this.supportName = supportName;
    }
    public String getSupportTel() {
        return supportTel;
    }

    public void setSupportTel(String supportTel) {
        this.supportTel = supportTel;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "IsvInfo{" +
                "IsvId=" + isvId +
                ", isvName=" + isvName +
                ", loginUserName=" + loginUserName +
                ", mobile=" + mobile +
                ", email=" + email +
                ", registerPassword=" + registerPassword +
                ", realName=" + realName +
                ", idCard=" + idCard +
                ", qq=" + qq +
                ", address=" + address +
                ", domain=" + domain +
                ", systemTitle=" + systemTitle +
                ", supportName=" + supportName +
                ", supportTel=" + supportTel +
                ", status=" + status +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
