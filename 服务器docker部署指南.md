# DragonVVCR 部署指南

## 概述
本指南介绍如何使用两个不同的脚本部署和更新DragonVVCR项目：
- `initial-deploy.sh`: 初次部署脚本，用于构建并启动所有服务
- `update-deploy.sh`: 更新部署脚本，用于拉取最新代码并更新前后端服务

## 前置条件
- Linux服务器（推荐CentOS 7+或Ubuntu 18+）
- Docker和Docker Compose已安装
- Git已安装
- 项目已克隆到`/opt/projects/DragonVVCR`目录

## 首次部署

### 1. 克隆项目
```bash
# 创建项目目录
mkdir -p /opt/projects
cd /opt/projects

# 克隆项目
git clone [你的Git仓库地址] DragonVVCR
cd DragonVVCR
```

### 2. 配置Docker镜像加速器（国内服务器推荐）
```bash
# 创建Docker配置目录
mkdir -p /etc/docker

# 备份现有配置（如果存在）
cp /etc/docker/daemon.json /etc/docker/daemon.json.bak 2>/dev/null || true

# 配置镜像加速器
cat > /etc/docker/daemon.json <<EOF
{
  "registry-mirrors": [
    "https://registry.docker-cn.com",
    "https://hub-mirror.c.163.com",
    "https://mirror.baidubce.com",
    "https://registry.cn-hangzhou.aliyuncs.com"
  ],
  "insecure-registries": [],
  "debug": false,
  "experimental": false
}
EOF

# 重新加载配置并重启Docker服务
systemctl daemon-reload
systemctl restart docker

# 验证配置是否生效
docker info | grep -A 10 "Registry Mirrors"
```

### 3. 执行初次部署
```bash
cd /opt/projects/DragonVVCR

# 确保脚本有执行权限
chmod +x server-initial-deploy.sh

# 执行初次部署脚本
./server-initial-deploy.sh
```

## 更新部署

### 1. 执行更新部署
```bash
cd /opt/projects/DragonVVCR

# 确保脚本有执行权限
chmod +x server-update-deploy.sh

# 执行更新部署脚本
./server-update-deploy.sh
```

## 脚本说明

### initial-deploy.sh
- 用途：首次部署项目
- 操作：
  1. 检查并创建Docker网络
  2. 停止可能存在的服务
  3. 构建并启动所有服务（前端、后端、MySQL和Redis）
  4. 等待服务启动并检查状态

### update-deploy.sh
- 用途：更新前后端代码
- 操作：
  1. 拉取最新代码
  2. 检查并创建Docker网络
  3. 停止并删除前后端容器（保留数据库和缓存容器）
  4. 仅构建并启动更新的前后端服务
  5. 等待服务启动并检查状态

## 环境变量
两个脚本都支持以下环境变量：
- `MYSQL_ROOT_PASSWORD`: MySQL root密码（默认：root）
- `REDIS_PASSWORD`: Redis密码（默认：admin）

可以通过以下方式设置：
```bash
export MYSQL_ROOT_PASSWORD=your_password
export REDIS_PASSWORD=your_redis_password
./server-initial-deploy.sh  # 或 ./server-update-deploy.sh
```

## 访问应用
部署成功后，可以通过以下地址访问：
- 前端页面：http://服务器IP
- 后端API：http://服务器IP:8088/dnvvcr
- MySQL：服务器IP:3306
- Redis：服务器IP:6379

## 故障排除
1. 如果构建失败，检查Docker和Docker Compose版本
2. 如果网络问题，确保已正确配置Docker镜像加速器
3. 查看日志：`docker compose logs -f [服务名称]`
4. 如果数据库初始化失败，可能需要手动删除数据卷并重新初始化：
   ```bash
   docker compose down -v
   ./server-initial-deploy.sh
   ```
