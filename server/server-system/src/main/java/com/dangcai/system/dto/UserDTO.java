package com.dangcai.system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户DTO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class UserDTO {

    /**
     * 用户ID（修改时需要）
     */
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {Add.class, Update.class})
    @Size(min = 2, max = 20, message = "用户名长度必须介于2到20之间", groups = {Add.class, Update.class})
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {Add.class})
    @Size(min = 6, max = 20, message = "密码长度必须介于6到20之间", groups = {Add.class})
    private String password;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空", groups = {Add.class, Update.class})
    private String realName;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {Add.class, Update.class})
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确", groups = {Add.class, Update.class})
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户类型：1-管理员 2-企业管理员 3-企业员工 4-小程序用户
     */
    private Integer userType;

    /**
     * 关联企业ID
     */
    private Long enterpriseId;

    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色ID列表
     */
    private Long[] roleIds;

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
