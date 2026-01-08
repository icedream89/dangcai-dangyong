package com.dangcai.miniapp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Policy;
import com.dangcai.business.service.PolicyService;
import com.dangcai.business.vo.PolicyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 小程序政策接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Tag(name = "小程序-政策", description = "小程序端政策相关接口")
@RestController
@RequestMapping("/miniapp-api/policy")
@RequiredArgsConstructor
public class MiniappPolicyController {

    private final PolicyService policyService;

    /**
     * 分页查询政策列表
     */
    @Operation(summary = "分页查询政策列表")
    @GetMapping("/page")
    public Result<IPage<PolicyVO>> page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String policyTitle,
            @RequestParam(required = false) String policyType) {

        IPage<PolicyVO> page = policyService.page(
                policyTitle, policyType, 1, // 只查询已发布的
                null, null,
                current, size
        );
        return Result.success(page);
    }

    /**
     * 查询政策详情
     */
    @Operation(summary = "查询政策详情")
    @GetMapping("/{id}")
    public Result<PolicyVO> getById(@PathVariable Long id) {
        PolicyVO vo = policyService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 查询热门政策
     */
    @Operation(summary = "查询热门政策")
    @GetMapping("/hot")
    public Result<java.util.List<PolicyVO>> getHotPolicies(
            @RequestParam(defaultValue = "5") Integer limit) {
        IPage<PolicyVO> page = policyService.page(
                null, null, 1,
                1, null,
                1, limit
        );
        return Result.success(page.getRecords());
    }

    /**
     * 查询置顶政策
     */
    @Operation(summary = "查询置顶政策")
    @GetMapping("/top")
    public Result<java.util.List<PolicyVO>> getTopPolicies() {
        IPage<PolicyVO> page = policyService.page(
                null, null, 1,
                null, 1,
                1, 10
        );
        return Result.success(page.getRecords());
    }

    /**
     * 搜索政策
     */
    @Operation(summary = "搜索政策")
    @GetMapping("/search")
    public Result<IPage<PolicyVO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<PolicyVO> page = policyService.page(
                keyword, null, 1,
                null, null,
                current, size
        );
        return Result.success(page);
    }
}
