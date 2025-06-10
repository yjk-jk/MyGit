package com.itheima.springboot.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private  static String SECRET_KEY ="UHdZd2Z3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3eGZ3" ; // 需要保密的密钥
    public static final long EXPIRATION_TIME = 864_000_000; // 10天（单位：毫秒）

    // 生成 Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)  //要存的自己的信息
                .setIssuedAt(new Date()) //发布时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) //签名算法
                .compact();
    }

    // 解析 Token
    public static String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 验证 Token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }


    }
   //判断token有没有过期
    public boolean isTokenExpired(Date expirationTime) {
        return expirationTime.before(new Date());
    }
}
