package com.dangcai.enterprise.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.enterprise.domain.Enterprise;
import com.dangcai.enterprise.dto.EnterpriseDTO;
import com.dangcai.enterprise.dto.EnterpriseQueryDTO;
import com.dangcai.enterprise.vo.EnterpriseVO;

/**
 * 企业服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface EnterpriseService {

    /**
     * 分页查询企业列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<EnterpriseVO> page(EnterpriseQueryDTO queryDTO);

    /**
     * 根据ID查询企业详情
     *
     * @param id 企业ID
     * @return 企业VO
     */
    EnterpriseVO getVOById(Long id);

    /**
     * 根据ID查询企业实体
     *
     * @param id 企业ID
     * @return 企业实体
     */
    Enterprise getById(Long id);

    /**
     * 新增企业
     *
     * @param dto 企业DTO
     * @return 企业ID
     */
    Long add(EnterpriseDTO dto);

    /**
     * 修改企业
     *
     * @param dto 企业DTO
     */
    void update(EnterpriseDTO dto);

    /**
     * 删除企业
     *
     * @param id 企业ID
     */
    void delete(Long id);

    /**
     * 审核企业
     *
     * @param id       企业ID
     * @param status   审核状态：1-通过 2-不通过
     * @param remark   审核备注
     */
    void audit(Long id, Integer status, String remark);

    /**
     * 修改企业状态
     *
     * @param id     企业ID
     * @param status 状态：0-待审核 1-正常 2-禁用
     */
    void updateStatus(Long id, Integer status);

    /**
     * 设置推荐
     *
     * @param id            企业ID
     * @param isRecommended 是否推荐：0-否 1-是
     */
    void setRecommended(Long id, Integer isRecommended);

    /**
     * 增加浏览次数
     *
     * @param id 企业ID
     */
    void increaseViewCount(Long id);
}
