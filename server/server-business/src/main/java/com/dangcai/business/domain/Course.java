package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 企业课堂实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_course")
public class Course extends BaseEntity {

    /**
     * 课堂ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 课堂标题
     */
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
     * 发布时间
     */
    private LocalDateTime publishTime;
}
