package com.dangcai.enterprise.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.result.Result;
import com.dangcai.enterprise.dto.EnterpriseDTO;
import com.dangcai.enterprise.dto.EnterpriseQueryDTO;
import com.dangcai.enterprise.service.EnterpriseService;
import com.dangcai.enterprise.vo.EnterpriseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 企业管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "企业管理", description = "企业管理相关接口")
@RestController
@RequestMapping("/enterprise")
@RequiredArgsConstructor
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    /**
     * 分页查询企业列表
     */
    @Operation(summary = "分页查询企业列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('enterprise:enterprise:list')")
    public Result<Page<EnterpriseVO>> page(EnterpriseQueryDTO queryDTO) {
        Page<EnterpriseVO> page = enterpriseService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 根据ID查询企业详情
     */
    @Operation(summary = "根据ID查询企业详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('enterprise:enterprise:query')")
    public Result<EnterpriseVO> getById(@PathVariable Long id) {
        EnterpriseVO vo = enterpriseService.getVOById(id);
        // 增加浏览次数
        enterpriseService.increaseViewCount(id);
        return Result.success(vo);
    }

    /**
     * 新增企业
     */
    @Operation(summary = "新增企业")
    @PostMapping
    @PreAuthorize("hasAuthority('enterprise:enterprise:add')")
    public Result<Long> add(@Validated(EnterpriseDTO.Add.class) @RequestBody EnterpriseDTO dto) {
        Long id = enterpriseService.add(dto);
        return Result.success(id, "新增企业成功");
    }

    /**
     * 修改企业
     */
    @Operation(summary = "修改企业")
    @PutMapping
    @PreAuthorize("hasAuthority('enterprise:enterprise:edit')")
    public Result<Void> update(@Validated(EnterpriseDTO.Update.class) @RequestBody EnterpriseDTO dto) {
        enterpriseService.update(dto);
        return Result.success("修改企业成功");
    }

    /**
     * 删除企业
     */
    @Operation(summary = "删除企业")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('enterprise:enterprise:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        enterpriseService.delete(id);
        return Result.success("删除企业成功");
    }

    /**
     * 审核企业
     */
    @Operation(summary = "审核企业")
    @PutMapping("/audit/{id}")
    @PreAuthorize("hasAuthority('enterprise:enterprise:audit')")
    public Result<Void> audit(
            @PathVariable Long id,
            @RequestParam Integer status,
            @RequestParam(required = false) String remark) {
        enterpriseService.audit(id, status, remark);
        return Result.success("审核企业成功");
    }

    /**
     * 修改企业状态
     */
    @Operation(summary = "修改企业状态")
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAuthority('enterprise:enterprise:edit')")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        enterpriseService.updateStatus(id, status);
        return Result.success("修改企业状态成功");
    }

    /**
     * 设置推荐
     */
    @Operation(summary = "设置推荐")
    @PutMapping("/recommend/{id}")
    @PreAuthorize("hasAuthority('enterprise:enterprise:edit')")
    public Result<Void> setRecommended(
            @PathVariable Long id,
            @RequestParam Integer isRecommended) {
        enterpriseService.setRecommended(id, isRecommended);
        return Result.success("设置推荐成功");
    }
}
