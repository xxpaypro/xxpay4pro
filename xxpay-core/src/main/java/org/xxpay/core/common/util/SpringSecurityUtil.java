package org.xxpay.core.common.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.xxpay.core.common.vo.JWTBaseUser;

/**
 * @Author terrfly
 * @Date 2019/9/11 11:20
 * @Description Spring Security 相关工具
 * xxpay-core 本身不依赖与 spring-security 插件， 使用工程需依赖spring-security
 **/
public class SpringSecurityUtil {


    /** 生成spring-security 加密密码 **/
    public static String generateSSPassword(String rawPassword){
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    /** 匹配密码是否一致 **/
    public static boolean passwordMatch(String rawPassword, String hashPassword){
        return new BCryptPasswordEncoder().matches(rawPassword, hashPassword);
    }

    /** 获取 spring security中的对象信息  **/
    public static JWTBaseUser getCurrentJWTUser(){

        /** 获取当前线程对象 **/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTBaseUser result =  (JWTBaseUser) authentication.getPrincipal();
        return result;
    }

}
