#!/bin/bash

# 服务器端更新脚本 - 用于拉取最新代码、构建项目并更新前后端容器

set -e

echo "===== 开始更新 DragonVVCR 项目 ====="

# 设置环境变量（可根据实际情况修改）
export MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-root}
export REDIS_PASSWORD=${REDIS_PASSWORD:-admin}

# 进入项目目录
cd /opt/projects/DragonVVCR

echo "===== 1. 拉取最新代码 ====="
git pull origin master

echo "===== 2. 检查网络是否存在 ====="
# 检查网络是否存在，不存在则创建
if ! docker network ls | grep -q dragonvvcr-network; then
    echo "创建dragonvvcr-network网络..."
    docker network create dragonvvcr-network
fi

echo "===== 3. 停止现有服务 ====="
# 停止并删除现有容器
docker compose down || true

echo "===== 4. 构建并启动服务 ====="
# 构建并启动服务
docker compose up --build -d

echo "===== 5. 等待服务启动 ====="
# 等待服务启动
sleep 30

echo "===== 6. 检查服务状态 ====="
# 检查服务状态
docker compose ps

echo "===== 更新完成 ====="
echo "后端API访问地址: http://localhost:8088/dnvvcr"
echo "前端页面访问地址: http://localhost"
