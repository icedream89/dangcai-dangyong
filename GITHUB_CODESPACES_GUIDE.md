# 🚀 GitHub Codespaces 快速启动指南

## 一、创建GitHub仓库并推送代码

### 1. 初始化Git仓库
```bash
git init
git add .
git commit -m "feat: 当才当用项目完整实现

- 完成系统模块（用户、角色、菜单、权限、日志）
- 完成企业模块（企业管理、员工管理、审核）
- 完成业务模块（政策、产品、分类、课堂、工单、供需）
- 完成文件服务（MinIO集成）
- 完成消息服务（WebSocket推送）
- 完成小程序API（微信登录、移动端接口）
- 完成云端开发环境配置"
```

### 2. 创建GitHub仓库
1. 访问 https://github.com/new
2. 仓库名称：`dang-cai-dang-yong`
3. 描述：`当才当用 - 政府企业服务平台`
4. **不要**初始化README、.gitignore、license
5. 点击"Create repository"

### 3. 推送代码到GitHub
```bash
git remote add origin https://github.com/YOUR_USERNAME/dang-cai-dang-yong.git
git branch -M main
git push -u origin main
```

---

## 二、创建GitHub Codespace

### 方式一：通过GitHub网页创建（推荐）

1. **打开项目仓库**
   - 访问你的GitHub仓库页面
   - 点击绿色的 "Code" 按钮
   - 选择 "Codespaces" 标签
   - 点击 "Create codespace on main"

2. **选择配置**
   - 选择机器类型：**2核**（推荐）或4核
   - 点击 "Create codespace"

3. **等待环境初始化（约3-5分钟）**
   - 自动安装JDK 17
   - 自动安装Maven
   - 自动安装Node.js 18
   - 自动安装Docker
   - 自动执行setup.sh脚本

---

### 方式二：通过GitHub CLI创建

```bash
# 安装GitHub CLI
# Mac: brew install gh
# Windows: scoop bucket add extras && scoop install gh

# 登录GitHub
gh auth login

# 创建Codespace
gh codespace create \
  --repo YOUR_USERNAME/dang-cai-dang-yong \
  --branch main \
  --machine standard-2x4 \
  --displayName "当才当用开发环境"
```

---

## 三、启动后端服务

Codespace创建完成后，会自动打开VS Code界面。在终端中执行以下命令：

### 1. 启动后台管理API
```bash
cd /workspace/server/server-admin
mvn spring-boot:run
```

### 2. 打开新终端启动小程序API
- 按 `` Ctrl+Shift+` ` `` 打开新终端
- 执行：
```bash
cd /workspace/server/server-miniapp
mvn spring-boot:run
```

---

## 四、访问服务

服务启动后，会自动进行端口转发：

| 服务 | 端口 | 访问方式 |
|-----|------|---------|
| 后台API文档 | 8080 | 点击弹出的"在浏览器中打开" |
| 小程序API | 8081 | 点击弹出的"在浏览器中打开" |
| MinIO控制台 | 9001 | 点击弹出的"在浏览器中打开" |

**直接访问URL**：
- 后台API: http://localhost:8080/api/doc.html
- 小程序API: http://localhost:8081/miniapp-api/doc.html
- MinIO: http://localhost:9001

---

## 五、测试API接口

### 1. 测试登录
```bash
# 在VS Code的集成终端中执行
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

### 2. 使用Knife4j文档测试
1. 访问 http://localhost:8080/api/doc.html
2. 点击"登录接口"
3. 点击"调试"
4. 输入用户名：admin，密码：123456
5. 点击"发送"
6. 复制返回的token
7. 点击页面右上角的"Authorize"按钮
8. 输入：`Bearer YOUR_TOKEN`（注意Bearer后面有空格）
9. 点击"Authorize"
10. 现在可以测试所有需要认证的接口了

---

## 六、常用操作

### 查看Docker服务状态
```bash
cd /workspace/docker
docker-compose ps
```

### 查看MySQL日志
```bash
cd /workspace/docker
docker-compose logs -f mysql
```

### 重启基础服务
```bash
cd /workspace/docker
docker-compose restart
```

### 停止所有服务
```bash
cd /workspace/docker
docker-compose down
```

### 重新启动基础服务
```bash
cd /workspace/docker
docker-compose up -d
```

---

## 七、调试技巧

### 1. 查看实时日志
在VS Code中打开终端，运行：
```bash
cd /workspace/server/server-admin
mvn spring-boot:run | grep -E "Started|ERROR|WARN"
```

### 2. 使用VS Code的Java调试器
1. 点击左侧调试图标（或按 `F5`）
2. 选择"Java"
3. 配置launch.json（如果需要）
4. 在代码中设置断点
5. 点击"开始调试"

### 3. 查看端口转发
1. 点击VS Code底部的"端口"标签
2. 查看所有转发的端口
3. 点击"在浏览器中打开"

---

## 八、停止Codespace

### 暂停Codespace（保留数据）
1. 访问 https://github.com/codespaces
2. 找到你的Codespace
3. 点击"..."菜单
4. 选择"Stop"

### 删除Codespace（清空数据）
1. 访问 https://github.com/codespaces
2. 找到你的Codespace
3. 点击"..."菜单
4. 选择"Delete permanently"

**注意**：
- 删除Codespace会清空所有数据
- 代码已保存在GitHub仓库，不会丢失
- Docker数据会丢失

---

## 九、费用说明

### 免费额度
- **免费用户**：每月60小时
- **Pro用户**：每月180小时

### 费用估算
- 标准配置（2核4核）：约$0.18/小时
- 每月使用100小时：约$18/月

### 节省费用
1. 不使用时及时停止Codespace
2. 使用2核配置（够用即可）
3. 避免长时间空闲

---

## 十、常见问题

### Q1：Codespace创建失败？
**A**：
- 检查GitHub账户是否已验证邮箱
- 检查是否超过免费额度
- 尝试刷新页面重新创建

### Q2：端口无法访问？
**A**：
- 检查服务是否已启动
- 查看VS Code底部的"端口"标签
- 检查防火墙设置

### Q3：编译失败？
**A**：
- 查看错误信息
- 确认setup.sh是否正常执行
- 检查基础服务是否启动

### Q4：数据库连接失败？
**A**：
```bash
cd /workspace/docker
docker-compose ps
docker-compose logs mysql
```

---

## 十一、下一步

环境启动成功后，你可以：

1. ✅ **查看API文档** - 测试所有接口
2. ✅ **调试代码** - 使用VS Code调试器
3. ✅ **编写单元测试** - 右键类名 → "Go to Test"
4. ✅ **提交代码** - 直接在VS Code中提交推送
5. ✅ **团队协作** - 邀请团队成员到Codespace

---

## 🎉 开始使用

现在就创建你的GitHub Codespace，体验云端开发的便利吧！

**快速开始**：
1. 推送代码到GitHub ✅
2. 创建Codespace ✅
3. 等待初始化 ✅
4. 启动服务 ✅
5. 开始开发 ✅

祝开发愉快！ 🚀
