# 使用你本地的 openjdk 26 镜像
FROM openjdk:26-trixie

# 设置工作目录
WORKDIR /app

# 复制打包好的 jar
COPY target/my-spring-boot-project-0.0.1-SNAPSHOT.jar /app/app.jar

# 暴露端口
EXPOSE 8080

# 启动命令
ENTRYPOINT ["java", "-jar", "app.jar"]
