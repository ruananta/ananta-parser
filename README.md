
# Ananta Parser

Данный web сайт предназначен для загрузки данных с веб сайтов.
Имеет регистрацию и авторизация пользователей. 

Разрабатываю личный кабинет, структуру сохранения задач в бд.

Идея такая:
На просторах интернета много задач по парсингу информации с интернет магазинов и не только. Люди готовы платить деньги за эту информацию и за софт, который будет её доставать. Я создаю личный кабинет, в котором человек сможет как добавить сам ресурсы для парсинга, так и я сам смогу настроить ему парсинг за отдельную плату. В дальнейшем человек платит подписку за доступ к ЛК.

В дальнейшем добавлю мониторинги цен на товары и услуги с последующим уведомлением на email/telegram.


## Technology Stack
- **Spring Boot**
    - Parent: `spring-boot-starter-parent` (version 3.2.5)
- **Spring Framework**
    - `spring-boot-starter-web`
    - `spring-boot-starter`
    - `spring-boot-starter-test` (scope: test)
    - `spring-boot-starter-data-jpa`
    - `spring-boot-devtools`
    - `spring-boot-starter-security`
- **Spring Security**
    - `spring-security-config` (version 6.2.4)
- **Thymeleaf**
    - `spring-boot-starter-thymeleaf`
    - `thymeleaf-extras-springsecurity5` (version 3.1.2.RELEASE)
- **Jsoup**
    - `jsoup` (version 1.17.2)
- **Apache POI**
    - `poi` (version 5.2.5)
    - `poi-ooxml` (version 5.2.5)
- **Log4j**
    - `log4j-api` (version 2.20.0)
    - `log4j-core` (version 2.20.0)
- **Hibernate Validator**
    - `hibernate-validator` (version 8.0.1.Final)
- **Jakarta Validation API**
    - `jakarta.validation-api` (version 3.1.0-M2)
      
## Features
- Управление через браузер
- Удобный интерфейс
- Возможность установки на сервер
- Выгрузка информации в JSON, CSV, XLSX
