С учетом вашей текущей конфигурации `docker-compose`, вот доработанный и подробный пример реализации `user-service` и `notification-service` для сценария отправки подтверждающего кода на email через Kafka. Мы будем использовать вашу инфраструктуру (MongoDB, PostgreSQL, Kafka, MailDev).

---

## 1. Дополнение `docker-compose.yml`

### Экспорт переменных среды
Для упрощения управления портами и переменными среды добавьте файл `.env`:
```env
MONGO_PORT=27017
POSTGRES_PORT=5432
ZOOKEEPER_PORT=2181
KAFKA_PORT=9092
MAILDEV_WEB_PORT=1080
MAILDEV_SMTP_PORT=1025
```

---

## 2. Реализация `user-service`

`user-service` будет взаимодействовать с PostgreSQL для хранения пользователей и публиковать события в Kafka.

### 2.1 Gradle-зависимости
Обновите `build.gradle`:
```groovy
plugins {
    id 'org.springframework.boot' version '3.1.0'
    id 'io.spring.dependency-management' version '1.1.0'
    id 'java'
}

group = 'com.example'
version = '1.0.0'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.kafka:spring-kafka'
    implementation 'org.postgresql:postgresql:42.6.0'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

### 2.2 Конфигурация `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://hospital-postgres-container:${POSTGRES_PORT}/user_db
spring.datasource.username=admin
spring.datasource.password=admin_password
spring.jpa.hibernate.ddl-auto=update
spring.kafka.bootstrap-servers=hospital-kafka-container:${KAFKA_PORT}
```

---

### 2.3 Код для `user-service`

#### Модель
```java
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;

    // Getters and Setters
}
```

#### Репозиторий
```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

#### Kafka Producer
```java
@Service
public class UserEventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public UserEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification(String email, String action) {
        String message = String.format("{\"email\":\"%s\", \"action\":\"%s\"}", email, action);
        kafkaTemplate.send("user-notifications", message);
    }
}
```

#### Контроллер
```java
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserEventPublisher eventPublisher;

    public UserController(UserRepository userRepository, UserEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/{id}/dangerous-action")
    public ResponseEntity<?> dangerousAction(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        eventPublisher.sendNotification(user.getEmail(), "DANGEROUS_ACTION");
        return ResponseEntity.ok("Notification sent");
    }
}
```

#### Kafka Producer Config
```java
@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

---

## 3. Реализация `notification-service`

`notification-service` будет принимать сообщения из Kafka и отправлять подтверждающие коды на email через MailDev.

---

### 3.1 Gradle-зависимости
Обновите `build.gradle`:
```groovy
plugins {
    id 'org.springframework.boot' version '3.1.0'
    id 'io.spring.dependency-management' version '1.1.0'
    id 'java'
}

group = 'com.example'
version = '1.0.0'
sourceCompatibility = '17'

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.kafka:spring-kafka'
    implementation 'org.springframework.boot:spring-boot-starter-mail'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

---

### 3.2 Конфигурация `application.properties`
```properties
spring.kafka.bootstrap-servers=hospital-kafka-container:${KAFKA_PORT}
spring.mail.host=hospital-maildev-container
spring.mail.port=${MAILDEV_SMTP_PORT}
spring.mail.username=
spring.mail.password=
spring.mail.protocol=smtp
```

---

### 3.3 Код для `notification-service`

#### Kafka Consumer
```java
@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @KafkaListener(topics = "user-notifications", groupId = "notification-service")
    public void handleNotification(String message) {
        try {
            JSONObject json = new JSONObject(message);
            String email = json.getString("email");
            String action = json.getString("action");

            sendEmail(email, action);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String email, String action) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Action Confirmation");
        message.setText("Your action '" + action + "' requires confirmation.");
        mailSender.send(message);
    }
}
```

#### Kafka Consumer Config
```java
@Configuration
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

---

### 4. Тестирование

1. Запустите `docker-compose`:
   ```bash
   docker-compose up -d
   ```

2. Запустите оба сервиса (`user-service` и `notification-service`).

3. Отправьте POST-запрос:
   ```bash
   curl -X POST http://localhost:8080/users/1/dangerous-action
   ```

4. Проверьте MailDev Web UI по адресу [http://localhost:1080](http://localhost:1080) для проверки email.

---

Если будут вопросы или нужно доработать что-то — дайте знать!