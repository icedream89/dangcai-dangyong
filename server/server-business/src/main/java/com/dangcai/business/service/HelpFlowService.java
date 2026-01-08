package com.dangcai.business.service;

import com.dangcai.business.domain.HelpFlow;
import com.dangcai.business.vo.HelpFlowVO;

import java.util.List;

/**
 * 工单流转记录Service接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface HelpFlowService {

    /**
     * 根据工单ID查询流转记录列表
     *
     * @param ticketId 工单ID
     * @return 流转记录VO列表
     */
    List<HelpFlowVO> listByTicketId(Long ticketId);

    /**
     * 添加流转记录
     *
     * @param flow 流转记录
     * @return 流转记录ID
     */
    Long add(HelpFlow flow);
}
