FROM openjdk:17-jre-slim

WORKDIR /app

# 添加时区支持
RUN apk add --no-cache tzdata

# 设置JVM参数
ENV JAVA_OPTS="-Xmx512m -Xms256m"

COPY build/libs/*.jar app.jar

EXPOSE 8080

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
