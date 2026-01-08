package com.dangcai.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.message.domain.BizMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 消息Mapper
 *
 * @author dangcai
 * @since 2026-01-08
 */
@Mapper
public interface MessageMapper extends BaseMapper<BizMessage> {
}
