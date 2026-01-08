package com.dangcai.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.business.domain.HelpFlow;
import com.dangcai.business.domain.HelpTicket;
import com.dangcai.business.dto.HandleDTO;
import com.dangcai.business.dto.HelpTicketDTO;
import com.dangcai.business.dto.HelpTicketQueryDTO;
import com.dangcai.business.dto.SatisfactionDTO;
import com.dangcai.business.mapper.HelpTicketMapper;
import com.dangcai.business.service.HelpFlowService;
import com.dangcai.business.service.HelpTicketService;
import com.dangcai.business.vo.HelpTicketVO;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 求助工单Service实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelpTicketServiceImpl implements HelpTicketService {

    private final HelpTicketMapper helpTicketMapper;
    private final HelpFlowService helpFlowService;

    @Override
    public Page<HelpTicketVO> page(HelpTicketQueryDTO queryDTO) {
        Page<HelpTicketVO> page = new Page<>(queryDTO.getCurrent(), queryDTO.getSize());
        page.setRecords(helpTicketMapper.selectHelpTicketList(
                queryDTO.getTicketNo(),
                queryDTO.getEnterpriseId(),
                queryDTO.getUserId(),
                queryDTO.getHelpType(),
                queryDTO.getPriority(),
                queryDTO.getStatus(),
                queryDTO.getTitle(),
                queryDTO.getStartTime(),
                queryDTO.getEndTime()
        ));
        return page;
    }

    @Override
    public HelpTicketVO getVOById(Long id) {
        HelpTicketVO vo = helpTicketMapper.selectHelpTicketVOById(id);
        if (vo == null) {
            throw new BusinessException("工单不存在");
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(HelpTicketDTO dto) {
        HelpTicket ticket = new HelpTicket();
        BeanUtils.copyProperties(dto, ticket);

        // 生成工单编号：HT + yyyyMMdd + 6位序号
        ticket.setTicketNo(generateTicketNo());

        // 默认状态为待处理
        ticket.setStatus(1);

        ticket.setCreateBy(SecurityUtils.getUsername());
        helpTicketMapper.insert(ticket);

        // 记录流转记录
        recordFlow(ticket.getId(), null, 1, "提交工单", null);

        log.info("新增求助工单成功，工单ID：{}，工单编号：{}", ticket.getId(), ticket.getTicketNo());
        return ticket.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(HelpTicketDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("工单ID不能为空");
        }

        HelpTicket oldTicket = getById(dto.getId());

        // 只能修改待处理状态的工单
        if (oldTicket.getStatus() != 1) {
            throw new BusinessException("只能修改待处理状态的工单");
        }

        HelpTicket ticket = new HelpTicket();
        BeanUtils.copyProperties(dto, ticket);
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        log.info("修改求助工单成功，工单ID：{}，工单编号：{}", ticket.getId(), oldTicket.getTicketNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        HelpTicket ticket = getById(id);

        // 只能删除待处理状态的工单
        if (ticket.getStatus() != 1) {
            throw new BusinessException("只能删除待处理状态的工单");
        }

        // 逻辑删除
        ticket.setDelFlag(1);
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        log.info("删除求助工单成功，工单ID：{}，工单编号：{}", id, ticket.getTicketNo());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignHandler(Long ticketId, Long handlerId) {
        HelpTicket ticket = getById(ticketId);

        if (ticket.getStatus() != 1) {
            throw new BusinessException("只能分配待处理状态的工单");
        }

        ticket.setHandlerId(handlerId);
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        // 记录流转记录
        recordFlow(ticketId, 1, 1, "分配处理人", "处理人ID：" + handlerId);

        log.info("分配工单处理人成功，工单ID：{}，处理人ID：{}", ticketId, handlerId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startHandle(Long ticketId) {
        HelpTicket ticket = getById(ticketId);

        if (ticket.getStatus() != 1) {
            throw new BusinessException("只能开始处理待处理状态的工单");
        }

        ticket.setStatus(2); // 处理中
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        // 记录流转记录
        recordFlow(ticketId, 1, 2, "开始处理", null);

        log.info("开始处理工单成功，工单ID：{}", ticketId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeHandle(HandleDTO dto) {
        HelpTicket ticket = getById(dto.getTicketId());

        if (ticket.getStatus() != 2) {
            throw new BusinessException("只能完成处理中状态的工单");
        }

        ticket.setHandleResult(dto.getHandleResult());
        ticket.setHandleTime(LocalDateTime.now());
        ticket.setStatus(3); // 已完成
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        // 记录流转记录
        recordFlow(dto.getTicketId(), 2, 3, "完成处理", "处理结果：" + dto.getHandleResult());

        log.info("完成处理工单成功，工单ID：{}", dto.getTicketId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void satisfaction(SatisfactionDTO dto) {
        HelpTicket ticket = getById(dto.getTicketId());

        if (ticket.getStatus() != 3) {
            throw new BusinessException("只能评价已完成的工单");
        }

        ticket.setSatisfaction(dto.getSatisfaction());
        ticket.setFeedback(dto.getFeedback());
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        // 记录流转记录
        recordFlow(dto.getTicketId(), 3, 3, "满意度评价",
                "评分：" + dto.getSatisfaction() + "分，反馈：" + dto.getFeedback());

        log.info("工单满意度评价成功，工单ID：{}，评分：{}", dto.getTicketId(), dto.getSatisfaction());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeTicket(Long ticketId) {
        HelpTicket ticket = getById(ticketId);

        if (ticket.getStatus() != 3) {
            throw new BusinessException("只能关闭已完成的工单");
        }

        ticket.setStatus(4); // 已关闭
        ticket.setUpdateBy(SecurityUtils.getUsername());
        helpTicketMapper.updateById(ticket);

        // 记录流转记录
        recordFlow(ticketId, 3, 4, "关闭工单", null);

        log.info("关闭工单成功，工单ID：{}", ticketId);
    }

    /**
     * 根据ID获取工单实体
     */
    private HelpTicket getById(Long id) {
        HelpTicket ticket = helpTicketMapper.selectById(id);
        if (ticket == null || ticket.getDelFlag() == 1) {
            throw new BusinessException("工单不存在");
        }
        return ticket;
    }

    /**
     * 生成工单编号
     */
    private String generateTicketNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomStr = String.format("%06d", new Random().nextInt(1000000));
        return "HT" + dateStr + randomStr;
    }

    /**
     * 记录流转记录
     */
    private void recordFlow(Long ticketId, Integer fromStatus, Integer toStatus, String operation, String remark) {
        HelpFlow flow = new HelpFlow();
        flow.setTicketId(ticketId);
        flow.setFromStatus(fromStatus);
        flow.setToStatus(toStatus);
        flow.setOperatorId(SecurityUtils.getUserId());
        flow.setOperatorName(SecurityUtils.getUsername());
        flow.setOperation(operation);
        flow.setRemark(remark);
        helpFlowService.add(flow);
    }
}
