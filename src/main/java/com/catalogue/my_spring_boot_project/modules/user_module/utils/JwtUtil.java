package com.catalogue.my_spring_boot_project.modules.user_module.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.catalogue.my_spring_boot_project.modules.common.utils.Log;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // 固定密钥（推荐放在 application.yml 或环境变量里）
    private static final String SECRET = "my-super-secret-key-1234567890-my-super-secret-key";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成 token
     * 
     * @param subject 一般是用户名或用户ID
     * Map<String, Object> claims, // 自定义数据
     */

    public String generateToken(String emailOrUid,  long exception) {
        try {
            return Jwts.builder()
                    .setSubject(emailOrUid)
                    // .setClaims(claims) // 自定义数据
                    .setIssuedAt(new Date()) // 签发时间
                    .setExpiration(new Date(System.currentTimeMillis() + exception)) // 过期时间
                    .signWith(key, SignatureAlgorithm.HS256) // 签名算法
                    .compact();
        } catch (Exception e) {
            Log.error(JwtUtil.class, "加密用户ID失败", e);
            return null;
        }

    }

    /**
     * 解析 token
     * 
     * @param token 前端传过来的 JWT
     * @return Claims，里面可以拿到 subject 和自定义数据
     */
    public String parseToken(String token) {
        try {
            Claims body = Jwts.parserBuilder()
                    .setSigningKey(key) // 验证签名
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return body.getSubject();
        } catch (Exception e) {
            Log.info(JwtUtil.class, "token解析失败");
            return null;
        }
    }

    /*
     * 验证签名
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(parseToken(token));
    }
}
