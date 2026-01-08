package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dangcai.business.domain.Category;
import com.dangcai.business.dto.CategoryDTO;
import com.dangcai.business.mapper.CategoryMapper;
import com.dangcai.business.service.CategoryService;
import com.dangcai.business.vo.CategoryVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> tree(Integer status) {
        // 查询所有分类
        List<CategoryVO> allCategories = categoryMapper.selectAllForTree(status);

        // 构建树形结构
        return buildTree(allCategories, 0L);
    }

    @Override
    public CategoryVO getVOById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null || category.getDelFlag() == 1) {
            throw new BusinessException("分类不存在");
        }

        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        vo.setStatusName(category.getStatus() == 1 ? "正常" : "禁用");

        // 查询子分类
        List<Category> children = categoryMapper.selectByParentId(id);
        if (children != null && !children.isEmpty()) {
            List<CategoryVO> childrenVO = children.stream()
                    .map(child -> {
                        CategoryVO childVO = new CategoryVO();
                        BeanUtils.copyProperties(child, childVO);
                        childVO.setStatusName(child.getStatus() == 1 ? "正常" : "禁用");
                        return childVO;
                    })
                    .collect(Collectors.toList());
            vo.setChildren(childrenVO);
        }

        return vo;
    }

    @Override
    public Category getById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null || category.getDelFlag() == 1) {
            throw new BusinessException("分类不存在");
        }
        return category;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(CategoryDTO dto) {
        // 检查分类编码唯一性
        if (dto.getCategoryCode() != null && !dto.getCategoryCode().isEmpty()) {
            Category existCategory = categoryMapper.selectOne(
                    new LambdaQueryWrapper<Category>()
                            .eq(Category::getCategoryCode, dto.getCategoryCode())
            );
            if (existCategory != null) {
                throw new BusinessException("分类编码已存在");
            }
        }

        // 检查父分类是否存在
        if (dto.getParentId() != 0) {
            Category parentCategory = categoryMapper.selectById(dto.getParentId());
            if (parentCategory == null || parentCategory.getDelFlag() == 1) {
                throw new BusinessException("父分类不存在");
            }
        }

        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        if (category.getManageMode() == null || category.getManageMode().isEmpty()) {
            category.setManageMode("list");
        }
        category.setCreateBy(SecurityUtils.getUsername());
        categoryMapper.insert(category);

        log.info("新增分类成功，分类ID：{}，分类名称：{}", category.getId(), category.getCategoryName());
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CategoryDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("分类ID不能为空");
        }

        Category oldCategory = getById(dto.getId());

        // 检查分类编码唯一性（排除自己）
        if (dto.getCategoryCode() != null && !dto.getCategoryCode().isEmpty()) {
            Category existCategory = categoryMapper.selectOne(
                    new LambdaQueryWrapper<Category>()
                            .eq(Category::getCategoryCode, dto.getCategoryCode())
                            .ne(Category::getId, dto.getId())
            );
            if (existCategory != null) {
                throw new BusinessException("分类编码已存在");
            }
        }

        // 检查父分类是否存在（且不能设置自己为父分类）
        if (dto.getParentId() != 0) {
            if (dto.getParentId().equals(dto.getId())) {
                throw new BusinessException("不能设置自己为父分类");
            }
            Category parentCategory = categoryMapper.selectById(dto.getParentId());
            if (parentCategory == null || parentCategory.getDelFlag() == 1) {
                throw new BusinessException("父分类不存在");
            }
        }

        Category category = new Category();
        BeanUtils.copyProperties(dto, category);
        category.setUpdateBy(SecurityUtils.getUsername());
        categoryMapper.updateById(category);

        log.info("修改分类成功，分类ID：{}，分类名称：{}", category.getId(), category.getCategoryName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Category category = getById(id);

        // 检查是否有子分类
        List<Category> children = categoryMapper.selectByParentId(id);
        if (children != null && !children.isEmpty()) {
            throw new BusinessException("该分类下有子分类，不能删除");
        }

        // 逻辑删除
        category.setDelFlag(1);
        category.setUpdateBy(SecurityUtils.getUsername());
        categoryMapper.updateById(category);

        log.info("删除分类成功，分类ID：{}，分类名称：{}", id, category.getCategoryName());
    }

    @Override
    public List<Category> listByParentId(Long parentId) {
        return categoryMapper.selectByParentId(parentId);
    }

    /**
     * 构建树形结构
     *
     * @param allCategories 所有分类列表
     * @param parentId      父分类ID
     * @return 树形结构
     */
    private List<CategoryVO> buildTree(List<CategoryVO> allCategories, Long parentId) {
        List<CategoryVO> tree = new ArrayList<>();

        for (CategoryVO category : allCategories) {
            if (category.getParentId().equals(parentId)) {
                // 递归查找子分类
                List<CategoryVO> children = buildTree(allCategories, category.getId());
                category.setChildren(children);
                tree.add(category);
            }
        }

        return tree;
    }
}
