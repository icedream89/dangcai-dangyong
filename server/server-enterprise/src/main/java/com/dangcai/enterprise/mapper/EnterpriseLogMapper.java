package com.dangcai.enterprise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.enterprise.domain.EnterpriseLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 企业变更记录Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface EnterpriseLogMapper extends BaseMapper<EnterpriseLog> {

    /**
     * 查询企业的变更记录列表
     *
     * @param enterpriseId 企业ID
     * @return 变更记录列表
     */
    List<EnterpriseLog> selectByEnterpriseId(@Param("enterpriseId") Long enterpriseId);
}
