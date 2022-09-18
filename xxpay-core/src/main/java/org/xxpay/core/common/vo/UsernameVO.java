package org.xxpay.core.common.vo;

import com.alibaba.fastjson.JSON;

/**
 * @Author terrfly
 * @Date 2018/12/13 10:32
 * @Description 用于Spring Security 框架 username参数拆分vo
 **/
public class UsernameVO {

    private String loginUsername;  //登录用户名

    private boolean isSubuser; //是否是子账户

    private String belongInfoUsername; //所属角色用户名

    private boolean isManageSkip; //是否运营平台跳转（运营平台跳转将不强制验证安全方式是否设置）

    private String loginType;//登录端

    private String version;//版本号

    public UsernameVO(){}

    public UsernameVO(String jsonStr){ //根据json字符串 实例化

        UsernameVO obj = JSON.parseObject(jsonStr, UsernameVO.class);
        this.setLoginUsername(obj.getLoginUsername());
        this.setSubuser(obj.isSubuser());
        this.setBelongInfoUsername(obj.getBelongInfoUsername());
        this.setManageSkip(obj.isManageSkip());
        this.setLoginType(obj.getLoginType());
        this.setVersion(obj.getVersion());
    }

    public UsernameVO(String loginUsername, boolean isSubuser, String belongInfoUsername, boolean isManageSkip, String loginType, String version) {
        this.loginUsername = loginUsername;
        this.isSubuser = isSubuser;
        this.belongInfoUsername = belongInfoUsername;
        this.isManageSkip = isManageSkip;
        this.loginType = loginType;
        this.version = version;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = loginUsername;
    }

    public String getBelongInfoUsername() {
        return belongInfoUsername;
    }

    public void setBelongInfoUsername(String belongInfoUsername) {
        this.belongInfoUsername = belongInfoUsername;
    }

    public boolean isSubuser() {
        return isSubuser;
    }

    public void setSubuser(boolean subuser) {
        isSubuser = subuser;
    }

    public boolean isManageSkip() {
        return isManageSkip;
    }

    public void setManageSkip(boolean manageSkip) {
        isManageSkip = manageSkip;
    }

    public String getLoginType() { return loginType; }

    public void setLoginType(String loginType) { this.loginType = loginType; }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }

}
