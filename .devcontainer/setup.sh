#!/bin/bash

# 当才当用项目环境初始化脚本

echo "================================"
echo "开始初始化开发环境..."
echo "================================"

# 1. 等待MySQL启动
echo "等待MySQL启动..."
while ! mysqladmin ping -h db -uroot -proot --silent; do
    sleep 1
done
echo "MySQL已启动"

# 2. 等待Redis启动
echo "等待Redis启动..."
while ! redis-cli -h redis ping; do
    sleep 1
done
echo "Redis已启动"

# 3. 编译项目
echo "开始编译项目..."
cd /workspace/server
mvn clean install -DskipTests

if [ $? -eq 0 ]; then
    echo "项目编译成功"
else
    echo "项目编译失败"
    exit 1
fi

# 4. 安装前端依赖（可选）
echo "是否安装前端依赖？(y/n)"
read answer

if [ "$answer" = "y" ]; then
    echo "安装管理后台依赖..."
    cd /workspace/admin
    npm install

    echo "安装小程序依赖..."
    cd /workspace/miniprogram
    npm install
fi

echo "================================"
echo "开发环境初始化完成"
echo "================================"
echo ""
echo "服务信息："
echo "  MySQL:    localhost:3306 (root/root)"
echo "  Redis:    localhost:6379"
echo "  MinIO:    localhost:9000 (minioadmin/minioadmin)"
echo "  MinIO控制台: http://localhost:9001"
echo ""
echo "后端服务："
echo "  启动后台API:  cd server/server-admin && mvn spring-boot:run"
echo "  启动小程序API: cd server/server-miniapp && mvn spring-boot:run"
echo ""
echo "前端服务："
echo "  启动管理后台: cd admin && npm run dev"
echo ""
echo "访问地址："
echo "  后台API:     http://localhost:8080/api"
echo "  小程序API:    http://localhost:8081/miniapp-api"
echo "  Knife4j文档: http://localhost:8080/api/doc.html"
echo "  管理后台:    http://localhost:3000"
echo "================================"
