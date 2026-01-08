package com.dangcai.enterprise.service.impl;

import com.dangcai.enterprise.domain.EnterpriseLog;
import com.dangcai.enterprise.mapper.EnterpriseLogMapper;
import com.dangcai.enterprise.service.EnterpriseLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业变更记录服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseLogServiceImpl implements EnterpriseLogService {

    private final EnterpriseLogMapper enterpriseLogMapper;

    @Override
    public Long add(EnterpriseLog log) {
        enterpriseLogMapper.insert(log);
        return log.getId();
    }

    @Override
    public List<EnterpriseLog> listByEnterpriseId(Long enterpriseId) {
        return enterpriseLogMapper.selectByEnterpriseId(enterpriseId);
    }
}
