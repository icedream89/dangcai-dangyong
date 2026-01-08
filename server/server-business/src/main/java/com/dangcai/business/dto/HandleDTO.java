package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 工单处理DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class HandleDTO {

    /**
     * 工单ID
     */
    @NotNull(message = "工单ID不能为空")
    private Long ticketId;

    /**
     * 处理结果
     */
    @NotBlank(message = "处理结果不能为空")
    private String handleResult;
}
