# 📚 LibraryAPI

A RESTful backend built with **Ktor** and **Kotlin**, providing authentication, role-based authorization, and CRUD
operations for a digital library system. Users can register, log in, browse books, and borrow or return them. Librarians
and admins manage users, books, and loans.

---

## 🚀 Features

* JWT-based authentication
* Role-based access control (`MEMBER`, `LIBRARIAN`, `ADMIN`)
* CRUD operations for books and loans
* User management (profile, roles, permissions)
* Exposed ORM + Flyway database migrations
* Centralized error handling and structured logging
* Unit and integration tests

---

## 🧩 Roles Overview

| Role          | Description                                            |
|---------------|--------------------------------------------------------|
| **MEMBER**    | Default user role. Can view, borrow, and return books. |
| **LIBRARIAN** | Can manage books and loans. Limited user access.       |
| **ADMIN**     | Full access to all data, including user management.    |

---

## 📖 Core Endpoints

### **Auth**

| Method | Endpoint                | Description                  |
|--------|-------------------------|------------------------------|
| `POST` | `/api/v1/auth/register` | Register new user            |
| `POST` | `/api/v1/auth/login`    | Authenticate and receive JWT |
| `GET`  | `/api/v1/auth/me`       | Get authenticated user info  |

### **Books**

| Method   | Endpoint             | Description         | Roles            |
|----------|----------------------|---------------------|------------------|
| `GET`    | `/api/v1/books`      | List all books      | Public           |
| `GET`    | `/api/v1/books/{id}` | Get a specific book | Public           |
| `POST`   | `/api/v1/books`      | Add new book        | Librarian, Admin |
| `PUT`    | `/api/v1/books/{id}` | Update book         | Librarian, Admin |
| `DELETE` | `/api/v1/books/{id}` | Delete book         | Librarian, Admin |

### **Loans**

| Method | Endpoint                    | Description     | Roles            |
|--------|-----------------------------|-----------------|------------------|
| `POST` | `/api/v1/loans`             | Borrow a book   | Member           |
| `GET`  | `/api/v1/loans`             | View user loans | Member           |
| `PUT`  | `/api/v1/loans/{id}/return` | Return a book   | Member           |
| `GET`  | `/api/v1/loans/all`         | View all loans  | Librarian, Admin |

### **Users**

| Method | Endpoint                  | Description      | Roles            |
|--------|---------------------------|------------------|------------------|
| `GET`  | `/api/v1/users`           | List all users   | Librarian, Admin |
| `GET`  | `/api/v1/users/me`        | Get own profile  | Member           |
| `PUT`  | `/api/v1/users/{id}/role` | Update user role | Admin            |

---

## ⚙️ Setup & Installation

### **Prerequisites**

* JDK 21+
* Gradle 8+
* MySQL

### **Clone and Build**

```bash
git clone https://github.com/devkoenv/libraryapi.git
cd libraryapi
./gradlew build
```

### **Run Locally**

```bash
./gradlew run
```

The server runs by default at `http://localhost:8080`.

### **Configuration**

Environment variables expected:

```bash
DB_TYPE=postgres
DB_HOST=localhost
DB_PORT=5432
DB_NAME=librarydb
DB_USER=library_user
DB_PASSWORD=secret
JWT_SECRET=your_jwt_secret
```

You can also set them in `application.yml`.

---

## 🧱 Tech Stack

| Component  | Tool               |
|------------|--------------------|
| Framework  | Ktor               |
| Language   | Kotlin             |
| ORM        | Exposed            |
| Database   | MySQL              |
| Migrations | Flyway             |
| DI         | Koin               |
| Auth       | JWT                |
| Logging    | Logback            |
| Testing    | JUnit5 + Ktor Test |

---

## 🧪 Testing

Run unit and integration tests:

```bash
./gradlew test
```

---

## 📄 License

MIT License — feel free to use this project for learning or demos.
