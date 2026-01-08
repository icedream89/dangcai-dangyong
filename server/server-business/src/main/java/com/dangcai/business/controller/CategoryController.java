package com.dangcai.business.controller;

import com.dangcai.business.domain.Category;
import com.dangcai.business.dto.CategoryDTO;
import com.dangcai.business.service.CategoryService;
import com.dangcai.business.vo.CategoryVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "分类管理", description = "分类管理相关接口")
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 查询分类树
     */
    @Operation(summary = "查询分类树")
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('business:category:list')")
    public Result<List<CategoryVO>> tree(@RequestParam(required = false) Integer status) {
        List<CategoryVO> tree = categoryService.tree(status);
        return Result.success(tree);
    }

    /**
     * 根据ID查询分类详情
     */
    @Operation(summary = "根据ID查询分类详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:category:query')")
    public Result<CategoryVO> getById(@PathVariable Long id) {
        CategoryVO vo = categoryService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 查询子分类列表
     */
    @Operation(summary = "查询子分类列表")
    @GetMapping("/children/{parentId}")
    @PreAuthorize("hasAuthority('business:category:list')")
    public Result<List<Category>> listByParentId(@PathVariable Long parentId) {
        List<Category> children = categoryService.listByParentId(parentId);
        return Result.success(children);
    }

    /**
     * 新增分类
     */
    @Operation(summary = "新增分类")
    @PostMapping
    @PreAuthorize("hasAuthority('business:category:add')")
    public Result<Long> add(@Validated @RequestBody CategoryDTO dto) {
        Long id = categoryService.add(dto);
        return Result.success(id, "新增分类成功");
    }

    /**
     * 修改分类
     */
    @Operation(summary = "修改分类")
    @PutMapping
    @PreAuthorize("hasAuthority('business:category:edit')")
    public Result<Void> update(@Validated @RequestBody CategoryDTO dto) {
        categoryService.update(dto);
        return Result.success("修改分类成功");
    }

    /**
     * 删除分类
     */
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:category:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return Result.success("删除分类成功");
    }
}
