package com.dangcai.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 菜单DTO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class MenuDTO {

    /**
     * 菜单ID（修改时需要）
     */
    private Long id;

    /**
     * 父菜单ID（0为根菜单）
     */
    private Long parentId = 0L;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空", groups = {Add.class, Update.class})
    @Size(min = 2, max = 20, message = "菜单名称长度必须介于2到20之间", groups = {Add.class, Update.class})
    private String menuName;

    /**
     * 菜单类型：1-目录 2-菜单 3-按钮
     */
    @NotNull(message = "菜单类型不能为空", groups = {Add.class, Update.class})
    private Integer menuType;

    /**
     * 路由路径
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sortOrder = 0;

    /**
     * 是否可见：0-隐藏 1-显示
     */
    private Integer visible = 1;

    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status = 1;

    /**
     * 新增分组
     */
    public interface Add {
    }

    /**
     * 修改分组
     */
    public interface Update {
    }
}
