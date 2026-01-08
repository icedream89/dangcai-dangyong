package com.dangcai.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 企业实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ent_enterprise")
public class Enterprise extends BaseEntity {

    /**
     * 企业ID
     */
    @TableId(type = IdType.AUTO)
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
     * 企业类型：国有企业、民营企业、外资企业
     */
    private String enterpriseType;

    /**
     * 企业规模：小型、中型、大型
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
     * 审核备注
     */
    private String auditRemark;
}
