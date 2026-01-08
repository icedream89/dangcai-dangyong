package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购需求回复VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseReplyVO {

    /**
     * 回复ID
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
     * 联系人
     */
    private String contactPerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 报价金额
     */
    private BigDecimal quotePrice;

    /**
     * 交付周期（天）
     */
    private Integer deliveryPeriod;

    /**
     * 附件地址（多个用逗号分隔）
     */
    private String attachments;

    /**
     * 状态：1-已回复 2-已确认 3-已完成交易
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

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
