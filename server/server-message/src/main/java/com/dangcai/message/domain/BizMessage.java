package com.dangcai.message.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 业务消息实体
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_message")
public class BizMessage extends BaseEntity {

    /**
     * 消息ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型：1-系统通知 2-工单消息 3-供需消息 4-审核消息
     */
    private Integer messageType;

    /**
     * 发送用户ID
     */
    private Long senderId;

    /**
     * 发送用户名
     */
    private String senderName;

    /**
     * 接收用户ID
     */
    private Long receiverId;

    /**
     * 接收用户名
     */
    private String receiverName;

    /**
     * 是否已读：0-未读 1-已读
     */
    private Integer isRead;

    /**
     * 阅读时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime readTime;

    /**
     * 关联业务类型
     */
    private String businessType;

    /**
     * 关联业务ID
     */
    private Long businessId;

    /**
     * 跳转URL
     */
    private String jumpUrl;

    /**
     * 消息状态：0-正常 1-已删除
     */
    private Integer status;
}
