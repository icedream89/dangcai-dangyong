package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 采购需求实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_purchase_requirement")
public class PurchaseRequirement extends BaseEntity {

    /**
     * 需求ID
     */
    @TableId(type = IdType.AUTO)
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
}
