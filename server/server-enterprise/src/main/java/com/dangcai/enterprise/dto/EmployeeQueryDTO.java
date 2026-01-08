package com.dangcai.enterprise.dto;

import lombok.Data;

/**
 * 员工查询DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EmployeeQueryDTO {

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 员工姓名（模糊查询）
     */
    private String employeeName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 部门
     */
    private String department;

    /**
     * 是否企业管理员：0-否 1-是
     */
    private Integer isAdmin;

    /**
     * 状态：0-离职 1-在职
     */
    private Integer status;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页条数
     */
    private Integer size = 10;
}
