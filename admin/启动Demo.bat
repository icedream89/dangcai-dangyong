@echo off
chcp 65001 >nul
echo ========================================
echo   当才当用 - 管理后台Demo启动器
echo ========================================
echo.

echo [1/3] 检查Node.js环境...
node -v >nul 2>&1
if errorlevel 1 (
    echo ❌ 未检测到Node.js！
    echo.
    echo 请先安装Node.js：
    echo 1. 访问 https://nodejs.org/
    echo 2. 下载并安装LTS版本
    echo 3. 安装完成后重新运行此脚本
    echo.
    pause
    exit /b 1
)
echo ✓ Node.js已安装
node -v
echo.

echo [2/3] 检查并安装依赖...
if not exist "node_modules\" (
    echo 正在安装依赖，请稍候...
    echo 这可能需要几分钟，请耐心等待...
    echo.
    call npm install
    if errorlevel 1 (
        echo ❌ 依赖安装失败！
        echo.
        echo 可能的解决方案：
        echo 1. 检查网络连接
        echo 2. 使用国内镜像：npm config set registry https://registry.npmmirror.com
        echo 3. 删除node_modules文件夹后重试
        echo.
        pause
        exit /b 1
    )
    echo ✓ 依赖安装完成
) else (
    echo ✓ 依赖已存在
)
echo.

echo [3/3] 启动开发服务器...
echo.
echo ========================================
echo   系统即将启动，请稍候...
echo   访问地址: http://localhost:3000
echo   演示账号: admin / 123456
echo ========================================
echo.
echo 按任意键打开浏览器，或手动访问上述地址...
pause >nul

start http://localhost:3000
echo.
echo 正在启动服务器...
echo (按 Ctrl+C 可停止服务器)
echo.

npm run dev
