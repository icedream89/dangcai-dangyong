package com.dangcai.system.service;

import com.dangcai.system.dto.MenuDTO;
import com.dangcai.system.dto.MenuTreeVO;
import com.dangcai.system.dto.MenuVO;

import java.util.List;

/**
 * 菜单服务接口
 *
 * @author dangcai
 * @since 2026-01-07
 */
public interface MenuService {

    /**
     * 查询菜单树
     *
     * @return 菜单树列表
     */
    List<MenuVO> tree();

    /**
     * 查询菜单树（用于角色权限分配）
     *
     * @return 菜单树列表
     */
    List<MenuTreeVO> treeForPermission();

    /**
     * 根据角色ID查询菜单列表
     *
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> getMenuIdsByRoleId(Long roleId);

    /**
     * 根据ID查询菜单详情
     *
     * @param id 菜单ID
     * @return 菜单详情
     */
    MenuVO getById(Long id);

    /**
     * 新增菜单
     *
     * @param menuDTO 菜单信息
     * @return 菜单ID
     */
    Long add(MenuDTO menuDTO);

    /**
     * 修改菜单
     *
     * @param menuDTO 菜单信息
     */
    void update(MenuDTO menuDTO);

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     */
    void delete(Long id);
}
