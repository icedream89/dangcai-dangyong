package com.dangcai.enterprise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 企业VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EnterpriseVO {

    /**
     * 企业ID
     */
    private Long id;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String unifiedCode;

    /**
     * 法人代表
     */
    private String legalPerson;

    /**
     * 法人电话
     */
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
     * 是否推荐：0-否 1-是
     */
    private Integer isRecommended;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 状态：0-待审核 1-正常 2-禁用
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 审核备注
     */
    private String auditRemark;

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
