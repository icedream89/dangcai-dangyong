package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 满意度评价DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class SatisfactionDTO {

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long ticketId;

    /**
     * 满意度评分：1-5分
     */
    @NotNull(message = "满意度评分不能为空")
    @Min(value = 1, message = "满意度评分最小为1分")
    @Max(value = 5, message = "满意度评分最大为5分")
    private Integer satisfaction;

    /**
     * 用户反馈
     */
    private String feedback;
}
