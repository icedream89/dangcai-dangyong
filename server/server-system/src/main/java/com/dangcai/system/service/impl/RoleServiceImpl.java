package com.dangcai.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.system.dto.RoleDTO;
import com.dangcai.system.dto.RoleQueryDTO;
import com.dangcai.system.dto.RoleVO;
import com.dangcai.system.domain.Role;
import com.dangcai.system.mapper.RoleMapper;
import com.dangcai.system.mapper.UserMapper;
import com.dangcai.system.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private com.dangcai.system.mapper.RoleMenuMapper roleMenuMapper;

    @Override
    public Page<RoleVO> page(RoleQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getRoleName())) {
            wrapper.like(Role::getRoleName, queryDTO.getRoleName());
        }
        if (StringUtils.hasText(queryDTO.getRoleCode())) {
            wrapper.eq(Role::getRoleCode, queryDTO.getRoleCode());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Role::getStatus, queryDTO.getStatus());
        }

        wrapper.eq(Role::getDelFlag, 0);
        wrapper.orderByAsc(Role::getSortOrder);
        wrapper.orderByDesc(Role::getCreateTime);

        // 分页查询
        Page<Role> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        Page<Role> rolePage = roleMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<RoleVO> voPage = new Page<>(rolePage.getCurrent(), rolePage.getSize(), rolePage.getTotal());
        List<RoleVO> voList = rolePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public List<RoleVO> listAll() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getDelFlag, 0);
        wrapper.eq(Role::getStatus, 1);
        wrapper.orderByAsc(Role::getSortOrder);

        List<Role> roles = roleMapper.selectList(wrapper);
        return roles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleVO getById(Long id) {
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        return convertToVO(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(RoleDTO roleDTO) {
        // 1. 检查角色编码是否存在
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleDTO.getRoleCode());
        wrapper.eq(Role::getDelFlag, 0);
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已存在");
        }

        // 2. 创建角色
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);

        // 设置默认值
        if (role.getStatus() == null) {
            role.setStatus(1); // 默认正常
        }
        if (role.getSortOrder() == null) {
            role.setSortOrder(0); // 默认排序为0
        }

        roleMapper.insert(role);

        // 3. 分配菜单权限
        if (roleDTO.getMenuIds() != null && roleDTO.getMenuIds().length > 0) {
            insertRoleMenus(role.getId(), roleDTO.getMenuIds());
        }

        log.info("新增角色成功：{}", role.getRoleName());
        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO roleDTO) {
        // 1. 检查角色是否存在
        Role role = roleMapper.selectById(roleDTO.getId());
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 2. 检查角色编码是否被其他角色使用
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleCode, roleDTO.getRoleCode());
        wrapper.eq(Role::getDelFlag, 0);
        wrapper.ne(Role::getId, roleDTO.getId());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new BusinessException("角色编码已被其他角色使用");
        }

        // 3. 更新角色信息
        BeanUtils.copyProperties(roleDTO, role, "id");
        roleMapper.updateById(role);

        // 4. 更新菜单权限
        if (roleDTO.getMenuIds() != null) {
            // 删除旧的菜单关联
            roleMenuMapper.deleteByRoleId(role.getId());

            // 插入新的菜单关联
            insertRoleMenus(role.getId(), roleDTO.getMenuIds());
        }

        log.info("修改角色成功：{}", role.getRoleName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 1. 检查角色是否存在
        Role role = roleMapper.selectById(id);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 2. 检查是否有用户使用该角色
        Long count = userMapper.selectRoleCodesByUserId(id).stream().count();
        if (count > 0) {
            throw new BusinessException("该角色已被用户使用，无法删除");
        }

        // 3. 逻辑删除角色
        role.setDelFlag(1);
        roleMapper.updateById(role);

        log.info("删除角色成功：{}", role.getRoleName());
    }

    @Override
    public List<RoleVO> listByUserId(Long userId) {
        List<String> roleCodes = userMapper.selectRoleCodesByUserId(userId);
        if (roleCodes == null || roleCodes.isEmpty()) {
            return List.of();
        }

        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Role::getRoleCode, roleCodes);
        wrapper.eq(Role::getDelFlag, 0);
        wrapper.eq(Role::getStatus, 1);
        wrapper.orderByAsc(Role::getSortOrder);

        List<Role> roles = roleMapper.selectList(wrapper);
        return roles.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    /**
     * 插入角色菜单关联
     *
     * @param roleId  角色ID
     * @param menuIds 菜单ID数组
     */
    private void insertRoleMenus(Long roleId, Long[] menuIds) {
        if (menuIds == null || menuIds.length == 0) {
            return;
        }

        List<com.dangcai.system.domain.RoleMenu> roleMenus = new ArrayList<>();
        for (Long menuId : menuIds) {
            com.dangcai.system.domain.RoleMenu roleMenu = new com.dangcai.system.domain.RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreateTime(LocalDateTime.now());
            roleMenus.add(roleMenu);
        }

        // 批量插入角色菜单关联
        roleMenuMapper.insertBatch(roleMenus);
    }

    /**
     * 转换为VO
     *
     * @param role 角色实体
     * @return 角色VO
     */
    private RoleVO convertToVO(Role role) {
        RoleVO vo = new RoleVO();
        BeanUtils.copyProperties(role, vo);
        vo.setDataScopeName(getDataScopeName(role.getDataScope()));
        return vo;
    }

    /**
     * 获取数据权限范围名称
     */
    private String getDataScopeName(Integer dataScope) {
        if (dataScope == null) {
            return "未设置";
        }
        switch (dataScope) {
            case 1:
                return "全部数据";
            case 2:
                return "本企业数据";
            case 3:
                return "本部门数据";
            case 4:
                return "本人数据";
            default:
                return "未设置";
        }
    }
}
