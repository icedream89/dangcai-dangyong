package com.dangcai.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.system.domain.SysOperLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface SysOperLogMapper extends BaseMapper<SysOperLog> {
}
