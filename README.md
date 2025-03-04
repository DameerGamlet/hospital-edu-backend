# Проект "hospital-edu"

## Описание проекта "hospital-edu"

**"hospital-edu"** — это веб-приложение для организации записи пациентов на прием к врачам в сети больниц и филиалов.
Цель проекта — упростить процесс взаимодействия пациентов с медицинскими учреждениями, сократить время ожидания записи,
а также оптимизировать работу врачей и администраторов клиники.

### Основные возможности:

1. **Для пациентов**:
    - Поиск ближайшего филиала больницы.
    - Выбор нужного отделения (хирургия, педиатрия, терапия и т. д.).
    - Просмотр списка специалистов с их доступным расписанием.
    - Оформление записи на прием, указав дату, время и жалобы.
    - Получение подтверждения записи и напоминаний.

2. **Для врачей**:
    - Получение уведомлений о новых записях (через email и систему уведомлений).
    - Просмотр и управление своим расписанием.
    - Возможность добавлять комментарии к записям или переносить их.

3. **Для администраторов**:
    - Управление данными о филиалах, врачах и расписаниях.
    - Просмотр статистики загрузки врачей и записей пациентов.

### Польза:

- **Для пациентов**: прозрачность расписания и удобство записи, экономия времени на походы в клинику.
- **Для врачей**: эффективное управление своим временем и минимизация накладных задач.
- **Для администраторов больницы**: улучшение организации процесса записи и взаимодействия с пациентами.

Превью проекта:

ПРЕВЬЮ_1

ПРЕВЬЮ_2

ПРЕВЬЮ_3

## Стек технологий

### Frontend:

- React
- Redux Toolkit
- React Router
- Material-UI
- Axios
- React Query

### Backend:

- Spring Boot
- Spring Data JPA
- Spring Data MongoDB
- Spring Security
- Spring WebSockets
- MapStruct

### База данных:

- PostgreSQL
- ClickHouse
- MongoDB

### Очереди сообщений: Kafka

### Кеширование: Redis

### DevOps:

- Docker
- Kubernetes
- Nginx
- Jenkins/GitHub Actions
- Prometheus + Grafana

### Подробный стек технологий для проекта "hospital-edu"

#### Backend (на основе Spring)

1. **Spring Boot**: основной фреймворк для создания REST API.
    - Обрабатывает запросы, маршрутизирует их, валидирует данные, взаимодействует с базой данных (MongoDB, ClickHouse) и
      очередями (Kafka).

2. **Spring Data MongoDB**: для работы с MongoDB.
    - Используется для управления данными о пользователях, врачах, расписаниях и записях.

3. **Spring JDBC (или ClickHouse JDBC)**: для взаимодействия с ClickHouse.
    - Используется для выполнения аналитических запросов и работы с временными рядами.

4. **Spring Security**: для аутентификации и авторизации.
    - Защищает эндпоинты и данные пользователей.

5. **Spring for Apache Kafka**:
    - Обеспечивает интеграцию с Kafka для отправки сообщений и обработки задач (уведомления врачей, аналитика).

6. **Spring WebSocket**: для уведомлений в реальном времени.
    - Например, уведомления врачам об изменении их расписания.

7. **Spring Mail**: для отправки писем врачам.
    - Используется для уведомления о новых записях.

8. **MapStruct**: для преобразования объектов (DTO ↔ Entity).
    - Упрощает обработку данных между слоями.

#### Frontend (на основе React)

1. **React**: основной фреймворк для пользовательского интерфейса.
    - Динамическая работа сайта, отображение данных о врачах, расписаниях.

2. **Redux Toolkit**: для управления состоянием.
    - Управляет выбором филиала, врача, расписания.

3. **React Query/Axios**: для работы с API.
    - Обеспечивает взаимодействие с бэкендом.

4. **Material-UI (MUI)**: для стилизации интерфейса.
    - Современный пользовательский интерфейс.

5. **React Router**: для маршрутизации.
    - Навигация между страницами (выбор филиала, врача, подтверждение записи).

#### База данных

1. **MongoDB**:
    - Основная база данных для хранения динамических данных:
        - Информация о пользователях, врачах, расписаниях и записях.
        - Гибкость работы с JSON-документами.

2. **ClickHouse**:
    - Используется для аналитики:
        - Хранение исторических данных (логов записей).
        - Генерация отчетов о загруженности филиалов, врачей, временных рядах данных.

#### Очереди сообщений

1. **Apache Kafka**:
    - Обеспечивает асинхронное выполнение задач:
        - Уведомления врачей через email или WebSocket.
        - Потоки аналитических данных:
            - Тема `appointments`: обработка записей на прием.
            - Тема `notifications`: уведомления врачей.
            - Тема `analytics`: данные для ClickHouse.

#### Кеширование

1. **Redis**:
    - Используется для кеширования данных о расписаниях, доступных для записи.
    - Хранит временные данные, такие как токены подтверждения записей.

#### DevOps

1. **Docker**:
    - Контейнеризация всех сервисов (бэкэнд, фронтэнд, MongoDB, ClickHouse, Kafka, Redis).

2. **Docker Compose**:
    - Настройка и запуск всех контейнеров.

3. **Jib**:
    - Генерация Docker-образов прямо из Java-проекта.

4. **Kubernetes (K8s)**:
    - Оркестрация контейнеров для масштабирования.

5. **GitHub Actions**:
    - CI/CD: автоматическая сборка и деплой приложения.

6. **Nginx**:
    - Обратный прокси для маршрутизации запросов между фронтендом и бэкэндом.

7. **Prometheus и Grafana**:
    - Мониторинг системы и визуализация метрик.

---

## Шаги реализации

1. План разработки MVP: [mvp-release.md](resources/mvp-release.md)