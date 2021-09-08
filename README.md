# VBoard - back-end
Back-end part of my engineering thesis' web application realizing virtual notice board written in Java using Spring Boot framework.

## Setup
This project requires Java 15 and running MySQL server.

### Environment variables
In order to run this application, a few environment variables must be defined:
* `DATABASE_USERNAME` - username used to access the database
* `DATABASE_PASSWORD` - password used to access the database
* `MAIL_HOST` - SMTP server address, used to send emails to app users
* `MAIL_PORT` - SMTP server port
* `MAIL_USERNAME` - mail server username
* `MAIL_PASSWORD` - mail server password

### Creating sample database
Run `create_database.sql` script located in `src/main/resources` to create sample database.

Sample database user credentials:
* username: `springuser`
* password: `j248Y2d3qW6xWYVi4DRUEZun686JvgDA`

### Running application
After creating database and defining environment variables please run:
* on Windows:
```shell script
gradlew.bat bootRun
```
* on Linux/macOS:
```shell script
./gradlew bootRun
```

Back-end server will then be available under `http://localhost:8080`.

API documentation will be available under `http://localhost:8080/swagger-ui/`.

In order to access user data, you have to log-in using `/login` endpoint providing proper credentials.
After successful authentication you'll receive `accessToken` in response.
It should be included with every request in `Authorization` header (formatted as `Bearer ACCESS_TOKEN_HERE`).

## Sample data
In order to fill database with sample data, `data.sql` script located in `src/main/resources` is executed at launch of the application.
Sample data contains: 10 user accounts (6 personal, 4 institutional), sample boards, posts, comments and likes.

### Sample users credentials:
* Personal accounts:
    * `jan.kowalski@example.com`
    * `lidia.lukasik@example.com`
    * `michalina.madej@example.com`
    * `mikolaj.czyz@example.com`
    * `michal.musial@example.com`
    * `adam.zak@example.com`


* Institutional accounts:
    * `szkola_muzyczna@example.com`
    * `jaworzno@example.com`
    * `majowe_osiedle@example.com`
    * `vboard@example.com`


* __Password for all accounts:__ `aB123456`