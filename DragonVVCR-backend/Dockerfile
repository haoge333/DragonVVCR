# 构建后端运行环境
FROM openjdk:17-jdk-slim

# 安装必要的工具
RUN apt-get update && apt-get install -y     wget     default-mysql-client     redis-tools     curl     && rm -rf /var/lib/apt/lists/*

# 设置工作目录
WORKDIR /app

# 复制打包好的jar文件到容器中
COPY target/dragonvvcr-0.0.1-SNAPSHOT.jar app.jar

# 不再需要前端构建产物

# 不再需要nginx配置
# 不再需要nginx配置命令\n    listen 80;\n    server_name localhost;\n    root /usr/share/nginx/html;\n    index index.html;\n\n    # 处理前端路由\n    location / {\n        try_files $uri $uri/ /index.html;\n    }\n\n    # 代理后端API\n    location /dnvvcr/ {\n        proxy_pass http://localhost:8088;\n        proxy_set_header Host $host;\n        proxy_set_header X-Real-IP $remote_addr;\n        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;\n    }\n}' > /etc/nginx/conf.d/default.conf

# 创建启动脚本
RUN echo '#!/bin/bash\n# 启动nginx\nnginx -g "daemon off;" &\n\n# 等待MySQL服务启动\necho "Waiting for MySQL to start..."\nuntil mysql -h mysql -u root -proot -e "SELECT 1"; do\n  echo "MySQL not ready, waiting..."\n  sleep 2\ndone\necho "MySQL is up and running!"\n\n# 等待Redis服务启动\necho "Waiting for Redis to start..."\nuntil redis-cli -h redis -p 6379 -a admin ping; do\n  echo "Redis not ready, waiting..."\n  sleep 2\ndone\necho "Redis is up and running!"\n\n# 启动应用\necho "Starting the application..."\njava -jar -Dserver.servlet.context-path=/dnvvcr app.jar' > start-all.sh

# 使脚本可执行
RUN chmod +x start-all.sh

# 设置环境变量，指定使用docker配置文件
ENV SPRING_PROFILES_ACTIVE=docker

# 暴露应用端口
EXPOSE 80 8088

# 运行启动脚本
ENTRYPOINT ["./start-all.sh"]
