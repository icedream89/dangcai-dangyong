package com.dangcai.enterprise.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

/**
 * 员工DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class EmployeeDTO {

    /**
     * 员工ID（修改时必填）
     */
    private Long id;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空", groups = {Add.class, Update.class})
    private Long enterpriseId;

    /**
     * 关联用户ID
     */
    private Long userId;

    /**
     * 员工姓名
     */
    @NotBlank(message = "员工姓名不能为空", groups = {Add.class, Update.class})
    private String employeeName;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {Add.class, Update.class})
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {Add.class, Update.class})
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joinDate;

    /**
     * 备注
     */
    private String remark;

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
