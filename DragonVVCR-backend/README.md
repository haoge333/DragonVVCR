# DragonVVCR 项目

这是一个集成了 Redis 和 MySQL 的全栈示例项目，包含 Spring Boot 后端和 Vue.js 前端。

## 项目结构

```
your-project/
├── backend/        # Java Spring Boot 后端代码
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── build.gradle
│   └── gradle/
├── frontend/       # Vue.js 前端代码
│   ├── src/
│   │   ├── components/
│   │   ├── router/
│   │   ├── services/
│   │   └── views/
│   ├── public/
│   └── package.json
├── docker/         # Docker 相关文件
│   ├── Dockerfile
│   └── docker-compose.yml
├── docs/           # 项目文档
└── README.md       # 项目说明
```

## 技术栈

### 后端
- Spring Boot
- Spring Data JPA
- Spring Data Redis
- MySQL 8.0
- Redis 7
- Lombok

### 前端
- Vue.js
- Vue Router
- Axios

### 部署
- Docker

## 快速开始

1. 启动 MySQL 和 Redis 服务：

```bash
cd docker
docker compose up -d
```

2. 启动后端服务：

```bash
cd backend
./gradlew bootRun
```

3. 启动前端服务：

```bash
cd frontend
npm install
npm run serve
```

## API 接口

- 获取用户信息：`GET /api/users/{id}`
- 创建用户：`POST /api/users`
- 更新用户：`PUT /api/users/{id}`
- 删除用户：`DELETE /api/users/{id}`

## 示例请求

创建用户：
```bash
curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '{"username": "testuser", "email": "test@example.com"}'
```

获取用户：
```bash
curl http://localhost:8080/api/users/1
```

更新用户：
```bash
curl -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d '{"username": "updateduser", "email": "updated@example.com"}'
```

删除用户：
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## 配置说明

### MySQL 配置

- 主机：localhost
- 端口：3306
- 数据库：demo
- 用户名：demo
- 密码：demo

### Redis 配置

- 主机：localhost
- 端口：6379

## 开发指南

详细的开发指南请参考 [docs/development.md](docs/development.md)
