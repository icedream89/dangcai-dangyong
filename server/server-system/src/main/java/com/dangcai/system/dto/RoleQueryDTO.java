package com.dangcai.system.dto;

import lombok.Data;

/**
 * 角色查询DTO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class RoleQueryDTO {

    /**
     * 角色名称（模糊查询）
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

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
