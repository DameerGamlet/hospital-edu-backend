# Используем официальный образ для Java
FROM openjdk:17-jdk-slim

WORKDIR /app

# Копируем JAR файл из build/libs в контейнер
COPY build/libs/eureka-server-0.0.1.jar /app/eureka-server.jar

# Указываем команду для запуска приложения
CMD ["java", "-Dmicrometer.metrics.use-jvm-only=true", "-jar", "/app/eureka-server.jar"]

