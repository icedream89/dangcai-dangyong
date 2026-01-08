# 系统模块开发文档

## 模块概述

系统模块（server-system）负责用户认证、授权、角色管理、菜单管理等核心功能。

## 已实现功能

### 1. 实体层（domain）

#### User - 用户实体
- 基本信息：用户名、密码、真实姓名、手机号、邮箱
- 扩展信息：头像、用户类型、关联企业ID
- 状态信息：状态、最后登录时间、最后登录IP
- 继承BaseEntity（创建时间、更新时间、删除标记）

#### Role - 角色实体
- 基本信息：角色名称、角色编码
- 权限配置：数据权限范围
- 状态信息：状态、排序

#### Menu - 菜单权限实体
- 树形结构：父菜单ID、菜单名称
- 类型：目录、菜单、按钮
- 路由配置：路径、组件、权限标识
- 显示配置：图标、排序、可见性、状态

#### UserRole & RoleMenu
- 关联实体，用于多对多关系

### 2. 数据访问层（mapper）

#### UserMapper
- `selectRoleCodesByUserId` - 查询用户角色编码列表
- `selectPermissionsByUserId` - 查询用户权限标识列表
- 继承BaseMapper，拥有基础CRUD方法

#### RoleMapper & MenuMapper
- 继承BaseMapper，提供基础数据访问

### 3. 服务层（service）

#### UserService接口
- `login` - 用户登录
- `getByUsername` - 根据用户名查询
- `getById` - 根据ID查询
- `getRoleCodesByUserId` - 查询用户角色
- `getPermissionsByUserId` - 查询用户权限

#### UserServiceImpl实现类
**登录流程**：
1. 根据用户名查询用户
2. BCrypt验证密码
3. 检查用户状态
4. 生成JWT Token（包含userId、username、userType）
5. 更新最后登录信息
6. 查询用户角色和权限
7. 返回LoginUserVO（含Token、角色、权限）

### 4. 安全配置（security）

#### SecurityConfig - Spring Security配置
- 密码编码器：BCryptPasswordEncoder
- 认证管理器：AuthenticationManager
- CORS配置：允许跨域访问
- 会话管理：无状态（STATELESS）
- 接口权限：
  - 公开接口：登录、注册、Knife4j文档
  - 其他接口：需要认证
- JWT过滤器：在UsernamePasswordAuthenticationFilter之前执行

#### JwtAuthenticationFilter - JWT认证过滤器
- 从请求头获取Token（Authorization: Bearer {token}）
- 验证Token有效性
- 解析用户信息（userId、username）
- 创建认证对象并设置到SecurityContext
- 处理异常情况

#### LoginUser - 登录用户信息
- 存储在SecurityContext中的用户信息
- 包含userId和username

### 5. 控制器层（controller）

#### AuthController - 认证控制器
- `POST /auth/login` - 用户登录
- `POST /auth/logout` - 用户登出（TODO: Token黑名单）
- `GET /auth/info` - 获取当前用户信息

#### UserController - 用户管理控制器
- `GET /system/user/page` - 分页查询（需要权限）
- `GET /system/user/{id}` - 根据ID查询（需要权限）
- `POST /system/user` - 新增用户（需要权限）
- `PUT /system/user` - 修改用户（需要权限）
- `DELETE /system/user/{id}` - 删除用户（需要权限）
- 使用@PreAuthorize进行权限控制

### 6. 工具类（utils）

#### SecurityUtils - 安全工具类
- `getLoginUser()` - 获取当前登录用户
- `getUserId()` - 获取当前用户ID
- `getUsername()` - 获取当前用户名
- `isAdmin()` - 判断是否为管理员

### 7. 数据传输对象（dto）

#### LoginDTO - 登录请求
- username（必填）
- password（必填）
- 使用@Valid进行参数校验

#### LoginUserVO - 登录响应
- 用户基本信息
- Token
- 角色列表
- 权限列表

## 认证流程

### 登录流程
```
1. 用户提交用户名和密码
   ↓
2. AuthController.login()
   ↓
3. UserService.login()
   ↓
4. 查询用户、验证密码、检查状态
   ↓
5. 生成JWT Token（包含userId、username、userType）
   ↓
6. 更新最后登录时间
   ↓
7. 查询用户角色和权限
   ↓
8. 返回LoginUserVO（含Token）
```

### 请求认证流程
```
1. 客户端请求携带Token（Header: Authorization: Bearer {token}）
   ↓
2. JwtAuthenticationFilter拦截请求
   ↓
3. 从请求头提取Token
   ↓
4. 验证Token有效性
   ↓
5. 解析Token获取用户信息
   ↓
6. 创建Authentication对象
   ↓
7. 设置到SecurityContext
   ↓
8. 请求继续到达Controller
```

### 权限控制流程
```
1. 请求到达Controller
   ↓
2. 方法上标注@PreAuthorize("hasAuthority('system:user:list')")
   ↓
3. Spring Security检查用户权限
   ↓
4. 从SecurityContext获取LoginUser
   ↓
5. 查询用户权限列表
   ↓
6. 判断是否拥有所需权限
   ↓
7. 有权限：执行方法
   无权限：抛出AccessDeniedException
```

## 技术要点

### 1. JWT配置
- 密钥：dang-cai-dang-yong-secret-key-2026
- 过期时间：2小时（7200000毫秒）
- 算法：HS256
- Claims存储：userId、username、userType

### 2. 密码加密
- 算法：BCrypt
- 强度：默认10轮
- 特点：每次加密结果不同，自带盐值

### 3. 数据权限范围
- 1: 全部数据
- 2: 本企业数据
- 3: 本部门数据
- 4: 本人数据

### 4. 用户类型
- 1: 管理员
- 2: 企业管理员
- 3: 企业员工
- 4: 小程序用户

## 接口文档

### 认证接口

#### 登录
- **URL**: POST /auth/login
- **描述**: 用户登录
- **请求体**:
```json
{
  "username": "admin",
  "password": "123456"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "id": 1,
    "username": "admin",
    "realName": "系统管理员",
    "avatar": "",
    "userType": 1,
    "enterpriseId": null,
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "roles": ["SUPER_ADMIN"],
    "permissions": ["system:user:list", ...]
  }
}
```

#### 登出
- **URL**: POST /auth/logout
- **描述**: 用户登出
- **请求头**: Authorization: Bearer {token}

### 用户管理接口

#### 分页查询
- **URL**: GET /system/user/page?page=1&size=10&username=admin
- **权限**: system:user:list
- **请求头**: Authorization: Bearer {token}

#### 查询详情
- **URL**: GET /system/user/{id}
- **权限**: system:user:query
- **请求头**: Authorization: Bearer {token}

## TODO事项

### 高优先级
- [ ] 实现用户CRUD完整功能
- [ ] 实现Token黑名单（Redis）
- [ ] 实现IP获取工具
- [ ] 实现角色管理接口
- [ ] 实现菜单管理接口
- [ ] 实现用户-角色关联管理
- [ ] 实现角色-菜单关联管理

### 中优先级
- [ ] 实现数据权限控制
- [ ] 实现操作日志记录
- [ ] 实现登录日志记录
- [ ] 添加参数校验注解
- [ ] 完善异常处理

### 低优先级
- [ ] 实现用户头像上传
- [ ] 实现密码修改功能
- [ ] 实现密码重置功能
- [ ] 实现用户导出功能
- [ ] 实现用户批量导入

## 测试用例

### 登录测试
```bash
# 正确用户名密码
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 错误用户名密码
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"wrong"}'
```

### 访问受保护接口
```bash
# 不带Token（应该返回401）
curl http://localhost:8080/api/system/user/1

# 带Token（应该返回200）
curl http://localhost:8080/api/system/user/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## 常见问题

### Q: Token过期怎么办？
A: 前端需要重新登录，或者实现Token刷新机制。

### Q: 如何实现记住登录？
A: 可以延长Token过期时间，或实现记住Token功能。

### Q: 如何实现单点登录？
A: 使用Redis存储最新的Token，旧Token失效。

### Q: 权限如何动态加载？
A: 从数据库查询用户权限，不需要重启服务。

## 更新日志

### 2026-01-07
- ✅ 创建系统模块基础结构
- ✅ 实现用户实体、角色实体、菜单实体
- ✅ 实现UserMapper、RoleMapper、MenuMapper
- ✅ 实现UserService及登录逻辑
- ✅ 配置Spring Security + JWT认证
- ✅ 实现JwtAuthenticationFilter认证过滤器
- ✅ 实现AuthController登录接口
- ✅ 实现UserController用户管理接口
- ✅ 实现SecurityUtils工具类
- ✅ 添加@PreAuthorize权限控制

---

**开发者**: Claude Code  
**最后更新**: 2026-01-07
