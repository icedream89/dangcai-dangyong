package com.dangcai.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.PolicyDTO;
import com.dangcai.business.service.PolicyService;
import com.dangcai.business.vo.PolicyVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 政策管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "政策管理", description = "政策管理相关接口")
@RestController
@RequestMapping("/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    /**
     * 分页查询政策列表
     */
    @Operation(summary = "分页查询政策列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('business:policy:list')")
    public Result<Page<PolicyVO>> page(
            @RequestParam(required = false) String policyTitle,
            @RequestParam(required = false) String policyType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(required = false) Integer isTop,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<PolicyVO> page = policyService.page(
                policyTitle, policyType, status, isHot, isTop, current, size
        );
        return Result.success(page);
    }

    /**
     * 根据ID查询政策详情
     */
    @Operation(summary = "根据ID查询政策详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:policy:query')")
    public Result<PolicyVO> getById(@PathVariable Long id) {
        PolicyVO vo = policyService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 新增政策
     */
    @Operation(summary = "新增政策")
    @PostMapping
    @PreAuthorize("hasAuthority('business:policy:add')")
    public Result<Long> add(@Validated @RequestBody PolicyDTO dto) {
        Long id = policyService.add(dto);
        return Result.success(id, "新增政策成功");
    }

    /**
     * 修改政策
     */
    @Operation(summary = "修改政策")
    @PutMapping
    @PreAuthorize("hasAuthority('business:policy:edit')")
    public Result<Void> update(@Validated @RequestBody PolicyDTO dto) {
        policyService.update(dto);
        return Result.success("修改政策成功");
    }

    /**
     * 删除政策
     */
    @Operation(summary = "删除政策")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:policy:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        policyService.delete(id);
        return Result.success("删除政策成功");
    }

    /**
     * 发布政策
     */
    @Operation(summary = "发布政策")
    @PutMapping("/publish/{id}")
    @PreAuthorize("hasAuthority('business:policy:publish')")
    public Result<Void> publish(@PathVariable Long id) {
        policyService.publish(id);
        return Result.success("发布政策成功");
    }

    /**
     * 取消发布政策
     */
    @Operation(summary = "取消发布政策")
    @PutMapping("/unpublish/{id}")
    @PreAuthorize("hasAuthority('business:policy:unpublish')")
    public Result<Void> unpublish(@PathVariable Long id) {
        policyService.unpublish(id);
        return Result.success("取消发布政策成功");
    }

    /**
     * 设置热门
     */
    @Operation(summary = "设置热门")
    @PutMapping("/hot/{id}")
    @PreAuthorize("hasAuthority('business:policy:edit')")
    public Result<Void> setHot(
            @PathVariable Long id,
            @RequestParam Integer isHot) {
        policyService.setHot(id, isHot);
        return Result.success("设置热门成功");
    }

    /**
     * 设置置顶
     */
    @Operation(summary = "设置置顶")
    @PutMapping("/top/{id}")
    @PreAuthorize("hasAuthority('business:policy:edit')")
    public Result<Void> setTop(
            @PathVariable Long id,
            @RequestParam Integer isTop) {
        policyService.setTop(id, isTop);
        return Result.success("设置置顶成功");
    }
}
