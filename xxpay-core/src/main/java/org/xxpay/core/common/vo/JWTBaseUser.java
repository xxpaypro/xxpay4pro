package org.xxpay.core.common.vo;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.xxpay.core.common.util.MyLog;
import org.xxpay.core.entity.SysUser;

import java.util.Collection;


public class JWTBaseUser extends SysUser implements UserDetails {

    private static final MyLog logger = MyLog.getLog(JWTBaseUser.class);

    private final Collection<? extends GrantedAuthority> authorities ;

    private String loginType ; //登录类型
    private String version ; //版本类型
    private String loginDeviceNo ; //登录硬件设备编号

    public JWTBaseUser(SysUser sysUser, Collection<? extends GrantedAuthority> authorities) {

        this.authorities = authorities;

        try {
            // BeanUtils.copyProperties(this, sysUser);  //beanUtils.copyProperties 会出现Long 为0的问题
            this.setUserId(sysUser.getUserId());
            this.setNickName(sysUser.getNickName());
            this.setLoginUserName(sysUser.getLoginUserName());
            this.setEmail(sysUser.getEmail());
            this.setMobile(sysUser.getMobile());
            this.setLoginPassword(sysUser.getLoginPassword());
            this.setStatus(sysUser.getStatus());
            this.setIsSuperAdmin(sysUser.getIsSuperAdmin());
            this.setBelongInfoId(sysUser.getBelongInfoId());
            this.setBelongInfoType(sysUser.getBelongInfoType());
            this.setAvatar(sysUser.getAvatar());
            this.setSex(sysUser.getSex());
            this.setStoreId(sysUser.getStoreId());
            this.setCreateUserId(sysUser.getCreateUserId());
            this.setLastLoginTime(sysUser.getLastLoginTime());
            this.setLastLoginIp(sysUser.getLastLoginIp());
            this.setWorkStatus(sysUser.getWorkStatus());
            this.setWorkStartTime(sysUser.getWorkStartTime());
            this.setLastPasswordResetTime(sysUser.getLastPasswordResetTime());
            this.setCreateTime(sysUser.getCreateTime());
            this.setUpdateTime(sysUser.getUpdateTime());

        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }


    /** spring-security 需要验证的密码 **/
    @Override
    public String getPassword() {
        return this.getLoginPassword();
    }

    /** spring-security 登录名 **/
    @Override
    public String getUsername() {
        return getLoginUserName();
    }

    /** 账户是否过期 **/
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** 账户是否锁定 **/
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /** 密码是否过期 **/
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 账户是否开启 **/
    @Override
    public boolean isEnabled() {
        return true;
    }

    /** 获取权限集合 **/
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }


    //getter and setter
    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLoginDeviceNo() {
        return loginDeviceNo;
    }

    public void setLoginDeviceNo(String loginDeviceNo) {
        this.loginDeviceNo = loginDeviceNo;
    }

    public void setLoginTypeAndVersion(String loginType, String version, String loginDeviceNo){
        this.loginType = loginType;
        this.version = version;
        this.loginDeviceNo = loginDeviceNo;
    }

}
