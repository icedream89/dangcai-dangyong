package com.dangcai.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.system.dto.RoleDTO;
import com.dangcai.system.dto.RoleQueryDTO;
import com.dangcai.system.dto.RoleVO;
import com.dangcai.system.domain.Role;

import java.util.List;

/**
 * 角色服务接口
 *
 * @author dangcai
 * @since 2026-01-07
 */
public interface RoleService {

    /**
     * 分页查询角色列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<RoleVO> page(RoleQueryDTO queryDTO);

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<RoleVO> listAll();

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情
     */
    RoleVO getById(Long id);

    /**
     * 新增角色
     *
     * @param roleDTO 角色信息
     * @return 角色ID
     */
    Long add(RoleDTO roleDTO);

    /**
     * 修改角色
     *
     * @param roleDTO 角色信息
     */
    void update(RoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void delete(Long id);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleVO> listByUserId(Long userId);
}
