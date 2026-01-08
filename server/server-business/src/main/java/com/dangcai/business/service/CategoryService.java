package com.dangcai.business.service;

import com.dangcai.business.domain.Category;
import com.dangcai.business.dto.CategoryDTO;
import com.dangcai.business.vo.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface CategoryService {

    /**
     * 查询分类树
     *
     * @param status 状态（可选，null查询所有）
     * @return 分类树
     */
    List<CategoryVO> tree(Integer status);

    /**
     * 根据ID查询分类详情
     *
     * @param id 分类ID
     * @return 分类VO
     */
    CategoryVO getVOById(Long id);

    /**
     * 根据ID查询分类实体
     *
     * @param id 分类ID
     * @return 分类实体
     */
    Category getById(Long id);

    /**
     * 新增分类
     *
     * @param dto 分类DTO
     * @return 分类ID
     */
    Long add(CategoryDTO dto);

    /**
     * 修改分类
     *
     * @param dto 分类DTO
     */
    void update(CategoryDTO dto);

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void delete(Long id);

    /**
     * 查询子分类列表
     *
     * @param parentId 父分类ID
     * @return 子分类列表
     */
    List<Category> listByParentId(Long parentId);
}
