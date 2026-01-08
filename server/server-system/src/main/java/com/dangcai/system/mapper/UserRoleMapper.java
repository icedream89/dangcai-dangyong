package com.dangcai.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.system.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关联Mapper
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 批量插入用户角色关联
     *
     * @param userRoles 用户角色关联列表
     * @return 插入条数
     */
    int insertBatch(@Param("list") List<UserRole> userRoles);

    /**
     * 根据用户ID删除角色关联
     *
     * @param userId 用户ID
     * @return 删除条数
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除用户关联
     *
     * @param roleId 角色ID
     * @return 删除条数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询角色ID列表
     *
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);
}
