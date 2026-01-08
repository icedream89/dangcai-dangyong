package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 工单流转记录VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class HelpFlowVO {

    /**
     * 流转记录ID
     */
    private Long id;

    /**
     * 工单ID
     */
    private Long ticketId;

    /**
     * 原状态
     */
    private Integer fromStatus;

    /**
     * 原状态名称
     */
    private String fromStatusName;

    /**
     * 目标状态
     */
    private Integer toStatus;

    /**
     * 目标状态名称
     */
    private String toStatusName;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作内容
     */
    private String operation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
