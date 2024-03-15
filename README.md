# API "Производственный журнал предприятия"
* Приложение разработано в виде REST API,
на языке Java, с использованием фреймворка Spring Boot. Обмен данными реализован в формате JSON.

* Аутентификация в приложении происходит с помощью JWT. При входе, сервер отправляет долгосрочный токен refresh-token в виде http-only куки с названием "refreshToken" и короткосрочный access-token в виде JSON. При отправлении каждого запроса пользователь  должен внести access-token в headers в виде "Bearer access-token". Как только время действия access-token истекло, пользователь должен продлить сессию, получив новый access-token, который выдается на основе куки refreshToken.

* Пароли пользователей хэшируются с помощью bcrypt.
___
# Реализованные задачи 
Функционал отличается в зависимости от роли пользователя.

Пользователям с ролью "Владелец" доступны:
* авторизация;
* регистрация сотрудников;
* перманентное/временное удаление сотрудников;
* изменение информации о сотрудниках;
* регистрация/удаление товара;
* просмотр статистики производства (с использованием фильтров по сотруднику, товару и периоду);
* ежедневное получение электронного письма, содержащего статистику производства за сутки;
* удаление записей из производственного журнала;

Пользователям с ролью "Работник" доступны:
 * авторизация;
 * занесение записей в производственный журнал;
___
# Технологии
* Java 17
* Spring Boot 3
* Spring Security
* Spring Mail Server
* Spring Data JPA
* Hibernate
* PostgreSQL
* JSON Web Token
* OpenAPI
___



