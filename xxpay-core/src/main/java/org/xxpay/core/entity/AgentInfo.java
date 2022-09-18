package org.xxpay.core.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import org.xxpay.core.entity.BaseModel;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * <p>
 * 代理商信息表
 * </p>
 *
 * @author xxpay generator
 * @since 2019-09-19
 */
@TableName("t_agent_info")
public class AgentInfo extends BaseModel<AgentInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 代理商ID
     */
    @TableId(value = "AgentId", type = IdType.AUTO)
    private Long agentId;

    /**
     * 代理商名称
     */
    @TableField("AgentName")
    private String agentName;

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
     * 联系人真实姓名
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
     * 代理商通讯地址
     */
    @TableField("Address")
    private String address;

    /**
     * 返佣百分比值,支持两位小数, 最高100%
     */
    @TableField("ProfitRate")
    private BigDecimal profitRate;

    /**
     * 状态:0-暂停使用, 1-正常
     */
    @TableField("Status")
    private Byte status;

    /**
     * 服务商ID
     */
    @TableField("IsvId")
    private Long isvId;

    /**
     * 上级代理商ID
     */
    @TableField("Pid")
    private Long pid;

    /**
     * 代理商等级：运营平台新增为一级代理 以此类推
     */
    @TableField("AgentLevel")
    private Integer agentLevel;

    /**
     * 是否可以发展子代理商 0-不允许, 1-允许
     */
    @TableField("AllowAddSubAgent")
    private Byte allowAddSubAgent;

    /**
     * 是否可以发展商户 0-不允许, 1-允许
     */
    @TableField("AllowAddMch")
    private Byte allowAddMch;

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

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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
    public BigDecimal getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate = profitRate;
    }
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
    public Long getIsvId() {
        return isvId;
    }

    public void setIsvId(Long isvId) {
        this.isvId = isvId;
    }
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }
    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }
    public Byte getAllowAddSubAgent() {
        return allowAddSubAgent;
    }

    public void setAllowAddSubAgent(Byte allowAddSubAgent) {
        this.allowAddSubAgent = allowAddSubAgent;
    }
    public Byte getAllowAddMch() {
        return allowAddMch;
    }

    public void setAllowAddMch(Byte allowAddMch) {
        this.allowAddMch = allowAddMch;
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
        return "AgentInfo{" +
                "AgentId=" + agentId +
                ", agentName=" + agentName +
                ", loginUserName=" + loginUserName +
                ", mobile=" + mobile +
                ", email=" + email +
                ", realName=" + realName +
                ", idCard=" + idCard +
                ", qq=" + qq +
                ", address=" + address +
                ", profitRate=" + profitRate +
                ", status=" + status +
                ", isvId=" + isvId +
                ", pid=" + pid +
                ", agentLevel=" + agentLevel +
                ", allowAddSubAgent=" + allowAddSubAgent +
                ", allowAddMch=" + allowAddMch +
                ", remark=" + remark +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "}";
    }
}
