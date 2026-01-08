package com.dangcai.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.system.domain.Menu;
import com.dangcai.system.domain.RoleMenu;
import com.dangcai.system.dto.MenuDTO;
import com.dangcai.system.dto.MenuTreeVO;
import com.dangcai.system.dto.MenuVO;
import com.dangcai.system.mapper.MenuMapper;
import com.dangcai.system.mapper.RoleMapper;
import com.dangcai.system.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<MenuVO> tree() {
        // 1. 查询所有菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getDelFlag, 0);
        wrapper.orderByAsc(Menu::getSortOrder);
        List<Menu> allMenus = menuMapper.selectList(wrapper);

        // 2. 转换为VO
        List<MenuVO> menuVOs = allMenus.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 3. 构建树形结构
        return buildTree(menuVOs, 0L);
    }

    @Override
    public List<MenuTreeVO> treeForPermission() {
        // 1. 查询所有菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getDelFlag, 0);
        wrapper.eq(Menu::getStatus, 1);
        wrapper.orderByAsc(Menu::getSortOrder);
        List<Menu> allMenus = menuMapper.selectList(wrapper);

        // 2. 转换为树形VO
        List<MenuTreeVO> treeVOs = allMenus.stream()
                .map(this::convertToTreeVO)
                .collect(Collectors.toList());

        // 3. 构建树形结构
        return buildTreeForPermission(treeVOs, 0L);
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        wrapper.select(RoleMenu::getMenuId);

        List<RoleMenu> roleMenus = roleMapper.selectList(wrapper);
        return roleMenus.stream()
                .map(RoleMenu::getMenuId)
                .collect(Collectors.toList());
    }

    @Override
    public MenuVO getById(Long id) {
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        return convertToVO(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(MenuDTO menuDTO) {
        // 1. 创建菜单
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);

        // 设置默认值
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (menu.getVisible() == null) {
            menu.setVisible(1); // 默认显示
        }
        if (menu.getStatus() == null) {
            menu.setStatus(1); // 默认正常
        }
        if (menu.getSortOrder() == null) {
            menu.setSortOrder(0);
        }

        menuMapper.insert(menu);

        log.info("新增菜单成功：{}", menu.getMenuName());
        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDTO menuDTO) {
        // 1. 检查菜单是否存在
        Menu menu = menuMapper.selectById(menuDTO.getId());
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 2. 不能将父菜单设置为自己
        if (menuDTO.getId().equals(menuDTO.getParentId())) {
            throw new BusinessException("不能将父菜单设置为自己");
        }

        // 3. 更新菜单信息
        BeanUtils.copyProperties(menuDTO, menu, "id");
        menuMapper.updateById(menu);

        log.info("修改菜单成功：{}", menu.getMenuName());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 1. 检查菜单是否存在
        Menu menu = menuMapper.selectById(id);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }

        // 2. 检查是否有子菜单
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, id);
        wrapper.eq(Menu::getDelFlag, 0);
        Long count = menuMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException("该菜单下还有子菜单，无法删除");
        }

        // 3. 逻辑删除菜单
        menu.setDelFlag(1);
        menuMapper.updateById(menu);

        log.info("删除菜单成功：{}", menu.getMenuName());
    }

    /**
     * 构建菜单树
     *
     * @param menuVOs 菜单VO列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<MenuVO> buildTree(List<MenuVO> menuVOs, Long parentId) {
        List<MenuVO> tree = new ArrayList<>();

        for (MenuVO menuVO : menuVOs) {
            if (menuVO.getParentId().equals(parentId)) {
                // 递归查找子菜单
                menuVO.setChildren(buildTree(menuVOs, menuVO.getId()));
                tree.add(menuVO);
            }
        }

        return tree;
    }

    /**
     * 构建权限菜单树
     *
     * @param treeVOs 菜单树VO列表
     * @param parentId 父菜单ID
     * @return 菜单树
     */
    private List<MenuTreeVO> buildTreeForPermission(List<MenuTreeVO> treeVOs, Long parentId) {
        List<MenuTreeVO> tree = new ArrayList<>();

        for (MenuTreeVO treeVO : treeVOs) {
            if (treeVO.getParentId().equals(parentId)) {
                // 递归查找子菜单
                treeVO.setChildren(buildTreeForPermission(treeVOs, treeVO.getId()));
                tree.add(treeVO);
            }
        }

        return tree;
    }

    /**
     * 转换为VO
     *
     * @param menu 菜单实体
     * @return 菜单VO
     */
    private MenuVO convertToVO(Menu menu) {
        MenuVO vo = new MenuVO();
        BeanUtils.copyProperties(menu, vo);
        vo.setMenuTypeName(getMenuTypeName(menu.getMenuType()));
        return vo;
    }

    /**
     * 转换为树形VO
     *
     * @param menu 菜单实体
     * @return 菜单树VO
     */
    private MenuTreeVO convertToTreeVO(Menu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setLabel(menu.getMenuName());
        vo.setMenuType(menu.getMenuType());
        vo.setPermission(menu.getPermission());
        return vo;
    }

    /**
     * 获取菜单类型名称
     */
    private String getMenuTypeName(Integer menuType) {
        if (menuType == null) {
            return "未知";
        }
        switch (menuType) {
            case 1:
                return "目录";
            case 2:
                return "菜单";
            case 3:
                return "按钮";
            default:
                return "未知";
        }
    }
}
