package com.dangcai.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dangcai.system.dto.LoginDTO;
import com.dangcai.system.dto.LoginUserVO;
import com.dangcai.system.dto.UserDTO;
import com.dangcai.system.dto.UserQueryDTO;
import com.dangcai.system.dto.UserVO;
import com.dangcai.system.domain.User;

/**
 * 用户服务接口
 *
 * @author dangcai
 * @since 2026-01-07
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginDTO 登录请求
     * @return 登录用户信息
     */
    LoginUserVO login(LoginDTO loginDTO);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(String username);

    /**
     * 根据用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    User getById(Long userId);

    /**
     * 根据用户ID查询角色编码列表
     *
     * @param userId 用户ID
     * @return 角色编码列表
     */
    java.util.List<String> getRoleCodesByUserId(Long userId);

    /**
     * 根据用户ID查询权限标识列表
     *
     * @param userId 用户ID
     * @return 权限标识列表
     */
    java.util.List<String> getPermissionsByUserId(Long userId);

    /**
     * 分页查询用户列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    Page<UserVO> page(UserQueryDTO queryDTO);

    /**
     * 根据ID查询用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserVO getUserVOById(Long id);

    /**
     * 新增用户
     *
     * @param userDTO 用户信息
     * @return 用户ID
     */
    Long add(UserDTO userDTO);

    /**
     * 修改用户
     *
     * @param userDTO 用户信息
     */
    void update(UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void delete(Long id);

    /**
     * 重置密码
     *
     * @param id          用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long id, String newPassword);

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(String oldPassword, String newPassword);

    /**
     * 修改用户状态
     *
     * @param id     用户ID
     * @param status 状态
     */
    void updateStatus(Long id, Integer status);
}
