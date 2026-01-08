package com.dangcai.enterprise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EmployeeVO {

    /**
     * 员工ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 员工姓名
     */
    private String employeeName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 职位
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 是否企业管理员：0-否 1-是
     */
    private Integer isAdmin;

    /**
     * 是否管理员名称
     */
    private String isAdminName;

    /**
     * 状态：0-离职 1-在职
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    /**
     * 备注
     */
    private String remark;

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
