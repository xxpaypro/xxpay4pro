package org.xxpay.core.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_sys_log")
public class SysLog implements Serializable {
    /**
     * ID
     *
     * @mbggenerated
     */
    @TableId(type= IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     *
     * @mbggenerated
     */
    @TableField("UserId")
    private Long userId;

    /**
     * 用户名
     *
     * @mbggenerated
     */
    @TableField("UserName")
    private String userName;

    /**
     * 用户IP
     *
     * @mbggenerated
     */
    @TableField("UserIp")
    private String userIp;

    /**
     * 系统: 1-商户 2-代理商 3-平台
     *
     * @mbggenerated
     */
    @TableField("System")
    private Byte system;

    /**
     * 方法名
     *
     * @mbggenerated
     */
    @TableField("MethodName")
    private String methodName;

    /**
     * 方法描述
     *
     * @mbggenerated
     */
    @TableField("MethodRemark")
    private String methodRemark;

    /**
     * 操作请求参数
     *
     * @mbggenerated
     */
    @TableField("OptReqParam")
    private String optReqParam;

    /**
     * 操作响应结果
     *
     * @mbggenerated
     */
    @TableField("OptResInfo")
    private String optResInfo;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    @TableField("CreateTime")
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    @TableField("UpdateTime")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public Byte getSystem() {
        return system;
    }

    public void setSystem(Byte system) {
        this.system = system;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodRemark() {
        return methodRemark;
    }

    public void setMethodRemark(String methodRemark) {
        this.methodRemark = methodRemark;
    }

    public String getOptReqParam() {
        return optReqParam;
    }

    public void setOptReqParam(String optReqParam) {
        this.optReqParam = optReqParam;
    }

    public String getOptResInfo() {
        return optResInfo;
    }

    public void setOptResInfo(String optResInfo) {
        this.optResInfo = optResInfo;
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
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userIp=").append(userIp);
        sb.append(", system=").append(system);
        sb.append(", methodName=").append(methodName);
        sb.append(", methodRemark=").append(methodRemark);
        sb.append(", optReqParam=").append(optReqParam);
        sb.append(", optResInfo=").append(optResInfo);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SysLog other = (SysLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserIp() == null ? other.getUserIp() == null : this.getUserIp().equals(other.getUserIp()))
            && (this.getSystem() == null ? other.getSystem() == null : this.getSystem().equals(other.getSystem()))
            && (this.getMethodName() == null ? other.getMethodName() == null : this.getMethodName().equals(other.getMethodName()))
            && (this.getMethodRemark() == null ? other.getMethodRemark() == null : this.getMethodRemark().equals(other.getMethodRemark()))
            && (this.getOptReqParam() == null ? other.getOptReqParam() == null : this.getOptReqParam().equals(other.getOptReqParam()))
            && (this.getOptResInfo() == null ? other.getOptResInfo() == null : this.getOptResInfo().equals(other.getOptResInfo()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserIp() == null) ? 0 : getUserIp().hashCode());
        result = prime * result + ((getSystem() == null) ? 0 : getSystem().hashCode());
        result = prime * result + ((getMethodName() == null) ? 0 : getMethodName().hashCode());
        result = prime * result + ((getMethodRemark() == null) ? 0 : getMethodRemark().hashCode());
        result = prime * result + ((getOptReqParam() == null) ? 0 : getOptReqParam().hashCode());
        result = prime * result + ((getOptResInfo() == null) ? 0 : getOptResInfo().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}
