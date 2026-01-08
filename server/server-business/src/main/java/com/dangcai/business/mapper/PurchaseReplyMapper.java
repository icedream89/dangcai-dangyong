package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.PurchaseReply;
import com.dangcai.business.vo.PurchaseReplyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采购需求回复Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface PurchaseReplyMapper extends BaseMapper<PurchaseReply> {

    /**
     * 根据需求ID查询回复列表
     *
     * @param requirementId 需求ID
     * @return 回复VO列表
     */
    List<PurchaseReplyVO> selectReplyListByRequirementId(@Param("requirementId") Long requirementId);

    /**
     * 根据需求ID和企业ID查询回复
     *
     * @param requirementId 需求ID
     * @param enterpriseId  企业ID
     * @return 回复VO
     */
    PurchaseReplyVO selectByRequirementIdAndEnterpriseId(
            @Param("requirementId") Long requirementId,
            @Param("enterpriseId") Long enterpriseId
    );
}
