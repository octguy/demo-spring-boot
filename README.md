# 🎓 Student Management System

A comprehensive Spring Boot application for managing student records with role-based security, full CRUD operations, and modern UI.

## 🚀 Quick Start with Docker

```bash
# Clone and navigate to the project
cd demo-spring-boot

# Start the application
docker-compose up -d

# Access at http://localhost:8080
```

**Login Credentials:**

- Admin: `admin` / `admin` (Full access)
- User: `user` / `user` (Read-only)

## 📋 Features

✅ Full CRUD operations  
✅ Search & pagination  
✅ Role-based security (ADMIN/USER)  
✅ Form validation  
✅ Bootstrap 5 UI  
✅ H2 in-memory database  
✅ Docker support

## 🛠 Tech Stack

- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- Thymeleaf
- Bootstrap 5.3.0
- H2 Database
- Docker & Docker Compose

## 📖 Documentation

See [GUIDE.md](GUIDE.md) for complete documentation including:

- Detailed setup instructions
- Docker deployment guide
- API endpoints
- Testing procedures
- Troubleshooting

## 🐳 Docker Commands

```bash
# Start
docker-compose up -d

# View logs
docker-compose logs -f

# Stop
docker-compose down

# Rebuild
docker-compose up -d --build
```

## 💻 Local Development

```bash
# Build
./gradlew clean build

# Run
./gradlew bootRun
```

## 📊 Endpoints

- **App**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:studentdb`
  - Username: `sa`
  - Password: _(empty)_

## 📝 License

Educational/Demo Project

---

**Happy Coding! 🚀**
