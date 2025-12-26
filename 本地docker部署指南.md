# 本地部署指南

## 概述
本指南介绍如何在本地Windows环境中使用两个不同的脚本部署和更新DragonVVCR项目：
- `local-initial-build.bat`: 本地初次部署脚本，用于构建并启动所有服务
- `local-update-build.bat`: 本地更新部署脚本，用于更新前后端服务

## 前置条件
- Windows 10/11
- Docker Desktop已安装并运行
- JDK 8+已安装
- Maven已安装
- Node.js和npm已安装
- 项目已克隆到本地目录

## 首次部署

### 1. 克隆项目
```bash
# 克隆项目到本地目录
git clone [你的Git仓库地址]
cd DragonVVCR
```

### 2. 执行初次部署
```bash
# 双击运行或在命令行中执行
local-initial-build.bat
```

## 更新部署

### 1. 执行更新部署
```bash
# 双击运行或在命令行中执行
local-update-build.bat
```

## 脚本说明

### local-initial-build.bat
- 用途：首次本地部署项目
- 操作：
  1. 检查并创建Docker网络
  2. 构建后端项目（使用Maven）
  3. 构建前端项目（使用npm）
  4. 停止可能存在的服务
  5. 构建并启动所有服务（前端、后端、MySQL和Redis）
  6. 等待服务启动并检查状态

### local-update-build.bat
- 用途：更新本地前后端代码
- 操作：
  1. 构建后端项目（使用Maven）
  2. 构建前端项目（使用npm）
  3. 停止并删除前后端容器（保留数据库和缓存容器）
  4. 仅构建并启动更新的前后端服务
  5. 等待服务启动并检查状态

### local-windows-build.bat（原有脚本）
- 用途：通用构建脚本（功能与local-update-build.bat类似）
- 操作：
  1. 构建后端项目（使用Maven）
  2. 构建前端项目（使用npm）
  3. 停止所有容器
  4. 重新构建并启动所有容器

## 访问应用
部署成功后，可以通过以下地址访问：
- 前端页面：http://localhost
- 后端API：http://localhost:8088/dnvvcr
- MySQL：localhost:3306
- Redis：localhost:6379

## 故障排除
1. 如果Maven构建失败，检查JDK和Maven环境变量
2. 如果npm构建失败，检查Node.js和npm版本
3. 如果Docker命令失败，确保Docker Desktop正在运行
4. 查看日志：`docker-compose logs -f [服务名称]`
5. 如果数据库初始化失败，可能需要手动删除数据卷并重新初始化：
   ```bash
   docker-compose down -v
   local-initial-build.bat
   ```

## 脚本选择建议

- **首次部署**：使用`local-initial-build.bat`
- **日常开发更新**：使用`local-update-build.bat`
- **通用场景**：可以继续使用原有的`local-windows-build.bat`

这三个脚本都能完成本地部署，但`local-initial-build.bat`和`local-update-build.bat`提供了更明确的场景区分，使部署流程更加清晰。
