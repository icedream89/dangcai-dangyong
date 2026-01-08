package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Course;
import com.dangcai.business.dto.CourseDTO;
import com.dangcai.business.mapper.CourseMapper;
import com.dangcai.business.service.CourseService;
import com.dangcai.business.vo.CourseVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 企业课堂服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    @Override
    public Page<CourseVO> page(String courseTitle, String courseType,
                               String category, Integer isRecommended,
                               Integer status, Integer current, Integer size) {
        Page<CourseVO> page = new Page<>(current, size);
        page.setRecords(courseMapper.selectCourseList(
                courseTitle, courseType, category, isRecommended, status
        ));
        return page;
    }

    @Override
    public CourseVO getVOById(Long id) {
        CourseVO vo = courseMapper.selectCourseVOById(id);
        if (vo == null) {
            throw new BusinessException("课堂不存在");
        }

        // 设置状态名称
        if (vo.getStatus() != null) {
            vo.setStatusName(vo.getStatus() == 1 ? "已发布" : "下架");
        }

        // 设置是否推荐名称
        if (vo.getIsRecommended() != null) {
            vo.setIsRecommendedName(vo.getIsRecommended() == 1 ? "是" : "否");
        }

        // 设置课堂类型名称
        if (vo.getCourseType() != null) {
            vo.setCourseTypeName(getCourseTypeName(vo.getCourseType()));
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(CourseDTO dto) {
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setViewCount(0);
        course.setIsRecommended(0);
        course.setStatus(0); // 默认下架
        if (course.getSortOrder() == null) {
            course.setSortOrder(0);
        }
        course.setCreateBy(SecurityUtils.getUsername());
        courseMapper.insert(course);

        log.info("新增课堂成功，课堂ID：{}，课堂标题：{}", course.getId(), course.getCourseTitle());
        return course.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(CourseDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("课堂ID不能为空");
        }

        Course oldCourse = getById(dto.getId());

        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setUpdateBy(SecurityUtils.getUsername());
        courseMapper.updateById(course);

        log.info("修改课堂成功，课堂ID：{}，课堂标题：{}", course.getId(), course.getCourseTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Course course = getById(id);

        // 逻辑删除
        course.setDelFlag(1);
        course.setUpdateBy(SecurityUtils.getUsername());
        courseMapper.updateById(course);

        log.info("删除课堂成功，课堂ID：{}，课堂标题：{}", id, course.getCourseTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void publish(Long id) {
        Course course = getById(id);

        if (course.getStatus() == 1) {
            throw new BusinessException("课堂已发布，无需重复发布");
        }

        course.setStatus(1);
        course.setPublishTime(LocalDateTime.now());
        course.setUpdateBy(SecurityUtils.getUsername());
        courseMapper.updateById(course);

        log.info("发布课堂成功，课堂ID：{}，课堂标题：{}", id, course.getCourseTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unpublish(Long id) {
        Course course = getById(id);

        if (course.getStatus() == 0) {
            throw new BusinessException("课堂已下架，无需重复下架");
        }

        course.setStatus(0);
        course.setUpdateBy(SecurityUtils.getUsername());
        courseMapper.updateById(course);

        log.info("取消发布课堂成功，课堂ID：{}，课堂标题：{}", id, course.getCourseTitle());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setRecommended(Long id, Integer isRecommended) {
        Course course = getById(id);

        course.setIsRecommended(isRecommended);
        course.setUpdateBy(SecurityUtils.getUsername());
        courseMapper.updateById(course);

        log.info("设置课堂推荐成功，课堂ID：{}，是否推荐：{}", id, isRecommended == 1 ? "是" : "否");
    }

    /**
     * 根据ID获取课堂实体
     */
    private Course getById(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null || course.getDelFlag() == 1) {
            throw new BusinessException("课堂不存在");
        }
        return course;
    }

    /**
     * 获取课堂类型名称
     */
    private String getCourseTypeName(String courseType) {
        if (courseType == null) {
            return "未知";
        }
        switch (courseType) {
            case "video":
                return "视频";
            case "document":
                return "文档";
            case "article":
                return "图文";
            default:
                return courseType;
        }
    }
}
