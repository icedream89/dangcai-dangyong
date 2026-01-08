package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.Course;
import com.dangcai.business.vo.CourseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业课堂Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 分页查询课堂列表
     *
     * @param courseTitle    课堂标题
     * @param courseType     课堂类型
     * @param category       分类
     * @param isRecommended  是否推荐
     * @param status         状态
     * @return 课堂VO列表
     */
    List<CourseVO> selectCourseList(
            @Param("courseTitle") String courseTitle,
            @Param("courseType") String courseType,
            @Param("category") String category,
            @Param("isRecommended") Integer isRecommended,
            @Param("status") Integer status
    );

    /**
     * 根据ID查询课堂VO
     *
     * @param id 课堂ID
     * @return 课堂VO
     */
    CourseVO selectCourseVOById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 课堂ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);
}
