package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.Category;
import com.dangcai.business.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 分类Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询所有分类（用于构建树形结构）
     *
     * @param status 状态（可选）
     * @return 分类VO列表
     */
    List<CategoryVO> selectAllForTree(@Param("status") Integer status);

    /**
     * 查询子分类列表
     *
     * @param parentId 父分类ID
     * @return 分类列表
     */
    List<Category> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 查询分类及其所有子分类的数量
     *
     * @param categoryId 分类ID
     * @return 总数量（含自己）
     */
    int countWithChildren(@Param("categoryId") Long categoryId);
}
