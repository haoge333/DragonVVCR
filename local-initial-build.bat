@echo off
chcp 65001 > nul
echo ===== 开始初次部署 DragonVVCR 项目 =====
echo 当前目录: %cd%

echo 1. 检查Docker网络是否存在...
docker network ls | findstr dragonvvcr-network > nul
if %errorlevel% neq 0 (
    echo 创建dragonvvcr-network网络...
    docker network create dragonvvcr-network
)

echo 2. 进入后端项目执行mvn clean package命令...
cd DragonVVCR-backend
echo 后端项目目录: %cd%
call mvn clean package -DskipTests
set MVN_EXIT_CODE=%errorlevel%
echo Maven命令退出码: %MVN_EXIT_CODE%
echo 后端项目构建完成!
cd ..
echo 返回到根目录: %cd%

echo 3. 进入前端项目重新构建...
echo 当前目录: %cd%
cd DragonVVCR-frontend
echo 前端项目目录: %cd%
call npm run build
set NPM_EXIT_CODE=%errorlevel%
echo NPM构建命令退出码: %NPM_EXIT_CODE%
if %NPM_EXIT_CODE% neq 0 (
    echo 前端项目构建失败!
    pause
    exit /b 1
)
echo 前端项目构建完成!

echo 4. 停止并删除现有容器，然后重新构建并启动所有服务...
cd ..
echo 根目录: %cd%
docker-compose down
set DOCKER_DOWN_EXIT_CODE=%errorlevel%
echo Docker Down命令退出码: %DOCKER_DOWN_EXIT_CODE%
if %DOCKER_DOWN_EXIT_CODE% neq 0 (
    echo Docker容器停止失败!
    pause
    exit /b 1
)

docker-compose up --build -d
set DOCKER_UP_EXIT_CODE=%errorlevel%
echo Docker Up命令退出码: %DOCKER_UP_EXIT_CODE%
if %DOCKER_UP_EXIT_CODE% neq 0 (
    echo Docker容器构建并启动失败!
    pause
    exit /b 1
)

echo 5. 检查服务状态...
docker-compose ps

echo ===== 初次部署完成 =====
echo 后端API访问地址: http://localhost:8088/dnvvcr
echo 前端页面访问地址: http://localhost
echo MySQL访问地址: localhost:3306
echo Redis访问地址: localhost:6379
pause
