package com.dangcai.business.service.impl;

import com.dangcai.business.domain.HelpFlow;
import com.dangcai.business.mapper.HelpFlowMapper;
import com.dangcai.business.service.HelpFlowService;
import com.dangcai.business.vo.HelpFlowVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工单流转记录Service实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelpFlowServiceImpl implements HelpFlowService {

    private final HelpFlowMapper helpFlowMapper;

    @Override
    public List<HelpFlowVO> listByTicketId(Long ticketId) {
        return helpFlowMapper.selectFlowListByTicketId(ticketId);
    }

    @Override
    public Long add(HelpFlow flow) {
        flow.setCreateTime(LocalDateTime.now());
        helpFlowMapper.insert(flow);
        log.info("新增工单流转记录成功，工单ID：{}，操作：{}", flow.getTicketId(), flow.getOperation());
        return flow.getId();
    }
}
