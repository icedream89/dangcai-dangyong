package com.dangcai.business.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 企业课堂VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class CourseVO {

    /**
     * 课堂ID
     */
    private Long id;

    /**
     * 课堂标题
     */
    private String courseTitle;

    /**
     * 课堂类型
     */
    private String courseType;

    /**
     * 课堂类型名称
     */
    private String courseTypeName;

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
     * 时长显示（分钟）
     */
    private String durationDisplay;

    /**
     * 浏览次数
     */
    private Integer viewCount;

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
     * 状态名称
     */
    private String statusName;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
