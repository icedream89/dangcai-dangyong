package com.dangcai.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.Policy;
import com.dangcai.business.dto.PolicyDTO;
import com.dangcai.business.vo.PolicyVO;

/**
 * 政策服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface PolicyService {

    /**
     * 分页查询政策列表
     *
     * @param policyTitle 政策标题
     * @param policyType  政策类型
     * @param status      状态
     * @param isHot       是否热门
     * @param isTop       是否置顶
     * @param current     当前页
     * @param size        每页条数
     * @return 分页结果
     */
    Page<PolicyVO> page(String policyTitle, String policyType,
                        Integer status, Integer isHot, Integer isTop,
                        Integer current, Integer size);

    /**
     * 根据ID查询政策详情
     *
     * @param id 政策ID
     * @return 政策VO
     */
    PolicyVO getVOById(Long id);

    /**
     * 新增政策
     *
     * @param dto 政策DTO
     * @return 政策ID
     */
    Long add(PolicyDTO dto);

    /**
     * 修改政策
     *
     * @param dto 政策DTO
     */
    void update(PolicyDTO dto);

    /**
     * 删除政策
     *
     * @param id 政策ID
     */
    void delete(Long id);

    /**
     * 发布政策
     *
     * @param id 政策ID
     */
    void publish(Long id);

    /**
     * 取消发布
     *
     * @param id 政策ID
     */
    void unpublish(Long id);

    /**
     * 设置热门
     *
     * @param id    政策ID
     * @param isHot 是否热门
     */
    void setHot(Long id, Integer isHot);

    /**
     * 设置置顶
     *
     * @param id    政策ID
     * @param isTop 是否置顶
     */
    void setTop(Long id, Integer isTop);
}
