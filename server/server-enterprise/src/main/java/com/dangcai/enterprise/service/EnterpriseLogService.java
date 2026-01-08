package com.dangcai.enterprise.service;

import com.dangcai.enterprise.domain.EnterpriseLog;

import java.util.List;

/**
 * 企业变更记录服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface EnterpriseLogService {

    /**
     * 新增变更记录
     *
     * @param log 变更记录
     * @return 记录ID
     */
    Long add(EnterpriseLog log);

    /**
     * 查询企业的变更记录列表
     *
     * @param enterpriseId 企业ID
     * @return 变更记录列表
     */
    List<EnterpriseLog> listByEnterpriseId(Long enterpriseId);
}
