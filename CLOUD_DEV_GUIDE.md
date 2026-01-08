# 云端开发环境使用指南

## 🌥️ 云端开发环境

本项目支持在云端进行开发，无需本地搭建复杂的环境。

### 支持的云端平台

#### 1. GitHub Codespaces（推荐）⭐

**优势**:
- ✅ 免费（每月60小时）
- ✅ 完美支持Java/Maven项目
- ✅ 内置Docker支持
- ✅ 与GitHub无缝集成
- ✅ VS Code界面

**使用方法**:

1. **创建Codespace**
   - 将项目推送到GitHub仓库
   - 在GitHub上打开项目
   - 点击 "Code" → "Codespaces" → "Create codespace on main"
   - 等待环境初始化（约3-5分钟）

2. **自动初始化**
   - 环境创建后会自动执行 `setup.sh` 脚本
   - 自动编译项目
   - 启动MySQL、Redis、MinIO服务

3. **开始开发**
   - 在VS Code中编辑代码
   - 使用集成终端运行服务
   - 通过端口转发访问Web服务

---

#### 2. Gitpod

**优势**:
- ✅ 免费500小时/月
- ✅ 支持Java/Maven
- ✅ 自动预览URL

**使用方法**:

1. 访问 https://gitpod.io
2. 连接GitHub账号
3. 在项目URL前加 `https://gitpod.io/#/`
4. 例如：`https://gitpod.io/#/github.com/yourusername/dang-cai-dang-yong`

---

#### 3. 阿里云 Cloud Studio

**优势**:
- ✅ 国内访问速度快
- ✅ 免费额度（每月3000分钟）

**使用方法**:

1. 访问 https://cloudstudio.net
2. 注册阿里云账号
3. 创建工作空间，选择Java模板（JDK 17 + Maven）
4. 导入项目

---

## 📦 环境配置详情

### 预装软件

- **Java**: OpenJDK 17
- **Maven**: 3.8.6
- **Node.js**: 18
- **Docker**: 最新版
- **MySQL**: 8.0
- **Redis**: 7
- **MinIO**: 最新版

### 预装VS Code扩展

- Spring Boot Extension Pack
- Lombok
- Spring Boot Dashboard
- Java Extension Pack
- REST Client
- GitHub Copilot（可选）

---

## 🚀 快速开始

### 方式一：使用Codespaces

1. **创建Codespace**
   ```bash
   # 在GitHub上点击 "Code" → "Codespaces" → "Create codespace"
   ```

2. **等待初始化完成**
   - 自动安装依赖
   - 自动编译项目
   - 自动启动基础服务

3. **启动后端服务**
   ```bash
   # 启动后台管理API
   cd server/server-admin
   mvn spring-boot:run

   # 或启动小程序API
   cd ../server-miniapp
   mvn spring-boot:run
   ```

4. **启动前端服务**
   ```bash
   # 启动管理后台
   cd admin
   npm run dev
   ```

5. **访问服务**
   - 后台API: http://localhost:8080/api
   - Knife4j文档: http://localhost:8080/api/doc.html
   - 管理后台: http://localhost:3000

### 方式二：手动配置

如果自动初始化失败，可以手动执行：

```bash
# 1. 编译项目
cd server
mvn clean install -DskipTests

# 2. 启动基础服务（Docker）
cd ../docker
docker-compose up -d

# 3. 初始化数据库
mysql -h db -uroot -proot < doc/DB.sql

# 4. 启动后端服务
cd server/server-admin
mvn spring-boot:run

# 5. 安装前端依赖
cd ../../admin
npm install
npm run dev
```

---

## 🔧 常用命令

### 编译项目
```bash
cd server
mvn clean install
```

### 运行测试
```bash
cd server
mvn test
```

### 启动MySQL
```bash
docker-compose -f .devcontainer/docker-compose.yml up -d db
```

### 启动Redis
```bash
docker-compose -f .devcontainer/docker-compose.yml up -d redis
```

### 启动MinIO
```bash
docker-compose -f .devcontainer/docker-compose.yml up -d minio
```

### 查看服务状态
```bash
docker-compose -f .devcontainer/docker-compose.yml ps
```

### 查看日志
```bash
docker-compose -f .devcontainer/docker-compose.yml logs -f
```

---

## 🌐 端口映射

| 服务 | 端口 | 说明 |
|-----|------|------|
| 后台管理API | 8080 | 后台管理系统API |
| 小程序API | 8081 | 小程序API |
| 管理后台前端 | 3000 | Vue3管理后台 |
| MySQL | 3306 | 数据库 |
| Redis | 6379 | 缓存 |
| MinIO API | 9000 | 对象存储API |
| MinIO控制台 | 9001 | MinIO管理界面 |

---

## 📝 开发建议

### 1. 使用热重载

后端使用Spring Boot DevTools：
```bash
mvn spring-boot:run
```

前端使用Vite热重载：
```bash
npm run dev
```

### 2. 使用Git

在Codespaces中使用Git：
```bash
git status
git add .
git commit -m "commit message"
git push
```

### 3. 使用端口转发

Codespaces会自动转发端口，点击"在浏览器中打开"即可访问。

### 4. 调试代码

- 后端：使用VS Code的Java调试器
- 前端：使用Chrome DevTools

---

## ⚠️ 常见问题

### Q: 编译失败？
**A**: 检查Java版本是否为17+：
```bash
java -version
```

### Q: MySQL连接失败？
**A**: 等待MySQL启动完成：
```bash
docker-compose -f .devcontainer/docker-compose.yml logs -f db
```

### Q: 端口被占用？
**A**: 修改application.yml中的端口号

### Q: 内存不足？
**A**: Codespaces默认2核4核，如需更多资源可升级套餐

---

## 💡 提示

1. **保存工作**: Codespaces会在一段时间不活动后自动休眠，记得及时push代码
2. **数据持久化**: Docker数据会保存在volumes中，删除Codespace会丢失数据
3. **费用**: 免费额度用完后会按小时计费，记得删除不用的Codespace

---

## 📚 相关文档

- [GitHub Codespaces文档](https://docs.github.com/en/codespaces)
- [Spring Boot文档](https://spring.io/projects/spring-boot)
- [Vue3文档](https://vuejs.org/)
- [项目开发文档](./README.md)

---

**祝开发愉快！** 🎉
