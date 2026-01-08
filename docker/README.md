# Docker开发环境使用说明

## 环境要求

- Docker 20.10+
- Docker Compose 2.0+

## 快速启动

1. **启动所有服务**
```bash
cd docker
docker-compose up -d
```

2. **查看服务状态**
```bash
docker-compose ps
```

3. **查看日志**
```bash
# 查看所有服务日志
docker-compose logs -f

# 查看特定服务日志
docker-compose logs -f mysql
docker-compose logs -f redis
docker-compose logs -f minio
```

## 服务访问

### MySQL
- **地址**: localhost:3306
- **用户名**: root
- **密码**: root
- **数据库**: dang_cai_dang_yong
- **客户端工具**: Navicat、DBeaver等

### Redis
- **地址**: localhost:6379
- **密码**: 无
- **客户端工具**: RedisInsight、redis-cli等

### MinIO
- **API地址**: http://localhost:9000
- **控制台**: http://localhost:9001
- **用户名**: minioadmin
- **密码**: minioadmin

## 数据初始化

首次启动时，Docker会自动执行 `../doc/DB.sql` 初始化数据库。

如需手动执行：
```bash
# 进入MySQL容器
docker exec -it dang-cai-mysql mysql -uroot -proot

# 执行SQL脚本
source /docker-entrypoint-initdb.d/DB.sql
```

## MinIO配置

### 创建存储桶

访问 http://localhost:9001，登录后创建存储桶：
- 存储桶名称: `dang-cai-dang-yong`
- 访问权限: Public

### 设置访问策略

1. 进入存储桶 `dang-cai-dang-yong`
2. 点击 `Access Policy`
3. 选择 `Public` 或添加自定义策略

## 常用命令

### 停止所有服务
```bash
docker-compose down
```

### 停止并删除数据卷（谨慎使用）
```bash
docker-compose down -v
```

### 重启特定服务
```bash
docker-compose restart mysql
```

### 进入容器
```bash
# 进入MySQL
docker exec -it dang-cai-mysql bash

# 进入Redis
docker exec -it dang-cai-redis sh

# 进入MinIO
docker exec -it dang-cai-minio sh
```

## 数据备份

### MySQL备份
```bash
docker exec dang-cai-mysql mysqldump -uroot -proot dang_cai_dang_yong > backup.sql
```

### MySQL恢复
```bash
docker exec -i dang-cai-mysql mysql -uroot -proot dang_cai_dang_yong < backup.sql
```

### Redis备份
```bash
docker cp dang-cai-redis:/data/dump.rdb ./redis_backup.rdb
```

## 故障排查

### 端口冲突
如果端口被占用，修改 `docker-compose.yml` 中的端口映射：
```yaml
ports:
  - "3307:3306"  # 将MySQL改为3307端口
```

### 权限问题
确保数据目录有正确的权限：
```bash
chmod -R 755 ./data
```

### 清理并重新启动
```bash
docker-compose down -v
docker-compose up -d
```

## 生产环境部署

生产环境请使用 `docker-compose.prod.yml`：
```bash
docker-compose -f docker-compose.prod.yml up -d
```

注意事项：
1. 修改所有默认密码
2. 配置持久化存储
3. 设置资源限制
4. 启用SSL/TLS
5. 配置日志收集
