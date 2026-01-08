# 当才当用 - 政府企业服务平台

> 当阳市科技经济信息商务局企业服务平台

## 项目简介

"当才当用"是一个为当阳市科技经济信息商务局打造的企业服务平台，旨在搭建政府-企业-用户三方服务桥梁，提供政策推送、企业展示、供需对接、在线求助等核心功能。

### 项目概况

- **项目名称**: 当才当用 (Dang Cai Dang Yong)
- **客户**: 当阳市科技经济信息商务局
- **开发状态**: 开发中 (Phase 1 - 基础设施搭建完成)
- **技术栈**: Spring Boot 3.2 + Vue 3 + MySQL 8.0 + Redis 7.x + MinIO

### Demo版本 ✅

**前端Demo已完成开发，可用于客户演示**
- ✅ 管理后台（Web端）- Vue3 + Element Plus
- ✅ 微信小程序 - uni-app
- ✅ 使用Mock数据，无需后端即可运行

**详细说明**: 参见 [DEMO_README.md](./DEMO_README.md)

## 核心功能

- **企业管理**: 企业信息录入、审核、展示、员工管理
- **政策管理**: 政策发布、分类查询、精准推送
- **企业课堂**: 培训资料上传、在线学习
- **产品展示**: 企业产品发布、推荐、搜索
- **求助工单**: 在线求助、工单流转、满意度评价
- **供需对接**: 采购需求发布、供应商匹配、在线沟通

---

## 📖 快速开始

### Demo演示（前端Mock版本）

适用场景：客户演示、UI/UX展示

#### 管理后台Demo
```bash
cd admin
npm install
npm run dev
# 访问: http://localhost:3000
# 账号: admin / 123456
```

#### 小程序Demo
```bash
# 使用HBuilderX打开 miniprogram 目录
# 运行 → 运行到小程序模拟器 → 微信开发者工具
```

**详细说明**: [DEMO_README.md](./DEMO_README.md)

---

### 开发环境（完整版本）

适用场景：后端开发、API联调

#### 环境要求
- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0
- Redis 7.x
- Docker (可选)

#### 1. 启动基础服务（Docker）
```bash
cd docker
docker-compose up -d
```

服务说明:
- MySQL: `localhost:3306` (root/root)
- Redis: `localhost:6379`
- MinIO: `localhost:9000` (minioadmin/minioadmin)
- MinIO控制台: http://localhost:9001

#### 2. 初始化数据库
```bash
# Docker自动初始化，或手动执行：
mysql -uroot -proot < doc/DB.sql
```

#### 3. 启动后端服务
```bash
cd server
mvn clean install

# 启动后台管理API (端口: 8080)
cd server-admin
mvn spring-boot:run

# 或启动小程序API (端口: 8081)
cd ../server-miniapp
mvn spring-boot:run
```

**后端服务**:
- 后台管理API: http://localhost:8080/api
- 小程序API: http://localhost:8081/miniapp-api
- Knife4j文档: http://localhost:8080/api/doc.html

**默认管理员**: admin / 123456

#### 4. 启动前端服务

**管理后台**:
```bash
cd admin
npm install
npm run dev
```

**小程序**:
```bash
# 使用HBuilderX打开 miniprogram 目录
# 运行到小程序模拟器
```

---

## 项目结构

```
dang-cai-dang-yong/
├── server/                      # 后端服务（多模块Maven项目）
│   ├── server-common/           # 通用模块
│   ├── server-system/           # 系统模块
│   ├── server-enterprise/       # 企业模块
│   ├── server-business/         # 业务模块
│   ├── server-file/             # 文件服务
│   ├── server-message/          # 消息服务
│   ├── server-admin/            # 后台API (8080)
│   └── server-miniapp/          # 小程序API (8081)
├── admin/                       # Web管理后台 (Vue3)
├── miniprogram/                # uni-app小程序
├── doc/                        # 文档
│   └── DB.sql                  # 数据库脚本
├── docker/                     # Docker配置
│   └── docker-compose.yml
└── README.md                   # 项目说明
```

## 数据库设计

### 核心表（22张）

**系统核心（6张）**:
- sys_user, sys_role, sys_menu, sys_user_role, sys_role_menu, sys_config

**企业核心（3张）**:
- ent_enterprise, ent_employee, ent_enterprise_log

**业务核心（9张）**:
- biz_category, biz_product, biz_policy, biz_course
- biz_help_ticket, biz_help_flow
- biz_purchase_requirement, biz_purchase_reply, biz_purchase_match

**日志表（4张）**:
- sys_oper_log, sys_login_log, biz_message_subscribe, sys_file

详细脚本: `doc/DB.sql`

## 开发进度

### ✅ Phase 1: 基础设施搭建 (已完成)
- 多模块Maven项目结构
- 22张核心数据库表
- Spring Boot + MyBatis-Plus + Redis + MinIO集成
- 通用模块（base entity、响应封装、异常处理、工具类）
- Docker开发环境
- 前端Demo（管理后台 + 小程序）

### 🔄 Phase 2: 核心模块开发 (进行中)
- ⏳ 用户认证与授权
- ⏳ 企业管理模块
- ⏳ 业务管理模块
- ⏳ 文件服务
- ⏳ 消息服务

### ⏳ Phase 3: 前端集成 (待开始)
- API集成
- 微信登录
- 实时通信

### ⏳ Phase 4: 测试与部署 (待开始)

## 常见问题

**Q: Docker服务启动失败？**
A: 检查端口占用，修改docker-compose.yml端口映射

**Q: 数据库连接失败？**
A: 确认MySQL已启动，检查application.yml配置

**Q: MinIO上传失败？**
A: 访问http://localhost:9001创建存储桶`dang-cai-dang-yong`

## 更新日志

### 2026-01-07
- ✅ 完成后端基础架构搭建
- ✅ 创建22张核心数据库表
- ✅ 完成前端Demo开发
- ✅ 进入正式开发阶段

### 2026-01-06
- ✅ 制定开发计划
- ✅ 完成需求分析与技术选型

---

**客户**: 当阳市科技经济信息商务局
**开发**: 2026年1月
**License**: Copyright © 2026

