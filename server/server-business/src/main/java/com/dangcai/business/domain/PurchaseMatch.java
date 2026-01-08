package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 需求匹配实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@TableName("biz_purchase_match")
public class PurchaseMatch {

    /**
     * 匹配ID
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
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

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
