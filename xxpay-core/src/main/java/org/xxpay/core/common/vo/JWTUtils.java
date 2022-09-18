package org.xxpay.core.common.vo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/** JWT工具包 **/
public class JWTUtils {

    /** 生成token **/
    public static String generateToken(JWTPayload jwtPayload, String jwtSecret, Long jwtExpiration) {
        return Jwts.builder()
                .setClaims(jwtPayload.toMap())
                //过期时间 = 当前时间 + （设置过期时间[单位 ：s ] ）
                .setExpiration(new Date(System.currentTimeMillis() + (jwtExpiration * 1000) ))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /** 根据token与秘钥 解析token并转换为 JWTPayload **/
    public static JWTPayload parseToken(String token, String secret){
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            JWTPayload result = new JWTPayload();
            result.setUserId(claims.get("userId", Long.class));
            result.setLoginUserName((String)claims.get("loginUserName"));
            result.setBelongInfoId(claims.get("belongInfoId",  Long.class));
            result.setLoginType((String)claims.get("loginType"));
            result.setVersion((String)claims.get("version"));
            result.setLoginDeviceNo((String)claims.get("loginDeviceNo"));
            result.setCreated(claims.get("created", Long.class));
            return result;
        } catch (Exception e) {
            return null; //解析失败
        }
    }


}
