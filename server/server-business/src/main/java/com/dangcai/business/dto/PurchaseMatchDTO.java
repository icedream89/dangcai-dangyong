package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 需求匹配DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseMatchDTO {

    /**
     * 需求ID
     */
    @NotNull(message = "需求ID不能为空")
    private Long requirementId;

    /**
     * 供应商企业ID列表
     */
    @NotEmpty(message = "供应商企业ID不能为空")
    private List<Long> enterpriseIds;
}
