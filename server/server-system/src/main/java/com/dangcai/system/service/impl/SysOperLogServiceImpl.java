package com.dangcai.system.service.impl;

import com.dangcai.system.domain.SysOperLog;
import com.dangcai.system.mapper.SysOperLogMapper;
import com.dangcai.system.service.SysOperLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 操作日志服务实现
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Slf4j
@Service
public class SysOperLogServiceImpl implements SysOperLogService {

    @Resource
    private SysOperLogMapper operLogMapper;

    @Override
    public void save(SysOperLog operLog) {
        operLogMapper.insert(operLog);
    }

    @Override
    @Async
    public void saveAsync(SysOperLog operLog) {
        try {
            operLogMapper.insert(operLog);
        } catch (Exception e) {
            log.error("操作日志保存失败：{}", e.getMessage());
        }
    }
}
