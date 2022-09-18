package org.xxpay.mbr.secruity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtToken {

    /** 生成token **/
    public static String generateToken(Map<String, Object> map, String jwtSecret, Long jwtExpiration) {
        return Jwts.builder()
                .setClaims(map)
                //过期时间 = 当前时间 + （设置过期时间[单位 ：s ] ）
                .setExpiration(new Date(System.currentTimeMillis() + (jwtExpiration * 1000) ))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /** 根据token与秘钥 解析token并转换为 JWTPayload **/
    public static Map<String, Object> parseToken(String token, String secret){
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            return null; //解析失败
        }
    }

}
