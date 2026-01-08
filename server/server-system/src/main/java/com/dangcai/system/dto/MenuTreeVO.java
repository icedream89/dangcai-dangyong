package com.dangcai.system.dto;

import lombok.Data;

import java.util.List;

/**
 * 菜单树VO（用于角色分配权限）
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class MenuTreeVO {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String label;

    /**
     * 菜单类型：1-目录 2-菜单 3-按钮
     */
    private Integer menuType;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 子菜单列表
     */
    private List<MenuTreeVO> children;
}
