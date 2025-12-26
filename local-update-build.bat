@echo off
chcp 65001 > nul
echo ===== 开始更新 DragonVVCR 项目 =====
echo 当前目录: %cd%

echo 1. 进入后端项目执行mvn clean package命令...
cd DragonVVCR-backend
echo 后端项目目录: %cd%
call mvn clean package -DskipTests
set MVN_EXIT_CODE=%errorlevel%
echo Maven命令退出码: %MVN_EXIT_CODE%
echo 后端项目构建完成!
cd ..
echo 返回到根目录: %cd%

echo 2. 进入前端项目重新构建...
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

echo 3. 仅更新前后端服务，保留数据库和缓存...
cd ..
echo 根目录: %cd%
echo 停止前后端容器...
docker stop dragonvvcr-backend dragonvvcr-frontend 2>nul
echo 删除前后端容器...
docker rm dragonvvcr-backend dragonvvcr-frontend 2>nul

echo 4. 构建并启动更新的前后端服务...
docker-compose up --build -d backend frontend
set DOCKER_UP_EXIT_CODE=%errorlevel%
echo Docker Up命令退出码: %DOCKER_UP_EXIT_CODE%
if %DOCKER_UP_EXIT_CODE% neq 0 (
    echo Docker容器构建并启动失败!
    pause
    exit /b 1
)

echo 5. 等待服务启动...
timeout /t 30 /nobreak

echo 6. 检查服务状态...
docker-compose ps

echo ===== 更新部署完成 =====
echo 后端API访问地址: http://localhost:8088/dnvvcr
echo 前端页面访问地址: http://localhost
pause
