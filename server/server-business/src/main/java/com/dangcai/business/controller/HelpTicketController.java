package com.dangcai.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.HandleDTO;
import com.dangcai.business.dto.HelpTicketDTO;
import com.dangcai.business.dto.HelpTicketQueryDTO;
import com.dangcai.business.dto.SatisfactionDTO;
import com.dangcai.business.service.HelpTicketService;
import com.dangcai.business.vo.HelpTicketVO;
import com.dangcai.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 求助工单Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "求助工单管理", description = "求助工单相关接口")
@RestController
@RequestMapping("/help-ticket")
@RequiredArgsConstructor
public class HelpTicketController {

    private final HelpTicketService helpTicketService;

    /**
     * 分页查询工单列表
     */
    @Operation(summary = "分页查询工单列表")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('business:ticket:list')")
    public Result<Page<HelpTicketVO>> page(HelpTicketQueryDTO queryDTO) {
        Page<HelpTicketVO> page = helpTicketService.page(queryDTO);
        return Result.success(page);
    }

    /**
     * 根据ID查询工单详情
     */
    @Operation(summary = "根据ID查询工单详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('business:ticket:query')")
    public Result<HelpTicketVO> getById(@PathVariable Long id) {
        HelpTicketVO vo = helpTicketService.getVOById(id);
        return Result.success(vo);
    }

    /**
     * 新增工单
     */
    @Operation(summary = "新增工单")
    @PostMapping
    @PreAuthorize("hasAuthority('business:ticket:add')")
    public Result<Long> add(@Validated(HelpTicketDTO.Add.class) @RequestBody HelpTicketDTO dto) {
        Long id = helpTicketService.add(dto);
        return Result.success(id, "新增工单成功");
    }

    /**
     * 修改工单
     */
    @Operation(summary = "修改工单")
    @PutMapping
    @PreAuthorize("hasAuthority('business:ticket:edit')")
    public Result<Void> update(@Validated(HelpTicketDTO.Update.class) @RequestBody HelpTicketDTO dto) {
        helpTicketService.update(dto);
        return Result.success("修改工单成功");
    }

    /**
     * 删除工单
     */
    @Operation(summary = "删除工单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('business:ticket:remove')")
    public Result<Void> delete(@PathVariable Long id) {
        helpTicketService.delete(id);
        return Result.success("删除工单成功");
    }

    /**
     * 分配处理人
     */
    @Operation(summary = "分配处理人")
    @PutMapping("/assign/{id}")
    @PreAuthorize("hasAuthority('business:ticket:assign')")
    public Result<Void> assignHandler(
            @PathVariable Long id,
            @RequestParam Long handlerId) {
        helpTicketService.assignHandler(id, handlerId);
        return Result.success("分配处理人成功");
    }

    /**
     * 开始处理工单
     */
    @Operation(summary = "开始处理工单")
    @PutMapping("/start/{id}")
    @PreAuthorize("hasAuthority('business:ticket:handle')")
    public Result<Void> startHandle(@PathVariable Long id) {
        helpTicketService.startHandle(id);
        return Result.success("开始处理工单成功");
    }

    /**
     * 完成处理工单
     */
    @Operation(summary = "完成处理工单")
    @PutMapping("/complete")
    @PreAuthorize("hasAuthority('business:ticket:handle')")
    public Result<Void> completeHandle(@Validated @RequestBody HandleDTO dto) {
        helpTicketService.completeHandle(dto);
        return Result.success("完成处理工单成功");
    }

    /**
     * 满意度评价
     */
    @Operation(summary = "满意度评价")
    @PutMapping("/satisfaction")
    @PreAuthorize("hasAuthority('business:ticket:evaluate')")
    public Result<Void> satisfaction(@Validated @RequestBody SatisfactionDTO dto) {
        helpTicketService.satisfaction(dto);
        return Result.success("满意度评价成功");
    }

    /**
     * 关闭工单
     */
    @Operation(summary = "关闭工单")
    @PutMapping("/close/{id}")
    @PreAuthorize("hasAuthority('business:ticket:close')")
    public Result<Void> closeTicket(@PathVariable Long id) {
        helpTicketService.closeTicket(id);
        return Result.success("关闭工单成功");
    }
}
