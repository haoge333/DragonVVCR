#!/bin/bash

# 更新脚本 - 用于拉取最新代码、构建项目并更新前后端容器

set -e

echo "===== 开始更新 DragonVVCR 项目 ====="

# 设置环境变量（可根据实际情况修改）
export MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD:-root}
export REDIS_PASSWORD=${REDIS_PASSWORD:-admin}

# 进入项目目录
cd /opt/projects/DragonVVCR

echo "===== 1. 检查网络是否存在 ====="
# 检查网络是否存在，不存在则创建
if ! docker network ls | grep -q dragonvvcr-network; then
    echo "创建dragonvvcr-network网络..."
    docker network create dragonvvcr-network
fi

echo "===== 2. 拉取最新代码并构建项目 ====="
# 使用Docker Compose的build profile执行构建
docker-compose -f docker-compose-update.yml --profile build up --build builder

echo "===== 3. 停止现有的前后端服务 ====="
# 停止并删除现有前后端容器
docker stop dragonvvcr-backend dragonvvcr-frontend || true
docker rm dragonvvcr-backend dragonvvcr-frontend || true

echo "===== 4. 启动新的前后端服务 ====="
# 启动更新后的前后端服务
docker-compose -f docker-compose-update.yml --profile update up -d

echo "===== 5. 等待服务启动 ====="
# 等待服务启动
sleep 30

echo "===== 6. 检查服务状态 ====="
# 检查服务状态
docker-compose -f docker-compose-update.yml --profile update ps

echo "===== 更新完成 ====="
echo "后端API访问地址: http://localhost:8088/dnvvcr"
echo "前端页面访问地址: http://localhost"
