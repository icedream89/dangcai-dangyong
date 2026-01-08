package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.PurchaseRequirement;
import com.dangcai.business.vo.PurchaseRequirementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购需求Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface PurchaseRequirementMapper extends BaseMapper<PurchaseRequirement> {

    /**
     * 查询采购需求列表（含企业名称）
     *
     * @param requirementNo 需求编号
     * @param enterpriseId  企业ID
     * @param userId        用户ID
     * @param category      采购类别
     * @param status        状态
     * @param title         标题（模糊查询）
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return 采购需求VO列表
     */
    List<PurchaseRequirementVO> selectPurchaseRequirementList(
            @Param("requirementNo") String requirementNo,
            @Param("enterpriseId") Long enterpriseId,
            @Param("userId") Long userId,
            @Param("category") String category,
            @Param("status") Integer status,
            @Param("title") String title,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据ID查询采购需求VO（含企业名称）
     *
     * @param id 需求ID
     * @return 采购需求VO
     */
    PurchaseRequirementVO selectPurchaseRequirementVOById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 需求ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);

    /**
     * 增加回复数量
     *
     * @param id 需求ID
     * @return 影响行数
     */
    int increaseReplyCount(@Param("id") Long id);

    /**
     * 减少回复数量
     *
     * @param id 需求ID
     * @return 影响行数
     */
    int decreaseReplyCount(@Param("id") Long id);
}
