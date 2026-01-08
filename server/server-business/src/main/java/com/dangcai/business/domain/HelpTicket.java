package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 求助工单实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_help_ticket")
public class HelpTicket extends BaseEntity {

    /**
     * 工单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工单编号
     */
    private String ticketNo;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 求助类型：1-技术咨询 2-故障报修 3-服务请求 4-投诉建议
     */
    private Integer helpType;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片地址（多个用逗号分隔）
     */
    private String images;

    /**
     * 优先级：1-紧急 2-普通 3-低
     */
    private Integer priority;

    /**
     * 状态：1-待处理 2-处理中 3-已完成 4-已关闭
     */
    private Integer status;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理时间
     */
    private LocalDateTime handleTime;

    /**
     * 满意度评分：1-5分
     */
    private Integer satisfaction;

    /**
     * 用户反馈
     */
    private String feedback;
}
