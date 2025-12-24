# 使用多阶段构建
# 第一阶段：构建应用
FROM node:22-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json (如果存在)
COPY package*.json ./

# 安装所有依赖（包括 devDependencies）
RUN npm install

# 复制项目文件
COPY . .

# 构建应用
RUN npm run build

# 第二阶段：使用 Nginx 提供静态文件服务
FROM nginx:stable-alpine

# 从构建阶段复制构建产物到 nginx 的默认目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制自定义的 nginx 配置文件
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80

# 启动 nginx
CMD ["nginx", "-g", "daemon off;"]
