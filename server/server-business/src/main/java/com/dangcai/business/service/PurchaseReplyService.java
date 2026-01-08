package com.dangcai.business.service;

import com.dangcai.business.dto.PurchaseReplyDTO;
import com.dangcai.business.vo.PurchaseReplyVO;

import java.util.List;

/**
 * 采购需求回复Service接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface PurchaseReplyService {

    /**
     * 根据需求ID查询回复列表
     *
     * @param requirementId 需求ID
     * @return 回复VO列表
     */
    List<PurchaseReplyVO> listByRequirementId(Long requirementId);

    /**
     * 企业回复需求
     *
     * @param dto 回复DTO
     * @return 回复ID
     */
    Long add(PurchaseReplyDTO dto);

    /**
     * 修改回复
     *
     * @param dto 回复DTO
     */
    void update(PurchaseReplyDTO dto);

    /**
     * 删除回复
     *
     * @param id 回复ID
     */
    void delete(Long id);

    /**
     * 确认回复
     *
     * @param id 回复ID
     */
    void confirmReply(Long id);

    /**
     * 完成交易
     *
     * @param id 回复ID
     */
    void completeDeal(Long id);
}
