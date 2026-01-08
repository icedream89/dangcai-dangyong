package com.dangcai.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dangcai.system.domain.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色菜单关联Mapper
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 批量插入角色菜单关联
     *
     * @param roleMenus 角色菜单关联列表
     * @return 插入条数
     */
    int insertBatch(@Param("list") List<RoleMenu> roleMenus);

    /**
     * 根据角色ID删除菜单关联
     *
     * @param roleId 角色ID
     * @return 删除条数
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据菜单ID删除角色关联
     *
     * @param menuId 菜单ID
     * @return 删除条数
     */
    int deleteByMenuId(@Param("menuId") Long menuId);

    /**
     * 根据角色ID查询菜单ID列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> selectMenuIdsByRoleId(@Param("roleId") Long roleId);
}
