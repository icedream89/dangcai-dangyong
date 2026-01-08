package com.dangcai.miniapp.controller;

import com.dangcai.common.domain.Result;
import com.dangcai.miniapp.dto.WechatLoginDTO;
import com.dangcai.miniapp.service.WechatLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 小程序认证Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Tag(name = "小程序认证", description = "微信小程序登录、注册等接口")
@RestController
@RequestMapping("/miniapp-api/auth")
@RequiredArgsConstructor
public class MiniappAuthController {

    private final WechatLoginService wechatLoginService;

    /**
     * 微信登录
     */
    @Operation(summary = "微信登录")
    @PostMapping("/login")
    public Result<Object> login(@Valid @RequestBody WechatLoginDTO loginDTO) {
        log.info("微信登录请求，昵称：{}", loginDTO.getNickName());
        return (Result<Object>) wechatLoginService.login(loginDTO);
    }

    /**
     * 获取用户手机号
     */
    @Operation(summary = "获取用户手机号")
    @PostMapping("/phone")
    public Result<String> getPhoneNumber(@RequestParam String code) {
        String phoneNumber = wechatLoginService.getPhoneNumber(code);
        return Result.success("获取成功", phoneNumber);
    }
}
