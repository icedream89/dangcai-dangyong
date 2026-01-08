package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.PurchaseMatch;
import com.dangcai.business.vo.PurchaseMatchVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 需求匹配Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface PurchaseMatchMapper extends BaseMapper<PurchaseMatch> {

    /**
     * 根据需求ID查询匹配列表
     *
     * @param requirementId 需求ID
     * @return 匹配VO列表
     */
    List<PurchaseMatchVO> selectMatchListByRequirementId(@Param("requirementId") Long requirementId);

    /**
     * 根据需求ID和企业ID查询匹配记录
     *
     * @param requirementId 需求ID
     * @param enterpriseId  企业ID
     * @return 匹配记录
     */
    PurchaseMatch selectByRequirementIdAndEnterpriseId(
            @Param("requirementId") Long requirementId,
            @Param("enterpriseId") Long enterpriseId
    );

    /**
     * 查询需求匹配的企业ID列表
     *
     * @param requirementId 需求ID
     * @return 企业ID列表
     */
    List<Long> selectMatchedEnterpriseIds(@Param("requirementId") Long requirementId);
}
