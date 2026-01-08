package com.dangcai.enterprise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 企业DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EnterpriseDTO {

    /**
     * 企业ID（修改时必填）
     */
    private Long id;

    /**
     * 企业名称
     */
    @NotBlank(message = "企业名称不能为空", groups = {Add.class, Update.class})
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    @NotBlank(message = "统一社会信用代码不能为空", groups = {Add.class, Update.class})
    @Pattern(regexp = "^[0-9A-Z]{18}$", message = "统一社会信用代码格式不正确", groups = {Add.class, Update.class})
    private String unifiedCode;

    /**
     * 法人代表
     */
    @NotBlank(message = "法人代表不能为空", groups = {Add.class, Update.class})
    private String legalPerson;

    /**
     * 法人电话
     */
    @NotBlank(message = "法人电话不能为空", groups = {Add.class, Update.class})
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "法人电话格式不正确", groups = {Add.class, Update.class})
    private String legalPersonPhone;

    /**
     * 注册资本（万元）
     */
    private BigDecimal registeredCapital;

    /**
     * 成立日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate establishDate;

    /**
     * 企业地址
     */
    private String address;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 企业类型
     */
    private String enterpriseType;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 企业简介
     */
    private String intro;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 审核备注
     */
    private String auditRemark;

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
