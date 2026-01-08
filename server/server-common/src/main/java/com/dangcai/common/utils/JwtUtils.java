package com.dangcai.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Component
public class JwtUtils {

    /**
     * JWT密钥
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * JWT过期时间（毫秒）
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 生成JWT Token
     *
     * @param subject    主题（通常是用户ID或用户名）
     * @param claims     自定义声明
     * @return Token
     */
    public String generateToken(String subject, Map<String, Object> claims) {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 生成JWT Token（无自定义声明）
     *
     * @param subject 主题
     * @return Token
     */
    public String generateToken(String subject) {
        return generateToken(subject, new HashMap<>());
    }

    /**
     * 从Token中获取Claims
     *
     * @param token Token
     * @return Claims
     */
    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从Token中获取主题
     *
     * @param token Token
     * @return 主题
     */
    public String getSubjectFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    /**
     * 从Token中获取自定义声明
     *
     * @param token Token
     * @param key   键
     * @return 值
     */
    public Object getClaimFromToken(String token, String key) {
        return getClaimsFromToken(token).get(key);
    }

    /**
     * 验证Token是否过期
     *
     * @param token Token
     * @return true-已过期 false-未过期
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 验证Token
     *
     * @param token Token
     * @return true-有效 false-无效
     */
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新Token
     *
     * @param token 旧Token
     * @return 新Token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.setExpiration(new Date());
        return generateToken(claims.getSubject(), claims);
    }

    /**
     * 获取密钥
     *
     * @return 密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
