package com.dangcai.miniapp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dangcai.common.domain.Result;
import com.dangcai.common.utils.JwtUtils;
import com.dangcai.miniapp.config.WechatMiniappConfig;
import com.dangcai.miniapp.dto.WechatLoginDTO;
import com.dangcai.miniapp.service.WechatLoginService;
import com.dangcai.system.domain.User;
import com.dangcai.system.dto.LoginUserVO;
import com.dangcai.system.mapper.UserMapper;
import com.dangcai.system.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信登录服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatLoginServiceImpl implements WechatLoginService {

    private final WechatMiniappConfig wechatConfig;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * 微信API地址
     */
    private static final String WX_API_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object login(WechatLoginDTO loginDTO) {
        try {
            // 1. 通过code获取OpenID
            String openId = getWxOpenId(loginDTO.getCode());

            // 2. 查询是否已存在该OpenID的用户
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getUsername, "wx_" + openId);
            User user = userMapper.selectOne(wrapper);

            // 3. 如果用户不存在，则创建新用户
            if (user == null) {
                user = createWxUser(openId, loginDTO);
            } else {
                // 更新用户信息
                updateWxUser(user, loginDTO);
            }

            // 4. 生成JWT Token
            String token = jwtUtils.createToken(user.getId(), user.getUsername(), user.getUserType());

            // 5. 查询用户角色和权限
            Map<String, Object> roleData = userService.getUserRoleData(user.getId());

            // 6. 构建登录响应
            LoginUserVO loginUserVO = new LoginUserVO();
            loginUserVO.setId(user.getId());
            loginUserVO.setUsername(user.getUsername());
            loginUserVO.setRealName(user.getRealName());
            loginUserVO.setAvatar(user.getAvatar());
            loginUserVO.setUserType(user.getUserType());
            loginUserVO.setEnterpriseId(user.getEnterpriseId());
            loginUserVO.setToken(token);
            loginUserVO.setRoles((java.util.List<String>) roleData.get("roles"));
            loginUserVO.setPermissions((java.util.List<String>) roleData.get("permissions"));

            // 7. 更新最后登录信息
            user.setLastLoginTime(LocalDateTime.now());
            userMapper.updateById(user);

            log.info("微信登录成功，用户ID：{}，OpenID：{}", user.getId(), openId);

            return Result.success("登录成功", loginUserVO);

        } catch (Exception e) {
            log.error("微信登录失败", e);
            throw new RuntimeException("微信登录失败：" + e.getMessage());
        }
    }

    @Override
    public String getWxOpenId(String code) {
        try {
            // 构建请求URL
            String url = WX_API_URL +
                    "?appid=" + wechatConfig.getAppId() +
                    "&secret=" + wechatConfig.getAppSecret() +
                    "&js_code=" + code +
                    "&grant_type=authorization_code";

            // 发送请求
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String responseBody = response.getBody();

            // 解析响应
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // 检查是否有错误
            if (jsonNode.has("errcode")) {
                int errcode = jsonNode.get("errcode").asInt();
                String errmsg = jsonNode.get("errmsg").asText();
                throw new RuntimeException("微信API调用失败：" + errcode + " - " + errmsg);
            }

            // 获取OpenID
            String openId = jsonNode.get("openid").asText();
            log.info("获取微信OpenID成功：{}", openId);

            return openId;

        } catch (Exception e) {
            log.error("获取微信OpenID失败", e);
            throw new RuntimeException("获取微信OpenID失败：" + e.getMessage());
        }
    }

    @Override
    public String getPhoneNumber(String code) {
        try {
            // TODO: 实现获取手机号逻辑
            // 需要调用微信API获取用户手机号
            log.info("获取微信手机号，code：{}", code);
            return "";
        } catch (Exception e) {
            log.error("获取微信手机号失败", e);
            throw new RuntimeException("获取微信手机号失败：" + e.getMessage());
        }
    }

    /**
     * 创建微信用户
     */
    private User createWxUser(String openId, WechatLoginDTO loginDTO) {
        User user = new User();
        user.setUsername("wx_" + openId);
        user.setPassword(""); // 微信用户无需密码
        user.setRealName(StringUtils.hasText(loginDTO.getNickName()) ? loginDTO.getNickName() : "微信用户");
        user.setAvatar(StringUtils.hasText(loginDTO.getAvatarUrl()) ? loginDTO.getAvatarUrl() : "");
        user.setPhone("");
        user.setEmail("");
        user.setUserType(4); // 小程序用户
        user.setEnterpriseId(null);
        user.setStatus(1); // 正常
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDelFlag(0);

        userMapper.insert(user);

        log.info("创建微信用户成功，用户ID：{}，OpenID：{}", user.getId(), openId);
        return user;
    }

    /**
     * 更新微信用户信息
     */
    private void updateWxUser(User user, WechatLoginDTO loginDTO) {
        boolean needUpdate = false;

        if (StringUtils.hasText(loginDTO.getNickName()) && !loginDTO.getNickName().equals(user.getRealName())) {
            user.setRealName(loginDTO.getNickName());
            needUpdate = true;
        }

        if (StringUtils.hasText(loginDTO.getAvatarUrl()) && !loginDTO.getAvatarUrl().equals(user.getAvatar())) {
            user.setAvatar(loginDTO.getAvatarUrl());
            needUpdate = true;
        }

        if (needUpdate) {
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);
            log.info("更新微信用户信息成功，用户ID：{}", user.getId());
        }
    }
}
