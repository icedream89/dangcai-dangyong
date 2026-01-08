package com.dangcai.business.dto;

import lombok.Data;

/**
 * 求助工单查询DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class HelpTicketQueryDTO {

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
     * 求助类型
     */
    private Integer helpType;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 标题（模糊查询）
     */
    private String title;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 当前页码
     */
    private Integer current = 1;

    /**
     * 每页条数
     */
    private Integer size = 10;
}
