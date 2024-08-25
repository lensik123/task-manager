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

1. Скачайте или клонируйте репозиторий.

2. Настройте файл `application.yml` (описание ниже).

3. Запустите приложение с помощью Maven:

    ```bash
    ./mvnw spring-boot:run
    ```

## Конфигурация

### application.yml

Вам нужно настроить файл `application.yml`, чтобы установить параметры подключения к базе данных и другие параметры приложения. Вот пример настройки:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db_name
    username: your_db_username
    password: your_db_password
    driver-class-name: org.postgresql.Driver
 jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect
  profiles:
    active: default
  jwt:
    secret: your_secret_jwt_key




