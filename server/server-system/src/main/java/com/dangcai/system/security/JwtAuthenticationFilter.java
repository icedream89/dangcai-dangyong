package com.dangcai.system.security;

import com.dangcai.common.service.TokenService;
import com.dangcai.common.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 从请求头中获取Token
        String token = getTokenFromRequest(request);

        // 2. 验证Token
        if (StringUtils.hasText(token) && jwtUtils.validateToken(token)) {
            // 3. 检查Token是否在黑名单中
            if (tokenService.isBlacklisted(token)) {
                log.debug("Token已在黑名单中：{}", token);
                filterChain.doFilter(request, response);
                return;
            }

            try {
                // 4. 从Token中获取用户信息
                String username = jwtUtils.getSubjectFromToken(token);
                Object userIdObj = jwtUtils.getClaimFromToken(token, "userId");

                if (username != null && userIdObj != null) {
                    Long userId = Long.valueOf(userIdObj.toString());

                    // 5. 创建认证信息
                    LoginUser loginUser = new LoginUser(userId, username);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 6. 设置到Security上下文中
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("用户{}认证成功", username);
                }
            } catch (Exception e) {
                log.error("Token验证失败：{}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取Token
     *
     * @param request 请求
     * @return Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
