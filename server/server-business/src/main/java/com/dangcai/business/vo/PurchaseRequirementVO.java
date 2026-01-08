package com.dangcai.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购需求VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class PurchaseRequirementVO {

    /**
     * 需求ID
     */
    private Long id;

    /**
     * 需求编号
     */
    private String requirementNo;

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
     * 标题
     */
    private String title;

    /**
     * 采购类别
     */
    private String category;

    /**
     * 需求内容
     */
    private String content;

    /**
     * 预算金额
     */
    private BigDecimal budget;

    /**
     * 期望交付日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expectDate;

    /**
     * 交付地址
     */
    private String address;

    /**
     * 图片地址（多个用逗号分隔）
     */
    private String images;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 回复数量
     */
    private Integer replyCount;

    /**
     * 状态：1-待匹配 2-已匹配 3-已完成 4-已关闭
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
