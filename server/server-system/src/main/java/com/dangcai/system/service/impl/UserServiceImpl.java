package com.dangcai.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.common.exception.BusinessException;
import com.dangcai.common.utils.JwtUtils;
import com.dangcai.system.dto.LoginDTO;
import com.dangcai.system.dto.LoginUserVO;
import com.dangcai.system.dto.RoleVO;
import com.dangcai.system.dto.UserDTO;
import com.dangcai.system.dto.UserQueryDTO;
import com.dangcai.system.dto.UserVO;
import com.dangcai.system.domain.Role;
import com.dangcai.system.domain.User;
import com.dangcai.system.domain.UserRole;
import com.dangcai.system.mapper.RoleMapper;
import com.dangcai.system.mapper.UserMapper;
import com.dangcai.system.security.LoginUser;
import com.dangcai.system.service.UserService;
import com.dangcai.system.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 *
 * @author dangcai
 * @since 2026-01-07
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private com.dangcai.system.mapper.UserRoleMapper userRoleMapper;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 密码编码器
     */
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginUserVO login(LoginDTO loginDTO) {
        // 1. 查询用户
        User user = getByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用，请联系管理员");
        }

        // 4. 生成Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        String token = jwtUtils.generateToken(user.getUsername(), claims);

        // 5. 更新最后登录信息
        user.setLastLoginTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        // user.setLastLoginIp(IpUtils.getIpAddr()); // TODO: 获取IP
        userMapper.updateById(user);

        // 6. 查询角色和权限
        List<String> roles = getRoleCodesByUserId(user.getId());
        List<String> permissions = getPermissionsByUserId(user.getId());

        // 7. 构建返回结果
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(user.getId());
        loginUserVO.setUsername(user.getUsername());
        loginUserVO.setRealName(user.getRealName());
        loginUserVO.setAvatar(user.getAvatar());
        loginUserVO.setUserType(user.getUserType());
        loginUserVO.setEnterpriseId(user.getEnterpriseId());
        loginUserVO.setToken(token);
        loginUserVO.setRoles(roles);
        loginUserVO.setPermissions(permissions);

        log.info("用户登录成功：{}", user.getUsername());
        return loginUserVO;
    }

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.eq(User::getDelFlag, 0);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User getById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    public List<String> getRoleCodesByUserId(Long userId) {
        return userMapper.selectRoleCodesByUserId(userId);
    }

    @Override
    public List<String> getPermissionsByUserId(Long userId) {
        return userMapper.selectPermissionsByUserId(userId);
    }

    @Override
    public Page<UserVO> page(UserQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(queryDTO.getUsername())) {
            wrapper.like(User::getUsername, queryDTO.getUsername());
        }
        if (StringUtils.hasText(queryDTO.getRealName())) {
            wrapper.like(User::getRealName, queryDTO.getRealName());
        }
        if (StringUtils.hasText(queryDTO.getPhone())) {
            wrapper.eq(User::getPhone, queryDTO.getPhone());
        }
        if (queryDTO.getUserType() != null) {
            wrapper.eq(User::getUserType, queryDTO.getUserType());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(User::getStatus, queryDTO.getStatus());
        }

        wrapper.eq(User::getDelFlag, 0);
        wrapper.orderByDesc(User::getCreateTime);

        // 分页查询
        Page<User> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        Page<User> userPage = userMapper.selectPage(page, wrapper);

        // 转换为VO
        Page<UserVO> voPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        List<UserVO> voList = userPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public UserVO getUserVOById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(UserDTO userDTO) {
        // 1. 检查用户名是否存在
        if (getByUsername(userDTO.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 检查手机号是否存在
        if (StringUtils.hasText(userDTO.getPhone())) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, userDTO.getPhone());
            wrapper.eq(User::getDelFlag, 0);
            if (userMapper.selectCount(wrapper) > 0) {
                throw new BusinessException("手机号已被使用");
            }
        }

        // 3. 创建用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 加密密码
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // 设置默认值
        if (user.getUserType() == null) {
            user.setUserType(4); // 默认为小程序用户
        }
        if (user.getStatus() == null) {
            user.setStatus(1); // 默认正常
        }

        userMapper.insert(user);

        // 4. 分配角色
        if (userDTO.getRoleIds() != null && userDTO.getRoleIds().length > 0) {
            insertUserRoles(user.getId(), userDTO.getRoleIds());
        }

        log.info("新增用户成功：{}", user.getUsername());
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO userDTO) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(userDTO.getId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 检查用户名是否被其他用户使用
        User existUser = getByUsername(userDTO.getUsername());
        if (existUser != null && !existUser.getId().equals(userDTO.getId())) {
            throw new BusinessException("用户名已被其他用户使用");
        }

        // 3. 更新用户信息
        BeanUtils.copyProperties(userDTO, user, "id", "password");

        // 如果提供了新密码，则加密
        if (StringUtils.hasText(userDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userMapper.updateById(user);

        // 4. 更新角色关联
        if (userDTO.getRoleIds() != null) {
            // 删除旧的角色关联
            userRoleMapper.deleteByUserId(user.getId());

            // 插入新的角色关联
            insertUserRoles(user.getId(), userDTO.getRoleIds());
        }

        log.info("修改用户成功：{}", user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 不能删除管理员
        if (id == 1L) {
            throw new BusinessException("不能删除超级管理员");
        }

        // 3. 逻辑删除用户
        user.setDelFlag(1);
        userMapper.updateById(user);

        log.info("删除用户成功：{}", user.getUsername());
    }

    @Override
    public void resetPassword(Long id, String newPassword) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 重置密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);

        log.info("重置用户密码成功：{}", user.getUsername());
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // 1. 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new BusinessException("用户未登录");
        }

        // 2. 查询用户信息
        User user = userMapper.selectById(loginUser.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 3. 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 4. 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateById(user);

        log.info("用户修改密码成功：{}", user.getUsername());
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        // 1. 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 不能禁用管理员
        if (id == 1L && status == 0) {
            throw new BusinessException("不能禁用超级管理员");
        }

        // 3. 更新状态
        user.setStatus(status);
        userMapper.updateById(user);

        log.info("修改用户状态成功：{}，状态：{}", user.getUsername(), status);
    }

    /**
     * 插入用户角色关联
     *
     * @param userId  用户ID
     * @param roleIds 角色ID数组
     */
    private void insertUserRoles(Long userId, Long[] roleIds) {
        if (roleIds == null || roleIds.length == 0) {
            return;
        }

        List<UserRole> userRoles = new ArrayList<>();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(LocalDateTime.now());
            userRoles.add(userRole);
        }

        // 批量插入用户角色关联
        userRoleMapper.insertBatch(userRoles);
    }

    /**
     * 转换为VO
     *
     * @param user 用户实体
     * @return 用户VO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);

        // 设置用户类型名称
        vo.setUserTypeName(getUserTypeName(user.getUserType()));

        // 设置状态名称
        vo.setStatusName(user.getStatus() == 1 ? "正常" : "禁用");

        // 查询用户角色
        List<String> roleCodes = getRoleCodesByUserId(user.getId());
        if (roleCodes != null && !roleCodes.isEmpty()) {
            LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Role::getRoleCode, roleCodes);
            wrapper.eq(Role::getDelFlag, 0);
            List<Role> roles = roleMapper.selectList(wrapper);

            List<RoleVO> roleVOs = roles.stream().map(role -> {
                RoleVO roleVO = new RoleVO();
                BeanUtils.copyProperties(role, roleVO);
                roleVO.setDataScopeName(getDataScopeName(role.getDataScope()));
                return roleVO;
            }).collect(Collectors.toList());

            vo.setRoles(roleVOs);
            vo.setRoleIds(roles.stream().map(Role::getId).collect(Collectors.toList()));
        }

        return vo;
    }

    /**
     * 获取用户类型名称
     */
    private String getUserTypeName(Integer userType) {
        if (userType == null) {
            return "未知";
        }
        switch (userType) {
            case 1:
                return "管理员";
            case 2:
                return "企业管理员";
            case 3:
                return "企业员工";
            case 4:
                return "小程序用户";
            default:
                return "未知";
        }
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
