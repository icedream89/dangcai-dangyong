package com.dangcai.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.dangcai.common.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 产品实体类
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("biz_product")
public class Product extends BaseEntity {

    /**
     * 产品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 企业ID
     */
    private Long enterpriseId;

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
}
