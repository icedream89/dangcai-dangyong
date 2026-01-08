package com.dangcai.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Course;
import com.dangcai.business.dto.CourseDTO;
import com.dangcai.business.vo.CourseVO;

/**
 * 企业课堂服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface CourseService {

    /**
     * 分页查询课堂列表
     *
     * @param courseTitle   课堂标题
     * @param courseType    课堂类型
     * @param category      分类
     * @param isRecommended 是否推荐
     * @param status        状态
     * @param current       当前页
     * @param size          每页条数
     * @return 分页结果
     */
    Page<CourseVO> page(String courseTitle, String courseType,
                        String category, Integer isRecommended,
                        Integer status, Integer current, Integer size);

    /**
     * 根据ID查询课堂详情
     *
     * @param id 课堂ID
     * @return 课堂VO
     */
    CourseVO getVOById(Long id);

    /**
     * 新增课堂
     *
     * @param dto 课堂DTO
     * @return 课堂ID
     */
    Long add(CourseDTO dto);

    /**
     * 修改课堂
     *
     * @param dto 课堂DTO
     */
    void update(CourseDTO dto);

    /**
     * 删除课堂
     *
     * @param id 课堂ID
     */
    void delete(Long id);

    /**
     * 发布课堂
     *
     * @param id 课堂ID
     */
    void publish(Long id);

    /**
     * 取消发布
     *
     * @param id 课堂ID
     */
    void unpublish(Long id);

    /**
     * 设置推荐
     *
     * @param id            课堂ID
     * @param isRecommended 是否推荐
     */
    void setRecommended(Long id, Integer isRecommended);
}
