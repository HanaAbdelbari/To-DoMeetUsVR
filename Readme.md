# 📝 To-Do MeetUsVR — Task Management REST API

A REST API built with **Java Spring Boot** that handles user authentication and task management using JWT tokens.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming Language |
| Spring Boot 3.2 | Framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Database ORM |
| MySQL | Database |
| JWT (jjwt 0.11.5) | Token-based Auth |
| BCrypt | Password Hashing |
| Lombok | Reduce Boilerplate |

---

## 📁 Project Structure

```
src/main/java/com/marketplace/todomeetusvr/
├── config/
│   ├── SecurityConfig.java          # Security rules & filter chain
│   ├── JwtAuthFilter.java           # JWT validation filter
│   └── CustomUserDetailsService.java # Load user from DB
├── controller/
│   ├── AuthController.java          # /auth/register, /auth/login
│   └── TaskController.java          # /tasks CRUD endpoints
├── dto/
│   ├── RegisterRequest.java         # Register request body
│   ├── LoginRequest.java            # Login request body
│   └── TaskRequest.java             # Task create/update body
├── exception/
│   └── GlobalExceptionHandler.java  # Global error handling
├── model/
│   ├── User.java                    # User entity
│   └── Task.java                    # Task entity
├── repository/
│   ├── UserRepository.java          # User DB queries
│   └── TaskRepository.java          # Task DB queries
├── service/
│   ├── AuthService.java             # Register & login logic
│   ├── TaskService.java             # Task CRUD logic
│   └── JwtService.java              # JWT generate & validate
└── ToDoMeetUsVrApplication.java
```

---

## ⚙️ Setup & Run

### 1. Create MySQL Database
```sql
CREATE DATABASE taskdb;
```

### 2. Configure `application.properties`
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskdb
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=your_super_secret_key_must_be_at_least_32_characters
jwt.expiration=86400000
```

### 3. Run the Application
```bash
mvn spring-boot:run
```
App runs on: `http://localhost:8080`

---

## 🔐 Authentication Endpoints

### Register
```
POST /auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "mypassword",
  "name": "John Doe"
}
```
**Response:** `201 Created`
```json
{
  "message": "User registered successfully"
}
```

---

### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "mypassword"
}
```
**Response:** `200 OK`
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9....."
}
```

---

## ✅ Task Endpoints

> All task endpoints require the Authorization header:
> `Authorization: Bearer YOUR_TOKEN_HERE`

---

### Create Task
```
POST /tasks
Authorization: Bearer YOUR_TOKEN

{
  "title": "My Task",
  "description": "Task description",
  "status": "open"
}
```
**Response:** `201 Created`

---

### Get All Tasks
```
GET /tasks
Authorization: Bearer YOUR_TOKEN
```
**Response:** `200 OK` — returns only tasks belonging to the logged-in user

---

### Update Task
```
PUT /tasks/{id}
Authorization: Bearer YOUR_TOKEN

{
  "title": "My Task",
  "description": "Task description",
  "status": "done"
}
```
**Response:** `200 OK`

---

### Delete Task
```
DELETE /tasks/{id}
Authorization: Bearer YOUR_TOKEN
```
**Response:** `204 No Content`

---

## 🚨 Error Handling

| Scenario | HTTP Status |
|---|---|
| Invalid email or password | `401 Unauthorized` |
| No token provided | `401 Unauthorized` |
| Accessing another user's task | `403 Forbidden` |
| Task not found | `404 Not Found` |
| Email already registered | `409 Conflict` |
| Invalid request body | `400 Bad Request` |

**Error Response Format:**
```json
{
  "error": "Error message here"
}
```

---

## 🔒 Security Design

- Passwords are hashed using **BCrypt** before storing in DB
- Authentication uses **stateless JWT tokens** (no sessions)
- Each token expires after **24 hours**
- Every task is **user-scoped** — users can only access their own tasks
- Requests without a token return **401 Unauthorized** via `authenticationEntryPoint`

---

## 📊 HTTP Status Codes Summary

| Action | Status |
|---|---|
| Register | `201 Created` |
| Login | `200 OK` |
| Create Task | `201 Created` |
| Get Tasks | `200 OK` |
| Update Task | `200 OK` |
| Delete Task | `204 No Content` |
| No token | `401 Unauthorized` |
| Wrong credentials | `401 Unauthorized` |
| Other user's task | `403 Forbidden` |