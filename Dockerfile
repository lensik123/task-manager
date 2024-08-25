# Используем официальный образ Java
FROM openjdk:17

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем jar файл из target директории в контейнер
COPY target/task-manager-0.0.1-SNAPSHOT.jar /app/app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
