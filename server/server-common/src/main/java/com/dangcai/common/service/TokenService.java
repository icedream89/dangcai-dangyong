package com.dangcai.common.service;

/**
 * Token服务接口
 *
 * @author dangcai
 * @since 2026-01-07
 */
public interface TokenService {

    /**
     * 将Token加入黑名单
     *
     * @param token Token
     */
    void addToBlacklist(String token);

    /**
     * 检查Token是否在黑名单中
     *
     * @param token Token
     * @return true-在黑名单中 false-不在黑名单中
     */
    boolean isBlacklisted(String token);

    /**
     * 根据用户ID将其所有Token加入黑名单
     *
     * @param userId 用户ID
     */
    void blacklistAllTokens(Long userId);
}
