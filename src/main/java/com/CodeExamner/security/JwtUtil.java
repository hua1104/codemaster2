package com.CodeExamner.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // 导入 SecurityException 的基类
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication; // 导入 Spring Security 的 Authentication
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    // 🔑 修正点 1：配置属性名与 application.properties (app.jwt...) 保持一致
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // ⏱️ 修正点 1：配置属性名与 application.properties (app.jwt...) 保持一致
    @Value("${app.jwtExpirationMs}")
    private Long jwtExpirationMs; // 确保使用 Long 类型

    // 内部存储密钥对象，避免重复创建
    private SecretKey key;

    /**
     * 获取用于签名的 SecretKey
     */
    private SecretKey getSigningKey() {
        if (this.key == null) {
            // 使用 HS512 算法，并基于 jwtSecret 生成密钥
            this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
        return this.key;
    }

    /**
     * 修正点 2：使用 Spring Security 的 Authentication 对象生成 Token
     */
    public String generateJwtToken(Authentication authentication) {
        // 从 Authentication 中获取实际的用户对象 (UserDetails 是父接口)
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        // 使用用户名 (通常是学号/邮箱) 作为 Token 的 Subject
        String subject = userPrincipal.getUsername(); 

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                // 修正了过期时间的计算方式：使用配置的毫秒数
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) 
                // 修正点 3：使用更安全的 HS512 算法
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) 
                .compact();
    }

    /**
     * 从 JWT Token 中获取 Subject (用户名/用户ID)
     */
    public String getSubjectFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 修正点 4：增强异常处理和日志记录
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}