package com.dangcai.system.dto;

import lombok.Data;

/**
 * 用户查询DTO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class UserQueryDTO {

    /**
     * 用户名（模糊查询）
     */
    private String username;

    /**
     * 真实姓名（模糊查询）
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 页码
     */
    private Integer page = 1;

    /**
     * 每页大小
     */
    private Integer size = 10;
}
