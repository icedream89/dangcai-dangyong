package com.dangcai.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.PurchaseMatchDTO;
import com.dangcai.business.dto.PurchaseRequirementDTO;
import com.dangcai.business.dto.PurchaseRequirementQueryDTO;
import com.dangcai.business.service.PurchaseRequirementService;
import com.dangcai.business.vo.PurchaseMatchVO;
import com.dangcai.business.vo.PurchaseRequirementVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购需求Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "采购需求管理", description = "采购需求相关接口")
@RestController
@RequestMapping("/purchase-requirement")
@RequiredArgsConstructor
public class PurchaseRequirementController {

    private final PurchaseRequirementService purchaseRequirementService;

    /**
     * 分页查询采购需求列表
     */
    @Operation(summary = "分页查询采购需求列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('business:purchase:list')")
    public Result<Page<PurchaseRequirementVO>> page(PurchaseRequirementQueryDTO queryDTO) {
        Page<PurchaseRequirementVO> page = purchaseRequirementService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 根据ID查询采购需求详情
     */
    @Operation(summary = "根据ID查询采购需求详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:purchase:query')")
    public Result<PurchaseRequirementVO> getById(@PathVariable Long id) {
        PurchaseRequirementVO vo = purchaseRequirementService.getVOById(id);
        // 增加浏览次数
        purchaseRequirementService.increaseViewCount(id);
        return Result.success(vo);
    }

    /**
     * 新增采购需求
     */
    @Operation(summary = "新增采购需求")
    @PostMapping
    @PreAuthorize("hasAuthority('business:purchase:add')")
    public Result<Long> add(@Validated(PurchaseRequirementDTO.Add.class) @RequestBody PurchaseRequirementDTO dto) {
        Long id = purchaseRequirementService.add(dto);
        return Result.success(id, "新增采购需求成功");
    }

    /**
     * 修改采购需求
     */
    @Operation(summary = "修改采购需求")
    @PutMapping
    @PreAuthorize("hasAuthority('business:purchase:edit')")
    public Result<Void> update(@Validated(PurchaseRequirementDTO.Update.class) @RequestBody PurchaseRequirementDTO dto) {
        purchaseRequirementService.update(dto);
        return Result.success("修改采购需求成功");
    }

    /**
     * 删除采购需求
     */
    @Operation(summary = "删除采购需求")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:purchase:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        purchaseRequirementService.delete(id);
        return Result.success("删除采购需求成功");
    }

    /**
     * 匹配供应商企业
     */
    @Operation(summary = "匹配供应商企业")
    @PostMapping("/match")
    @PreAuthorize("hasAuthority('business:purchase:match')")
    public Result<Void> matchEnterprises(@Validated @RequestBody PurchaseMatchDTO dto) {
        purchaseRequirementService.matchEnterprises(dto);
        return Result.success("匹配供应商企业成功");
    }

    /**
     * 取消匹配
     */
    @Operation(summary = "取消匹配")
    @DeleteMapping("/match/{requirementId}/{enterpriseId}")
    @PreAuthorize("hasAuthority('business:purchase:match')")
    public Result<Void> cancelMatch(
            @PathVariable Long requirementId,
            @PathVariable Long enterpriseId,
            @RequestParam(required = false) String cancelReason) {
        purchaseRequirementService.cancelMatch(requirementId, enterpriseId, cancelReason);
        return Result.success("取消匹配成功");
    }

    /**
     * 查询需求已匹配的企业列表
     */
    @Operation(summary = "查询需求已匹配的企业列表")
    @GetMapping("/match/{requirementId}")
    @PreAuthorize("hasAuthority('business:purchase:query')")
    public Result<List<PurchaseMatchVO>> listMatchedEnterprises(@PathVariable Long requirementId) {
        List<PurchaseMatchVO> list = purchaseRequirementService.listMatchedEnterprises(requirementId);
        return Result.success(list);
    }

    /**
     * 修改需求状态
     */
    @Operation(summary = "修改需求状态")
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAuthority('business:purchase:edit')")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        purchaseRequirementService.updateStatus(id, status);
        return Result.success("修改需求状态成功");
    }
}
