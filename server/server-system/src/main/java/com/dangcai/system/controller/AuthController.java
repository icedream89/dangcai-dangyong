package com.dangcai.system.controller;

import com.dangcai.common.domain.Result;
import com.dangcai.common.service.TokenService;
import com.dangcai.system.dto.LoginDTO;
import com.dangcai.system.dto.LoginUserVO;
import com.dangcai.system.dto.UserVO;
import com.dangcai.system.service.UserService;
import com.dangcai.system.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Tag(name = "认证管理", description = "用户登录、注册、登出等接口")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @return 登录用户信息
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginUserVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("用户登录：{}", loginDTO.getUsername());
        LoginUserVO loginUserVO = userService.login(loginDTO);
        return Result.success("登录成功", loginUserVO);
    }

    /**
     * 用户登出
     *
     * @param request HTTP请求
     * @return 提示信息
     */
    @Operation(summary = "用户登出")
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        // 1. 获取Token
        String token = getTokenFromRequest(request);

        if (token != null) {
            // 2. 将Token加入黑名单
            tokenService.addToBlacklist(token);
            log.info("用户登出，Token已加入黑名单");
        }

        return Result.success("登出成功");
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<UserVO> getInfo() {
        Long userId = SecurityUtils.getUserId();
        if (userId == null) {
            return Result.error(401, "用户未登录");
        }
        UserVO userVO = userService.getUserVOById(userId);
        return Result.success(userVO);
    }

    /**
     * 从请求头中获取Token
     *
     * @param request HTTP请求
     * @return Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

