package com.dangcai.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.domain.Result;
import com.dangcai.system.dto.RoleDTO;
import com.dangcai.system.dto.RoleQueryDTO;
import com.dangcai.system.dto.RoleVO;
import com.dangcai.system.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Tag(name = "角色管理", description = "角色CRUD操作")
@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件
     * @return 角色分页列表
     */
    @Operation(summary = "分页查询角色列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<Page<RoleVO>> page(RoleQueryDTO queryDTO) {
        Page<RoleVO> page = roleService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    @Operation(summary = "查询所有角色列表")
    @GetMapping("/listAll")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<RoleVO>> listAll() {
        List<RoleVO> list = roleService.listAll();
        return Result.success(list);
    }

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    @Operation(summary = "根据ID查询角色详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public Result<RoleVO> getById(@PathVariable Long id) {
        RoleVO roleVO = roleService.getById(id);
        return Result.success(roleVO);
    }

    /**
     * 新增角色
     *
     * @param roleDTO 角色信息
     * @return 角色ID
     */
    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public Result<Long> add(@Valid @RequestBody RoleDTO roleDTO) {
        Long roleId = roleService.add(roleDTO);
        return Result.success("新增成功", roleId);
    }

    /**
     * 修改角色
     *
     * @param roleDTO 角色信息
     * @return 提示信息
     */
    @Operation(summary = "修改角色")
    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<Void> update(@Valid @RequestBody RoleDTO roleDTO) {
        roleService.update(roleDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 提示信息
     */
    @Operation(summary = "删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @Operation(summary = "根据用户ID查询角色列表")
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Result<List<RoleVO>> listByUserId(@PathVariable Long userId) {
        List<RoleVO> list = roleService.listByUserId(userId);
        return Result.success(list);
    }
}
