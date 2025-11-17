# Student Management System - Complete Guide

## 📋 Project Overview

A comprehensive **Student Management System** built with Spring Boot, featuring full CRUD operations, role-based security, search functionality, and pagination. The system provides a clean, modern web interface using Thymeleaf and Bootstrap.

### Key Features

- ✅ **Full CRUD Operations** - Create, Read, Update, Delete students
- 🔍 **Search & Filter** - Search by name or email
- 📄 **Pagination** - Navigate through large datasets
- 🔐 **Role-Based Security** - ADMIN and USER roles with different permissions
- ✨ **Modern UI** - Bootstrap 5 responsive design
- ✔️ **Validation** - Server-side validation with proper error handling
- 📊 **H2 Database Console** - Built-in database management
- 📝 **Logging** - Comprehensive logging with SLF4J

---

## 🛠 Tech Stack

| Technology         | Version | Purpose                        |
| ------------------ | ------- | ------------------------------ |
| Spring Boot        | 3.5.7   | Application Framework          |
| Spring Data JPA    | Latest  | Database Access                |
| Spring Security    | Latest  | Authentication & Authorization |
| Thymeleaf          | Latest  | Template Engine                |
| Bootstrap          | 5.3.0   | UI Framework                   |
| H2 Database        | Latest  | In-Memory Database             |
| Lombok             | Latest  | Reduce Boilerplate Code        |
| Jakarta Validation | Latest  | Input Validation               |
| Gradle             | Latest  | Build Tool                     |
| Java               | 17/25   | Programming Language           |
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
│   │   │   │   └── StudentController.java        # Student CRUD controller
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
│   │           ├── layout.html                   # Base layout (optional)
│   │           ├── login.html                    # Login page
│   │           └── students/
│   │               ├── form.html                 # Add/Edit student form
│   │               ├── list.html                 # Student list with search
│   │               └── view.html                 # View student details
│   └── test/
│       └── java/octguy/demospringboot/
│           └── DemoSpringBootApplicationTests.java
├── build.gradle                                   # Gradle build configuration
├── Dockerfile                                     # Docker image definition
├── docker-compose.yml                             # Docker Compose configuration
├── .dockerignore                                  # Docker build exclusions
└── GUIDE.md                                       # This file
```

---

## 🚀 Getting Started

### Prerequisites

**Option 1: Local Development**

- Java 17 or higher (Java 25 recommended)
- Gradle (or use the included Gradle wrapper)
- Any modern web browser

**Option 2: Docker Deployment (Recommended)**

- Docker Engine 20.10+
- Docker Compose 2.0+

---

## 🐳 Docker Deployment (Recommended)

### Quick Start with Docker Compose

1. **Navigate to the project directory**

   ```bash
   cd /Users/octguy/Personalize/tech/java/project/demo-spring-boot
   ```

2. **Build and run with Docker Compose**

   ```bash
   docker-compose up -d
   ```

3. **Access the application**

   - Main Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:studentdb`
     - Username: `sa`
     - Password: (leave empty)

4. **View logs**

   ```bash
   docker-compose logs -f student-management-app
   ```

5. **Stop the application**
   ```bash
   docker-compose down
   ```

### Docker Commands Reference

**Build only (without starting)**

```bash
docker-compose build
```

**Rebuild and restart**

```bash
docker-compose up -d --build
```

**View container status**

```bash
docker-compose ps
```

**Access container shell**

```bash
docker-compose exec student-management-app sh
```

**Remove all containers and networks**

```bash
docker-compose down --volumes
```

### Using Dockerfile Directly

**Build the Docker image**

```bash
docker build -t student-management-system:latest .
```

**Run the container**

```bash
docker run -d \
  --name student-management \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=docker \
  student-management-system:latest
```

**Stop and remove container**

```bash
docker stop student-management
docker rm student-management
```

### Docker Features

✅ **Multi-stage build** - Optimized image size  
✅ **Non-root user** - Enhanced security  
✅ **Health checks** - Container monitoring  
✅ **Environment variables** - Easy configuration  
✅ **Alpine Linux** - Minimal footprint (~150MB)

---

## 💻 Local Development Setup

### Installation & Setup

1. **Clone or navigate to the project directory**

   ```bash
   cd /Users/octguy/Personalize/tech/java/project/demo-spring-boot
   ```

2. **Build the project**

   ```bash
   ./gradlew clean build
   ```

3. **Run the application**

   ```bash
   ./gradlew bootRun
   ```

4. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:studentdb`
     - Username: `sa`
     - Password: (leave empty)

---

## 🔐 User Credentials

The system comes with two pre-configured users:

| Username | Password | Role  | Permissions        |
| -------- | -------- | ----- | ------------------ |
| `admin`  | `admin`  | ADMIN | Full CRUD access   |
| `user`   | `user`   | USER  | View & Search only |

---

## 📊 Student Model

### Student Entity

| Field   | Type   | Constraints                   | Description             |
| ------- | ------ | ----------------------------- | ----------------------- |
| `id`    | Long   | Auto-generated                | Primary key             |
| `name`  | String | 2-100 chars, Required         | Student's full name     |
| `email` | String | Valid email, Unique, Required | Student's email address |
| `major` | String | Required                      | Student's major field   |
| `gpa`   | Double | 0.0-4.0, Required             | Grade Point Average     |

### Validation Rules

- **Name**: Must be between 2 and 100 characters
- **Email**: Must be a valid email format and unique in the system
- **Major**: Required field
- **GPA**: Must be between 0.0 and 4.0 (inclusive)

---

## 🎯 Features & Functionality

### 1. Authentication & Authorization

**Login**

- Accessible at `/login`
- Form-based authentication
- Remember session across browser refreshes

**Role-Based Access Control**

| Feature         | ADMIN | USER |
| --------------- | ----- | ---- |
| View Students   | ✅    | ✅   |
| Search Students | ✅    | ✅   |
| Add Student     | ✅    | ❌   |
| Edit Student    | ✅    | ❌   |
| Delete Student  | ✅    | ❌   |

### 2. Student Management

#### List Students (`GET /students`)

- Paginated display (5 students per page by default)
- Sortable by: ID, Name, Email, GPA
- Search functionality (name or email)
- Color-coded GPA badges:
  - 🟢 Green: GPA ≥ 3.5 (Excellent)
  - 🔵 Blue: 3.0 ≤ GPA < 3.5 (Good)
  - 🟡 Yellow: GPA < 3.0 (Satisfactory)

#### View Student Details (`GET /students/{id}`)

- Display all student information
- Quick actions: Edit, Delete (ADMIN only)

#### Add Student (`GET /students/new`, `POST /students`)

- **ADMIN only**
- Form with client-side and server-side validation
- Duplicate email detection
- POST-REDIRECT-GET pattern

#### Edit Student (`GET /students/edit/{id}`, `POST /students/{id}`)

- **ADMIN only**
- Pre-populated form with existing data
- Validation and duplicate email check

#### Delete Student (`POST /students/delete/{id}`)

- **ADMIN only**
- Confirmation dialog before deletion
- Success/error flash messages

### 3. Search & Pagination

**Search**

- Search by name or email (case-insensitive)
- Real-time filtering
- Clear search button

**Pagination**

- Configurable page size
- Page navigation controls
- Total items count display

### 4. User Interface

**Design Principles**

- Responsive Bootstrap 5 design
- Mobile-friendly layout
- Icon-based navigation (Bootstrap Icons)
- Color-coded feedback (alerts, badges)
- Professional login page with gradient background

**Components**

- Navigation bar with user info and logout
- Flash messages for success/error notifications
- Data tables with hover effects
- Form validation with inline error messages
- Confirmation dialogs for destructive actions

---

## 🔧 Configuration

### Docker Configuration

**Docker Compose Environment Variables**

The application can be configured via environment variables in `docker-compose.yml`:

```yaml
environment:
  # Spring Profile
  - SPRING_PROFILES_ACTIVE=docker

  # Database
  - SPRING_DATASOURCE_URL=jdbc:h2:mem:studentdb
  - SPRING_DATASOURCE_USERNAME=sa
  - SPRING_DATASOURCE_PASSWORD=

  # Logging Levels
  - LOGGING_LEVEL_OCTGUY_DEMOSPRINGBOOT=INFO
  - LOGGING_LEVEL_ORG_SPRINGFRAMEWORK_SECURITY=WARN

  # JVM Options
  - JAVA_OPTS=-Xmx512m -Xms256m
```

**Dockerfile Features**

- Multi-stage build (Build stage + Runtime stage)
- Alpine-based images for minimal size
- Non-root user for security
- Health checks for monitoring
- Optimized JVM settings for containers

**Resource Limits (Optional)**

Add to `docker-compose.yml` under service configuration:

```yaml
deploy:
  resources:
    limits:
      cpus: "1.0"
      memory: 1G
    reservations:
      cpus: "0.5"
      memory: 512M
```

### Application Properties

**Database Configuration**

```properties
spring.datasource.url=jdbc:h2:mem:studentdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

**JPA Configuration**

```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

**H2 Console**

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Thymeleaf Configuration**

```properties
spring.thymeleaf.cache=false
```

**Logging**

```properties
logging.level.octguy.demospringboot=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Security Configuration

**URL Access Control**

- `/h2-console/**` - Permit all (for development)
- `/css/**, /js/**, /images/**` - Permit all (static resources)
- `/login` - Permit all
- `/students/new, /students/edit/**, /students/delete/**` - ADMIN only
- `/students/**` - ADMIN and USER
- All other requests - Authenticated users only

**CSRF Protection**

- Enabled for all endpoints except H2 Console

---

## 📝 API Endpoints

### Public Endpoints

| Method | Endpoint  | Description   |
| ------ | --------- | ------------- |
| GET    | `/login`  | Login page    |
| POST   | `/login`  | Process login |
| POST   | `/logout` | Logout user   |

### Protected Endpoints (Authenticated)

| Method | Endpoint                | Description                       | Role Required |
| ------ | ----------------------- | --------------------------------- | ------------- |
| GET    | `/`                     | Redirect to students list         | Any           |
| GET    | `/home`                 | Redirect to students list         | Any           |
| GET    | `/students`             | List all students with pagination | Any           |
| GET    | `/students?keyword=xxx` | Search students                   | Any           |
| GET    | `/students/{id}`        | View student details              | Any           |
| GET    | `/students/new`         | Show create form                  | ADMIN         |
| POST   | `/students`             | Create new student                | ADMIN         |
| GET    | `/students/edit/{id}`   | Show edit form                    | ADMIN         |
| POST   | `/students/{id}`        | Update student                    | ADMIN         |
| POST   | `/students/delete/{id}` | Delete student                    | ADMIN         |

### Query Parameters

**List Students (`/students`)**

- `page` (int, default: 0) - Page number
- `size` (int, default: 5) - Items per page
- `sortBy` (string, default: "id") - Sort field (id, name, email, gpa)
- `keyword` (string, optional) - Search keyword

---

## 🧪 Testing the Application

### Manual Testing Steps

1. **Login as ADMIN**

   - Navigate to http://localhost:8080
   - Login with `admin`/`admin`
   - Verify you can see "Add Student" button

2. **Create a Student**

   - Click "Add New Student"
   - Fill in the form with valid data
   - Submit and verify redirect to details page

3. **Test Validation**

   - Try creating a student with invalid data:
     - Name too short (< 2 chars)
     - Invalid email format
     - GPA out of range (< 0 or > 4.0)
   - Verify error messages appear

4. **Test Duplicate Email**

   - Try creating a student with an existing email
   - Verify duplicate email error message

5. **Search Functionality**

   - Search by partial name
   - Search by partial email
   - Verify results update correctly

6. **Pagination**

   - Navigate through pages
   - Change page size
   - Change sort order

7. **Edit Student**

   - Click edit on any student
   - Modify fields
   - Submit and verify changes

8. **Delete Student**

   - Click delete on any student
   - Confirm deletion
   - Verify student is removed

9. **Login as USER**

   - Logout and login with `user`/`user`
   - Verify no "Add Student" button
   - Verify edit/delete buttons are hidden
   - Verify you can still view and search

10. **Access Control**
    - As USER, try to access `/students/new` directly
    - Verify access is denied

---

## 🏗️ Development Notes

### Design Patterns Used

1. **MVC (Model-View-Controller)**

   - Model: `Student`, `User` entities
   - View: Thymeleaf templates
   - Controller: `StudentController`, `HomeController`

2. **Repository Pattern**

   - Spring Data JPA repositories for data access

3. **Service Layer Pattern**

   - Business logic in service classes

4. **POST-REDIRECT-GET (PRG)**

   - After POST operations, redirect to prevent duplicate submissions

5. **Dependency Injection**
   - Constructor injection with Lombok's `@RequiredArgsConstructor`

### Logging Strategy

- **DEBUG**: Detailed operation logs in service and controller layers
- **INFO**: Important business events (student created, updated, deleted)
- **ERROR**: Exception handling and error cases

### Security Considerations

⚠️ **For Development Only**

- Using `NoOpPasswordEncoder` (passwords stored in plain text)
- H2 Console is enabled

🔒 **For Production**

- Use `BCryptPasswordEncoder` for password hashing
- Disable H2 Console
- Use a production database (PostgreSQL, MySQL)
- Enable HTTPS
- Configure CSRF properly
- Add rate limiting
- Implement proper session management

---

## 🐛 Troubleshooting

### Docker Issues

**Issue: Port 8080 already in use**

```bash
# Find and stop the process using port 8080
lsof -ti:8080 | xargs kill -9

# Or change the port in docker-compose.yml
ports:
  - "8081:8080"  # Maps host port 8081 to container port 8080
```

**Issue: Container fails to start**

```bash
# Check container logs
docker-compose logs student-management-app

# Check container status
docker-compose ps

# Restart with fresh build
docker-compose down
docker-compose up -d --build
```

**Issue: Out of memory or disk space**

```bash
# Remove unused Docker resources
docker system prune -a

# Check Docker disk usage
docker system df
```

**Issue: Application not responding**

```bash
# Check health status
docker-compose ps

# Check logs for errors
docker-compose logs --tail=100 student-management-app

# Restart the container
docker-compose restart student-management-app
```

**Issue: Changes not reflected after rebuild**

```bash
# Force rebuild without cache
docker-compose build --no-cache
docker-compose up -d
```

### Application Issues

**Issue: Application won't start**

- Check if port 8080 is already in use
- Verify Java version (Java 17+)
- Run `./gradlew clean build` to rebuild

**Issue: Can't login**

- Verify credentials (admin/admin or user/user)
- Check if database is initialized (look for DataLoader logs)

**Issue: 404 errors**

- Verify the application is running
- Check the URL path is correct
- Look at server logs for routing issues

**Issue: Validation not working**

- Check `@Valid` annotation is present in controller
- Verify `BindingResult` parameter follows the model parameter
- Check validation annotations on model class

**Issue: Access Denied**

- Verify you're logged in
- Check if your user has the required role
- Review SecurityConfig role mappings

---

## 📚 Additional Resources

### Spring Boot Documentation

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf](https://www.thymeleaf.org/documentation.html)

### Bootstrap Documentation

- [Bootstrap 5 Docs](https://getbootstrap.com/docs/5.3/)
- [Bootstrap Icons](https://icons.getbootstrap.com/)

---

## 🎓 Learning Outcomes

By exploring this project, you will learn:

1. **Spring Boot Fundamentals**

   - Application structure and configuration
   - Dependency injection and component scanning
   - Auto-configuration

2. **Spring Data JPA**

   - Entity mapping and relationships
   - Repository pattern and custom queries
   - Pagination and sorting

3. **Spring Security**

   - Authentication and authorization
   - Role-based access control
   - Form login configuration

4. **Thymeleaf**

   - Template engine basics
   - Form binding and validation
   - Security integration

5. **Web Development Best Practices**

   - MVC pattern
   - POST-REDIRECT-GET pattern
   - Input validation
   - Error handling
   - User experience design

6. **Docker & Containerization**
   - Multi-stage Docker builds
   - Docker Compose orchestration
   - Container health checks
   - Environment-based configuration

---

## 🔄 Future Enhancements

Potential improvements for the system:

- [x] Docker containerization
- [x] Docker Compose configuration
- [ ] Kubernetes deployment manifests
- [ ] PostgreSQL/MySQL database support
- [ ] Redis caching layer
- [ ] Export students to CSV/Excel
- [ ] Import students from file
- [ ] Student profile pictures
- [ ] Advanced filtering (by major, GPA range)
- [ ] Student course enrollment
- [ ] Grade management
- [ ] Email notifications
- [ ] REST API endpoints
- [ ] User registration
- [ ] Password reset functionality
- [ ] Audit logging
- [ ] Dark mode theme
- [ ] Multi-language support

---

## 📄 License

This is a demo project for educational purposes.

---

## 👥 Author

Created as a comprehensive Spring Boot demonstration project.

---

## 🙏 Acknowledgments

- Spring Framework Team
- Bootstrap Team
- Thymeleaf Community
- All open-source contributors

---

**Happy Coding! 🚀**
