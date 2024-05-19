
# Ananta Parser

Данный софт предназначен для загрузки данных с веб сайтов. Запуск загрузки возможен как в ручном так и в автоматическом режиме по рассписанию.
## Стек
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
- Запись файлов в JSON, CSV, XLSX
- Отправка комманд через telegram bot


## Что нужно сделать

#### Функционал

- Работа как по списку ссылок так и при динамической загрузке данных с сайта.
- Загрузка картинок
- Ограничение по времени
- 
#### Интерфейс
Одна или несколько страниц в веб браузере

#### JSON Parser

Реализовать загрузку и парсинг JSON файлов из WEB

#### WEB Parser

Реализовать загрузку и парсинг WEB страниц

#### Telegram Bot API

Реализовать подключение Telegram бота

#### CSV

Реализовать запись в CSV

#### XLSX

Реализовать запись в XLSX




