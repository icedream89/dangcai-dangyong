package com.dangcai.enterprise.dto;

import lombok.Data;

/**
 * 企业查询DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EnterpriseQueryDTO {

    /**
     * 企业名称（模糊查询）
     */
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    private String unifiedCode;

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
     * 状态：0-待审核 1-正常 2-禁用
     */
    private Integer status;

    /**
     * 是否推荐：0-否 1-是
     */
    private Integer isRecommended;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页条数
     */
    private Integer size = 10;
}
