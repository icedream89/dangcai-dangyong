package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 采购需求回复DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseReplyDTO {

    /**
     * 回复ID（修改时必填）
     */
    private Long id;

    /**
     * 需求ID
     */
    @NotNull(message = "需求ID不能为空")
    private Long requirementId;

    /**
     * 供应商企业ID
     */
    private Long enterpriseId;

    /**
     * 供应商企业名称
     */
    private String enterpriseName;

    /**
     * 联系人
     */
    @NotBlank(message = "联系人不能为空")
    private String contactPerson;

    /**
     * 联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String contactPhone;

    /**
     * 回复内容
     */
    @NotBlank(message = "回复内容不能为空")
    private String replyContent;

    /**
     * 报价金额
     */
    private BigDecimal quotePrice;

    /**
     * 交付周期（天）
     */
    private Integer deliveryPeriod;

    /**
     * 附件地址（多个用逗号分隔）
     */
    private String attachments;
}
