# 🎓 Student Management System

A comprehensive Spring Boot application for managing student records with role-based security, advanced analytics dashboard, bulk import/export, and modern emoji-enhanced UI.

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

## ✨ New Demo Features

🎯 **Interactive Dashboard** - Real-time statistics, GPA distribution charts, top performers  
📊 **Data Visualization** - Chart.js integration with doughnut and bar charts  
📥 **CSV Import/Export** - Bulk student operations with validation  
🔍 **Advanced Filtering** - Multi-criteria search (keyword, major, GPA range)  
🎨 **Emoji UI** - Modern interface with zero icon dependencies

👉 See [DEMO_FEATURES.md](DEMO_FEATURES.md) for detailed demo scenarios!

## 📋 Core Features

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
- Thymeleaf + Chart.js 4.4.0
- Bootstrap 5.3.0
- OpenCSV 5.9
- H2 Database
- Docker & Docker Compose

## 📖 Documentation

- **[GUIDE.md](GUIDE.md)** - Complete setup and deployment guide
- **[DEMO_FEATURES.md](DEMO_FEATURES.md)** - New features showcase and demo scenarios

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
- **Dashboard**: http://localhost:8080/dashboard
- **Students**: http://localhost:8080/students
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:studentdb`
  - Username: `sa`
  - Password: _(empty)_

---

**Happy Coding! 🚀**
