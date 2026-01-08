package com.dangcai.business.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类VO（树形结构）
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class CategoryVO {

    /**
     * 分类ID
     */
    private Long id;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 分类名称
     */
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
     * 管理模式
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
     * 状态名称
     */
    private String statusName;

    /**
     * 子分类列表
     */
    private List<CategoryVO> children;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
