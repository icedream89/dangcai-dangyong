package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 需求匹配VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseMatchVO {

    /**
     * 匹配ID
     */
    private Long id;

    /**
     * 需求ID
     */
    private Long requirementId;

    /**
     * 供应商企业ID
     */
    private Long enterpriseId;

    /**
     * 供应商企业名称
     */
    private String enterpriseName;

    /**
     * 匹配操作人ID
     */
    private Long operatorId;

    /**
     * 匹配操作人名称
     */
    private String operatorName;

    /**
     * 匹配时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime matchTime;

    /**
     * 是否已回复：0-否 1-是
     */
    private Integer hasReplied;

    /**
     * 状态：1-已匹配 2-已取消
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 取消时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;
}
