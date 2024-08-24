# Task Manager API

## Описание

Task Manager API — это приложение для управления задачами, позволяющее пользователям регистрироваться, аутентифицироваться, создавать и управлять задачами, а также добавлять комментарии к ним.

## Возможности

- Регистрация нового пользователя
- Аутентификация пользователя
- Управление задачами (создание, обновление, удаление, получение задач)
- Добавление комментариев к задачам
- Назначение задач пользователям
- Просмотр задач и комментариев

## Требования

- Java 17
- Maven
- PostgreSQL или другая поддерживаемая база данных

## Запуск приложения

### Локальная разработка

1. Клонируйте репозиторий:

    ```bash
    git clone https://github.com/your-repo/task-manager-api.git
    ```

2. Перейдите в директорию проекта:

    ```bash
    cd task-manager-api
    ```

3. Настройте файл `application.yml` (описание ниже).

4. Запустите приложение с помощью Maven:

    ```bash
    ./mvnw spring-boot:run
    ```

## Конфигурация

### application.yml

Вам нужно настроить файл `application.yml`, чтобы установить параметры подключения к базе данных и другие параметры приложения. Вот пример настройки:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/task_manager
    username: your_db_username
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  security:
    jwt:
      secret: your_jwt_secret
      expiration: 3600
  server:
    port: 8080
