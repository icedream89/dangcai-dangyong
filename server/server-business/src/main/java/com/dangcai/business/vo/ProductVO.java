package com.dangcai.business.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产品VO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class ProductVO {

    /**
     * 产品ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品分类
     */
    private String category;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 单位
     */
    private String unit;

    /**
     * 封面图片
     */
    private String coverImage;

    /**
     * 产品图片（JSON数组）
     */
    private String images;

    /**
     * 产品描述
     */
    private String description;

    /**
     * 规格参数（JSON格式）
     */
    private String specs;

    /**
     * 推荐权重（0-100）
     */
    private Integer recommendWeight;

    /**
     * 推荐等级
     */
    private String recommendLevel;

    /**
     * 销量
     */
    private Integer salesCount;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
