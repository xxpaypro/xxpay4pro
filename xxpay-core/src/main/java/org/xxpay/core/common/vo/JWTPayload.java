package org.xxpay.core.common.vo;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * @Author terrfly
 * @Date 2019/9/12 9:05
 * @Description JWT payload
   JWT payload 格式：
    {
        "userId": "309911111",
        "loginUserName": "xiaoming",
        "belongInfoId": "20000000",
        "loginType": "pc",
        "version": "1.0.0",
        "created": "1568250147846"
    }
 **/
public class JWTPayload {

    private Long userId;          //登录用户ID
    private String loginUserName;   //登录用户名
    private Long belongInfoId;    //所属ID
    private String loginType;       //登录类型: pc：收银插件,  uni-app：手机客户端,  web：浏览器网页访问
    private String version;         //版本信息
    private String loginDeviceNo;   //登录版本信息
    private Long created;         //创建时间, 格式：13位时间戳

    protected JWTPayload(){}

    public JWTPayload(JWTBaseUser jwtBaseUser){

        setUserId(jwtBaseUser.getUserId());
        setLoginUserName(jwtBaseUser.getLoginUserName());
        setBelongInfoId(jwtBaseUser.getBelongInfoId());
        setLoginType(jwtBaseUser.getLoginType());
        setVersion(jwtBaseUser.getVersion());
        setLoginDeviceNo(jwtBaseUser.getLoginDeviceNo());
        setCreated(new Date().getTime());
    }


    /** toMap **/
    public Map<String, Object> toMap(){
        JSONObject json = (JSONObject)JSONObject.toJSON(this);
        return json.toJavaObject(Map.class);
    }

    /**
     * 验证该token是否有效
     * @param userId 当前登录人ID
     * @param lastPasswordResetTime 最后一次密码修改时间
     * @return
     */
    public Boolean validateToken(Long userId, Date lastPasswordResetTime) {

        if(userId == null || !userId.equals(this.getUserId())){
            return false;
        }

        if(lastPasswordResetTime == null){
            return false;
        }

        return this.getCreated() > lastPasswordResetTime.getTime();
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public Long getBelongInfoId() {
        return belongInfoId;
    }

    public void setBelongInfoId(Long belongInfoId) {
        this.belongInfoId = belongInfoId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getLoginDeviceNo() {
        return loginDeviceNo;
    }

    public void setLoginDeviceNo(String loginDeviceNo) {
        this.loginDeviceNo = loginDeviceNo;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }
}
