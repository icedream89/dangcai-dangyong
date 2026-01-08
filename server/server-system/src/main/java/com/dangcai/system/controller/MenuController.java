package com.dangcai.system.controller;

import com.dangcai.common.domain.Result;
import com.dangcai.system.dto.MenuDTO;
import com.dangcai.system.dto.MenuTreeVO;
import com.dangcai.system.dto.MenuVO;
import com.dangcai.system.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Tag(name = "菜单管理", description = "菜单CRUD操作")
@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单树
     *
     * @return 菜单树列表
     */
    @Operation(summary = "查询菜单树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Result<List<MenuVO>> tree() {
        List<MenuVO> tree = menuService.tree();
        return Result.success(tree);
    }

    /**
     * 查询菜单树（用于角色权限分配）
     *
     * @return 菜单树列表
     */
    @Operation(summary = "查询菜单树（权限分配）")
    @GetMapping("/tree/permission")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Result<List<MenuTreeVO>> treeForPermission() {
        List<MenuTreeVO> tree = menuService.treeForPermission();
        return Result.success(tree);
    }

    /**
     * 根据ID查询菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    @Operation(summary = "根据ID查询菜单详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public Result<MenuVO> getById(@PathVariable Long id) {
        MenuVO menuVO = menuService.getById(id);
        return Result.success(menuVO);
    }

    /**
     * 新增菜单
     *
     * @param menuDTO 菜单信息
     * @return 菜单ID
     */
    @Operation(summary = "新增菜单")
    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Result<Long> add(@Valid @RequestBody MenuDTO menuDTO) {
        Long menuId = menuService.add(menuDTO);
        return Result.success("新增成功", menuId);
    }

    /**
     * 修改菜单
     *
     * @param menuDTO 菜单信息
     * @return 提示信息
     */
    @Operation(summary = "修改菜单")
    @PutMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Result<Void> update(@Valid @RequestBody MenuDTO menuDTO) {
        menuService.update(menuDTO);
        return Result.success("修改成功");
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return 提示信息
     */
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success("删除成功");
    }
}
