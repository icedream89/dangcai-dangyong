package com.dangcai.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.business.domain.Policy;
import com.dangcai.business.vo.PolicyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 政策Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface PolicyMapper extends BaseMapper<Policy> {

    /**
     * 分页查询政策列表
     *
     * @param policyTitle 政策标题
     * @param policyType  政策类型
     * @param status      状态
     * @param isHot       是否热门
     * @param isTop       是否置顶
     * @return 政策VO列表
     */
    List<PolicyVO> selectPolicyList(
            @Param("policyTitle") String policyTitle,
            @Param("policyType") String policyType,
            @Param("status") Integer status,
            @Param("isHot") Integer isHot,
            @Param("isTop") Integer isTop
    );

    /**
     * 根据ID查询政策VO
     *
     * @param id 政策ID
     * @return 政策VO
     */
    PolicyVO selectPolicyVOById(@Param("id") Long id);

    /**
     * 增加浏览次数
     *
     * @param id 政策ID
     * @return 影响行数
     */
    int increaseViewCount(@Param("id") Long id);
}
