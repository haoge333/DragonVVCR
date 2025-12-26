# 构建后端运行环境
FROM openjdk:17-jdk-slim

# 更换为国内镜像源并安装必要的工具
RUN sed -i 's/deb.debian.org/mirrors.aliyun.com/g' /etc/apt/sources.list &&     apt-get update && apt-get install -y     wget     default-mysql-client     redis-tools     curl     tzdata     && rm -rf /var/lib/apt/lists/*

# 设置工作目录
WORKDIR /app

# 复制打包好的jar文件到容器中
COPY target/dragonvvcr-0.0.1-SNAPSHOT.jar app.jar

# ===== 关键修改2：启动脚本使用环境变量，避免硬编码 =====
# 创建启动脚本（使用环境变量替代硬编码的密码）
RUN echo '#!/bin/bash\n# 等待MySQL服务启动\necho "Waiting for MySQL to start..."\nuntil mysql -h ${MYSQL_HOST:-mysql} -u ${MYSQL_USER:-root} -p${MYSQL_PASSWORD:-root} -e "SELECT 1"; do\n  echo "MySQL not ready, waiting..."\n  sleep 2\ndone\necho "MySQL is up and running!"\n\n# 等待Redis服务启动\necho "Waiting for Redis to start..."\nuntil redis-cli -h ${REDIS_HOST:-redis} -p ${REDIS_PORT:-6379} -a ${REDIS_PASSWORD:-admin} ping; do\n  echo "Redis not ready, waiting..."\n  sleep 2\ndone\necho "Redis is up and running!"\n\n# 启动应用（保留user.timezone确保Java时区正确）\necho "Starting the application..."\njava -Duser.timezone=Asia/Shanghai -jar -Dserver.servlet.context-path=/dnvvcr app.jar' > start-all.sh

# 使脚本可执行
RUN chmod +x start-all.sh

# 设置环境变量，指定使用docker配置文件
ENV SPRING_PROFILES_ACTIVE=docker

# 暴露应用端口
EXPOSE 8088

# 运行启动脚本
ENTRYPOINT ["./start-all.sh"]
