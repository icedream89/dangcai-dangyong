package com.dangcai.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 登录用户信息VO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class LoginUserVO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 关联企业ID
     */
    private Long enterpriseId;

    /**
     * Token
     */
    private String token;

    /**
     * 角色列表
     */
    private List<String> roles;

    /**
     * 权限列表
     */
    private List<String> permissions;
}
