# 🎓 Student Management System

A comprehensive **Spring Boot** application for managing student records with role-based security, advanced analytics dashboard, bulk import/export, and modern emoji-enhanced UI. Built with enterprise-grade architecture and production-ready features.

---

## 📋 Table of Contents

- [Quick Start](#-quick-start-with-docker)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Installation](#-installation--setup)
- [User Guide](#-user-guide)
- [API Endpoints](#-api-endpoints)
- [Demo Scenarios](#-demo-scenarios)
- [Troubleshooting](#-troubleshooting)

---

## 🚀 Quick Start with Docker

```bash
# Clone and navigate to the project
cd demo-spring-boot

# Start the application
docker-compose up -d

# Access at http://localhost:8080
```

**Login Credentials:**

| Username | Password | Role  | Permissions        |
| -------- | -------- | ----- | ------------------ |
| `admin`  | `admin`  | ADMIN | Full CRUD access   |
| `user`   | `user`   | USER  | View & Search only |

**Quick Links:**

- 🏠 **Main App**: http://localhost:8080
- 📊 **Dashboard**: http://localhost:8080/dashboard
- 👥 **Students**: http://localhost:8080/students
- 🗄️ **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:studentdb`
  - Username: `sa`
  - Password: _(empty)_

---

## ✨ Features

### Core Features

✅ **Full CRUD Operations** - Create, Read, Update, Delete students  
🔍 **Search & Filter** - Multi-criteria filtering with keyword, major, and GPA range  
📄 **Pagination** - Navigate through large datasets efficiently  
🔐 **Role-Based Security** - ADMIN and USER roles with different permissions  
✨ **Modern UI** - Bootstrap 5 responsive design with emoji icons  
✔️ **Validation** - Server-side validation with proper error handling  
📊 **H2 Database Console** - Built-in database management  
📝 **Logging** - Comprehensive logging with SLF4J

### Advanced Features (Demo-Ready)

#### 1. 📊 Interactive Dashboard

**Route:** `/dashboard` (default landing page after login)

- 📈 **Real-time Statistics Cards**
  - Total Students Count
  - Average GPA (color-coded)
  - Performance Distribution (Excellent/Good/Satisfactory)
- 📉 **Visual Analytics with Chart.js**
  - **GPA Distribution Chart** (Doughnut) - Performance tier breakdown
  - **Students by Major Chart** (Bar) - Major distribution visualization
- 🏆 **Top 5 Performers**
  - Medal emojis (🥇🥈🥉) for top 3
  - Real-time ranking with GPA scores

#### 2. 📥📤 CSV Import/Export (ADMIN Only)

**Routes:** `/students/import` (POST), `/students/export` (GET)

**CSV Import Features:**

- 📁 Upload CSV file with student data
- ✅ Automatic validation (name length, email format, GPA range)
- 🔍 Duplicate detection (emails must be unique)
- 📝 Detailed error reporting (shows which rows failed and why)
- ✨ Success message with count of imported students

**CSV Export Features:**

- 💾 Download all students as CSV
- 📅 Filename includes current date
- 📋 Standard format: ID, Name, Email, Major, GPA

**Sample CSV Format:**

```csv
Name,Email,Major,GPA
Alice Cooper,alice.cooper@example.com,Biology,3.8
Bob Dylan,bob.dylan@example.com,Music,3.6
```

#### 3. 🔍 Advanced Multi-Filter System

- 🔎 **Text Search** - Search by name or email (case-insensitive)
- 🎓 **Major Filter** - Dropdown populated with all existing majors
- 📊 **GPA Range** - Min/Max GPA filters (0.0 - 4.0)
- 🔀 **Sorting** - Sort by ID, Name, Email, GPA, or Major
- 🎯 **Combined Filters** - All filters work together seamlessly
- 🔄 **Clear Filters** - Quick reset to default view

#### 4. 🎨 Emoji-Enhanced UI

- ✨ Replaced all Bootstrap Icons with native emojis
- 🚀 Faster page loads (no CSS icon library needed)
- 🎯 Better accessibility and universal compatibility

**Emoji Map:** 🎓 (Student Management) | 👥 (Students) | ➕ (Add) | 📥 (Import) | 📤 (Export) | 🔍 (Search) | ✏️ (Edit) | 👁️ (View) | 🗑️ (Delete) | 📊 (Dashboard) | 🥇🥈🥉 (Top Performers) | ✅ (Success) | ❌ (Error)

---

## 🛠 Tech Stack

| Technology         | Version | Purpose                        |
| ------------------ | ------- | ------------------------------ |
| Spring Boot        | 3.5.7   | Application Framework          |
| Spring Data JPA    | Latest  | Database Access                |
| Spring Security    | Latest  | Authentication & Authorization |
| Thymeleaf          | Latest  | Template Engine                |
| Chart.js           | 4.4.0   | Data Visualization             |
| Bootstrap          | 5.3.0   | UI Framework                   |
| OpenCSV            | 5.9     | CSV Import/Export              |
| H2 Database        | Latest  | In-Memory Database             |
| Lombok             | Latest  | Reduce Boilerplate Code        |
| Jakarta Validation | Latest  | Input Validation               |
| Gradle             | 9.1.0   | Build Tool                     |
| Java               | 25      | Programming Language           |
| Docker             | 20.10+  | Containerization               |
| Docker Compose     | 2.0+    | Container Orchestration        |

---

## 📁 Project Structure

```
demo-spring-boot/
├── src/
│   ├── main/
│   │   ├── java/octguy/demospringboot/
│   │   │   ├── DemoSpringBootApplication.java    # Main application class
│   │   │   ├── config/
│   │   │   │   ├── DataLoader.java               # Loads initial data
│   │   │   │   └── SecurityConfig.java           # Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java           # Home & login controller
│   │   │   │   ├── DashboardController.java      # Dashboard analytics
│   │   │   │   └── StudentController.java        # Student CRUD + CSV
│   │   │   ├── dto/
│   │   │   │   └── DashboardStats.java           # Dashboard statistics DTO
│   │   │   ├── exception/
│   │   │   │   ├── DuplicateEmailException.java  # Custom exception
│   │   │   │   └── StudentNotFoundException.java # Custom exception
│   │   │   ├── model/
│   │   │   │   ├── Student.java                  # Student entity
│   │   │   │   └── User.java                     # User entity
│   │   │   ├── repository/
│   │   │   │   ├── StudentRepository.java        # Student data access
│   │   │   │   └── UserRepository.java           # User data access
│   │   │   └── service/
│   │   │       ├── CustomUserDetailsService.java # User authentication
│   │   │       └── StudentService.java           # Student business logic
│   │   └── resources/
│   │       ├── application.properties            # Application configuration
│   │       └── templates/                        # Thymeleaf templates
│   │           ├── login.html                    # Login page
│   │           ├── dashboard.html                # Analytics dashboard
│   │           └── students/
│   │               ├── form.html                 # Add/Edit student form
│   │               ├── list.html                 # Student list with filters
│   │               └── view.html                 # View student details
│   └── test/
│       └── java/octguy/demospringboot/
│           └── DemoSpringBootApplicationTests.java
├── build.gradle                                   # Gradle build configuration
├── Dockerfile                                     # Docker image definition
├── docker-compose.yml                             # Docker Compose configuration
├── .dockerignore                                  # Docker build exclusions
├── sample_students.csv                            # Sample CSV for import testing
└── README.md                                      # This file
```

---

## 🐳 Installation & Setup

### Option 1: Docker Deployment (Recommended)

**Prerequisites:**

- Docker Engine 20.10+
- Docker Compose 2.0+

**Quick Start:**

```bash
# Build and run
docker-compose up -d

# View logs
docker-compose logs -f student-management-app

# Stop
docker-compose down
```

**Docker Commands Reference:**

```bash
# Rebuild and restart
docker-compose up -d --build

# View container status
docker-compose ps

# Access container shell
docker-compose exec student-management-app sh

# Remove all containers and volumes
docker-compose down --volumes
```

**Docker Features:**

✅ Multi-stage build - Optimized image size (~150MB)  
✅ Non-root user - Enhanced security  
✅ Alpine Linux - Minimal footprint  
✅ Environment variables - Easy configuration  
✅ JVM container optimizations

### Option 2: Local Development

**Prerequisites:**

- Java 25
- Gradle 9.1.0 (or use the included wrapper)

**Installation Steps:**

```bash
# 1. Navigate to project directory
cd demo-spring-boot

# 2. Build the project
./gradlew clean build

# 3. Run the application
./gradlew bootRun

# 4. Access at http://localhost:8080
```

---

## 📖 User Guide

### Student Model

| Field   | Type   | Constraints                   | Description             |
| ------- | ------ | ----------------------------- | ----------------------- |
| `id`    | Long   | Auto-generated                | Primary key             |
| `name`  | String | 2-100 chars, Required         | Student's full name     |
| `email` | String | Valid email, Unique, Required | Student's email address |
| `major` | String | Required                      | Student's major field   |
| `gpa`   | Double | 0.0-4.0, Required             | Grade Point Average     |

### User Roles & Permissions

| Feature         | ADMIN | USER |
| --------------- | ----- | ---- |
| View Dashboard  | ✅    | ✅   |
| View Students   | ✅    | ✅   |
| Search Students | ✅    | ✅   |
| Add Student     | ✅    | ❌   |
| Edit Student    | ✅    | ❌   |
| Delete Student  | ✅    | ❌   |
| Import CSV      | ✅    | ❌   |
| Export CSV      | ✅    | ❌   |

### Application Features

#### Dashboard (`GET /dashboard`)

- View real-time statistics
- Analyze GPA distribution with doughnut chart
- See students by major with bar chart
- Check top 5 performers with medals

#### List Students (`GET /students`)

- Paginated display (5 students per page by default)
- Multi-criteria filtering:
  - Keyword search (name/email)
  - Major dropdown filter
  - GPA range (min/max)
  - Sorting options
- Color-coded GPA badges:
  - 🟢 Green: GPA ≥ 3.5 (Excellent)
  - 🔵 Blue: 3.0 ≤ GPA < 3.5 (Good)
  - 🟡 Yellow: GPA < 3.0 (Satisfactory)

#### Add Student (`GET /students/new`, `POST /students`)

- **ADMIN only**
- Form with validation
- Duplicate email detection
- POST-REDIRECT-GET pattern

#### Edit Student (`GET /students/edit/{id}`, `POST /students/{id}`)

- **ADMIN only**
- Pre-populated form
- Validation and duplicate check

#### Delete Student (`POST /students/delete/{id}`)

- **ADMIN only**
- Confirmation dialog
- Flash messages

#### CSV Import (`POST /students/import`)

- **ADMIN only**
- Upload CSV file
- Format: `Name,Email,Major,GPA`
- Automatic validation
- Error reporting

#### CSV Export (`GET /students/export`)

- **ADMIN only**
- Download all students
- Filename: `students_YYYY-MM-DD.csv`

---

## 📝 API Endpoints

### Public Endpoints

| Method | Endpoint  | Description   |
| ------ | --------- | ------------- |
| GET    | `/login`  | Login page    |
| POST   | `/login`  | Process login |
| POST   | `/logout` | Logout user   |

### Protected Endpoints (Authenticated)

| Method | Endpoint                | Description                    | Role Required |
| ------ | ----------------------- | ------------------------------ | ------------- |
| GET    | `/`                     | Redirect to dashboard          | Any           |
| GET    | `/home`                 | Redirect to dashboard          | Any           |
| GET    | `/dashboard`            | View analytics dashboard       | Any           |
| GET    | `/students`             | List all students with filters | Any           |
| GET    | `/students?keyword=xxx` | Search students                | Any           |
| GET    | `/students/{id}`        | View student details           | Any           |
| GET    | `/students/new`         | Show create form               | ADMIN         |
| POST   | `/students`             | Create new student             | ADMIN         |
| GET    | `/students/edit/{id}`   | Show edit form                 | ADMIN         |
| POST   | `/students/{id}`        | Update student                 | ADMIN         |
| POST   | `/students/delete/{id}` | Delete student                 | ADMIN         |
| GET    | `/students/export`      | Export students to CSV         | ADMIN         |
| POST   | `/students/import`      | Import students from CSV       | ADMIN         |

### Query Parameters

**List Students (`/students`)**

- `page` (int, default: 0) - Page number
- `size` (int, default: 5) - Items per page
- `sortBy` (string, default: "id") - Sort field (id, name, email, gpa, major)
- `keyword` (string, optional) - Search keyword
- `major` (string, optional) - Filter by major
- `minGpa` (decimal, optional) - Minimum GPA filter
- `maxGpa` (decimal, optional) - Maximum GPA filter

---

## ⚙️ Configuration

### Application Properties

**Database Configuration:**

```properties
spring.datasource.url=jdbc:h2:mem:studentdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

**JPA Configuration:**

```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

**H2 Console:**

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Logging:**

```properties
logging.level.octguy.demospringboot=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Docker Environment Variables

Configure in `docker-compose.yml`:

```yaml
environment:
  - SPRING_PROFILES_ACTIVE=docker
  - SPRING_DATASOURCE_URL=jdbc:h2:mem:studentdb
  - LOGGING_LEVEL_OCTGUY_DEMOSPRINGBOOT=INFO
  - JAVA_OPTS=-Xmx512m -Xms256m
```

### Security Configuration

**URL Access Control:**

- `/h2-console/**` - Permit all
- `/css/**, /js/**, /images/**` - Permit all
- `/login` - Permit all
- `/dashboard` - Authenticated users
- `/students/new, /students/edit/**, /students/delete/**` - ADMIN only
- `/students/import, /students/export` - ADMIN only
- `/students/**` - Authenticated users

---

## 🎭 Demo Scenarios

### Scenario 1: Dashboard Analytics

1. Login as admin (`admin`/`admin`)
2. View dashboard with 8 sample students
3. Show GPA distribution doughnut chart
4. Show students by major bar chart
5. Highlight top 5 performers with medals

### Scenario 2: Bulk CSV Import

1. Navigate to Students list
2. Click "Export CSV" to download current data
3. Open exported CSV file
4. Click "Import CSV" button
5. Upload `sample_students.csv` (10 additional students)
6. Watch success message: "Successfully imported 10 students"
7. Return to dashboard to see updated statistics

### Scenario 3: Advanced Filtering

1. Use text search to find "john"
2. Clear and filter by major "Computer Science"
3. Set GPA range: 3.5 - 4.0 to find excellent students
4. Combine filters: "Engineering" + GPA > 3.7
5. Sort results by GPA descending
6. Click "Clear Filters" to reset

### Scenario 4: Full CRUD Operations

1. Create new student (show validation)
2. Try duplicate email (show error handling)
3. Edit student details
4. View student profile
5. Delete student (show confirmation dialog)
6. Verify success messages

---

## 🔧 Troubleshooting

### Common Issues

**Issue: Application won't start**

```bash
# Check if port 8080 is already in use
lsof -ti:8080 | xargs kill -9

# Rebuild the project
./gradlew clean build
```

**Issue: Can't login**

- Verify credentials: `admin`/`admin` or `user`/`user`
- Check DataLoader logs for user creation
- Verify database is initialized

**Issue: Docker container fails**

```bash
# Check logs
docker-compose logs -f

# Rebuild from scratch
docker-compose down --volumes
docker-compose up -d --build
```

**Issue: CSV import fails**

- Verify CSV format: `Name,Email,Major,GPA`
- Check for duplicate emails
- Ensure GPA values are between 0.0-4.0
- Validate email format

**Issue: Charts not displaying**

- Check browser console for JavaScript errors
- Verify Chart.js CDN is accessible
- Ensure students data exists in database

**Issue: Access Denied (403)**

- Verify user role (ADMIN vs USER)
- Check SecurityConfig role mappings
- Ensure CSRF token is included in forms

---

## 📊 Sample Data

### Default Users

| Username | Password | Role       | Permissions |
| -------- | -------- | ---------- | ----------- |
| admin    | admin    | ROLE_ADMIN | Full access |
| user     | user     | ROLE_USER  | Read-only   |

### Pre-loaded Students

**8 sample students** across various majors:

- Computer Science: 2 students
- Mathematics: 2 students
- Engineering: 2 students
- Physics: 1 student
- Business: 1 student

### Additional Test Data

**`sample_students.csv`** - 10 additional students for import testing

---

## 🎯 Key Highlights

1. ✅ **Professional Dashboard** - Production-ready analytics
2. ✅ **Bulk Operations** - CSV import/export for scalability
3. ✅ **Advanced Filtering** - Multi-criteria search system
4. ✅ **Modern UI** - Bootstrap 5 + Emoji aesthetic
5. ✅ **Full CRUD** - Complete application lifecycle
6. ✅ **Security** - Enterprise-grade Spring Security
7. ✅ **Data Visualization** - Chart.js integration
8. ✅ **Docker Ready** - Easy deployment anywhere
9. ✅ **Clean Code** - Lombok, validation, logging
10. ✅ **Well Tested** - Comprehensive validation

---

## 💡 Technical Highlights

### Architecture Patterns

- **MVC Pattern** - Clear separation of concerns
- **Service Layer Pattern** - Business logic encapsulation
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - Dashboard statistics transfer
- **POST-REDIRECT-GET** - Prevent duplicate submissions

### Performance Optimizations

- **Stream API** - Efficient filtering and transformations
- **Pagination** - Handle large datasets
- **In-memory H2** - Fast development/demo performance
- **Connection Pooling** - HikariCP for database efficiency

### Security Best Practices

- **Role-based access control** (RBAC)
- **CSRF protection** enabled
- **Password encoding** with BCrypt
- **Method-level security** with `@PreAuthorize`
- **Secured endpoints** by role

---

**Built with ❤️ using Spring Boot**
