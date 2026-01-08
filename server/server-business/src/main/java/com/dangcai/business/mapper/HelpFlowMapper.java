package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.HelpFlow;
import com.dangcai.business.vo.HelpFlowVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工单流转记录Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface HelpFlowMapper extends BaseMapper<HelpFlow> {

    /**
     * 根据工单ID查询流转记录列表
     *
     * @param ticketId 工单ID
     * @return 流转记录VO列表
     */
    List<HelpFlowVO> selectFlowListByTicketId(@Param("ticketId") Long ticketId);
}
