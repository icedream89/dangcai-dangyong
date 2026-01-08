package com.dangcai.enterprise.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 企业员工实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ent_employee")
public class Employee extends BaseEntity {

    /**
     * 员工ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 关联用户ID
     */
    private Long userId;

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
     * 状态：0-离职 1-在职
     */
    private Integer status;

    /**
     * 入职日期
     */
    private LocalDate joinDate;

    /**
     * 备注
     */
    private String remark;
}
