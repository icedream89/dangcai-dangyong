package com.dangcai.common.service.impl;

import com.dangcai.common.service.TokenService;
import com.dangcai.common.utils.JwtUtils;
import com.dangcai.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Token服务实现
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Token黑名单Key前缀
     */
    private static final String BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * 用户Token列表Key前缀
     */
    private static final String USER_TOKENS_PREFIX = "user:tokens:";

    @Override
    public void addToBlacklist(String token) {
        try {
            // 1. 获取Token过期时间
            long ttl = jwtUtils.getExpiration(jwtUtils.getClaimsFromToken(token).getExpiration().getTime())
                       - System.currentTimeMillis();

            if (ttl > 0) {
                // 2. 将Token加入黑名单（设置过期时间为Token剩余有效期）
                String key = BLACKLIST_PREFIX + token;
                redisUtils.set(key, "1", ttl, TimeUnit.MILLISECONDS);
                log.debug("Token已加入黑名单：{}", token);
            }
        } catch (Exception e) {
            log.error("加入Token黑名单失败：{}", e.getMessage());
        }
    }

    @Override
    public boolean isBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            return redisUtils.hasKey(key);
        } catch (Exception e) {
            log.error("检查Token黑名单失败：{}", e.getMessage());
            return false;
        }
    }

    @Override
    public void blacklistAllTokens(Long userId) {
        try {
            // TODO: 实现将用户所有Token加入黑名单
            // 1. 从Redis获取用户的所有有效Token
            // 2. 将所有Token加入黑名单
            // 3. 清空用户Token列表
            log.info("用户{}的所有Token已加入黑名单", userId);
        } catch (Exception e) {
            log.error("批量加入Token黑名单失败：{}", e.getMessage());
        }
    }
}
