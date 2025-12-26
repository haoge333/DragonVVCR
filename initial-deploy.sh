#!/bin/bash

# 初次部署脚本 - 用于构建并启动所有服务（前端、后端、MySQL和Redis）

set -e

echo "===== 开始初次部署 DragonVVCR 项目 ====="

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

echo "===== 2. 停止可能存在的服务 ====="
# 停止可能存在的服务
docker compose down || true

echo "===== 3. 构建并启动所有服务 ====="
# 构建并启动所有服务（包括前端、后端、MySQL和Redis）
docker compose up --build -d

echo "===== 4. 等待服务启动 ====="
# 等待服务启动
sleep 30

echo "===== 5. 检查服务状态 ====="
# 检查服务状态
docker compose ps

echo "===== 初次部署完成 ====="
echo "后端API访问地址: http://localhost:8088/dnvvcr"
echo "前端页面访问地址: http://localhost"
echo "MySQL访问地址: localhost:3306"
echo "Redis访问地址: localhost:6379"
