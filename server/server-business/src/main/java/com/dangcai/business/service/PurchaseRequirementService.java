package com.dangcai.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.PurchaseMatchDTO;
import com.dangcai.business.dto.PurchaseRequirementDTO;
import com.dangcai.business.dto.PurchaseRequirementQueryDTO;
import com.dangcai.business.vo.PurchaseMatchVO;
import com.dangcai.business.vo.PurchaseRequirementVO;

import java.util.List;

/**
 * 采购需求Service接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface PurchaseRequirementService {

    /**
     * 分页查询采购需求列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<PurchaseRequirementVO> page(PurchaseRequirementQueryDTO queryDTO);

    /**
     * 根据ID查询采购需求详情
     *
     * @param id 需求ID
     * @return 采购需求VO
     */
    PurchaseRequirementVO getVOById(Long id);

    /**
     * 新增采购需求
     *
     * @param dto 采购需求DTO
     * @return 需求ID
     */
    Long add(PurchaseRequirementDTO dto);

    /**
     * 修改采购需求
     *
     * @param dto 采购需求DTO
     */
    void update(PurchaseRequirementDTO dto);

    /**
     * 删除采购需求
     *
     * @param id 需求ID
     */
    void delete(Long id);

    /**
     * 匹配供应商企业
     *
     * @param dto 匹配DTO
     */
    void matchEnterprises(PurchaseMatchDTO dto);

    /**
     * 取消匹配
     *
     * @param requirementId 需求ID
     * @param enterpriseId  企业ID
     * @param cancelReason  取消原因
     */
    void cancelMatch(Long requirementId, Long enterpriseId, String cancelReason);

    /**
     * 查询需求已匹配的企业列表
     *
     * @param requirementId 需求ID
     * @return 匹配VO列表
     */
    List<PurchaseMatchVO> listMatchedEnterprises(Long requirementId);

    /**
     * 增加浏览次数
     *
     * @param id 需求ID
     */
    void increaseViewCount(Long id);

    /**
     * 更新需求状态
     *
     * @param id     需求ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);
}
