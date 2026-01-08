package com.dangcai.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.dto.HandleDTO;
import com.dangcai.business.dto.HelpTicketDTO;
import com.dangcai.business.dto.HelpTicketQueryDTO;
import com.dangcai.business.dto.SatisfactionDTO;
import com.dangcai.business.vo.HelpTicketVO;

/**
 * 求助工单Service接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface HelpTicketService {

    /**
     * 分页查询工单列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<HelpTicketVO> page(HelpTicketQueryDTO queryDTO);

    /**
     * 根据ID查询工单详情
     *
     * @param id 工单ID
     * @return 工单VO
     */
    HelpTicketVO getVOById(Long id);

    /**
     * 新增工单
     *
     * @param dto 工单DTO
     * @return 工单ID
     */
    Long add(HelpTicketDTO dto);

    /**
     * 修改工单
     *
     * @param dto 工单DTO
     */
    void update(HelpTicketDTO dto);

    /**
     * 删除工单
     *
     * @param id 工单ID
     */
    void delete(Long id);

    /**
     * 分配处理人
     *
     * @param ticketId  工单ID
     * @param handlerId 处理人ID
     */
    void assignHandler(Long ticketId, Long handlerId);

    /**
     * 开始处理工单
     *
     * @param ticketId 工单ID
     */
    void startHandle(Long ticketId);

    /**
     * 完成处理工单
     *
     * @param dto 处理DTO
     */
    void completeHandle(HandleDTO dto);

    /**
     * 满意度评价
     *
     * @param dto 评价DTO
     */
    void satisfaction(SatisfactionDTO dto);

    /**
     * 关闭工单
     *
     * @param ticketId 工单ID
     */
    void closeTicket(Long ticketId);
}
