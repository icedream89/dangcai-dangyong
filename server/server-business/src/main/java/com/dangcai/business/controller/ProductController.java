package com.dangcai.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.ProductDTO;
import com.dangcai.business.service.ProductService;
import com.dangcai.business.vo.ProductVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 产品管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "产品管理", description = "产品管理相关接口")
@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 分页查询产品列表
     */
    @Operation(summary = "分页查询产品列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('business:product:list')")
    public Result<Page<ProductVO>> page(
            @RequestParam(required = false) Long enterpriseId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minRecommendWeight,
            @RequestParam(required = false) Integer maxRecommendWeight,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<ProductVO> page = productService.page(
                enterpriseId, productName, category,
                minRecommendWeight, maxRecommendWeight, status, current, size
        );
        return Result.success(page);
    }

    /**
     * 根据ID查询产品详情
     */
    @Operation(summary = "根据ID查询产品详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:product:query')")
    public Result<ProductVO> getById(@PathVariable Long id) {
        ProductVO vo = productService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 新增产品
     */
    @Operation(summary = "新增产品")
    @PostMapping
    @PreAuthorize("hasAuthority('business:product:add')")
    public Result<Long> add(@Validated @RequestBody ProductDTO dto) {
        Long id = productService.add(dto);
        return Result.success(id, "新增产品成功");
    }

    /**
     * 修改产品
     */
    @Operation(summary = "修改产品")
    @PutMapping
    @PreAuthorize("hasAuthority('business:product:edit')")
    public Result<Void> update(@Validated @RequestBody ProductDTO dto) {
        productService.update(dto);
        return Result.success("修改产品成功");
    }

    /**
     * 删除产品
     */
    @Operation(summary = "删除产品")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:product:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success("删除产品成功");
    }

    /**
     * 设置推荐权重
     */
    @Operation(summary = "设置推荐权重")
    @PutMapping("/recommendWeight/{id}")
    @PreAuthorize("hasAuthority('business:product:edit')")
    public Result<Void> setRecommendWeight(
            @PathVariable Long id,
            @RequestParam Integer recommendWeight) {
        productService.setRecommendWeight(id, recommendWeight);
        return Result.success("设置推荐权重成功");
    }

    /**
     * 上架/下架
     */
    @Operation(summary = "上架/下架")
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAuthority('business:product:edit')")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return Result.success("修改产品状态成功");
    }
}
