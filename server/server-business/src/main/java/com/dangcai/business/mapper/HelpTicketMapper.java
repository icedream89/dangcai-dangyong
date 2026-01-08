package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.HelpTicket;
import com.dangcai.business.vo.HelpTicketVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 求助工单Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface HelpTicketMapper extends BaseMapper<HelpTicket> {

    /**
     * 查询工单列表（含企业名称、处理人名称）
     *
     * @param ticketNo   工单编号
     * @param enterpriseId 企业ID
     * @param userId     用户ID
     * @param helpType   求助类型
     * @param priority   优先级
     * @param status     状态
     * @param title      标题（模糊查询）
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 工单VO列表
     */
    List<HelpTicketVO> selectHelpTicketList(
            @Param("ticketNo") String ticketNo,
            @Param("enterpriseId") Long enterpriseId,
            @Param("userId") Long userId,
            @Param("helpType") Integer helpType,
            @Param("priority") Integer priority,
            @Param("status") Integer status,
            @Param("title") String title,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据ID查询工单VO（含企业名称、处理人名称）
     *
     * @param id 工单ID
     * @return 工单VO
     */
    HelpTicketVO selectHelpTicketVOById(@Param("id") Long id);

    /**
     * 增加工单浏览次数
     *
     * @param id 工单ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);
}
