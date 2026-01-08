package com.dangcai.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 角色DTO
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Data
public class RoleDTO {

    /**
     * 角色ID（修改时需要）
     */
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空", groups = {Add.class, Update.class})
    @Size(min = 2, max = 20, message = "角色名称长度必须介于2到20之间", groups = {Add.class, Update.class})
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空", groups = {Add.class, Update.class})
    @Size(min = 2, max = 20, message = "角色编码长度必须介于2到20之间", groups = {Add.class, Update.class})
    private String roleCode;

    /**
     * 数据权限范围：1-全部数据 2-本企业数据 3-本部门数据 4-本人数据
     */
    @NotNull(message = "数据权限范围不能为空", groups = {Add.class, Update.class})
    private Integer dataScope;

    /**
     * 状态：0-禁用 1-正常
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 备注
     */
    private String remark;

    /**
     * 菜单ID列表
     */
    private Long[] menuIds;

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
