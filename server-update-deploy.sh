#!/bin/bash

# 更新部署脚本 - 用于拉取最新代码并更新前后端服务

set -e

echo "===== 开始更新 DragonVVCR 项目 ====="

# 设置环境变量（可根据实际情况修改）
export MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-root}
export REDIS_PASSWORD=${REDIS_PASSWORD:-admin}

# 进入项目目录
cd /opt/projects/DragonVVCR

echo "===== 1. 拉取最新代码 ====="
git pull origin main

echo "===== 2. 检查网络是否存在 ====="
# 检查网络是否存在，不存在则创建
if ! docker network ls | grep -q dragonvvcr-network; then
    echo "创建dragonvvcr-network网络..."
    docker network create dragonvvcr-network
fi

echo "===== 3. 仅更新前后端服务 ====="
# 停止并删除前后端容器，但保留数据库和缓存容器
docker stop dragonvvcr-backend dragonvvcr-frontend || true
docker rm dragonvvcr-backend dragonvvcr-frontend || true

echo "===== 4. 构建并启动更新的前后端服务 ====="
# 仅构建并启动前后端服务
docker compose up --build -d backend frontend

echo "===== 5. 等待服务启动 ====="
# 等待服务启动
sleep 30

echo "===== 6. 检查服务状态 ====="
# 检查服务状态
docker compose ps

echo "===== 更新部署完成 ====="
echo "后端API访问地址: http://localhost:8088/dnvvcr"
echo "前端页面访问地址: http://localhost"
