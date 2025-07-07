# 🏥 Hospital Management System

**Hospital System** — сервис записи к врачу, реализованный на Kotlin и Spring Boot.  
Проект демонстрирует навыки работы с REST API, работы с базами данных и тестирования.

---

## 🌟 Key Features

### 📅 Appointment Management
- Создание, обновление и удаление записей
- Два статуса записи: `FREE` и `BOOKED`
- Автоматические напоминания пациентам:
    - ⏰ За 24 часа до приема
    - 🔔 За 2 часа до приема

### 👨‍⚕️ User Management
- Полноценный CRUD для врачей и пациентов
- Каскадное удаление зависимых сущностей
- Валидация входных данных

### 🛠️ Technical Stack
- **OpenAPI 3.0** документация (Swagger UI)
- Интеграционные тесты с:
    - `MockMvc`
    - `TestContainers`
    - `WireMock`
- Миграции через **Liquibase**
- Работа с **PostgreSQL** через **JDBC**

---

## 🏗️ Project Structure

```plaintext
src/
├── main/
│   ├── kotlin/org/example/hospital/
│   │   ├── core/                  # Core business logic
│   │   │   ├── appointment/       # Appointment management
│   │   │   ├── doctor/            # Doctor operations
│   │   │   ├── patient/           # Patient operations
│   │   │   ├── configuration/     # Spring configurations
│   │   │   ├── advise/            # Exception handlers
│   │   │   └── exception/         # Custom exceptions
│   │   └── notifier/              # Notification service integration
│   └── resources/
│       ├── application.yml        # Configuration with ENV support
│       ├── db.changelog/          # Liquibase migrations
│       └── swagger.yaml           # OpenAPI specification
└── test/                          # Comprehensive test suite
```

## 🚀 Запуск проекта

1. Соберите проект с помощью Gradle:  
```bash
./gradlew clean build
```

2. Запустите необходимые сервисы через Docker:
```bash
docker-compose up -d
```

3. Запустите сгенерированный JAR-файл:
```bash
java -jar build/libs/testGaz-0.0.1-SNAPSHOT.jar
```

## 📚 Документация API

После запуска сервиса документация OpenAPI (Swagger UI) будет доступна по адресу:

```plaintext
http://localhost:8080/swagger-ui.html