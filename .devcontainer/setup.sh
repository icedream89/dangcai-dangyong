#!/bin/bash
set -e

echo "========================================"
echo "  当才当用 - 云端开发环境初始化"
echo "========================================"
echo ""

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. 显示欢迎信息
echo -e "${GREEN}🚀 欢迎使用当才当用项目云端开发环境！${NC}"
echo ""

# 2. 检查Docker是否可用
echo -e "${YELLOW}📦 检查Docker环境...${NC}"
if docker --version &> /dev/null; then
    echo -e "${GREEN}✓ Docker已安装: $(docker --version | head -1)${NC}"
else
    echo -e "${YELLOW}⚠ Docker未安装，将跳过基础服务启动${NC}"
    NO_DOCKER=1
fi

# 3. 启动基础服务（如果Docker可用）
if [ -z "$NO_DOCKER" ]; then
    echo ""
    echo -e "${YELLOW}🐳 启动基础服务（MySQL、Redis、MinIO）...${NC}"

    cd /workspace/docker

    # 检查docker-compose是否存在
    if [ -f "docker-compose.yml" ]; then
        docker-compose up -d

        echo ""
        echo -e "${GREEN}等待服务启动...${NC}"
        sleep 10

        # 检查服务状态
        echo ""
        echo -e "${GREEN}基础服务状态：${NC}"
        docker-compose ps

        echo ""
        echo -e "${GREEN}✓ 基础服务启动成功${NC}"
        echo "  - MySQL: localhost:3306 (root/root)"
        echo "  - Redis: localhost:6379"
        echo "  - MinIO: http://localhost:9000 (minioadmin/minioadmin)"
        echo "  - MinIO控制台: http://localhost:9001"

        cd /workspace
    else
        echo -e "${YELLOW}⚠ 未找到docker-compose.yml，跳过基础服务启动${NC}"
    fi
fi

# 4. Maven编译项目
echo ""
echo -e "${YELLOW}📦 Maven编译项目...${NC}"
cd /workspace/server

if mvn clean install -DskipTests -q; then
    echo -e "${GREEN}✓ 项目编译成功${NC}"
else
    echo -e "${YELLOW}⚠ 项目编译失败，请检查代码${NC}"
fi

# 5. 创建MinIO存储桶（如果MinIO运行）
if [ -z "$NO_DOCKER" ]; then
    echo ""
    echo -e "${YELLOW}📦 创建MinIO存储桶...${NC}"

    # 等待MinIO启动
    sleep 5

    # 使用mc客户端创建bucket
    if docker exec dang-cai-minio mc alias set local http://localhost:9000 minioadmin minioadmin &> /dev/null; then
        if docker exec dang-cai-minio mc mb local/dang-cai-dang-yong --ignore-existing &> /dev/null; then
            echo -e "${GREEN}✓ MinIO存储桶创建成功${NC}"
        fi
    fi
fi

# 6. 显示启动指南
echo ""
echo "========================================"
echo -e "${GREEN}✅ 环境初始化完成！${NC}"
echo "========================================"
echo ""
echo -e "${YELLOW}📝 下一步操作：${NC}"
echo ""
echo "1. 启动后台管理API (8080端口)："
echo "   cd /workspace/server/server-admin"
echo "   mvn spring-boot:run"
echo ""
echo "2. 启动小程序API (8081端口)："
echo "   cd /workspace/server/server-miniapp"
echo "   mvn spring-boot:run"
echo ""
echo "3. 启动管理后台前端 (3000端口)："
echo "   cd /workspace/admin"
echo "   npm install"
echo "   npm run dev"
echo ""
echo -e "${YELLOW}🌐 访问地址：${NC}"
echo "  后台API文档: http://localhost:8080/api/doc.html"
echo "  小程序API:   http://localhost:8081/miniapp-api/doc.html"
echo "  管理后台:    http://localhost:3000"
echo "  MinIO控制台: http://localhost:9001"
echo ""
echo -e "${YELLOW}🔐 测试账号：${NC}"
echo "  管理员: admin / 123456"
echo ""
echo "========================================"
echo ""
