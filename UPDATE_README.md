# DragonVVCR 项目更新指南

本指南说明如何使用新的更新系统来更新DragonVVCR项目的前后端代码，而不影响Redis和MySQL容器。

## 文件说明

- `docker-compose-update.yml`: 用于更新前后端服务的Docker Compose配置文件
- `update.sh`: 自动化更新脚本
- `Dockerfile.prod`: 生产环境优化的Dockerfile

## 更新流程

### 1. 准备工作

确保服务器上已安装以下软件：
- Docker
- Git

确保项目目录结构为：
```
/opt/projects/DragonVVCR/
├── DragonVVCR-backend/
├── DragonVVCR-frontend/
```

### 2. 设置环境变量

可以通过环境变量设置数据库和Redis密码：

```bash
export MYSQL_ROOT_PASSWORD=your_secure_mysql_password
export REDIS_PASSWORD=your_secure_redis_password
```

如果不设置，将使用默认值：
- MySQL密码: root
- Redis密码: admin

### 3. 执行更新

将`update.sh`上传到服务器，然后执行：

```bash
chmod +x update.sh
./update.sh
```

### 4. 验证更新

更新完成后，可以通过以下地址访问应用：
- 后端API: http://服务器IP:8088/dnvvcr
- 前端页面: http://服务器IP

## 手动更新步骤

如果需要手动执行更新过程，可以按照以下步骤：

### 1. 拉取最新代码并构建

```bash
cd /opt/projects/DragonVVCR
git pull origin main
cd DragonVVCR-backend
mvn clean package -DskipTests
cd ../DragonVVCR-frontend
npm install
npm run build
```

### 2. 更新前后端服务

```bash
cd /opt/projects/DragonVVCR/DragonVVCR-backend

# 停止现有前后端容器
docker stop dragonvvcr-backend dragonvvcr-frontend || true
docker rm dragonvvcr-backend dragonvvcr-frontend || true

# 启动新的前后端服务
docker compose -f docker-compose-update.yml --profile update up -d
```

### 3. 查看服务状态

```bash
docker compose -f docker-compose-update.yml --profile update ps
```

## 与完整部署的区别

- **完整部署** (docker-compose.yml): 构建并启动所有服务，包括MySQL、Redis、前端和后端
- **更新部署** (docker compose update): 只更新前后端代码和容器，不重建MySQL和Redis容器

## 常见问题

### 1. 网络连接问题

如果遇到网络连接问题，确保`dragonvvcr-network`网络存在：

```bash
docker network ls | grep dragonvvcr-network
```

如果不存在，可以手动创建：

```bash
docker network create dragonvvcr-network
```

### 2. 构建失败

检查Maven和NPM构建日志，可能是依赖问题或代码编译错误。

### 3. 服务启动失败

使用以下命令查看日志：
```bash
docker logs dragonvvcr-backend
docker logs dragonvvcr-frontend
```

### 4. 数据库连接问题

检查后端服务是否能连接到MySQL容器：
```bash
docker exec -it dragonvvcr-backend ping mysql
```

### 5. 前端页面无法访问

检查Nginx配置和前端构建输出：
```bash
docker exec -it dragonvvcr-frontend ls /usr/share/nginx/html
```

## 高级配置

### 1. 自定义端口

如需修改端口映射，可以编辑`docker-compose-update.yml`文件中的`ports`配置。

### 2. 持续集成

可以将此更新脚本集成到CI/CD流水线中，实现自动化部署。

### 3. 回滚操作

如需回滚到上一版本，可以：

```bash
cd /opt/projects/DragonVVCR
git checkout HEAD~1
./update.sh
```
