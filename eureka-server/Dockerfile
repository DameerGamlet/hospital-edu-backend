# Используем официальный образ для Java
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем JAR-файл приложения в контейнер
COPY target/eureka-server-0.0.1.jar eureka-server.jar

# Открываем порт, который будет использовать Eureka
EXPOSE 8761

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
