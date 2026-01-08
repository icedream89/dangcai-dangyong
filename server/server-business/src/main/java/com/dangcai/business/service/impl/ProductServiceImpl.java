package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Product;
import com.dangcai.business.dto.ProductDTO;
import com.dangcai.business.mapper.ProductMapper;
import com.dangcai.business.service.ProductService;
import com.dangcai.business.vo.ProductVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 产品服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Page<ProductVO> page(Long enterpriseId, String productName, String category,
                                Integer minRecommendWeight, Integer maxRecommendWeight,
                                Integer status, Integer current, Integer size) {
        Page<ProductVO> page = new Page<>(current, size);
        page.setRecords(productMapper.selectProductList(
                enterpriseId, productName, category,
                minRecommendWeight, maxRecommendWeight, status
        ));
        return page;
    }

    @Override
    public ProductVO getVOById(Long id) {
        ProductVO vo = productMapper.selectProductVOById(id);
        if (vo == null) {
            throw new BusinessException("产品不存在");
        }

        // 设置推荐等级名称
        if (vo.getRecommendWeight() != null) {
            vo.setRecommendLevelName(getRecommendLevelName(vo.getRecommendWeight()));
        }

        // 设置状态名称
        if (vo.getStatus() != null) {
            vo.setStatusName(vo.getStatus() == 1 ? "上架" : "下架");
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(ProductDTO dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setRecommendWeight(0);
        product.setSalesCount(0);
        product.setViewCount(0);
        product.setStatus(0); // 默认下架
        product.setCreateBy(SecurityUtils.getUsername());
        productMapper.insert(product);

        log.info("新增产品成功，产品ID：{}，产品名称：{}", product.getId(), product.getProductName());
        return product.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ProductDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("产品ID不能为空");
        }

        Product oldProduct = getById(dto.getId());

        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        product.setUpdateBy(SecurityUtils.getUsername());
        productMapper.updateById(product);

        log.info("修改产品成功，产品ID：{}，产品名称：{}", product.getId(), product.getProductName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Product product = getById(id);

        // 逻辑删除
        product.setDelFlag(1);
        product.setUpdateBy(SecurityUtils.getUsername());
        productMapper.updateById(product);

        log.info("删除产品成功，产品ID：{}，产品名称：{}", id, product.getProductName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRecommendWeight(Long id, Integer recommendWeight) {
        if (recommendWeight < 0 || recommendWeight > 100) {
            throw new BusinessException("推荐权重必须在0-100之间");
        }

        Product product = getById(id);

        product.setRecommendWeight(recommendWeight);
        product.setUpdateBy(SecurityUtils.getUsername());
        productMapper.updateById(product);

        log.info("设置产品推荐权重成功，产品ID：{}，推荐权重：{}，推荐等级：{}",
                id, recommendWeight, getRecommendLevelName(recommendWeight));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Product product = getById(id);

        product.setStatus(status);
        product.setUpdateBy(SecurityUtils.getUsername());
        productMapper.updateById(product);

        log.info("修改产品状态成功，产品ID：{}，状态：{}", id, status == 1 ? "上架" : "下架");
    }

    /**
     * 根据ID获取产品实体
     */
    private Product getById(Long id) {
        Product product = productMapper.selectById(id);
        if (product == null || product.getDelFlag() == 1) {
            throw new BusinessException("产品不存在");
        }
        return product;
    }

    /**
     * 获取推荐等级名称
     */
    private String getRecommendLevelName(Integer recommendWeight) {
        if (recommendWeight == null) {
            return "未知";
        }
        if (recommendWeight >= 80) {
            return "热门";
        } else if (recommendWeight >= 50) {
            return "推荐";
        } else {
            return "普通";
        }
    }
}
