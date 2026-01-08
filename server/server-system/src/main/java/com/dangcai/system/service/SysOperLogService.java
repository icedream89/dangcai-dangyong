package com.dangcai.system.service;

import com.dangcai.system.domain.SysOperLog;

/**
 * 操作日志服务接口
 *
 * @author dangcai
 * @since 2026-01-08
 */
public interface SysOperLogService {

    /**
     * 保存操作日志
     *
     * @param operLog 操作日志
     */
    void save(SysOperLog operLog);

    /**
     * 异步保存操作日志
     *
     * @param operLog 操作日志
     */
    void saveAsync(SysOperLog operLog);
}
