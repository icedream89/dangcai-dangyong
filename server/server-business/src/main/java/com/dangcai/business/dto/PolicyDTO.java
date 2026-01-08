package com.dangcai.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 政策DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PolicyDTO {

    /**
     * 政策ID（修改时必填）
     */
    private Long id;

    /**
     * 政策标题
     */
    @NotBlank(message = "政策标题不能为空", groups = {Add.class, Update.class})
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
     * 标签（逗号分隔）
     */
    private String tags;

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
