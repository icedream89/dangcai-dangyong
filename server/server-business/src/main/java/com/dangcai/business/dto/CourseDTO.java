package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 企业课堂DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class CourseDTO {

    /**
     * 课堂ID（修改时必填）
     */
    private Long id;

    /**
     * 课堂标题
     */
    @NotBlank(message = "课堂标题不能为空", groups = {Add.class, Update.class})
    private String courseTitle;

    /**
     * 课堂类型：视频、文档、图文
     */
    private String courseType;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 分类
     */
    private String category;

    /**
     * 简介
     */
    private String summary;

    /**
     * 内容（富文本或视频URL）
     */
    private String content;

    /**
     * 附件（JSON数组）
     */
    private String attachments;

    /**
     * 时长（秒）
     */
    private Integer duration;

    /**
     * 是否推荐：0-否 1-是
     */
    private Integer isRecommended;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态：0-下架 1-发布
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
