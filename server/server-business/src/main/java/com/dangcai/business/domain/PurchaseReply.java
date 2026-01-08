package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购需求回复实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@TableName("biz_purchase_reply")
public class PurchaseReply {

    /**
     * 回复ID
     */
    @TableId(type = IdType.AUTO)
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
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标志（0-未删除 1-已删除）
     */
    @TableLogic
    private Integer delFlag;
}
