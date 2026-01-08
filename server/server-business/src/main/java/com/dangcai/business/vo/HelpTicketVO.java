package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 求助工单VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class HelpTicketVO {

    /**
     * 工单ID
     */
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
     * 企业名称
     */
    private String enterpriseName;

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
     * 求助类型名称
     */
    private String helpTypeName;

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
     * 优先级名称
     */
    private String priorityName;

    /**
     * 状态：1-待处理 2-处理中 3-已完成 4-已关闭
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 处理人名称
     */
    private String handlerName;

    /**
     * 处理结果
     */
    private String handleResult;

    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    /**
     * 满意度评分：1-5分
     */
    private Integer satisfaction;

    /**
     * 用户反馈
     */
    private String feedback;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
