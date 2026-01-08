package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分类DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class CategoryDTO {

    /**
     * 分类ID（修改时必填）
     */
    private Long id;

    /**
     * 父分类ID（0为根分类）
     */
    @NotNull(message = "父分类ID不能为空", groups = {Add.class, Update.class})
    private Long parentId;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空", groups = {Add.class, Update.class})
    private String categoryName;

    /**
     * 分类编码
     */
    private String categoryCode;

    /**
     * 图标
     */
    private String icon;

    /**
     * 图片
     */
    private String image;

    /**
     * 管理模式：list-列表 grid-网格 tree-树形 image-图文
     */
    private String manageMode;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status;

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
