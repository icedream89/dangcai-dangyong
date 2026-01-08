package com.dangcai.business.service;

import com.dangcai.business.dto.PurchaseMatchDTO;
import com.dangcai.business.domain.PurchaseMatch;
import com.dangcai.business.vo.PurchaseMatchVO;

import java.util.List;

/**
 * 需求匹配Service接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface PurchaseMatchService {

    /**
     * 根据需求ID查询匹配列表
     *
     * @param requirementId 需求ID
     * @return 匹配VO列表
     */
    List<PurchaseMatchVO> listByRequirementId(Long requirementId);

    /**
     * 添加匹配记录
     *
     * @param match 匹配记录
     * @return 匹配ID
     */
    Long add(PurchaseMatch match);

    /**
     * 批量匹配企业
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
     * 根据需求ID和企业ID查询匹配记录
     *
     * @param requirementId 需求ID
     * @param enterpriseId  企业ID
     * @return 匹配记录
     */
    PurchaseMatch getByRequirementIdAndEnterpriseId(Long requirementId, Long enterpriseId);
}
