package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PolicyVO {

    /**
     * 政策ID
     */
    private Long id;

    /**
     * 政策标题
     */
    private String policyTitle;

    /**
     * 政策编号
     */
    private String policyNo;

    /**
     * 政策类型
     */
    private String policyType;

    /**
     * 发布日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishDate;

    /**
     * 发布部门
     */
    private String publishDept;

    /**
     * 申报开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyStartDate;

    /**
     * 申报结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate applyEndDate;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 政策摘要
     */
    private String summary;

    /**
     * 政策内容（富文本）
     */
    private String content;

    /**
     * 附件（JSON数组）
     */
    private String attachments;

    /**
     * 标签数组
     */
    private String[] tagsArray;

    /**
     * 标签（逗号分隔）
     */
    private String tags;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 是否热门：0-否 1-是
     */
    private Integer isHot;

    /**
     * 是否置顶：0-否 1-是
     */
    private Integer isTop;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
