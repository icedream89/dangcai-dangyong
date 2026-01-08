package com.dangcai.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import com.dangcai.message.domain.BizMessage;
import com.dangcai.message.mapper.MessageMapper;
import com.dangcai.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 消息服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long send(BizMessage message) {
        // 设置默认值
        if (message.getIsRead() == null) {
            message.setIsRead(0);
        }
        if (message.getStatus() == null) {
            message.setStatus(0);
        }

        messageMapper.insert(message);

        log.info("消息发送成功，消息ID：{}，接收用户：{}", message.getId(), message.getReceiverId());

        // TODO: 通过WebSocket推送消息给在线用户

        return message.getId();
    }

    @Override
    public Long sendSystemNotification(Long receiverId, String title, String content) {
        BizMessage message = new BizMessage();
        message.setTitle(title);
        message.setContent(content);
        message.setMessageType(1); // 系统通知
        message.setSenderId(SecurityUtils.getUserId());
        message.setSenderName(SecurityUtils.getUsername());
        message.setReceiverId(receiverId);

        return send(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchSend(Long[] receiverIds, String title, String content, Integer messageType) {
        int successCount = 0;

        for (Long receiverId : receiverIds) {
            try {
                BizMessage message = new BizMessage();
                message.setTitle(title);
                message.setContent(content);
                message.setMessageType(messageType);
                message.setSenderId(SecurityUtils.getUserId());
                message.setSenderName(SecurityUtils.getUsername());
                message.setReceiverId(receiverId);

                send(message);
                successCount++;
            } catch (Exception e) {
                log.error("消息发送失败，接收用户ID：{}", receiverId, e);
            }
        }

        return successCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAsRead(Long id) {
        BizMessage message = getById(id);

        if (message.getIsRead() == 1) {
            throw new BusinessException("消息已阅读");
        }

        message.setIsRead(1);
        message.setReadTime(LocalDateTime.now());
        messageMapper.updateById(message);

        log.info("消息标记为已读，消息ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchMarkAsRead(Long[] ids) {
        for (Long id : ids) {
            try {
                markAsRead(id);
            } catch (Exception e) {
                log.error("标记已读失败，消息ID：{}", id, e);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void markAllAsRead(Long receiverId) {
        LambdaUpdateWrapper<BizMessage> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BizMessage::getReceiverId, receiverId)
                .eq(BizMessage::getIsRead, 0)
                .set(BizMessage::getIsRead, 1)
                .set(BizMessage::getReadTime, LocalDateTime.now());

        messageMapper.update(null, wrapper);

        log.info("用户{}的所有消息已标记为已读", receiverId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        BizMessage message = getById(id);

        // 逻辑删除
        message.setStatus(1);
        messageMapper.updateById(message);

        log.info("消息删除成功，消息ID：{}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        for (Long id : ids) {
            try {
                delete(id);
            } catch (Exception e) {
                log.error("删除消息失败，消息ID：{}", id, e);
            }
        }
    }

    @Override
    public BizMessage getById(Long id) {
        BizMessage message = messageMapper.selectById(id);
        if (message == null || message.getStatus() == 1) {
            throw new BusinessException("消息不存在");
        }
        return message;
    }

    @Override
    public IPage<BizMessage> page(Long receiverId, Integer messageType, Integer isRead, Integer page, Integer size) {
        Page<BizMessage> pageParam = new Page<>(page, size);

        LambdaQueryWrapper<BizMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BizMessage::getReceiverId, receiverId)
                .eq(messageType != null, BizMessage::getMessageType, messageType)
                .eq(isRead != null, BizMessage::getIsRead, isRead)
                .eq(BizMessage::getStatus, 0)
                .orderByDesc(BizMessage::getCreateTime);

        return messageMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Long getUnreadCount(Long receiverId) {
        LambdaQueryWrapper<BizMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BizMessage::getReceiverId, receiverId)
                .eq(BizMessage::getIsRead, 0)
                .eq(BizMessage::getStatus, 0);

        return messageMapper.selectCount(wrapper);
    }

    @Override
    public List<BizMessage> getLatestMessages(Long receiverId, Integer limit) {
        LambdaQueryWrapper<BizMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BizMessage::getReceiverId, receiverId)
                .eq(BizMessage::getStatus, 0)
                .orderByDesc(BizMessage::getCreateTime)
                .last("LIMIT " + (limit != null ? limit : 10));

        return messageMapper.selectList(wrapper);
    }
}
