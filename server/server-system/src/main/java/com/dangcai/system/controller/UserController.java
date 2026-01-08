package com.dangcai.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.domain.Result;
import com.dangcai.system.dto.UserDTO;
import com.dangcai.system.dto.UserQueryDTO;
import com.dangcai.system.dto.UserVO;
import com.dangcai.system.service.UserService;
import com.dangcai.system.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Tag(name = "用户管理", description = "用户CRUD操作")
@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 用户分页列表
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Result<Page<UserVO>> page(UserQueryDTO queryDTO) {
        Page<UserVO> page = userService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    @Operation(summary = "根据ID查询用户详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public Result<UserVO> getById(@PathVariable Long id) {
        UserVO userVO = userService.getUserVOById(id);
        return Result.success(userVO);
    }

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 用户ID
     */
    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public Result<Long> add(@Valid @RequestBody UserDTO userDTO) {
        Long userId = userService.add(userDTO);
        return Result.success("新增成功", userId);
    }

    /**
     * 修改用户
     *
     * @param userDTO 用户信息
     * @return 提示信息
     */
    @Operation(summary = "修改用户")
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> update(@Valid @RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 提示信息
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 重置密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     * @return 提示信息
     */
    @Operation(summary = "重置密码")
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public Result<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return Result.success("密码重置成功");
    }

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 提示信息
     */
    @Operation(summary = "修改密码")
    @PutMapping("/change-password")
    public Result<Void> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return Result.success("密码修改成功");
    }

    /**
     * 修改用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return 提示信息
     */
    @Operation(summary = "修改用户状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return Result.success("状态修改成功");
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    @Operation(summary = "获取当前登录用户信息")
    @GetMapping("/current")
    public Result<UserVO> getCurrentUser() {
        Long userId = SecurityUtils.getUserId();
        UserVO userVO = userService.getUserVOById(userId);
        return Result.success(userVO);
    }
}
