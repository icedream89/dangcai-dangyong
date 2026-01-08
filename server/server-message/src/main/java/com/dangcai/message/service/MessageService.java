package com.dangcai.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.message.domain.BizMessage;

/**
 * 消息服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface MessageService {

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 消息ID
     */
    Long send(BizMessage message);

    /**
     * 发送系统通知
     *
     * @param receiverId 接收用户ID
     * @param title      标题
     * @param content    内容
     * @return 消息ID
     */
    Long sendSystemNotification(Long receiverId, String title, String content);

    /**
     * 批量发送消息
     *
     * @param receiverIds 接收用户ID列表
     * @param title       标题
     * @param content     内容
     * @param messageType 消息类型
     * @return 发送数量
     */
    int batchSend(Long[] receiverIds, String title, String content, Integer messageType);

    /**
     * 标记为已读
     *
     * @param id 消息ID
     */
    void markAsRead(Long id);

    /**
     * 批量标记为已读
     *
     * @param ids 消息ID列表
     */
    void batchMarkAsRead(Long[] ids);

    /**
     * 标记所有消息为已读
     *
     * @param receiverId 接收用户ID
     */
    void markAllAsRead(Long receiverId);

    /**
     * 删除消息
     *
     * @param id 消息ID
     */
    void delete(Long id);

    /**
     * 批量删除消息
     *
     * @param ids 消息ID列表
     */
    void batchDelete(Long[] ids);

    /**
     * 获取消息详情
     *
     * @param id 消息ID
     * @return 消息
     */
    BizMessage getById(Long id);

    /**
     * 分页查询消息列表
     *
     * @param receiverId  接收用户ID
     * @param messageType 消息类型
     * @param isRead      是否已读
     * @param page        页码
     * @param size        每页数量
     * @return 消息列表
     */
    IPage<BizMessage> page(Long receiverId, Integer messageType, Integer isRead, Integer page, Integer size);

    /**
     * 获取未读消息数量
     *
     * @param receiverId 接收用户ID
     * @return 未读数量
     */
    Long getUnreadCount(Long receiverId);

    /**
     * 获取最新消息列表
     *
     * @param receiverId 接收用户ID
     * @param limit      数量限制
     * @return 消息列表
     */
    java.util.List<BizMessage> getLatestMessages(Long receiverId, Integer limit);
}
