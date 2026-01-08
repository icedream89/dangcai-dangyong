package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.Product;
import com.dangcai.business.vo.ProductVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 分页查询产品列表（含企业名称）
     *
     * @param enterpriseId      企业ID
     * @param productName       产品名称
     * @param category          分类
     * @param minRecommendWeight 最小推荐权重
     * @param maxRecommendWeight 最大推荐权重
     * @param status            状态
     * @return 产品VO列表
     */
    List<ProductVO> selectProductList(
            @Param("enterpriseId") Long enterpriseId,
            @Param("productName") String productName,
            @Param("category") String category,
            @Param("minRecommendWeight") Integer minRecommendWeight,
            @Param("maxRecommendWeight") Integer maxRecommendWeight,
            @Param("status") Integer status
    );

    /**
     * 根据ID查询产品VO
     *
     * @param id 产品ID
     * @return 产品VO
     */
    ProductVO selectProductVOById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 产品ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);

    /**
     * 增加销量
     *
     * @param id        产品ID
     * @param quantity  数量
     * @return 影响行数
     */
    int increaseSalesCount(@Param("id") Long id, @Param("quantity") int quantity);
}
