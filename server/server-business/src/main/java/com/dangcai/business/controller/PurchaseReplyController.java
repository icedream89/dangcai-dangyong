package com.dangcai.business.controller;

import com.dangcai.business.dto.PurchaseReplyDTO;
import com.dangcai.business.service.PurchaseReplyService;
import com.dangcai.business.vo.PurchaseReplyVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 采购需求回复Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "采购需求回复管理", description = "采购需求回复相关接口")
@RestController
@RequestMapping("/purchase-reply")
@RequiredArgsConstructor
public class PurchaseReplyController {

    private final PurchaseReplyService purchaseReplyService;

    /**
     * 根据需求ID查询回复列表
     */
    @Operation(summary = "根据需求ID查询回复列表")
    @GetMapping("/list/{requirementId}")
    @PreAuthorize("hasAuthority('business:purchase:query')")
    public Result<List<PurchaseReplyVO>> listByRequirementId(@PathVariable Long requirementId) {
        List<PurchaseReplyVO> list = purchaseReplyService.listByRequirementId(requirementId);
        return Result.success(list);
    }

    /**
     * 企业回复需求
     */
    @Operation(summary = "企业回复需求")
    @PostMapping
    @PreAuthorize("hasAuthority('business:purchase:reply')")
    public Result<Long> add(@Validated @RequestBody PurchaseReplyDTO dto) {
        Long id = purchaseReplyService.add(dto);
        return Result.success(id, "回复需求成功");
    }

    /**
     * 修改回复
     */
    @Operation(summary = "修改回复")
    @PutMapping
    @PreAuthorize("hasAuthority('business:purchase:reply')")
    public Result<Void> update(@Validated @RequestBody PurchaseReplyDTO dto) {
        purchaseReplyService.update(dto);
        return Result.success("修改回复成功");
    }

    /**
     * 删除回复
     */
    @Operation(summary = "删除回复")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:purchase:reply')")
    public Result<Void> delete(@PathVariable Long id) {
        purchaseReplyService.delete(id);
        return Result.success("删除回复成功");
    }

    /**
     * 确认回复
     */
    @Operation(summary = "确认回复")
    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasAuthority('business:purchase:confirm')")
    public Result<Void> confirmReply(@PathVariable Long id) {
        purchaseReplyService.confirmReply(id);
        return Result.success("确认回复成功");
    }

    /**
     * 完成交易
     */
    @Operation(summary = "完成交易")
    @PutMapping("/complete/{id}")
    @PreAuthorize("hasAuthority('business:purchase:complete')")
    public Result<Void> completeDeal(@PathVariable Long id) {
        purchaseReplyService.completeDeal(id);
        return Result.success("完成交易成功");
    }
}
