package com.dangcai.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Product;
import com.dangcai.business.dto.ProductDTO;
import com.dangcai.business.vo.ProductVO;

/**
 * 产品服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface ProductService {

    /**
     * 分页查询产品列表
     *
     * @param enterpriseId       企业ID
     * @param productName        产品名称
     * @param category           分类
     * @param minRecommendWeight 最小推荐权重
     * @param maxRecommendWeight 最大推荐权重
     * @param status             状态
     * @param current            当前页
     * @param size               每页条数
     * @return 分页结果
     */
    Page<ProductVO> page(Long enterpriseId, String productName, String category,
                         Integer minRecommendWeight, Integer maxRecommendWeight,
                         Integer status, Integer current, Integer size);

    /**
     * 根据ID查询产品详情
     *
     * @param id 产品ID
     * @return 产品VO
     */
    ProductVO getVOById(Long id);

    /**
     * 新增产品
     *
     * @param dto 产品DTO
     * @return 产品ID
     */
    Long add(ProductDTO dto);

    /**
     * 修改产品
     *
     * @param dto 产品DTO
     */
    void update(ProductDTO dto);

    /**
     * 删除产品
     *
     * @param id 产品ID
     */
    void delete(Long id);

    /**
     * 设置推荐权重
     *
     * @param id              产品ID
     * @param recommendWeight 推荐权重（0-100）
     */
    void setRecommendWeight(Long id, Integer recommendWeight);

    /**
     * 上架/下架
     *
     * @param id     产品ID
     * @param status 状态：0-下架 1-上架
     */
    void updateStatus(Long id, Integer status);
}
