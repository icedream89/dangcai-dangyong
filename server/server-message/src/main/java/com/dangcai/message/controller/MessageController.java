package com.dangcai.message.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dangcai.common.domain.Result;
import com.dangcai.message.domain.BizMessage;
import com.dangcai.message.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息管理Controller
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Tag(name = "消息管理", description = "站内消息、通知等接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 发送消息
     */
    @Operation(summary = "发送消息")
    @PostMapping
    public Result<Long> send(@RequestBody BizMessage message) {
        Long id = messageService.send(message);
        return Result.success("发送成功", id);
    }

    /**
     * 发送系统通知
     */
    @Operation(summary = "发送系统通知")
    @PostMapping("/system-notification")
    public Result<Long> sendSystemNotification(
            @RequestParam Long receiverId,
            @RequestParam String title,
            @RequestParam String content) {
        Long id = messageService.sendSystemNotification(receiverId, title, content);
        return Result.success("发送成功", id);
    }

    /**
     * 批量发送消息
     */
    @Operation(summary = "批量发送消息")
    @PostMapping("/batch")
    public Result<Integer> batchSend(
            @RequestBody Long[] receiverIds,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Integer messageType) {
        int count = messageService.batchSend(receiverIds, title, content, messageType);
        return Result.success(String.format("发送完成，成功%d条", count), count);
    }

    /**
     * 标记为已读
     */
    @Operation(summary = "标记为已读")
    @PutMapping("/read/{id}")
    public Result<Void> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return Result.success("标记成功");
    }

    /**
     * 批量标记为已读
     */
    @Operation(summary = "批量标记为已读")
    @PutMapping("/read/batch")
    public Result<Void> batchMarkAsRead(@RequestBody Long[] ids) {
        messageService.batchMarkAsRead(ids);
        return Result.success("标记成功");
    }

    /**
     * 标记所有消息为已读
     */
    @Operation(summary = "标记所有消息为已读")
    @PutMapping("/read/all")
    public Result<Void> markAllAsRead(@RequestParam Long receiverId) {
        messageService.markAllAsRead(receiverId);
        return Result.success("标记成功");
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        messageService.delete(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除消息
     */
    @Operation(summary = "批量删除消息")
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Long[] ids) {
        messageService.batchDelete(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取消息详情
     */
    @Operation(summary = "获取消息详情")
    @GetMapping("/{id}")
    public Result<BizMessage> getById(@PathVariable Long id) {
        BizMessage message = messageService.getById(id);
        return Result.success(message);
    }

    /**
     * 分页查询消息列表
     */
    @Operation(summary = "分页查询消息列表")
    @GetMapping("/page")
    public Result<IPage<BizMessage>> page(
            @RequestParam Long receiverId,
            @RequestParam(required = false) Integer messageType,
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        IPage<BizMessage> pageResult = messageService.page(receiverId, messageType, isRead, page, size);
        return Result.success(pageResult);
    }

    /**
     * 获取未读消息数量
     */
    @Operation(summary = "获取未读消息数量")
    @GetMapping("/unread-count")
    public Result<Long> getUnreadCount(@RequestParam Long receiverId) {
        Long count = messageService.getUnreadCount(receiverId);
        return Result.success(count);
    }

    /**
     * 获取最新消息列表
     */
    @Operation(summary = "获取最新消息列表")
    @GetMapping("/latest")
    public Result<List<BizMessage>> getLatestMessages(
            @RequestParam Long receiverId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<BizMessage> messages = messageService.getLatestMessages(receiverId, limit);
        return Result.success(messages);
    }
}
