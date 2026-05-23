# InternTrack Pro

Java Spring Boot internship tracker website with secure login credentials, dark dashboard UI, CRUD tracker, filters, and analytics.

## Demo Login

Email: `demo@interntrack.com`

Password: `demo123`

You can also register a new account.

## Requirements

- Java 17 or above
- Maven
- VS Code / IntelliJ / Eclipse

## How to Run

Open terminal inside the project folder and run:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

## Main Pages

- `/` Home page
- `/register` Create account
- `/login` Login
- `/dashboard` Dashboard
- `/internships` Internship tracker
- `/internships/new` Add internship
- `/analytics` Analytics

## Database

This project uses H2 database by default so it runs easily.

H2 Console:

```text
http://localhost:8080/h2-console
```

JDBC URL:

```text
jdbc:h2:file:./data/interntrackdb
```

Username:

```text
sa
```

Password is empty.

## MySQL Switch

Open `src/main/resources/application.properties`.

Comment the H2 settings and uncomment the MySQL settings.

Create database:

```sql
CREATE DATABASE interntrack_db;
```

Then update:

```properties
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```
