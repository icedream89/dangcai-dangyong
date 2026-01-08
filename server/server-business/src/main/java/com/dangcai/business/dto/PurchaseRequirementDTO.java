package com.dangcai.business.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 采购需求DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseRequirementDTO {

    /**
     * 需求ID（修改时必填）
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = {Add.class, Update.class})
    private String title;

    /**
     * 采购类别
     */
    private String category;

    /**
     * 需求内容
     */
    @NotBlank(message = "需求内容不能为空", groups = {Add.class, Update.class})
    private String content;

    /**
     * 预算金额
     */
    private BigDecimal budget;

    /**
     * 期望交付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectDate;

    /**
     * 交付地址
     */
    private String address;

    /**
     * 图片地址（多个用逗号分隔）
     */
    private String images;

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
