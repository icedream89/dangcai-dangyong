package com.dangcai.system.dto;

import lombok.Data;

/**
 * 角色VO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class RoleVO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 数据权限范围
     */
    private Integer dataScope;

    /**
     * 数据权限范围名称
     */
    private String dataScopeName;
}
