package com.dangcai.business.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 产品DTO
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Data
public class ProductDTO {

    /**
     * 产品ID（修改时必填）
     */
    private Long id;

    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空", groups = {Add.class, Update.class})
    private Long enterpriseId;

    /**
     * 产品名称
     */
    @NotBlank(message = "产品名称不能为空", groups = {Add.class, Update.class})
    private String productName;

    /**
     * 产品分类
     */
    private String category;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空", groups = {Add.class, Update.class})
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
     * 状态：0-下架 1-上架
     */
    private Integer status;

    /**
     * 新增分组
     */
    public interface Add {
    }

    /**
     * 修改分组
     */
    public interface Update {
    }
}
