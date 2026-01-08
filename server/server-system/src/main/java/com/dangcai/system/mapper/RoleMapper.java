package com.dangcai.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.system.domain.Role;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色Mapper
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
}
