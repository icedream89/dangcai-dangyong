package com.dangcai.miniapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.service.ProductService;
import com.dangcai.business.vo.ProductVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序产品接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Tag(name = "小程序-产品", description = "小程序端产品相关接口")
@RestController
@RequestMapping("/miniapp-api/product")
@RequiredArgsConstructor
public class MiniappProductController {

    private final ProductService productService;

    /**
     * 分页查询产品列表
     */
    @Operation(summary = "分页查询产品列表")
    @GetMapping("/page")
    public Result<IPage<ProductVO>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String productName) {

        IPage<ProductVO> page = productService.page(
                categoryId, productName, null,
                current, size
        );
        return Result.success(page);
    }

    /**
     * 查询产品详情
     */
    @Operation(summary = "查询产品详情")
    @GetMapping("/{id}")
    public Result<ProductVO> getById(@PathVariable Long id) {
        ProductVO vo = productService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 查询企业的产品列表
     */
    @Operation(summary = "查询企业的产品列表")
    @GetMapping("/enterprise/{enterpriseId}")
    public Result<java.util.List<ProductVO>> getByEnterpriseId(
            @PathVariable Long enterpriseId) {
        java.util.List<ProductVO> list = productService.getByEnterpriseId(enterpriseId);
        return Result.success(list);
    }

    /**
     * 查询推荐产品
     */
    @Operation(summary = "查询推荐产品")
    @GetMapping("/recommended")
    public Result<java.util.List<ProductVO>> getRecommended(
            @RequestParam(defaultValue = "10") Integer limit) {
        IPage<ProductVO> page = productService.page(
                null, null, 1,
                1, limit
        );
        return Result.success(page.getRecords());
    }
}
