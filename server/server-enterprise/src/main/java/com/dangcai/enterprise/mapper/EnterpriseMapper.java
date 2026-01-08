package com.dangcai.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.enterprise.domain.Enterprise;
import com.dangcai.enterprise.vo.EnterpriseVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface EnterpriseMapper extends BaseMapper<Enterprise> {

    /**
     * 查询企业列表（含企业名称模糊查询）
     *
     * @param enterpriseName 企业名称
     * @param unifiedCode    统一社会信用代码
     * @param industry       所属行业
     * @param enterpriseType 企业类型
     * @param scale          企业规模
     * @param status         状态
     * @param isRecommended  是否推荐
     * @return 企业VO列表
     */
    List<EnterpriseVO> selectEnterpriseList(
            @Param("enterpriseName") String enterpriseName,
            @Param("unifiedCode") String unifiedCode,
            @Param("industry") String industry,
            @Param("enterpriseType") String enterpriseType,
            @Param("scale") String scale,
            @Param("status") Integer status,
            @Param("isRecommended") Integer isRecommended
    );

    /**
     * 根据ID查询企业VO
     *
     * @param id 企业ID
     * @return 企业VO
     */
    EnterpriseVO selectEnterpriseVOById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 企业ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);
}
