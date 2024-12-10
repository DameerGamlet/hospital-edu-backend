# Пошаговый план реализации MVP с чекбоксами

## Шаг 1: Настройка проекта и инфраструктуры

### Репозиторий и структура
- [ ] Создать monorepo репозиторий на GitHub.
- [ ] Настроить структуру папок:
  ```
  hospital-edu/
  ├── services/
  │   ├── user-service/
  │   ├── appointment-service/
  │   └── notification-service/
  ├── frontend/
  ├── docker-compose.yml
  └── README.md
  ```
- [ ] Добавить корневой `pom.xml` для управления модулями.

### Docker Compose
- [ ] Добавить базовый `docker-compose.yml`:
    - MongoDB.
    - PostgreSQL.
    - Kafka.
    - Maildev.
- [ ] Проверить запуск инфраструктурных сервисов через `docker-compose up`.

---

## Шаг 2: Реализация `user-service`

### Создание сервиса
- [ ] Создать папку `services/user-service`.
- [ ] Инициализировать проект Spring Boot через Spring Initializr.
- [ ] Добавить зависимости: **Spring Web**, **Spring Data MongoDB**, **Lombok**, **DevTools**.

### Настройка MongoDB
- [ ] Добавить `application.properties` для подключения к MongoDB.
- [ ] Проверить подключение к базе через клиент MongoDB или лог сервиса.

### Логика сервиса
- [ ] Добавить базовую структуру папок:
  ```
  com.hospital.user
  ├── controller
  ├── service
  ├── repository
  └── model
  ```
- [ ] Создать модель пользователя `User` с полями: ID, имя, email, телефон.
- [ ] Написать интерфейс репозитория `UserRepository`.
- [ ] Создать сервисный класс `UserService` для работы с репозиторием.
- [ ] Добавить контроллер `UserController` с эндпоинтами:
    - [ ] **POST /users**: Создание нового пользователя.
    - [ ] **GET /users/{id}**: Получение информации о пользователе.

### Тестирование
- [ ] Тестировать API через Postman.
- [ ] Проверить, что данные сохраняются в MongoDB.

---

## Шаг 3: Реализация `appointment-service`

### Создание сервиса
- [ ] Создать папку `services/appointment-service`.
- [ ] Инициализировать проект Spring Boot через Spring Initializr.
- [ ] Добавить зависимости: **Spring Web**, **Spring Data JPA**, **Lombok**, **DevTools**.

### Настройка PostgreSQL
- [ ] Добавить `application.properties` для подключения к PostgreSQL.
- [ ] Проверить подключение к базе через лог сервиса.

### Логика сервиса
- [ ] Добавить базовую структуру папок:
  ```
  com.hospital.appointment
  ├── controller
  ├── service
  ├── repository
  └── model
  ```
- [ ] Создать модель записи `Appointment` с полями: ID, ID пользователя, ID врача, дата/время.
- [ ] Написать интерфейс репозитория `AppointmentRepository`.
- [ ] Создать сервисный класс `AppointmentService` для работы с репозиторием.
- [ ] Добавить контроллер `AppointmentController` с эндпоинтами:
    - [ ] **POST /appointments**: Создание записи на прием.
    - [ ] **GET /appointments/{id}**: Получение информации о записи.

### Связь с `user-service`
- [ ] Добавить HTTP-клиент для запросов к `user-service` (например, RestTemplate).
- [ ] Реализовать валидацию пользователя перед записью.

### Тестирование
- [ ] Тестировать API через Postman.
- [ ] Проверить, что данные сохраняются в PostgreSQL.

---

## Шаг 4: Реализация `notification-service`

### Создание сервиса
- [ ] Создать папку `services/notification-service`.
- [ ] Инициализировать проект Spring Boot через Spring Initializr.
- [ ] Добавить зависимости: **Spring Web**, **Spring Kafka**, **Spring Mail**, **Lombok**, **DevTools**.

### Настройка Kafka
- [ ] Добавить `application.properties` для подключения к Kafka.
- [ ] Проверить соединение с Kafka.

### Логика сервиса
- [ ] Добавить базовую структуру папок:
  ```
  com.hospital.notification
  ├── listener
  ├── service
  └── model
  ```
- [ ] Написать Kafka Listener для обработки сообщений о новых записях.
- [ ] Реализовать отправку email с использованием `JavaMailSender`.
- [ ] Настроить отправку через Maildev (SMTP).

### Тестирование
- [ ] Создать тестовый топик в Kafka для сообщений.
- [ ] Проверить отправку писем через Maildev.

---

## Шаг 5: Интеграция и настройка Docker Compose

### Интеграция сервисов
- [ ] Настроить `docker-compose.yml` для сборки всех сервисов.
- [ ] Добавить зависимости между сервисами (например, `user-service` зависит от MongoDB).

### Тестирование
- [ ] Поднять все контейнеры через `docker-compose up`.
- [ ] Проверить взаимодействие сервисов через Postman.

---

## Итог
После выполнения всех шагов MVP должно поддерживать:
- Регистрацию пользователя.
- Создание записи к врачу.
- Отправку уведомления врачу через Maildev.